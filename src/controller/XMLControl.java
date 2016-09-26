package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import model.Configuracoes;

public class XMLControl {
	
	
	public String numProcesso = null;
	public String data = null;
	public String hora = null;
	public String tipo = null;
	public String juiz = null;
	public String video = null;
	public String hashvideo = null;
	public String [] time = null;
	public String [] tema = null;
	public String [] subtema = null;
	public String [] qualificacao = null;
	public String [] depoente = null;
	
	private String usuarioLogado = null;
	private File file = null;
	
	public XMLControl(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
	}
	
	public XMLControl(String strUsuarioLogado, File f) {
		usuarioLogado = strUsuarioLogado;
		file = f;
	}
	
	public File getFile() {
		return file;
	}

	public boolean getDadosProcesso(File f) {
		try {
			if (f == null) {
				f = file;
			}
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(f);

			NodeList nodes = doc.getElementsByTagName("processo");
			
			for (int i = 0; i < nodes.getLength(); i++) {				
				Element elemento = (Element) nodes.item(i);
				
				NodeList node = elemento.getElementsByTagName("numero");
				Element linha = (Element) node.item(0);
				numProcesso = linha.getTextContent();
				
				node = elemento.getElementsByTagName("data");
				linha = (Element) node.item(0);
				data = linha.getTextContent();

				node = elemento.getElementsByTagName("hora");
				linha = (Element) node.item(0);
				hora = linha.getTextContent();
				
				node = elemento.getElementsByTagName("tipo");
				linha = (Element) node.item(0);
				tipo = linha.getTextContent();

				node = elemento.getElementsByTagName("juiz");
				linha = (Element) node.item(0);
				juiz = linha.getTextContent();
				
				node = elemento.getElementsByTagName("video");
				linha = (Element) node.item(0);
				video = linha.getTextContent();
				
				node = elemento.getElementsByTagName("hashvideo");
				linha = (Element) node.item(0);
				hashvideo = linha.getTextContent();
			}
		}
		catch (Exception e) {
			GravaLog logErro = new GravaLog("ERRO");
			logErro.gravaExcep("fidelis.controller.XMLControl.getDadosProcesso()", e);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean getDadosMarcacao(File f) {
		
		try {
			if (f == null) {
				f = file;
			}
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(f);

			NodeList nodes = doc.getElementsByTagName("marcacao");
			time = new String[nodes.getLength()];
			tema = new String[nodes.getLength()];
			subtema = new String[nodes.getLength()];
			qualificacao = new String[nodes.getLength()];
			depoente = new String[nodes.getLength()];
			
			for (int i = 0; i < nodes.getLength(); i++) {				
				Element elemento = (Element) nodes.item(i);
				
				NodeList node = elemento.getElementsByTagName("time");
				Element linha = (Element) node.item(0);
				time[i] = linha.getTextContent();
	
				node = elemento.getElementsByTagName("tema");
				linha = (Element) node.item(0);
				tema[i] = linha.getTextContent();
				
				node = elemento.getElementsByTagName("subtema");
				linha = (Element) node.item(0);
				subtema[i] = linha.getTextContent();
	
				node = elemento.getElementsByTagName("qualificacao");
				linha = (Element) node.item(0);
				qualificacao[i] = linha.getTextContent();
				
				node = elemento.getElementsByTagName("depoente");
				linha = (Element) node.item(0);
				depoente[i] = linha.getTextContent();
			}
		}
		catch (Exception e) {
			GravaLog logErro = new GravaLog("ERRO");
			logErro.gravaExcep("fidelis.controller.XMLControl.getDadosMarcacao()", e);
			e.printStackTrace();
		}
		
		return true;	
	}
		
	
	@SuppressWarnings("unchecked")
	public boolean grava(String strNumProcesso, String strData, String strHora, File dir, String strExtensao) {
		
		String xmlFile = dir.getPath() + "\\" + Sessao.fileName(strNumProcesso, strData, strHora) + ".xml";

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(xmlFile));

			DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", strNumProcesso, strData, strHora);
			String [] resultset = dealer.getAudiencia();
			
			if (resultset == null) {
				resultset = new String[3];
				resultset[0] = "";
				resultset[1] = "";
				resultset[2] = "";
			}
			
			bw.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			bw.newLine();
			bw.write("<processo>");
			bw.newLine();
			bw.write("   <numero>" + strNumProcesso + "</numero>");
			bw.newLine();
			bw.write("   <data>" + strData + "</data>");
			bw.newLine();
			bw.write("   <hora>" + strHora + "</hora>");
			bw.newLine();
			bw.write("   <tipo>" + resultset[0] + "</tipo>");
			bw.newLine();
			bw.write("   <juiz>" + resultset[1] + "</juiz>");
			bw.newLine();
			bw.write("   <video>" + Sessao.fileName(strNumProcesso, strData, strHora) + strExtensao + "</video>");
			bw.newLine();
			bw.write("   <hashvideo>" + resultset[2] + "</hashvideo>");	
			bw.newLine();
			
			DBDealer dealerMarca = new DBDealer(usuarioLogado, "MARCACOES", strNumProcesso, strData, strHora);
			Vector vtRet = dealerMarca.listar();
			for (int i = 0; i < vtRet.size(); i++) {
				bw.write("   <marcacao>");
				bw.newLine();

				String[] strRet = (String[]) vtRet.elementAt(i);				
				bw.write("      <time>" + strRet[0] + "</time>");
				bw.newLine();
				bw.write("      <tema>" + strRet[1] + "</tema>");
				bw.newLine();
				bw.write("      <subtema>" + strRet[2] + "</subtema>");
				bw.newLine();
				bw.write("      <qualificacao>" + strRet[3] + "</qualificacao>");
				bw.newLine();
				bw.write("      <depoente>" + strRet[4] + "</depoente>");
				bw.newLine();
				
				bw.write("   </marcacao>");
				bw.newLine();				
			}
			
			bw.write("</processo>");
			bw.newLine();
			bw.close();			
			
		} catch (IOException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.XMLControl.grava()", e);
			e.printStackTrace();
			return false;
		}

		return true;
	}

	
	@SuppressWarnings("unchecked")
	public boolean gravaPDF(String strNumProcesso, String strData, String strHora, File dir) {
		
		String pdfFile = dir.getPath() + "/" + Sessao.fileName(strNumProcesso, strData, strHora) + ".pdf";				

		try {			
			DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", strNumProcesso, strData, strHora);
			String [] resultset = dealer.getAudiencia();
			
			com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
			PdfWriter.getInstance(document, new FileOutputStream(pdfFile));		
			
			document.open();
			
			Configuracoes cfg = new Configuracoes();
			String strCabFile = cfg.getConfigParm("cabecalho");
			File fCab = new File(strCabFile);
			
			if (fCab.exists()) {
				Image iHdr = Image.getInstance(strCabFile);
				iHdr.scalePercent(54);
				iHdr.setAbsolutePosition(30, 780);
				//iHdr.setAlignment(Image.MIDDLE);
				document.add(iHdr);
			}
			
			//document.add(new Paragraph("VIDEOCAP", new Font(Font.HELVETICA, 20, Font.BOLD)));
			document.add(new Paragraph(" ", new Font(Font.HELVETICA, 20, Font.BOLD)));
			if (!strNumProcesso.equals("0000000-00.0000.0.00.0000")) 
				document.add(new Paragraph("Dados da Audiência\n", new Font(Font.HELVETICA, 14, Font.BOLDITALIC, new Color(0, 0, 255))));
			else
				document.add(new Paragraph("Dados do Evento\n", new Font(Font.HELVETICA, 14, Font.BOLDITALIC, new Color(0, 0, 255))));
			
			//document.add(new Paragraph(" ", new Font(Font.HELVETICA, 14, Font.BOLD)));
			if (!strNumProcesso.equals("0000000-00.0000.0.00.0000")) {
				document.add(new Phrase("Número do Processo: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
				document.add(new Phrase(strNumProcesso + "\n", new Font(Font.HELVETICA, 12, Font.NORMAL)));			
				document.add(new Phrase("Tipo de Audiência: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
				document.add(new Phrase(resultset[0] + "\n", new Font(Font.HELVETICA, 12, Font.NORMAL)));
				document.add(new Phrase("Juiz: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
				document.add(new Phrase(resultset[1] + "\n", new Font(Font.HELVETICA, 12, Font.NORMAL)));
			}
			else {
				document.add(new Phrase("Evento: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
				document.add(new Phrase(resultset[1] + "\n", new Font(Font.HELVETICA, 12, Font.NORMAL)));				
			}
			document.add(new Phrase("Data: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
			document.add(new Phrase(strData + "\n", new Font(Font.HELVETICA, 12, Font.NORMAL)));
			document.add(new Phrase("Hora: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
			document.add(new Phrase(strHora, new Font(Font.HELVETICA, 12, Font.NORMAL)));
			document.add(new Paragraph(" ", new Font(Font.HELVETICA, 16, Font.BOLD)));
			document.add(new Paragraph("Marcações de Tempo", new Font(Font.HELVETICA, 14, Font.BOLDITALIC, new Color(0, 0, 255))));
			document.add(new Paragraph(" ", new Font(Font.HELVETICA, 12, Font.BOLD)));
			
			PdfPTable table = new PdfPTable(4);
			//table.setHorizontalAlignment(1);
			table.setWidthPercentage(100);
			table.addCell(new Phrase("Tempo no Vídeo\n(hh:mm:ss)", new Font(Font.HELVETICA, 10, Font.BOLD)));
			table.addCell(new Phrase("Nome do Depoente", new Font(Font.HELVETICA, 10, Font.BOLD)));
			table.addCell(new Phrase("Qualificação do Depoente", new Font(Font.HELVETICA, 10, Font.BOLD)));
			table.addCell(new Phrase("Assunto e SubTema", new Font(Font.HELVETICA, 10, Font.BOLD)));
			
			DBDealer dealerMarca = new DBDealer(usuarioLogado, "MARCACOES", strNumProcesso, strData, strHora);
			Vector vtRet = dealerMarca.listar();
			for (int i = 0; i < vtRet.size(); i++) {
				String[] strRet = (String[]) vtRet.elementAt(i);
				
				table.addCell(new Phrase(strRet[0], new Font(Font.HELVETICA, 8, Font.NORMAL))); // tempo
				table.addCell(new Phrase(strRet[4], new Font(Font.HELVETICA, 8, Font.NORMAL))); // depoente
				table.addCell(new Phrase(strRet[3], new Font(Font.HELVETICA, 8, Font.NORMAL))); // qualificacao
				table.addCell(new Phrase(strRet[1] + "\n" + strRet[2], new Font(Font.HELVETICA, 8, Font.NORMAL))); // tema e subtema				
			}
			
			document.add(table);
			
			String strRodFile = cfg.getConfigParm("rodape");
			File fRod = new File(strRodFile);
			
			if (fRod.exists()) {
				Image iRod = Image.getInstance(strRodFile);
				iRod.scalePercent(53);
				iRod.setAbsolutePosition(30, 30);
				document.add(iRod);
			}

			document.close();		
			
		} catch (DocumentException de) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.XMLControl.gravaPDF()", de);
			de.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gravar o arquivo PDF.\nContacte o Administrador para análise da LOG.", "fidelis.controll.XMLControl.gravaPDF()", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch (FileNotFoundException fnf ) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.XMLControl.gravaPDF()", fnf);
			fnf.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gravar o arquivo PDF.\nContacte o Administrador para análise da LOG.", "fidelis.controll.XMLControl.gravaPDF()", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch (Exception e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.XMLControl.gravaPDF()", e);
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao gravar o arquivo PDF.\nContacte o Administrador para análise da LOG.", "fidelis.controll.XMLControl.gravaPDF()", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

}
