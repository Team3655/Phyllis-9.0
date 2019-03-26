package frc.robot.event.customevents;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;
import frc.robot.event.*;

public class DiskPushEvent extends Event{

    public DiskPushEvent(long duration){
        super(duration);
        Robot.getInstance().fireHatch(true);
    }
    public void task(){
        Robot.getInstance().fireHatch(false);
    }
    
}