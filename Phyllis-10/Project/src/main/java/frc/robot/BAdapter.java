package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot.driveType;

public class BAdapter extends ButtonHandler{
    private Robot robot;
    public BAdapter(GenericHID buttonArray, Robot robot){
        super(buttonArray);
        this.robot=robot;
    }
    
    public void buttonPressed(int no){
        if (no==7){
            if (robot.getDriveType()==Robot.driveType.oneJoystick){
                robot.setDriveType(Robot.driveType.twoJoyStick);

            } else {
                robot.setDriveType(Robot.driveType.oneJoystick);
            }
            return;
        }
    }
    public void buttonReleased(int no){
        
    }
}