package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.GravaLog;
import model.BancodeDados;

public class Temas {
	
	private Connection con = null;
	private String numProcesso = null;
	
	public Temas(String strNumProcesso) {
		
		numProcesso = strNumProcesso;
		
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "fidelis.model.Temas()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int getDbUltimoCodigo() {
		int ret = 0;
		String sql = "SELECT count(*) as ultimo from vc_temas WHERE numprocesso = '" + numProcesso + "'";
		
		if (con == null) {
			return -10;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ret = rs.getInt("ultimo");
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Temas.getDbUltimoCodigo()", e);
			e.printStackTrace();
			ret = -2;
		}
		
		return (ret + 1);		
	}	
	
	public int incluir(String strDesc, String strTemaPai) {		
		int ret = 0;
		
		String sql = "INSERT INTO vc_temas (numprocesso, descricao, temapai) VALUES ('" + numProcesso + "','" + strDesc + "','" + strTemaPai + "')";
		
		if (con == null) {
			return -10;
		}
		
		try {	   
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();
			ret = 1;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Temas.incluir()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;
	}
	
	public int alterar(String oldValue, String newValue) {
		int ret = 0;
		
		if (con == null) {
			return -10;
		}
		
		try {	   
			Statement st = con.createStatement();
			String sql = "UPDATE vc_temas SET descricao = '" + newValue + "' WHERE descricao = '" + oldValue + "' AND numprocesso = '" + numProcesso + "'";
			st.execute(sql);
			       sql = "UPDATE vc_temas SET temapai = '" + newValue + "' WHERE temapai = '" + oldValue + "' AND numprocesso = '" + numProcesso +"'";
			st.execute(sql);
			st.close();
			ret = 1;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Temas.alterar()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;				
	}
	
	public int excluir(String strValue) {
		int ret = 0;
		
		if (con == null) {
			return -10;
		}
		
		try {	   
			String sql = "DELETE FROM vc_temas WHERE descricao = '" + strValue + "' AND numprocesso = '" + numProcesso + "'";
			Statement st = con.createStatement();
			st.execute(sql);
			       sql = "DELETE FROM vc_temas WHERE temapai = '" + strValue + "' AND numprocesso = '" + numProcesso + "'";
			st.execute(sql);
			st.close();
			ret = 1;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Temas.excluir()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;				
	}
	
	public int excluirPorProcesso() {
		int ret = 0;
		
		if (con == null) {
			return -10;
		}
		
		try {	   
			String sql = "DELETE FROM vc_temas WHERE numprocesso = '" + numProcesso + "'";
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();
			ret = 1;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Temas.excluirPorProcesso()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;				
	}
	
	@SuppressWarnings("unchecked")
	public Vector carregarTemas() {
		Vector ret = null;
		String sql = "SELECT descricao FROM vc_temas WHERE numprocesso = '" + numProcesso + "' AND temapai = 'null' ORDER BY 1";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				ret.addElement((String)rs.getString("descricao"));
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Temas.carregarTemas()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector carregarSubTemas(String strTemaPai) {
		Vector ret = null;
		String sql = "SELECT descricao FROM vc_temas WHERE numprocesso = '" + numProcesso + "' AND temapai = '" + strTemaPai + "' ORDER BY 1";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				ret.addElement((String)rs.getString("descricao"));
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Temas.carregarSubTemas()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}	

}
