package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.GravaLog;

public class Audiencia {

	private Connection con = null;
	private String numProcesso = null;
	private String data = null;
	private String hora = null;
	
	public Audiencia(String strNumProcesso, String strData, String strHora) {		
		numProcesso = strNumProcesso;
		data = strData;
		hora = strHora;
		
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "fidelis.model.Audiencia()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Audiencia() {				
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Danos não estabelecida!", "fidelis.model.Audiencia()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int isProcessoConcluido() {
		int ret = 0;
		
		String sql = "SELECT count(*) as flag FROM vc_audiencias WHERE numprocesso = '" + numProcesso + "' AND cod_motivo_finalizacao in (0, 998, 999)";
		
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
			log.gravaExcep("fidelis.model.Audiencia.isProcessoConcluido()", e);
			e.printStackTrace();			
			ret = -2;
		}		
		return ret;
	}
	
	public int isProcInterrompidoEmD0() {
		int ret = 0;
		
		String sql = "SELECT 1 as flag FROM vc_audiencias WHERE numprocesso = '" + numProcesso + "' AND data = '" + data + "' AND cod_motivo_finalizacao BETWEEN 0 AND 990 ";
		
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
			log.gravaExcep("fidelis.model.Audiencia.isProcInterrompidoEmD0()", e);
			e.printStackTrace();			
			ret = -2;
		}		
		return ret;
	}	
	
	public int incluir(String strTipoAudiencia, String strJuiz) {		
		int ret = 0;
		
		TipoAudiencias tipo = new TipoAudiencias();
		int codTipoAudiencia = tipo.getCodTipoAudiencia(strTipoAudiencia);
		String sql = "INSERT INTO vc_audiencias (numprocesso, data, hora, nomejuiz, cod_tipo_audiencia, cod_motivo_finalizacao) VALUES ('" + numProcesso + "','" + data + "','" + hora + "','" + strJuiz + "'," + codTipoAudiencia + ", -1)";
		
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
			log.gravaExcep("fidelis.model.Audiencia.incluir()", e);
			e.printStackTrace();			
			ret = -1;
		}   
		return ret;
	}	
	
	public int alterar(String strMotivoFinalizacao) {		
		int ret = 0;
		int cod = 0;
		
		if (strMotivoFinalizacao.equals("CONCLUIDO"))
			cod = 0;
		else
			if (strMotivoFinalizacao.equals("VINCULADO"))
				cod = 998;
			else
				if (strMotivoFinalizacao.equals("-2"))
					cod = -2;
				else
					if (strMotivoFinalizacao.equals("-3"))
						cod = -3;
					else
						if (strMotivoFinalizacao.equals("1"))
							cod = 1;
						else
						{
							MotivoFinalizacoes mot = new MotivoFinalizacoes();
							cod = mot.getCodMotivoFinalizacao(strMotivoFinalizacao);
						}
		
		String sql = "UPDATE vc_audiencias SET cod_motivo_finalizacao = " + cod + " WHERE numprocesso = '" + numProcesso + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Audiencia.alterar()", e);
			e.printStackTrace();			
			ret = -1;
		}   
		return ret;
	}
	
	
	public boolean setStatusTransmitida() {		
		String sql = "UPDATE vc_audiencias SET cod_motivo_finalizacao = 999 WHERE numprocesso = '" + numProcesso + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Audiencia.setStatusTransmitida()", e);
			e.printStackTrace();			
			return false;
		}   
	}
	
	public int excluir() {		
		int ret = 0;
		
		String sql = "DELETE FROM vc_audiencias WHERE numprocesso = '" + numProcesso + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Audiencia.excluir()", e);
			e.printStackTrace();			
			ret = -1;
		}   
		return ret;
	}
	
	public int gravarHash(String strHash) {		
		int ret = 0;
		
		String sql = "UPDATE vc_audiencias SET mediahash = '" + strHash + "' WHERE numprocesso = '" + numProcesso + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Audiencia.gravarHash()", e);
			e.printStackTrace();			
			ret = -1;
		}   
		return ret;
	}	


	@SuppressWarnings("unchecked")
	public Vector getAudienciasPendentes() {
		Vector ret = null;
		String sql = "SELECT numprocesso, data, hora, cod_tipo_audiencia, nomejuiz, cod_motivo_finalizacao FROM vc_audiencias a WHERE cod_motivo_finalizacao not in (0, 998, 999) AND NOT EXISTS (SELECT 1 FROM vc_audiencias b WHERE a.numprocesso = b.numprocesso AND b.cod_motivo_finalizacao = 0) ORDER BY numprocesso, data, hora";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[6];
				strRet[0] = rs.getString("numprocesso");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				strRet[3] = new Integer(rs.getInt("cod_tipo_audiencia")).toString();
				strRet[4] = rs.getString("nomejuiz");
				strRet[5] = new Integer(rs.getInt("cod_motivo_finalizacao")).toString();
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Audiencias.getAudienciasPendentes()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}

	
	@SuppressWarnings("unchecked")
	public Vector getAudienciasConcluidas() {
		// Recupera audiencias para transmissao .. mundei query pra pegar as VINCULADAS ao inves das CONCLUIDAS.
		Vector ret = null;
		String sql = "SELECT numprocesso, data, hora, cod_tipo_audiencia, nomejuiz FROM vc_audiencias a WHERE a.cod_motivo_finalizacao <> 999 AND EXISTS (SELECT 1 FROM vc_audiencias b WHERE a.numprocesso = b.numprocesso AND b.cod_motivo_finalizacao in (0, 998))";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[5];
				strRet[0] = rs.getString("numprocesso");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				strRet[3] = new Integer(rs.getInt("cod_tipo_audiencia")).toString();
				strRet[4] = rs.getString("nomejuiz");
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Audiencias.getAudienciasConcluidas()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector getAudienciasTransmitidas() {
		Vector ret = null;
		String sql = "SELECT numprocesso, data, hora FROM vc_audiencias WHERE cod_motivo_finalizacao = 999";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[5];
				strRet[0] = rs.getString("numprocesso");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Audiencias.getAudienciasTransmitidas()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector getAudienciasComErro() {
		Vector ret = null;
		String sql = "SELECT numprocesso, data, hora FROM vc_audiencias WHERE cod_motivo_finalizacao = -1 AND mediahash is null ";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[5];
				strRet[0] = rs.getString("numprocesso");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Audiencias.getAudienciasComErro()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public Vector getAudiencias() {
		Vector ret = null;
		String sql = "SELECT numprocesso, data, hora, cod_tipo_audiencia, nomejuiz, cod_motivo_finalizacao FROM vc_audiencias ORDER BY numprocesso, data, hora";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			while(rs.next()) {
				String [] strRet = new String[6];
				strRet[0] = rs.getString("numprocesso");
				strRet[1] = rs.getString("data");
				strRet[2] = rs.getString("hora");
				strRet[3] = new Integer(rs.getInt("cod_tipo_audiencia")).toString();
				strRet[4] = rs.getString("nomejuiz");
				strRet[5] = new Integer(rs.getInt("cod_motivo_finalizacao")).toString();
				ret.addElement((String[]) strRet);				
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Audiencias.getAudiencias()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}

	public String [] getAudiencia() {
		String [] strRet = null;
		String sql = "SELECT b.descricao, a.nomejuiz, a.mediahash FROM vc_audiencias a, vc_tipo_audiencias b WHERE a.numprocesso = '" + numProcesso + "' AND a.data = '" + data + "' AND a.hora = '" + hora + "' AND a.cod_tipo_audiencia = b.codigo "; 
	
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				strRet = new String[3];
				strRet[0] = rs.getString(1);
				strRet[1] = rs.getString(2);		
				strRet[2] = rs.getString(3);
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Audiencias.getAudiencia()", e);
			e.printStackTrace();
			return null;
		}		
		return strRet;
	}
	
	public String getHashProcesso() {
		String ret = null;
		
		String sql = "SELECT mediahash FROM vc_audiencias  WHERE numprocesso = '" + numProcesso + "' AND data = '" + data + "' AND hora = '" + hora + "'"; 
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ret = rs.getString("mediahash");
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Audiencia.isProcessoConcluido()", e);
			e.printStackTrace();			
			ret = null;
		}		
		return ret;
	}

}

