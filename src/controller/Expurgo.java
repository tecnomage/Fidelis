package controller;

import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;

import model.Configuracoes;

public class Expurgo {	
	private String usuarioLogado;
	
	public Expurgo(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;		
	}
	
	@SuppressWarnings("unchecked")
	public void calcula() {
		File f = new File(System.getProperty("user.dir").substring(0,2)+ "\\");		
		long espacoTotal = f.getTotalSpace();
		long espacoLivre = f.getFreeSpace();		
		long percentualUtilizado = 100 - (espacoLivre * 100) / espacoTotal;
		
		Configuracoes cfg = new Configuracoes();
		if (percentualUtilizado >= Long.valueOf(cfg.getConfigParm("expurgo"))) {
			
			DBDealer dealer = new DBDealer("VideocapClient", "AUDIENCIAS");		
			Vector ret = dealer.getAudienciasTransmitidas();
			
			if (ret.size() > 0) {
				GravaLog log = new GravaLog("INFO");
				Object[] options = { "Sim", "Não" };
				String strMsg = "O espaço utilizado no disco (" + percentualUtilizado + "%) superou o limite estabelecido pelo administrador do sistema que é de (" + cfg.getConfigParm("expurgo") + "%).\nDeseja iniciar o expurgo das audiências já transmitidas para liberar espaço?";
	        	int resposta = JOptionPane.showOptionDialog(null, strMsg, "Videocap - Expurgo de Audiências", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	        	if (resposta == 0) {
	        		if (iniciaExpurgo()) {
	        			log.grava("fidelis.controller.Expurgo.calcula()", usuarioLogado, "Audiências Expurgadas com Sucesso!!");
	        			JOptionPane.showMessageDialog(null, "Audiências expurgadas com sucesso!", "Expurgo de Audiências", JOptionPane.INFORMATION_MESSAGE);
	        		}
	        		else {
	        			log.grava("fidelis.controller.Expurgo.calcula()", usuarioLogado, "O expurgo das audiências não foi bem sucedido...");
	        			JOptionPane.showMessageDialog(null, "O expurgo das audiências não foi bem sucedido. Verifique as LOGS do sistema!", "Expurgo de Audiências", JOptionPane.ERROR_MESSAGE);
	        		}
	        	}
			}
			else {
				String strMsg2 = "O espaço utilizado no disco (" + percentualUtilizado + "%) superou o limite estabelecido pelo administrador do sistema que é de (" + cfg.getConfigParm("expurgo") + "%).\nPorém não existem audiências já transmitidas que possam ser expurgadas.\nContacte o administrador imediatamente.";
				JOptionPane.showMessageDialog(null, strMsg2, "Videocap - AVISO IMPORTANTE", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean iniciaExpurgo() {
		
		DBDealer dealer = new DBDealer("VideocapClient", "AUDIENCIAS");		
		Vector ret = dealer.getAudienciasTransmitidas();
		GravaLog log = new GravaLog("INFO");
		
		for (int i=0; i<ret.size(); i++) {
			String [] campos = (String[]) ret.elementAt(i);
			
			log.grava("vidocap.controller.Expurgo.iniciaExpurgo()", usuarioLogado, "Iniciando o expurgo da audiência: [" + campos[0] + ", " + campos[1] + ", " + campos[2] + "]");
		
			DBDealer dealerAudi = new DBDealer("VideocapClient", "AUDIENCIAS", campos[0], campos[1], campos[2]);
			if (!dealerAudi.excluir())
				return false;
			
			File f = new File(System.getProperty("user.dir") + "/videos/" + Sessao.fileName(campos[0], campos[1], campos[2]) + ".asf");
			if (f.exists())
				f.delete();
			
			//f = new File(System.getProperty("user.dir") + "/videos/" + Sessao.fileName(campos[0], campos[1], campos[2]) + ".wav");
			//if (f.exists())
			//	f.delete();
			
			DBDealer dealerMarcacao = new DBDealer("VideocapClient", "MARCACOES", campos[0], campos[1], campos[2]);
			dealerMarcacao.excluir();
			
			DBDealer dealerTemas = new DBDealer("VideocapClient", "TEMAS", campos[0]);
			dealerTemas.excluirPorProcesso();
			
			DBDealer dealerQualif = new DBDealer("VideocapClient", "QUALIFICACOES", campos[0]);
			dealerQualif.excluirPorProcesso();
		}
		
		return true;
	}

}
