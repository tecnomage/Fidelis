package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.Ferramentas;
import controller.SessaoConfig;
import model.BancodeDados;

@SuppressWarnings("serial")
public class PanelConfigDBPlayer extends PanelConfigDB {
	
	private JButton btConectar;
	private JLabel lbVideos;
	private JTextField tfVideos;
	
	public PanelConfigDBPlayer(String strUsuarioLogado) {
		//usuarioLogado = strUsuarioLogado;		
		super(strUsuarioLogado);
		pDB.setBounds(8, 8, 300, 310);
		btDB.setVisible(false);
		cbDB.removeItemAt(0);
		
		lbVideos = new JLabel("Videos:");
		lbVideos.setBounds(15, 240, 70, 20);
		tfVideos = new JTextField();
		tfVideos.setBounds(90, 240, 195, 20);
		
		btConectar = new JButton("Conectar");
		btConectar.setBounds(195, 270, 90, 25);
		btConectar.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SessaoConfig.conexao = null;
				@SuppressWarnings("unused")
				BancodeDados dbremoto = new BancodeDados("1", tfServidor.getText(), tfPorta.getText(), tfXE.getText(), tfUsuario.getText(), String.valueOf(pfSenha.getPassword()));
				if (SessaoConfig.conexao != null) {
					SessaoConfig.videoPath = tfVideos.getText();
					JOptionPane.showMessageDialog(null, "Conexão com o BD remoto estabelecida!", "Atenção", JOptionPane.INFORMATION_MESSAGE);					
				}								
			}
		});
		
		pDB.add(btConectar);
		pDB.add(lbVideos);
		pDB.add(tfVideos);
		getDbConfig();
		Ferramentas f = new Ferramentas();		
		tfVideos.setText(f.getPropriedades("videos"));
	}
}
