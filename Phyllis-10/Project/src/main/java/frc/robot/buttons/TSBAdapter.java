package frc.robot.buttons;



import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;
import frc.robot.event.Event;
import frc.robot.event.EventSequence;
import frc.robot.event.customevents.ArmHoldPosEvent;
import frc.robot.event.customevents.DiskPushEvent;
import frc.robot.event.customevents.PrintEvent;
import frc.robot.motors.Elevator;
/**Tractor Simulator Button Adapter for long
 * 
 */
public class TSBAdapter extends ButtonHandler{
    private Robot robot;
    public enum Mode{RobotResponse,Tune,RobotRecord};
    private enum ControlMode{Joystick,PID};
    private ControlMode elevatorControlMode;
    private ControlMode armControlMode;
    private Mode mode;
    private String[] tuningValues={"eTop","eBot","eMid","eCar","eHat","aCar","aHat","aBal","aSit","aDec","lTop","lBot","aPIDOR","lPIDOR","ePIDORUp","ePIDORDow","eSpdRPM","eAclRPMS","eSpdUp","eSpdDow","lSpdRPM","lAclRPMS","lSpdUp","lSpdDow","eSpdJ","aSpd","aSpdJ","iSpd","eCurUp","eCurDow","eCurPID","eCurJoy","lCur","aCur","eErr","eP","eI","eD","eFF"};
    private int currentPropertyNo;
    private String currentTuningValue;
    private String inputCache;
    private Joystick armJoystick;


    public TSBAdapter(Joystick tractorPanel, Robot robot){
        super(tractorPanel,28); //button 28 is the red button on the joystick and button 27 is press on wheel (those buttons aren't labled on the panel)
        this.robot=robot;
        mode=Mode.RobotResponse;
        currentPropertyNo=0;
        currentTuningValue=tuningValues[currentPropertyNo];
        inputCache="";
        elevatorControlMode=ControlMode.Joystick;
        armControlMode=ControlMode.PID;
        setArmJoystick(getJoystick());
    }
    public void buttonPressed(int no){
        if (mode==Mode.RobotResponse&&robot.isEnabled()){
            switch (no){
                /*

                **Wrong right now will make program to automate later too tedious to do manually with constant changes
                //<<Table of Contents>>\\

                B# - FUNCTION ......... <|LN#|>


                1  - elevator top ..... <| 75|>
                2  - elevator mid ..... <| 80|>
                3  - elevator bot/hat . <| 85|>
                4  - lift up .......... <|100|>
                5  - lift off ......... <|108|>
                6  - cargoship position <|90|>
                15 - hatch protocol ... <|0|>
                16 - cargo position ... <|0|>
                17 - left eject ....... <|0|> **soft eject
                18 - right eject ...... <|0|> **soft eject
                20 - hatch eject ...... <|0|>
                21 - compressor toggle  <|0|>
                22 - everything off ... <|0|>
                23 - print motor pos .. <|0|>
                24 - debug info ....... <|0|>

                <|70|> (if this <|#|> changes change index #s accordingly)*/


                //<<ELEVATOR BUTTONS>>\\
                    
                    //button 1 moves elevator to top position <|75|>
                    case 1:
                        robot.elevatorTop();
                        elevatorControlMode=ControlMode.PID;
                    break;
                    //button 2 moves elevator to mid position <|80|>
                    case 2:
                        robot.elevatorMid();
                        elevatorControlMode=ControlMode.PID;
                    break;
                    //button 3 moves elevator to hatch/bottom position <|85|>
                    case 3:
                        robot.elevatorBottom();
                        elevatorControlMode=ControlMode.PID;
                    break;

                //LIFT BUTTONS
                    //button 4 moves lift up <|100|>
                    case 4:
                        robot.liftRaise();
                    break;
                    //button 9 moves lift down <|104|>
                    case 9:
                        robot.liftLower();
                    break;
                    //button 10 moves lift down slowly <99>
                    case 10:
                        robot.liftLowerSlow();
                    break;
                    //button 5 turns lift off (coast mode) <|108|>
                    case 5:
                        robot.liftOff();    
                    break;


                //<<ARM BUTTONS>>\\

                    //button 11 moves arm up <|116|>
                    case 11:
                        robot.armUp();
                    break;
                    //button 12 moves arm down **may become arm hatch <|120|>
                    case 12:
                        robot.armDown();
                    break;
                    //button 16 puts arm in ball pickup position <|124|>
                    case 16:
                        robot.armBall();
                        robot.pushSuckers(false);
                        robot.setVaccum(false);
                        armControlMode=ControlMode.PID;
                    break;

                //<<OUTTAKE>>\\

                //button 17 soft outtake <|133|>
                /*case 17: //not strong enough for anything
                    robot.outtakeSoft();
                break;
                //button 18 soft outtake <|137|>
                case 18:
                    robot.outtakeSoft();
                break;*/
                
                
                //<<COMPRESSOR BUTTON>>\\

                //button 21 toggles the compressor <|145|>
                case 21:
                    robot.toggleCompressor();
                break;
                
                
                //<<MIXED SYSTEMS BUTTONS>>\\

                    //button 15 puts arm in hatch position <|153|>
                    case 15:
                        robot.armHatch();
                        robot.elevatorHatch();
                        robot.pushSuckers(true);
                        robot.setVaccum(true);
                        armControlMode=ControlMode.PID;
                    break;
                    //button 6 moves elevator and arm into cargo position <|90|>
                    case 6:
                        robot.elevatorCargo();
                        robot.armCargo();
                        armControlMode=ControlMode.PID;
                        elevatorControlMode=ControlMode.PID;
                    break;
                    //button 20 fires a loaded hatch <|161|>
                    case 20:
                        Robot.eHandler.triggerEvent(new DiskPushEvent(100));
                    break;
                    //button 22 turns off all the motors and stuff <|165|>
                    case 22:
                        robot.elevatorOff();
                        robot.armOff();
                        robot.liftOff();
                        robot.fireHatch(false);
                        robot.iotakeOff();
                        robot.pushSuckers(false);
                    break;


                //<<DEBUG/TUNING BUTTONS>>\\

                    //button 23 prints the sensor locatations of sensored motors <|178|>
                    case 23:
                        robot.printSensorPositions();
                    break;
                    //button 24 prints debug info <|182|>
                    case 24:
                        robot.debug();
                    break;
                    //button 28 switches to tuning mode <|186|>
                    case 28:
                        mode=Mode.Tune;
                        Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'Tune'"));
                    break;
            }
        } else if (mode==Mode.Tune) {
            if (no<10){
                if (!this.getButtonDown(27)){
                    inputCache=inputCache+no;
                Robot.eHandler.triggerEvent(new PrintEvent("Input Cache: "+inputCache));
                } else {
                    if (no==1){
                        robot.setTuningValue("eTop", robot.elevatorPos());
                        if (robot.isEnabled()){
                            mode=Mode.RobotResponse;
                            Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'RobotResponse'"));
                        }
                    } else if (no==2){
                        robot.setTuningValue("eMid", robot.elevatorPos());
                        if (robot.isEnabled()){
                            mode=Mode.RobotResponse;
                            Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'RobotResponse'"));
                        }
                    } else if (no==3){
                        robot.setTuningValue("eHat", robot.elevatorPos());
                        if (robot.isEnabled()){
                            mode=Mode.RobotResponse;
                            Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'RobotResponse'"));
                        }
                    }
                }
                
            } else if (no<28) {
                switch (no){
                    case 10:
                        inputCache=inputCache+0;
                        Robot.eHandler.triggerEvent(new PrintEvent("Input Cache: "+inputCache));
                    break;
                    case 11:
                        try {
                            inputCache=inputCache.substring(0, inputCache.length()-1);
                        } catch (Exception e){
                        }
                        Robot.eHandler.triggerEvent(new PrintEvent("Input Cache: "+inputCache));
                    break;
                    case 12:
                        if (!inputCache.contains(".")){
                            inputCache=inputCache+".";
                            Robot.eHandler.triggerEvent(new PrintEvent("Input Cache: "+inputCache));
                        }
                    break;
                    case 15:
                        if (getButtonDown(28)){
                            robot.setTuningValue("aHat", robot.armPos());
                            if (robot.isEnabled()){
                                mode=Mode.RobotResponse;
                                Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'RobotResponse'"));
                            }
                        }
                    break;
                    case 13:
                        if (getButtonDown(28)){
                            robot.setTuningValue("eHat", robot.elevatorPos());
                            if (robot.isEnabled()){
                                mode=Mode.RobotResponse;
                                Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'RobotResponse'"));
                            }
                        }
                    break;
                    case 16:
                    if (getButtonDown(28)){
                        robot.setTuningValue("aBal", robot.elevatorPos());
                        if (robot.isEnabled()){
                            mode=Mode.RobotResponse;
                            Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'RobotResponse'"));
                        }
                    }
                    break;
                    case 17:
                        if (!inputCache.contains("-")){
                            inputCache="-"+inputCache;
                            Robot.eHandler.triggerEvent(new PrintEvent("Input Cache: "+inputCache));
                        } else {
                            inputCache=inputCache.substring(1);
                        }
                    break;


                    /*case 19:
                        Event[] printNos={new PrintEvent(4),new PrintEvent(3),new PrintEvent(2),new PrintEvent(1),new PrintEvent(0),new PrintEvent("Good Job!"),new PrintEvent(-1)};
                        Robot.eHandler.triggerEvent(new EventSequence(printNos));
                    break;
                    case 20:
                        Event[] printNos1={new PrintEvent(1),new PrintEvent(2,500,false),new PrintEvent(3,1000,false),new PrintEvent(4,1000,false)};
                        Robot.eHandler.triggerEvent(new EventSequence(printNos1));
                    break;*/
                                    

                    //Button 21 set value to input
                    case 21:
                        try {
                            robot.setTuningValue(currentTuningValue, Double.parseDouble(inputCache));
                            Robot.eHandler.triggerEvent(new PrintEvent(currentTuningValue+" set to "+inputCache));
                            inputCache="";
                            robot.elevatorPID(robot.getTuningValue("eP"), robot.getTuningValue("eI"), robot.getTuningValue("eD"),robot.getTuningValue("eFF"));
                        } catch (NumberFormatException e){
                            //robot.setProp(currentTuningValue, 0);
                            Robot.eHandler.triggerEvent(new PrintEvent("User did not enter a number"));
                            //System.err.println(currentTuningValue+" defaulted to 0");
                            //inputCache="";
                        }
                    break;
                    case 22:
                        robot.elevatorOff();
                        robot.armOff();
                        robot.liftOff();
                        robot.fireHatch(false);
                        robot.iotakeOff();
                        robot.pushSuckers(false);
                    break;
                    case 23:
                        robot.printSensorPositions();
                    break;
                    case 24:
                        robot.setTuningValue("eTop",60.26);
                        robot.setTuningValue("eBot",0.0);
                        robot.setTuningValue("eMid",32.42);
                        robot.setTuningValue("eCar",27.47599);
                        robot.setTuningValue("eHat",3.21);
                        robot.setTuningValue("aCar",-25.30935);
                        robot.setTuningValue("aHat",-7.71);
                        robot.setTuningValue("aBal",-27.66);
                        robot.setTuningValue("aSit",-.1);
                        robot.setTuningValue("aDec",-.1);
                        robot.setTuningValue("lTop",0.0);
                        robot.setTuningValue("lBot",-.5);

                        robot.setTuningValue("aPIDOR",0.0);
                        robot.setTuningValue("lPIDOR",.0);
                        robot.setTuningValue("ePIDORUp",.0);
                        robot.setTuningValue("ePIDORDow",.0);

                        robot.setTuningValue("eSpdRPM", 922.99);
                        robot.setTuningValue("eAclRPMS", 922.99);
                        robot.setTuningValue("eSpdUp",.3);
                        robot.setTuningValue("eSpdDow",.2);
                        robot.setTuningValue("lSpdRPM", 645);
                        robot.setTuningValue("lAclRPMS", 645);
                        robot.setTuningValue("lSpdUp",.8);
                        robot.setTuningValue("lSpdDow",.4);
                        robot.setTuningValue("eSpdJ",.6);
                        robot.setTuningValue("aSpd",.2);
                        robot.setTuningValue("aSpdJ",.75);
                        robot.setTuningValue("iSpd",1);

                        robot.setTuningValue("eCurUp",52.0);
                        robot.setTuningValue("eCurDow", 8.0);
                        robot.setTuningValue("eCurPID", 100.0);
                        robot.setTuningValue("eCurJoy", 60.0);
                        robot.setTuningValue("lCur", 80.0);
                        robot.setTuningValue("aCur", 120.0);

                        //robot.setTuningValue("eErr", 0);
                        robot.setTuningValue("eP", 1);
                        robot.setTuningValue("eI", 0);
                        robot.setTuningValue("eD", 0);
                        robot.setTuningValue("eFF", 0);
                        robot.elevatorPID(robot.getTuningValue("eP"), robot.getTuningValue("eI"), robot.getTuningValue("eD"),robot.getTuningValue("eFF"));
                        Robot.eHandler.triggerEvent(new PrintEvent("TUNING VALUES SET TO TEST ROBOT"));
                    break;
                    case 25:
                        Robot.eHandler.triggerEvent(new PrintEvent("Current value of "+currentTuningValue+": "+robot.getTuningValue(currentTuningValue)));
                    break;
                    //button 26 changes what property you are editing (++)
                    case 27:
                        currentPropertyNo++;
                        if (currentPropertyNo>38){
                            currentPropertyNo=0;
                        }
                        currentTuningValue=tuningValues[currentPropertyNo];
                        //System.out.println("Now edititing "+currentTuningValue);
                        if (!Robot.eHandler.triggerEvent(new PrintEvent("Now edititing "+currentTuningValue))){
                            System.err.println("Print failed to queue");
                        }
                    break;
                    //button 26 changes what property you are editing (--)
                    case 26:
                        currentPropertyNo--;
                        if (currentPropertyNo<0){
                            currentPropertyNo=38;
                        }
                        currentTuningValue=tuningValues[currentPropertyNo];
                        //System.out.println("Now edititing "+currentTuningValue);
                        if (!robot.eHandler.triggerEvent(new PrintEvent("Now editing"+currentTuningValue))){
                            System.err.println("Print failed to queue");
                        }
                    break;
                }
             } else {
                switch (no){
                case 28:
                    if (robot.isEnabled()){
                        mode=Mode.RobotResponse;
                        Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'RobotResponse'"));
                    } else {
                        Robot.eHandler.triggerEvent(new PrintEvent("RobotResponse mode not available while robot is disabled",true));
                    }
                break;
                }
            }
        }/* else {
            switch (no){
                //button 1 moves elevator up
                case 1:
                    robot.elevatorUp();
                    elevatorControlMode=ControlMode.PID;
                break;
                //button 6 moves elevator down
                case 6:
                    robot.elevatorDown();
                    elevatorControlMode=ControlMode.PID;
                break;
                //button 2 moves elevator to bottom
                case 2:
                    robot.elevatorBottom();
                    elevatorControlMode=ControlMode.PID;
                break;
                //button 3 moves elevator to middle
                case 3:
                    robot.elevatorMid();
                    elevatorControlMode=ControlMode.PID;
                break;
                //button 7 moves elevator to top
                case 7:
                    robot.elevatorTop();
                    elevatorControlMode=ControlMode.PID;
                break;
                //button 8 moves elevator into cargo position
                case 8:
                    robot.elevatorCargo();
                    elevatorControlMode=ControlMode.PID;
                break;
                //button 4 moves lift up
                case 4:
                    robot.liftUp();
                break;
                //button 9 moves lift down
                case 9:
                    robot.liftDown();
                break;
                //button 5 puts lift in raised position
                case 5:
                    //robot.liftRaise();
                break;
                //button 10 puts lift in lowered position
                case 10:
                    //robot.liftLower();
                break;
                //button 11 moves arm up
                case 11:
                    robot.armUp();
                break;
                //button 12 moves arm down
                case 12:
                    robot.armDown();
                break;
                //button 13 puts arm in ball position
                case 13:
                    robot.armBall();
                    armControlMode=ControlMode.PID;
                break;
                //button 15 puts arm in hatch position
                case 15:
                    robot.armHatch();
                    armControlMode=ControlMode.PID;
                break;
                //button 14 puts arm at deck height
                case 14:
                    robot.armCargo();
                    armControlMode=ControlMode.PID;
                break;
                //button 16 puts arm at sit height
                case 16:
                    robot.armSit();
                    armControlMode=ControlMode.PID;
                break;
                //button 17 initiates intake
                case 17:
                    robot.outtakeLeft();
                break;
                //button 18 outakes (shoots)
                case 18:
                    robot.outtakeRight();
                break;
                //button 20 fires a loaded hatch
                case 20:
                    Robot.eHandler.triggerEvent(new DiskPushEvent(1000));
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
                case 24:
                    robot.debug();
                break;
                case 28:
                        if (robot.isEnabled()){
                            mode=Mode.RobotResponse;
                            Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'RobotResponse'"));
                        } else {
                            Robot.eHandler.triggerEvent(new PrintEvent("RobotResponse mode not available while robot is disabled"));
                        }
                break;
            }
        }*/
    }
    public void buttonReleased(int no){
        if (mode==Mode.RobotResponse){
            switch (no){
                /*case 20:
                robot.fireHatch(false);
                break;*/
                //button 1 moves elevator up
                case 1:
                    robot.elevatorHoldPosOR();
                    elevatorControlMode=ControlMode.Joystick;
                break;
                //button 6 moves elevator down
                case 6:
                    robot.elevatorHoldPosOR();
                    elevatorControlMode=ControlMode.Joystick;
                break;
                case 4:
                    robot.liftOff();
                    Robot.eHandler.triggerEvent(robot::liftHoldPos, 120); //brushless motor only
                    //robot.liftOff(); //brushed motor
                break;
                case 9:
                    robot.liftOff();
                    Robot.eHandler.triggerEvent(robot::liftHoldPos, 120); //brushless motor only
                    //robot.liftOff(); //brushed motor
                break;
                case 11:
                    robot.armOff();
                    Robot.eHandler.triggerEvent(new ArmHoldPosEvent(120));
                    armControlMode=ControlMode.PID;
                break;
                case 12:
                    robot.armOff();
                    Robot.eHandler.triggerEvent(new ArmHoldPosEvent(120));
                    armControlMode=ControlMode.PID;
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
    public void buttonDown(int no){
    }

    @Override
    public void update() {
        super.update();
        if (Math.abs(getY())<.07&&!(elevatorControlMode==ControlMode.PID)){
            robot.elevatorHoldPos();
            elevatorControlMode=ControlMode.PID;
        } else if (!(getButtonDown(1)||getButtonDown(6))&&elevatorControlMode==ControlMode.Joystick){
            robot.elevatorJoystick();
        } else if (Math.abs(getY())>.07&&!(elevatorControlMode==ControlMode.Joystick)){
            elevatorControlMode=ControlMode.Joystick;
        }
        if (Math.abs(getX())<.05&&!(armControlMode==ControlMode.PID)){
            robot.armHoldPos();
            armControlMode=ControlMode.PID;
        } else if (!(getButtonDown(11)||getButtonDown(12))&&armControlMode==ControlMode.Joystick){
            robot.armJoystick(armJoystick);
        } else if (Math.abs(armJoystick.getX())>.05&&!(armControlMode==ControlMode.Joystick)){
            armControlMode=ControlMode.Joystick;
        }
    }
    public void setArmJoystick(Joystick j){
        armJoystick=j;
    }
    public Joystick getArmJoystick(){
        return armJoystick;
    }
    public void setMode(Mode mode){
        this.mode=mode;
    }

}