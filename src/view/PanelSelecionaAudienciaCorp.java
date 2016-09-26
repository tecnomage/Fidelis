package view;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import controller.GravaLog;
import controller.Sessao;
import model.DBCorp;
import model.TipoAudiencias;


@SuppressWarnings("serial")
public class PanelSelecionaAudienciaCorp extends JPanel {

	public String usuarioLogado = null;
	private DefaultTableModel tablemodel;
	private JScrollPane sp;
	private JTable tbDados;
	public int numAudiencias = 0;
	private JFormattedTextField dataDe;
	private JFormattedTextField dataAte;
	
	public PanelSelecionaAudienciaCorp(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
		Sessao.autor = null;
		
		this.setLayout(null);
		
		JPanel pRange = new JPanel();
		pRange.setLayout(null);
		//pRange.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleção do Período"));
		pRange.setBorder(javax.swing.BorderFactory.createTitledBorder("Agenda de Audiências"));
		pRange.setBounds(8, 5, 800, 70);
		
		JLabel lbDataDe = new JLabel("Audiências DE:");
		lbDataDe.setBounds(10, 25, 100, 20);
		JLabel lbDataAte = new JLabel("Audiências ATÉ:");
		lbDataAte.setBounds(10, 55, 100, 20);
		
		dataDe = new JFormattedTextField();
		dataDe.setBounds(130, 25, 80, 20);
		dataAte = new JFormattedTextField();
		dataAte.setBounds(130, 55, 80, 20);
		
		try {
			dataDe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
			dataAte.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		JButton btPesquisar = new JButton("Pesquisar Agenda");
		//btPesquisar.setBounds(10, 85, 150, 25);
		btPesquisar.setBounds(10, 25, 180, 25);
		
		btPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	try {
					btPesquisarEvent(evt);
				} catch (SQLException e) {
					e.printStackTrace();
					GravaLog log = new GravaLog("ERRO");
					log.gravaExcep("PanelSelecionaAudienciaCorp", e);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					GravaLog log = new GravaLog("ERRO");
					log.gravaExcep("PanelSelecionaAudienciaCorp", e);
				}
            }
        });
		
		//pRange.add(lbDataDe);
		//pRange.add(lbDataAte);
		//pRange.add(dataDe);
		//pRange.add(dataAte);
		pRange.add(btPesquisar);
	
		JPanel borda = new JPanel();
		borda.setLayout(null);
		borda.setBorder(javax.swing.BorderFactory.createTitledBorder("Relação de Audiências"));
		borda.setBounds(8, 80, 800, 450);

		tablemodel = new DefaultTableModel(){   
			public boolean isCellEditable(int rowIndex, int mColIndex) {   
				return false;   
			}   
		};  

		tablemodel.setColumnIdentifiers(new String[] {"Processo", "Data", "Hora", "Tipo", "Autor" });		
		tbDados = new JTable(tablemodel);		
		tbDados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbDados.setModel(tablemodel);	
		tbDados.setRowHeight(20);

        sp = new JScrollPane(tbDados);
        sp.setBounds(10, 20, 780, 420);
        borda.add(sp);		
		
		ListSelectionModel rowTbl = tbDados.getSelectionModel();   
		rowTbl.addListSelectionListener(new ListSelectionListener() {   
		    public void valueChanged(ListSelectionEvent evt) {   
		    	setAudienciaDados();   
		    }   
		});
		
		this.add(borda);	
		this.add(pRange);
	}
	
	public void setAudienciaDados() {
		if (tbDados.getSelectedRow() >= 0) {
			Sessao.numProcesso = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 0);
			Sessao.tipo = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 3);
			Sessao.autor = (String) tbDados.getValueAt(tbDados.getSelectedRow(), 4);
		}
		return;
	}	
	
	
	@SuppressWarnings("unchecked")
	private void btPesquisarEvent(java.awt.event.ActionEvent evt) throws SQLException, ClassNotFoundException {		
		DBCorp corp = new DBCorp(usuarioLogado);
		Vector vRetorno = corp.getAudienciasFunc(dataDe.getText(), dataAte.getText());
		
		while (tablemodel.getRowCount() > 0)
			tablemodel.removeRow(0);
		
		TipoAudiencias ta = new TipoAudiencias();
		
		for (int i = 0; i < vRetorno.size(); i++) {			
			String[] strRet = (String[]) vRetorno.elementAt(i);			
	        tablemodel.addRow(new String[] {null, null, null, null, null});
			tbDados.setValueAt(strRet[0], i, 0);
			tbDados.setValueAt(strRet[1], i, 1);
			tbDados.setValueAt(strRet[2], i, 2);
			tbDados.setValueAt(strRet[3], i, 3);
			tbDados.setValueAt(strRet[4], i, 4);
			
			if (ta.getCodTipoAudiencia(strRet[3]) < 1) {
				JOptionPane.showMessageDialog(null, "O Tipo de Audiência < " + strRet[3] + " > não está cadastrado neste sistema.", "Atenção", JOptionPane.WARNING_MESSAGE);
			}
		}		
		corp.fechaConexao();		
		JOptionPane.showMessageDialog(null, "Recuperada(s) " + vRetorno.size() + " audiência(s) do Banco de Dados Corporativo para o período informado.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
	}
}
