package controller;

import java.awt.Cursor;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Configuracoes;
import model.DBCorp;
import view.FidelisMain;
import view.FrameGravaAudiencia;
import view.FrameGravaEvento;
import view.FramePlayAudiencia;
import view.JWizard;
import view.PanelConfigDB;
import view.PanelConfigDBPlayer;
import view.PanelConfigDBRemoto;
import view.PanelConfigGerais;
import view.PanelDetectaDispositivos;
import view.PanelGridManutencao;
import view.PanelImportaAudiencia;
import view.PanelManutencaoTabelas;
import view.PanelPreVinculacao;
import view.PanelSelecionaAudienciaCorp;
import view.PanelSelecionaAudienciaTable;
import view.PanelSelecionaEventoTable;
import view.PanelTreeManutencao;
import view.PanelTreeTemasEQualif;

public class MenuPrincipal {

	private JFrame mainFrame;
	private String usuarioLogado;
	private JDesktopPane desktop;

	public MenuPrincipal() {
		mainFrame = SessaoConfig.mainFrame;
		usuarioLogado = SessaoConfig.usuarioLogado;
		desktop = ((FidelisMain)mainFrame).getDesktop();
	}
	
	@SuppressWarnings("unchecked")
	public void gravarNovaAudiencia() {		
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Sessao.numProcesso = null;
		Sessao.tipo = null;
		Sessao.juiz = null;
		Sessao.autor = null;
		Sessao.nomeDispositivoVideo = null;
		Sessao.nomeDispositivoAudio = null;
		
    	String [] titulos = {"Seleção de Audiências do Banco de Dados Corporativo", "Assuntos CNJ", "Detecção de Dispositivos de Áudio e Vídeo", "Gravação da Audiência"};
    	Vector p = new Vector();    	
    	
    	JPanel p0 = new PanelSelecionaAudienciaCorp(usuarioLogado);
    	JPanel p00 = new PanelPreVinculacao(usuarioLogado);
    	JPanel p1 = new PanelDetectaDispositivos(usuarioLogado);
    	JPanel p2 = new FrameGravaAudiencia(usuarioLogado);
    	p.addElement(p0);
    	p.addElement(p00);
    	p.addElement(p1);
    	p.addElement(p2);
    	
    	JWizard audienciaFrame = new JWizard("Gravar Nova Audiência", titulos, p);
    	((FrameGravaAudiencia)p2).close(audienciaFrame);
    	audienciaFrame.setSize(1000, 670);
    	audienciaFrame.setVisible(true);
    	mainFrame.setContentPane(desktop);
    	desktop.add(audienciaFrame);
    	try {
			audienciaFrame.setSelected(true);
			audienciaFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo GRAVAR NOVA AUDIENCIA iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void gravarAudienciaPendente() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		Sessao.numProcesso = null;
		Sessao.tipo = null;
		Sessao.juiz = null;
		Sessao.autor = null;
		Sessao.nomeDispositivoVideo = null;
		Sessao.nomeDispositivoAudio = null;

    	String [] titulos = {"Seleção de Audiência Pendente", "Detecção de Dispositivos de Áudio e Vídeo", "Gravação da Audiência"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelSelecionaAudienciaTable(usuarioLogado, "PENDENTES", false);
    	JPanel p1 = new PanelDetectaDispositivos(usuarioLogado);    	
    	JPanel p2 = new FrameGravaAudiencia(usuarioLogado);
    	p.addElement(p0);
    	p.addElement(p1);
    	p.addElement(p2);    	
    	    
    	JWizard audienciaFrame = new JWizard("Gravar Audiência Pendente", titulos, p);
    	((FrameGravaAudiencia)p2).close(audienciaFrame);
    	audienciaFrame.setSize(1000, 670);
    	audienciaFrame.setVisible(true);
    	mainFrame.setContentPane(desktop);
    	desktop.add(audienciaFrame);
    	try {
			audienciaFrame.setSelected(true);
			audienciaFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo GRAVAR AUDIENCIA PENDENTE iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	@SuppressWarnings("unchecked")
	public void gravarNovoEvento() {		
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Sessao.numProcesso = null;
		Sessao.tipo = null;
		Sessao.juiz = null;
		Sessao.nomeDispositivoVideo = null;
		Sessao.nomeDispositivoAudio = null;
		
    	String [] titulos = {"Detecção de Dispositivos de Áudio e Vídeo", "Gravação do Evento"};
    	Vector p = new Vector();    	
    	
    	JPanel p0 = new PanelDetectaDispositivos(usuarioLogado);
    	JPanel p1 = new FrameGravaEvento(usuarioLogado);
    	p.addElement(p0);
    	p.addElement(p1);
    	    
    	JWizard eventoF = new JWizard("Gravar Novo Evento", titulos, p);
    	((FrameGravaEvento)p1).close(eventoF);
    	eventoF.setSize(1000, 670);
    	eventoF.setVisible(true);
    	mainFrame.setContentPane(desktop);
    	desktop.add(eventoF);
    	try {
			eventoF.setSelected(true);
			eventoF.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo GRAVAR NOVO EVENTO iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void ConexaoRemota() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Conexão à um Banco de Dados Remoto"};
		PanelConfigDBPlayer    p0 = new PanelConfigDBPlayer(usuarioLogado);
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		
		JWizard wz = new JWizard("Conexão à um Banco de Dados Remoto", telasTitulos, p);
		wz.setVisible(true);		
    	mainFrame.setContentPane(desktop);
    	desktop.add(wz);
    	try {
			wz.setSelected(true);
			wz.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.MenuPrincipal()", usuarioLogado, "Módulo Configuração de Banco de Dados Remoto.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	
	@SuppressWarnings("unchecked")
	public void visualizarAudiencia() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		Sessao.numProcesso = null;
		Sessao.tipo = null;
		Sessao.juiz = null;
		Sessao.nomeDispositivoVideo = null;

    	String [] titulos = {"Seleção da Audiência", "Visualização da Audiência"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelSelecionaAudienciaTable(usuarioLogado, "", false);
    	JPanel p1 = new FramePlayAudiencia(usuarioLogado, false);
    	p.addElement(p0);
    	p.addElement(p1);    	
    	    
    	JWizard audienciaFrame = new JWizard("Visualização de Audiências", titulos, p);    	
    	audienciaFrame.setSize(900, 670);
    	audienciaFrame.setVisible(true);
    	mainFrame.setContentPane(desktop);
    	desktop.add(audienciaFrame);
    	try {
			audienciaFrame.setSelected(true);
			audienciaFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo VISUALIZAR AUDIENCIA iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	@SuppressWarnings("unchecked")
	public void vincularAssuntoCNJ() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		DBCorp corp = new DBCorp(usuarioLogado, "Local");
		if (corp.getConexao() == null) {			
			mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showMessageDialog(null, "Não foi possível conectar com o Banco de Dados Corporativo.\nPara vinculação de assuntos CNJ esta conexão é obrigatória.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
		}
		else {
			//corp.fechaConexao();

			Sessao.numProcesso = null;
			Sessao.tipo = null;
			Sessao.juiz = null;
			Sessao.nomeDispositivoVideo = null;
	
	    	String [] titulos = {"Seleção da Audiência", "Vinculação de Assuntos CNJ"};
	    	Vector p = new Vector();
	    	    	
	    	JPanel p0 = new PanelSelecionaAudienciaTable(usuarioLogado, "", true);
	    	JPanel p1 = new FramePlayAudiencia(usuarioLogado, true);
	    	p.addElement(p0);
	    	p.addElement(p1);    	
	    	    
	    	JWizard audienciaFrame = new JWizard("Vinculação de Assuntos CNJ", titulos, p);    	
	    	audienciaFrame.setSize(900, 670);
	    	audienciaFrame.setVisible(true);
	    	mainFrame.setContentPane(desktop);
	    	desktop.add(audienciaFrame);
	    	try {
				audienciaFrame.setSelected(true);
				audienciaFrame.setMaximum(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			GravaLog gl = new GravaLog("INFO");
			gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo VINCULAR ASSUNTOS CNJ iniciado.");
			mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	

	@SuppressWarnings("unchecked")
	public void visualizarEvento() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		Sessao.numProcesso = null;
		Sessao.tipo = null;
		Sessao.juiz = null;
		Sessao.nomeDispositivoVideo = null;

    	String [] titulos = {"Seleção do Evento", "Visualização do Evento"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelSelecionaEventoTable(usuarioLogado, "");
    	JPanel p1 = new FramePlayAudiencia(usuarioLogado, false);
    	p.addElement(p0);
    	p.addElement(p1);    	
    	    
    	JWizard audienciaFrame = new JWizard("Visualização de Eventos", titulos, p);    	
    	audienciaFrame.setSize(900, 670);
    	audienciaFrame.setVisible(true);
    	mainFrame.setContentPane(desktop);
    	desktop.add(audienciaFrame);
    	try {
			audienciaFrame.setSelected(true);
			audienciaFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo VISUALIZAR EVENTO iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	@SuppressWarnings("unchecked")
	public void cadastroDeUsuarios() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    	String [] titulos = {"Cadastro de Usuários"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelGridManutencao(usuarioLogado);
    	p.addElement(p0);
    	    
    	JWizard cadusu = new JWizard("Cadastro de Usuários", titulos, p);    	
    	cadusu.setSize(900, 670);
    	cadusu.setVisible(true);
    	mainFrame.setContentPane(desktop);
    	desktop.add(cadusu);
    	try {
			cadusu.setSelected(true);
			cadusu.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo CADASTRO DE USUARIOS iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void configuracaoInicial() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Configurações Gerais", "Configurações de Banco de Dados", "Cadastro de Tipos de Audiências", "Cadastro de Motivos de Finalizações de Audiências", "Cadastro de Perfil de Usuários", "Cadastro Inicial de Assuntos/SubTemas", "Cadastro Inicial de Qualificações"};
		PanelConfigGerais     p0 = new PanelConfigGerais(usuarioLogado);
		PanelConfigDB         p1 = new PanelConfigDB(usuarioLogado);
		PanelTreeManutencao   p2 = new PanelTreeManutencao(usuarioLogado, "tabela: TIPOS DE AUDIÊNCIAS", "TIPOAUDIENCIAS");
		PanelTreeManutencao   p3 = new PanelTreeManutencao(usuarioLogado, "tabela: MOTIVO DE FINALIZAÇÕES", "MOTIVOFINALIZACOES");
		PanelTreeManutencao   p4 = new PanelTreeManutencao(usuarioLogado, "tabela: PERFIL DE USUÁRIOS", "PERFIL");
		PanelTreeTemasEQualif p5 = new PanelTreeTemasEQualif(usuarioLogado, "1111111-11.1111.1.11.1111", "TEMAS");
		PanelTreeTemasEQualif p6 = new PanelTreeTemasEQualif(usuarioLogado, "1111111-11.1111.1.11.1111", "QUALIFICACOES");
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		p.addElement((JPanel)p1);
		p.addElement((JPanel)p2);
		p.addElement((JPanel)p3);
		p.addElement((JPanel)p4);
		p.addElement((JPanel)p5);
		p.addElement((JPanel)p6);
		
		JWizard cadBasico = new JWizard("Configuração do Sistema", telasTitulos, p);
		cadBasico.setVisible(true);		
    	mainFrame.setContentPane(desktop);
    	desktop.add(cadBasico);
    	try {
			cadBasico.setSelected(true);
			cadBasico.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo CONFIGURACAO DO SISTEMA iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	@SuppressWarnings("unchecked")
	public void manutencaoDB() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Configuração de Acesso à Base de Dados", "Manutenção de Tabelas do Banco de Dados"};
		PanelConfigDBRemoto    p0 = new PanelConfigDBRemoto(usuarioLogado);
		PanelManutencaoTabelas p1 = new PanelManutencaoTabelas(usuarioLogado);
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		p.addElement((JPanel)p1);
		
		JWizard wz = new JWizard("Manutenção de Tabelas do Banco de Dados", telasTitulos, p);
		wz.setVisible(true);		
    	mainFrame.setContentPane(desktop);
    	desktop.add(wz);
    	try {
			wz.setSelected(true);
			wz.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.MenuPrincipal()", usuarioLogado, "Módulo Configuração das Tabelas do BD iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void relatorios() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Relatórios"};
		PanelRelatorios p0 = new PanelRelatorios(usuarioLogado);
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		
		JWizard wz = new JWizard("Relatórios", telasTitulos, p);
		wz.setVisible(true);		
    	mainFrame.setContentPane(desktop);
    	desktop.add(wz);
    	try {
			wz.setSelected(true);
			wz.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.MenuPrincipal()", usuarioLogado, "Módulo de Relatórios");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void transmissaoServidor() {
    	if (SessaoConfig.isTransmissaoServerUp) {
    		JOptionPane.showMessageDialog(null, "Módulo Transmissão Servidor já está ativo!", "Transmissão Servidor", JOptionPane.WARNING_MESSAGE);
    	}
    	else {            		
        	Configuracoes cfg = new Configuracoes();
        	String[] args = new String[2];
        	args[0] = cfg.getConfigParm("diretorio_servidor");
        	args[1] = cfg.getConfigParm("porta");
        	ThreadRunStatic trs = new ThreadRunStatic(args, 1);
        	trs.start();
        	SessaoConfig.isTransmissaoServerUp = true;
        	//JOptionPane.showMessageDialog(null, "Módulo Transmissão Servidor iniciado com sucesso!", "Transmissão Servidor", JOptionPane.INFORMATION_MESSAGE);
    	}
	}
	
	public void transmissaoCliente() {
    	String[] args = new String[1];
    	args[0] = "S"; // Flag de interacao com o usuario. Se "S", mostra os messagebox de mensagem !!!
    	ThreadRunStatic trs = new ThreadRunStatic(args, 2);
    	trs.start();        
	}
	
	public void deteccaoDispositivos() {
    	mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	ThreadRunStatic trs = new ThreadRunStatic(null, 3);
    	trs.start();
    	mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void recuperarMedia() {
    	mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	RecuperaMedia rm = new RecuperaMedia(usuarioLogado);
		int ret = rm.recupera();
		if (ret == 1) {
			JOptionPane.showMessageDialog(null, "Arquivos de Áudio/Vídeo recuperados.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
		} 
		else
			if (ret == 0)
				JOptionPane.showMessageDialog(null, "Não existem Arquivos de Áudio/Vídeo a serem recuperados.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
    	mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void importarAudiencia() {
		
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Importar Audiência"};
		PanelImportaAudiencia p0 = new PanelImportaAudiencia(usuarioLogado);
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		
		JWizard cadBasico = new JWizard("Importar Audiência", telasTitulos, p);
		cadBasico.setVisible(true);		
    	mainFrame.setContentPane(desktop);
    	desktop.add(cadBasico);
    	try {
			cadBasico.setSelected(true);
			cadBasico.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo IMPORTAR AUDIENCIA iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
		
		/*
    	JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);  
        fc.addChoosableFileFilter(new FiltroXML());

        int res = fc.showOpenDialog(null);        
        if (res == JFileChooser.APPROVE_OPTION) {
        	File fRet = fc.getSelectedFile();
        	XMLControl xc = new XMLControl(usuarioLogado, fRet);
        	if (xc.getDadosProcesso(fRet)) {
        		ImportarAudiencia ia = new ImportarAudiencia(xc.numProcesso, xc.data, xc.hora);
        		if (ia.copiaVideo(fRet)) {
	        		DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", xc.numProcesso, xc.data, xc.hora);
	        		dealer.incluir(xc.tipo, xc.juiz);
        		}
        	}
        	else {
        		JOptionPane.showMessageDialog(null, "O Arquivo selecionado NÃO contém dados válidos para o formato esperado.", "Importar Audiência", JOptionPane.ERROR_MESSAGE);
        	}
        }
        */
	}
}

class FiltroXML extends javax.swing.filechooser.FileFilter {
    public boolean accept(File file) {
        String filename = file.getName();
        return file.isDirectory() || filename.endsWith(".xml");
    }
    public String getDescription() {
        return "Arquivos XML";
    }
}
