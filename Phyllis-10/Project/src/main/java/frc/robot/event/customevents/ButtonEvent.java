package frc.robot.event.customevents;

import frc.robot.buttons.ButtonHandler;
import frc.robot.event.Event;

public class ButtonEvent extends Event{
    private boolean buttonReleased;
    private ButtonHandler buttonHandler;
    private int no;
    public ButtonEvent(long delay,long duration,ButtonHandler b,int no){
        super(delay,duration);
        buttonReleased=false;
        buttonHandler=b;
        this.no=no;
    }
    public void task(){
        if (buttonReleased=false){
            buttonHandler.simButtonPress(no);
            buttonReleased=true;
        } else {
            buttonHandler.simButtonRelease(no);
        }
    }
    @Override
    public boolean eventCompleteCondition() {
        return buttonReleased;
    }
}