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
import javax.swing.*;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  public static enum driveType {twoJoyStick,oneJoystick};
  private DifferentialDrive robot;
  private Joystick leftStick;
  private Joystick rightStick;
  private Joystick xStick;
  private Button button7;
  private BAdapter bAdapter;
  private CANSparkMax leftFrontCAN=new CANSparkMax(10,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax leftBackCAN=new CANSparkMax(11,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightFrontCAN=new CANSparkMax(13,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightBackCAN=new CANSparkMax(12,CANSparkMaxLowLevel.MotorType.kBrushless);
  private Talon leftFront = new Talon(10);
  private Talon leftBack = new Talon(11);
  private Talon rightFront = new Talon(13);
  private Talon righhBack = new Talon(12);
  @Override
  public void robotInit() {
    //if (leftFrontCAN.getBusVoltage()>0){
      leftBackCAN.follow(leftFrontCAN);
    rightBackCAN.follow(rightFrontCAN);
    
    robot = new DifferentialDrive(leftFrontCAN,rightFrontCAN);
    //} else {
      //leftBack.follow(leftFront);
    //}
    leftStick = new Joystick(0);
    rightStick = new Joystick(1);
    xStick = leftStick;
    bAdapter=new BAdapter(rightStick, this);
    //Button button1 = new JoystickButton(leftStick, 1);
    button7 = new JoystickButton(rightStick, 7);
  }

  @Override
  public void teleopPeriodic() {
    //driving two joystick
    robot.arcadeDrive(rightStick.getY()*-.5, xStick.getX()*.5,true);
    if (!bAdapter.equals(null)){
      bAdapter.update();
    }
  }
  public void setDriveType(driveType d){
    if (d==driveType.oneJoystick){
      xStick=rightStick;
    } else {
      xStick=leftStick;
    }
  }
  public driveType getDriveType(){
    if (xStick.equals(rightStick)){
      return driveType.oneJoystick;
    } else {
      return driveType.twoJoyStick;
    }
  }
  /**
   * Prints debug information to console
   */
  private void debug(){
    System.out.println("X:"+leftStick.getX());
    System.out.println("Y: "+leftStick.getY());
    System.out.println("Left: "+leftFrontCAN.getAppliedOutput());
    System.out.println("Right: "+rightFrontCAN.getAppliedOutput());
  }
}
