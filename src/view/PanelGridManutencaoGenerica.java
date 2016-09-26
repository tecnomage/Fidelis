package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.GravaLog;
import controller.SessaoConfig;

@SuppressWarnings("serial")
public class PanelGridManutencaoGenerica extends JPanel {
	
	private String usuarioLogado = null;
	private JPopupMenu popup = null;
	private DefaultTableModel tablemodel = null;
	private JTable tbDados = null;
	private String tabela;
	private int totalColunas;
	private String [][] colunas;
	@SuppressWarnings("unchecked")
	private Vector vLinha;
	
	@SuppressWarnings("unchecked")
	public PanelGridManutencaoGenerica(String strUsuarioLogado, String strTabela) {
		usuarioLogado = strUsuarioLogado;
		tabela = strTabela;
		
		//JPanel pCentro = new JPanel();	
		
		tablemodel = new DefaultTableModel(); 
		tablemodel.setColumnIdentifiers(getColunas(tabela));
		
		tbDados = new JTable(tablemodel);
		tbDados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbDados.setModel(tablemodel);
		tbDados.setRowHeight(20);
				
        JScrollPane sp = new JScrollPane(tbDados);
        sp.setBounds(10, 10, 785, 555);
        //pCentro.add(sp);
        this.setLayout(null);
        this.add(sp);
        
		String sql = "select * from " + tabela;		
		Connection con = SessaoConfig.conexaoRemota;
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			
			int linha = 0;
			vLinha = new Vector();
			while(rs.next()) {
				tablemodel.addRow(new String[md.getColumnCount()]);
				for (int coluna=1; coluna<=md.getColumnCount(); coluna++) {			        
					tbDados.setValueAt(rs.getObject(coluna), linha, coluna-1);
				}
				linha++;
			}
			rs.close();
			st.close();
			
			for (int i=0; i<tbDados.getRowCount(); i++) {
				String [] strValoresLinha = new String[totalColunas];
				for (int j=0; j<totalColunas; j++) {
					strValoresLinha[j] = String.valueOf(tbDados.getValueAt(i, j));
				}
				vLinha.addElement(strValoresLinha);	
			}
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.PanelGridManutencaoGenerica.PanelGridManutencaoGenerica()", e);
			e.printStackTrace();
		}		

		popup = new JPopupMenu();
		
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				if (event.getActionCommand().toString().equals("Incluir Registro")) {
			        tablemodel.addRow(new String[] {null});
			        JOptionPane.showMessageDialog(null, "Após inserir os dados, clique em SALVAR REGISTRO\npara efetivar a inserção no banco de dados.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if (event.getActionCommand().toString().equals("Excluir Registro")) {				
					if (excluir(tbDados.getSelectedRow())) {			
						vLinha.removeElementAt(tbDados.getSelectedRow());
						tablemodel.removeRow(tbDados.getSelectedRow());						
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelGridManutencaoGenerica.excluir()", usuarioLogado, "Exclusão do registro realizada com sucesso.");
					}
					else {
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelGridManutencao.excluir()", usuarioLogado, "Erro ao excluir registro");
						JOptionPane.showMessageDialog(null, "Erro ao excluir registro.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				if (event.getActionCommand().toString().equals("Salvar Registro")) {
					if (atualizar(tbDados.getSelectedRow())) {							
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelGridManutencaoGenerica.atualizar()", usuarioLogado, "Atualização do registro realizada com sucesso.");
					}
					else {
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelGridManutencao.atualizar()", usuarioLogado, "Erro ao atualizar registro");
						JOptionPane.showMessageDialog(null, "Erro ao atualizar registro.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		};

		JMenuItem item = new JMenuItem();
		popup.add(item = new JMenuItem("Incluir Registro"));
	    item.addActionListener(menuListener);
	    popup.add(item = new JMenuItem("Excluir Registro"));
	    item.addActionListener(menuListener);
	    popup.addSeparator();
	    popup.add(item = new JMenuItem("Salvar Registro"));
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
		    		popup.show(e.getComponent(), e.getX(), e.getY());
		    	}
		    }
	    });

	    sp.addMouseListener(new MouseAdapter() {
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
		    		popup.show(e.getComponent(), e.getX(), e.getY());
		    	}
		    }
	    });
		
		//this.setLayout(new BorderLayout());
	    //this.setLayout(null);
		//this.add(pCentro);//, BorderLayout.CENTER);		
	}
		
	public String[] getColunas(String nomeTabela) {
		String sql = "select * from " + nomeTabela + " where 1 = 2";		
		Connection con = SessaoConfig.conexaoRemota;
		
		String [] ret;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			
			totalColunas = md.getColumnCount();
			
			colunas = new String[totalColunas][2];
			ret = new String[totalColunas];			
			for (int i=1; i<=totalColunas; i++) {
				ret[i-1] = md.getColumnName(i);
				colunas[i-1][0] = md.getColumnName(i);
				colunas[i-1][1] = md.getColumnTypeName(i);
				//System.out.println(md.getColumnTypeName(i));
			}
			rs.close();
			st.close();			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.PanelGridManutencaoGenerica.getColunas()", e);
			e.printStackTrace();
			ret = null;
		}		
		return ret;		
	}
	
	public boolean excluir(int linha) {
		String sql = "DELETE FROM " + tabela + " WHERE ";
		String and = "";
		
		for (int i=0; i<totalColunas; i++) {			
			if (!String.valueOf(tbDados.getValueAt(linha, i)).equals("null")) {
				
				if (colunas[i][1].lastIndexOf("DATE") > -1)
					sql += and + "to_char(" + colunas[i][0] + ", 'YYYY-MM-DD')" + " = ";
				else
					sql += and + colunas[i][0] + " = ";
				
				if ((colunas[i][1].lastIndexOf("CHAR") > -1 || colunas[i][1].lastIndexOf("DATE") > -1))
					sql += "'" + String.valueOf(tbDados.getValueAt(linha, i)) + "'";
				else
					sql += String.valueOf(tbDados.getValueAt(linha, i));
				
				and = " AND ";
			}
		}
		
		Connection con = SessaoConfig.conexaoRemota;
		try {	   
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.PanelGridManutencaoGenerica.excluir()", e);
			e.printStackTrace();
			return false;
		} 
		//System.out.println(sql);
		
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean atualizar(int linha) {		
		// atualiza registro existente no db
		if (linha <= vLinha.size() - 1) { 		
			String sql = "UPDATE " + tabela + " SET ";
			String and = "";
			String where = " WHERE ";
			String [] virgula = new String [totalColunas];		
			
			for (int i=0; i<totalColunas-1; i++)
				virgula[i] = ", ";
			virgula[totalColunas-1] = " ";		
			
			for (int i=0; i<totalColunas; i++) {
				
				sql += colunas[i][0] + " = ";
				
				if (colunas[i][1].lastIndexOf("CHAR") > -1)
					sql += "'" + tbDados.getValueAt(linha, i) + "'" + virgula[i];
				else if	(colunas[i][1].lastIndexOf("DATE") > -1)
					sql += "to_date('" + tbDados.getValueAt(linha, i) + "', 'YYYY-MM-DD')" + virgula[i];
				else
					sql += tbDados.getValueAt(linha, i) + virgula[i];
				
			}				
			
			String [] valores = (String[]) vLinha.elementAt(linha);
			for (int i=0; i<totalColunas; i++) {				
				if (!valores[i].equals("null")) {
					
					if (colunas[i][1].lastIndexOf("DATE") > -1)
						where += and + "to_char(" + colunas[i][0] + ", 'YYYY-MM-DD')" + " = ";
					else
						where += and + colunas[i][0] + " = ";
					
					if ((colunas[i][1].lastIndexOf("CHAR") > -1 || colunas[i][1].lastIndexOf("DATE") > -1))
						where += "'" + valores[i] + "'";
					else
						where += valores[i];
					
					and = " AND ";
				}
			}
			
			sql += where;
			
			Connection con = SessaoConfig.conexaoRemota;
			try {	   
				Statement st = con.createStatement();
				st.execute(sql);
				st.close();
				String [] newValores = new String[totalColunas];
				for (int i=0; i<totalColunas; i++) {
					newValores[i] = String.valueOf(tbDados.getValueAt(linha, i));
				}
				vLinha.insertElementAt(newValores, linha);
				vLinha.removeElementAt(linha + 1);
			}
			catch (SQLException e) {
				GravaLog log = new GravaLog("ERRO");
				log.gravaExcep("fidelis.view.PanelGridManutencaoGenerica.atualizar()", e);
				e.printStackTrace();
				return false;
			} 
		}
		// insere um novo registro no db
		else {
			String sql = "INSERT INTO " + tabela + " VALUES (";
			for (int i=0; i<totalColunas; i++) {
				if (colunas[i][1].lastIndexOf("CHAR") > -1)
					sql += "'" + String.valueOf(tbDados.getValueAt(linha, i)) + "',";
				else if (colunas[i][1].lastIndexOf("DATE") > -1)
					sql += "to_date('" + String.valueOf(tbDados.getValueAt(linha, i)) + "', 'YYYY-MM-DD'),";
				else
					sql += String.valueOf(tbDados.getValueAt(linha, i)) + ",";
			}

			sql = sql.substring(0, sql.length() - 1) + ")";
			//System.out.println(sql);
			
			Connection con = SessaoConfig.conexaoRemota;
			try {	   
				Statement st = con.createStatement();
				st.execute(sql);
				st.close();
				String [] newValores = new String[totalColunas];
				for (int i=0; i<totalColunas; i++) {
					newValores[i] = String.valueOf(tbDados.getValueAt(linha, i));
				}
				vLinha.addElement(newValores);
			}
			catch (SQLException e) {
				GravaLog log = new GravaLog("ERRO");
				log.gravaExcep("fidelis.view.PanelGridManutencaoGenerica.atualizar()", e);
				e.printStackTrace();
				return false;
			}					
		}
		
		return true;
	}
}
