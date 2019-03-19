package frc.robot.event;

public abstract class Event{
    private long triggerTime;
    private long delay;
    private long loopDelay;
    private boolean taskDone;
    public Event(){
        taskDone=false;
        delay=0l;
        loopDelay=0;
        triggerTime=System.currentTimeMillis();
    }
    /**Initializes an event that executes the task with <code>delay</code> delay in millseconds
     * 
     * @param delay delay ms
     */
    public Event(long delay){
        taskDone=false;
        this.delay=delay;
        loopDelay=0;
        triggerTime=System.currentTimeMillis();
    }
    /**Initializes an event that executes the task with <code>delay</code> delay in millseconds
     * 
     * @param delay delay ms
     */
    public Event(long delay,long loopDelay){
        taskDone=false;
        this.delay=delay;
        this.loopDelay=loopDelay;
        triggerTime=System.currentTimeMillis();
    }
    /**Does task if the delay is over
     * 
     */
    public final void trigger(){
        if (System.currentTimeMillis()>=triggerTime+delay){
            task();
            taskDone=eventCompleteCondition();
            if (!taskDone){
                triggerTime=System.currentTimeMillis();
                delay=loopDelay;
            }
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
    /**Override this to make task repeat until condition in function is met
     * 
     * @return weather the complete condition has been met (defualt of true)
     */
    public boolean eventCompleteCondition(){
        return true;
    }
    /**Terminates task immediately and executes terminateTask
     * 
     */
    public final void terminate(){
        taskDone=true;
        endTask();
    }
    /**What to do when task is terminated or task is done for last time
     * 
     */
    public void endTask(){}

    public final void resetTriggerTime(){
        triggerTime=System.currentTimeMillis();
    }
}