package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*import pt.voiceinteraction.AudimusMediaServerService;
import pt.voiceinteraction.AudimusMediaServerServicePortType;
import pt.voiceinteraction.AudimusServerResultObj;
import pt.voiceinteraction.AudimusServerStatusRequestObj;
import pt.voiceinteraction.AudimusServerSubmitRequestObj;
import pt.voiceinteraction.Authentication;
*/
public class FidelisVCTranscricao {

	public static void main(String[] args) {
		
		File dirVideos = new File(getPropriedades("dirvideos"));
		File [] lista = dirVideos.listFiles();
		for (int i=0; i<lista.length; i++) {
			if (lista[i].isFile()) {
				submete(lista[i].getName(), lista[i].getName());
				//submete("test", lista[i].getName());
				boolean done = false;
				int minutos = 0;
				while (!done) {
					try { Thread.sleep(60000); } catch (Exception e) { }
					minutos++;
					System.out.print(minutos + " min :: ");
					String strStatus = getStatus(lista[i].getName());
					//String strStatus = getStatus("test");
					if (strStatus.equals("ERROR"))
						done = true;
					
					if (strStatus.equals("DONE")) {
						getResult(lista[i].getName(), getPropriedades("dirtransc"));
						//getResult("test", getPropriedades("dirtransc"));
						lista[i].delete();
						done = true;
					}
				}
			}
		}
		System.out.println("Execucao finalizada. Arquivos restantes do diretorio de origem (videos) não puderam ser transcritos.");
	}	
	
	public static void getResult(String jobID, String pathSaida) {
		
/*		AudimusMediaServerService a = new AudimusMediaServerService();	
		AudimusMediaServerServicePortType proxy = a.getAudimusMediaServerPort();
		
		Authentication ath = new Authentication();
		ath.setUserID(getPropriedades("user"));
		ath.setPassword(getPropriedades("pwd"));
		
		AudimusServerResultObj getRst = new AudimusServerResultObj();
		getRst.setAuthentication(ath);
		getRst.setJobID(jobID);
		getRst.setResultType(getPropriedades("formatosaida"));
		String strConteudo = proxy.audimusServerGetResult(getRst);
		String strJobId = jobID.substring(0, jobID.toLowerCase().lastIndexOf(".asf"));
		
		String fileName = pathSaida + "/" + strJobId + "." + getPropriedades("formatosaida");
		try {
			BufferedWriter bw = new BufferedWriter (new FileWriter(fileName));
			bw.write(strConteudo);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Resultado da Transcricao do Job " + jobID + " foi gravado em: " + fileName);
		
		return;*/
	}
	
	public static String getStatus(String jobID) {
		/*
		AudimusMediaServerService a = new AudimusMediaServerService();	
		AudimusMediaServerServicePortType proxy = a.getAudimusMediaServerPort();
		
		Authentication ath = new Authentication();
		ath.setUserID(getPropriedades("user"));
		ath.setPassword(getPropriedades("pwd"));
		
		AudimusServerStatusRequestObj status = new AudimusServerStatusRequestObj ();
		status.setAuthentication(ath);
		status.setJobID(jobID);
		
		String strStatus = proxy.audimusServerGetStatus(status);
		System.out.println("Status do Job " + jobID + ": " + strStatus);		
		
		return (strStatus);*/
		return"";
	}
	
	public static String submete(String jobID, String fileName) {
		
	/*	System.out.print("Submetendo Job " + jobID + " .. ");		
		AudimusMediaServerService a = new AudimusMediaServerService();	
		AudimusMediaServerServicePortType proxy = a.getAudimusMediaServerPort();
		
		Authentication ath = new Authentication();
		ath.setUserID(getPropriedades("user"));
		ath.setPassword(getPropriedades("pwd"));
		
		AudimusServerSubmitRequestObj objx = new AudimusServerSubmitRequestObj();
		objx.setAuthentication(ath);
		objx.setJobID(jobID);
		objx.setDescription("FidelisVCTranscricao" + jobID);
		objx.setLanguage(getPropriedades("language"));
		objx.setUserModel(getPropriedades("usermodel"));
		objx.setLanguageModel(getPropriedades("languagemodel"));
		objx.setResultType(getPropriedades("resulttype"));
		objx.setFileType(getPropriedades("filetype"));
		objx.setInFile(getPropriedades("url") + fileName);
		objx.setEmail(getPropriedades("email"));
		objx.setEmailSubject(getPropriedades("emailsubj"));
		objx.setPunctuate(true);
		objx.setCapitalize(true);
		String ret = proxy.audimusServerSubmit(objx);
		System.out.println(ret);
		return (ret);*/
		return "";
	}
	
	public static String getPropriedades(String strChave) {
		
		String retorno = null;
		
		try {			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new File(System.getProperty("user.dir") + "/FidelisVCTranscricao.xml"));

			NodeList nodes = doc.getElementsByTagName("propriedades");
			
			for (int i = 0; i < nodes.getLength(); i++) {				
				Element elemento = (Element) nodes.item(i);
				
				NodeList node = elemento.getElementsByTagName(strChave);
				Element linha = (Element) node.item(0);
				retorno = linha.getTextContent();				
			}
			
			return retorno;
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
