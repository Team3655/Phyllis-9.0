package frc.robot.event;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**A seprate thread to handle events
 * 
 */
public class EventHandler extends Thread{
    private boolean enabled;
    private List<Event> queuedEvents;
    private List<EventSequence> activeSequences;
    
    public EventHandler(){
        setName(this.getClass().toString());
        queuedEvents=new CopyOnWriteArrayList<Event>();
        activeSequences=new CopyOnWriteArrayList<EventSequence>();
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
            for (EventSequence e:activeSequences){                    e.triggerNext();
                if (e.sequenceComplete){
                    activeSequences.remove(e);
                }
            }
        }
    }
    /**Adds an event to the event queue
     * 
     * @param e the event to add to the queue
     * @return wheather the opperation was successful
     */
    public boolean triggerEvent(Event e){
            return queuedEvents.add(e);
    }

/**Adds an event to the event queue
     * 
     * @param e the event sequence to add to the queue
     * @return wheather the opperation was successful
     */
    public boolean triggerEvent(EventSequence e){
            return activeSequences.add(e);
    }

    /**Adds an event to the event queue
     * 
     * @param e the runnable task to add to the queue
     * @return wheather the opperation was successful
     */
    public boolean triggerEvent(Runnable e){
        return queuedEvents.add(new Event(e));
    }

    /**Adds an event to the event queue
     * 
     * @param e the runnable task to add to the queue
     * @param delay the delay to execute the task
     * @return wheather the opperation was successful
     */
    public boolean triggerEvent(Runnable e,long delay){
        return queuedEvents.add(new Event(e,delay));
    }

    /**Enables thread
     * 
     * @return wheather the operation was successful
     */
    public boolean enable(){
            enabled=true;
            return true;
    }
    /**Ends thread
     * 
     * @return wheather the operation was successful
     */
    public boolean disable(){
            enabled=false;
            return true;
    }
}