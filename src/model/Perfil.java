package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.GravaLog;


public class Perfil {
	
	private int codigo = 0;
	private String descricao = null; 
	private Connection con = null;
	
	public Perfil() {		  
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "Perfil: Atenção", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int getDbUltimoCodigo() {
		int ret = 0;
		String sql = "SELECT max(codigo) as ultimo from vc_perfil";
		
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
			log.gravaExcep("fidelis.model.Perfil.getDbUltimoCodigo()", e);
			e.printStackTrace();	
			ret = -2;
		}
		
		return (ret + 1);		
	}	
	
	public int incluir(String strDesc) {		
		int ret = 0;
		codigo = getDbUltimoCodigo();
		descricao = strDesc;
		String sql = "INSERT INTO vc_perfil (codigo, perfil) VALUES (" + codigo + "," + "'" + descricao + "')";
		
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
			log.gravaExcep("fidelis.model.perfil.incluir()", e);
			e.printStackTrace();	
			ret = -1;
		}   
		return ret;
	}
	
	public int alterar(String oldValue, String newValue) {
		int ret = 0;
		descricao = newValue;
		String sql = "UPDATE vc_perfil SET perfil = '" + newValue + "' WHERE perfil = '" + oldValue + "'";
		
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
			log.gravaExcep("fidelis.model.perfil.alterar()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;				
	}
	
	public int excluir(String newValue) {
		int ret = 0;
		descricao = newValue;
		
		if (temDependencias(descricao) > 0) {
			JOptionPane.showMessageDialog(null, "Perfil possui dependênicas no Banco de Dados", "Classe Perfil()", JOptionPane.ERROR_MESSAGE);
			return -2;
		}
				
		String sql = "DELETE FROM vc_perfil WHERE perfil = '" + newValue + "'";
		
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
			log.gravaExcep("fidelis.model.Perfil.excluir()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;				
	}
	
	public int temDependencias(String strDesc) {
		int ret = 0;
		descricao = strDesc;
		String sql = "SELECT count(*) as total FROM vc_usuarios a, vc_perfil b WHERE a.cod_perfil = b.codigo AND b.perfil = '"+ strDesc + "'";
		
		if (con == null) {
			return -10;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ret = rs.getInt("total");
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Perfil.temDependencias()", e);
			e.printStackTrace();
			ret = -2;
		}		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector lista() {
		Vector ret = null;
		String sql = "SELECT perfil FROM vc_perfil";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				ret.addElement((String)rs.getString("perfil"));
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Perfil.lista()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}

	
	@SuppressWarnings("unchecked")
	public Vector listaAll() {
		Vector ret = null;
		String sql = "SELECT codigo, perfil FROM vc_perfil";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] registro = new String[2];
				registro[0] = String.valueOf(rs.getInt("codigo"));
				registro[1] = rs.getString("perfil");
				ret.addElement(registro);
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Perfil.listaAll()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	
	public int getCodPerfil(String strDesc) {
		int ret = 0;
		descricao = strDesc;
		String sql = "SELECT codigo FROM vc_perfil WHERE perfil = '"+ strDesc + "'";
		
		if (con == null) {
			return -10;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ret = rs.getInt("codigo");
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Perfil.getCodTipoAudiencia()", e);
			e.printStackTrace();
			ret = -2;
		}		
		return ret;
	}
	

	public String getDescPerfil(int cod) {
		String ret = null;
		String sql = "SELECT perfil FROM vc_perfil WHERE codigo = " + cod;
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ret = rs.getString("perfil");
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Perfil.getDescTipoAudiencia()", e);
			e.printStackTrace();
			ret = null;
		}		
		return ret;
	}
}
