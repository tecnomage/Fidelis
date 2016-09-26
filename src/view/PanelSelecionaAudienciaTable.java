package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import controller.DBDealer;
import controller.GravaLog;
import controller.Sessao;
import controller.SessaoConfig;
import controller.UpperField;


@SuppressWarnings("serial")
public class PanelSelecionaAudienciaTable extends JPanel {

	public String usuarioLogado = null;
	private DefaultTableModel tablemodel;
	private JScrollPane sp;
	private JTable tbDados;
	public int numAudiencias = 0;
	private boolean isVinculacao = false;
	
	@SuppressWarnings("unchecked")
	public PanelSelecionaAudienciaTable(String strUsuarioLogado, String strFiltro, boolean vinculacao) {
		usuarioLogado = strUsuarioLogado;
		isVinculacao = vinculacao;
		
		this.setLayout(null);
		
		JPanel pFiltro = new JPanel();
		pFiltro.setLayout(null);
		pFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro"));
		pFiltro.setBounds(8, 5, 800, 95);
		
		final JComboBox cbCampos = new JComboBox();
		cbCampos.addItem("NÚMERO DO PROCESSO");
		cbCampos.addItem("DATA");
		cbCampos.addItem("TIPO DE AUDIÊNICA");
		cbCampos.addItem("JUIZ");
		cbCampos.addItem("STATUS");
		cbCampos.setBounds(10, 25, 220, 20);
		
		final UpperField tfBusca = new UpperField();
		tfBusca.setBounds(10, 55, 350, 20);
		
		JButton btBusca = new JButton("Filtrar");
		btBusca.setBounds(370, 55, 100, 20);
		
		pFiltro.add(cbCampos);		
		pFiltro.add(tfBusca);
		pFiltro.add(btBusca);
	
		JPanel borda = new JPanel();
		borda.setLayout(null);
		borda.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Sobre as Audiências"));
		borda.setBounds(8, 105, 800, 400);

		tablemodel = new DefaultTableModel(){   
			public boolean isCellEditable(int rowIndex, int mColIndex) {   
				return false;   
			}   
		};  

		tablemodel.setColumnIdentifiers(new String[] {"Processo", "Data", "Hora", "Tipo", "Juiz", "Status"});		
		tbDados = new JTable(tablemodel);
		
		//TableCellRenderer tcr = new Colorir();
	    //TableColumn column = tbDados.getColumnModel().getColumn(5);
		//column.setCellRenderer(tcr);
		
		tbDados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbDados.setModel(tablemodel);	
		tbDados.setRowHeight(20);
		
		javax.swing.table.TableColumnModel colmod = tbDados.getColumnModel();
		colmod.getColumn(1).setMaxWidth(120);
		colmod.getColumn(2).setMaxWidth(120);
		colmod.getColumn(3).setMaxWidth(150);
		//colmod.getColumn(5).setMaxWidth(200);
        sp = new JScrollPane(tbDados);
        sp.setBounds(10, 20, 780, 370);
        borda.add(sp);		
     
        Vector vtRetorno = null;
		final DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS");
		if (strFiltro.equals("PENDENTES")) {
			vtRetorno = dealer.getAudienciasPendentes();
		}
		else {
			vtRetorno = dealer.getAudiencias();
		}
		
		String [] strRetorno = new String[5];
		int linha = 0;
		for (int i = 0; i < vtRetorno.size(); i++) {
			strRetorno = (String[]) vtRetorno.elementAt(i);
			
			if (!strRetorno[0].equals("0000000-00.0000.0.00.0000")) {
				
				if (isVinculacao) {
					// Se for vinculacao, descarta audiencias que nao estao concluidas ou vinculadas anteriormente.
					if (!strRetorno[5].equals("0") && !strRetorno[5].equals("998"))
						continue;
				}
				
		        tablemodel.addRow(new String[] {null, null, null, null, null});
				tbDados.setValueAt(strRetorno[0], linha, 0);
				tbDados.setValueAt(strRetorno[1], linha, 1);
				tbDados.setValueAt(strRetorno[2], linha, 2);
				tbDados.setValueAt(dealer.getDescTipoAudiencia(new Integer(strRetorno[3])).toString(), linha, 3);
				tbDados.setValueAt(strRetorno[4], linha, 4);
				if (strRetorno[5].equals("-1"))
					tbDados.setValueAt("ERRO GRAVAÇÃO", linha, 5);
				else
					if (strRetorno[5].equals("0"))
						tbDados.setValueAt("CONCLUÍDA", linha, 5);
					else
						if (strRetorno[5].equals("999"))
							tbDados.setValueAt("TRANSMITIDA", linha, 5);
						else
							if (strRetorno[5].equals("998"))
								tbDados.setValueAt("VINCULADA", linha, 5);
							else
								if (strRetorno[5].equals("-2"))
									tbDados.setValueAt("IMPORTADA", linha, 5);
								else
									if (strRetorno[5].equals("-3"))
										tbDados.setValueAt("RECUPERADA", linha, 5);
									else
										tbDados.setValueAt("INTERROMPIDA", linha, 5);
				
				linha++;
			}
		}
		
		numAudiencias = vtRetorno.size();
		
		ListSelectionModel rowTbl = tbDados.getSelectionModel();   
		rowTbl.addListSelectionListener(new ListSelectionListener() {   
		    public void valueChanged(ListSelectionEvent evt) {   
		    	setAudienciaDados();   
		    }   
		});
		
		final JPopupMenu popup = new JPopupMenu();
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().toString().equals("Mudar status para VINCULADA")) {
					Object[] options = { "Sim", "Não" };
                	int resposta = JOptionPane.showOptionDialog(null, "Deseja alterar o status desta audiência para VINCULADA?", "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        	    	if (resposta == 0) {
						DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0), (String) tbDados.getValueAt(tbDados.getSelectedRow(), 1), (String) tbDados.getValueAt(tbDados.getSelectedRow(), 2));
	    	    		if (dealer.mudaStatus("VINCULADO")) {
	    	    			tbDados.setValueAt("VINCULADA", tbDados.getSelectedRow(), 5);
	    	    			GravaLog logInfo = new GravaLog("INFO");
	    	    			logInfo.grava("fidelis.view.PanelSelecionaAudienciaTable()", usuarioLogado, "Status do Processo " + (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0) + " alterado para VINCULADO!");
	    	    		}
        	    	}
				}
				if (event.getActionCommand().toString().equals("Mudar status para INTERROMPIDA")) {
					Object[] options = { "Sim", "Não" };
                	int resposta = JOptionPane.showOptionDialog(null, "Deseja alterar o status desta audiência para INTERROMPIDA?", "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        	    	if (resposta == 0) {
						DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0), (String) tbDados.getValueAt(tbDados.getSelectedRow(), 1), (String) tbDados.getValueAt(tbDados.getSelectedRow(), 2));
	    	    		// Muda o status da audiencia para o 1. motivo de finalizacao cadastrado. 
						if (dealer.mudaStatus("1")) {
	    	    			tbDados.setValueAt("INTERROMPIDA", tbDados.getSelectedRow(), 5);
	    	    			GravaLog logInfo = new GravaLog("INFO");
	    	    			logInfo.grava("fidelis.view.PanelSelecionaAudienciaTable()", usuarioLogado, "Status do Processo " + (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0) + " alterado para INTERROMPIDO!");
	    	    		}
        	    	}
				}
			}
		};
		JMenuItem item = new JMenuItem();
		popup.add(item = new JMenuItem("Mudar status para VINCULADA"));
		popup.add(item = new JMenuItem("Mudar status para INTERROMPIDA"));
	    item.addActionListener(menuListener);
	    
	    tbDados.addMouseListener(new MouseAdapter() {
	    	public void mousePressed(MouseEvent e) {
	    		checkPopup(e);
		    }
		    public void mouseClicked(MouseEvent e) {
		    	checkPopup(e);
		    }
		    public void mouseReleased(MouseEvent e) {
		    	checkPopup(e);
		    }
		    private void checkPopup(MouseEvent e) {
		    	if (e.isPopupTrigger()) {
		    		if (SessaoConfig.isAdmin && ((String) tbDados.getValueAt(tbDados.getSelectedRow(), 5)).equals("CONCLUÍDA"))
		    			popup.show(e.getComponent(), e.getX(), e.getY());
		    	}
		    }
	    });
	    
	    btBusca.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				int i = 0;
				int coluna = 0;
				if (cbCampos.getSelectedItem().equals("NÚMERO DO PROCESSO")) {
					coluna = 0;
				}
				if (cbCampos.getSelectedItem().equals("DATA")) {
					coluna = 1;
				}
				if (cbCampos.getSelectedItem().equals("TIPO DE AUDIÊNICA")) {
					coluna = 3;
				}
				if (cbCampos.getSelectedItem().equals("JUIZ")) {
					coluna = 4;
				}
				if (cbCampos.getSelectedItem().equals("STATUS")) {
					coluna = 5;
				}
				while (i < tbDados.getRowCount()) {					
					if (((String) tbDados.getValueAt(i, coluna)).lastIndexOf(tfBusca.getText()) < 0) {
						DefaultTableModel model = (DefaultTableModel)tbDados.getModel();
						model.removeRow(i);
					}
					else {
						i++;
					}						
				}
			}		
		});
		
		//this.setLayout(null);
	    this.add(pFiltro);
		this.add(borda);
		
		//Ferramentas f = new Ferramentas();
		//if (f.getPropriedades("player").equals("1")) {
		//	JOptionPane.showMessageDialog(null, "As audiências serão recuperadas da base de dados remota configurada.\nDiretório dos arquivos de áudio e vídeo: " + f.getPropriedades("videos"), "ATENÇÃO", JOptionPane.INFORMATION_MESSAGE);
		//}
	}
	
	public void setAudienciaDados() {
		if (tbDados.getSelectedRow() >= 0) {
			Sessao.numProcesso = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0);
			Sessao.data = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 1);
			Sessao.hora = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 2);
			Sessao.tipo = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 3);
			Sessao.juiz = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 4);
		}
		return;
	}
	/*
	class Colorir extends JLabel implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			
			setForeground(Color.BLACK);
			
			if (value.equals("PENDENTE")){
				setForeground(Color.YELLOW);	
			}
			if (value.equals("ERRO GRAVAÇÃO")) {
				setForeground(Color.RED);
			}
			if (value.equals("CONCLUÍDA")) {
				setForeground(Color.GREEN);
			}	    	 
			 		     
			setText(value.toString());		     	
			return this;   	
		}
	}
	*/
}
