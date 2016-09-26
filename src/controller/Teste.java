package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

public class Teste {

	public static void main(String [] args) {
	
		FTPClient ftp = new FTPClient();		
		
		try {
			System.out.println("conectando no servidor ...");
			ftp.connect("ftp.e-nuts.com.br");
			ftp.login("ios", "ios");
			ftp.cwd("/ios/transm");
			ftp.setFileType( FTPClient.BINARY_FILE_TYPE ); 
			System.out.println("transmitindo arquivo ...");
			InputStream is = new FileInputStream("c:/Temp/SERVIDOR/46464 6464 612 34 56 7 23092009 221029.zip");   
			ftp.storeFile("y.tmp", is);
			ftp.rename("y.tmp", "final.zip");
			ftp.disconnect();
			System.out.println("transmissao finalizada....");
			is.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}	
	
}
