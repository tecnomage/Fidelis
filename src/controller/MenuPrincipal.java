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
		
    	String [] titulos = {"Sele��o de Audi�ncias do Banco de Dados Corporativo", "Assuntos CNJ", "Detec��o de Dispositivos de �udio e V�deo", "Grava��o da Audi�ncia"};
    	Vector p = new Vector();    	
    	
    	JPanel p0 = new PanelSelecionaAudienciaCorp(usuarioLogado);
    	JPanel p00 = new PanelPreVinculacao(usuarioLogado);
    	JPanel p1 = new PanelDetectaDispositivos(usuarioLogado);
    	JPanel p2 = new FrameGravaAudiencia(usuarioLogado);
    	p.addElement(p0);
    	p.addElement(p00);
    	p.addElement(p1);
    	p.addElement(p2);
    	
    	JWizard audienciaFrame = new JWizard("Gravar Nova Audi�ncia", titulos, p);
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
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo GRAVAR NOVA AUDIENCIA iniciado.");
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

    	String [] titulos = {"Sele��o de Audi�ncia Pendente", "Detec��o de Dispositivos de �udio e V�deo", "Grava��o da Audi�ncia"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelSelecionaAudienciaTable(usuarioLogado, "PENDENTES", false);
    	JPanel p1 = new PanelDetectaDispositivos(usuarioLogado);    	
    	JPanel p2 = new FrameGravaAudiencia(usuarioLogado);
    	p.addElement(p0);
    	p.addElement(p1);
    	p.addElement(p2);    	
    	    
    	JWizard audienciaFrame = new JWizard("Gravar Audi�ncia Pendente", titulos, p);
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
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo GRAVAR AUDIENCIA PENDENTE iniciado.");
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
		
    	String [] titulos = {"Detec��o de Dispositivos de �udio e V�deo", "Grava��o do Evento"};
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
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo GRAVAR NOVO EVENTO iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void ConexaoRemota() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Conex�o � um Banco de Dados Remoto"};
		PanelConfigDBPlayer    p0 = new PanelConfigDBPlayer(usuarioLogado);
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		
		JWizard wz = new JWizard("Conex�o � um Banco de Dados Remoto", telasTitulos, p);
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
		gl.grava("fidelis.view.MenuPrincipal()", usuarioLogado, "M�dulo Configura��o de Banco de Dados Remoto.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	
	@SuppressWarnings("unchecked")
	public void visualizarAudiencia() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		Sessao.numProcesso = null;
		Sessao.tipo = null;
		Sessao.juiz = null;
		Sessao.nomeDispositivoVideo = null;

    	String [] titulos = {"Sele��o da Audi�ncia", "Visualiza��o da Audi�ncia"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelSelecionaAudienciaTable(usuarioLogado, "", false);
    	JPanel p1 = new FramePlayAudiencia(usuarioLogado, false);
    	p.addElement(p0);
    	p.addElement(p1);    	
    	    
    	JWizard audienciaFrame = new JWizard("Visualiza��o de Audi�ncias", titulos, p);    	
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
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo VISUALIZAR AUDIENCIA iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	@SuppressWarnings("unchecked")
	public void vincularAssuntoCNJ() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		DBCorp corp = new DBCorp(usuarioLogado, "Local");
		if (corp.getConexao() == null) {			
			mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showMessageDialog(null, "N�o foi poss�vel conectar com o Banco de Dados Corporativo.\nPara vincula��o de assuntos CNJ esta conex�o � obrigat�ria.", "ATEN��O", JOptionPane.ERROR_MESSAGE);
		}
		else {
			//corp.fechaConexao();

			Sessao.numProcesso = null;
			Sessao.tipo = null;
			Sessao.juiz = null;
			Sessao.nomeDispositivoVideo = null;
	
	    	String [] titulos = {"Sele��o da Audi�ncia", "Vincula��o de Assuntos CNJ"};
	    	Vector p = new Vector();
	    	    	
	    	JPanel p0 = new PanelSelecionaAudienciaTable(usuarioLogado, "", true);
	    	JPanel p1 = new FramePlayAudiencia(usuarioLogado, true);
	    	p.addElement(p0);
	    	p.addElement(p1);    	
	    	    
	    	JWizard audienciaFrame = new JWizard("Vincula��o de Assuntos CNJ", titulos, p);    	
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
			gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo VINCULAR ASSUNTOS CNJ iniciado.");
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

    	String [] titulos = {"Sele��o do Evento", "Visualiza��o do Evento"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelSelecionaEventoTable(usuarioLogado, "");
    	JPanel p1 = new FramePlayAudiencia(usuarioLogado, false);
    	p.addElement(p0);
    	p.addElement(p1);    	
    	    
    	JWizard audienciaFrame = new JWizard("Visualiza��o de Eventos", titulos, p);    	
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
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo VISUALIZAR EVENTO iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	@SuppressWarnings("unchecked")
	public void cadastroDeUsuarios() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    	String [] titulos = {"Cadastro de Usu�rios"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelGridManutencao(usuarioLogado);
    	p.addElement(p0);
    	    
    	JWizard cadusu = new JWizard("Cadastro de Usu�rios", titulos, p);    	
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
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo CADASTRO DE USUARIOS iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void configuracaoInicial() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Configura��es Gerais", "Configura��es de Banco de Dados", "Cadastro de Tipos de Audi�ncias", "Cadastro de Motivos de Finaliza��es de Audi�ncias", "Cadastro de Perfil de Usu�rios", "Cadastro Inicial de Assuntos/SubTemas", "Cadastro Inicial de Qualifica��es"};
		PanelConfigGerais     p0 = new PanelConfigGerais(usuarioLogado);
		PanelConfigDB         p1 = new PanelConfigDB(usuarioLogado);
		PanelTreeManutencao   p2 = new PanelTreeManutencao(usuarioLogado, "tabela: TIPOS DE AUDI�NCIAS", "TIPOAUDIENCIAS");
		PanelTreeManutencao   p3 = new PanelTreeManutencao(usuarioLogado, "tabela: MOTIVO DE FINALIZA��ES", "MOTIVOFINALIZACOES");
		PanelTreeManutencao   p4 = new PanelTreeManutencao(usuarioLogado, "tabela: PERFIL DE USU�RIOS", "PERFIL");
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
		
		JWizard cadBasico = new JWizard("Configura��o do Sistema", telasTitulos, p);
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
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo CONFIGURACAO DO SISTEMA iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	@SuppressWarnings("unchecked")
	public void manutencaoDB() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Configura��o de Acesso � Base de Dados", "Manuten��o de Tabelas do Banco de Dados"};
		PanelConfigDBRemoto    p0 = new PanelConfigDBRemoto(usuarioLogado);
		PanelManutencaoTabelas p1 = new PanelManutencaoTabelas(usuarioLogado);
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		p.addElement((JPanel)p1);
		
		JWizard wz = new JWizard("Manuten��o de Tabelas do Banco de Dados", telasTitulos, p);
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
		gl.grava("fidelis.view.MenuPrincipal()", usuarioLogado, "M�dulo Configura��o das Tabelas do BD iniciado.");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void relatorios() {
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Relat�rios"};
		PanelRelatorios p0 = new PanelRelatorios(usuarioLogado);
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		
		JWizard wz = new JWizard("Relat�rios", telasTitulos, p);
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
		gl.grava("fidelis.view.MenuPrincipal()", usuarioLogado, "M�dulo de Relat�rios");
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void transmissaoServidor() {
    	if (SessaoConfig.isTransmissaoServerUp) {
    		JOptionPane.showMessageDialog(null, "M�dulo Transmiss�o Servidor j� est� ativo!", "Transmiss�o Servidor", JOptionPane.WARNING_MESSAGE);
    	}
    	else {            		
        	Configuracoes cfg = new Configuracoes();
        	String[] args = new String[2];
        	args[0] = cfg.getConfigParm("diretorio_servidor");
        	args[1] = cfg.getConfigParm("porta");
        	ThreadRunStatic trs = new ThreadRunStatic(args, 1);
        	trs.start();
        	SessaoConfig.isTransmissaoServerUp = true;
        	//JOptionPane.showMessageDialog(null, "M�dulo Transmiss�o Servidor iniciado com sucesso!", "Transmiss�o Servidor", JOptionPane.INFORMATION_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "Arquivos de �udio/V�deo recuperados.", "Aten��o", JOptionPane.INFORMATION_MESSAGE);
		} 
		else
			if (ret == 0)
				JOptionPane.showMessageDialog(null, "N�o existem Arquivos de �udio/V�deo a serem recuperados.", "Aten��o", JOptionPane.INFORMATION_MESSAGE);
    	mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	@SuppressWarnings("unchecked")
	public void importarAudiencia() {
		
		mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String [] telasTitulos = {"Importar Audi�ncia"};
		PanelImportaAudiencia p0 = new PanelImportaAudiencia(usuarioLogado);
		Vector p = new Vector();
		p.addElement((JPanel)p0);
		
		JWizard cadBasico = new JWizard("Importar Audi�ncia", telasTitulos, p);
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
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "M�dulo IMPORTAR AUDIENCIA iniciado.");
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
        		JOptionPane.showMessageDialog(null, "O Arquivo selecionado N�O cont�m dados v�lidos para o formato esperado.", "Importar Audi�ncia", JOptionPane.ERROR_MESSAGE);
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
