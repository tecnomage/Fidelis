package controller;

import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MonitoraEspacoThread extends Thread {
	
	private File drive = null;
	private JLabel lbEspaco = null;
	private JLabel lbTamanho = null;
	//private long lEspacoInicial = 0;
	private long lEspacoTotal;
	private String nomeArquivo;

	public MonitoraEspacoThread(String strDrive, JLabel lblEspaco, JLabel lblTamanho, String arquivoGravando) {		
		drive = new File(strDrive);
		//lEspacoInicial = drive.getFreeSpace();
		lEspacoTotal = drive.getTotalSpace();
		lbEspaco = lblEspaco;
		lbTamanho = lblTamanho;
		nomeArquivo = arquivoGravando;
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		long lEspacoLivre;
		long lTamanho;
		DecimalFormat nfmb = new DecimalFormat("#,###,### MB");
		nfmb.setDecimalSeparatorAlwaysShown(false);
		DecimalFormat nfkb = new DecimalFormat("#,###,### KB");
		nfkb.setDecimalSeparatorAlwaysShown(false);
		
		lbEspaco.setForeground(Color.BLUE);
		lbTamanho.setForeground(Color.BLUE);
		long percentualLivre;
		File f = new File(nomeArquivo);
		
		while (Sessao.gravando) {
			lEspacoLivre = drive.getFreeSpace();
			//lTamanho = lEspacoInicial - lEspacoLivre;
			lTamanho = f.length();
			percentualLivre = (lEspacoLivre * 100) / lEspacoTotal;
			lEspacoLivre = (((lEspacoLivre) / 1024) / 1024);
			lTamanho = (lTamanho / 1024);
			
			if (lEspacoLivre < 1000) {
				lbEspaco.setForeground(Color.RED);
			}
			lbEspaco.setText(nfmb.format(lEspacoLivre) + "  (" + percentualLivre + "%)");
			lbTamanho.setText(nfkb.format(lTamanho));

			try {
				this.sleep(2000);
			} catch (InterruptedException e) {
				GravaLog log = new GravaLog("ERRO");
				log.gravaExcep("fidelis.controller.MonitoraEspacoThread.run()", e);				
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "fidelis.controller.MonitoraEspacoThread.run() gerou uma exception!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
}
