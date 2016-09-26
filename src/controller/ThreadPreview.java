package controller;

import javax.swing.JButton;
import javax.swing.JLabel;

public class ThreadPreview extends Thread {
	
	private DSJCaptura cap;
	private JButton visualizar;
	private JButton testar;
	private JButton ajuste;
	private JLabel lbTempo;
	
	public ThreadPreview(DSJCaptura c, JButton tt, JButton v, JButton aj, JLabel t) {
		cap = c;
		testar = tt;
		visualizar = v;
		ajuste = aj;
		lbTempo = t;
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		
		cap.setVolume(0);	
		lbTempo.setVisible(true);		
		try { 
			for (int i = 19; i > -1; i--) {			
				Thread.currentThread().sleep(1000);
				if (cap.isRecording)
					lbTempo.setText("Gravando " + i + " seg.");
				else
					i = -1;
			}
		} catch (Exception e) { }
		lbTempo.setVisible(false);
		if (cap.isRecording) {
			cap.setVolume(1);
			cap.finalizaFilmagem();
		}
		visualizar.setEnabled(true);
		testar.setEnabled(true);
		ajuste.setEnabled(false);
	}

}
