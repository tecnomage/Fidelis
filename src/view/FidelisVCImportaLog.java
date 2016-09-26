package view;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import controller.GravaLog;
import model.BancodeDados;

public class FidelisVCImportaLog {
	
	public static void main(String[] args) {		
		if (args.length != 1) {
			System.out.println("Sintaxe: FidelisVCImportaLog <diretorio de logs>");
			System.exit(0);
		}
		
		while (true) {		
			File dir = new File(args[0]);
			File [] fileList = dir.listFiles();
			
			for (int i=0; i<fileList.length; i++) {
				if (fileList[i].isFile() && fileList[i].getName().lastIndexOf(".xml") > -1) {
					if (importaLog(fileList[i])) {
						// Se importou com sucesso, deleta o arquivo.
						fileList[i].delete();
					}
				}
			}
			try { Thread.sleep(60000); } catch(Exception e) { }
		}
	}
	
	public static boolean importaLog(File file) {
		System.out.println("FidelisVCImportaLog.importaLog(): Importanto arquivo de log " + file.getPath());
		
		String [] data;
		String [] classe;
		String [] usuario;
		String [] mensagem;
		String vara;
		
		try {
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);

			NodeList nodes = doc.getElementsByTagName("ocorrencia");
			data = new String[nodes.getLength()];
			classe = new String[nodes.getLength()];
			usuario = new String[nodes.getLength()];
			mensagem = new String[nodes.getLength()];
			
			vara = file.getName().substring(10, 17);
			
			for (int i = 0; i < nodes.getLength(); i++) {				
				Element elemento = (Element) nodes.item(i);
				
				NodeList node = elemento.getElementsByTagName("horario");
				Element linha = (Element) node.item(0);
				data[i] = linha.getTextContent();
	
				node = elemento.getElementsByTagName("classe");
				linha = (Element) node.item(0);
				classe[i] = decrip(linha.getTextContent());
				
				node = elemento.getElementsByTagName("usuario");
				linha = (Element) node.item(0);
				usuario[i] = decrip(linha.getTextContent());
	
				node = elemento.getElementsByTagName("mensagem");
				linha = (Element) node.item(0);
				mensagem[i] = decrip(linha.getTextContent());
			}
			
			BancodeDados db = new BancodeDados();
			Connection con = db.getConexao();
			
			for (int i=0; i<data.length; i++) {
				String sql = "INSERT INTO vc_log VALUES ('" + data[i] + "','" + classe[i] + "','" + usuario[i] + "','" + mensagem[i] + "','','" + vara + "')";
 
				try {	   
					Statement st = con.createStatement();
					st.execute(sql);
					st.close();
				}
				catch (SQLException e) {
					GravaLog log = new GravaLog("ERRO");
					log.gravaExcep("FidelisVCImportaLog.importaLog()", e);
					e.printStackTrace();
					return false;
				} 
			}			
			
			System.out.println("concluido!");
			return true;
		}
		catch (Exception e) {
			GravaLog logErro = new GravaLog("ERRO");
			logErro.gravaExcep("FidelisVCImportaLog.importaLog()", e);
			e.printStackTrace();
			return false;
		}
	}
	
	private static String decrip(String s) {
		String aux = "";
		for (int i=0; i<s.length(); i+=3) {
			char c = (char) (Integer.parseInt(s.substring(i, i+3)) - 100);
			aux += c;
		}		
		return(aux);
	}

}
