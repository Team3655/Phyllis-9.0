package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift extends TalonSRX{
    public static enum State {activeUp,activeDown,off};
    private State state;
    private double defaultDemand;
    private double activationTime=0;
    public Lift(){
        super(22);
        defaultDemand=.75;
        state=State.off;
    }
    public Lift(int id){
        super(id);
        defaultDemand=.75;
        state=State.off;
    }
    public Lift(int id, double defaultDemand){
        super(id);
        this.defaultDemand=defaultDemand;
        state=State.off;
    }
    /**Moves rear lift up at <code>defaultDemand</code> (.75 at default) percent demand
     * 
     */
    public void up(){
        if (!(state==State.activeUp)){
            set(ControlMode.PercentOutput, defaultDemand);
            state = State.activeUp;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves rear lift up
     * Uses percent output for control mode of set function
     * The absolute value of <code>demand</code> is used (rear lift will only move up)
     * @param demand
     */
    public void up(double demand){
        if (!(state==State.activeUp)){
            set(ControlMode.PercentOutput, Math.abs(demand));
            state = State.activeUp;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves rear lift up with control mode <code>Mode</code>
     * The absolute value of <code>demand</code> is used (rear lift will only move up)
     * @param demand
     * @param Mode
     */
    public void up(ControlMode Mode, double demand){
        if (!(state==State.activeUp)){
            set(Mode, Math.abs(demand));
            state=State.activeUp;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves rear lift down at <code>defaultDemand</code> (.75 at default) percent demand
     * 
     */
    public void down(){
        if (!(state==State.activeDown)){
            set(ControlMode.PercentOutput,-1*Math.abs(defaultDemand));
            state = State.activeDown;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves rear lift down
     * Uses percent output for control mode of set function
     * The negative absolute value of <code>demand</code> is used (rear lift will only move down)
     * @param demand
     */
    public void down(double demand){
        if (!(state==State.activeDown)){
            set(ControlMode.PercentOutput,-1*Math.abs(demand));
            state = State.activeDown;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves rear lift down with control mode <code>Mode</code>
     * The negative absolute value of <code>demand</code> is used (rear lift will only move down)
     * @param demand
     * @param Mode
     */
    public void down(ControlMode Mode, double demand){
        if (!(state==State.activeDown)){
            set(Mode, -1*Math.abs(demand));
            state = State.activeDown;
            activationTime = System.currentTimeMillis();
        }
    }
    public void off(){
        set(ControlMode.PercentOutput,0);
        state = State.off;
    }
    public double getActivationTime(){
        return activationTime;
    }
    public State getState(){
        return state;
    }
}