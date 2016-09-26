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
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class FidelisVCRecvAud extends JFrame {

	private static JTextArea taTexto;
	private static String strVersao = "FIDELIS 1.06";
	private static PrintWriter pwVerbose;

	public FidelisVCRecvAud() {
		setSize(660, 480);
		setTitle(strVersao + " - Receptor de Audiências");
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		JPanel pEsq = new JPanel();
		JLabel lbLogo = new JLabel(new ImageIcon("images/FidelisVCRecvAud.png"));
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
		JButton btInterromper = new JButton("Interromper");
		pSul.add(btInterromper);
		pSul.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		this.add(pEsq, BorderLayout.WEST);
		this.add(pDir, BorderLayout.CENTER);
		this.add(pSul, BorderLayout.SOUTH);
		
		btInterromper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Object[] options = { "Sim", "Não" };
            	int confirma = JOptionPane.showOptionDialog(null, "Deseja realmente fechar a recepção de audiências?", "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);                
                if (confirma == 0) {
                	pwVerbose.flush();
                	pwVerbose.close();
                	dispose();      
                	System.exit(-1);
                }
            }
        });
	}

	@SuppressWarnings("static-access")
	public static void main (final String [] args) {
		
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
		
		ServerSocket sv = null;
		RecebeArquivo ra = null;

		final FidelisVCRecvAud vCapServer = new FidelisVCRecvAud();

		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
	    vCapServer.setLocation((tela.width - vCapServer.getSize().width) / 2, (tela.height - vCapServer.getSize().height) / 2);   
	    vCapServer.setVisible(false);
		
		if (args.length != 2) {
			System.out.println("ATENCAO: informe o diretorio destino e a porta de escuta. Ex: c:\\VideosRecebidos 60000");
			JOptionPane.showMessageDialog(null, "Informe o diretório destino e a porta de escuta. Ex: c:\\VideosRecebidos 60000", "Atenção", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		try {
			SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy HHmmss");
			String strTimestamp = df.format(new Date());			
			pwVerbose = new PrintWriter(System.getProperty("user.dir") + "/logs/FidelisVCRecvAudVerbose." + SessaoConfig.vara + "." + strTimestamp + ".log");			

			vCapServer.taTexto.append(strVersao + "\n");
			vCapServer.taTexto.append("Porta de conexão -> " + args[1] + "\n");
			vCapServer.taTexto.append("Diretório de recepção dos arquivos -> " + args[0] + "\n");
			vCapServer.taTexto.append("Aguardando audiências ...\n");
			
			pwVerbose.println(strVersao + "\n");
			pwVerbose.println("Porta de conexão -> " + args[1] + "\n");
			pwVerbose.println("Diretório de recepção dos arquivos -> " + args[0] + "\n");
			pwVerbose.println("Aguardando audiências ...\n");
			
		    if (!SystemTray.isSupported()) {		    	
		    	GravaLog log = new GravaLog("INFO");
		    	log.grava("fidelis.controller.videocapserver()", SessaoConfig.usuarioLogado, "SystemTray não é suportado por este sistema operacional.");
		    	JOptionPane.showMessageDialog(null, "SystemTray não é suportado por este sistema operacional.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
		    }
		    
		    TrayIcon trayIcon = null;
		    SystemTray tray = SystemTray.getSystemTray();
	    	Image image = Toolkit.getDefaultToolkit().getImage("images/transmissaoservidor2.gif");

	        ActionListener listener = new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        		if (arg0.getActionCommand().toString().equals("Ocultar")) {
	        			vCapServer.setVisible(false);
	        		}
	        		if (arg0.getActionCommand().toString().equals("Mostrar")) {
	        			vCapServer.setVisible(true);
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
	         
	        trayIcon = new TrayIcon(image, "FidelisVCRecvAud", popup);
	        trayIcon.addActionListener(listener);

	        try {
	            tray.add(trayIcon);
	        } catch (AWTException e) {
		    	GravaLog log = new GravaLog("ERRO");
		    	log.gravaExcep("fidelis.controller.videocapserver()", e);
		    	JOptionPane.showMessageDialog(null, "Erro ao adicionar icone ao SystemTray.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
	        }	    	 

			sv = new ServerSocket(Integer.valueOf(args[1]));
			
			while (true) {				
				ra = new RecebeArquivo(sv.accept(), args[0], taTexto, pwVerbose);
				ra.start();			
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
			vCapServer.taTexto.append(e.getMessage());
			pwVerbose.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			vCapServer.taTexto.append(e.getMessage());
			vCapServer.taTexto.append("\n*** ATENÇÃO: O Arquivo de Verbose não pôde ser criado.\n");
		}
		finally {
			try {
				sv.close();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}				
	}
	
}
