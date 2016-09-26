package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import controller.UpperField;

@SuppressWarnings("serial")
public class FrameBuscaAssuntoCNJ extends JDialog {
	
	private JButton btFecha = null;
	private JButton btSalva = null;

	private UpperField strAssunto;
	private DefaultTableModel tablemodelAssuntos = null;
	private JTable tbAssuntos = null;
	private JPanel pTemas = null;
	private String strNumProcesso = null;

	@SuppressWarnings("unchecked")
	public FrameBuscaAssuntoCNJ(UpperField assunto, Vector vAssuntos, PanelTreeTemasEQualif temas, String numProcesso) {
		strAssunto = assunto;
		pTemas = temas;
		strNumProcesso = numProcesso;
		
		this.setTitle("Assuntos CNJ - Lista");
		this.setSize(500, 640);
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
        this.setLocation((tela.width - this.getSize().width), (tela.height - this.getSize().height) / 2);   
        JPanel pCentro = new JPanel();
        JPanel pSul = new JPanel();
        
     // Painel de listagem das marcacoes		
		tablemodelAssuntos = new DefaultTableModel(){   
			public boolean isCellEditable(int rowIndex, int mColIndex) {   
				return false;   
			}  
		};       
		
		tablemodelAssuntos.setColumnIdentifiers(new String[] {"Assuntos CNJ - Lista"});		
		tbAssuntos = new JTable(tablemodelAssuntos);		
		tbAssuntos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbAssuntos.setModel(tablemodelAssuntos);			
        JScrollPane spListaAssuntos = new JScrollPane(tbAssuntos);   
        
        ListSelectionModel selectionModel = tbAssuntos.getSelectionModel();   
        selectionModel.addListSelectionListener(new ListSelectionListener() {   
            public void valueChanged(ListSelectionEvent event) {   
            	tbAssuntos.scrollRectToVisible(tbAssuntos.getCellRect(   
            			tbAssuntos.getSelectedRow(), 0, true));   
            } 
        }); 
        
        for (int i = 0; i < vAssuntos.size(); i++) {				
        	tablemodelAssuntos.addRow(new String[] {null});
        	tablemodelAssuntos.setValueAt((String) vAssuntos.elementAt(i), i, 0);
        }
        
        for (int i = 0; i < tbAssuntos.getRowCount(); i++) {
        	if (strAssunto.getText().length() > 0 && tbAssuntos.getValueAt(i, 0).toString().contains((CharSequence) strAssunto.getText())) {
        		tbAssuntos.setRowSelectionInterval(i, i);
        		break;
        	}        	
        }
        
        btFecha = new JButton("Fechar");
		btFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	dispose();            	
            }
		});
		
        btSalva = new JButton("Incluir Assunto");
		btSalva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {            	
            	if (tbAssuntos.getSelectedRow() >= 0) {           	
            		strAssunto.setText((String) tbAssuntos.getValueAt(tbAssuntos.getSelectedRow(), 0).toString().trim());
            		((PanelTreeTemasEQualif) pTemas).incluirNode(strNumProcesso, (String)tbAssuntos.getValueAt(tbAssuntos.getSelectedRow(), 0).toString().trim());
            	}            	
            }
		});
		
        pCentro.add(spListaAssuntos);
        
        pSul.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pSul.add(btSalva);
        pSul.add(btFecha);
                
        this.add(pCentro, BorderLayout.CENTER);
        this.add(pSul, BorderLayout.SOUTH);
	}
}
