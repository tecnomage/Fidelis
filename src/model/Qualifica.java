package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.GravaLog;

public class Qualifica {
	
	private Connection con = null;
	private String numProcesso = null;
	
	public Qualifica(String strNumProcesso) {
		
		numProcesso = strNumProcesso;
		
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "Qualifica: Atenção", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int getDbUltimoCodigo() {
		int ret = 0;
		String sql = "SELECT count(*) as ultimo from vc_qualif_dep WHERE numprocesso = '" + numProcesso + "'";
		
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
			log.gravaExcep("fidelis.model.Qualifica.getDbUltimoCodigo()", e);
			e.printStackTrace();
			ret = -2;
		}
		
		return (ret + 1);		
	}	
	
	public int incluir(String strDesc, String strQualificacao) {		
		int ret = 0;
		
		String sql = "INSERT INTO vc_qualif_dep (numprocesso, descricao, qualificacao) VALUES ('" + numProcesso + "','" + strDesc + "','" + strQualificacao + "')";
		
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
			log.gravaExcep("fidelis.model.Qualifica.incluir()", e);
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
			String sql = "UPDATE vc_qualif_dep SET descricao = '" + newValue + "' WHERE descricao = '" + oldValue + "' AND numprocesso = '" + numProcesso + "'";
			st.execute(sql);
			       sql = "UPDATE vc_qualif_dep SET qualificacao = '" + newValue + "' WHERE qualificacao = '" + oldValue + "' AND numprocesso = '" + numProcesso + "'";
			st.execute(sql);
			st.close();
			ret = 1;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Qualifica.alterar()", e);
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
			String sql = "DELETE FROM vc_qualif_dep WHERE descricao = '" + strValue + "' AND numprocesso = '" + numProcesso + "'";
			Statement st = con.createStatement();
			st.execute(sql);
			       sql = "DELETE FROM vc_qualif_dep WHERE qualificacao = '" + strValue + "' AND numprocesso = '" + numProcesso + "'";
			st.execute(sql);
			st.close();
			ret = 1;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Qualifica.excluir()", e);
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
			String sql = "DELETE FROM vc_qualif_dep WHERE numprocesso = '" + numProcesso + "'";
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();
			ret = 1;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Qualifica.excluirPorProcesso()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;				
	}
	
	
	@SuppressWarnings("unchecked")
	public Vector carregarQualifica() {
		Vector ret = null;
		String sql = "SELECT descricao FROM vc_qualif_dep WHERE numprocesso = '" + numProcesso + "' AND qualificacao = 'null'";
		
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
			log.gravaExcep("fidelis.model.Qualifica.carregarQualifica()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector carregarDepoentes(String strQualifica) {
		Vector ret = null;
		String sql = "SELECT descricao FROM vc_qualif_dep WHERE numprocesso = '" + numProcesso + "' AND qualificacao = '" + strQualifica + "'";
		
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
			log.gravaExcep("fidelis.model.Qualifica.carregarDepoentes()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}	

}
