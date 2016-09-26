package controller;

import javax.swing.JButton;

public class ThreadVisualiza extends Thread {
	
	private DSJCaptura cap;
	private JButton visualizar;
	private JButton testar;
	
	public ThreadVisualiza(DSJCaptura c, JButton tt, JButton v) {
		cap = c;
		testar = tt;
		visualizar = v;
	}
	
	@SuppressWarnings("static-access")
	public void run() {
					
		try {
			while (cap.isPlaying && cap.getIsShowing())
				Thread.currentThread().sleep(500);
		} 
		catch (Exception e) { }
		visualizar.setEnabled(true);
		testar.setEnabled(true);
	}

}
