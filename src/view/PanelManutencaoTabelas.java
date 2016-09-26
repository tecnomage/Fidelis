package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.GravaLog;
import controller.SessaoConfig;



@SuppressWarnings("serial")
public class PanelManutencaoTabelas extends JPanel {
		
	private String usuarioLogado;
	JComboBox cbTabs;
	
	public PanelManutencaoTabelas(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
		
		// --P1 -----------------------
		JPanel pTabs = new JPanel();
		pTabs.setLayout(null);
		pTabs.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabelas"));
		pTabs.setBounds(8, 8, 315, 90);
	
		String [] strSGBD = {""};
		cbTabs = new JComboBox(strSGBD);
		cbTabs.setBounds(15, 20, 285, 25);
		
		JButton btCarregarTabelas = new JButton("Carregar Tabelas");
		btCarregarTabelas.setBounds(15, 55, 140, 25);
		
		final JButton btCarregarDados = new JButton("Carregar Dados");
		btCarregarDados.setBounds(160, 55, 140, 25);
		btCarregarDados.setEnabled(false);
		
		pTabs.add(cbTabs);
		pTabs.add(btCarregarTabelas);
		pTabs.add(btCarregarDados);
		//---------------------------------------------------
		this.setLayout(null);
		this.add(pTabs);

		
		btCarregarTabelas.addActionListener( new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent event) {				
				if (SessaoConfig.conexaoRemota == null) {
					JOptionPane.showMessageDialog(null, "Nenhuma conexão remota foi estabelecida. Retorne à tela anterior.", "Atenção", JOptionPane.ERROR_MESSAGE );
				}
				else {
					cbTabs.removeAllItems();
					Vector vTab = carregaTabelas();
					for (int i=0; i < vTab.size(); i++)
						cbTabs.addItem((String) vTab.elementAt(i));
					
					btCarregarDados.setEnabled(true);					
				}								
			}
		});
		
		btCarregarDados.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				FrameGrid fg = new FrameGrid(usuarioLogado, (String) cbTabs.getSelectedItem());
				fg.setVisible(true);
			}
		});

	}

	@SuppressWarnings("unchecked")
	public Vector carregaTabelas() {		
		String sql = "select distinct table_name from all_tab_columns where table_name like 'VC_%'";		
		Connection con = SessaoConfig.conexaoRemota;
		Vector ret;
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ret = new Vector();
			while(rs.next()) {
				ret.addElement((String) rs.getString("table_name"));
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.PanelManutencaoTabelas.carregaTabelas()", e);
			e.printStackTrace();
			ret = null;
		}		
		return ret;	
	}

}
