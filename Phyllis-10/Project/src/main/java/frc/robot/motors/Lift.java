package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

public class Lift extends CANSparkMax{
    public static enum State {activeUp,activeDown,off};
    private State state;
    private double defaultDemand;
    private double activationTime=0;
    public Lift(){
        super(22,MotorType.kBrushless);
        defaultDemand=.75;
        state=State.off;
        getEncoder().setPosition(0);
        CANPIDController p=getPIDController();
        p.setP(1);
        p.setD(0);
        p.setI(0);
        p.setFF(0);
        p.setOutputRange(-.5, .5);
        //leftBackCAN.setParameter(ConfigParameter.), value)
        p.setReference(10.75, ControlType.kPosition);
    
    
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
    public void up(){
        if (!(state==State.activeUp)){
            set(Math.abs(defaultDemand));
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
            set(Math.abs(demand));
            state = State.activeUp;
            activationTime = System.currentTimeMillis();
        }
    }
    /**Moves rear lift down at <code>defaultDemand</code> (.75 at default) percent demand
     * 
     */
    public void down(){
        if (!(state==State.activeDown)){
            set(-1*Math.abs(defaultDemand));
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
        configPeakOutputForward(defaultDemand);
        configPeakOutputReverse(-defaultDemand);
        if (pos>getSelectedSensorPosition()){
            state=State.activeUp;
        } else if(pos<getEncoder.pos){
            state=State.activeDown;
        } else {
            state=State.off;
        }
        getPIDController().setReference(pos,ControlType.kPosition);
    }
    public void moveToPos(double pos){
        configPeakOutputForward(defaultDemand);
        configPeakOutputReverse(-defaultDemand);
        if (pos>getSelectedSensorPosition()){
            state=State.activeUp;
        } else if(pos<getSelectedSensorPosition()){
            state=State.activeDown;
        } else {
            state=State.off;
        }
        set(ControlMode.Position,pos);
    }
    public void moveToPos(double pos,double demand){
        configPeakOutputForward(demand);
        configPeakOutputReverse(-demand);
        if (pos>getSelectedSensorPosition()){
            state=State.activeUp;
        } else if(pos<getSelectedSensorPosition()){
            state=State.activeDown;
        } else {
            state=State.off;
        }
        set(ControlMode.Position,pos);
    }
    public void printSensorPosition(){
        System.out.println("Sensor Position: "+getSelectedSensorPosition());
        System.out.println("Target Position: "+getClosedLoopTarget(0));
    }
    public double getActivationTime(){
        return activationTime;
    }
    public State getState(){
        return state;
    }
}