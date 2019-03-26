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
    private enum ModifyingThread{EventHandler,Main};
    private ModifyingThread modifyingThread;
    private boolean modifierLocked;
    public EventHandler(){
        queuedEvents=new CopyOnWriteArrayList<Event>();
        activeSequences=new CopyOnWriteArrayList<EventSequence>();
        setDaemon(true);
        modifierLocked=false;
        modifyingThread=ModifyingThread.EventHandler;
    }
    @Override
    public void run() {
        enable();
        while (enabled){
            modifierLocked=false;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e){

            }
            if (modifyingThread==ModifyingThread.EventHandler){
                modifierLocked=true;
                //try {
                    for (Event e:queuedEvents){
                        e.trigger();
                        if (e.taskDone()){
                            queuedEvents.remove(e);
                        }
                    }
                    for (EventSequence e:activeSequences){
                        e.triggerNext();
                        if (e.sequenceComplete){
                            activeSequences.remove(e);
                        }
                    }
                //} catch (ConcurrentModificationException ex){
                    //class is being accessed from external thread
                //}
            }
        }
    }
    /**Adds an event to the event queue
     * 
     * @param e the event to add to the queue
     * @return wheather the opperation was successful
     */
    public boolean triggerEvent(Event e){
        /*try{
            if (setModifier(ModifyingThread.Main)){
                modifierLocked=true;
                if (modifyingThread==ModifyingThread.Main){*/
                    queuedEvents.add(e);
                /*}
            } else {
                return false;
            }
            modifierLocked=false;
            setModifier(ModifyingThread.EventHandler);
            */
            return true;
        /*} catch (Exception ex){
            ex.printStackTrace();
            return false;
        }*/
    }

/**Adds an event to the event queue
     * 
     * @param e the event to add to the queue
     * @return wheather the opperation was successful
     */
    public boolean triggerEvent(EventSequence e){
        /*try{
            if (setModifier(ModifyingThread.Main)){
                modifierLocked=true;
                if (modifyingThread==ModifyingThread.Main){*/
                    activeSequences.add(e);
                /*}
            } else {
                return false;
            }
            modifierLocked=false;
            */setModifier(ModifyingThread.EventHandler);
            return true;
        /*} catch (Exception ex){
            ex.printStackTrace();
            return false;
        }*/
    }

    /**Enables thread
     * 
     * @return wheather the operation was successful
     */
    public boolean enable(){
        /*try{
            if (setModifier(ModifyingThread.Main)){
                modifierLocked=true;*/
                enabled=true;
            /*} else {
                return false;
            }
            modifierLocked=false;
            setModifier(ModifyingThread.EventHandler);
            modifierLocked=true;*/
            return true;
        /*} catch (Exception ex){
            ex.printStackTrace();
            return false;
        }*/
    }
    /**Ends thread
     * 
     * @return wheather the operation was successful
     */
    public boolean disable(){
        /*try{
            if (setModifier(ModifyingThread.Main)){
                modifierLocked=true;*/
                enabled=false;
            /*} else {
                return false;
            }
            modifierLocked=false;
            setModifier(ModifyingThread.EventHandler);
            modifierLocked=true;*/
            return true;
        /*} catch (Exception ex){
            ex.printStackTrace();
            return false;
        }*/
    }
    private boolean setModifier(ModifyingThread modifyingThread){
        if (!modifierLocked){
            this.modifyingThread=modifyingThread;
            return true;
        }
        return false;
    }
}