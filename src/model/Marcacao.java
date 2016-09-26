package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.GravaLog;

public class Marcacao {
	private Connection con = null;
	private String numProcesso = null;
	private String data = null;
	private String hora = null;
	
	public Marcacao(String strNumProcesso, String strData, String strHora) {		
		numProcesso = strNumProcesso;
		data = strData;
		hora = strHora;
		
		BancodeDados db = new BancodeDados();
		con = db.getConexao();
		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Conexão com o Banco de Dados não estabelecida!", "fidelis.model.Marcacao()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public int incluir(String strTempo, String strTema, String strSubTema, String strQualificacao, String strDepoente) {		
		int ret = 0;		
		String sql = "INSERT INTO vc_marcacoes (numprocesso, data, hora, tempo_marcacao, tema, subtema, qualificacao, depoente) VALUES ('" + numProcesso + "','" + data + "','" + hora + "','" + strTempo + "','" + strTema + "','" + strSubTema + "','" + strQualificacao + "','" + strDepoente + "')";
		
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
			log.gravaExcep("fidelis.model.Marcacao.incluir()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;
	}
	
	
	public int excluir() {		
		int ret = 0;		
		String sql = "DELETE FROM vc_marcacoes WHERE numprocesso = '" + numProcesso + "' AND data = '" + data + "' AND hora = '" + hora + "'";
		
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
			log.gravaExcep("fidelis.model.Marcacao.excluir()", e);
			e.printStackTrace();
			ret = -1;
		}   
		return ret;
	}

	
	@SuppressWarnings("unchecked")
	public Vector carregar() {
		Vector ret = null;
		String sql = "SELECT tempo_marcacao, tema, subtema, qualificacao, depoente FROM vc_marcacoes WHERE numprocesso = '" + numProcesso + "' AND data = '" + data + "' AND hora = '" + hora + "' ORDER BY tempo_marcacao";
		
		if (con == null) {
			return null;
		}
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ret = new Vector();
			
			while(rs.next()) {
				String [] strRecord = {null, null, null, null, null};
				strRecord[0] = (String) rs.getString("tempo_marcacao");
				strRecord[1] = (String) rs.getString("tema");
				strRecord[2] = (String) rs.getString("subtema");
				strRecord[3] = (String) rs.getString("qualificacao");
				strRecord[4] = (String) rs.getString("depoente");
				ret.addElement(strRecord);
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.model.Marcacao.carregar()", e);
			e.printStackTrace();
			return null;
		}		
		return ret;
	}
	
}
