package view;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.table.DefaultTableModel;

import controller.Sessao;
import controller.UpperField;
import model.DBCorp;

@SuppressWarnings("serial")
public class PanelPreVinculacao extends JPanel {
	
	private JFormattedTextField tfNumProcesso;
	private JButton btIncluir = null;
	private JButton btIncluiAssunto = null;	
	private JButton btAssunto = null;
	private JButton btPesquisar = null;
	private String usuarioLogado = null;
	private PanelTreeTemasEQualif pTemas = null;	
	private DefaultTableModel tablemodelMarcacoes;
	private JTable tbMarcacoes;
	private UpperField tfAssunto;
	
	public PanelPreVinculacao(String strUsuarioLogado) {		
		usuarioLogado = strUsuarioLogado;
		
		int MARGEM_LABEL = 12;
		int MARGEM_TF = 145;
		int Y = 12;

		this.setLayout(null);		
		
		FocusListener focusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				tfNumProcesso.setCaretPosition(0);				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		};
		
		// Componentes de dados dos Processos. Labels e textfields
		JPanel pDadosProc = new JPanel();
		pDadosProc.setLayout(null);
		pDadosProc.setBounds(5, 5, 355, 80);
		pDadosProc.setBorder(javax.swing.BorderFactory.createEtchedBorder());		  
	
		JLabel lb1 = new JLabel("Número do Processo:");
		lb1.setBounds(MARGEM_LABEL, Y, 200, 20);
		
		tfNumProcesso = new JFormattedTextField();		
		tfNumProcesso.addFocusListener(focusListener);
		tfNumProcesso.setBounds(MARGEM_TF, Y, 200, 20);		
		if (Sessao.numProcesso != null) {
			tfNumProcesso.setText(Sessao.numProcesso.trim());
		}
		
	    // Botoes de inclusao dos dados da audiencia
		btIncluir = new JButton("Validar");
		btIncluir.setBounds(MARGEM_TF + 103, Y+30, 95, 25);
		
		// Painel de Temas/SubTemas e Qualificacoes/Depoentes
		pTemas = new PanelTreeTemasEQualif(usuarioLogado, "", "TEMAS");
		pTemas.setLocation(370, 1);
		this.add(pTemas);
		
		// Painel de listagem das marcacoes		
		tablemodelMarcacoes = new DefaultTableModel(){   
			public boolean isCellEditable(int rowIndex, int mColIndex) {   
				return false;   
			}   
		};      
		tablemodelMarcacoes.setColumnIdentifiers(new String[] {"Assuntos CNJ - Lista"});		
		tbMarcacoes = new JTable(tablemodelMarcacoes);
		
		tbMarcacoes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbMarcacoes.setModel(tablemodelMarcacoes);	
		
        JScrollPane spListaMarcacoes = new JScrollPane(tbMarcacoes);
        spListaMarcacoes.setBounds(5, 91, 355, 320);    
		
		JPanel pBotoesVideo = new JPanel();
		pBotoesVideo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pBotoesVideo.setBounds(5, 418, 355, 37);
		pBotoesVideo.setLayout(null);
				
		btIncluiAssunto = new JButton("Incluir Assuntos Selecionados");	
		btIncluiAssunto.setBounds(125, 5, 220, 25);
		btIncluiAssunto.setEnabled(false);		
		
		// Painel de Finalizacao
		JPanel pFinaliza = new JPanel();
		pFinaliza.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pFinaliza.setBounds(5, 462, 355, 96);
		pFinaliza.setLayout(null);
		
		JLabel lbMotivo = new JLabel("Assunto CNJ:");
		lbMotivo.setBounds(6, 6, 150, 15);
		tfAssunto = new UpperField();
		tfAssunto.setBounds(6, 28, 340, 21);

		btPesquisar = new JButton("Pesquisar");		
		btPesquisar.setBounds(95, 55, 100, 25);
		btPesquisar.setEnabled(false);
		
		btAssunto = new JButton("Incluir Assunto");
		btAssunto.setBounds(204, 55, 140, 25);
		btAssunto.setEnabled(false);
		pFinaliza.add(lbMotivo);  
		pFinaliza.add(tfAssunto);
		pFinaliza.add(btAssunto);
		pFinaliza.add(btPesquisar);
		
		
		// Funcoes de eventos dos botoes
		btIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btIncluirEvent(evt);
            }
        });
		
		btIncluiAssunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btIncluiAssuntoEvent(evt);
            }
        });	
		
		btAssunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btAssuntoEvent(evt);
            }
        });
		
		btPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btPesquisarEvent(evt);
            }
        });
		
		// Adicao dos componentes nos paineis e container		
		pDadosProc.add(lb1);
		pDadosProc.add(tfNumProcesso);	  
		pDadosProc.add(btIncluir);
        pBotoesVideo.add(btIncluiAssunto);
		this.add(pDadosProc);
        this.add(pBotoesVideo);
		this.add(pFinaliza);
		this.add(spListaMarcacoes);
	}
	
	
	public boolean validaNumProcesso(String strNumProcesso) {
		
		String strAux = "";
		for (int i=0; i<strNumProcesso.length(); i++) {
			if (new String ("0123456789").lastIndexOf(strNumProcesso.substring(i, i + 1)) > -1)
				strAux += (strNumProcesso.substring(i, i + 1)); 
		}
		strNumProcesso = strAux;

		if (tfNumProcesso.getFormatterFactory() != null && strNumProcesso.length() > 0)
			return true;
		
		if (strNumProcesso.length() == 20) {
			try {
				tfNumProcesso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#######-##.####.#.##.####")));
				tfNumProcesso.setText(strNumProcesso);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return true;
		}	
		
		if (strNumProcesso.length() == 17) {
			try {
				tfNumProcesso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##### #### ### ## ## #")));
				tfNumProcesso.setText(strNumProcesso);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return true;
		}
		else
			return false;
	}
	
	
	@SuppressWarnings("unchecked")
	private void btIncluirEvent(java.awt.event.ActionEvent evt) {		
		tfNumProcesso.setText(tfNumProcesso.getText().trim());
		if (!validaNumProcesso(tfNumProcesso.getText())) {
			JOptionPane.showMessageDialog(null, "Número de Processo inválido!", "ATENÇÃO", JOptionPane.WARNING_MESSAGE);
		}
		else {
			limpaAssuntos();
	        if (Sessao.numProcesso == null) {
	        	Sessao.numProcesso = tfNumProcesso.getText();
	        }	
	        
        	pTemas.carregaTemas(tfNumProcesso.getText());
        	
	        DBCorp corp = new DBCorp(usuarioLogado);
	        Vector vtRetAssunto = corp.getAssuntos(Sessao.numProcesso);
	        //corp.fechaConexao();
	        
	        //((AutoFillField)tfAssunto).setPropriedades(vtRetTodos);

	        for (int i = 0; i < vtRetAssunto.size(); i++) {				
	        	tablemodelMarcacoes.addRow(new String[] {null});
	        	tablemodelMarcacoes.setValueAt((String) vtRetAssunto.elementAt(i), i, 0);
	        }
	        
	        if (vtRetAssunto.size() < 1)
	        	JOptionPane.showMessageDialog(null, "Nenhum Assunto CNJ foi cadastrado para este Processo!", "Atenção", JOptionPane.WARNING_MESSAGE);
	        else
	        	btIncluiAssunto.setEnabled(true);
	        
	        btAssunto.setEnabled(true);
	        btPesquisar.setEnabled(true);
		}
	}
	
	private void btIncluiAssuntoEvent(java.awt.event.ActionEvent evt) {
		int [] ii = tbMarcacoes.getSelectedRows();
		for (int i=0; i<ii.length; i++) {			
			pTemas.incluirNode(tfNumProcesso.getText(), (String)tablemodelMarcacoes.getValueAt(ii[i], 0).toString().trim());
		}
	}
	
	private void btAssuntoEvent(java.awt.event.ActionEvent evt) {
		pTemas.incluirNode(tfNumProcesso.getText(), tfAssunto.getText());
	}
	
	@SuppressWarnings("unchecked")
	private void btPesquisarEvent(java.awt.event.ActionEvent evt) {
		DBCorp corp = new DBCorp(usuarioLogado, "Local");
        Vector vtRetTodos = corp.getAssuntosTodos();
        //corp.fechaConexao();        
		FrameBuscaAssuntoCNJ buscaAssunto = new FrameBuscaAssuntoCNJ(tfAssunto, vtRetTodos, pTemas, tfNumProcesso.getText());
		buscaAssunto.setVisible(true);
	}
	
	public void setNumProcesso() {
		tfNumProcesso.setText(Sessao.numProcesso);
		if (Sessao.numProcesso != null) 
			tfNumProcesso.setEditable(false);
	}
	
	public void limpaAssuntos() {
		while(tablemodelMarcacoes.getRowCount() > 0)
			tablemodelMarcacoes.removeRow(0);	
		
		btIncluiAssunto.setEnabled(false);
	}
}
