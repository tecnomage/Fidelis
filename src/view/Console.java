package view;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.BancodeDados;

public class Console {
	
	public static void main(String [] args) {
		BancodeDados db = new BancodeDados();
		db.removeTabelas();
		db.criaTabelas();
		//db.exportaHelp();
		//db.fechaConexao();
		
		//GeraHash g = new GeraHash();
		//System.out.println(g.getHashFromString("admin"));
	}
	
	public static void pdf() throws DocumentException, MalformedURLException, IOException {
		 
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, new FileOutputStream("logs/HelloWorld.pdf"));
		
		document.open();	
		
		Image iHdr = Image.getInstance("images/TRT9cab.jpg");
		iHdr.scalePercent(54);
		iHdr.setAbsolutePosition(30, 780);
		iHdr.setAlignment(Image.MIDDLE);

		document.add(iHdr);

		
		//document.add(new Paragraph("VIDEOCAP", new Font(Font.HELVETICA, 20, Font.BOLD)));
		document.add(new Paragraph(" ", new Font(Font.HELVETICA, 20, Font.BOLD)));
		document.add(new Paragraph("Dados da Audiência", new Font(Font.HELVETICA, 16, Font.BOLDITALIC, new Color(0, 0, 255))));
		document.add(new Paragraph(" ", new Font(Font.HELVETICA, 16, Font.BOLD)));
		document.add(new Paragraph("Número do Processo: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
		document.add(new Paragraph("Tipo de Audiência: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
		document.add(new Paragraph("Juíz: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
		document.add(new Paragraph("Data: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
		document.add(new Paragraph("Hora: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
		document.add(new Paragraph(" ", new Font(Font.HELVETICA, 16, Font.BOLD)));
		document.add(new Paragraph("Marcações de Tempo", new Font(Font.HELVETICA, 16, Font.BOLDITALIC, new Color(0, 0, 255))));
		document.add(new Paragraph(" ", new Font(Font.HELVETICA, 12, Font.BOLD)));
		
		PdfPTable table = new PdfPTable(4);
		table.setHorizontalAlignment(1);
		table.addCell(new Phrase("Tempo no Vídeo\n(hh:mm:ss)", new Font(Font.HELVETICA, 12, Font.BOLD)));
		table.addCell(new Phrase("Nome do Depoente", new Font(Font.HELVETICA, 12, Font.BOLD)));
		table.addCell(new Phrase("Qualificação do Depoente", new Font(Font.HELVETICA, 12, Font.BOLD)));
		table.addCell(new Phrase("Tema e SubTema", new Font(Font.HELVETICA, 12, Font.BOLD)));
		table.addCell("00:01:00");
		table.addCell("José da Silva");
		table.addCell("Autor");
		table.addCell("Vale Transporte");
		document.add(table);
		
		Image iRod = Image.getInstance("images/TRT9rod.jpg");
		iRod.scalePercent(53);
		iRod.setAbsolutePosition(30, 30);
		document.add(iRod);

		
		document.close();
		
		//HeaderFooter foot = new HeaderFooter(new Phrase("Página "), new Phrase("."));
		//foot.setPageNumber(1);
		//document.setFooter(foot);


	}

}
