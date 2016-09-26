package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ButtonMain extends JFrame {
	
	String usuarioLogado = null;
	
	JPanel pTop = null;
	JPanel pBotoes = null;
	Container cont = null;
	 
	JButton btCadastros = null;
	JButton btAudiencias = null;
	JButton btEventos = null;
	JButton btSair = null;
	
	ButtonMain(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(1010, 728);
		//setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);		
		setTitle("Videocap - Sistema de Gravação em Áudio e Vídeo de Audiências - [" + usuarioLogado + "]");
		
		cont = getContentPane();		
		cont.setLayout(new BorderLayout());
		//cont.setLayout(null);
		cont.setBackground(Color.gray);
		
		//pTop = new JPanel();
		//pTop.setLayout(new FlowLayout(FlowLayout.RIGHT));
		//pTop.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));		
		//btSair = new JButton("Sair");
		//pTop.add(btSair);
		
		pBotoes = new JPanel();
		pBotoes.setLayout(new GridLayout(20, 1, 1, 1));
		pBotoes.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		//pBotoes.setBounds(1, 1, 110, 600);
		//pBotoes.setBackground(Color.darkGray);
		
		btCadastros = new JButton("Cadastros");	
		btCadastros.setToolTipText("Tipos de Audiências / Motivos de Finalizações");
		pBotoes.add(btCadastros);
		
		btAudiencias = new JButton("Audiêcias");	
		btAudiencias.setToolTipText("Gravação de Audiências");
		pBotoes.add(btAudiencias);

		btEventos = new JButton("Eventos");	
		btEventos.setToolTipText("Gravação de Eventos");
		pBotoes.add(btEventos);
			
		btSair = new JButton("Sair");
		btSair.setToolTipText("Sair do Sistema");
		pBotoes.add(btSair);
		
		//cont.add(pTop, BorderLayout.NORTH);
		cont.add(pBotoes, BorderLayout.WEST);
		
		btCadastros.addActionListener(new java.awt.event.ActionListener() {
            @SuppressWarnings("unchecked")
			public void actionPerformed(java.awt.event.ActionEvent evt) {
        		String [] telasTitulos = {"Cadastro de Tipos de Audiências", "Cadastro de Motivos de Finalizações de Audiências"};
        		PanelTreeManutencao p0 = new PanelTreeManutencao(usuarioLogado, "tabela: TIPOS DE AUDIÊNCIAS", "TIPOAUDIENCIAS");
        		PanelTreeManutencao p1 = new PanelTreeManutencao(usuarioLogado, "tabela: MOTIVO DE FINALIZAÇÕES", "MOTIVOFINALIZACOES");		
        		Vector p = new Vector();
        		p.addElement((JPanel)p0);
        		p.addElement((JPanel)p1);
        		
        		JWizard w = new JWizard("Cadastros Básicos", telasTitulos, p);
        		w.setVisible(true);
        		w.setSize(300, 200);
        		cont.add(w);
           }
        });		
		
		btAudiencias.addActionListener(new java.awt.event.ActionListener() {
	        @SuppressWarnings("unchecked")
			public void actionPerformed(java.awt.event.ActionEvent evt) {
	        	@SuppressWarnings("unused")
				String [] titulos = {"Gravação da Audiência"};
	        	@SuppressWarnings("unused")
				Vector p = new Vector();
	        	//JPanel p0 = new FrameGravaAudiencia();
	        	//p.addElement(p0);
	        	
	        	//JWizard audienciaFrame = new JWizard("Gravação de Audiências", titulos, p);
	        	//audienciaFrame.setVisible(true);
	        	//cont.add(audienciaFrame);
	        }
	    });		


		btSair.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            int confirma = JOptionPane.showConfirmDialog(null, "Deseja realmente sair do sistema?", "Encerrar Sessão", JOptionPane.OK_CANCEL_OPTION);
	            if (confirma == 0) {
	                System.exit(0);
	            }
	        }
	    });		
	}
	
	public static void main(String [] args) {
		ButtonMain bm = new ButtonMain("xxx");
		bm.setVisible(true);
	}

}
