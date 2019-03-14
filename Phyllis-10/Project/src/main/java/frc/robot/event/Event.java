package frc.robot.event;

public abstract class Event{
    private long triggerTime;
    private long delay;
    private boolean taskDone;
    public Event(){
        taskDone=false;
        delay=0l;
        triggerTime=System.currentTimeMillis();
    }
    /**Initializes an event that executes the task with <code>delay</code> delay in millseconds
     * 
     * @param delay delay ms
     */
    public Event(long delay){
        taskDone=false;
        this.delay=delay;
        triggerTime=System.currentTimeMillis();
    }
    /**Does task if the delay is over
     * 
     */
    public final void trigger(){
        if (System.currentTimeMillis()>triggerTime+delay){
            task();
            taskDone=true;
        }
    }
    /**
     * 
     * @return wheather the task has been done yet
     */
    public final boolean taskDone(){
        return taskDone;
    }
    /**Task to be carried out when triggered after delay
     * 
     */
    public abstract void task();
}