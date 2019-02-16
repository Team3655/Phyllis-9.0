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
                robot.outtake(true);
            break;
            //button 2 initiate intake
            case 2:
              //  robot.intake(true);
            break;
            //button 3 to emergency stop intake (shouldn't be necessary but just in case, in a place where it won't accidentaly be bumped)
            case 3:
                robot.intake(false);
            break;
            //button 5 hold for rear lift **WILL NOT STAY ON THIS CONTROLLER**
            case 5:
             //   robot.lift(true);
            break;
            //button 7 switch drive type (one joystick/two joystick arcade)
            case 7:
                if (robot.getDriveType()==Robot.driveType.oneJoystick){
                    robot.setDriveType(Robot.driveType.twoJoyStick);

                } else {
                    robot.setDriveType(Robot.driveType.oneJoystick);
                }
            break;
            //button 8 hold rotation of arm **WILL NOT STAY ON THIS CONTROLLER**
            case 8:
              //  robot.armRotate(true);
            break;
        }
        System.out.println(no);   
    }
    public void buttonReleased(int no){
        switch (no){
            //button 1 hold for outtake
            case 1:
                robot.outtake(false);
            break;
            
            //button 5 hold for rear lift **WILL NOT STAY ON THIS CONTROLLER**
            case 5:
                //robot.lift(false);
            break;
            //button 8 hold for rotation of arm **WILL NOT STAY ON THIS CONTROLLER**
            case 8:
               // robot.armRotate(false);
            break;
        }
    }
}