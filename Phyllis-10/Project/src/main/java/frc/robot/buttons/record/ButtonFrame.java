package frc.robot.buttons.record;
import frc.robot.buttons.*;

public class ButtonFrame {
    private long pressTime;
    private long duration;
    private long triggerTime;
    private ButtonHandler buttonHandler;
    private boolean recorded;

    public ButtonFrame(ButtonHandler b){
        pressTime=System.currentTimeMillis();
        buttonHandler=b;
        recorded=false;
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
        
    }

}