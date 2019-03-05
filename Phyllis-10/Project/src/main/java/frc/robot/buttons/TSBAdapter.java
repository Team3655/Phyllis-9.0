package frc.robot.buttons;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;
import frc.robot.motors.Elevator;
/**Tractor Simulator Button Adapter for long
 * 
 */
public class TSBAdapter extends ButtonHandler{
    private Robot robot;
    private enum Mode{RobotResponse,Tune};
    private Mode mode;
    private String[] tuningValues={"eTop","eBot","eMid","eDec","aHat","aBal","aSit","aDec","lTop","lBot"};
    private int currentPropertyNo;
    private String currentTuningValue;
    private String inputCache;
    public TSBAdapter(Joystick tractorPanel, Robot robot){
        super(tractorPanel,28); //button 28 is the red button on the joystick and button 27 is press on wheel (those buttons aren't labled on the panel)
        this.robot=robot;
        mode=Mode.RobotResponse;
        currentPropertyNo=0;
        currentTuningValue=tuningValues[currentPropertyNo];
        inputCache="";
    }
    public void buttonPressed(int no){
        if (mode==Mode.RobotResponse){
            switch (no){
                //button 1 moves elevator up
                case 1:
                    robot.elevatorUp();
                break;
                //button 6 moves elevator down
                case 6:
                    robot.elevatorDown();
                break;
                //button 2 moves elevator to bottom
                /*case 2:
                    robot.elevatorBottom();
                break;
                //button 3 moves elevator to middle
                case 3:
                    robot.elevatorMid();
                break;
                //button 7 moves elevator to top
                case 7:
                    robot.elevatorTop();
                break;*/
                //button 4 moves lift up
                case 4:
                    robot.liftUp();;
                break;
                //button 9 moves lift down
                case 9:
                    robot.liftDown();
                break;
                //button 5 puts lift in raised position
                /*case 5:
                    robot.liftUp();
                break;
                //button 10 puts lift in lowered position
                case 10:
                    robot.liftLower();
                break;*/
                //button 11 moves arm up
                case 11:
                    robot.armUp();
                break;
                //button 12 moves arm down
                case 12:
                    robot.armDown();
                break;
                //button 13 puts arm in ball position
                /*case 13:
                    robot.armBall();
                break;
                //button 15 puts arm in hatch position
                case 15:
                    robot.armHatch();
                break;
                //button 14 puts arm at deck height
                case 14:
                    robot.armDeck();
                break;
                //button 16 puts arm at sit height
                case 16:
                    robot.armSit();
                break;*/
                //button 17 initiates intake
                case 17:
                    robot.intake();
                break;
                //button 18 outakes (shoots)
                case 18:
                    robot.outtake();
                break;
                //button 20 fires a loaded hatch
                case 20:
                    robot.fireHatch(true);
                break;
                //button 21 toggles the compressor
                case 21:
                    robot.toggleCompressor();
                break;
                //button 22 turns off elevator, mostly won't be used but we're gonna keep it in case of emergency (failure to disable elevator upon voltage spike or critical elevator damage)
                case 22:
                    robot.elevatorOff();
                    robot.armOff();
                    robot.liftOff();
                    robot.fireHatch(false);
                    robot.iotakeOff();
                break;
                //button 23 prints the sensor locatations of sensored motors
                case 23:
                    robot.printSensorPositions();
                break;
                case 28:
                    mode=Mode.Tune;
                    System.out.println("Mode set to 'Tune'");
                break;
            }
        } else {
            if (no<10){
                inputCache=inputCache+no;
                System.out.println("Input Cache: "+inputCache);
            } else {
                switch (no){
                    case 10:
                        inputCache=inputCache+0;
                        System.out.println("Input Cache: "+inputCache);
                    break;
                    case 11:
                        inputCache=inputCache.substring(0, inputCache.length()-1);
                        System.out.println("Input Cache: "+inputCache);
                    break;
                    case 12:
                        if (!inputCache.contains(".")){
                            inputCache=inputCache+".";
                            System.out.println("Input Cache: "+inputCache);
                        }
                    break;
                    case 17:
                        if (!inputCache.contains("-")){
                            inputCache="-"+inputCache;
                        } else {
                            inputCache=inputCache.substring(1);
                        }
                    break;
                    //Button 21 set value to input
                    case 21:
                        try {
                            robot.setTuningValue(currentTuningValue, Double.parseDouble(inputCache));
                            System.out.println(currentTuningValue+" set to "+inputCache);
                            inputCache="";
                        } catch (NumberFormatException e){
                            //robot.setProp(currentTuningValue, 0);
                            System.err.println("User did not enter a number");
                            //System.err.println(currentTuningValue+" defaulted to 0");
                            //inputCache="";
                        }
                    break;
                    case 25:
                        System.out.println("Current value of "+currentTuningValue+": "+robot.getTuningValue(currentTuningValue));
                    break;
                    //button 26 changes what property you are editing (++)
                    case 27:
                        currentPropertyNo++;
                        if (currentPropertyNo>9){
                            currentPropertyNo=0;
                        }
                        currentTuningValue=tuningValues[currentPropertyNo];
                        System.out.println("Now edititing "+currentTuningValue);
                    break;
                    //button 26 changes what property you are editing (--)
                    case 26:
                        currentPropertyNo--;
                        if (currentPropertyNo<0){
                            currentPropertyNo=9;
                        }
                        currentTuningValue=tuningValues[currentPropertyNo];
                        System.out.println("Now edititing "+currentTuningValue);
                    break;
                    case 28:
                        mode=Mode.RobotResponse;
                        System.out.println("Mode set to 'RobotResponse'");
                    break;
                }
            }
        }
    }
    public void buttonReleased(int no){
        switch (no){
            /*case 20:
            robot.fireHatch(false);
            break;*/
            //button 1 moves elevator up
            case 1:
                robot.elevatorOff();
            break;
            //button 6 moves elevator down
            case 6:
                robot.elevatorOff();
            break;
            case 4:
                robot.liftOff();
            break;
            case 9:
                robot.liftOff();
            break;
            case 11:
                robot.armOff();
            break;
            case 12:
                robot.armOff();
            break;
            case 17:
                robot.iotakeOff();
            break;
            case 18:
                robot.iotakeOff();
            break;
            
        }
    }
}