package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.BancodeDados;

public class FidelisVCExport {
	
	public static void main (String [] args) {
		System.out.println("VideocapExport - Extrai os dados do banco configurado no SGBD.INI");
		copiaVCAjuda();
		copiaVCTipoAudiencias();
		copiaVCMotivoFinalizacoes();
		copiaVCAudiencias();
		copiaVCTemas();
		copiaVCQualif_Dep();
		copiaVCMarcacoes();
		copiaVCPerfil();
		copiaVCUsuarios();
		copiaVCConfig();
	}	
	
	public static boolean copiaVCAjuda() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_AJUDA para o arquivo VC_Ajuda.txt ..");		
		String sql = "select chave, linha1, linha2, linha3, linha4, linha5 from vc_ajuda";		
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_Ajuda.txt"));
			
			while(rs.next()) {				
				String linha[] = new String[5];
				for (int j=0; j<5; j++) {
					linha[j] = rs.getString("linha" + (j+1));
					if (linha[j] == null || linha[j].equals(""))
						linha[j] = " ";
				}				
				
				for (int i=0; i<5; i++) {
					if (linha[i].length() > 235) {
						String excedente = linha[i].substring(235);
						String strAux = linha[i].substring(0, 235);
						linha[i] = strAux;
						if (i < 4) {
							strAux = linha[i + 1];						
							linha[i + 1] = excedente + strAux;
						}
					}
				}
				
				ret = "INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values (" +
				      "'" + rs.getString("chave") + "'," +
				      "'" + linha[0] + "'," +
				      "'" + linha[1] + "'," +
				      "'" + linha[2] + "'," +
				      "'" + linha[3] + "'," +
				      "'" + linha[4] + "')" ;
				bw.write(ret);
				bw.newLine();
			}

			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	public static boolean copiaVCTipoAudiencias() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_TIPO_AUDIENCIAS para o arquivo VC_TipoAudiencias.txt ..");		
		String sql = "select codigo, descricao from vc_tipo_audiencias";		
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_TipoAudiencias.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_TIPO_AUDIENCIAS (codigo, descricao) values (" +
			      rs.getInt("codigo") + "," +
			      "'" + rs.getString("descricao") + "')" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	public static boolean copiaVCMotivoFinalizacoes() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_MOTIVO_FINALIZACOES para o arquivo VC_MotivoFinalizacoes.txt ..");		
		String sql = "select codigo, descricao from vc_motivo_finalizacoes";	
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_MotivoFinalizacoes.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_MOTIVO_FINALIZACOES (codigo, descricao) values (" +
			      rs.getInt("codigo") + "," +
			      "'" + rs.getString("descricao") + "')" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	public static boolean copiaVCAudiencias() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_AUDIENCIAS para o arquivo VC_Audiencias.txt ..");		
		String sql = "select numprocesso, data, hora, nomejuiz, cod_tipo_audiencia, cod_motivo_finalizacao, mediahash from vc_audiencias";	
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_Audiencias.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_AUDIENCIAS (numprocesso, data, hora, nomejuiz, cod_tipo_audiencia, cod_motivo_finalizacao, mediahash) values (" +
			      "'" + rs.getString("numprocesso") + "'," +
			      "'" + rs.getString("data") + "'," +
			      "'" + rs.getString("hora") + "'," +
			      "'" + rs.getString("nomejuiz") + "'," +
			      rs.getInt("cod_tipo_audiencia") + "," +
			      rs.getInt("cod_motivo_finalizacao") + "," +
			      "'" + rs.getString("mediahash") + "')" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	public static boolean copiaVCTemas() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_TEMAS para o arquivo VC_Temas.txt ..");		
		String sql = "select numprocesso, descricao, temapai from vc_temas";	
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_Temas.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_TEMAS (numprocesso, descricao, temapai) values (" +
			      "'" + rs.getString("numprocesso") + "'," +
			      "'" + rs.getString("descricao") + "'," +
			      "'" + rs.getString("temapai") + "')" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	public static boolean copiaVCQualif_Dep() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_QUALIF_DEP para o arquivo VC_QualifDep.txt ..");		
		String sql = "select numprocesso, descricao, qualificacao from vc_qualif_dep";	
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_QualifDep.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_QUALIF_DEP (numprocesso, descricao, qualificacao) values (" +
			      "'" + rs.getString("numprocesso") + "'," +
			      "'" + rs.getString("descricao") + "'," +
			      "'" + rs.getString("qualificacao") + "')" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}

	public static boolean copiaVCMarcacoes() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_MARCACOES para o arquivo VC_Marcacoes.txt ..");		
		String sql = "select numprocesso, data, hora, tempo_marcacao, tema, subtema, qualificacao, depoente from vc_marcacoes";	
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_Marcacoes.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_MARCACOES (numprocesso, data, hora, tempo_marcacao, tema, subtema, qualificacao, depoente) values (" +
			      "'" + rs.getString("numprocesso") + "'," +
			      "'" + rs.getString("data") + "'," +
			      "'" + rs.getString("hora") + "'," +
			      "'" + rs.getString("tempo_marcacao") + "'," +
			      "'" + rs.getString("tema") + "'," +
			      "'" + rs.getString("subtema") + "'," +
			      "'" + rs.getString("qualificacao") + "'," +
			      "'" + rs.getString("depoente") + "')" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	public static boolean copiaVCPerfil() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_PERFIL para o arquivo VC_Perfil.txt ..");		
		String sql = "select codigo, perfil from vc_perfil";	
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_Perfil.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_PERFIL (codigo, perfil) values (" +
			      rs.getInt("codigo") + "," +
			      "'" + rs.getString("perfil") + "')" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	public static boolean copiaVCUsuarios() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_USUARIOS para o arquivo VC_Usuarios.txt ..");		
		String sql = "select nome, senha, cod_perfil from vc_usuarios";
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_Usuarios.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_USUARIOS (nome, senha, cod_perfil) values (" +
			      "'" + rs.getString("nome") + "'," +
			      "'" + rs.getString("senha") + "'," +
			      rs.getString("cod_perfil") + ")" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
	
	public static boolean copiaVCConfig() {
		Connection conAccess;
		BancodeDados db = new BancodeDados();		
		conAccess = db.getConexao();		
		System.out.print("Exportando tabela VC_CONFIG para o arquivo VC_Config.txt ..");		
		String sql = "select tipo_log, tipo_login, cabecalho, rodape, fundo, diretorio_servidor, endereco_servidor, porta, expurgo, horario_inicial, horario_final from vc_config";
		try {			
			Statement stAccess = conAccess.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			BufferedWriter bw = new BufferedWriter(new FileWriter("VC_config.txt"));
			
			while(rs.next()) {				
				ret = "INSERT INTO VC_CONFIG (tipo_log, tipo_login, cabecalho, rodape, fundo, diretorio_servidor, endereco_servidor, porta, expurgo, horario_inicial, horario_final) VALUES (" +
			      "'" + rs.getString("tipo_log") + "'," +
			      "'" + rs.getString("tipo_login") + "'," +
			      "'" + rs.getString("cabecalho") + "'," +
			      "'" + rs.getString("rodape") + "'," +
			      "'" + rs.getString("fundo") + "'," +
			      "'" + rs.getString("diretorio_servidor") + "'," +
			      "'" + rs.getString("endereco_servidor") + "'," +
			      "'" + rs.getString("porta") + "'," +
			      "'" + rs.getString("expurgo") + "'," +
			      "'" + rs.getString("horario_inicial") + "'," +
			      "'" + rs.getString("horario_final") + "')" ;

				bw.write(ret);
				bw.newLine();
			}
			bw.close();
			rs.close();			
			stAccess.close();
			System.out.println("[ok]");			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		return true;		
	}
}
