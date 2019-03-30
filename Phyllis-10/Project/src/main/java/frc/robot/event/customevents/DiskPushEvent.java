package frc.robot.event.customevents;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;
import frc.robot.event.*;

public class DiskPushEvent extends Event{
    boolean hatchFired;
    public DiskPushEvent(long duration){
        super(duration,900);
        Robot.getInstance().fireHatch(true);
        hatchFired=false;
    }
    public void task(){
        if (hatchFired){
            Robot.getInstance().pushSuckers(false);
        } else {
            Robot.getInstance().fireHatch(false);
        }
    }
    @Override
    public boolean eventCompleteCondition() {
        if (!hatchFired){
            hatchFired=true;
            return false;
        }
        return true;
    }
    
}