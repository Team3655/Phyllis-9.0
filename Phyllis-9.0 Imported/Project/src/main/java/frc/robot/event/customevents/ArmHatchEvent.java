package frc.robot.event.customevents;

import frc.robot.Robot;
import frc.robot.event.Event;

public class ArmHatchEvent extends Event{
    public ArmHatchEvent(long delay){
        super(delay);
    }
    public ArmHatchEvent(){
        super();
    }
    
    public void task(){
        Robot.getInstance().armHatch();
    }
}