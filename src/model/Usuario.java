package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.GravaLog;

public class Usuario {
	private Connection con = null;

	public Usuario() {		
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "fidelis.model.Usuario()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public boolean isUsuarioUnico(String strNome) {
		String sql = "SELECT 1 as flag FROM vc_usuarios WHERE nome = '" + strNome + "'"; 
		int ret = 0;
		
		if (con == null) {
			return false;
		}
		
		try {	   
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ret = rs.getInt("flag");
			}
			rs.close();
			st.close();
			if (ret != 1) {
				return true;
			}
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Usuario.isUsuarioUnico()", e);
			e.printStackTrace();
			return false;
		}   
		return false;
	}
	
	public boolean incluir(String strNome, String strSenha, int perfil) {
		
		if (!isUsuarioUnico(strNome)) {
			return false;
		}

		String sql = "INSERT INTO vc_usuarios (nome, senha, cod_perfil) VALUES ('" + strNome + "','" + strSenha + "', " + perfil + ")";
		
		if (con == null) {
			return false;
		}
		
		try {	   
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();			
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Usuario.incluir()", e);
			e.printStackTrace();
			return false;
		}   
		return true;
	}
	
	public boolean excluir(String strNome) {

		String sql = "DELETE FROM vc_usuarios WHERE nome = '" + strNome + "'"; 
		
		if (con == null) {
			return false;
		}
		
		try {	   
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();			
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Usuario.excluir()", e);
			e.printStackTrace();
			return false;
		}   
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Vector listar() {
		Vector ret = null;
		String sql = "SELECT a.nome, a.senha, a.cod_perfil, b.perfil FROM vc_usuarios a, vc_perfil b WHERE a.cod_perfil = b.codigo";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {				
				String[] registro = new String[3];				
				registro[0] = rs.getString("nome");
				registro[1] = rs.getString("senha");
				registro[2] = String.valueOf(rs.getInt("cod_perfil")) + "-" + rs.getString("perfil");
				ret.addElement((String[])registro);
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Usuario.listar()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	
	public boolean alterar(int campo, String chave, String newValue) {

		String sql = null;

		if (campo == 1) { // nome
			sql = "UPDATE vc_usuarios SET nome = '" + newValue + "' WHERE nome = '" + chave + "'";
		}
		if (campo == 2) { // senha
			sql = "UPDATE vc_usuarios SET senha = '" + newValue + "' WHERE nome = '" + chave + "'";
		}
		if (campo == 3) { // perfil
			sql = "UPDATE vc_usuarios SET cod_perfil = " + newValue + " WHERE nome = '" + chave + "'";
		}
		
		if (con == null) {
			return false;
		}
		
		try {	   
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();
			return true;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Usuario.alterar()", e);
			e.printStackTrace();
			return false;
		}				
	}
	
	public boolean validaLogin(String strUsuario, String strSenha) {
		String sql = "SELECT 1 as ok FROM vc_usuarios WHERE nome = '" + strUsuario + "' AND senha = '" + strSenha + "'";
		int ret = 0;
		
		if (con == null) {
			return false;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {								
				ret = rs.getInt("ok");
			}
			
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Usuario.listar()", e);
			e.printStackTrace();
			return false;
		}		
		if (ret == 1) {
			return true;
		}
		else {
			return false;
		}			
	}

	public boolean alteraSenha(String strUsuario, String strSenha, String newValue) {
		String sql = null;

		sql = "UPDATE vc_usuarios SET senha = '" + newValue + "' WHERE nome = '" + strUsuario + "' AND senha = '" + strSenha + "'";
		
		if (con == null) {
			return false;
		}
		
		try {	   
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();
			return true;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Usuario.alteraSenha()", e);
			e.printStackTrace();
			return false;
		}				
	}
	
	public String getNomePerfil(String strUsuario) {
		String sql = "SELECT a.perfil FROM vc_perfil a, vc_usuarios b WHERE b.nome = '" + strUsuario + "' AND b.cod_perfil = a.codigo";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String ret = null;
			
			while(rs.next()) {								
				ret = rs.getString("perfil");
			}		
			
			rs.close();
			st.close();
			return ret;
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Usuario.listar()", e);
			e.printStackTrace();
			return null;
		}		
	}

}

