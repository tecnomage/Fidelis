package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.GravaLog;
import model.BancodeDados;


public class TipoAudiencias {
	
	private int codigo = 0;
	private String descricao = null;
	private Connection con = null;
	
	public TipoAudiencias() {		  
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "fidelis.model.TipoAudiencias()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int getDbUltimoCodigo() {
		int ret = 0;
		String sql = "SELECT max(codigo) as ultimo from vc_tipo_audiencias";
		
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
			log.gravaExcep("fidelis.model.TipoAudiencias.getDbUltimoCodigo()", e);
			e.printStackTrace();	
			ret = -2;
		}
		
		return (ret + 1);		
	}	
	
	public int incluir(String strDesc) {		
		int ret = 0;
		codigo = getDbUltimoCodigo();
		descricao = strDesc;
		String sql = "INSERT INTO vc_tipo_audiencias (codigo, descricao) VALUES (" + codigo + "," + "'" + descricao + "')";
		
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
			log.gravaExcep("fidelis.model.TipoAudiencias.incluir()", e);
			e.printStackTrace();	
			ret = -1;
		}   
		return ret;
	}
	
	public int alterar(String oldValue, String newValue) {
		int ret = 0;
		descricao = newValue;
		String sql = "UPDATE vc_tipo_audiencias SET descricao = '" + newValue + "' WHERE descricao = '" + oldValue + "'";
		
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
			log.gravaExcep("fidelis.model.TipoAudiencias.alterar()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;				
	}
	
	public int excluir(String newValue) {
		int ret = 0;
		descricao = newValue;
		
		if (temDependencias(descricao) > 0) {
			JOptionPane.showMessageDialog(null, "Tipo de Audiência possui dependênicas no Banco de Dados", "Classe TipoAudiencias()", JOptionPane.ERROR_MESSAGE);
			return -2;
		}
				
		String sql = "DELETE FROM vc_tipo_audiencias WHERE descricao = '" + newValue + "'";
		
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
			log.gravaExcep("fidelis.model.TipoAudiencias.excluir()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;				
	}
	
	public int temDependencias(String strDesc) {
		int ret = 0;
		descricao = strDesc;
		String sql = "SELECT count(*) as total FROM vc_audiencias a, vc_tipo_audiencias b WHERE a.cod_tipo_audiencia = b.codigo AND b.descricao = '"+ strDesc + "'";
		
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
			log.gravaExcep("fidelis.model.TipoAudiencias.temDependencias()", e);
			e.printStackTrace();
			ret = -2;
		}		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector lista() {
		Vector ret = null;
		String sql = "SELECT descricao FROM vc_tipo_audiencias";
		
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
			log.gravaExcep("fidelis.model.TipoAudiencias.lista()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	public int getCodTipoAudiencia(String strDesc) {
		int ret = 0;
		descricao = strDesc;
		String sql = "SELECT codigo FROM vc_tipo_audiencias WHERE descricao = '"+ strDesc + "'";
		
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
			log.gravaExcep("fidelis.model.TipoAudiencias.getCodTipoAudiencia()", e);
			e.printStackTrace();
			ret = -2;
		}		
		return ret;
	}
	

	public String getDescTipoAudiencia(int cod) {
		String ret = null;
		String sql = "SELECT descricao FROM vc_tipo_audiencias WHERE codigo = " + cod;
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ret = rs.getString("descricao");
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.TipoAudiencias.getDescTipoAudiencia()", e);
			e.printStackTrace();
			ret = null;
		}		
		return ret;
	}
}
