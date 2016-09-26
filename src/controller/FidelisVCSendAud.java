package controller;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Audiencia;
import model.BancodeDados;
import model.Configuracoes;
import model.Operador;
import view.Progress;

@SuppressWarnings("serial")
public class FidelisVCSendAud extends JFrame{
	
	private static String strIPServer;
	//private static int iPorta;
	private static boolean interacao = false;
	private static JTextArea taTexto;
	private static String strVersao = "FIDELIS 1.09 - Transmissão de Audiências";
	private static PrintWriter pwVerbose;

	public FidelisVCSendAud() {
		setSize(640, 480);
		setTitle(strVersao);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		JPanel pEsq = new JPanel();
		JLabel lbLogo = new JLabel(new ImageIcon("images/FidelisVCSendAud.png"));
		pEsq.add(lbLogo);

		JPanel pDir = new JPanel();
		taTexto = new JTextArea();
		taTexto.setEditable(false);
		taTexto.setRows(25);
		taTexto.setColumns(51);
		taTexto.setWrapStyleWord(true);
		taTexto.setLineWrap(true);
		taTexto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		JScrollPane js = new JScrollPane(taTexto);
		pDir.add(js);
		
		JPanel pSul = new JPanel();
		JButton btInterromper = new JButton("Fechar");
		pSul.add(btInterromper);
		pSul.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		this.add(pEsq, BorderLayout.WEST);
		this.add(pDir, BorderLayout.CENTER);
		this.add(pSul, BorderLayout.SOUTH);
		
		btInterromper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Object[] options = { "Sim", "Não" };
            	int confirma = JOptionPane.showOptionDialog(null, "Deseja realmente fechar a janela de transmissão de audiências?", "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);                
                if (confirma == 0) {    
                	pwVerbose.flush();
                	pwVerbose.close();
                	setVisible(false);
                	dispose();
                }
            }
        });
	}
	
	@SuppressWarnings("static-access")
	public static void main(String [] args) {
		
		Ferramentas fr = new Ferramentas();
		SessaoConfig.vara = fr.getPropriedades("vara");
		
		if (SessaoConfig.vara == null || SessaoConfig.vara.equals("")) {
			JOptionPane.showMessageDialog(null, "Tag VARA não pôde ser lida do arquivo fidelis.propriedades.xml.\nA chave não pode ser validada sem esta informação.", "Atenção", JOptionPane.ERROR_MESSAGE);
			System.exit(-10);
		}
		
		SimpleDateFormat dfHoje = new SimpleDateFormat("yyyyMMdd");
		String sHoje = dfHoje.format(new Date());		
		ValidaChave vc = new ValidaChave(sHoje);
		
		if (!vc.isChaveValida()) {
			JOptionPane.showMessageDialog(null, "A chave de acesso ao sistema não é válida ou está vencida.\nPor favor entre em contato com o TRT 9a. Região para continuar usufruindo das funcionalidades do sistema.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}		
		
		final FidelisVCSendAud vCapClient = new FidelisVCSendAud();
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
		vCapClient.setLocation((tela.width - vCapClient.getSize().width) / 2, (tela.height - vCapClient.getSize().height) / 2);   
		//vCapClient.setVisible(true);
		
		/*
		if (args.length != 1) {
			System.out.println("VideocapClient.main(): Sintaxe: VideocapClient <interacao [S|N]>");
			JOptionPane.showMessageDialog(null, "O VideocapCliente precisa receber como parâmetro o seguinte valor:\nExemplo: VideocapClient <interacao [S|N]>", "Atenção", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		*/
		
		//Configuracoes cini = new Configuracoes();		
		//strIPServer = cini.getConfigParm("endereco_servidor");
		//iPorta = Integer.valueOf(cini.getConfigParm("porta"));
		Ferramentas frr = new Ferramentas();
		strIPServer = frr.getPropriedades("ftpsrv");

		if (args != null && args.length > 0 && args[0].equals("S"))
			interacao = true;
		
		try {
			SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy HHmmss");
			String strTimestamp = df.format(new Date());			
			pwVerbose = new PrintWriter(System.getProperty("user.dir") + "/logs/FidelisVCSendAudVerbose." + SessaoConfig.vara + "." + strTimestamp + ".log");			
		} catch (IOException e1) {
			taTexto.append("\n*** ATENÇÃO: O Arquivo de Verbose não pôde ser criado.\n");
		}
		
		taTexto.append(strVersao);
		taTexto.append("\nEndereço do Servidor -> " + strIPServer); // + ", Porta -> " + Integer.valueOf(iPorta).toString());
		pwVerbose.println(strVersao);
		pwVerbose.println("Endereço do Servidor -> " + strIPServer); // + ", Porta -> " + Integer.valueOf(iPorta).toString());

		
		//Progress wp = null;
		//Operador op = new Operador();
		//GravaLog log = new GravaLog("INFO");
		
		
		if (interacao) {
			//wp = new Progress("Transmitindo audiências para o servidor ...");
			//wp.setVisible(true);
			vCapClient.setVisible(true);
			taTexto.append("\n\nInteração com usuário -> Ligada");
			pwVerbose.println("Interação com usuário -> Ligada");
			carregaProcessos();			
		}
		else {
			vCapClient.setVisible(false);
		    if (!SystemTray.isSupported()) {		    	
		    	GravaLog log = new GravaLog("INFO");
		    	log.grava("fidelis.controller.FidelisVCSendAud()", SessaoConfig.usuarioLogado, "SystemTray não é suportado por este sistema operacional.");
		    	JOptionPane.showMessageDialog(null, "SystemTray não é suportado por este sistema operacional.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
		    }
		    
		    TrayIcon trayIcon = null;
		    SystemTray tray = SystemTray.getSystemTray();
	    	Image image = Toolkit.getDefaultToolkit().getImage("images/transmissaoservidor2.gif");

	        ActionListener listener = new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        		if (arg0.getActionCommand().toString().equals("Ocultar")) {
	        			vCapClient.setVisible(false);
	        		}
	        		if (arg0.getActionCommand().toString().equals("Mostrar")) {
	        			vCapClient.setVisible(true);
	        		}
	        		if (arg0.getActionCommand().toString().equals("Sair")) {
	        			System.exit(0);
	        		}
	        	}
	        };
	         
	        PopupMenu popup = new PopupMenu();
	         
	        MenuItem defaultItem = new MenuItem("Ocultar");
	        defaultItem.addActionListener(listener);
	        popup.add(defaultItem);
	        defaultItem = new MenuItem("Mostrar");
	        defaultItem.addActionListener(listener);
	        popup.add(defaultItem);
	        MenuItem sp = new MenuItem("-");
	        popup.add(sp);
	        defaultItem = new MenuItem("Sair");
	        defaultItem.addActionListener(listener);
	        popup.add(defaultItem);
	         
	        trayIcon = new TrayIcon(image, "FidelisVCSendAud", popup);
	        trayIcon.addActionListener(listener);

	        try {
	            tray.add(trayIcon);
	        } catch (AWTException e) {
		    	GravaLog log = new GravaLog("ERRO");
		    	log.gravaExcep("fidelis.controller.FidelisVCSendAud()", e);
		    	JOptionPane.showMessageDialog(null, "Erro ao adicionar icone ao SystemTray.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
	        }
	        
			while (true) {
				//log.grava("fidelis.controller.VideocapClient()", op.getOperador() + " (SO)", "DAEMON DE TRANSMISSAO DE AUDIENCIAS - CICLO DE TRANSMISSAO INICIADO, AGUARDANDO JANELA DE TRANSMISSAO ...");
				//System.out.println("DAEMON DE TRANSMISSAO DE AUDIENCIAS");
				//System.out.println("CICLO DE TRANSMISSAO INICIADO, AGUARDANDO JANELA DE TRANSMISSAO ...");
				taTexto.append("\n\nInteração com usuário -> Desligada");
				pwVerbose.println("Interação com usuário -> Desligada");
				
				BancodeDados db = new BancodeDados();
				if (db.getConexao() != null) {			
					Configuracoes cfg = new Configuracoes();
					String strHoraInicio = cfg.getConfigParm("horario_inicial");
					int iHoraInicio = Integer.valueOf(strHoraInicio.substring(0,2) + strHoraInicio.substring(3));			
					int iHoraAgora = -1;
					//System.out.println("Hora Inicial da Janela: " + strHoraInicio);
					taTexto.append("\nAguardando a janela de transmissão. Inicio -> " + strHoraInicio);
					pwVerbose.println("Aguardando a janela de transmissão. Inicio -> " + strHoraInicio);
					//log.grava("fidelis.controller.VideocapClient()", op.getOperador() + " (SO)", "Hora Inicial da Janela: " + strHoraInicio);
					String strAgora = null;
					
					while (iHoraAgora < iHoraInicio) {				
						SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm");			
						strAgora = dfHora.format(new Date());
						iHoraAgora = Integer.valueOf(strAgora.substring(0,2) + strAgora.substring(3));
						
						try {
							Thread.currentThread().sleep(60000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}			
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					String strTimestamp = df.format(new Date());
					//System.out.println("Iniciando Transmissao Em: " + strAgora);
					taTexto.append("\n" + strTimestamp + " | Iniciando Transmissão ...");
					pwVerbose.println(strTimestamp + " | Iniciando Transmissão ...");
					//log.grava("fidelis.controller.VideocapClient()", op.getOperador() + " (SO)", "Iniciando Transmissao Em: " + strAgora);
					carregaProcessos();
					pwVerbose.flush();
				}
				else {
					try {
						Thread.currentThread().sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}					
			}
		}
		
		if (interacao) {
			//wp.setVisible(false);
			//wp.dispose();		
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strTimestamp = df.format(new Date());
			
			taTexto.append("\n" + strTimestamp + " | Transmissão das audiências finalizada.");
			pwVerbose.println(strTimestamp + " | Transmissão das audiências finalizada.");
			//JOptionPane.showMessageDialog(null, "Transmissão das audiências finalizada!", "Transmissão de Audiências", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			String strCmd = "cmd /c " + System.getProperty("user.dir") + "/transflog.bat " + System.getProperty("user.dir");
			try {
				Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + strCmd);
			} catch (IOException e) {
				GravaLog log = new GravaLog("ERRO");
				log.gravaExcep("FidelisVCSendAud - Modo Batch", e);
			}
		}
		
		pwVerbose.close();
	}
	
	@SuppressWarnings("unchecked")
	public static void carregaProcessos() {
		DBDealer dealer = new DBDealer("FidelisVCSendAud", "AUDIENCIAS");		
		Vector ret = dealer.getAudienciasConcluidas();
		GravaLog log = new GravaLog("INFO");
		Operador op = new Operador();
		String strUsuario = op.getOperador() + " (SO)";
		
		Ferramentas frm = new Ferramentas();
		String strTx = frm.getPropriedades("txscert");		
		
		for (int i=0; i<ret.size(); i++) {
			String [] campos = (String[]) ret.elementAt(i);	
			
			/**
			 * Neste ponto, testa se a assinatura digital está presente junto com o arquivo de vídeo.
			 * Pelo flag no arquivo de propriedades permite a transmissão ou não.
			 */
			Ferramentas fmt = new Ferramentas();
			String strP7SName = fmt.getArquivoAssinejus(campos[0], campos[1], campos[2]);
			
			if (strP7SName == null && strTx.equals("0"))
				continue; // nao encontrou a assinatura do video.
			
			if (!interacao) {
				Configuracoes cfg = new Configuracoes();
				String strHoraInicio = cfg.getConfigParm("horario_inicial");
				int iHoraInicio = Integer.valueOf(strHoraInicio.substring(0,2) + strHoraInicio.substring(3));
				String strHoraFim = cfg.getConfigParm("horario_final");
				int iHoraFim = Integer.valueOf(strHoraFim.substring(0,2) + strHoraFim.substring(3));
				
				SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm");			
				String strAgora = dfHora.format(new Date());
				int iHoraAgora = Integer.valueOf(strAgora.substring(0,2) + strAgora.substring(3));
				
				if (iHoraAgora > iHoraFim && iHoraInicio < iHoraFim) {
					continue;
				}
			}
			
			Progress wp = null;
			if (interacao) {
				wp = new Progress("Transmitindo " + (i+1) + " de " + ret.size() + " - [" + campos[0] + "]");
				wp.setVisible(true);
			}
			
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strTimestamp = df.format(new Date());
			taTexto.append("\n\n" + strTimestamp + " | Transmitindo audiência -> " + (i+1) + " de " + ret.size() + " - [" + campos[0] + " - " + campos[1] + " - " + campos[2] + "]");
			pwVerbose.println("\n" + strTimestamp + " | Transmitindo audiência -> " + (i+1) + " de " + ret.size() + " - [" + campos[0] + " - " + campos[1] + " - " + campos[2] + "]");
			
			//try { Thread.sleep(5000); } catch(Exception e) { }
			
			if (transmite(campos[0], campos[1], campos[2])) {				
				Audiencia audi = new Audiencia(campos[0], campos[1], campos[2]);
				audi.setStatusTransmitida();				
				log.grava("FidelisVCSendAud.carregaProcessos()", strUsuario, "Audiência Transmitida: " + campos[0] + " - " + campos[1] + " - " + campos[2]);
				strTimestamp = df.format(new Date());
				taTexto.append("\n" + strTimestamp + " | Audiência Transmitida: " + campos[0] + " - " + campos[1] + " - " + campos[2]);
				pwVerbose.println(strTimestamp + " | Audiência Transmitida: " + campos[0] + " - " + campos[1] + " - " + campos[2]);
			}
			else {
				strTimestamp = df.format(new Date());
				taTexto.append("\n" + strTimestamp + " | Problemas na transmissão da audiência [" + campos[0] + ", " + campos[1] + ", " + campos[2] + "] Detalhes do problema foram gravados na LOG do sistema.");
				pwVerbose.println(strTimestamp + " | Problemas na transmissão da audiência [" + campos[0] + ", " + campos[1] + ", " + campos[2] + "] Detalhes do problema foram gravados na LOG do sistema.");
				if (interacao) {					
					JOptionPane.showMessageDialog(null, "Problemas na transmissão da audiência [" + campos[0] + ", " + campos[1] + ", " + campos[2] + "]\nDetalhes do problema foram gravados na LOG do sistema.", "Atenção", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			if (interacao) {
				wp.setVisible(false);
				wp.dispose();
			}
		}

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String strTimestamp = df.format(new Date());
		taTexto.append("\n\n" + strTimestamp + " | Não existem mais audiências concluídas para transmissão.");
		pwVerbose.println("\n" + strTimestamp + " | Não existem mais audiências concluídas para transmissão.");
		
		if (interacao)
			log.grava("FidelisVCSendAud.carregaProcessos()", strUsuario, "Não existem mais audiências concluídas para transmissão!");
		//System.out.println("VideocapClient.carregaProcessos(): Nao existem mais audiencias finalizadas para transmissao neste cliente!");
		
	}
	
	public static boolean transmite(String numProcesso, String data, String hora) {
		FileInputStream fin = null;
		GravaLog logInfo = new GravaLog("INFO");
		GravaLog logErro = new GravaLog("ERRO");
		Operador op = new Operador();
		String strUsuario = op.getOperador() + " (SO)";
		
		if (!geraArquivos(numProcesso, data, hora)) {
			logInfo.grava("FidelisVCSendAud.transmite()", strUsuario, "Erro ao gerar o arquivo ZIP para transmissão. Abortando a transmissão .. (" + numProcesso + ", " + data + ", " + hora + ")");
			taTexto.append("\nErro ao gerar o arquivo ZIP para transmissão. Abortando a transmissão -> (" + numProcesso + ", " + data + ", " + hora + ")");
			pwVerbose.println("Erro ao gerar o arquivo ZIP para transmissão. Abortando a transmissão -> (" + numProcesso + ", " + data + ", " + hora + ")");
			if (interacao)
				JOptionPane.showMessageDialog(null, "Erro ao gerar o arquivo ZIP para transmissão.\nAbortando a transmissão de (" + numProcesso + ", " + data + ", " + hora + ")", "Atenção", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		try {			
			//System.out.println("FidelisVCSendAud.transmite(): Conectando ao servidor para transmissao do processo (" + numProcesso + ", " + data + ", " + hora + ")");
			logInfo.grava("FidelisVCSendAud.transmite()", strUsuario, "Conectando ao servidor para transmissao do processo (" + numProcesso + ", " + data + ", " + hora + ")");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strTimestamp = df.format(new Date());
			taTexto.append("\n" + strTimestamp + " | Conectando ao servidor para transmissão do processo -> (" + numProcesso + ", " + data + ", " + hora + ")");
			pwVerbose.println(strTimestamp + " | Conectando ao servidor para transmissão do processo -> (" + numProcesso + ", " + data + ", " + hora + ")");
			
			Ferramentas frm = new Ferramentas();			
			FTPClient ftp = new FTPClient();
			ftp.connect(frm.getPropriedades("ftpsrv"));
			
			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				logInfo.grava("FidelisVCSendAud.transmite()", strUsuario, "Não foi possivel conectar no servidor [" + strIPServer + "]"); // - Porta " + iPorta);
				taTexto.append("\nNão foi possivel conectar no servidor -> " + strIPServer); // + ", Porta -> " + iPorta);
				pwVerbose.println("Não foi possivel conectar no servidor -> " + strIPServer); // + ", Porta -> " + iPorta);
				ftp.disconnect();
				return false;
			}
			else {
				if (!ftp.login(frm.getPropriedades("ftpusr"), frm.getPropriedades("ftppwd"))) {
					logInfo.grava("FidelisVCSendAud.transmite()", strUsuario, "Não foi possivel logar no servidor [" + strIPServer + "]"); // - Porta " + iPorta);
					taTexto.append("\nNão foi possivel logar no servidor -> " + strIPServer); // + ", Porta -> " + iPorta);
					pwVerbose.println("Não foi possivel logar no servidor -> " + strIPServer); // + ", Porta -> " + iPorta);
					ftp.disconnect();
					return false;					
				}
				ftp.cwd(frm.getPropriedades("ftpdir"));
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				
				// Pega o hash do zip que serah transmitido para comparar com o zip que chegou no servidor;
				// Verificacao de intergridade da transmissao.
				strTimestamp = df.format(new Date());
				taTexto.append("\n" + strTimestamp + " | Gerando HASH de validação do arquivo ZIP à transmitir ...");
				pwVerbose.println(strTimestamp + " | Gerando HASH de validação do arquivo ZIP à transmitir ...");
				GeraHash gh = new GeraHash();
				String strHashZip = gh.getHashFromFile(System.getProperty("user.dir") + "\\tmp\\" + Sessao.fileName(numProcesso, data, hora) + ".zip");
				
				if (strHashZip == null) {
					logInfo.grava("FidelisVCSendAud.transmite()", strUsuario, "Erro ao gerar HASH do arquivo ZIP antes da transmissão!");
					taTexto.append("\nErro ao gerar HASH do arquivo ZIP antes da transmissão!");
					pwVerbose.println("Erro ao gerar HASH do arquivo ZIP antes da transmissão!");
					if (interacao)
						JOptionPane.showMessageDialog(null, "Erro ao gerar HASH do arquivo ZIP antes da transmissão!",  "Atenção", JOptionPane.ERROR_MESSAGE);
					
					return false;
				}
				
				strTimestamp = df.format(new Date());
				taTexto.append("\n" + strTimestamp + " | Transmitindo audiência -> (" + Sessao.fileName(numProcesso, data, hora) + ")");
				pwVerbose.println(strTimestamp + " | Transmitindo audiência -> (" + Sessao.fileName(numProcesso, data, hora) + ")");
				
				File f = new File(System.getProperty("user.dir") + "\\tmp\\" + Sessao.fileName(numProcesso, data, hora) + ".zip");
				fin = new FileInputStream(f);
				
				ftp.dele(Sessao.fileName(numProcesso, data, hora) + ".tmp");
				ftp.dele(Sessao.fileName(numProcesso, data, hora) + ".zip");				
				ftp.storeFile(Sessao.fileName(numProcesso, data, hora) + ".tmp", fin);
				ftp.rename(Sessao.fileName(numProcesso, data, hora) + ".tmp", Sessao.fileName(numProcesso, data, hora) + ".zip");
				ftp.disconnect();
				fin.close();
				f.delete();
				
				strTimestamp = df.format(new Date());
				taTexto.append("\n" + strTimestamp + " | Audiência completamente enviada ao servidor.");
				pwVerbose.println(strTimestamp + " | Audiência completamente enviada ao servidor.");
				logInfo.grava("FidelisVCSendAud.transmite()", strUsuario, "003-Audiencia transmitida para o servidor: " + numProcesso);

				return true;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			logErro.gravaExcep("FidelisVCSendAud.transmite()", e);
			logInfo.grava("FidelisVCSendAud.transmite()", strUsuario, "Erro de comunicação com o servidor.");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strTimestamp = df.format(new Date());
			taTexto.append("\n" + strTimestamp + " | Erro de comunicação com o servidor.");
			pwVerbose.println(strTimestamp + " | Erro de comunicação com o servidor.");
			if (interacao)
				JOptionPane.showMessageDialog(null, "Erro de comunicação com o servidor.", "Atenção", JOptionPane.ERROR_MESSAGE);
			return false;
		} 
	}
	
	private static boolean geraArquivos(String numProcesso, String data, String hora) {
		GravaLog logErro = new GravaLog("ERRO");
		
		ExportarAudiencia ea = new ExportarAudiencia("FidelisVCSendAud", numProcesso, data, hora);
		if (!ea.copiaVideo(new File(System.getProperty("user.dir") + "/tmp/")))
			return false;
		//ea.geraXML(,new File(System.getProperty("user.dir") + "/tmp/"));
		
		Ferramentas fmt = new Ferramentas();
		String strP7SName    = fmt.getArquivoAssinejus(numProcesso, data, hora);
		String strP7SOrigem  = null; 
		String strP7SDestino = null;
		
		if (strP7SName != null) {
			strP7SOrigem  = System.getProperty("user.dir") + "/videos/" + strP7SName;
			strP7SDestino = System.getProperty("user.dir") + "/tmp/" + strP7SName;		
			if (!fmt.copiaArquivo(strP7SOrigem, strP7SDestino))
				strP7SName = null;
		}

		String strAvi, strExtensao;
		//File fe = new File(System.getProperty("user.dir") + "/tmp/" + Sessao.fileName(numProcesso, data, hora) + ".avi");
		//if (fe.exists()) {
			strAvi = System.getProperty("user.dir") + "/tmp/" + Sessao.fileName(numProcesso, data, hora) + ".asf";
			strExtensao = ".asf";
			ea.geraXML(strExtensao, new File(System.getProperty("user.dir") + "/tmp/"));
		//}
		//else {
		//	strAvi = System.getProperty("user.dir") + "/tmp/" + Sessao.fileName(numProcesso, data, hora) + ".wav";
		//	strExtensao = ".wav";
		//	ea.geraXML(strExtensao,new File(System.getProperty("user.dir") + "/tmp/"));
		//}
		
		String strXml = System.getProperty("user.dir") + "/tmp/" + Sessao.fileName(numProcesso, data, hora) + ".xml";
		String strZip = System.getProperty("user.dir") + "/tmp/" + Sessao.fileName(numProcesso, data, hora) + ".zip";
		
		ZipOutputStream zipFile = null;
		byte [] buf = new byte [1024];
		
		try {
			zipFile = new ZipOutputStream(new FileOutputStream(strZip));
			int entries = 0;
			if (strP7SName != null) {
				entries = 3;
			}
			else {
				entries = 2;
			}
				
			String filenames [] = {strAvi, strXml, strP7SDestino};
			String fileEntries[] = {Sessao.fileName(numProcesso, data, hora) + strExtensao,
					                Sessao.fileName(numProcesso, data, hora) + ".xml",
					                strP7SName };
			
	        for (int i=0; i<entries; i++) {
	            FileInputStream in = new FileInputStream(filenames[i]);
	            zipFile.putNextEntry(new ZipEntry(fileEntries[i]));
	    
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                zipFile.write(buf, 0, len);
	            }
	    
	            zipFile.closeEntry();
	            in.close();
	        }
	        
	        zipFile.close();
	        
	        File f = new File(filenames[0]);
	        f.delete();
	        f = new File(filenames[1]);
	        f.delete();
	        
	        return true;

		} catch (FileNotFoundException e) {
			logErro.gravaExcep("FidelisVCSendAud.geraArquivos(" + numProcesso + ", " + data + ", " + hora + ")", e);
			e.printStackTrace();
			return true;  // Erro de arquivo nao encontrado nao inviabiliza a transmissao dos dados.
		} catch (IOException e) {
			logErro.gravaExcep("FidelisVCSendAud.geraArquivos(" + numProcesso + ", " + data + ", " + hora + ")", e);
			e.printStackTrace();
			return false;
		}
	}

}
