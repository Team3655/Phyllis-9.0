package frc.robot.buttons;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;
/**Tractor Simulator Button Adapter for long
 * 
 */
public class TSBAdapter extends ButtonHandler{
    Robot robot;
    public TSBAdapter(Joystick tractorPanel, Robot robot){
        super(tractorPanel,28); //button 28 is the red button on the joystick and button 27 is press on wheel (those buttons aren't labled on the panel)
        this.robot=robot;
    }
    public void buttonPressed(int no){
        switch (no){
            //button 1 moves elevator up
            case 1:
                robot.elevatorUp();
            break;
            //button 6 moves elevator down
            case 6:
                robot.elevatorDown();
            break;
            //button 2 turns off elevator, mostly won't be used but we're gonna keep it in case of emergency (failure to disable elevator upon voltage spike or critical elevator damage)
            case 2:
                robot.elevatorOff();
            break;
        }
    }
    public void buttonReleased(int no){
    }
}