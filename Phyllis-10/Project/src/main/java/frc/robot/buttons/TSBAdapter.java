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
    private String[] tuningValues={"eTop","eBot","eMid","eCar","eHat","aCar","aHat","aBal","aSit","aDec","lTop","lBot","aPIDOR","lPIDOR","ePIDORUp","ePIDORDow","eSpdUp","eSpdDow","lSpdUp","lSpdDow","eSpdJ","aSpd","aSpdJ","iSpd","eCurUp","eCurDow","eCurPID","eCurJoy","lCur","aCur"};
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
        armControlMode=ControlMode.Joystick;
        setArmJoystick(getJoystick());
    }
    public void buttonPressed(int no){
        if (mode==Mode.RobotResponse&&robot.isEnabled()){
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
                //button 5 turns lift off//puts lift in raised position
                case 5:
                    robot.liftOff();    

                    /*robot.pushSuckers(true);
                    robot.setVaccum(true);*/

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
                    robot.pushSuckers(true);
                    robot.setVaccum(true);
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
                    Robot.eHandler.triggerEvent(new DiskPushEvent(100));
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
                    robot.pushSuckers(false);
                break;
                //button 23 prints the sensor locatations of sensored motors
                case 23:
                    robot.printSensorPositions();
                break;
                case 24:
                    robot.debug();
                break;
                case 28:
                    mode=Mode.Tune;
                    Robot.eHandler.triggerEvent(new PrintEvent("Mode set to 'Tune'"));
                break;
            }
        } else if (mode==Mode.Tune) {
            if (no<10){
                inputCache=inputCache+no;
                Robot.eHandler.triggerEvent(new PrintEvent("Input Cache: "+inputCache));
            } else {
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
                        robot.setTuningValue("eTop",44.83285);
                        robot.setTuningValue("eBot",0.0);
                        robot.setTuningValue("eMid",22.6903);
                        robot.setTuningValue("eCar",27.47599);
                        robot.setTuningValue("eHat",3.88);
                        robot.setTuningValue("aCar",-25.30935);
                        robot.setTuningValue("aHat",-8.88);
                        robot.setTuningValue("aBal",-27.85693);
                        robot.setTuningValue("aSit",.1);
                        robot.setTuningValue("aDec",.1);
                        robot.setTuningValue("lTop",0.0);
                        robot.setTuningValue("lBot",-.5);

                        robot.setTuningValue("aPIDOR",0.0);
                        robot.setTuningValue("lPIDOR",.0);
                        robot.setTuningValue("ePIDORUp",.0);
                        robot.setTuningValue("ePIDORDow",.0);


                        robot.setTuningValue("eSpdUp",.3);
                        robot.setTuningValue("eSpdDow",.2);
                        robot.setTuningValue("lSpdUp",.4);
                        robot.setTuningValue("lSpdDow",.8);
                        robot.setTuningValue("eSpdJ",.6);
                        robot.setTuningValue("aSpd",.2);
                        robot.setTuningValue("aSpdJ",.75);
                        robot.setTuningValue("iSpd",1);

                        robot.setTuningValue("eCurUp",52.0);
                        robot.setTuningValue("eCurDow", 8.0);
                        robot.setTuningValue("eCurPID", 60.0);
                        robot.setTuningValue("eCurJoy", 60.0);
                        robot.setTuningValue("lCur", 80.0);
                        robot.setTuningValue("aCur", 120.0);
                        Robot.eHandler.triggerEvent(new PrintEvent("TUNING VALUES SET TO TEST ROBOT"));
                    break;
                    case 25:
                        Robot.eHandler.triggerEvent(new PrintEvent("Current value of "+currentTuningValue+": "+robot.getTuningValue(currentTuningValue)));
                    break;
                    //button 26 changes what property you are editing (++)
                    case 27:
                        currentPropertyNo++;
                        if (currentPropertyNo>29){
                            currentPropertyNo=0;
                        }
                        currentTuningValue=tuningValues[currentPropertyNo];
                        //System.out.println("Now edititing "+currentTuningValue);
                        if (!robot.eHandler.triggerEvent(new PrintEvent("Now edititing "+currentTuningValue))){
                            System.err.println("Print failed to queue");
                        }
                    break;
                    //button 26 changes what property you are editing (--)
                    case 26:
                        currentPropertyNo--;
                        if (currentPropertyNo<0){
                            currentPropertyNo=29;
                        }
                        currentTuningValue=tuningValues[currentPropertyNo];
                        //System.out.println("Now edititing "+currentTuningValue);
                        if (!robot.eHandler.triggerEvent(new PrintEvent("Now editing"+currentTuningValue))){
                            System.err.println("Print failed to queue");
                        }
                    break;
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
        } else {
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
        }
    }
    public void buttonReleased(int no){
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
                robot.liftHoldPos();
            break;
            case 9:
                robot.liftHoldPos();
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