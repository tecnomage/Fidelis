package controller;

import java.io.IOException;

public class Executa extends Thread {
	
	private String strCmd;
	public Executa(String cmd) {
		strCmd = cmd;
	}

	public void run() {
		try {
			Runtime.getRuntime().exec("cmd /c " + strCmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
