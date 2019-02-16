/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.buttons.*;
import frc.robot.motors.Elevator;
import frc.robot.motors.Iotake;

import com.revrobotics.*;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Robot extends TimedRobot {

  public static enum driveType {twoJoyStick,oneJoystick};
  private DifferentialDrive robot;
  private Joystick leftStick;
  private Joystick rightStick;
  private Joystick tractorPanel;
  private Joystick xStick;
  private JSBAdapter jsbAdapter;
  private TSBAdapter tsbAdapter;
  private CANSparkMax leftFrontCAN=new CANSparkMax(10,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax leftBackCAN=new CANSparkMax(11,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightFrontCAN=new CANSparkMax(13,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightBackCAN=new CANSparkMax(12,CANSparkMaxLowLevel.MotorType.kBrushless);
  private Elevator elevator=new Elevator();
  private Iotake iotake=new Iotake();//Iotake is intake/outtake system
  private TalonSRX rearLift=new TalonSRX(22);
  private TalonSRX armRotate=new TalonSRX(23);
  private TalonSRX intakeLeft=new TalonSRX(31);
  private TalonSRX intakeRight=new TalonSRX(32);
  private ExtendableMotor extendableElevator  = new ExtendableMotor(elevator, 0.05, 1);
  private ExtendableMotor extendablerearLift = new ExtendableMotor(rearLift, 0.05, 1);
  private ExtendableMotor extendablearmRotate = new ExtendableMotor(armRotate, 0.05, 1);
  private ExtendableMotor extendableintakeLeft = new ExtendableMotor(intakeLeft, 0.05, 1);
  private ExtendableMotor extendableintakeRight = new ExtendableMotor(intakeRight, 0.05, 1);
  @Override
  public void robotInit() {    
    leftBackCAN.follow(leftFrontCAN);
    rightBackCAN.follow(rightFrontCAN);
    robot = new DifferentialDrive(leftFrontCAN,rightFrontCAN);
    leftStick = new Joystick(0);
    rightStick = new Joystick(1);
    xStick = leftStick;
    tractorPanel = new Joystick(2);
    jsbAdapter=new JSBAdapter(rightStick, this);
    tsbAdapter=new TSBAdapter(tractorPanel, this);
  }


  @Override
  public void teleopPeriodic() {
    //driving arcade
    robot.arcadeDrive(rightStick.getY()*-.75, xStick.getX()*.75);
    this.extendableElevator.stepMotor(leftStick.getRawButton(1), leftStick.getRawButton(2));
    this.extendablerearLift.stepMotor(leftStick.getRawButton(3), leftStick.getRawButton(4));
    this.extendablearmRotate.stepMotor(leftStick.getRawButton(5), leftStick.getRawButton(6));
    //update button inputs
    if (!(jsbAdapter.equals(null)&&tsbAdapter.equals(null))){
      jsbAdapter.update();
      tsbAdapter.update();
    }
    //stop elevator at voltage spike
    //currently testing at 500 milliseconds (.5 seconds) after motor activation/directional change invocation 
    if (elevator.getMotorOutputVoltage()>7 && System.currentTimeMillis()>elevator.getActivationTime()+500){
      elevatorOff();
      System.out.println("Elevator disabled from high output voltage");
    }
    //stop intake at voltage spike
    //currently testing at 500 milliseconds (.5 seconds) after motor activation/directional change invocation
    if (iotake.getAvgMotorOutputVoltage()>7 && System.currentTimeMillis()>iotake.getActivationTime()+500){
      outtake(false);
      System.out.println("Outtake disabled from high output voltage");
    } 
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
  public void lift(boolean on){
    if (on){
      rearLift.set(ControlMode.PercentOutput, .3);
    } else {
      rearLift.set(ControlMode.PercentOutput,0);
    }
  }


  /**Sets arm rotation to either be on or off
   * 
   * @param on
   */
  public void armRotate(boolean on){
    if (on){
      armRotate.set(ControlMode.PercentOutput, .3);
    } else {
      armRotate.set(ControlMode.PercentOutput,0);
    }
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
