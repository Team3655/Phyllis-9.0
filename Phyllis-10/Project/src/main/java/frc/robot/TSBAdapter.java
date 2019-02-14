package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class TSBAdapter extends ButtonHandler{
    Robot robot;
    public TSBAdapter(Joystick tractorPanel, Robot robot){
        super(tractorPanel,28);
        this.robot=robot;
    }
    public void buttonPressed(int no){
        switch (no){
            case 1:
                robot.elevatorUp();
            break;
            case 6:
                robot.elevatorDown();
            break;
            case 2:
                robot.elevatorOff();
            break;
        }
        System.out.println(no);
    }
    public void buttonReleased(int no){
    }
}