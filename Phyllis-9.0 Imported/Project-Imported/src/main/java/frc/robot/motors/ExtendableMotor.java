package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;


public class ExtendableMotor {
  public enum State{stepUp,stepDown,offFromUp,offFromDown,off};
  private State state;
  private TalonSRX motor;
  private double demand;
  private double stepLimit;
  private double maxLimit;
  public ExtendableMotor(TalonSRX motor, double stepLimit, int maxLimit) {
    this.motor = motor;
    this.stepLimit = stepLimit;
    this.maxLimit = maxLimit;
    demand = 0;
    state=State.off;
  }
  public void update(){
      if(state==State.stepUp&&demand<maxLimit) {
        demand += this.stepLimit;
      } else if (state==State.stepDown&&demand>-1*maxLimit) {
        demand -= this.stepLimit;
      } else if (state==State.offFromUp&&demand>0){
        demand -=stepLimit;
      } else if (state==State.offFromUp&&demand<=0){
        state=State.off;
        demand=0;
      } else if (state==State.offFromDown&&demand<0){
        demand+=stepLimit;
      } else if (state==State.offFromDown&&demand>=0){
        state=State.off;
        demand=0;
      }
      motor.set(ControlMode.PercentOutput,demand);
  }
    
    public void setState(State state) {
      //Function to ramp up shooter speed with two buttons
      //Not currently being used (more of a diagnostic)
      if (this.state==State.stepDown&&state==State.off) {
        this.state=State.offFromDown;
      } else if (this.state==State.stepUp&&state==State.off){
        this.state=State.offFromUp;
      } else if (state==State.offFromUp||state==State.offFromDown){
        //do nothing
      } else {
        this.state=state;
      }      
    }
      
  }