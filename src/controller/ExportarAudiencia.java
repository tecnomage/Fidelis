package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Audiencia;

public class ExportarAudiencia {
	
	private String usuarioLogado;
	private String numProcesso;
	private String data;
	private String hora;
	
	public ExportarAudiencia (String strUsuarioLogado, String strNumProcesso, String strData, String strHora) {
		usuarioLogado = strUsuarioLogado;
		numProcesso = strNumProcesso;
		data = strData;
		hora = strHora;		
	}
	
	public boolean escolheDiretorio() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Abrir");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);        
        int res = fc.showOpenDialog(null);
        
        if (res == JFileChooser.APPROVE_OPTION) {
            File diretorio = fc.getSelectedFile();
            if (copiaVideo(diretorio)) {
            	if (!geraPDF(diretorio)) {
            		JOptionPane.showMessageDialog(null, "Não foi possível gerar arquivo PDF.\nContacte o Administrador do sistema para análise da Log.", "fidelis.controller.ExportarAudiencia.geraPDF()", JOptionPane.ERROR_MESSAGE);
            		return false;
            	}            	
            	return true;
            }
            else {
            	JOptionPane.showMessageDialog(null, "Não foi possível copiar o arquivo de vídeo.\nContacte o Administrador do sistema para análise da Log.", "fidelis.controller.ExportarAudiencia.copiaVideo()", JOptionPane.ERROR_MESSAGE);
            	return false;
            }
        }
        else {
        	return false;
        }
	}
	
	public boolean copiaVideo(File dir) {
		String fileOrigem;
		Ferramentas tmFmt = new Ferramentas();
		String fp = tmFmt.getPropriedades("player");
		if (fp == null || !fp.equals("1")) {
			fileOrigem = System.getProperty("user.dir") + "/videos/" + Sessao.fileName(numProcesso, data, hora) + ".asf";
		}
		else {
			fileOrigem = SessaoConfig.videoPath + "/" + Sessao.fileName(Sessao.numProcesso, Sessao.data, Sessao.hora) + ".asf";
		}	
		
		String fileDestino = dir.getPath() + "/" + Sessao.fileName(numProcesso, data, hora) + ".asf";
		
//		File f = new File(fileOrigem);
//		if (!f.exists()) {
//			fileOrigem = System.getProperty("user.dir") + "/videos/" + Sessao.fileName(numProcesso, data, hora) + ".wav";
//			fileDestino = dir.getPath() + "/" + Sessao.fileName(numProcesso, data, hora) + ".wav";
//		}
		
		/**
		 *  Verifica se o arquivo que serah transmitido eh uma audiencia/evento integro.
		 */
		GeraHash gh = new GeraHash();
		String hashArqTmp = gh.getHashFromFile(fileOrigem);
		
		Audiencia aud = new Audiencia(numProcesso, data, hora);
		String hashProcesso = aud.getHashProcesso();
		
		if (hashArqTmp == null || !hashArqTmp.equals(hashProcesso)) {
			GravaLog log = new GravaLog("INFO");
			log.grava("fidelis.controller.ExportarAudiencia.copiaVideo()", usuarioLogado, "Media (.asf) do Processo: " + numProcesso + ", " + data + ", " + hora + " está corrompido. Checagem de HASH não validou o arquivo de média. Contacte o Administrador.");
			return false;
		}
			
		/**/
		
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
			log.gravaExcep("fidelis.controller.ExportarAudiencia", e);
			return false;
		} catch (IOException ioe) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.ExportarAudiencia", ioe);
			return false;
		}			
		return true;
	}
	
	public boolean geraXML(String strExtensao, File dir) {		
		XMLControl xml = new XMLControl(usuarioLogado);
		return xml.grava(numProcesso, data, hora, dir, strExtensao);				
	}

	public boolean geraPDF(File dir) {		
		XMLControl xml = new XMLControl(usuarioLogado);
		return xml.gravaPDF(numProcesso, data, hora, dir);				
	}
	
}
