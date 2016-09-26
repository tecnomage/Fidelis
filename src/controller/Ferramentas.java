package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.annotation.processing.Processor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Ferramentas {
	
	public Ferramentas() {}
	
	public String getArquivoAssinejus(String numProcesso, String data, String hora) {
		File dir = new File(System.getProperty("user.dir") + "/videos");		
		String[] lista = dir.list(); 
		String fileRetorno = null;
		for (int i = 0; i < lista.length; i++) {
			if (lista[i].contains(Sessao.fileName(numProcesso, data, hora)) && lista[i].contains("p7s")) {
				fileRetorno = lista[i];				
			}
		}
		return fileRetorno;
	}
	
	public boolean copiaArquivo(String fileOrigem, String fileDestino) {		
		FileInputStream Source;
		FileOutputStream Dest;
		try {
			Source = new FileInputStream(fileOrigem);
			Dest  = new FileOutputStream(fileDestino); 

			int BytesRead;   
			byte[] bBuffer = new byte[8096];   
	  
			while (true) {   
				BytesRead = Source.read(bBuffer);   
				if (BytesRead == -1){   
					break;   
				}   
				Dest.write(bBuffer, 0, BytesRead);   
			}   
			
			Source.close();   
			Dest.close();
			
			return true;
			
		} catch (FileNotFoundException e) {		
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.Ferramentas().copiaArquivo()", e);
			return false;
		} catch (IOException ioe) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.Ferramentas().copiaArquivo()", ioe);
			return false;
		}			
	}
	
	public String getPropriedades(String strChave) {
		
		String retorno = null;
		
		try {			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new File(System.getProperty("user.dir") + "/fidelis.propriedades.xml"));

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
			GravaLog logErro = new GravaLog("ERRO");
			logErro.gravaExcep("fidelis.controller.Ferramentas.getPropriedades()", e);
			e.printStackTrace();
			return "";
		}
	}

	public String formataHora(int iTime) {		
		String ret = null;
		int min = 0, seg = 0, hr = 0, resto = 0;
		
		hr    = iTime / 3600;
		resto = iTime % 3600;
		
		min   = resto / 60;
		resto = resto % 60;
		
		seg   = resto;
		
		if (hr > 9) {
			ret = String.valueOf(hr);
		} else {
			ret = "0" + String.valueOf(hr);
		}
		ret += ":";
		if (min > 9) {
			ret = ret.concat(String.valueOf(min));
		} else {
			ret = ret.concat("0" + String.valueOf(min));
		}
		ret += ":";
		if (seg > 9) {
			ret = ret.concat(String.valueOf(seg));
		} else {
			ret = ret.concat("0" + String.valueOf(seg));
		}

		return ret;		
	}
	
	public int getSegundos(String horaFormatada) {
		int totalSegundos = 0;
		int hr = 0, min = 0, seg = 0;
		
		hr = new Integer(horaFormatada.substring(0, 2)).intValue();
		min = new Integer(horaFormatada.substring(3, 5)).intValue();
		seg = new Integer(horaFormatada.substring(6)).intValue();
		
		totalSegundos = (hr * 60 * 60) + (min * 60) + seg;		
		
		return totalSegundos;
	}
	
    @SuppressWarnings("unchecked")
	public Vector detectaCameras() {    	
        Vector lista = CaptureDeviceManager.getDeviceList(null);
        Vector devInfoRet = new Vector();
 
        if (lista != null) {
             for (int i=0; i<lista.size(); i++ ) {            	
                if (((CaptureDeviceInfo) lista.elementAt(i)).getName().startsWith("vfw:")) {                	
                	devInfoRet.addElement(((CaptureDeviceInfo) lista.elementAt(i)).getName());
                }
            } 
        }
        return (devInfoRet);        
    }
    
    
    @SuppressWarnings("unchecked")
	public Vector detectaAudio() {
    	
        Vector lista = CaptureDeviceManager.getDeviceList(new javax.media.Format("LINEAR"));
        Vector devInfo = new Vector();
 
        if (lista != null) {
             for (int i=0; i<lista.size(); i++ ) {                
            	 devInfo.addElement(((CaptureDeviceInfo) lista.elementAt(i)).getName());
            } 
        }
        return (devInfo);        
    } 

    
    @SuppressWarnings("unchecked")
	public CaptureDeviceInfo getDevice(String strDeviceName) {
        Vector lista = CaptureDeviceManager.getDeviceList(null);
        CaptureDeviceInfo devInfoRet = null;
 
        if (lista != null) {
             for (int i=0; i<lista.size(); i++ ) {            	
                if (((CaptureDeviceInfo) lista.elementAt(i)).getName().equals(strDeviceName)) {                	
                	devInfoRet = (CaptureDeviceInfo) lista.elementAt(i);
                }
            } 
        }
        return (devInfoRet);
    }
    
    @SuppressWarnings("static-access")
	public boolean realize(Processor proc, Player player, int timeout) {
    	boolean retorno = true;
    	
    	System.out.println("Antes de realizado: " + proc.Realized);

	    synchronized(this) {	
	    	if (proc != null) {
	    		proc.realize();
	    	}
	    	if (player != null) {
	    		player.realize();
	    	}
	    	try {
	    		wait(timeout);
    			System.out.println("Depois de realizado: " + proc.Realized);
	    	}
	    	//catch (InterruptedException ie) {
	    	catch (Exception ie) {
	    		GravaLog logErro = new GravaLog("ERRO");
	    		logErro.gravaExcep("fidelis.controller.Ferramentas.realize()", ie);
	    		retorno = false;
	    	}	            	
	    }
	    return retorno;
    }
    
    public boolean configure(Processor proc, int timeout) {
    	boolean retorno = true;
    	
	    synchronized(this) {	
	    	proc.configure();
	    	try {
	    		wait(timeout);
	    	}
	    	//catch (InterruptedException ie) {
	    	catch (Exception ie) {
	    		GravaLog logErro = new GravaLog("ERRO");
	    		logErro.gravaExcep("fidelis.controller.Ferramentas.configure()", ie);
	    		retorno = false;
	    	}	            	
	    }
	    return retorno;
    }

}
