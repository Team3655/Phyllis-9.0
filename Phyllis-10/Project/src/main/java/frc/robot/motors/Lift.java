package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import frc.robot.Robot;
import frc.robot.event.customevents.PrintEvent;

public class Lift extends CANSparkMax{
    public static enum State {activeUp,activeDown,activePID,off};
    private State state;
    private double defaultDemand;
    private double activationTime=0;
    private double targetPosition;
    private CANPIDController p;
    private CANSparkMax helper;
    public Lift(){
        super(22,MotorType.kBrushless);
        this.setIdleMode(IdleMode.kBrake);
        defaultDemand=.75;
        state=State.off;
        getEncoder().setPosition(0);
        p=getPIDController();
        p.setP(1);
        p.setD(0);
        p.setI(0);
        p.setFF(0);
        p.setOutputRange(.75, .75);
        helper=new CANSparkMax(25, MotorType.kBrushless);
        helper.follow(this);
        
    }

    @Override
    public void close(){
        super.close();
        helper.close();
    }

    /*public Lift(int id){
        super(id);
        defaultDemand=.75;
        state=State.off;
        configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        setSensorPhase(false);
        setSelectedSensorPosition(0);
        configNominalOutputForward(0);
		configNominalOutputReverse(0);
		configPeakOutputForward(defaultDemand);
        configPeakOutputReverse(-defaultDemand);
        configAllowableClosedloopError(0, 0);
        config_kF(2, 0.0);
		config_kP(2, .15);
		config_kI(2, 0.0);
		config_kD(2, 1.0);
    }
    public Lift(int id, double defaultDemand){
        super(id);
        this.defaultDemand=defaultDemand;
        state=State.off;
        configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        setSensorPhase(false);
        setSelectedSensorPosition(0);
        configNominalOutputForward(0);
		configNominalOutputReverse(0);
		configPeakOutputForward(defaultDemand);
        configPeakOutputReverse(-defaultDemand);
        configAllowableClosedloopError(0, 0);
        config_kF(2, 0.0);
		config_kP(2, .15);
		config_kI(2, 0.0);
		config_kD(2, 1.0);
    }*/
    /**Moves rear lift up at <code>defaultDemand</code> (.75 at default) percent demand
     * 
     */

    /**Sets default demand of motor
     * 
     * @param defaultDemand
     */
    public void setDefaultDemand(double defaultDemand){
        this.defaultDemand=defaultDemand;
    }

    public void up(){
        if (!(state==State.activeUp)){
            set(Math.abs(defaultDemand));
            //p.setReference(-.2, ControlType.kVelocity);
            state = State.activeUp;
            activationTime = System.currentTimeMillis();
        }
    }

    @Deprecated
    /**Moves rear lift up
     * Uses percent output for control mode of set function
     * The absolute value of <code>demand</code> is used (rear lift will only move up)
     * @param demand
     */
    public void up(double demand){
        if (!(state==State.activeUp)){
            set(Math.abs(demand));
            //p.setReference(-Math.abs(demand), ControlType.kVelocity);
            state = State.activeUp;
            activationTime = System.currentTimeMillis();
        }
    }

    @Deprecated
    /**Moves rear lift down at <code>defaultDemand</code> (.75 at default) percent demand
     * 
     */
    public void down(){
        if (!(state==State.activeDown)){
            set(-1*Math.abs(defaultDemand));
            //p.setReference(.01, ControlType.kVelocity);
            state = State.activeDown;
            activationTime = System.currentTimeMillis();
            //Robot.eHandler.triggerEvent(new PrintEvent("down no args"));
        }
    }
    /**Moves rear lift down
     * Uses percent output for control mode of set function
     * The negative absolute value of <code>demand</code> is used (rear lift will only move down)
     * @param demand
     */
    public void down(double demand){
        if (!(state==State.activeDown)){
            set(-1*Math.abs(demand));
            //p.setReference(Math.abs(demand), ControlType.kVelocity);
            state = State.activeDown;
            activationTime = System.currentTimeMillis();
            //Robot.eHandler.triggerEvent(new PrintEvent("down args"));
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