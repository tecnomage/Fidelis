package controller;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTable;


public class BarraProgressaoPlayVideo extends Thread {	
	private DSJCaptura capLocal;
	private JLabel jl = null;
	private JSlider js = null;
	private int segundos;
	private boolean running = false;
	private JTable tbMarca = null;	
	
	public BarraProgressaoPlayVideo(DSJCaptura c, JSlider s, JLabel j, JTable table) {
        capLocal = c;
		js = s;
		jl = j;
		running = true;
		tbMarca = table;
	}
	
	public void run() {
		Ferramentas tmFmt = new Ferramentas();
		int i = 1;
		while (running) {			
			segundos = capLocal.getSegundos();
			jl.setText(tmFmt.formataHora(segundos));
			
			if (tbMarca != null)
				mostraMarca(tmFmt.formataHora(segundos));
			
			if (i++ >= 3) {
				js.setValue(segundos);
				i = 1;
			}
			try {
				BarraProgressaoPlayVideo.sleep(500);
			} catch (InterruptedException e) {
				GravaLog log = new GravaLog("ERRO");
				log.gravaExcep("fidelis.controller.BarraProgressaoPlayVideo.run()", e);				
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "fidelis.controller.BarraProgressaoPlayVideo.run() gerou uma exception!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void parar() {
		running = false;
		segundos = 0;
		jl.setText("00:00:00");
		js.setValue(0);
	}
	
	public void reiniciar() {
		running = true;
	}
	
	private void mostraMarca(String seg) {		
		for (int i = 0; i < tbMarca.getRowCount(); i++) {
			if (tbMarca.getValueAt(i, 0).equals(seg)) {				
				tbMarca.setRowSelectionInterval(i, i);
			}
		}		
	}

}
