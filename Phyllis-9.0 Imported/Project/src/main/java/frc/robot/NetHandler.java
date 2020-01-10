/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import frc.robot.event.core.NetScanEvent;

/**
 * Handles all things to do with the network
 */
public class NetHandler {

    private boolean Connected;
    private Socket s;
    private String hostName="10.0.0.1";
    private NetScanEvent runningNetScan;

    public NetHandler(){
        Connected=false;
        attemptConnection(hostName);
    }

    public boolean attemptConnection(String hostName){
        //dispose of running NetScan
        if (!(runningNetScan==null)){
            runningNetScan.terminate();
            runningNetScan=null;
        }

        //dispose of current socket
        if (!(s==null)){
            try {
                s.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        s=null;
        
        //attempt connection
        try {
            s = new Socket(hostName, (int) Math.floor(Robot.getInstance().getTuningValue("port")));
            runningNetScan=new NetScanEvent();
            Robot.eHandler.triggerEvent(runningNetScan);
            System.out.println("Connected to "+hostName+"!");
            Connected=true;
          } catch (UnknownHostException e) {
            System.out.println("Failed to connect to "+hostName);
            
            Connected=false;
            e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
        }
        return Connected;
    }

    /**Scans socket input stream for the oldest unread message.
     * 
     * @return returns "" if no message is available
     */
    public String getMesssage(){
        try {
            return new DataInputStream(s.getInputStream()).readUTF();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**Attempts to send a message to host
     * 
     * @param message
     * @return if the message was sent
     */
    public boolean sendMessage(String message){
        try {
            new DataOutputStream(s.getOutputStream()).writeUTF(message);
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    /**Queues a message in the event handler
     * 
     * @param message
     * @return whether the message was successfully queued (not if it was actually sent)
     */
    public boolean queueMessage(String message){
        if (Connected){
            runningNetScan.sendMessage(message);
            return true;
        }
        return false;
    }

    public boolean isConnected(){
        return Connected;
    }

    public void receiveMessage(String message){
        //interpret message
        if (message.equals("fireHatch(true)")){
            Robot.getInstance().fireHatch(true);
        } else if (message.equals("fireHatch(false)")){
            Robot.getInstance().fireHatch(false);
        }
    }
}
