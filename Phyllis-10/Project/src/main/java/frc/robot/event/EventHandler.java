package frc.robot.event;

import java.util.ArrayList; 

public class EventHandler extends Thread{
    private boolean enabled;
    private ArrayList<Event> queuedEvents;
    public EventHandler(){
        queuedEvents=new ArrayList<Event>();
    }
    @Override
    public void run() {
        enable();
        while (enabled){
            for (Event e:queuedEvents){
                e.trigger();
                if (e.taskDone()){
                    queuedEvents.remove(e);
                }
            }
        }
    }
    public void triggerEvent(Event e){
        queuedEvents.add(e);
    }
    public void enable(){
        enabled=true;
    }
    public void disable(){
        enabled=false;
    }
}