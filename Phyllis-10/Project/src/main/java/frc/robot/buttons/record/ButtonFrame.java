package frc.robot.buttons.record;
import frc.robot.Robot;
import frc.robot.buttons.*;
import frc.robot.event.customevents.ButtonEvent;

public class ButtonFrame {
    private long pressTime;
    private long duration;
    private long triggerTime;
    private int no;
    private ButtonHandler buttonHandler;
    private boolean recorded;

    public ButtonFrame(ButtonHandler b,int no){
        pressTime=System.currentTimeMillis();
        buttonHandler=b;
        recorded=false;
        this.no=no;
    }

    public boolean release(long reference){
        long currentTime=System.currentTimeMillis();
        if (!recorded){
            duration=currentTime-pressTime;
            triggerTime=currentTime-reference;
            recorded=true;
            return true;
        }
        return false;
    }
    public void play(){
        Robot.getInstance().eHandler.triggerEvent(new ButtonEvent(triggerTime,duration,buttonHandler,no)); 
    }

}