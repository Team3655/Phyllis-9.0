/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.event.core;

import frc.robot.NetHandler;
import frc.robot.Robot;
import frc.robot.event.Event;

/**
 * Add your docs here.
 */
public class NetScanEvent extends Event{
    


    public NetScanEvent(){
        
    }

    public boolean EventCompleteCondition(){
        return !Robot.getInstance().netH.isConnected();
    }

    public void task(){
        String incomingMessage=Robot.getInstance().netH.getMesssage();
        if (!(incomingMessage.equals(""))){
            Robot.getInstance().netH.receiveMessage(incomingMessage);
        }
    }

    public void sendMessage(String message){
        Robot.eHandler.triggerEvent(new sendMessageEvent(message));
    }

    private class sendMessageEvent extends Event{

        private String message;

        public sendMessageEvent(String message){
            this.message=message;
        }

        public void task(){
            Robot.getInstance().netH.sendMessage(message);
        }
    }
}
