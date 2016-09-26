package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import controller.GravaLog;

public class Configuracoes {

	private Connection con = null;
	
	public Configuracoes() {		
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
		}
	}

	public String getConfigParm(String strCampo) {
		String sql = "SELECT " + strCampo + " FROM vc_config";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String ret = null;
			
			while(rs.next()) {								
				ret = rs.getString(strCampo);
			}		
			
			rs.close();
			st.close();
			return ret;
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Configuracoes.getConfigParm(" + strCampo + ")", e);
			e.printStackTrace();
			return null;
		}		
	}
	
	public boolean setConfigParm(String strCampo, String strValor) {
		String sql = "UPDATE vc_config SET " + strCampo + " = '" + strValor + "'";
		
		if (con == null) {
			return false;
		}
		
		try {
			Statement st = con.createStatement();
			st.execute(sql);			
			st.close();
			return true;
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Configuracoes.setTipoLogin(" + strCampo + ")", e);
			e.printStackTrace();
			return false;
		}		
	}	
}
