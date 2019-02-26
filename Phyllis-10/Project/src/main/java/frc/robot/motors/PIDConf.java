package frc.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class PIDConf{
    private final TalonSRX motor;
    private int idx;
    private int timeoutMs;
    private double peakDemandF;
    private double peakDemandB;
    private double nominalDemandF;
    private double nominalDemandB;
    private boolean sensorPhase;
    public PIDConf(TalonSRX motor){
        sensorPhase=false;
        idx=0;
        timeoutMs=30;
        peakDemandB=.75;
        peakDemandF=.75;
        nominalDemandB=0;
        nominalDemandF=0;
        sensorPhase=false;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,boolean sensorPhase){
        sensorPhase=sensorPhase;
        idx=0;
        timeoutMs=30;
        peakDemandB=.75;
        peakDemandF=.75;
        nominalDemandB=0;
        nominalDemandF=0;
        this.sensorPhase=sensorPhase;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,double peakDemand){
        sensorPhase=false;
        idx=0;
        timeoutMs=30;
        peakDemandB=peakDemand;
        peakDemandF=peakDemand;
        nominalDemandB=0;
        nominalDemandF=0;
        sensorPhase=false;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,double peakDemandF,double peakDemandB){
        sensorPhase=false;
        idx=0;
        timeoutMs=30;
        this.peakDemandB=peakDemandB;
        this.peakDemandF=peakDemandF;
        nominalDemandB=0;
        nominalDemandF=0;
        sensorPhase=false;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,double peakDemandF,double peakDemandB,boolean sensorPhase){
        sensorPhase=false;
        idx=0;
        timeoutMs=30;
        this.peakDemandB=peakDemandB;
        this.peakDemandF=peakDemandF;
        nominalDemandB=0;
        nominalDemandF=0;
        this.sensorPhase=sensorPhase;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,double peakDemandF,double peakDemandB,boolean sensorPhase,double nominalDemand){
        sensorPhase=false;
        idx=0;
        timeoutMs=30;
        this.peakDemandB=peakDemandB;
        this.peakDemandF=peakDemandF;
        nominalDemandB=nominalDemand;
        nominalDemandF=nominalDemand;
        this.sensorPhase=sensorPhase;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,double peakDemandF,double peakDemandB,boolean sensorPhase,double nominalDemandF,double nominalDemandB){
        sensorPhase=false;
        idx=0;
        timeoutMs=30;
        this.peakDemandB=peakDemandB;
        this.peakDemandF=peakDemandF;
        this.nominalDemandB=nominalDemandB;
        this.nominalDemandF=nominalDemandF;
        this.sensorPhase=sensorPhase;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,double peakDemandF,double peakDemandB,boolean sensorPhase,double nominalDemandF,double nominalDemandB,int timeoutMs){
        sensorPhase=false;
        idx=0;
        this.timeoutMs=timeoutMs;
        this.peakDemandB=peakDemandB;
        this.peakDemandF=peakDemandF;
        this.nominalDemandB=nominalDemandB;
        this.nominalDemandF=nominalDemandF;
        this.sensorPhase=sensorPhase;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,double peakDemandF,double peakDemandB,boolean sensorPhase,double nominalDemandF,double nominalDemandB,int timeoutMs,int idx){
        sensorPhase=false;
        this.idx=0;
        this.timeoutMs=timeoutMs;
        this.peakDemandB=peakDemandB;
        this.peakDemandF=peakDemandF;
        this.nominalDemandB=nominalDemandB;
        this.nominalDemandF=nominalDemandF;
        this.sensorPhase=sensorPhase;
        this.motor=motor;
        init();
    }
    public PIDConf(TalonSRX motor,int idx){
        sensorPhase=false;
        this.idx=idx;
        timeoutMs=30;
        peakDemandB=.75;
        peakDemandF=.75;
        nominalDemandB=0;
        nominalDemandF=0;
        sensorPhase=false;
        this.motor=motor;
        init();
    }

    private void init(){
        /* Config the sensor used for Primary PID and sensor direction */
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,30);
        /* Set sensor deriction to default false */
        motor.setSensorPhase(sensorPhase);
        motor.configNominalOutputForward(nominalDemandF);
        motor.configNominalOutputReverse(nominalDemandB);
        /* Config the peak and nominal outputs, 12V means full */
        motor.configPeakOutputForward(peakDemandF);
        motor.configPeakOutputReverse(peakDemandB);
        /* Set the quadrature (relative) sensor to match absolute */
		motor.setSelectedSensorPosition(getEncoderPosition(), idx, timeoutMs);

    }


    public int getEncoderPosition(){
        /**
		 * Grab the 360 degree position of the MagEncoder's absolute
		 * position, and intitally set the relative sensor to match.
		 */
        int absolutePosition = motor.getSensorCollection().getPulseWidthPosition();
        /* Mask out overflows, keep bottom 12 bits */
		absolutePosition &= 0xFFF;
        if (motor.getInverted()) { absolutePosition *= -1; }
        if (sensorPhase) { absolutePosition *= -1;}
        return absolutePosition;
    }


    public void moveRotations(double r){
        motor.set(ControlMode.Position,r*4096);
    }
    public void moveRotations(double r, double demand){
        motor.configPeakOutputForward(demand);
        motor.configPeakOutputReverse(-demand);
        motor.set(ControlMode.Position,r*4096);
    }
}