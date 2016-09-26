package controller;

import view.JMFInit;

public class ThreadRunStatic extends Thread {
	
	private String[] argumentos;
	private int modulo;
	
	public ThreadRunStatic(String[] args, int mod) {
		argumentos = args;
		modulo = mod;
	}
	
	public void run() {
		if (modulo == 1)
			FidelisVCRecvAud.main(argumentos);
		
		if (modulo == 2)
			FidelisVCSendAud.main(argumentos);
		
		if (modulo == 3) {
        	JMFInit init = new JMFInit(argumentos);
        	init.setVisible(false);
        	init.dispose();
		}
	}

}
