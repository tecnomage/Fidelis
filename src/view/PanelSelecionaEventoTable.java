package view;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import controller.DBDealer;
import controller.Sessao;


@SuppressWarnings("serial")
public class PanelSelecionaEventoTable extends JPanel {

	public String usuarioLogado = null;
	private DefaultTableModel tablemodel;
	private JScrollPane sp;
	private JTable tbDados;
	public int numAudiencias = 0;
	
	@SuppressWarnings("unchecked")
	public PanelSelecionaEventoTable(String strUsuarioLogado, String strFiltro) {
		usuarioLogado = strUsuarioLogado;
		
		this.setLayout(null);
	
		JPanel borda = new JPanel();
		borda.setLayout(null);
		borda.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Sobre os Eventos"));
		borda.setBounds(8, 5, 800, 500);

		tablemodel = new DefaultTableModel(){   
			public boolean isCellEditable(int rowIndex, int mColIndex) {   
				return false;   
			}   
		};  

		tablemodel.setColumnIdentifiers(new String[] {"Nome do Evento", "Data", "Hora", "Status"});		
		tbDados = new JTable(tablemodel);
		
		//TableCellRenderer tcr = new Colorir();
	    //TableColumn column = tbDados.getColumnModel().getColumn(5);
		//column.setCellRenderer(tcr);
		
		tbDados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbDados.setModel(tablemodel);	
		tbDados.setRowHeight(20);
		
		javax.swing.table.TableColumnModel colmod = tbDados.getColumnModel();
		colmod.getColumn(0).setMinWidth(300);
		//colmod.getColumn(2).setMaxWidth(600);
		//colmod.getColumn(3).setMaxWidth(600);
		//colmod.getColumn(5).setMaxWidth(200);
        sp = new JScrollPane(tbDados);
        sp.setBounds(10, 20, 780, 470);
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
			
			if (strRetorno[0].equals("0000000-00.0000.0.00.0000")) {
		        tablemodel.addRow(new String[] {null, null, null, null});
				tbDados.setValueAt(strRetorno[4], linha, 0);
				tbDados.setValueAt(strRetorno[1], linha, 1);
				tbDados.setValueAt(strRetorno[2], linha, 2);
				//tbDados.setValueAt(dealer.getDescTipoAudiencia(new Integer(strRetorno[3])).toString(), linha, 3);
				//tbDados.setValueAt(strRetorno[4], linha, 4);
				/*
				if (strRetorno[5].equals("-1"))
					tbDados.setValueAt("ERRO GRAVAÇÃO", linha, 3);
				else
					if (strRetorno[5].equals("0"))
						tbDados.setValueAt("CONCLUÍDA", linha, 3);
					else
						if (strRetorno[5].equals("999"))
							tbDados.setValueAt("TRANSMITIDA", linha, 3);
						else
							tbDados.setValueAt("INTERROMPIDA", linha, 3);
				*/
				if (strRetorno[5].equals("-1"))
					tbDados.setValueAt("ERRO GRAVAÇÃO", linha, 3);
				else
					if (strRetorno[5].equals("0") || strRetorno[5].equals("998"))
						tbDados.setValueAt("CONCLUÍDO", linha, 3);
					else
						if (strRetorno[5].equals("999"))
							tbDados.setValueAt("TRANSMITIDO", linha, 3);
						else
							if (strRetorno[5].equals("-2"))
								tbDados.setValueAt("IMPORTADO", linha, 3);
							else
								if (strRetorno[5].equals("-3"))
									tbDados.setValueAt("RECUPERADO", linha, 3);
								else
									tbDados.setValueAt("INTERROMPIDO", linha, 3);
				
				linha++;
			}
		}
		
		numAudiencias = linha;
		
		ListSelectionModel rowTbl = tbDados.getSelectionModel();   
		rowTbl.addListSelectionListener(new ListSelectionListener() {   
		    public void valueChanged(ListSelectionEvent evt) {   
		    	setAudienciaDados();   
		    }   
		});
		
		//this.setLayout(null);
		this.add(borda);
		
		//Ferramentas f = new Ferramentas();
		//if (f.getPropriedades("player").equals("1")) {
		//	JOptionPane.showMessageDialog(null, "Os eventos serão recuperados da base de dados configurada no arquivo SGBD.INI.\nDiretório dos arquivos de áudio e vídeo: " + f.getPropriedades("videos"), "ATENÇÃO", JOptionPane.INFORMATION_MESSAGE);
		//}
	}
	
	public void setAudienciaDados() {
		if (tbDados.getSelectedRow() >= 0) {
			Sessao.numProcesso = "0000000-00.0000.0.00.0000";
			Sessao.data = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 1);
			Sessao.hora = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 2);
			Sessao.tipo = "";
			Sessao.juiz = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0);
		}
		return;
	}
	
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
	
}
