package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class ledStrip{
    Solenoid R;
    Solenoid G;
    Solenoid B;
    public ledStrip(int redPort,int greenPort,int bluePort){
        R=new Solenoid(redPort);
        G=new Solenoid(greenPort);
        B=new Solenoid(bluePort);
    }
    public void green(){
        R.set(false);
        G.set(true);
        B.set(false);
    }
    public void red(){
        R.set(true);
        G.set(false);
    }
}