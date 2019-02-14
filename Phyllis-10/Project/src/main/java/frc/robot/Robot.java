/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.*;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import javax.swing.*;


public class Robot extends TimedRobot {
  public static enum driveType {twoJoyStick,oneJoystick};
  private DifferentialDrive robot;
  private Joystick leftStick;
  private Joystick rightStick;
  private Joystick tractorPanel;
  private Joystick xStick;
  private JSBAdapter jsbAdapter;
  TSBAdapter tsbAdapter;
  private CANSparkMax leftFrontCAN=new CANSparkMax(10,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax leftBackCAN=new CANSparkMax(11,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightFrontCAN=new CANSparkMax(13,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightBackCAN=new CANSparkMax(12,CANSparkMaxLowLevel.MotorType.kBrushless);
  private TalonSRX elevator=new TalonSRX(21);
  private TalonSRX rearLift=new TalonSRX(22);
  private TalonSRX armRotate=new TalonSRX(23);
  private TalonSRX intakeLeft=new TalonSRX(31);
  private TalonSRX intakeRight=new TalonSRX(32);
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
  //Conditional delay for motor spike
  @Override
  public void teleopPeriodic() {
    //driving arcade
    robot.arcadeDrive(rightStick.getY()*-.75, xStick.getX()*.75);
    if (!(jsbAdapter.equals(null)&&tsbAdapter.equals(null))){
      jsbAdapter.update();
      tsbAdapter.update();
    }
    debug();
    if (elevator.getMotorOutputVoltage()>7){
      elevatorOff();
      System.out.println("Elevator disabled from high bus voltage");
    }
  }
  public void setDriveType(driveType d){
    if (d==driveType.oneJoystick){
      xStick=rightStick;
    } else {
      xStick=leftStick;
    }
  }
  /**
   * Returns 
   * @return
   */
  public driveType getDriveType(){
    if (xStick.equals(rightStick)){
      return driveType.oneJoystick;
    } else {
      return driveType.twoJoyStick;
    }
  }
  public void elevatorUp(){
    elevator.set(ControlMode.PercentOutput, .75);
  }
  public void elevatorDown(){
    elevator.set(ControlMode.PercentOutput, -.75);
  }
  public void elevatorOff(){
    elevator.set(ControlMode.PercentOutput,0);
  }
  public void lift(boolean on){
    if (on){
      rearLift.set(ControlMode.PercentOutput, .3);
    } else {
      rearLift.set(ControlMode.PercentOutput,0);
    }
  }
  public void armRotate(boolean on){
    if (on){
      armRotate.set(ControlMode.PercentOutput, .3);
    } else {
      armRotate.set(ControlMode.PercentOutput,0);
    }
  }
  //TODO outtake
  public void intake(boolean on){
    if (on){
      intakeLeft.set(ControlMode.PercentOutput, .3);
      intakeRight.set(ControlMode.PercentOutput, .3);
    } else {
      intakeLeft.set(ControlMode.PercentOutput,0);
      intakeRight.set(ControlMode.PercentOutput,0);
    }
    
  }
  /**
   * Prints debug information to console
   */
  private void debug(){
    System.out.println(elevator.getMotorOutputVoltage());
    /*System.out.println("X:"+leftStick.getX());
    System.out.println("Y: "+leftStick.getY());
    System.out.println("Left: "+leftFrontCAN.getAppliedOutput());
    System.out.println("Right: "+rightFrontCAN.getAppliedOutput());*/
  }
}
