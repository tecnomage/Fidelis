package view;

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
import model.Qualifica;
import model.Temas;

@SuppressWarnings("serial")
public class PanelTreeTemasEQualif extends JPanel {
	
	private DefaultMutableTreeNode tnTop = null;
	private DefaultMutableTreeNode tnRoot = null;
	private DefaultMutableTreeNode tnNode = null;
	private DefaultMutableTreeNode tnLinha = null;
	private JTree treeView = null;
	private JPopupMenu popup = null;
	private DBDealer dealer = null;
	private String nodeOldValue = null;
	private int itemCont = 0;
	private String numProcesso = null;
	private int modulo = 0;
	
	@SuppressWarnings("unchecked")
	public PanelTreeTemasEQualif(String strUsuarioLogado, String strNumProcesso, String strModulo) {
		numProcesso = strNumProcesso;
		dealer = new DBDealer(strUsuarioLogado, strModulo, strNumProcesso);
		itemCont = dealer.getProximoCodigo();
		
		if (strModulo.equals("TEMAS")) {
			modulo = 3;
		}
		
		if (strModulo.equals("QUALIFICACOES")){
			modulo = 4;
		}
		
		if (strModulo.equals("TEMASE")) {
			modulo = 5;
		}
		
		if (strModulo.equals("QUALIFICACOESE")){
			modulo = 6;
		}

		
		this.setLayout(null);
		this.setBounds(5, 155, 340, 260);
		if (modulo == 3) {
			this.setBorder(javax.swing.BorderFactory.createTitledBorder("Assuntos e Sub-Temas"));		
			tnTop = new DefaultMutableTreeNode("ASSUNTOS");
		}

		if (modulo == 4) {
			this.setBorder(javax.swing.BorderFactory.createTitledBorder("Qualificações e Depoentes"));		
			tnTop = new DefaultMutableTreeNode("DEPOENTES");
		}
		
		if (modulo == 5) {
			this.setBorder(javax.swing.BorderFactory.createTitledBorder("Assuntos e Sub-Temas"));		
			tnTop = new DefaultMutableTreeNode("ASSUNTOS");
		}

		if (modulo == 6) {
			this.setBorder(javax.swing.BorderFactory.createTitledBorder("Qualificações e Oradores"));		
			tnTop = new DefaultMutableTreeNode("ORADORES");
		}

		
		treeView = new JTree(tnTop);
		treeView.setEditable(true);		
		treeView.setBounds(8, 18, getWidth() - 17, getHeight() - 25);
		treeView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		
		final JTextField tfEdicao = new UpperField();
		TreeCellEditor editor = new DefaultCellEditor(tfEdicao);		
		treeView.setCellEditor(editor);
		
		popup = new JPopupMenu();
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				if (event.getActionCommand().toString().equals("Incluir")) {
					if (numProcesso == null || numProcesso.equals("")) {
						JOptionPane.showMessageDialog(null, "Primeiro informe os dados do processo!", "ASSUNTOS E QUALIFICAÇÕES", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						try {
							//String strValue = "Pressione <F2> p/ Editar (" + ++itemCont + ")";
							String strValue = "NOVO ITEM (" + ++itemCont + ")";
							tnLinha = new DefaultMutableTreeNode(strValue);
							tnNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();				
							if (tnNode.isRoot()) {								
								tnTop.add(tnLinha);
								dealer.incluir(strValue, null);
							}
							else {
								if (tnNode.getLevel() > 1) {
									JOptionPane.showMessageDialog(null, "O nível selecionado NÃO pode conter um subnível!", "INCLUIR", JOptionPane.ERROR_MESSAGE);
								}
								else {	
									tnNode.add(tnLinha);
									
									int linha = 0;
									while (linha < treeView.getRowCount()) {
										treeView.expandRow(linha++);
									}

									/*
									int items = 0;
									for (int i = tnTop.getIndex(tnNode); i >= 0; i--) {
										if (treeView.isExpanded(i + 1)) {										
											items += (tnTop.getChildAt(i)).getChildCount() + 1;
										} 
										else {
											items += 1;
										}
									}									
									treeView.expandRow(items);
									*/																		
									dealer.incluir(strValue, treeView.getLastSelectedPathComponent().toString());
								}
							}
							treeView.updateUI();						
							treeView.setToolTipText("PRESSIONE <F2> PARA EDITAR E <ENTER> PARA CONFIRMAR");
						} catch(Exception e) {
							JOptionPane.showMessageDialog(null, "Primeiro selecione um node para referência!", "INCLUIR", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				
				if (event.getActionCommand().toString().equals("Excluir")) {		
					if (numProcesso == null || numProcesso.equals("")) {
						JOptionPane.showMessageDialog(null, "Primeiro informe os dados do processo/evento!", "ASSUNTOS E QUALIFICAÇÕES", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
					   try {
							tnNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
							((DefaultMutableTreeNode)tnNode.getParent()).remove(tnNode);
							dealer.excluir(treeView.getLastSelectedPathComponent().toString());
					   }
					   catch(Exception e) {
						   JOptionPane.showMessageDialog(null, "Item da Arvore não pode ser excluído!", "EXCLUIR", JOptionPane.ERROR_MESSAGE);
					   }
					   treeView.updateUI();
					}
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
		    	try {
		    		nodeOldValue = treeView.getLastSelectedPathComponent().toString();
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
	    
	    treeView.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if (e.getKeyCode() == 113) {
	    			nodeOldValue = treeView.getLastSelectedPathComponent().toString();
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
		
	    //treeView.setScrollsOnExpand(true);
	    //treeView.setAutoscrolls(true);
	    JScrollPane sp = new JScrollPane(treeView);
	    sp.setBounds(8, 18, getWidth() - 17, getHeight() - 25);
		//this.add(treeView);		
	    this.add(sp);
	    
	    if (modulo == 3) {
	    	if (strNumProcesso.equals("1111111-11.1111.1.11.1111")) {
	    		Temas temaInicial = new Temas("1111111-11.1111.1.11.1111");			
				Vector vTemasInicial = temaInicial.carregarTemas();
				for (int i = 0; i < vTemasInicial.size(); i++) {
					tnRoot = new DefaultMutableTreeNode((String) vTemasInicial.elementAt(i));
					tnTop.add(tnRoot);
					Vector vSubTemas = temaInicial.carregarSubTemas((String) vTemasInicial.elementAt(i));
					for (int j = 0; j < vSubTemas.size(); j++) {
						tnNode = new DefaultMutableTreeNode((String) vSubTemas.elementAt(j));
						tnRoot.add(tnNode);
					}
				}
				int linha = 0;
				while (linha < treeView.getRowCount()) {
					treeView.expandRow(linha++);
				}
				treeView.updateUI();
	    	}
	    }
	    
	    if (modulo == 4) {
	    	if (strNumProcesso.equals("1111111-11.1111.1.11.1111")) {
				Qualifica qualiInicial = new Qualifica("1111111-11.1111.1.11.1111");		
				Vector vQualiInicial = qualiInicial.carregarQualifica();			
				for (int i = 0; i < vQualiInicial.size(); i++) {
					tnRoot = new DefaultMutableTreeNode((String) vQualiInicial.elementAt(i));
					tnTop.add(tnRoot);
					Vector vDepoentes = qualiInicial.carregarDepoentes((String) vQualiInicial.elementAt(i));
					for (int j = 0; j < vDepoentes.size(); j++) {
						tnNode = new DefaultMutableTreeNode((String) vDepoentes.elementAt(j));
						tnRoot.add(tnNode);
					}
				}
				int linha = 0;
				while (linha < treeView.getRowCount()) {
					treeView.expandRow(linha++);
				}
				treeView.updateUI();
	    	}
	    }
	}
	
	public void incluirNode(String strNumProcesso, String strValue) {
		
		int rowcount = treeView.getRowCount();
		String stridx = null;
		if (rowcount < 10)
			stridx = "0" + new Integer(rowcount).toString() + ". ";
		else
			stridx = new Integer(rowcount).toString() + ". ";
		
		strValue = stridx + strValue;
		
		dealer.setNumProcesso(strNumProcesso);
		dealer.incluir(strValue, null);
		tnRoot = new DefaultMutableTreeNode(strValue);
		tnTop.add(tnRoot);
		treeView.updateUI();
	}
	
	@SuppressWarnings("unchecked")
	public void cargaInicialTemas() {
		/*
		dealer.incluir("Acordo e Convenção Coletivos de Trabalho", null);
		dealer.incluir("Aposentadoria e Pensão", null);
		dealer.incluir("Categoria Profissional Especial", null);
		dealer.incluir("Contrato Individual de Trabalho", null);
		dealer.incluir("Direito de Greve / Lockout", null);
		dealer.incluir("Direito Sindical e Questões Análogas", null);
		dealer.incluir("Duração do Trabalho", null);
		dealer.incluir("Férias", null);
		dealer.incluir("Outras Relações de Trabalho", null);
		dealer.incluir("Prescrição", null);
		dealer.incluir("Remuneração, Verbas Indenizatórias e Benefícios", null);
		dealer.incluir("Rescisão do Contrato de Trabalho", null);
		dealer.incluir("Responsabilidade Civil do Empregador", null);
		dealer.incluir("Responsabilidade Solidária / Subsidiária", null);
		dealer.incluir("Sentença Normativa", null);
		*/	

		Temas temaInicial = new Temas("1111111-11.1111.1.11.1111");				
		Vector vTemasInicial = temaInicial.carregarTemas();
		for (int i = 0; i < vTemasInicial.size(); i++) {
			dealer.incluir((String) vTemasInicial.elementAt(i), null);
			Vector vSubTemas = temaInicial.carregarSubTemas((String) vTemasInicial.elementAt(i));
			for (int j = 0; j < vSubTemas.size(); j++) {
				dealer.incluir((String) vSubTemas.elementAt(j), (String) vTemasInicial.elementAt(i));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaInicialQualif() {			
		Qualifica qualiInicial = new Qualifica("1111111-11.1111.1.11.1111");		
		Vector vQualiInicial = qualiInicial.carregarQualifica();		
		for (int i = 0; i < vQualiInicial.size(); i++) {
			dealer.incluir((String) vQualiInicial.elementAt(i), null);
			Vector vDepoentes = qualiInicial.carregarDepoentes((String) vQualiInicial.elementAt(i));
			for (int j = 0; j < vDepoentes.size(); j++) {
				dealer.incluir((String) vDepoentes.elementAt(j), (String) vQualiInicial.elementAt(i));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void carregaTemas(String strNumProcesso) {
		numProcesso = strNumProcesso;

		if (strNumProcesso.equals("0000000-00.0000.0.00.0000")) {
			tnTop.setUserObject("ASSUNTOS");
			tnTop.removeAllChildren();
		}
		else {			
			Temas tema = new Temas(strNumProcesso);		
			Vector vTemas = tema.carregarTemas();
			dealer.setNumProcesso(strNumProcesso);
			
			tnTop.setUserObject("Processo Nr: " + strNumProcesso);
			tnTop.removeAllChildren();

			if (vTemas.size() > 0) {
				for (int i = 0; i < vTemas.size(); i++) {
					tnRoot = new DefaultMutableTreeNode((String) vTemas.elementAt(i));
					tnTop.add(tnRoot);
					Vector vSubTemas = tema.carregarSubTemas((String) vTemas.elementAt(i));
					for (int j = 0; j < vSubTemas.size(); j++) {
						tnNode = new DefaultMutableTreeNode((String) vSubTemas.elementAt(j));
						tnRoot.add(tnNode);
					}
				}
			}
			else {
				cargaInicialTemas();
				Temas temaInicial = new Temas(strNumProcesso);			
				Vector vTemasInicial = temaInicial.carregarTemas();
				for (int i = 0; i < vTemasInicial.size(); i++) {
					tnRoot = new DefaultMutableTreeNode((String) vTemasInicial.elementAt(i));
					tnTop.add(tnRoot);
					Vector vSubTemas = temaInicial.carregarSubTemas((String) vTemasInicial.elementAt(i));
					for (int j = 0; j < vSubTemas.size(); j++) {
						tnNode = new DefaultMutableTreeNode((String) vSubTemas.elementAt(j));
						tnRoot.add(tnNode);
					}
				}
			}
			int linha = 0;
			while (linha < treeView.getRowCount()) {
				treeView.expandRow(linha++);
			}
		}
		treeView.updateUI();		
	}
	
	@SuppressWarnings("unchecked")
	public void carregaQualifica(String strNumProcesso, String strAutor) {
		numProcesso = strNumProcesso;
		Qualifica quali = new Qualifica(strNumProcesso);		
		Vector vQuali = quali.carregarQualifica();
		dealer.setNumProcesso(strNumProcesso);
		
		if (strNumProcesso.equals("0000000-00.0000.0.00.0000")) {
			tnTop.setUserObject("ORADORES");
			tnTop.removeAllChildren();
		}
		else {
			tnTop.setUserObject("Processo Nr: " + strNumProcesso);		
			tnTop.removeAllChildren();
			
			if (vQuali.size() > 0) {
				for (int i = 0; i < vQuali.size(); i++) {
					tnRoot = new DefaultMutableTreeNode((String) vQuali.elementAt(i));
					tnTop.add(tnRoot);
					if (((String)vQuali.elementAt(i)).toUpperCase().equals("AUTOR") && strAutor != null) {
						tnNode = new DefaultMutableTreeNode(strAutor);
						tnRoot.add(tnNode);
					}
					Vector vDepoente = quali.carregarDepoentes((String) vQuali.elementAt(i));
					for (int j = 0; j < vDepoente.size(); j++) {
						tnNode = new DefaultMutableTreeNode((String) vDepoente.elementAt(j));
						tnRoot.add(tnNode);
					}
				}		
			}
			else {
				cargaInicialQualif();				
				Qualifica qualiInicial = new Qualifica(strNumProcesso);		
				Vector vQualiInicial = qualiInicial.carregarQualifica();
				
				for (int i = 0; i < vQualiInicial.size(); i++) {
					tnRoot = new DefaultMutableTreeNode((String) vQualiInicial.elementAt(i));
					tnTop.add(tnRoot);
					if (((String)vQualiInicial.elementAt(i)).toUpperCase().equals("AUTOR") && strAutor != null) {
						tnNode = new DefaultMutableTreeNode(strAutor);
						tnRoot.add(tnNode);
					}						
					Vector vDepoentes = qualiInicial.carregarDepoentes((String) vQualiInicial.elementAt(i));
					for (int j = 0; j < vDepoentes.size(); j++) {
						tnNode = new DefaultMutableTreeNode((String) vDepoentes.elementAt(j));
						tnRoot.add(tnNode);
					}					
				}
			}
			int linha = 0;
			while (linha < treeView.getRowCount()) {
				treeView.expandRow(linha++);
			}
		}
		treeView.updateUI();
	}
	
	public String[] getNodesSelecionados() {
		tnLinha = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
		if (tnLinha == null) {
			String[] ret = {"", ""};
			return ret;
		}
		if (tnLinha.isRoot()) {					
			String [] ret = {"",""};
			return ret;
		}
		else {
			if (tnLinha.getLevel() > 1) {
				String [] ret = { tnLinha.getParent().toString(), tnLinha.toString() };
				return ret;
			}
			else {								
				String [] ret = { tnLinha.toString(), "" };
				return ret;
			}
		}
	}
}
