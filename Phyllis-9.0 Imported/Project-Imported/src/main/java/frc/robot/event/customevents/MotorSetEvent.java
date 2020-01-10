package frc.robot.event.customevents;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Talon;
import frc.robot.event.Event;

public class MotorSetEvent extends Event {
    private CANSparkMax motorC;
    private Talon motorT;
    private enum MotorType{Spark,Talon};
    private MotorType motorType;
    private double demand;
    public MotorSetEvent(CANSparkMax motor,double demand,long delay){
        super(delay);
        motorType=MotorType.Spark;
        motorC=motor;
    }
    public MotorSetEvent(Talon motor,double demand,long delay){
        super(delay);
        motorType=MotorType.Talon;
        motorT=motor;
    }
    public MotorSetEvent(CANSparkMax motor,double demand){
        super();
        motorType=MotorType.Spark;
        motorC=motor;
    }
    public MotorSetEvent(Talon motor,double demand){
        super();
        motorType=MotorType.Talon;
        motorT=motor;
    }

    public void task(){
        if (motorType==MotorType.Spark){
            motorC.set(demand);
        } else {
            motorT.set(demand);
        }
    }
}