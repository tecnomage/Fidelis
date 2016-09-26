package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Servidor {
	
	public static void main (String [] args) {
		
		try {
			ServerSocket sv = new ServerSocket(60000);			
			sv.accept();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
