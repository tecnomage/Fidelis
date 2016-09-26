package controller;

import java.awt.Color;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import view.FrameGravaAudiencia;
import view.FrameGravaEvento;

public class ThreadMonitorMicrofone extends Thread {
	
	private JLabel [] lbLocal;
	private JLabel lbRec;
	private FrameGravaAudiencia pPaiAudiencia = null;
	private FrameGravaEvento pPaiEvento = null;
	private String nomeArquivo;
	private int arquivoParado;

	public ThreadMonitorMicrofone(JLabel [] lbs, JLabel rec, JPanel panelPai, String strNomeArquivo) {		
		lbLocal = lbs;		
		lbRec = rec;
		
		if (panelPai instanceof FrameGravaAudiencia)
			pPaiAudiencia = (FrameGravaAudiencia) panelPai;
		else
			pPaiEvento = (FrameGravaEvento) panelPai;
		
		nomeArquivo = strNomeArquivo;
		arquivoParado = 0;
		Sessao.gravando = true;
		lbRec.setVisible(true);
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		int i = 0;
		int j = 0;
		
		try { this.sleep(300); } catch (InterruptedException e) { }
		
		lbLocal[0].setVisible(false);
		lbLocal[1].setVisible(true); // 8
		try { this.sleep(400); } catch (InterruptedException e) { }
		
		lbLocal[1].setVisible(false);
		lbLocal[2].setVisible(true); // 20
		try { this.sleep(400); } catch (InterruptedException e) { }

		lbLocal[2].setVisible(false);
		lbLocal[3].setVisible(true); // 39
		try { this.sleep(400); } catch (InterruptedException e) { }

		lbLocal[3].setVisible(false);
		lbLocal[4].setVisible(true); // 40
		
		File f = new File(nomeArquivo);
		long tamanho = 0;
		long tamanhoAnterior =0;
		arquivoParado = 0;
		
		while(arquivoParado < 3) {
			try { this.sleep(500); } catch (InterruptedException e) { }
			lbRec.setForeground(Color.red);
			try { this.sleep(500); } catch (InterruptedException e) { }
			lbRec.setForeground(Color.gray.darker());
			
			tamanho = f.length();
			if (tamanho != tamanhoAnterior) {
				tamanhoAnterior = tamanho;
				arquivoParado = 0;				
			}
			else
				arquivoParado++;
			
			if (arquivoParado > 2) {
				lbLocal[0].setVisible(true);
				for (i=1; i<6; i++)
					lbLocal[i].setVisible(false);
				
				if (Sessao.gravando) {
					GravaLog logInfo = new GravaLog("INFO");
					logInfo.grava("fidelis.controller.ThreadMonitorMicrofone()", SessaoConfig.usuarioLogado, "O arquivo de áudio/vídeo perdeu a sincronia. A gravação foi interrompida delo sistema.");
					JOptionPane.showMessageDialog(null, "O arquivo de áudio/vídeo perdeu a sincronia. A gravação será interrompida com status de ERRO.\nEste evento foi gravado em log.", "Erro de Sincronia", JOptionPane.ERROR_MESSAGE);
					if (pPaiAudiencia != null)
						pPaiAudiencia.interrompePorErroSincronia();
					else
						pPaiEvento.interrompePorErroSincronia();
				}				
			}
			
			if (!Sessao.gravando)
				lbRec.setVisible(false);
						
			for (i=3; i<6; i++)
				lbLocal[i].setVisible(false);
			
			if (j++ % 2 == 0)
				lbLocal[3].setVisible(true);
			else
				if (j==3 || j==8)
					lbLocal[4].setVisible(true);
				else
					lbLocal[5].setVisible(true);
			
			if (j > 10)
				j = 0;
		}
	}
}
