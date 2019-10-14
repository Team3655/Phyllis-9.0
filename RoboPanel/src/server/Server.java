package server;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import client.Client;
import shared.KFrame;
import shared.KPane;


public class Server extends TimerTask{
	ServerSocket SS;
	CopyOnWriteArrayList<Socket> sockets;
	
	public static void main(String[] args){
		new Server();
	}
	
	public Server() {
		try {
			SS=new ServerSocket(7001);
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);
		}
		sockets=new CopyOnWriteArrayList<Socket>();
		Timer t=new Timer();
		KFrame f=new KFrame();
		ServerPane pane=new ServerPane();
		f.setUndecorated(true);
		f.setLocationRelativeTo(null);
		f.setSize(700,230);
		f.add(pane);
		f.setVisible(true);
		
		
		
		
		
		t.scheduleAtFixedRate(this, 10, 100);
		while(true){
			for (Socket s:sockets){
				System.out.println("Interacting with "+s.getInetAddress().getHostAddress());
				pane.addMessage("Connected to "+s.getInetAddress().getHostAddress());
				try {
					new DataOutputStream(s.getOutputStream()).writeUTF("Connected.");
					new DataOutputStream(s.getOutputStream()).writeUTF("Hi from LOVE!");
					new DataOutputStream(s.getOutputStream()).writeUTF("Disconnect");
					pane.addMessage("Disconnected from "+s.getInetAddress().getHostAddress());
					s.close();
					sockets.remove(s);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			sockets.add(SS.accept());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
