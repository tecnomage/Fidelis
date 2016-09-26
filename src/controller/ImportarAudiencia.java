package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import model.TipoAudiencias;


public class ImportarAudiencia {
	
	@SuppressWarnings("unused")
	private String usuarioLogado;
	private String numProcesso;
	private String data;
	private String hora;
	
	public ImportarAudiencia (String strUsuarioLogado, String strNumProcesso, String strData, String strHora) {
		usuarioLogado = strUsuarioLogado;
		numProcesso = strNumProcesso;
		data = strData;
		hora = strHora;
	}
	
	public int getCodTipoAudiencia(String strDesc) {
		TipoAudiencias ta = new TipoAudiencias();
		return(ta.getCodTipoAudiencia(strDesc));		
	}
	
	public boolean copiaVideo(File dir) {		
		String fileDestino = System.getProperty("user.dir") + "/videos/" + Sessao.fileName(numProcesso, data, hora) + ".asf";
		String fileOrigem = dir.getPath();
		
		FileInputStream Source;
		FileOutputStream Dest;
		try {
			Source = new FileInputStream(fileOrigem);
			Dest  = new FileOutputStream(fileDestino); 

			int BytesRead;   
			byte[] bBuffer = new byte[1024];   
	  
			while (true) {   
				BytesRead = Source.read(bBuffer);   
				if (BytesRead == -1){   
					break;   
				}   
				Dest.write(bBuffer, 0, BytesRead);   
			}   
			
			Source.close();   
			Dest.close();   
		   
		} catch (FileNotFoundException e) {		
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.ImportarAudiencia", e);
			return false;
		} catch (IOException ioe) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.ImportarAudiencia", ioe);
			return false;
		}			
		return true;
	}
	
}
