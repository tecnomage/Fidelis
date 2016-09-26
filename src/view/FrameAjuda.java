package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.GravaLog;
import controller.SessaoConfig;
import model.Ajuda;

@SuppressWarnings("serial")
public class FrameAjuda extends JDialog {
	
	private JTextArea taTexto = null;
	private JButton btFecha = null;
	private JButton btSobre = null;
	private JButton btSalva = null;
	
	private String chave;

	public FrameAjuda(String strChave) {
		this.setTitle("Ajuda");
		this.setSize(300, 680);
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
        this.setLocation((tela.width - this.getSize().width), (tela.height - this.getSize().height) / 2);   
        JPanel pCentro = new JPanel();
        JPanel pSul = new JPanel();
        
        chave = strChave.substring(0, strChave.lastIndexOf(" (P")); // (Página x de y)
        
        taTexto = new JTextArea();
        taTexto.setRows(32);
        taTexto.setColumns(24);
        taTexto.setWrapStyleWord(true);
        taTexto.setLineWrap(true);
        taTexto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        
        Ajuda ajuda = new Ajuda();
        taTexto.setText(ajuda.getTexto(chave));
        taTexto.setEditable(false);
        
        btFecha = new JButton("Fechar");
		btFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	dispose();            	
            }
		});
		
        btSobre = new JButton("Sobre");
		btSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	FrameSobre sob = new FrameSobre();
            	sob.setVisible(true);            	
            }
		});
		
        btSalva = new JButton("Salvar");
		btSalva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (taTexto.getText().length() > 1024) {
            		JOptionPane.showMessageDialog(null, "Texto de Ajuda não pode exceder 1.024 caracteres. Total: " + taTexto.getText().length(), "Salvar Ajuda", JOptionPane.WARNING_MESSAGE);
            	}
            	else {
            		Ajuda ajuda2 = new Ajuda();
            		GravaLog log = new GravaLog("INFO");            		
            		if (ajuda2.setTexto(chave, taTexto.getText())) { 
            			log.grava("fidelis.view.FrameAjuda(" + chave + ")", SessaoConfig.usuarioLogado, "Texto de Ajuda atualizado com SUCESSO!");
            			JOptionPane.showMessageDialog(null, "Texto de Ajuda atualizado com SUCESSO!", "Salvar Ajuda", JOptionPane.INFORMATION_MESSAGE);
            		}
            		else {
            			log.grava("fidelis.view.FrameAjuda(" + chave + ")", SessaoConfig.usuarioLogado, "ERRO na atualização do Texto de Ajuda.");
            			JOptionPane.showMessageDialog(null, "ERRO na atualização do Texto de Ajuda.", "Salvar Ajuda", JOptionPane.ERROR_MESSAGE);
            		}
            	}
            }
		});
		
        pCentro.add(taTexto);
        
        pSul.setLayout(new GridLayout(2,1));
        pSul.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        JPanel pSul_1 = new JPanel();
        JPanel pSul_2 = new JPanel();
        
        pSul.add(pSul_1);
        pSul.add(pSul_2);
        
        if (SessaoConfig.isAdmin) {
        	taTexto.setEditable(true);
        	pSul_1.add(btSalva);
        }
        pSul_1.add(btFecha);
        pSul_1.add(btSobre);
        
        //JLabel lbDireitos1 = new JLabel("(C) 2009, TRT 9a. Região - Paraná - Todos os Direitos Reservados"); 
        JLabel lbDireitos1 = new JLabel(new ImageIcon("images/FidelisDireitosAjuda.png"));
        //lbDireitos1.setFont(new Font("Arial", Font.PLAIN, 9));
        //lbDireitos2.setFont(new Font("Monospaced", Font.PLAIN, 10));
        pSul_2.add(lbDireitos1);
        //pSul_2.add(lbDireitos2);
        
        this.add(pCentro, BorderLayout.CENTER);
        this.add(pSul, BorderLayout.SOUTH);
	}
}
