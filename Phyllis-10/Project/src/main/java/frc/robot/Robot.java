/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.buttons.*;
import frc.robot.motors.Arm;
import frc.robot.motors.Elevator;
import frc.robot.motors.ExtendableMotor;
import frc.robot.motors.Iotake;
import frc.robot.motors.Lift;

import com.revrobotics.*;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Robot extends TimedRobot {

  public static enum driveType {twoJoyStick,oneJoystick};
  private final double voltageCutoff=8.5;
  private DifferentialDrive robot;
  private Joystick leftStick;
  private Joystick rightStick;
  private Joystick tractorPanel;
  private Joystick xStick;
  private JSBAdapter jsbAdapter;
  private TSBAdapter tsbAdapter;
  private Solenoid solenoid;
  private Compressor compressor;
  private CANSparkMax leftFrontCAN=new CANSparkMax(10,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax leftBackCAN=new CANSparkMax(11,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightFrontCAN=new CANSparkMax(13,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightBackCAN=new CANSparkMax(12,CANSparkMaxLowLevel.MotorType.kBrushless);
  private Elevator elevator=new Elevator();
  private Iotake iotake=new Iotake();//Iotake is intake/outtake system
  private Arm arm=new Arm();
  private Lift rearLift=new Lift();
  private TalonSRX intakeLeft=new TalonSRX(31);
  private TalonSRX intakeRight=new TalonSRX(32);
  private ExtendableMotor extendableintakeRight = new ExtendableMotor(intakeRight, 0.05, 1);
  @Override
  public void robotInit() { 
    CameraServer.getInstance().startAutomaticCapture();  
    leftBackCAN.follow(leftFrontCAN);
    rightBackCAN.follow(rightFrontCAN);
    robot = new DifferentialDrive(leftFrontCAN,rightFrontCAN);
    leftStick = new Joystick(0);
    rightStick = new Joystick(1);
    xStick = leftStick;
    tractorPanel = new Joystick(2);
    jsbAdapter=new JSBAdapter(rightStick, this);
    tsbAdapter=new TSBAdapter(tractorPanel, this);
    solenoid=new Solenoid(0); //DOUBLE CHECK IDS
    compressor=new Compressor(0); //DOUBLE CHECK IDS
  }


  @Override
  public void teleopPeriodic() {
    //driving arcade
    robot.arcadeDrive(rightStick.getY()*-.75, xStick.getX()*.75);
    
    //update button inputs
    if (!(jsbAdapter.equals(null)&&tsbAdapter.equals(null))){
      jsbAdapter.update();
      tsbAdapter.update();
    }
    //stop elevator at voltage spike
    //currently testing at 500 milliseconds (.5 seconds) after motor activation/directional change invocation 
    if (!(elevator.getState()==Elevator.State.off)&&Math.abs(elevator.getMotorOutputVoltage())<voltageCutoff && System.currentTimeMillis()>elevator.getActivationTime()+500){
      elevatorOff();
      System.out.println("Elevator disabled from low output voltage");
    }
    if (!(rearLift.getState()==Lift.State.off)&&Math.abs(rearLift.getMotorOutputVoltage())<voltageCutoff && System.currentTimeMillis()>rearLift.getActivationTime()+500){
      liftOff();
      System.out.println("Rearlift disabled from low output voltage");
    }
    if (!(arm.getState()==Arm.State.off)&&Math.abs(arm.getMotorOutputVoltage())<voltageCutoff && System.currentTimeMillis()>arm.getActivationTime()+500){
      armOff();
      System.out.println("Arm disabled from low output voltage");
    }
    //stop intake at voltage spike
    //currently testing at 500 milliseconds (.5 seconds) after motor activation/directional change invocation
    //no abs value needed for this one because outtake doesn't need to be ended at a voltage spike
    if (iotake.getState()==Iotake.State.activeIn&&iotake.getAvgMotorOutputVoltage()<voltageCutoff && System.currentTimeMillis()>iotake.getActivationTime()+500){
      intake(false);
      System.out.println("Outtake disabled from low output voltage");
    }
    //debug();
  }


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
  /**Activates/deactivates solenoid to fire hatch*/
  public void fireHatch(boolean on){
    solenoid.set(on);
  }
  /**Toggle compressor
   * 
   */
  public void toggleCompressor(){
    compressor.setClosedLoopControl(!compressor.enabled());
  }
  /**Moves elevator up
   * 
   */
  public void elevatorUp(){
    elevator.up();
  }

  /**Moves elevator down
   * 
   */
  public void elevatorDown(){
    elevator.down();
  }

  /**turns off elevator
   * 
   */
  public void elevatorOff(){
    elevator.off();
  }

  /**Sets lift to either be on or off
   * 
   * @param on
   */
  public void liftUp(){
    rearLift.up();;
  }
  public void liftDown(){
    rearLift.down();
  }
  public void liftOff(){
    rearLift.off();
  }

  /**Moves arm up
   * 
   */
  public void armUp(){
    arm.up();
  }

  public void armDown(){
    arm.down();
  }
  public void armOff(){
    arm.off();
  }

  /**Sets outtake to either be on or off
   * 
   */
  public void outtake(boolean on){
    if (on){
      iotake.outtake();
    } else {
      iotake.off();
    }
  }

  /**Sets intake to either be on or off
   * 
   */
  public void intake(boolean on){
    if (on){
      iotake.intake();
    } else {
      iotake.off();
    }
    
  }


  /**
   * Prints debug information to console, currently only for debugging purposes (cannot invoke with robot IO)
   * Might make more detailed and advanced debugging methods later, also will probably make write to a log file to better examen debug info (kind of hard to analize a periodically changing console)
   */
  private void debug(){
    System.out.println(elevator.getMotorOutputVoltage());
    /*System.out.println("X:"+leftStick.getX()); //not necessary right now 
    System.out.println("Y: "+leftStick.getY());
    System.out.println("Left: "+leftFrontCAN.getAppliedOutput());
    System.out.println("Right: "+rightFrontCAN.getAppliedOutput());*/
  }
}
