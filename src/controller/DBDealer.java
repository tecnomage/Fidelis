package controller;

import java.util.Vector;

import javax.swing.JOptionPane;

import model.Audiencia;
import model.Marcacao;
import model.MotivoFinalizacoes;
import model.Perfil;
import model.Qualifica;
import model.Temas;
import model.TipoAudiencias;

public class DBDealer {
	
	private TipoAudiencias ta = null;
	private MotivoFinalizacoes mf = null;
	private Perfil pf = null;
	private Temas temas = null;
	private Qualifica quali = null;
	private Marcacao marca = null;
	private Audiencia audi = null;

	private int modulo = 0;
	private String usuarioLogado = null;
	private String numProcesso = null;
	private String data = null;
	private String hora = null;
	
	public DBDealer(String strUsuarioLogado, String strModulo) {		
		usuarioLogado = strUsuarioLogado;
		
		if (strModulo.equals("TIPOAUDIENCIAS")) {
			//GravaLog log = new GravaLog();
			//log.grava("fidelis.controller.DBDealer()", usuarioLogado, "Instanciou DBDealer para TIPOS DE AUDIENCIAS");
			modulo = 1;			
		}
		
		if (strModulo.equals("MOTIVOFINALIZACOES")) {
			//GravaLog log = new GravaLog();
			//log.grava("fidelis.controller.DBDealer()", usuarioLogado, "Instanciou DBDealer para MOTIVOS DE FINALIZACOES");
			modulo = 2;			
		}
		
		if (strModulo.equals("PERFIL")) {
			modulo = 7;
		}
		
		if (strModulo.equals("AUDIENCIAS")) {
			modulo = 61;
			// 6 p/ Audiencias e 1 p/ indicar que eh uma limitacao do modulo de audiencias
			// A classe AUDIENCIA serah instanciada sem numero de processo, data e hora somente para recuperar
			// os processos pedentes ... 
			// Isto previde que o DBDealer execute um metodo da classe AUDIENCIA sem os valores chave de 
			// Numero de Process, data e hora.
		}
	}
	
	public DBDealer(String strUsuarioLogado, String strModulo, String strNumProcesso) {
		numProcesso = strNumProcesso;
		usuarioLogado = strUsuarioLogado;
		
		if (strModulo.equals("TEMAS")) {
			//GravaLog log = new GravaLog();
			//log.grava("fidelis.controller.DBDealer()", usuarioLogado, "Instanciou DBDealer para TEMAS E SUBTEMAS");
			modulo = 3;
		}
		
		if (strModulo.equals("QUALIFICACOES")) {
			//GravaLog log = new GravaLog();
			//log.grava("fidelis.controller.DBDealer()", usuarioLogado, "Instanciou DBDealer para QUALIFICACOES E DEPOENTES");
			modulo = 4;
		}
	}
	
	public DBDealer(String strUsuarioLogado, String strModulo, String strNumProcesso, String strData, String strHora) {
		numProcesso = strNumProcesso;
		data = strData;
		hora = strHora;
		usuarioLogado = strUsuarioLogado;
		
		if (strModulo.equals("MARCACOES")) {
			//GravaLog log = new GravaLog();
			//log.grava("fidelis.controller.DBDealer()", usuarioLogado, "Instanciou DBDealer para MARCACOES");
			modulo = 5;
		}
		
		if (strModulo.equals("AUDIENCIAS")) {
			//GravaLog log = new GravaLog();
			//log.grava("fidelis.controller.DBDealer()", usuarioLogado, "Instanciou DBDealer para AUDIENCIAS");
			modulo = 6;
		}
	}
	
	public void setNumProcesso(String strNumProcesso) {
		numProcesso = strNumProcesso;
	}
	
	public void setData(String strData) {
		data = strData;
	}
	
	public void setHora(String strHora) {
		hora = strHora;
	}
	
	public int getProximoCodigo() {
		int ret = -1;		
		if (modulo == 1) { // Tipos de Audiencias
			ta = new TipoAudiencias();
			ret = ta.getDbUltimoCodigo();
		}
		
		if (modulo == 2) { // Motivos de Finalizacoes	
			mf = new MotivoFinalizacoes();
			ret = mf.getDbUltimoCodigo();
		}		
		
		if (modulo == 7) { // Perfil	
			pf = new Perfil();
			ret = pf.getDbUltimoCodigo();
		}
		
		if (modulo == 3) { // Temas e SubTemas	
			temas = new Temas(numProcesso);
			ret = temas.getDbUltimoCodigo();
		}		
		
		if (modulo == 4) { // Qualificacoes e Depoentes	
			quali = new Qualifica(numProcesso);
			ret = quali.getDbUltimoCodigo();
		}	
		return ret;
	}

	public boolean incluir(String strValor) {
		int ret = 0;		
		
		if (modulo == 1) {
			ta = new TipoAudiencias();
			ret = ta.incluir(strValor);
			
			GravaLog log = new GravaLog("INFO");
			if (ret < 0) {
				String msg = "Não foi possivel incluir no banco de dados.";
				log.grava("TIPO DE AUDIENCIAS | fidelis.controller.DBDealer.incluir(" + strValor + ")", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.ERROR_MESSAGE);		
				return false;
			}
			else {
				String msg = "Inclusão realizada com sucesso.";
				log.grava("TIPO DE AUDIENCIAS | fidelis.controller.DBDealer.incluir(" + strValor + ")", usuarioLogado, msg);
				//JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
		}
		
		if (modulo == 2) {
			mf = new MotivoFinalizacoes();
			ret = mf.incluir(strValor);
			GravaLog log = new GravaLog("INFO");
			if (ret < 0) {
				String msg = "Não foi possivel incluir no banco de dados.";
				log.grava("MOTIVO FINALIZACOES | fidelis.controller.DBDealer.incluir(" + strValor + ")", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.ERROR_MESSAGE);	
				return false;
			}
			else {
				String msg = "Inclusão realizada com sucesso.";
				log.grava("MOTIVO FINALIZACOES | fidelis.controller.DBDealer.incluir(" + strValor + ")", usuarioLogado, msg);
				//JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
		}		
		
		if (modulo == 7) {
			pf = new Perfil();
			ret = pf.incluir(strValor);
			
			GravaLog log = new GravaLog("INFO");
			if (ret < 0) {
				String msg = "Não foi possivel incluir no banco de dados.";
				log.grava("PERFIL | fidelis.controller.DBDealer.incluir(" + strValor + ")", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.ERROR_MESSAGE);		
				return false;
			}
			else {
				String msg = "Inclusão realizada com sucesso.";
				log.grava("PERFIL | fidelis.controller.DBDealer.incluir(" + strValor + ")", usuarioLogado, msg);
				//JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
		}
		return false;
	}
	
	public boolean incluir(String strDesc, String strPai) {
		if (modulo == 3) {
			temas = new Temas(numProcesso);
			int ret = temas.incluir(strDesc, strPai);
			String msg = null;
			if (ret > 0) {
				msg = "Inclusão de TEMA/SUBTEMA realizada com sucesso!";
				GravaLog log = new GravaLog("INFO");			
				log.grava("TEMAS/SUBTEMAS | fidelis.controller.DBDealer.incluir(" + numProcesso + "," + strDesc + ", " + strPai + ")", usuarioLogado, msg);
				return true;
			}
			else {
				msg = "Não foi possível incluir TEMA/SUBTEMA no banco de dados.";
				GravaLog log = new GravaLog("INFO");			
				log.grava("TEMAS/SUBTEMAS | fidelis.controller.DBDealer.incluir(" + numProcesso + "," + strDesc + ", " + strPai + ")", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		if (modulo == 4) {
			quali = new Qualifica(numProcesso);
			int ret = quali.incluir(strDesc, strPai);
			String msg = null;
			if (ret > 0) {
				msg = "Inclusão de QUALIFICACAO/DEPOENTE realizada com sucesso!";
				GravaLog log = new GravaLog("INFO");			
				log.grava("QUALIFICACAO/DEPOENTE | fidelis.controller.DBDealer.incluir(" + numProcesso + "," + strDesc + ", " + strPai + ")", usuarioLogado, msg);
				return true;
			}
			else {
				msg = "Não foi possível incluir QUALIFICACAO/DEPOENTE no banco de dados.";
				GravaLog log = new GravaLog("INFO");			
				log.grava("QUALIFICACAO/DEPOENTE | fidelis.controller.DBDealer.incluir(" + numProcesso + "," + strDesc + ", " + strPai + ")", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		if (modulo == 6) {
			audi = new Audiencia(numProcesso, data, hora);
			int ret = audi.incluir(strDesc, strPai); //(Tipo de Audiencia, Juiz)
			String msg = null;
			if (ret > 0) {
				msg = "Inclusão de AUDIENCIA realizada com sucesso!";
				GravaLog log = new GravaLog("INFO");			
				log.grava("AUDIENCIA | fidelis.controller.DBDealer.incluir(" + numProcesso + "," + data + "," + hora + ")", usuarioLogado, msg);
				return true;
			}
			else {
				msg = "Não foi possível incluir AUDIENCIA no banco de dados.";
				GravaLog log = new GravaLog("INFO");			
				log.grava("AUDIENCIA | fidelis.controller.DBDealer.incluir(" + numProcesso + "," + data + "," + hora + ")", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return false;
	}
	
	public boolean incluir(String strTempo, String strTema, String strSubtema, String strQualificacao, String strDepoente) {
		if (modulo == 5) {
			marca = new Marcacao(numProcesso, data, hora);
			int ret = marca.incluir(strTempo, strTema, strSubtema, strQualificacao, strDepoente);
			String msg = null;
			if (ret > 0) {
				msg = "Inclusão da MARCAÇÃO realizada com sucesso!";
				GravaLog log = new GravaLog("INFO");			
				log.grava("MARCACOES | fidelis.controller.DBDealer.incluir(" + numProcesso + "," + data + "," + hora + "," + strTempo + ", " + strTema + " ...)", usuarioLogado, msg);
				return true;
			}
			else {
				msg = "Não foi possível incluir MARCAÇÃO no banco de dados.";
				GravaLog log = new GravaLog("INFO");			
				log.grava("MARCACOES | fidelis.controller.DBDealer.incluir(" + numProcesso + "," + data + "," + hora + "," + strTempo + ", " + strTema + " ...)", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "INCLUIR", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return false;
	}
	
	public boolean excluir(String strValor) {		
		int ret = 0;
		String strModMsg = null;
		
		if (modulo == 1) {
			ta = new TipoAudiencias();
			ret = ta.excluir(strValor);
			strModMsg = "TIPO AUDIENCIAS | fidelis.controller.DBDealer.excluir(" + strValor + ")";
		}
		
		if (modulo == 2) {
			mf = new MotivoFinalizacoes();
			ret = mf.excluir(strValor);
			strModMsg = "MOTIVO FINALIZACOES | fidelis.controller.DBDealer.excluir(" + strValor + ")";
		}				
		
		if (modulo == 7) {
			pf = new Perfil();
			ret = pf.excluir(strValor);
			strModMsg = "PERFIL | fidelis.controller.DBDealer.excluir(" + strValor + ")";
		}	
		
		if (modulo == 3) {
			temas = new Temas(numProcesso);
			ret = temas.excluir(strValor);
			strModMsg = "TEMAS/SUBTEMAS | fidelis.controller.DBDealer.excluir(" + strValor + ")";
		}

		if (modulo == 4) {
			quali = new Qualifica(numProcesso);
			ret = quali.excluir(strValor);
			strModMsg = "QUALIFICACOES/DEPOENTES | fidelis.controller.DBDealer.excluir(" + strValor + ")";
		}
		
		GravaLog log = new GravaLog("INFO");
		if (ret < 0) {
			String msg = "Não foi possivel excluir registro no banco de dados.";	
			log.grava(strModMsg, usuarioLogado, msg);
			JOptionPane.showMessageDialog(null, msg, "EXCLUIR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {
			String msg = "Exclusão realizada com sucesso.";
			log.grava(strModMsg, usuarioLogado, msg);
			return true;
		}
	}
	
	
	public boolean excluir() {		
		int ret = 0;
		String strModMsg = null;

		if (modulo == 5) {
			Marcacao marca = new Marcacao(numProcesso, data, hora);
			ret = marca.excluir();
			strModMsg = "MARCACOES | fidelis.controller.DBDealer.excluir()";
		}
				
		if (modulo == 6) {
			Audiencia audi = new Audiencia(numProcesso, data, hora);
			ret = audi.excluir();
			strModMsg = "AUDIENCIAS | fidelis.controller.DBDealer.excluir()";
		}
		
		GravaLog log = new GravaLog("INFO");
		if (ret < 0) {
			String msg = "Não foi possivel excluir registro no banco de dados.";	
			log.grava(strModMsg, usuarioLogado, msg);
			JOptionPane.showMessageDialog(null, msg, "EXCLUIR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {
			String msg = "Exclusão realizada com sucesso.";
			log.grava(strModMsg, usuarioLogado, msg);
			return true;
		}
	}
	
	
	public boolean excluirPorProcesso() {		
		int ret = 0;
		String strModMsg = null;

		if (modulo == 3) {
			Temas tema = new Temas(numProcesso);
			ret = tema.excluirPorProcesso();
			strModMsg = "TEMAS | fidelis.controller.DBDealer.excluirPorProcesso()";
		}
				
		if (modulo == 4) {
			Qualifica qualif = new Qualifica(numProcesso);
			ret = qualif.excluirPorProcesso();
			strModMsg = "QUALIFICA | fidelis.controller.DBDealer.excluirPorProcesso()";
		}
		
		GravaLog log = new GravaLog("INFO");
		if (ret < 0) {
			String msg = "Não foi possivel excluir registro no banco de dados.";	
			log.grava(strModMsg, usuarioLogado, msg);
			JOptionPane.showMessageDialog(null, msg, "EXCLUIR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {
			String msg = "Exclusão realizada com sucesso.";
			log.grava(strModMsg, usuarioLogado, msg);
			return true;
		}
	}
	
	
	public boolean atualizar(String strValorAntigo, String strValorNovo) {
		int ret = 0;
		String strModMsg = null;
		
		if (modulo == 1) {
			ta = new TipoAudiencias();
			ret = ta.alterar(strValorAntigo, strValorNovo);
			strModMsg = "TIPO AUDIENCIAS | fidelis.controller.DBDealer.atualizar(" + strValorAntigo + "," + strValorNovo + ")";
		}
		
		if (modulo == 2) {
			mf = new MotivoFinalizacoes();
			ret = mf.alterar(strValorAntigo, strValorNovo);
			strModMsg = "MOTIVO FINALIZACOES | fidelis.controller.DBDealer.atualizar(" + strValorAntigo + "," + strValorNovo + ")";
		}		
		
		if (modulo == 7) {
			pf = new Perfil();
			ret = pf.alterar(strValorAntigo, strValorNovo);
			strModMsg = "PERFIL | fidelis.controller.DBDealer.atualizar(" + strValorAntigo + "," + strValorNovo + ")";
		}
		
		if (modulo == 3) {
			temas = new Temas(numProcesso);
			ret = temas.alterar(strValorAntigo, strValorNovo);
			strModMsg = "TEMAS/SUBTEMAS | fidelis.controller.DBDealer.atualizar(" + strValorAntigo + "," + strValorNovo + ")";
		}	

		if (modulo == 4) {
			quali = new Qualifica(numProcesso);
			ret = quali.alterar(strValorAntigo, strValorNovo);
			strModMsg = "QUALIFICACOES/DEPOENTES | fidelis.controller.DBDealer.atualizar(" + strValorAntigo + "," + strValorNovo + ")";
		}	
		
		GravaLog log = new GravaLog("INFO");
		if (ret < 0) {
			String msg = "Não foi possivel alterar registro no banco de dados.";
			log.grava(strModMsg, usuarioLogado, msg);
			JOptionPane.showMessageDialog(null, msg, "ALTERAR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {
			String msg = "Alteração realizada com sucesso.";
			log.grava(strModMsg, usuarioLogado, msg);
			//JOptionPane.showMessageDialog(null, msg, "ALTERAR", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public Vector listar() {
		Vector ret = null;		
		//GravaLog log = new GravaLog();
		
		if (modulo == 1) {
			ta = new TipoAudiencias();
			ret = ta.lista();
			//log.grava("fidelis.controller.DBDealer.listar()", usuarioLogado, "Carregou dados de TIPO DE AUDIENCIAS");
		}
		
		if (modulo == 2) {
			mf = new MotivoFinalizacoes();
			ret = mf.lista();
			//log.grava("fidelis.controller.DBDealer.listar()", usuarioLogado, "Carregou dados de MOTIVOS DE FINALIZACOES");
		}
		
		if (modulo == 7) {
			pf = new Perfil();
			ret = pf.lista();
			//log.grava("fidelis.controller.DBDealer.listar()", usuarioLogado, "Carregou dados de TIPO DE AUDIENCIAS");
		}

		if (modulo == 5) {
			marca = new Marcacao(numProcesso, data, hora);
			ret = marca.carregar();
			//log.grava("fidelis.controller.DBDealer.listar()", usuarioLogado, "Carregou dados de MARCACOES");
		}

		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector listarTudo() {
		Vector ret = null;		
		
		if (modulo == 7) {
			pf = new Perfil();
			ret = pf.listaAll();
		}
		
		return ret;
	}

	
	public int isProcessoConcluido() {
		int ret = 0;
		if (modulo == 6) {			
			audi = new Audiencia(numProcesso, data, hora);
			ret = audi.isProcessoConcluido();
			//GravaLog log = new GravaLog();
			//log.grava("fidelis.controller.DBDealer.isProcessoConcluido(" + numProcesso + ")", usuarioLogado, "Verificou se o processo está concluido.");
		}
		return ret;
	}
	
	public int isProcInterrompidoEmD0() {
		int ret = 0;
		if (modulo == 6) {			
			audi = new Audiencia(numProcesso, data, hora);
			ret = audi.isProcInterrompidoEmD0();
		}
		return ret;
	}
	
	public boolean mudaStatus(String strMotivoFinalizacao) {
		int ret = 0;		
		if (modulo == 6) {
			audi = new Audiencia(numProcesso, data, hora);
			ret = audi.alterar(strMotivoFinalizacao);
			GravaLog log = new GravaLog("INFO");
			if (ret < 0) {
				String msg = "Não foi possivel gravar o motivo da finalização da audiência.";	
				log.grava("AUDIENCIA | fidelis.controller.DBDealer.mudaStatus(" + strMotivoFinalizacao + ")", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "INTERROMPER AUDIÊNCIA", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			else {
				String msg = "Alteração do MOTIVO DE FINALIZACAO na AUDIENCIA foi realizada com sucesso.";
				log.grava("AUDIENCIA | fidelis.controller.DBDealer.mudaStatus(" + strMotivoFinalizacao + ")", usuarioLogado, msg);
				return true;
			}
		}
		return false;
	}
	
	public boolean gravarHash(String strHash) {
		int ret = 0;		
		if (modulo == 6) {
			audi = new Audiencia(numProcesso, data, hora);
			ret = audi.gravarHash(strHash);
			GravaLog log = new GravaLog("INFO");
			if (ret < 0) {
				String msg = "Não foi possivel gravar a assinatura(HASH) do vídeo gravado na audiência.";	
				log.grava("AUDIENCIA | fidelis.controller.DBDealer.gravarHash(" + strHash + ")", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "CONCLUSÃO DA AUDIÊNCIA", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			else {
				String msg = "Gravação da assinatura(HASH) do vídeo realizada com sucesso.";
				log.grava("AUDIENCIA | fidelis.controller.DBDealer.gravarHash(" + strHash + ")", usuarioLogado, msg);
				return true;
			}
		}
		return false;		
	}
	
	@SuppressWarnings("unchecked")
	public Vector getAudienciasPendentes() {
		Vector ret = null;
		
		if (modulo == 61) {
			Audiencia audi61 = new Audiencia();
			ret = audi61.getAudienciasPendentes();
		}
		
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Vector getAudiencias() {
		Vector ret = null;
		
		if (modulo == 61) {
			Audiencia audi61 = new Audiencia();
			ret = audi61.getAudiencias();
		}
		
		return ret;
	}
	
	
	@SuppressWarnings("unchecked")
	public Vector getAudienciasConcluidas() {
		Vector ret = null;
		
		if (modulo == 61) {
			Audiencia audi61 = new Audiencia();
			ret = audi61.getAudienciasConcluidas();
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector getAudienciasTransmitidas() {
		Vector ret = null;
		
		if (modulo == 61) {
			Audiencia audi61 = new Audiencia();
			ret = audi61.getAudienciasTransmitidas();
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector getAudienciasComErro() {
		Vector ret = null;
		
		if (modulo == 61) {
			Audiencia audi61 = new Audiencia();
			ret = audi61.getAudienciasComErro();
		}
		
		return ret;
	}

	public String [] getAudiencia() {
		String [] ret = null;
		
		if (modulo == 6) {
			Audiencia audi = new Audiencia(numProcesso, data, hora);
			ret = audi.getAudiencia();
		}
		
		return ret;
	}

	public String getDescTipoAudiencia(int cod) {
		String ret = null;
		
		if (modulo == 61) {
			TipoAudiencias ta = new TipoAudiencias();
			ret = ta.getDescTipoAudiencia(cod);			
			
			if (ret == null) {
				GravaLog log = new GravaLog("INFO");
				String msg = "Não foi possivel recuperar a descrição do tipo de audiencia Codigo = " + cod;	
				log.grava("AUDIENCIA | fidelis.controller.DBDealer.getDescTipoAudiencia()", usuarioLogado, msg);
				JOptionPane.showMessageDialog(null, msg, "SELEÇÃO DE AUDIÊNCIA PENDENTE", JOptionPane.ERROR_MESSAGE);
				return "";
			}
		}
		
		return ret;
	}
}
