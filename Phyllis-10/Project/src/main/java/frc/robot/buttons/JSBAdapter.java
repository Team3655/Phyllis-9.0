package frc.robot.buttons;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;
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
            //button 1 hold for outtake
            case 1:
                robot.outtake();
            break;
            //button 2 hold for intake
            case 2:
                robot.intake();
            break;
            //button 3 to emergency stop intake (shouldn't be necessary but just in case, in a place where it won't accidentaly be bumped)
            case 3:
                robot.iotakeOff();
            break;
            //button 4 toggles weather or not the robot is in climbing mode
            case 4:
                robot.toggleClimbing();
            break;
            //button 7 switch drive type (one joystick/two joystick arcade)
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
            //button 1 hold for outtake
            case 1:
                robot.iotakeOff();
            break;
            //button 2 hold for intake
            case 2:
                robot.iotakeOff();
            break;
        }
    }
    public void buttonDown(int no){}
}