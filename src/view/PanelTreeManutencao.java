package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import controller.DBDealer;
import controller.UpperField;

@SuppressWarnings("serial")
public class PanelTreeManutencao extends JPanel {
	
	private JTree tv = null;
	private DefaultMutableTreeNode tnTop = null;
	private DefaultMutableTreeNode tnLinha = null;	
	private JPopupMenu popup = null;
	private int itemCont = 0;
	private String nodeOldValue = null;
	
	private DBDealer dealer = null;
	private String usuarioLogado = null;
	
	public PanelTreeManutencao(String strUsuarioLogado, String strRootNode, String strModulo) {
		
		usuarioLogado = strUsuarioLogado;
		
		dealer = new DBDealer(usuarioLogado, strModulo);
		itemCont = dealer.getProximoCodigo();
		
		JPanel pCentro = new JPanel();				
		tnTop = new DefaultMutableTreeNode(strRootNode + "        ");
		
		carregaNodes();
		
		tv = new JTree(tnTop);
		tv.setEditable(true);
		
		final JTextField tfEdicao = new UpperField();
		TreeCellEditor editor = new DefaultCellEditor(tfEdicao);		
		tv.setCellEditor(editor);		
		
		JScrollPane treeView = new JScrollPane(tv);		
		pCentro.add(treeView);
		
		popup = new JPopupMenu();
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				if (event.getActionCommand().toString().equals("Incluir")) {
					//String strValue = "Pressione <F2> p/ Editar (" + ++itemCont + ")";
					String strValue = "NOVO ITEM (" + ++itemCont + ")";
					tnLinha = new DefaultMutableTreeNode(strValue);					
					tnTop.add(tnLinha);
					tv.setToolTipText("PRESSIONE <F2> PARA EDITAR E <ENTER> PARA CONFIRMAR");
					tv.updateUI();
					dealer.incluir(strValue);
				}
				
				if (event.getActionCommand().toString().equals("Excluir")) {				   
				   try {
					   if (dealer.excluir(tv.getLastSelectedPathComponent().toString())) {
						   tnTop.remove((DefaultMutableTreeNode)tv.getLastSelectedPathComponent());
					   }
				   }
				   catch(Exception e) {
					   JOptionPane.showMessageDialog(null, "Item da Arvore não pode ser excluído!", "EXCLUIR", JOptionPane.ERROR_MESSAGE);
				   }
				   tv.updateUI();
				}
			}
		};

		JMenuItem item = new JMenuItem();
		popup.add(item = new JMenuItem("Incluir"));
	    item.addActionListener(menuListener);
	    popup.addSeparator();
	    popup.add(item = new JMenuItem("Excluir"));
	    item.addActionListener(menuListener);
	    
	    tv.addMouseListener(new MouseAdapter() {
	    	public void mousePressed(MouseEvent e) {
	    		checkPopup(e);
		    }
		    public void mouseClicked(MouseEvent e) {
		    	try {
		    		nodeOldValue = tv.getLastSelectedPathComponent().toString();
		    	}
		    	catch(Exception ex) {
		    		nodeOldValue = null;
		    	}
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
	    
	    tv.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if (e.getKeyCode() == 113) {
	    			nodeOldValue = tv.getLastSelectedPathComponent().toString();
	    		}
	    	}	    	
	    });
	    
	    tfEdicao.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if (e.getKeyCode() == 10) {
	    			dealer.atualizar(nodeOldValue, tfEdicao.getText());
	    		}	    		
	    	}		    	
	    	public void keyTyped(KeyEvent e) {
	    		if (tfEdicao.getWidth() < 200) {
	    			tfEdicao.setSize(200, 20);
	    		}	    		
	    	}		    	
	    });
	    
		this.setLayout(new BorderLayout());
		this.add(pCentro, BorderLayout.WEST);		
	}
	
	@SuppressWarnings("unchecked")
	private void carregaNodes() {		
		Vector itens = dealer.listar();			
		if (itens != null) {			
			for (int i = 0; i < itens.size(); i++) {
				tnLinha = new DefaultMutableTreeNode(itens.elementAt(i));
				tnTop.add(tnLinha);
			}
		}
	}
}
