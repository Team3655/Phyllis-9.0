package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Iotake {
    public static enum State {activeIn,activeOut,off};
    private State state;
    private TalonSRX left;
    private TalonSRX right;
    private double defaultDemand;
    private double activationTime=0;
    /**Constructs a new intake/outtake system with default motor ids and default <code>defaultDemand</code>
     * Left motor id: 31
     * Right motor id: 32
     * defaultDemand: .3
     */
    public Iotake(){
        left=new TalonSRX(31);
        right=new TalonSRX(32);
        left.setNeutralMode(NeutralMode.Brake);
        right.setNeutralMode(NeutralMode.Brake);
        right.setInverted(true);//actual robot
        //left.setInverted(true);//practice robot
        defaultDemand = .75;
        state = State.off;
    }
    /**Constructs a new intake/outtake system with default motor ids and specified <code>defaultDemand</code>
     * Left motor id: 31
     * Right motor id: 32
     * 
     */
    public Iotake(double defaultDemand){
        left=new TalonSRX(31);
        right=new TalonSRX(32);
        left.setNeutralMode(NeutralMode.Brake);
        right.setNeutralMode(NeutralMode.Brake);
        this.defaultDemand = defaultDemand;
        state = State.off;
    }
    /**Constructs a new intake/outtake system with specified motor ids and default <code>defaultDemand</code>
     * defaultDemand: .3
     * 
     * @param idLeft
     * @param idRight
     */
    public Iotake(int idLeft,int idRight){
        left = new TalonSRX(idLeft);
        right = new TalonSRX(idRight);
        left.setNeutralMode(NeutralMode.Brake);
        right.setNeutralMode(NeutralMode.Brake);
        defaultDemand = .3;
        state = State.off;
    }
    /**Constructs a new intake/outtake system with specified motor ids and specified <code>defaultDemand</code>
     * 
     * @param idLeft
     * @param idRight
     * @param defaultDemand
     */
    public Iotake(int idLeft,int idRight,double defaultDemand){
        left = new TalonSRX(idLeft);
        right = new TalonSRX(idRight);
        left.setNeutralMode(NeutralMode.Brake);
        right.setNeutralMode(NeutralMode.Brake);
        this.defaultDemand=defaultDemand;
        state = State.off;
    }
    /**Sets default demand of motor
     * 
     * @param defaultDemand
     */
    public void setDefaultDemand(double defaultDemand){
        this.defaultDemand=defaultDemand;
    }
    //sorry this isn't commented as well as elevator
    public void intake(){
        if (!(state==State.activeIn)){
            //necessary so pushing button multiple times doesn't screw up activation time
            left.set(ControlMode.PercentOutput,-1*Math.abs(defaultDemand));
            right.set(ControlMode.PercentOutput,-1*Math.abs(defaultDemand));
            activationTime=System.currentTimeMillis();
            state=State.activeIn;
        }
    }
    public void intake(double demand){
        //necessary so pushing button multiple times doesn't screw up activation time
        if (!(state==State.activeIn)){
            left.set(ControlMode.PercentOutput,-1*Math.abs(demand));
            right.set(ControlMode.PercentOutput,-1*Math.abs(demand));
            activationTime=System.currentTimeMillis();
            state=State.activeIn;
        }
    }
    public void intake(ControlMode Mode, double demand){
        //necessary so pushing button multiple times doesn't screw up activation time
        if (!(state==State.activeIn)){
            left.set(Mode,-1*Math.abs(demand));
            right.set(Mode,-1*Math.abs(demand));
            activationTime=System.currentTimeMillis();
            state=State.activeIn;
        }
    }
    public void outtake(){
        //necessary so pushing button multiple times doesn't screw up activation time
        if (!(state==State.activeOut)){
            left.set(ControlMode.PercentOutput,Math.abs(defaultDemand));
            right.set(ControlMode.PercentOutput,Math.abs(defaultDemand));
            activationTime=System.currentTimeMillis();
            state=State.activeOut;
        }
    }
    public void outtake(double demand){
        //necessary so pushing button multiple times doesn't screw up activation time
        if (!(state==State.activeOut)){
            left.set(ControlMode.PercentOutput,Math.abs(demand));
            right.set(ControlMode.PercentOutput,Math.abs(demand));
            activationTime=System.currentTimeMillis();
            state=State.activeOut;
        }
    }
    public void outtakeRight(double demand){
        //necessary so pushing button multiple times doesn't screw up activation time
        if (!(state==State.activeOut)){
            right.set(ControlMode.PercentOutput,Math.abs(demand));
            activationTime=System.currentTimeMillis();
            state=State.activeOut;
        }
    }
    public void outtakeLeft(double demand){
        //necessary so pushing button multiple times doesn't screw up activation time
        if (!(state==State.activeOut)){
            left.set(ControlMode.PercentOutput,Math.abs(demand));
            activationTime=System.currentTimeMillis();
            state=State.activeOut;
        }
    }
    public void outtake(ControlMode Mode, double demand){
        //necessary so pushing button multiple times doesn't screw up activation time
        if (!(state==State.activeOut)){
            left.set(Mode,Math.abs(demand));
            right.set(Mode,Math.abs(demand));
            activationTime=System.currentTimeMillis();
            state=State.activeOut;
        }
    }
    /**Stops iostate systems
     * 
     */
    public void off(){
        left.set(ControlMode.PercentOutput,0);
        right.set(ControlMode.PercentOutput,0);
        state=State.off;
    }
    /**Returns the state of this motorgroup
     * @return state of this motorgroup
     */
    public State getState(){
        return state;
    }
    public double getOutputCurrent(){
        return left.getOutputCurrent();
    }
    //testing function
    public double getMotorOutputVoltageLeft(){
        return left.getMotorOutputVoltage();
    }
    public double getActivationTime(){
        return activationTime;
    }
}