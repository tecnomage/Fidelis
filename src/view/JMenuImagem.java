package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fidelis.controller.MenuPrincipal;
import fidelis.controller.SessaoConfig;

@SuppressWarnings("serial")
public class JMenuImagem extends JInternalFrame {
	
	private Container cont;
	private JCheckBox cbMostrarNovamente;
	
	private JLabel lbOperTexto1;
	private JLabel lbOperTexto11;
	private JLabel lbOperTexto2;
	private JLabel lbOperTexto22;
	private JLabel lbOperTexto3;
	private JLabel lbOperTexto33;
	private JLabel lbOperTexto4;
	//private JLabel lbOperTexto5;
	
	private JLabel lbAdmin1;
	private JLabel lbAdmin2;
	//private JLabel lbAdmin3;
	private JLabel lbAdmin4;
	private JLabel lbAdmin5;
	
	public JMenuImagem() {
		setTitle("Menu Principal");
		setSize(1000, 670);		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setMaximizable(true);
	    setResizable(true);
	    setVisible(true);

		cont = getContentPane();
				
		JPanel pOperacao = new JPanel();
		pOperacao.setLayout(new GridLayout(4, 2));

		lbOperTexto1 = new JLabel("Gravar Nova Audiência", new ImageIcon("images/icones/gravaraudiencia.png"), JLabel.LEFT);
		lbOperTexto11 = new JLabel("Vincular Assuntos CNJ", new ImageIcon("images/icones/recupaudi.png"), JLabel.LEFT);
		lbOperTexto2 = new JLabel("Gravar Audiência Pendente", new ImageIcon("images/icones/gravarpendente.png"), JLabel.LEFT);
		lbOperTexto22 = new JLabel("Importar Audiência", new ImageIcon("images/icones/importaraudi.png"), JLabel.LEFT);
		lbOperTexto3 = new JLabel("Gravar Evento", new ImageIcon("images/icones/gravarevento.png"), JLabel.LEFT);
		lbOperTexto33 = new JLabel("Visualizar Evento", new ImageIcon("images/icones/visualizarpesquisar.png"), JLabel.LEFT);
		lbOperTexto4 = new JLabel("Visualizar Audiência", new ImageIcon("images/icones/visualizarpesquisar.png"), JLabel.LEFT);
		//lbOperTexto5 = new JLabel("Sair", new ImageIcon("images/icones/sair.png"), JLabel.LEFT);	
		
		pOperacao.add(lbOperTexto1);
		pOperacao.add(lbOperTexto4);
		pOperacao.add(lbOperTexto2);
		//pOperacao.add(lbOperTexto22);
		pOperacao.add(lbOperTexto11);
		pOperacao.add(lbOperTexto3);
		pOperacao.add(lbOperTexto33);		
		//pOperacao.add(lbOperTexto5);
		
		JPanel pAdministracao = new JPanel();
		pAdministracao.setLayout(new GridLayout(4, 1));
		
		lbAdmin1 = new JLabel("Gerenciar Usuários   ", new ImageIcon("images/icones/cadastrousuarios.png"), JLabel.LEFT);
		lbAdmin2 = new JLabel("Configurar Sistema   ", new ImageIcon("images/icones/configinicial.png"), JLabel.LEFT);
		//lbAdmin3 = new JLabel("Transmissão Servidor", new ImageIcon("images/icones/transmissaoservidor.png"), JLabel.LEFT);
		lbAdmin4 = new JLabel("Transmitir Audiências   ", new ImageIcon("images/icones/transmissaoservidor.png"), JLabel.LEFT);
		lbAdmin5 = new JLabel("Detecção de Dispositivos    ", new ImageIcon("images/icones/deteccaodispositivos.png"), JLabel.LEFT);	
		
		pAdministracao.add(lbAdmin1);
		pAdministracao.add(lbAdmin2);
		//pAdministracao.add(lbAdmin3);
		pAdministracao.add(lbAdmin4);
		//pAdministracao.add(lbAdmin5);

		
		JPanel pSul = new JPanel();
		pSul.setLayout(new FlowLayout(FlowLayout.LEFT));		
		cbMostrarNovamente = new JCheckBox("Mostrar este menu sempre que iniciar o sistema.");
		cbMostrarNovamente.setSelected(true);
		cbMostrarNovamente.setEnabled(false);
		pSul.add(cbMostrarNovamente);

		cont.add(pOperacao, BorderLayout.WEST);
		if (SessaoConfig.isAdmin)
			cont.add(pAdministracao, BorderLayout.EAST);
		cont.add(pSul, BorderLayout.SOUTH);
		
		lbOperTexto1.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbOperTexto1.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.gravarNovaAudiencia();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbOperTexto1.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbOperTexto1.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		lbOperTexto11.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbOperTexto11.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.vincularAssuntoCNJ();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbOperTexto11.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbOperTexto11.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		lbOperTexto2.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbOperTexto2.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.gravarAudienciaPendente();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbOperTexto2.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbOperTexto2.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		lbOperTexto22.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbOperTexto22.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.gravarAudienciaPendente();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbOperTexto22.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbOperTexto22.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		lbOperTexto3.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbOperTexto3.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.gravarNovoEvento();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbOperTexto3.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbOperTexto3.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		lbOperTexto33.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbOperTexto33.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.visualizarEvento();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbOperTexto33.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbOperTexto33.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		lbOperTexto4.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbOperTexto4.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.visualizarAudiencia();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbOperTexto4.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbOperTexto4.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		lbAdmin1.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbAdmin1.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.cadastroDeUsuarios();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbAdmin1.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbAdmin1.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		lbAdmin2.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbAdmin2.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.configuracaoInicial();		    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbAdmin2.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbAdmin2.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		/*
		lbAdmin3.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbAdmin3.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.transmissaoServidor();	    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbAdmin3.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbAdmin3.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		*/
		
		lbAdmin4.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbAdmin4.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.transmissaoCliente();	    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbAdmin4.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbAdmin4.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		lbAdmin5.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
		    	lbAdmin5.setForeground(Color.BLACK);
		    	MenuPrincipal mp = new MenuPrincipal();
		    	mp.deteccaoDispositivos();	    	
		    }
			public void mouseEntered(MouseEvent e) {
				lbAdmin5.setForeground(Color.blue);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent me) {
				lbAdmin5.setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

}
