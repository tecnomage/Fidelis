package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class TreeMain extends JFrame {
	
	private JPanel pEsquerdo = null;
	private JTree treeView = null;
	private DefaultMutableTreeNode tnTop = null;
	private JPopupMenu popup = null;
	private DefaultMutableTreeNode tnLinha = null;
	
	TreeMain() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(1000, 730);
		//setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);		
		setTitle("IOS - Sistema de Gravação em Audio e Vídeo de Audiências");
		
		Container cont = getContentPane();
		cont.setLayout(null);
		//cont.setLayout(new BorderLayout());
		cont.setBackground(Color.gray);
		
		pEsquerdo = new JPanel();
		//pEsquerdo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		pEsquerdo.setSize(400, 480);
		
		cont.add(pEsquerdo);//, BorderLayout.WEST);
		
		tnTop = new DefaultMutableTreeNode("Menu Principal");
		treeView = new JTree(tnTop);
		treeView.setEditable(true);
		
		JScrollPane spTv = new JScrollPane(treeView);
		spTv.setSize(400, 480);
		spTv.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spTv.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		pEsquerdo.setLayout(null);
		pEsquerdo.add(spTv);	
		
		
		carregaTipoAudiencias();
		carregaMotivoFinalizacoes();
		carregaEventos();
		carregaAudiencias();
		carregaUsuarios();
		
		popup = new JPopupMenu();
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				if (event.getActionCommand().toString().equals("Incluir")) {
					String strValue = "Pressione <F2> p/ Editar";
					tnLinha = new DefaultMutableTreeNode(strValue);
					DefaultMutableTreeNode tnPai = (DefaultMutableTreeNode)treeView.getLastSelectedPathComponent();
					//((DefaultMutableTreeNode) tnPai.getParent()).add(tnLinha);
					tnPai.add(tnLinha);
					treeView.updateUI();
					//incluirDB(strValue);
				}
				
				if (event.getActionCommand().toString().equals("Excluir")) {				   
				   try {
					   DefaultMutableTreeNode tnPai = (DefaultMutableTreeNode)treeView.getLastSelectedPathComponent();
					   ((DefaultMutableTreeNode) tnPai.getParent()).remove(tnPai);
					   //excluirDB(tv.getLastSelectedPathComponent().toString());
				   }
				   catch(Exception e) {
					   JOptionPane.showMessageDialog(null, "Item da Arvore não pode ser excluído!", "Excluir", JOptionPane.ERROR_MESSAGE);
				   }
				   treeView.updateUI();
				}
			}
		};

		JMenuItem item = new JMenuItem();
		popup.add(item = new JMenuItem("Incluir"));
	    item.addActionListener(menuListener);
	    popup.addSeparator();
	    popup.add(item = new JMenuItem("Excluir"));
	    item.addActionListener(menuListener);
	    
	    treeView.addMouseListener(new MouseAdapter() {
	    	public void mousePressed(MouseEvent e) {
	    		checkPopup(e);
		    }
		    public void mouseClicked(MouseEvent e) {
/*		    	try {
		    		nodeOldValue = treeView.getLastSelectedPathComponent().toString();
		    	}
		    	catch(Exception ex) {
		    		nodeOldValue = null;
		    	}*/
		    	checkPopup(e);
		    }
		    public void mouseReleased(MouseEvent e) {
		    	checkPopup(e);
		    }
		    private void checkPopup(MouseEvent e) {
		    	if (e.isPopupTrigger()) {
		    		popup.show(e.getComponent(), e.getX(), e.getY());
		    	}
		    }
	    });
	    
	    treeView.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if (e.getKeyCode() == 113) {
	    			//nodeOldValue = tv.getLastSelectedPathComponent().toString();
	    		}
	    	}	    	
	    });
	    
/*	    tfEdicao.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if (e.getKeyCode() == 10) {
	    			atualizarDB(nodeOldValue, tfEdicao.getText());
	    		}	    		
	    	}		    	
	    	public void keyTyped(KeyEvent e) {
	    		if (tfEdicao.getWidth() < 150) {
	    			tfEdicao.setSize(150, 20);
	    		}	    		
	    	}		    	
	    });
*/
	}
	
	public void carregaTipoAudiencias() {
		DefaultMutableTreeNode tnRoot = new DefaultMutableTreeNode("Tipos de Audiências"); 
		DefaultMutableTreeNode tnNode = null;		
		tnTop.add(tnRoot);		
	
		tnNode = new DefaultMutableTreeNode("INICIAL");		
		tnRoot.add(tnNode);
		tnNode = new DefaultMutableTreeNode("UNO");		
		tnRoot.add(tnNode);
		
/*		TipoAudiencias ta = new TipoAudiencias();
		Vector itens = ta.lista();			
		if (itens != null) {			
			for (int i = 0; i < itens.size(); i++) {
				tnNode = new DefaultMutableTreeNode(itens.elementAt(i));
				tnRoot.add(tnNode);
			}
		}
		*/
		treeView.updateUI();
	}

	public void carregaMotivoFinalizacoes() {
		DefaultMutableTreeNode tnRoot = new DefaultMutableTreeNode("Motivos de Finalizações"); 
		DefaultMutableTreeNode tnNode = null;		
		tnTop.add(tnRoot);
		
		tnNode = new DefaultMutableTreeNode("Falta de Espaço em Disco");		
		tnRoot.add(tnNode);
		tnNode = new DefaultMutableTreeNode("Apresentação de Novas Testemunhas");		
		tnRoot.add(tnNode);
		
/*		MotivoFinalizacoes mf = new MotivoFinalizacoes();
		Vector itens = mf.lista();			
		if (itens != null) {			
			for (int i = 0; i < itens.size(); i++) {
				tnNode = new DefaultMutableTreeNode(itens.elementAt(i));
				tnRoot.add(tnNode);
			}
		}
*/
		treeView.updateUI();
	}
	
	public void carregaEventos() {
		DefaultMutableTreeNode tnRoot = new DefaultMutableTreeNode("Eventos"); 
		@SuppressWarnings("unused")
		DefaultMutableTreeNode tnNode = null;		
		tnTop.add(tnRoot);
		treeView.updateUI();
	}

	public void carregaAudiencias() {
		DefaultMutableTreeNode tnRoot = new DefaultMutableTreeNode("Audiências"); 
		DefaultMutableTreeNode tnNode = null;		
		tnTop.add(tnRoot);

		tnNode = new DefaultMutableTreeNode("Processo: 12345 2008 003 02 02 1 - 29/12/2008 16:00");		
		tnRoot.add(tnNode);
		
		treeView.updateUI();
	}
	
	public void carregaUsuarios() {
		DefaultMutableTreeNode tnRoot = new DefaultMutableTreeNode("Usuários"); 
		DefaultMutableTreeNode tnNode = null;		
		tnTop.add(tnRoot);
		
		tnNode = new DefaultMutableTreeNode("admin (Administrador da Silva)");		
		tnRoot.add(tnNode);
		tnNode = new DefaultMutableTreeNode("oper (Operador de Barros)");		
		tnRoot.add(tnNode);
		
		treeView.updateUI();
	}
	
	public static void main(String [] args) {
		TreeMain window = new TreeMain();
		window.setVisible(true);
	}

}
	