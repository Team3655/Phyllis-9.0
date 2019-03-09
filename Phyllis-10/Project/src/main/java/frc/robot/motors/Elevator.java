package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXPIDSetConfiguration;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.PIDController;

public class Elevator extends CANSparkMax {
    public static enum State {activeUp,activeDown,activePID,off};
    private State state;
    private double defaultDemand;
    private double activationTime=0;
    private double targetPosition;
    private CANPIDController p;
    /**Constructs a new elevator with an id of 21 and a default demand percent of .75
     * 
     */
    public Elevator(){
        super(21,MotorType.kBrushless);
        defaultDemand=.75;
        state=State.off;
        getEncoder().setPosition(0);
        p=getPIDController();
        p.setP(1);
        p.setD(0);
        p.setI(0);
        p.setFF(0);
        p.setOutputRange(.75, .75);
    }
    /**Constructs a new elevator with an id of <code>id</code> and a default demand percent of .75
     * 
     * @param id
     */
    public Elevator(int id){
        super(id,MotorType.kBrushless);
        defaultDemand=.75;
        state=State.off;
        getEncoder().setPosition(0);
        p=getPIDController();
        p.setP(1);
        p.setD(0);
        p.setI(0);
        p.setFF(0);
        p.setOutputRange(.75, .75);
    }
    /**Constructs a new elevator with an id of 21 and a default demand percent of <code>defaultDemand</code>
     * 
     * @param defaultDemand
     */
    public Elevator(double defaultDemand){
        super(21,MotorType.kBrushless);
        this.defaultDemand=defaultDemand;
        state=State.off;
        getEncoder().setPosition(0);
        p=getPIDController();
        p.setP(1);
        p.setD(0);
        p.setI(0);
        p.setFF(0);
        p.setOutputRange(defaultDemand, defaultDemand);
    }
    /**Constructs a new elevator with an id of <code>id</code> and a default demand percent of <code>defaultDemand</code>
     * 
     * @param id
     * @param defaultDemand
     */
    public Elevator(int id, double defaultDemand){
        super(id,MotorType.kBrushless);
        this.defaultDemand=defaultDemand;
        state=State.off;
        getEncoder().setPosition(0);
        p=getPIDController();
        p.setP(1);
        p.setD(0);
        p.setI(0);
        p.setFF(0);
        p.setOutputRange(defaultDemand, defaultDemand);
    }
    /**Sets default demand of motor
     * 
     * @param defaultDemand
     */
    public void setDefaultDemand(double defaultDemand){
        this.defaultDemand=defaultDemand;
    }
    /**Moves elevator up at <code>defaultDemand</code> (.75 at default) percent demand
     * 
     */
    public void up(){
        if (!(state==State.activeUp)){
            set(Math.abs(defaultDemand));
            state = State.activeUp;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves elevator up
     * Uses percent output for control mode of set function
     * The absolute value of <code>demand</code> is used (elevator will only move up)
     * @param demand
     */
    public void up(double demand){
        if (!(state==State.activeUp)){
            set(Math.abs(demand));
            state = State.activeUp;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves elevator down at <code>defaultDemand</code> (.75 at default) percent demand
     * 
     */
    public void down(){
        if (!(state==State.activeDown)){
            set(-1*Math.abs(defaultDemand));
            state = State.activeDown;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves elevator down
     * Uses percent output for control mode of set function
     * The negative absolute value of <code>demand</code> is used (elevator will only move down)
     * @param demand
     */
    public void down(double demand){
        if (!(state==State.activeDown)){
            set(-1*Math.abs(demand));
            state = State.activeDown;
            activationTime = System.currentTimeMillis();
        }
    }
    public void off(){
        set(0);
        state = State.off;
    }
    public void moveToPos(int pos){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(defaultDemand), Math.abs(defaultDemand));
        state=State.activePID;
        p.setReference(pos,ControlType.kPosition);
    }
    public void moveToPos(double pos){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(defaultDemand), Math.abs(defaultDemand));
        state=State.activePID;
        p.setReference(pos,ControlType.kPosition);
    }
    public void moveToPos(double pos,double demand){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(demand), Math.abs(demand));
        state=State.activePID;
        p.setReference(pos,ControlType.kPosition);
    }
    public void printSensorPosition(){
        System.out.println("Sensor Position: "+getEncoder());
        if (state==State.activePID) System.out.println("Target Position: "+targetPosition);
    }
    public double getActivationTime(){
        return activationTime;
    }
    public State getState(){
        return state;
    }
}