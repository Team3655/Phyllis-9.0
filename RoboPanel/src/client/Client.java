package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server.ServerPane;
import shared.KFrame;

public class Client {
	Socket s;
	
	public static void main(String[] args){
		new Client();
	}
	
	public Client(){
		try {
			s=new Socket("10.0.0.11", 7001);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(2);
		}
		
		boolean Connected=true;
		KFrame f=new KFrame();
		ClientPane pane=new ClientPane();
		f.setUndecorated(true);
		f.setLocationRelativeTo(null);
		f.setSize(700,230);
		f.add(pane);
		f.setVisible(true);
		
		while (Connected){
			try {
				String data=new DataInputStream(s.getInputStream()).readUTF();
				
				if (data.equals("Disconnect")){
					Connected=false;
					s.close();
					break;
				}
				System.out.println(data);
				pane.addMessage(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Disconnected.");
		pane.addMessage("Disconnected.");
	}
}
