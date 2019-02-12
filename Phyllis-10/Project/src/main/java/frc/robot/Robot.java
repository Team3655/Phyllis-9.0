/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive robot;
  private Joystick leftStick;
  //private Joystick m_rightStick;
  private CANSparkMax leftFront=new CANSparkMax(10,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax leftBack=new CANSparkMax(11,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightFront=new CANSparkMax(13,CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightBack=new CANSparkMax(12,CANSparkMaxLowLevel.MotorType.kBrushless);
  @Override
  public void robotInit() {
    
    leftBack.follow(leftFront);
    rightBack.follow(rightFront);
    robot = new DifferentialDrive(leftFront,rightFront);
    leftStick = new Joystick(0);
    
    //Button button1 = new JoystickButton(leftStick, 1);
  }

  @Override
  public void teleopPeriodic() {
    //driving one joystick
    robot.arcadeDrive(leftStick.getY()*-1, leftStick.getX(),true);
    
  }
  /**
   * Prints debug information to console
   */
  private void debug(){
    System.out.println("X:"+leftStick.getX());
    System.out.println("Y: "+leftStick.getY());
    System.out.println("Left: "+leftFront.getAppliedOutput());
    System.out.println("Right: "+rightFront.getAppliedOutput());
  }
}
