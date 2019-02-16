package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot.driveType;
/**
 * Button handler for right joystick
 */
public class JSBAdapter extends ButtonHandler{
    private Robot robot;
    public JSBAdapter(Joystick joystick, Robot robot){
        super(joystick,12);
        this.robot=robot;
    }
    
    public void buttonPressed(int no){
        switch (no){
            case 1:
                robot.intake(true);
            break;
            case 2:
                robot.outtake(true);
            break;
            case 7:
                if (robot.getDriveType()==Robot.driveType.oneJoystick){
                    robot.setDriveType(Robot.driveType.twoJoyStick);

                } else {
                    robot.setDriveType(Robot.driveType.oneJoystick);
                }
            break;
        }
        System.out.println(no);   
    }
    public void buttonReleased(int no){
        switch (no){
            case 2:
              robot.outtake(false);
            break;
        }
    }
}