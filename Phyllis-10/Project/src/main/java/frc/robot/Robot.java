/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.buttons.*;
import frc.robot.event.EventHandler;
import frc.robot.event.customevents.PrintEvent;
import frc.robot.motors.Arm;
import frc.robot.motors.Elevator;
import frc.robot.motors.ExtendableMotor;
import frc.robot.motors.Iotake;
import frc.robot.motors.Lift;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;

import java.util.Hashtable;
public class Robot extends TimedRobot {
  
  
  //CLASS VARIABLES

  public static enum driveType {twoJoyStick,oneJoystick,climbing};
  public static EventHandler eHandler=new EventHandler();
  private final double voltageCutoff=8.5;
  private DifferentialDrive robot;
  private Joystick leftStick;
  private Joystick rightStick;
  private Joystick tractorPanel;
  private Joystick xStick;
  private JSBAdapter jsbAdapter;
  private TSBAdapter tsbAdapter;
  private Solenoid solenoid;
  private long solenoidActivationTime;
  private Compressor compressor;
  private CANSparkMax leftFrontCAN=new CANSparkMax(10,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax leftBackCAN=new CANSparkMax(11,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightFrontCAN=new CANSparkMax(13,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightBackCAN=new CANSparkMax(12,CANSparkMaxLowLevel.MotorType.kBrushless);
  private Elevator elevator=new Elevator();
  private CANSparkMax elevatorF=new CANSparkMax(20, CANSparkMaxLowLevel.MotorType.kBrushless);
  private Iotake iotake=new Iotake();//Iotake is intake/outtake system
  private Arm arm=new Arm();
  private Lift rearLift=new Lift();
  private AnalogGyro gyro=new AnalogGyro(0);
  private Boolean climbing;
  private Hashtable<String,Double> tuningValues;
  private double Y;
  private final double rampup=.1;
  private static Robot instance;
  //private ExtendableMotor extendableintakeRight = new ExtendableMotor(intakeRight, 0.05, 1);
  

  //ROBOT ESSENTIAL METHODS

  @Override
  public void robotInit() { 
    instance=this;
    UsbCamera front=CameraServer.getInstance().startAutomaticCapture(); 
    /*if (front.isConnected()==false){
      front.close();
    }*/
    //UsbCamera back=CameraServer.getInstance().startAutomaticCapture(1);
    leftBackCAN.follow(leftFrontCAN);
    rightBackCAN.follow(rightFrontCAN);
    elevatorF.follow(elevator);
     eHandler.start();
    //rightFrontCAN.setOpenLoopRampRate(10);
    //leftFrontCAN.setOpenLoopRampRate(10);
    robot = new DifferentialDrive(leftFrontCAN,rightFrontCAN);
    elevator.setIdleMode(IdleMode.kBrake);
    elevatorF.setIdleMode(IdleMode.kBrake);
    rearLift.setIdleMode(IdleMode.kBrake);
    arm.setIdleMode(IdleMode.kBrake);
    leftStick = new Joystick(0);
    rightStick = new Joystick(1);
    xStick = leftStick;
    tractorPanel = new Joystick(2);
    jsbAdapter=new JSBAdapter(rightStick, this);
    tsbAdapter=new TSBAdapter(tractorPanel, this);
    tuningValues=new Hashtable<>();
    tuningValues.put("eTop",44.83285);
    tuningValues.put("eBot",0.0);
    tuningValues.put("eMid",22.6903);
    tuningValues.put("eCar",27.47599);
    tuningValues.put("aCar",-25.30935);
    tuningValues.put("aHat",-6.9285);
    tuningValues.put("aBal",-27.85693);
    tuningValues.put("aSit",.1);
    tuningValues.put("aDec",.1);
    tuningValues.put("lTop",0.0);
    tuningValues.put("lBot",-.5);

    tuningValues.put("aPIDOR",0.0);
    tuningValues.put("lPIDOR",.0);
    tuningValues.put("ePIDORUp",.0);
    tuningValues.put("ePIDORDow",.0);


    tuningValues.put("eSpdUp",.3);
    tuningValues.put("eSpdDow",.2);
    tuningValues.put("lSpdUp",.8);
    tuningValues.put("lSpdDow",.8);
    tuningValues.put("eSpdJ",.6);
    tuningValues.put("aSpd",.2);
    tuningValues.put("aSpdJ",.6);

    tuningValues.put("eCurUp",52.0);
    tuningValues.put("eCurDow", 8.0);
    tuningValues.put("eCurPID", 60.0);
    tuningValues.put("eCurJoy", 60.0);
    tuningValues.put("lCur", 80.0);
    tuningValues.put("aCur", 120.0);
    climbing=false;
    solenoid=new Solenoid(0);
    compressor=new Compressor(0); //DOUBLE CHECK IDS
    solenoidActivationTime=0;
  }
  @Override
  public void teleopInit() {
    if (!(tsbAdapter==null)){
      tsbAdapter.setMode(TSBAdapter.Mode.RobotResponse);
    }
  }
  @Override
  public void teleopPeriodic() {
    periodic();
  }
  @Override
  public void autonomousInit() {
    if (!(tsbAdapter==null)){
      tsbAdapter.setMode(TSBAdapter.Mode.RobotResponse);
    }
  }
  @Override
  public void autonomousPeriodic() {
    super.autonomousPeriodic();
    periodic();
  }
  private void periodic(){
    //driving arcade
    if (!climbing){
      robot.arcadeDrive(-rightStick.getY()*.75, xStick.getX()*.75); 
    } else {
      robot.arcadeDrive(-leftStick.getY()*.75, 0);
    }
    //update button inputs
    if (!(jsbAdapter.equals(null)&&tsbAdapter.equals(null))){
      jsbAdapter.update();
      tsbAdapter.update();
    }
    if (rearLift.getMotorTemperature()>100){
      eHandler.triggerEvent(new PrintEvent("WARNING: Rear lift high temperature of "+rearLift.getMotorTemperature()));
    } else if (rearLift.getMotorTemperature()>200){
      rearLift.off();
      eHandler.triggerEvent(new PrintEvent("Rearlift disabled from high temperature of "+rearLift.getMotorTemperature(),true));
    }
    if (solenoid.get()){
      if (System.currentTimeMillis()>solenoidActivationTime+1000){
        solenoid.set(false);
      }
    }
    double elevatorCurrent=elevator.getOutputCurrent();
    //stop elevator at current spike
    //currently testing at 500 milliseconds (.5 seconds) after motor activation/directional change invocation 
    if (((elevator.getState()==Elevator.State.activeUp&&Math.abs(elevatorCurrent)>tuningValues.get("eCurUp"))||(elevator.getState()==Elevator.State.activeDown&&elevatorCurrent>tuningValues.get("eCurDow")&&!climbing)||(elevator.getState()==Elevator.State.activePID&&elevatorCurrent>tuningValues.get("eCurPID"))||(elevator.getState()==Elevator.State.active&&elevatorCurrent>tuningValues.get("eCurJoy")&&!climbing)||(elevator.getState()==Elevator.State.activeDown&&elevatorCurrent>50/*and climbing not necessary to specify*/)) && System.currentTimeMillis()>elevator.getActivationTime()+250){
      elevatorOff();
      eHandler.triggerEvent(new PrintEvent("Elevator disabled from high output current of "+elevatorCurrent,true));
    }
    double liftCurrent=rearLift.getOutputCurrent();
    if (!(rearLift.getState()==Lift.State.off)&&Math.abs(liftCurrent)>tuningValues.get("lCur") && System.currentTimeMillis()>rearLift.getActivationTime()+500){
      liftOff();
      eHandler.triggerEvent(new PrintEvent("Rearlift disabled from high output current of "+liftCurrent,true));
    }
    double armCurrent=arm.getOutputCurrent();
    if (!(arm.getState()==Arm.State.off)&&Math.abs(armCurrent)>tuningValues.get("aCur") && System.currentTimeMillis()>arm.getActivationTime()+500){
      armOff();
      eHandler.triggerEvent(new PrintEvent("Arm disabled from high output current of "+armCurrent,true));
    }
    //stop intake at voltage spike
    //currently testing at 500 milliseconds (.5 seconds) after motor activation/directional change invocation
    //no abs value needed for this one because outtake doesn't need to be ended at a voltage spike
    if (iotake.getState()==Iotake.State.activeIn&&iotake.getOutputCurrent()>25 && System.currentTimeMillis()>iotake.getActivationTime()+500){
      iotakeOff();
      eHandler.triggerEvent(new PrintEvent("Intake disabled from high output current",true));
    }
    
    //debug();
  }
  @Override
  public void disabledInit() {
    if (!(tsbAdapter==null)){
      tsbAdapter.setMode(TSBAdapter.Mode.Tune);
    }
  }
  @Override
  public void disabledPeriodic() {
    tsbAdapter.update();
  }

  //CLIMBING

  /**Toggles climbing (changes elevator down current limit)
   * 
   * 
   */
  public void toggleClimbing(){
    climbing=!climbing;
    if (climbing){
      tsbAdapter.setArmJoystick(rightStick);
    } else{
      tsbAdapter.setArmJoystick(tractorPanel);
    }
    eHandler.triggerEvent(new PrintEvent("CLIMBING MODE SET TO "+String.valueOf(climbing)+" - CHANGE IN ELEVATOR DOWN CURRENT LIMIT"));
  }

  //DRIVE TYPE

  /**Set drive type e.g. <code>driveType.oneJoystick</code> for one joystick arcade drive
   * 
   * @param d
   */
  public void setDriveType(driveType d){
    if (d==driveType.oneJoystick){
      xStick=rightStick;
    } else {
      xStick=leftStick;
    }
  }


  /**Returns current drive type
   * 
   * @return
   */
  public driveType getDriveType(){
    if (xStick.equals(rightStick)){
      return driveType.oneJoystick;
    } else {
      return driveType.twoJoyStick;
    }
  }


  //TUNING
  public double getTuningValue(String key){
    return tuningValues.get(key);
  }

  public void setTuningValue(String key,int value){
    tuningValues.replace(key,(double)value);
  }
  public void setTuningValue(String key,double value){
    tuningValues.replace(key,value);
  }


  //PNUEMATICS

  /**Activates/deactivates solenoid to fire hatch*/
  public void fireHatch(boolean on){
    solenoid.set(on);
    solenoidActivationTime=System.currentTimeMillis();
  }
  /**Toggle compressor
   * 
   */
  public void toggleCompressor(){
    compressor.setClosedLoopControl(!compressor.enabled());
  }


  //ELEVATOR CONTROL METHODS

  /**Moves elevator up
   * 
   */
  public void elevatorUp(){
    elevator.up(tuningValues.get("eSpdUp"));
  }

  /**Moves elevator up with specified demand
   * 
   */
  public void elevatorUp(double demand){
    elevator.up(demand);
  }

  /**Moves elevator down
   * 
   */
  public void elevatorDown(){
    elevator.down(tuningValues.get("eSpdDow"));
  }

  /**Moves elevator down with specified demand
   * 
   */
  public void elevatorDown(double demand){
    elevator.down(demand);
  }

  public void elevatorHoldPos(){
    if (elevator.getEncoder().getPosition()<.8){
      return;
    }
    elevator.moveToPos(elevator.getEncoder().getPosition());
  }

  public void elevatorHoldPosOR(){
    if(elevator.getOutputCurrent()>0){
      elevator.moveToPos(elevator.getEncoder().getPosition()+tuningValues.get("ePIDORUp"));
    } else {
      elevator.moveToPos(elevator.getEncoder().getPosition()-tuningValues.get("ePIDORDow"));      
    }
  }
  
  public void elevatorJoystick(){
    elevator.set(tractorPanel.getY()*-tuningValues.get("eSpdJ"));
  }

  //Not fuctional!
  public void elevatorTop(){
    elevator.moveToPos(tuningValues.get("eTop"),tuningValues.get("eSpdDow"),tuningValues.get("eSpdUp"));
  }

  public void elevatorBottom(){
    elevator.moveToPos(tuningValues.get("eBot"),tuningValues.get("eSpdDow"),tuningValues.get("eSpdUp"));
  }
  
  /**Moves elevator to middle possition
   * NOT FUCTIONAL
   * <--NEEDS TO HAVE VALUE TUNED-->
   * 
   */
  public void elevatorMid(){
    elevator.moveToPos(tuningValues.get("eMid"),tuningValues.get("eSpdDow"),tuningValues.get("eSpdUp"));
  }
  
  /**Moves elevator to cargo position
   * 
   * <--NEEDS TO HAVE VALUE TUNED-->
   * 
   */
  public void elevatorCargo(){
    elevator.moveToPos(tuningValues.get("eCar"),tuningValues.get("eSpdDow"),tuningValues.get("eSpdUp"));
  }

  /**turns off elevator
   * 
   */
  public void elevatorOff(){
    elevator.off();
  }


  //LIFT CONTROL METHODS

  /**Moves lift up
   * 
   */
  public void liftUp(){
    rearLift.up(tuningValues.get("lSpdUp"));
  }
  
  /**Moves lift down
   * 
   */
  public void liftDown(){
    rearLift.down(tuningValues.get("lSpdDow"));
  }
  public void liftHoldPos(){
    //rearLift.moveToPos(rearLift.getEncoder().getPosition());
  }
  public void liftHoldPosOR(){
    /*if(rearLift.getOutputCurrent()>0){
      rearLift.moveToPos(rearLift.getEncoder().getPosition()+tuningValues.get("lPIDOR"));
    } else {
      rearLift.moveToPos(rearLift.getEncoder().getPosition()-tuningValues.get("lPIDOR"));
    }*/
  }
  /**Moves lift to raised position
   * 
   */
  public void liftRaise(){
    //rearLift.moveToPos(tuningValues.get("lTop"),tuningValues.get("lSpdUp"));
  }

  public void liftLower(){
    //rearLift.moveToPos(tuningValues.get("lBot"),tuningValues.get("lSpdUp"));
  }
  /**Turns lift off
   * 
   */
  public void liftOff(){
    rearLift.off();
  }


  //ARM CONTROL METHODS

  /**Moves arm up
   * 
   */
  public void armUp(){
    arm.up(tuningValues.get("aSpd"));
  }

  public void armDown(){
    arm.down(tuningValues.get("aSpd"));
  }

  public void armJoystick(Joystick j) {
    arm.set(j.getX()*tuningValues.get("aSpdJ"));
  }

  public void armHoldPos(){
    arm.moveToPos(arm.getEncoder().getPosition());
  }

  public void armHoldPosOR(){
    if (arm.getOutputCurrent()>0){
      arm.moveToPos(arm.getEncoder().getPosition()+tuningValues.get("aPIDOR"));
    } else {
      arm.moveToPos(arm.getEncoder().getPosition()-tuningValues.get("aPIDOR"));
    }
  }

  public void armSit(){
    arm.moveToPos(tuningValues.get("aSit"),tuningValues.get("aSpd"));
  }
  public void armCargo(){
    arm.moveToPos(tuningValues.get("aCar"),tuningValues.get("aSpd"));
  }
  public void armDeck(){
    arm.moveToPos(tuningValues.get("aDec"),tuningValues.get("aSpd"));
  }

  public void armHatch(){
    arm.moveToPos(tuningValues.get("aHat"),tuningValues.get("aSpd"));
  }

  public void armBall(){
    arm.moveToPos(tuningValues.get("aBal"),tuningValues.get("aSpd"));
  }

  public void armOff(){
    arm.off();
  }
  

  //IOTAKE CONTROL METHODS

  /**Activates outtake
   * 
   */
  public void outtake(){
    iotake.outtake(.5);
  }

  /**Initiates intake
   * 
   */
  public void intake(){
    iotake.intake(.8);
  }

  /**Turns iotake off
   * 
   */
  public void iotakeOff(){
    iotake.off();
  }


  //OTHER METHODS

  /**Prints sensor positions for all monitored motors
   * 
   */
  public void printSensorPositions(){
    eHandler.triggerEvent(new PrintEvent("<--ELEVATOR-->"));
    elevator.printSensorPosition();
    eHandler.triggerEvent(new PrintEvent("<--REAR LIFT-->"));
    rearLift.printSensorPosition();
    eHandler.triggerEvent(new PrintEvent("<--ARM-->"));
    arm.printSensorPosition();
  }

  /**
   * Prints debug information to console, currently only for debugging purposes (cannot invoke with robot IO)
   * Might make more detailed and advanced debugging methods later, also will probably make write to a log file to better examen debug info (kind of hard to analize a periodically changing console)
   */
  public void debug(){
    //printSensorPositions();
    //eHandler.triggerEvent(new PrintEvent("Test:"+Preferences.getInstance().getDouble("Test", 0));
    eHandler.triggerEvent(new PrintEvent("Elevator motor temp: "+elevator.getMotorTemperature()));
    eHandler.triggerEvent(new PrintEvent("Lift motor temp: "+rearLift.getMotorTemperature()));
    eHandler.triggerEvent(new PrintEvent("Arm motor temp: "+arm.getMotorTemperature()));
    
    
    
    /*eHandler.triggerEvent(new PrintEvent("X:"+leftStick.getX()); //not necessary right now 
    eHandler.triggerEvent(new PrintEvent("Y: "+leftStick.getY());
    eHandler.triggerEvent(new PrintEvent("Left: "+leftFrontCAN.getAppliedOutput());
    eHandler.triggerEvent(new PrintEvent("Right: "+rightFrontCAN.getAppliedOutput());*/
  }


  public static Robot getInstance(){
    return instance;
  }

}
