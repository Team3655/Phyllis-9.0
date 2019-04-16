package tractorTech.event;

import java.util.ArrayList;

public class EventSequence {
    private Event[] events;
    int stage;
    boolean sequenceComplete;


    public EventSequence(Event[] events){
        this.events=events;
        stage=0;
        sequenceComplete=false;
        events[stage].resetTriggerTime();
    }

    public void triggerNext(){
        events[stage].trigger();
        if (events[stage].taskDone()){
            stage=stage+1;
            if (stage>events.length-1){
                sequenceComplete=true;
            } else {
                events[stage].resetTriggerTime();
            }
        }
    }

    public boolean sequenceComplete(){
        return sequenceComplete;
    }

    public int getStage(){
        return stage;
    }
}