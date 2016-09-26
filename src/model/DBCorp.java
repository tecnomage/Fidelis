package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import controller.GravaLog;

public class DBCorp {
	
	private Connection corpCon;
	private String usuarioLogado;
	
	public DBCorp(String strUsuarioLogado) {
		//System.out.println("conectando no db corporativo ...");
		usuarioLogado = strUsuarioLogado;
		inicia();
	}
	
	public DBCorp(String strUsuarioLogado, String flgLocal) {
		//System.out.println("conectando no db local ...");
	    usuarioLogado = strUsuarioLogado;
	    BancodeDados db = new BancodeDados();
	    corpCon = db.getConexao();
	}
	
		
	private void inicia() {
		String [] strSGBD = getDBCorpDataXml();		
	    try {
	        String driverName = "oracle.jdbc.driver.OracleDriver";
	        Class.forName(driverName);	        
	        String serverName = strSGBD[0];
	        String portNumber = strSGBD[1]; 
	        String sid = strSGBD[2]; 
	        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
	        String username = strSGBD[3]; 
	        String password = strSGBD[4];
	        String connectStringTrt = strSGBD[5];
	        if (!connectStringTrt.equals(""))
	        	if (username.equals(""))
	        		corpCon = DriverManager.getConnection("jdbc:oracle:thin:@" + connectStringTrt);
	        	else
	        		corpCon = DriverManager.getConnection("jdbc:oracle:thin:@" + connectStringTrt, username, password);
	        else
	        	corpCon = DriverManager.getConnection(url, username, password);
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
	        //JOptionPane.showMessageDialog(null, "Não foi possível conectar com o Banco de Dados Corporativo.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
	        corpCon = null;
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);		
			corpCon = null;
		}
	}	
	
	public Connection getConexao() {
		return corpCon;
	}
	
	public void fechaConexao() {
		try {
			corpCon.close();
		} catch (SQLException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		}

	}

	public String [] getDBCorpDataXml() {
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();		
			Document doc = builder.parse(new File(System.getProperty("user.dir") + "/DBCorp/dbconf.xml"));
	
			NodeList nodes = doc.getElementsByTagName("ora");
			String retorno[] = new String[6];
			
			for (int i = 0; i < nodes.getLength(); i++) {				
				Element elemento = (Element) nodes.item(i);
				
				NodeList node = elemento.getElementsByTagName("servidor");
				Element linha = (Element) node.item(0);
				retorno[0] = linha.getTextContent();
				
				node = elemento.getElementsByTagName("porta");
				linha = (Element) node.item(0);
				retorno[1] = linha.getTextContent();
				
				node = elemento.getElementsByTagName("sid");
				linha = (Element) node.item(0);
				retorno[2] = linha.getTextContent();

				node = elemento.getElementsByTagName("user");
				linha = (Element) node.item(0);
				retorno[3] = linha.getTextContent();
	
				node = elemento.getElementsByTagName("pass");
				linha = (Element) node.item(0);
				retorno[4] = linha.getTextContent();

				node = elemento.getElementsByTagName("connectstringtrt");
				linha = (Element) node.item(0);
				retorno[5] = linha.getTextContent();

				return retorno;
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		} catch (SAXException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		} catch (IOException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		}		
		return null;
	}

	@SuppressWarnings("unchecked")
	public Vector getAudienciasFunc(String dataDe, String dataAte) {	
		if (corpCon == null) {
    		JOptionPane.showMessageDialog(null, "Não foi possível conectar com o Banco de Dados Corporativo.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
    		return null;
		}

		String strSql = "";		
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/DBCorp/audiencias_func.sql"));
			strSql = br.readLine();
			br.close();			
			strSql = strSql.replace("?1", usuarioLogado);
			strSql = strSql.replace("?2", dataDe);
			strSql = strSql.replace("?3", dataAte);
		} catch (IOException e) {			
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		}
		
		Vector vResultSet = new Vector();		
		try {
			Statement st = corpCon.createStatement();
			ResultSet rs = st.executeQuery(strSql);	
			
			while(rs.next()) {
				String[] strRecord = new String[5];
				strRecord[0] = rs.getString(1).trim();
				strRecord[1] = rs.getString(2).trim();
				strRecord[2] = rs.getString(3).trim();
				strRecord[3] = rs.getString(4).trim();	
				strRecord[4] = rs.getString(5).trim();
				vResultSet.addElement(strRecord);
			}
			rs.close();
			st.close();			
		} catch (SQLException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		}
		
		return vResultSet;		
	}
	
	@SuppressWarnings("unchecked")
	public Vector getAssuntos(String strNumProcesso) {			
		String strSql = "";		
		if (corpCon == null) {
    		JOptionPane.showMessageDialog(null, "Não foi possível conectar com o Banco de Dados Corporativo.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
    		return null;
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/DBCorp/assuntos.sql"));
			strSql = br.readLine();
			br.close();
			strSql = strSql.replace("?1", strNumProcesso.substring(0, 5));
			strSql = strSql.replace("?2", strNumProcesso.substring(6, 10));
			strSql = strSql.replace("?3", strNumProcesso.substring(11, 14));
			strSql = strSql.replace("?4", strNumProcesso.substring(15, 17));
			strSql = strSql.replace("?5", strNumProcesso.substring(18, 20));
			strSql = strSql.replace("?6", strNumProcesso.substring(21));
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		Vector vResultSet = new Vector();		
		try {
			Statement st = corpCon.createStatement();
			ResultSet rs = st.executeQuery(strSql);	
			
			while(rs.next()) {
				//vResultSet.addElement(rs.getString(1).trim());
				vResultSet.addElement(rs.getString(1));
			}
			rs.close();
			st.close();			
		} catch (SQLException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		}
		
		return vResultSet;		
	}
	
	@SuppressWarnings("unchecked")
	public Vector getAssuntosTodos() {			
		String strSql = "";		
		if (corpCon == null) {
    		JOptionPane.showMessageDialog(null, "Não foi possível conectar com o Banco de Dados Corporativo.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
    		return null;
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/DBCorp/assuntos_todos.sql"));
			strSql = br.readLine();
			br.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		Vector vResultSet = new Vector();		
		try {
			Statement st = corpCon.createStatement();
			ResultSet rs = st.executeQuery(strSql);	
			
			while(rs.next()) {
				vResultSet.addElement(rs.getString(1));
			}
			rs.close();
			st.close();			
		} catch (SQLException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		}
		
		return vResultSet;		
	}	
	
	@SuppressWarnings("unchecked")
	public Vector getJuizes() {			
		String strSql = "";		
		if (corpCon == null) {
    		JOptionPane.showMessageDialog(null, "Não foi possível conectar com o Banco de Dados Corporativo.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
    		return null;
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/DBCorp/juizes.sql"));
			strSql = br.readLine();
			br.close();			
		} catch (IOException e) {			
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		}
		
		Vector vResultSet = new Vector();		
		try {
			Statement st = corpCon.createStatement();
			ResultSet rs = st.executeQuery(strSql);	
			
			while(rs.next()) {				
				vResultSet.addElement((String) rs.getString(1).trim());
			}
			rs.close();
			st.close();			
		} catch (SQLException e) {
			e.printStackTrace();
	        GravaLog log = new GravaLog("ERRO");
	        log.gravaExcep("DBCorp", e);
		}
		
		return vResultSet;		
	}

}
