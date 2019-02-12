package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
/**
 * Handles button presses without commands (specify actions in an extended class)
 */
abstract class ButtonHandler {
    private GenericHID joystick;
    public ButtonHandler(GenericHID joystick){
        this.joystick=joystick;
    }
    public void update(){
        for (int i=1; i<12; i++){
            if (joystick.getRawButtonPressed(i)){
                buttonPressed(i);
            } else if (joystick.getRawButtonReleased(i)){
                buttonReleased(i);
            }
            i++;
        }
        
    }
    public boolean getButtonDown(int no){
        return joystick.getRawButton(no);
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