package client;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


import shared.KPane;

public class ClientPane extends KPane{
	
	String lastMessage;
	CopyOnWriteArrayList<String> messages;
	int index;
	int textX;
	
	public ClientPane(){
		super("Client");
		messages=new CopyOnWriteArrayList<String>();
		addMessage("Client initialized");
		index=0;
		textX=2;
		addKeyListener(new TAdapter());
	}
	
	public void paintComponent(Graphics g){
		g.drawString("Last message: "+lastMessage, textX, 35);
		g.drawString("Message #"+index+": "+messages.get(index), textX, 55);
	}
	
	public void addMessage(String message){
		lastMessage=message;
		messages.add(message);
	}
	
	private class TAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			if (e.getKeyCode()==KeyEvent.VK_UP){
				try {
					messages.get(index+1);
					index++;
				} catch(IndexOutOfBoundsException e1){
					
				}
			} else if (e.getKeyCode()==KeyEvent.VK_DOWN){
				try {
					messages.get(index-1);
					index--;
				} catch(IndexOutOfBoundsException e1){
					
				}
			} else if (e.getKeyCode()==KeyEvent.VK_RIGHT){
				textX+=4;
			} else if (e.getKeyCode()==KeyEvent.VK_LEFT){
				textX-=4;
			} 
		}
		
	}
}
