package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import model.Configuracoes;

public class GravaLog {
	
	private FileOutputStream fout = null;
	private String fileNameTxt = null;
	private String fileNameXml = null;
	private PrintWriter p = null;
	
	public GravaLog() {
		SimpleDateFormat logData = new SimpleDateFormat("yyyyMMdd");
		String dia = logData.format(new Date());
		SessaoConfig.TipoLog = "2";		
		fileNameTxt = "logs/FidelisVC." + SessaoConfig.vara + ".ERRO." + dia + ".log";
	}
	
	public GravaLog(String strTipo) {		
		if (SessaoConfig.TipoLog == null) {
			Configuracoes cfg = new Configuracoes();
			SessaoConfig.TipoLog = cfg.getConfigParm("tipo_log");
		}
		
		SimpleDateFormat logData = new SimpleDateFormat("yyyyMMdd");
		String dia = logData.format(new Date());
		
		if (strTipo.equals("INFO")) {
			fileNameTxt = "logs/FidelisVC." + SessaoConfig.vara + ".INFO." + dia + ".log";
			fileNameXml = "logs/FidelisVC." + SessaoConfig.vara + ".INFO." + dia + ".xml";
		}
		else {
			fileNameTxt = "logs/FidelisVC." + SessaoConfig.vara + ".ERRO." + dia + ".log";
		}
	}
	
	public void grava(String strClasse, String strUsuario, String strMsg) {
		
		strClasse = crip(strClasse);
		strUsuario = crip(strUsuario);
		strMsg = crip(strMsg);
		
		try {					
			if (SessaoConfig.TipoLog.equals("1") || SessaoConfig.TipoLog.equals("3")) { // XML ou Ambos
				boolean flagHeader = false;
				File f = new File(fileNameXml);
				if (!f.exists()) {
					flagHeader = true;
				}				
				else {
					FileInputStream fin = new FileInputStream(fileNameXml);
					FileOutputStream fot = new FileOutputStream("logs/tmp");
					
					int BytesRead = 0;   
					byte[] bBuffer = new byte[1000000];   
			  
					while (BytesRead > -1) {   
						BytesRead = fin.read(bBuffer);   
						if (BytesRead > -1){							
							if (bBuffer[BytesRead-9] == 60  && bBuffer[BytesRead-8] == 47  && bBuffer[BytesRead-7] == 105 &&
								bBuffer[BytesRead-6] == 110 && bBuffer[BytesRead-5] == 102 && bBuffer[BytesRead-4] == 111 &&
								bBuffer[BytesRead-3] == 62) {
								fot.write(bBuffer, 0, BytesRead - 9);
							}
							else {
								fot.write(bBuffer, 0, BytesRead);
							}
						}
					}   
					
					fin.close();   
					fot.close();
					f.delete();
					File fTmp = new File("logs/tmp");
					fTmp.renameTo(f);
				}
				
				fout = new FileOutputStream(fileNameXml, true);
				p = new PrintWriter(fout);
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String strTimestamp = df.format(new Date());
			
				if (flagHeader) {
					p.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
					p.println("<info>");
				}

				p.println("   <ocorrencia>");
				p.println("      <horario>" + strTimestamp + "</horario>");
				p.println("      <classe>" + strClasse + "</classe>");
				p.println("      <usuario>" + strUsuario + "</usuario>");
				p.println("      <mensagem>" + strMsg + "</mensagem>");
				p.println("   </ocorrencia>");				
				p.println("</info>");
				
				p.close();
				fout.close();
			}
			
			if (SessaoConfig.TipoLog.equals("2") || SessaoConfig.TipoLog.equals("3")) { // TXT ou Ambos
				fout = new FileOutputStream(fileNameTxt, true);
				p = new PrintWriter(fout);
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String strTimestamp = df.format(new Date());
				p.println(strTimestamp + " | " + strClasse);
				p.println("Usuário: " + strUsuario + " | " + strMsg);
				p.println("");
				p.close();
				fout.close();
			}			
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ATENÇÃO: A log do sistema não pôde ser gerada.\nPara garantir a integridade dos dados contidos no mesmo, esta sessão será abortada.\nContate o Administrador do sistema para urgente correção!!!", "fidelis.controller.GravaLog()", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void gravaExcep(String strClasse, Exception excep) {
		try {
			fout = new FileOutputStream(fileNameTxt, true);
			p = new PrintWriter(fout);
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strTimestamp = df.format(new Date());
			p.println(strTimestamp + " | " + strClasse);			
			excep.printStackTrace(p);
			p.close();
			fout.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ATENÇÃO: A log do sistema não pôde ser gerada.\nPara garantir a integridade dos dados contidos no mesmo, esta sessão será abortada.\nContate o Administrador do sistema para urgente correção!!!", "fidelis.controller.GravaLog()", JOptionPane.ERROR_MESSAGE);
			System.exit(0);			
		}
	}
	
	private String crip(String s) {
		String aux = "";
		for (int i=0; i<s.length(); i++) {
			aux += pZero(((int) s.charAt(i)) + 100, 3);
		}
		return aux;
	}
	
	private String pZero(int n, int nz) {
		String aux = "";
		int fator=0;
		if (n < 10) fator = nz - 1;
		if (n > 9 && n < 100) fator = nz - 2;
		if (n > 99) fator = nz - 3;
		
		for (int i=0; i<fator; i++) {
			aux += "0";
		}
		aux += String.valueOf(n);
		return aux;
	}
}
