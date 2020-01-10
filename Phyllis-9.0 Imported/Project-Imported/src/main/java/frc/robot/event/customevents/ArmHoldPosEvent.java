package frc.robot.event.customevents;

import frc.robot.Robot;
import frc.robot.event.Event;

public class ArmHoldPosEvent extends Event{

    public ArmHoldPosEvent(long delay){
        super(delay);
    }
    public void task(){
        Robot.getInstance().armHoldPos();
    }
}