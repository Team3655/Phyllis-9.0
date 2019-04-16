package tractorTech.buttons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
/**
 * Handles button presses without commands (specify actions in an extended class)
 */
public abstract class ButtonHandler /*extends Thread*/{
    private boolean enabled;
    private GenericHID buttonInterface;
    private int buttonNo;
    /**Initializes a button handler with specified numberof buttons
     * 
     * @param buttonInterface
     * @param buttonNo Number of buttons on button interface
     */
    public ButtonHandler(GenericHID buttonInterface,int buttonNo){
        //this.setName(this.getClass().toString());
        this.buttonInterface=buttonInterface;
        this.buttonNo=buttonNo;
        enabled=true;
        //start();
    }

    /*@Override
    public void run(){
        while (enabled){
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            update();
        }
    }*/
    /**Call during a periodic function in order to regocnize button events
     * 
    */
    public void update(){
        for (int i=1; i<=buttonNo; i++){
            if (buttonInterface.getRawButton(i)){
                if (buttonInterface.getRawButtonPressed(i)){
                    buttonPressed(i);
                }
                buttonDown(i);
            } else if (buttonInterface.getRawButtonReleased(i)){
                buttonReleased(i);
            }
        }
        
    }
    public final boolean getButtonDown(int no){
        return buttonInterface.getRawButton(no);
    }
    /**
     * Is called in update function when a button is pressed
     * @param no the button that was pressed
     */
    protected abstract void buttonPressed(int no);
    /**
     * Is called in update function when a button is released
     * @param no
     */
    protected abstract void buttonReleased(int no);
    /**
     * Is called in update function when a button is down
     * @param no the button that was pressed
     */
    protected abstract void buttonDown(int no);
    public final double getY(){
        return buttonInterface.getY();
    }
    public final double getX(){
        return buttonInterface.getX();
    }
    public Joystick getJoystick(){
        return (Joystick) buttonInterface;
    }

    public void simButtonPress(int no){
        buttonPressed(no);
    }

    public void simButtonRelease(int no){
        buttonReleased(no);
    }
    public void disable(){
        enabled=false;
    }
}