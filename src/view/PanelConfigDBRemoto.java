package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import controller.SessaoConfig;
import model.BancodeDados;

@SuppressWarnings("serial")
public class PanelConfigDBRemoto extends PanelConfigDB {
	
	private JButton btConectar;
	
	public PanelConfigDBRemoto(String strUsuarioLogado) {
		//usuarioLogado = strUsuarioLogado;		
		super(strUsuarioLogado);
		btDB.setVisible(false);
		cbDB.removeItemAt(0);
		
		btConectar = new JButton("Conectar");
		btConectar.setBounds(195, 240, 90, 25);
		btConectar.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SessaoConfig.conexaoRemota = null;
				@SuppressWarnings("unused")
				BancodeDados dbremoto = new BancodeDados(tfServidor.getText(), tfPorta.getText(), tfXE.getText(), tfUsuario.getText(), String.valueOf(pfSenha.getPassword()));
				if (SessaoConfig.conexaoRemota != null) {
					JOptionPane.showMessageDialog(null, "Conexão com o BD remoto estabelecida!", "Atenção", JOptionPane.INFORMATION_MESSAGE);					
				}								
			}
		});
		
		pDB.add(btConectar);		
		getDbConfig();		
	}
}
