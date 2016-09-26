package view;

//import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.beans.PropertyVetoException;
import java.util.Vector;

//import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import controller.Expurgo;
import controller.Ferramentas;
import controller.GravaLog;
import controller.MenuPrincipal;
import controller.RecuperaMedia;
import controller.Sessao;
import controller.SessaoConfig;
import controller.ThreadRunStatic;
import model.BancodeDados;
import model.Configuracoes;
import model.Usuario;


/**
 *   	MODULO INICIAL DO SISTEMA FIDELIS
 */

@SuppressWarnings("serial")
public class FidelisMain extends JFrame {
		
	private JDesktopPane desktop = null; 
	private String usuarioLogado = null;
	private boolean administrador;
	private boolean fPlayer;
	
	// Construtor da Frame Inicial - Modulo Principal
	FidelisMain(String userName, boolean admin) {
		usuarioLogado = userName;
		administrador = admin;
		
		SessaoConfig.usuarioLogado = usuarioLogado;
		SessaoConfig.mainFrame = this;
		
		Ferramentas f = new Ferramentas();
		String fp = f.getPropriedades("player");
		if (fp == null  || !fp.equals("1"))
			fPlayer = false;
		else
			fPlayer = true;
		
		//setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(1024, 730);

		setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		setTitle("FIDELIS 1.09 - SISTEMA MULTIMÍDIA DE GRAVAÇÃO DE AUDIÊNCIAS - [" + usuarioLogado + "]");
		
		getContentPane().setBackground(Color.gray);
		desktop = new JDesktopPane();
		desktop.setBackground(Color.gray);
		
		//Configuracoes cfg = new Configuracoes();
		//JLabel lbImg = new JLabel(new ImageIcon(cfg.getConfigParm("fundo")));		
		//JPanel pImg = new JPanel();
		//pImg.setBackground(Color.gray);
		//pImg.add(lbImg);
		//getContentPane().add(pImg, BorderLayout.CENTER);
		
		JMenuBar menubar = new JMenuBar();
		JMenu menuAdministracao = new JMenu();
		JMenu menuOperacao = new JMenu();
		JMenu menuManutencao = new JMenu();
		
		JMenuItem miMDB = new JMenuItem();
		JMenuItem miRelatorios = new JMenuItem();
		
		JMenuItem mi1 = new JMenuItem();
		JMenuItem mi2 = new JMenuItem();
		JMenuItem mi3 = new JMenuItem();
		JMenuItem mi4 = new JMenuItem();
		JMenuItem mi5 = new JMenuItem();
		JMenuItem mi6 = new JMenuItem();
		JMenuItem mi7 = new JMenuItem();
		JMenuItem mi8 = new JMenuItem();
		JMenuItem mi9 = new JMenuItem();
		JMenuItem miVisualizaEvento = new JMenuItem();
		JMenuItem miTS = new JMenuItem();
		JMenuItem miTC = new JMenuItem();
		JMenuItem miImporta = new JMenuItem();
		JMenuItem miInit = new JMenuItem();
		JMenuItem miZera = new JMenuItem();
		JMenuItem miRecupera = new JMenuItem();
		JMenuItem miVincular = new JMenuItem();
		JMenuItem miConexao = new JMenuItem();
		
		JSeparator separador1 = new JSeparator();
		JSeparator separador2 = new JSeparator();
		JSeparator separador3 = new JSeparator();
		JSeparator separador4 = new JSeparator();
		JSeparator separador5 = new JSeparator();
		JSeparator separador6 = new JSeparator();
		//JSeparator separador7 = new JSeparator();
		JSeparator separador8 = new JSeparator();
		
		menuManutencao.setText("Manutenção");
		menuManutencao.setMnemonic('M');
		
		menuAdministracao.setText("Administração");
		menuAdministracao.setMnemonic('A');
		//miPerfil.setText("Cadastro de Perfis");
		//miPerfil.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK));
		
		miMDB.setText("Manutenção de Tabelas (Local/Remoto)");
		miRelatorios.setText("Relatórios");
		
		
		mi1.setText("Gerenciamento de Usuários");
		mi1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK));
		
		miTS.setText("Recepção de Audiências");
		miTS.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
		miTC.setText("Transmissão de Audiências");
		miTC.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
		miImporta.setText("Importação de Audiências");
		miImporta.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
		//miImporta.setEnabled(false);		

		mi2.setText("Configuração do Sistema");
		mi2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
		
		miInit.setText("Detecção de Dispositivos");
		miZera.setText("Criação do Banco de Dados");
		
			
		menuOperacao.setText("Operação");	
		menuOperacao.setMnemonic('O');
		
		mi3.setText("Sair");
		mi3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
		
		mi4.setText("Gravar Nova Audiência");
		mi4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
		mi5.setText("Gravar Audiência Pendente");
		mi5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
		
		mi6.setText("Gravar Novo Evento");
		mi6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		
		mi9.setText("Visualizar Audiência");
		mi9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK));
		
		miVisualizaEvento.setText("Visualizar Evento");
		miVisualizaEvento.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
		
		miRecupera.setText("Recuperar Audiência/Evento");
		miRecupera.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
		
		miVincular.setText("Vincular Assuntos CNJ");
		miVincular.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.ALT_MASK));
		
		menuManutencao.add(miMDB);
		menuManutencao.add(miRelatorios);
		
		//menuAdministracao.add(miPerfil);
		menuAdministracao.add(mi1);
		menuAdministracao.add(separador1);
		menuAdministracao.add(mi2);
		menuAdministracao.add(separador2);
		menuAdministracao.add(miTS);
		menuAdministracao.add(miTC);
		menuAdministracao.add(separador6);
		menuAdministracao.add(miImporta);
		//menuAdministracao.add(separador7);
		//menuAdministracao.add(miZera);
		//menuAdministracao.add(miInit);
		
		if (!fPlayer) {
			menuOperacao.add(mi4);
			menuOperacao.add(mi5);
			menuOperacao.add(separador3);
			menuOperacao.add(mi6);						
			menuOperacao.add(miVincular);
			menuOperacao.add(separador8);
			menuOperacao.add(miRecupera);
			menuOperacao.add(separador4);
		}
		
		if (fPlayer) {
			miConexao.setText("Conectar à um BD Remoto");
			menuOperacao.add(miConexao);
			JSeparator jscr = new JSeparator();
			menuOperacao.add(jscr);
		}
		
		menuOperacao.add(mi9);
		menuOperacao.add(miVisualizaEvento);
		menuOperacao.add(separador5);
		menuOperacao.add(mi3);
		
		menubar.add(menuOperacao);
		
		Configuracoes conf = new Configuracoes();
		
		if (!fPlayer) {
			if (conf.getConfigParm("tipo_login").equals("1")) {		
				if (administrador) {
					menubar.add(menuAdministracao);
					menubar.add(menuManutencao);
					SessaoConfig.isAdmin = true;
				}
			}
			else {
				Usuario user = new Usuario();
				if (user.getNomePerfil(usuarioLogado).toUpperCase().equals("ADMINISTRADOR")) {
					menubar.add(menuAdministracao);
					menubar.add(menuManutencao);
					SessaoConfig.isAdmin = true;
				}
			}
		}
		
		setJMenuBar(menubar);
		
		miMDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	MenuPrincipal mp = new MenuPrincipal();
                mp.manutencaoDB();
            }
        });
		
		miRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	MenuPrincipal mp = new MenuPrincipal();
                mp.relatorios();
            }
        });
		
		miRecupera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
        		MenuPrincipal mp = new MenuPrincipal();
        		mp.recuperarMedia();
            }
        });		
		
		miImporta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuPrincipal mp = new MenuPrincipal();
                mp.importarAudiencia();
            }
        });

		
		// Sair
		mi3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSairActionPerformed(evt);
            }
        });
		
		// Cadastro de Usuarios
		mi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi1ActionPerformed(evt);
            }
        });
		
		// Configuracao Inicial
		mi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi2ActionPerformed(evt);
            }
        });
		
		// Transmissao: SERVIDOR
		miTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	MenuPrincipal mp = new MenuPrincipal();
            	mp.transmissaoServidor();           	
            }
        });

		// Transmissao: CLIENTE
		miTC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	MenuPrincipal mp = new MenuPrincipal();
            	mp.transmissaoCliente();
            }
        });
		
		// Deteccao de dispositivos
		miInit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
            	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            	ThreadRunStatic trs = new ThreadRunStatic(null, 3);
            	trs.start();
            	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
		
		// Recriar o Banco de Dados
		miZera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            	Object[] options = { "Sim", "Não" };
            	int confirma = JOptionPane.showOptionDialog(null, "Esta opção apagará todo o conteúdo do banco de dados para recriá-lo novamente.\nDeseja continuar?", "** ATENÇÃO **", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                
                if (confirma == 0) {
                	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    Progress t = new Progress("Apagando/Recriando o banco de dados ...");
                    t.setVisible(true);
                    BancodeDados db = new BancodeDados();
                    db.removeTabelas();
                    boolean ret = db.criaTabelas();
	            	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	            	t.setVisible(false);
	            	if (ret) 
	            		JOptionPane.showMessageDialog(null, "Tabelas recriadas com SUCESSO!", "fidelis.view.VideocapMain()", JOptionPane.INFORMATION_MESSAGE);
	            	else
	            		JOptionPane.showMessageDialog(null, "Ocorreu um erro ao recriar as tabelas.\nVerifique as logs.", "fidelis.view.VideocapMain()", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


		// Grava Nova Audiencia
		mi4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi4ActionPerformed(evt);
            }
        });
		
		// Grava Audiencia Pendente
		mi5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi5ActionPerformed(evt);
            }
        });
		
		// Grava Novo Evento
		mi6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi6ActionPerformed(evt);
            }
        });
		
		// Importar Audiencia
		mi7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	mi7ActionPerformed(evt);
            }
        });

		// Exportar Audiencia
		mi8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi8ActionPerformed(evt);
            }
        });
	
		// Conexao Remota
		miConexao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
        		MenuPrincipal mp = new MenuPrincipal();
        		mp.ConexaoRemota();
            }
        });
		
		// Visualizacao de Audiencia
		mi9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi9ActionPerformed(evt);
            }
        });
		
		// Visualizacao de Evento
		miVisualizaEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVisualizaEventoActionPerformed(evt);
            }
        });
		
		// Vinculacao de Assunto CNJ
		miVincular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVincularActionPerformed(evt);
            }
        });
		
		// VERIFICA SE EXISTEM AUDIENCIAS/EVENTOS CORROMPIDOS E TENTA CORRIGI-LOS. 
		// CASO O USUARIO ACEITE.
		RecuperaMedia rm = new RecuperaMedia(usuarioLogado);
		if (rm.recupera() == 1) {
			JOptionPane.showMessageDialog(null, "Arquivos de Áudio/Vídeo recuperados.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
		}

		
		// VERIFICA A UTILIZACAO DO DISCO E INFORMA SE EH NECESSARIO FAZER O EXPURGO DAS AUDIENCIAS
		Expurgo exp = new Expurgo(usuarioLogado);
		exp.calcula();		
		
		// ABRE O MENU DE IMAGENS
		if (!fPlayer) {
			JMenuImagem mImg = new JMenuImagem();
			mImg.setVisible(true);		
	    	setContentPane(desktop);
	    	desktop.add(mImg);
	    	try {
				mImg.setSelected(true);
				mImg.setMaximum(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		
		UIManager.put("FileChooser.lookInLabelMnemonic", new Integer('E'));     
		UIManager.put("FileChooser.lookInLabelText", "Examinar");   
		UIManager.put("FileChooser.saveInLabelText", "Salvar em");   
		  
		UIManager.put("FileChooser.fileNameLabelMnemonic", new Integer('N'));  // N   
		UIManager.put("FileChooser.fileNameLabelText", "Nome do arquivo");   
		  
		UIManager.put("FileChooser.filesOfTypeLabelMnemonic", new Integer('T'));  // T   
		UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo");   
		  
		UIManager.put("FileChooser.upFolderToolTipText", "Um nível acima");   
		UIManager.put("FileChooser.upFolderAccessibleName", "Um nível acima");   
		  
		UIManager.put("FileChooser.homeFolderToolTipText", "Desktop");   
		UIManager.put("FileChooser.homeFolderAccessibleName", "Desktop");   
		  
		UIManager.put("FileChooser.newFolderToolTipText", "Criar nova pasta");   
		UIManager.put("FileChooser.newFolderAccessibleName", "Criar nova pasta");   
		  
		UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");   
		UIManager.put("FileChooser.listViewButtonAccessibleName", "Lista");   
		  
		UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalhes");   
		UIManager.put("FileChooser.detailsViewButtonAccessibleName", "Detalhes");  
		
		UIManager.put("FileChooser.cancelButtonText", "Cancelar");
		UIManager.put("FileChooser.cancelButtonToolTipText", "Cancelar Operação");
		UIManager.put("FileChooser.openButtonText", "Abrir");
		UIManager.put("FileChooser.openButtonToolTipText", "Abrir o arquivo");		
		  
		UIManager.put("FileChooser.fileNameHeaderText", "Nome");   
		UIManager.put("FileChooser.fileSizeHeaderText", "Tamanho");   
		UIManager.put("FileChooser.fileTypeHeaderText", "Tipo");   
		UIManager.put("FileChooser.fileDateHeaderText", "Data");   
		UIManager.put("FileChooser.fileAttrHeaderText", "Atributos"); 

	} // FIM DA CONSTRUTORA
	
	// Operacao > Sair
	private void menuSairActionPerformed(java.awt.event.ActionEvent evt) {
    	Object[] options = { "Sim", "Não" };
    	int confirma = JOptionPane.showOptionDialog(null, "Deseja realmente sair do sistema?", "FIDELIS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (confirma == 0) {
        	GravaLog gl = new GravaLog("INFO");
        	gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Encerrou o Sistema");
            System.exit(0);
        }
    }
	
	// Administracao > Cadastro de Usuarios
	private void mi1ActionPerformed(java.awt.event.ActionEvent evt) {
		MenuPrincipal mp = new MenuPrincipal();
		mp.cadastroDeUsuarios();
    }
	
	
	// Administracao > Configuracao Inicial
	private void mi2ActionPerformed(java.awt.event.ActionEvent evt) {
		MenuPrincipal mp = new MenuPrincipal();
		mp.configuracaoInicial();
    }   
	
	
	// Operacao > Gravar Nova Audiencia
	private void mi4ActionPerformed(java.awt.event.ActionEvent evt) {
		MenuPrincipal mp = new MenuPrincipal();
		mp.gravarNovaAudiencia();
    }   
	
	// Operacao > Gravar Audiencia Pendente
	private void mi5ActionPerformed(java.awt.event.ActionEvent evt) {
		MenuPrincipal mp = new MenuPrincipal();
		mp.gravarAudienciaPendente();
    }
	
	// Operacao > Gravar Novo Evento
	private void mi6ActionPerformed(java.awt.event.ActionEvent evt) {
		MenuPrincipal mp = new MenuPrincipal();
		mp.gravarNovoEvento();
    }
	
	// Operacao > Importar Audiencias
	@SuppressWarnings("unchecked")
	private void mi7ActionPerformed(java.awt.event.ActionEvent evt) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		Sessao.numProcesso = null;
		Sessao.tipo = null;
		Sessao.juiz = null;
		Sessao.nomeDispositivoVideo = null;

    	String [] titulos = {"Seleção da Audiência em Arquivo"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelImportaAudiencia(usuarioLogado);
    	p.addElement(p0);
    	    
    	JWizard importarFrame = new JWizard("Importar Audiência", titulos, p);    	
    	importarFrame.setVisible(true);
    	setContentPane(desktop);
    	desktop.add(importarFrame);
    	try {
			importarFrame.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo IMPORTAR AUDIENCIA iniciado.");
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

	
	
	// Operacao > Exportar Audiencias
	@SuppressWarnings("unchecked")
	private void mi8ActionPerformed(java.awt.event.ActionEvent evt) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		Sessao.numProcesso = null;
		Sessao.tipo = null;
		Sessao.juiz = null;
		Sessao.nomeDispositivoVideo = null;

    	String [] titulos = {"Seleção da Audiência", "Exportar Audiência"};
    	Vector p = new Vector();
    	    	
    	JPanel p0 = new PanelSelecionaAudienciaTable(usuarioLogado, "", false);
    	JPanel p1 = new PanelExportaAudiencia(usuarioLogado);
    	p.addElement(p0);
    	p.addElement(p1);    	
    	    
    	JWizard exportarFrame = new JWizard("Exportar Audiência", titulos, p);    	
    	exportarFrame.setSize(900, 670);
    	exportarFrame.setVisible(true);
    	setContentPane(desktop);
    	desktop.add(exportarFrame);
    	try {
			exportarFrame.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		GravaLog gl = new GravaLog("INFO");
		gl.grava("fidelis.view.VideocapMain()", usuarioLogado, "Módulo EXPORTAR AUDIENCIA iniciado.");
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }


	// Operacao > Visualizacao de Audiencias
	private void mi9ActionPerformed(java.awt.event.ActionEvent evt) {
		MenuPrincipal mp = new MenuPrincipal();
		mp.visualizarAudiencia();
    }
	
	// Operacao > Visualizacao de Audiencias
	private void miVisualizaEventoActionPerformed(java.awt.event.ActionEvent evt) {
		MenuPrincipal mp = new MenuPrincipal();
		mp.visualizarEvento();
    }
	
	// Operacao > Vinculacao de Assuntos CNJ
	private void miVincularActionPerformed(java.awt.event.ActionEvent evt) {
		MenuPrincipal mp = new MenuPrincipal();
		mp.vincularAssuntoCNJ();
    }
	
	public JDesktopPane getDesktop() {
		return desktop;
	}

}
