package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
/**
 * Handles button presses without commands (specify actions in an extended class)
 */
abstract class ButtonHandler {
    private GenericHID buttonInterface;
    private int buttonNo;
    /**Initializes a button handler with specified numberof buttons
     * 
     * @param buttonInterface
     * @param buttonNo Number of buttons on button interface
     */
    public ButtonHandler(GenericHID buttonInterface,int buttonNo){
        this.buttonInterface=buttonInterface;
        this.buttonNo=buttonNo;
    }
    /**Call during a periodic function in order to regocnize button events
     * 
    */
    public void update(){
        for (int i=1; i<=buttonNo; i++){
            if (buttonInterface.getRawButtonPressed(i)){
                buttonPressed(i);
            } else if (buttonInterface.getRawButtonReleased(i)){
                buttonReleased(i);
            }
        }
        
    }
    public boolean getButtonDown(int no){
        return buttonInterface.getRawButton(no);
    }
    /**
     * Is called in update function when a button is pressed
     * @param no the button that was pressed
     */
    abstract void buttonPressed(int no);
    /**
     * Is called in update function when a button is released
     * @param no
     */
    abstract void buttonReleased(int no);

}