package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.GravaLog;

public class Evento {

	private Connection con = null;
	private String evento = null;
	private String data = null;
	private String hora = null;
	
	public Evento(String strEvento, String strData, String strHora) {		
		evento = strEvento;
		data = strData;
		hora = strHora;
		
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "fidelis.model.Evento()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Evento() {				
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Danos não estabelecida!", "fidelis.model.Evento()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int isProcessoConcluido() {
		int ret = 0;
		
		String sql = "SELECT count(*) as flag FROM vc_Eventos WHERE evento = '" + evento + "' AND cod_motivo_finalizacao = 0";
		
		if (con == null) {
			return -10;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ret = rs.getInt("flag");
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Evento.isProcessoConcluido()", e);
			e.printStackTrace();			
			ret = -2;
		}		
		return ret;
	}
	
	public boolean incluir() {		
		boolean ret = false;
		
		String sql = "INSERT INTO vc_Eventos (evento, data, hora, cod_motivo_finalizacao) VALUES ('" + evento + "','" + data + "','" + hora + "', -1)";
		
		if (con == null) {
			return false;
		}
		
		try {	   
			Statement st = con.createStatement();
			st.execute(sql);
			st.close();
			ret = true;
		}
		catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Evento.incluir()", e);
			e.printStackTrace();			
			ret = false;
		}   
		return ret;
	}	
	
	public int alterar(String strMotivoFinalizacao) {		
		int ret = 0;
		int cod = 0;
		
		if (!strMotivoFinalizacao.equals("CONCLUIDO")) {
			MotivoFinalizacoes mot = new MotivoFinalizacoes();
			cod = mot.getCodMotivoFinalizacao(strMotivoFinalizacao);
		}
		String sql = "UPDATE vc_Eventos SET cod_motivo_finalizacao = " + cod + " WHERE evento = '" + evento + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Evento.alterar()", e);
			e.printStackTrace();			
			ret = -1;
		}   
		return ret;
	}
	
	
	public boolean setStatusTransmitida() {		
		String sql = "UPDATE vc_Eventos SET cod_motivo_finalizacao = 999 WHERE evento = '" + evento + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Evento.setStatusTransmitida()", e);
			e.printStackTrace();			
			return false;
		}   
	}
	
	public int excluir() {		
		int ret = 0;
		
		String sql = "DELETE FROM vc_Eventos WHERE evento = '" + evento + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Evento.excluir()", e);
			e.printStackTrace();			
			ret = -1;
		}   
		return ret;
	}
	
	public int gravarHash(String strHash) {		
		int ret = 0;
		
		String sql = "UPDATE vc_Eventos SET mediahash = '" + strHash + "' WHERE evento = '" + evento + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Evento.gravarHash()", e);
			e.printStackTrace();			
			ret = -1;
		}   
		return ret;
	}	


	@SuppressWarnings("unchecked")
	public Vector getEventosPendentes() {
		Vector ret = null;
		String sql = "SELECT evento, data, hora, cod_motivo_finalizacao FROM vc_Eventos a WHERE cod_motivo_finalizacao not in (0, 999) AND NOT EXISTS (SELECT 1 FROM vc_Eventos b WHERE a.evento = b.evento AND b.cod_motivo_finalizacao = 0) ORDER BY evento, data, hora";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[4];
				strRet[0] = rs.getString("evento");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				strRet[3] = new Integer(rs.getInt("cod_motivo_finalizacao")).toString();
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Eventos.getEventosPendentes()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}

	
	@SuppressWarnings("unchecked")
	public Vector getEventosConcluidas() {
		Vector ret = null;
		String sql = "SELECT evento, data, hora FROM vc_Eventos a WHERE EXISTS (SELECT 1 FROM vc_Eventos b WHERE a.evento = b.evento AND b.cod_motivo_finalizacao = 0)";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[3];
				strRet[0] = rs.getString("evento");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Eventos.getEventosConcluidas()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector getEventosTransmitidas() {
		Vector ret = null;
		String sql = "SELECT evento, data, hora FROM vc_Eventos WHERE cod_motivo_finalizacao = 999";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[5];
				strRet[0] = rs.getString("evento");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Eventos.getEventosTransmitidas()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	
	@SuppressWarnings("unchecked")
	public Vector getEventos() {
		Vector ret = null;
		String sql = "SELECT evento, data, hora, cod_motivo_finalizacao FROM vc_Eventos ORDER BY data, hora, evento";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[4];
				strRet[0] = rs.getString("numprocesso");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				strRet[3] = new Integer(rs.getInt("cod_motivo_finalizacao")).toString();
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Eventos.getEventos()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
}

