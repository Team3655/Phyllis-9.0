package shared;

import java.util.InputMismatchException;
import java.util.Scanner;

import client.Client;
import server.Server;

public class UI {

	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		System.out.print("Would you like to run a server(1) or a client(2)?");
		int Choice=0;
		try{
			Choice=s.nextInt();
		} catch(InputMismatchException e){
			System.err.println("Not a number");
		}
		if (Choice==1){
			new Server();
		} else {
			new Client();
		}

	}

}
