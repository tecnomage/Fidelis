package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

public class RecebeArquivo extends Thread {
	
	private Socket skt = null;
	private String strFileName;
	private String strDiretorio;
	private FileOutputStream fout = null;
	private DataInputStream din = null;
	private JTextArea texto = null;
	private PrintWriter pwVerbose;
	
	public RecebeArquivo(Socket s, String dir, JTextArea ta, PrintWriter pw) {
		skt = s;
		strDiretorio = dir;
		texto = ta;
		pwVerbose = pw;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String strTimestamp = df.format(new Date());
		texto.append("\n" + strTimestamp + " | Conexão com cliente estabelecida. IP do Cliente -> " + skt.getInetAddress());
		pwVerbose.println("\n" + strTimestamp + " | Conexão com cliente estabelecida. IP do Cliente -> " + skt.getInetAddress());
	}

	public void run() {		
		try {			
			din = new DataInputStream(skt.getInputStream());
			DataOutputStream dout = new DataOutputStream(skt.getOutputStream());
			
			strFileName = din.readUTF();
			String strHashZip = din.readUTF();
			
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strTimestamp = df.format(new Date());
			
			texto.append("\n" + strTimestamp + " | Recebendo arquivo -> " + strFileName);
			pwVerbose.println("\n" + strTimestamp + " | Recebendo arquivo -> " + strFileName);
			
			fout = new FileOutputStream(new File(strDiretorio + "\\" + strFileName + ".tmp"));
			
			int BytesRead = 0;   
			byte[] bBuffer = new byte[8192];
			//boolean fFim = false;
			/*while (BytesRead > 1 && !fFim) {				
				BytesRead = din.read(bBuffer);
				
				if (BytesRead == 1 && bBuffer[0] == (byte) -1) {
					fFim = true;
				}
				else {
					fout.write(bBuffer, 0, BytesRead);					
				}				
				//dout.writeUTF("0");
				//dout.flush();
			}*/
			
			while ((BytesRead = din.read(bBuffer)) > 1) {
				fout.write(bBuffer, 0, BytesRead);
				dout.writeInt(BytesRead);
			}
			
			fout.flush();
			fout.close();			
			
			strTimestamp = df.format(new Date());
			texto.append("\n" + strTimestamp + " | Arquivo recebido -> " + strFileName);
			pwVerbose.println("\n" + strTimestamp + " | Arquivo recebido -> " + strFileName);
			
			// Envia confirmacao de recebimento ok ou nao.
			boolean ret = validaArquivo(strDiretorio + "\\" + strFileName + ".tmp", strHashZip);
			//boolean ret = true;		
			
			try { Thread.sleep(10000); } catch(Exception e) { }

			if (ret) {
				dout.writeUTF("OK");
				texto.append("\nIntegridade do arquivo -> " + strFileName + " [Status: OK]\nRecepção Finalizada.\n");
				pwVerbose.println("\nIntegridade do arquivo -> " + strFileName + " [Status: OK]");
				pwVerbose.println("Recepção Finalizada.");
				File f = new File(strDiretorio + "\\" + strFileName + ".tmp");
				f.renameTo(new File(strDiretorio + "\\" + strFileName));
			}
			else {
				dout.writeUTF("NOK");
				texto.append("\nIntegridade do arquivo -> " + strFileName + " [Status: CORROMPIDO]\nRecepção Finalizada.\n");
				pwVerbose.println("\nIntegridade do arquivo -> " + strFileName + " [Status: CORROMPIDO]");
				pwVerbose.println("Recepção Finalizada.");
				File f = new File(strDiretorio + "\\" + strFileName);
				f.renameTo(new File(strDiretorio + "\\" + strFileName + ".erro_tx"));
			}
			dout.flush();			
			dout.close();
			din.close();
			skt.close();
			pwVerbose.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean validaArquivo(String strFile, String strHash) {
		GeraHash gh = new GeraHash();		
		if (gh.getHashFromFile(strFile).equals(strHash))
			return true;
		else
			return false;
	}
}
