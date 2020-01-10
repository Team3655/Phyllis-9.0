package frc.robot.buttons.record;

import java.util.ArrayList;
import java.util.Hashtable;

import frc.robot.Robot;
import frc.robot.buttons.ButtonHandler;

public class ButtonLog {
    private long reference;
    private Hashtable<Integer,ButtonFrame> bFramesRec;
    private ArrayList<ButtonFrame> bFrames;
    private ButtonHandler bHandler;
    public void buttonPressed(int no){
        bFramesRec.put(no,new ButtonFrame(bHandler, no));
    }
    public boolean buttonRelease(int no){
        if (bFramesRec.containsKey(no)){
            ButtonFrame bFrame=bFrames.get(no);
            bFramesRec.remove(bFrame);
            bFrame.release(reference);
            bFrames.add(bFrame);
            return true;
        }
        //button not being recorded
        return false;
    }
    public void play(){
        for (ButtonFrame f:bFrames){
            f.play();
        }
    }
}