package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.GravaLog;

@SuppressWarnings("serial")
public class FrameSobre extends JDialog {
	
	private JTextArea taTexto = null;
	private JButton btFecha = null;

	public FrameSobre() {
		this.setTitle("Sobre");
		this.setSize(600, 680);
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
        this.setLocation((tela.width - this.getSize().width) / 2, (tela.height - this.getSize().height) / 2);   
        JPanel pCentro = new JPanel();
        JPanel pSul = new JPanel();
        
        taTexto = new JTextArea();
        taTexto.setRows(34);
        taTexto.setColumns(48);
        taTexto.setWrapStyleWord(true);
        taTexto.setLineWrap(true);
        taTexto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        
		try {
			FileInputStream fin = new FileInputStream(new File(System.getProperty("user.dir") + "/sobre.txt"));
			byte[] bBuffer = new byte[4096];     
			String strBuffer = "";
			while (true) {   
				int BytesRead = fin.read(bBuffer);	
				if (BytesRead == -1){   
					break;   
				}			
				strBuffer += new String(bBuffer, 0, BytesRead);
			}
			fin.close();
			taTexto.setText(strBuffer);
		}	 
		catch (FileNotFoundException e) {		
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.FrameSobre", e);
		} 
		catch (IOException ioe) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.FrameSobre", ioe);	
		}

        taTexto.setEditable(false);
        
        btFecha = new JButton("Fechar");
		btFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	dispose();            	
            }
		});
		
		JScrollPane sp = new JScrollPane(taTexto);
		taTexto.setCaretPosition(0);
		
        pCentro.add(sp);
        
        pSul.setLayout(new GridLayout(2,1));
        pSul.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JPanel pSul_1 = new JPanel();
        //JPanel pSul_2 = new JPanel();
        
        pSul.add(pSul_1);
        //pSul.add(pSul_2);

        pSul_1.add(btFecha);
        
        //JLabel lbDireitos1 = new JLabel("(C) 2009, TRT 9a. Região - Paraná - Todos os Direitos Reservados"); 
        //JLabel lbDireitos1 = new JLabel(new ImageIcon("images/FidelisDireitosAjuda.png"));
        //lbDireitos1.setFont(new Font("Arial", Font.PLAIN, 9));
        //lbDireitos2.setFont(new Font("Monospaced", Font.PLAIN, 10));
        //pSul_2.add(lbDireitos1);
        //pSul_2.add(lbDireitos2);
        
        this.add(pCentro, BorderLayout.CENTER);
        this.add(pSul, BorderLayout.SOUTH);
	}
}
