package controller;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ClockThread extends Thread {
	private DSJCaptura capLocal;
	private JLabel jl = null;
	private int segundos;
	
	public ClockThread(DSJCaptura c, JLabel j) {
		capLocal = c;
		jl = j;
	}
		
	public void run() {
		Ferramentas tmFmt = new Ferramentas();
		while (Sessao.gravando) {
			segundos = capLocal.getSegundos();
			jl.setText(tmFmt.formataHora(segundos));
			try {
				ClockThread.sleep(1000);	
			} catch (InterruptedException e) {
				GravaLog log = new GravaLog("ERRO");
				log.gravaExcep("fidelis.controller.ClockThread.run()", e);				
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "fidelis.controller.ClockThread.run() gerou uma exception!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
