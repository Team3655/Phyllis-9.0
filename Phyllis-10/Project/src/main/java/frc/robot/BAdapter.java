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
        switch (no){
            case 5:
                robot.lift();
            break;
            case 6:
                robot.elevatorUp();
            break;
            case 8:
                robot.armRotate();
            break;
            case 9:
                robot.intake();
            break;
            case 7:
                if (robot.getDriveType()==Robot.driveType.oneJoystick){
                    robot.setDriveType(Robot.driveType.twoJoyStick);

                } else {
                    robot.setDriveType(Robot.driveType.oneJoystick);
                }
            break;
            default:
            break;
        }
            
    }
    public void buttonReleased(int no){
        
    }
}