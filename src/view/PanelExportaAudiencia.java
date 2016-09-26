package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Sessao;

@SuppressWarnings("serial")
public class PanelExportaAudiencia extends JPanel {
	
	public String usuarioLogado = null;
	private JButton btExportar = null;
	private PanelDadosAudiencia pDados= null;
	
	public PanelExportaAudiencia(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
		
		JPanel borda = new JPanel();
		borda.setLayout(null);
		borda.setBorder(javax.swing.BorderFactory.createTitledBorder("Clique no botão p/ exportar a audiência"));
		borda.setBounds(8, 8, 670, 200);
				
		btExportar = new JButton("Exportar");
		btExportar.setBounds(20, 130, 120, 25);
		btExportar.setEnabled(false);
		btExportar.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
										
			}		
		});
		
		pDados = new PanelDadosAudiencia();
		pDados.setDados();
		pDados.setLocation(20, 25);
		
		borda.add(pDados);
		borda.add(btExportar);		
		
		this.setLayout(null);
		this.add(borda);		
	}
	
	public void setNumProcesso() {
		if (Sessao.numProcesso != null) {
			btExportar.setEnabled(true);
			//this.remove(pDados);
			//pDados = new PanelDadosAudiencia();			
			pDados.setDados();
			//this.add(pDados);
			//pDados.setLocation(20, 25);
		}
		return;
	}
}
