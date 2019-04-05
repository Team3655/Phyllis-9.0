package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXPIDSetConfiguration;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.PIDController;
import frc.robot.Robot;
import frc.robot.event.customevents.PrintEvent;

public class Elevator extends CANSparkMax {
    public static enum State {active,activeUp,activeDown,activePID,off};
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
        p.setP(1,0);
        p.setD(0,0);
        p.setI(0,0);
        p.setFF(0,0);
        p.setOutputRange(-.75, .75);
        p.setP(1,1);
        p.setD(0,1);
        p.setI(0,1);
        p.setFF(0,1);
        p.setOutputRange(-.75, .75,1);
        p.setOutputRange(-.75, .75,0);
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
    @Override
    public void set(double speed) {
        super.set(speed);
        state=State.active;
        activationTime = System.currentTimeMillis();
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
        p.setReference(pos,ControlType.kPosition,1);
    }
    public void moveToPos(double pos,double demand){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(demand), Math.abs(demand));
        state=State.activePID;
        p.setReference(pos,ControlType.kPosition,1);
    }
    public void moveToPos(double pos,double demandDown,double demandUp){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(demandDown), Math.abs(demandUp));
        state=State.activePID;
        p.setReference(pos,ControlType.kPosition,1);
    }
    public void moveToPos(double pos,double demandDown,double demandUp,int PIDSlot){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(demandDown), Math.abs(demandUp));
        state=State.activePID;
        p.setReference(pos,ControlType.kPosition,PIDSlot);
    }
    public void smartPos(double pos){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(Robot.getInstance().getTuningValue("eSpdDow")), Math.abs(Robot.getInstance().getTuningValue("eSpdUp")));
        state=State.activePID;
        p.setSmartMotionAllowedClosedLoopError(Robot.getInstance().getTuningValue("eErr"), 0);
        p.setSmartMotionMaxAccel(Robot.getInstance().getTuningValue("eSpdRPM"), 0);
        p.setSmartMotionMaxVelocity(Robot.getInstance().getTuningValue("eAclRPMS"), 0);
        p.setReference(pos,ControlType.kSmartMotion,0);
    }
    public void smartPos(double pos, double maxAccel,double maxVelocity){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(Robot.getInstance().getTuningValue("eSpdDow")), Math.abs(Robot.getInstance().getTuningValue("eSpdUp")));
        state=State.activePID;
        p.setSmartMotionMaxAccel(Robot.getInstance().getTuningValue("eSpdRPM"), 0);
        p.setSmartMotionMaxVelocity(Robot.getInstance().getTuningValue("eAclRPMS"), 0);
        p.setReference(pos,ControlType.kSmartMotion,0);
    }
    public void smartPos(double pos, double maxAccel,double maxVelocity,double demandUp,double demandDown){
        set(0);
        targetPosition=pos;
        p.setOutputRange(-1*Math.abs(demandDown), Math.abs(demandUp));
        state=State.activePID;
        p.setSmartMotionMaxAccel(maxAccel, 0);
        p.setSmartMotionMaxVelocity(maxVelocity, 0);
        p.setReference(pos,ControlType.kSmartMotion,0);
    }
    public void printSensorPosition(){
        Robot.eHandler.triggerEvent(new PrintEvent("Sensor Position: "+getEncoder().getPosition()));
        if (state==State.activePID) Robot.eHandler.triggerEvent(new PrintEvent("Target Position: "+targetPosition));
    }
    public double getActivationTime(){
        return activationTime;
    }
    public State getState(){
        return state;
    }
}