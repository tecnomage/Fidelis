package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import controller.GravaLog;

public class Ajuda {

	private Connection con = null;
	
	public Ajuda() {		
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "fidelis.model.Ajuda()", JOptionPane.ERROR_MESSAGE);
		}
	}

	public String getTexto(String strChave) {
		String sql = "SELECT linha1, linha2, linha3, linha4, linha5 FROM vc_ajuda WHERE chave = '" + strChave + "'";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String ret = null;
			
			while(rs.next()) {								
				ret  = rs.getString(1);
				ret += rs.getString(2);
				ret += rs.getString(3);
				ret += rs.getString(4);
				ret += rs.getString(5);
			}		
			
			rs.close();
			st.close();
			return ret;
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Ajuda.getTexto(" + strChave + ")", e);
			e.printStackTrace();
			return null;
		}		
	}
	
	public boolean setTexto(String strChave, String strValor) {		
		String[] strLinhas = new String[5];
		
		for (int i=0; i<5; i++)
			strLinhas[i] = " ";
		
		if (strValor.length() < 206)
			strLinhas[0] = strValor;
		else {
			strLinhas[0] = strValor.substring(0, 205);		
		
			if (strValor.length() > 205 && strValor.length() < 410)
				strLinhas[1] = strValor.substring(205);
			else {
				strLinhas[1] = strValor.substring(205, 410);
				
				if (strValor.length() > 410 && strValor.length() < 615)
					strLinhas[2] = strValor.substring(410);
				else {			
					strLinhas[2] = strValor.substring(410, 615);
					
					if (strValor.length() > 615 && strValor.length() < 820)
					    strLinhas[3] = strValor.substring(615);
					else {
					   	strLinhas[3] = strValor.substring(615, 820);
					   	strLinhas[4] = strValor.substring(820);
					}
				}
			}
		}
		
		if (con == null) {
			return false;
		}
		
		String ret = isUpdate(strChave);
		if (ret.equals("U")) {
			return atualiza(strChave, strLinhas);
		}
		
		if (ret.equals("I")) {
			return insere(strChave, strLinhas);
		}
		
		return false;	
	}
	
	private String isUpdate(String strChave) {
		String sql = "SELECT 1 as flag FROM vc_ajuda WHERE chave = '" + strChave + "'";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String ret = "";
			
			while(rs.next()) {								
				ret = rs.getString("flag");
			}		
			
			rs.close();
			st.close();
			
			if (ret == null) {
				ret = "I";
			}
			else
				if (ret.equals("1"))
					ret = "U";
				else
					ret = "I";
			
			return ret;
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Ajuda.getTexto(" + strChave + ")", e);
			e.printStackTrace();
			return null;
		}		
		
	}
	
	private boolean insere(String strChave, String[] strValor) {
		String sql = "INSERT INTO vc_ajuda VALUES ('" + strChave + "', '" + strValor[0] + "', " +
		                                                              "'" + strValor[1] + "', " + 
		                                                              "'" + strValor[2] + "', " +
		                                                              "'" + strValor[3] + "', " + 
		                                                              "'" + strValor[4] + "')";
		
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
			log.gravaExcep("fidelis.model.Ajuda.insere(" + strChave + ")", e);
			e.printStackTrace();
			return false;
		}		
	}	
	
	private boolean atualiza(String strChave, String[] strValor) {
		String sql = "UPDATE vc_ajuda SET linha1 = '" + strValor[0] + "', " +
		                                  "linha2 = '" + strValor[1] + "', " + 
		                                  "linha3 = '" + strValor[2] + "', " +
		                                  "linha4 = '" + strValor[3] + "', " + 
		                                  "linha5 = '" + strValor[4] + "'" +
		               " WHERE chave = '" + strChave + "'";
		
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
			log.gravaExcep("fidelis.model.Ajuda.atualiza(" + strChave + ")", e);
			e.printStackTrace();
			return false;
		}		
	}	
	
}
