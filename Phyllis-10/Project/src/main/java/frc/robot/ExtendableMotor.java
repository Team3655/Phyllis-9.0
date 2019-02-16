package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class ExtendableMotor {
    private TalonSRX motor;
    private double motorSpeed;
    private double stepLimit;
    private double maxLimit;
    public ExtendableMotor(TalonSRX motor, double stepLimit, int maxLimit) {
      this.motor = motor;
      this.stepLimit = stepLimit;
      this.maxLimit = maxLimit;
      this.motorSpeed = 0;
    }
  
    public void MotorControl(boolean buttonA, boolean buttonB, double Speed) {
  
      //Function to control intake/outtake of balls from field into/out of hopper
      if (true) { // current limit hard stop
        if(buttonA) {
          motor.set(ControlMode.PercentOutput,Speed * -1);
        } else if(buttonB) {
          motor.set(ControlMode.PercentOutput,Speed);
        } else {
          motor.set(ControlMode.PercentOutput,0);
        }
      } else {
        motor.set(ControlMode.PercentOutput,0);
      }
    }
  
    public void stepMotor(boolean buttonA, boolean buttonB) {
      //Function to ramp up shooter speed with two buttons
      //Not currently being used (more of a diagnostic)
      if (true) { // current limit hard stop
        double speed = this.motorSpeed;
        if(buttonA) {
          speed -= this.stepLimit;
        } else if(buttonB){
          speed += this.stepLimit;
        }
        if((speed >= -1*this.maxLimit)  && (speed <= this.maxLimit)) {
          this.motorSpeed = speed;
          motor.set(ControlMode.PercentOutput,this.motorSpeed);
        }
      } else {
        motor.set(ControlMode.PercentOutput,0);
      }
    }
  }