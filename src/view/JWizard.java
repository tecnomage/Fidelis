package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Sessao;

@SuppressWarnings("serial")
public class JWizard extends JInternalFrame {
	
	private Container cont = null;
	private JPanel pSul = null;
	private JPanel pNorte = null;
	private JPanel pCentro = null;
	@SuppressWarnings("unchecked")
	private Vector vetPanel = null;
	
	private JButton btAnterior = null;
	private JButton btProximo = null;
	private JButton btFinalizar = null;
	
	private JLabel lbTitulo = null;
	private JButton btHelp = null;
	private int totalTelas = 0;
	private int idxTela = 0;
	private String[] titulos;
	
	
	@SuppressWarnings("unchecked")
	public JWizard(String strTitle, String telaTitulos[], Vector p) {		
		setTitle(strTitle);
		setSize(800, 600);		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setMaximizable(true);
	    setResizable(true);
	    setVisible(true);
		
		cont = getContentPane();
		cont.setLayout(new BorderLayout());

		totalTelas = telaTitulos.length;
		idxTela = 1;
		titulos = telaTitulos;
		
		pSul = new JPanel();
		pSul.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pSul.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		
		pNorte = new JPanel();
		pNorte.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pNorte.setBackground(Color.gray);		
		pNorte.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		vetPanel = p;
		pCentro = (JPanel) vetPanel.elementAt(0);
	
		lbTitulo = new JLabel();
		lbTitulo.setFont(new Font("Arial", Font.BOLD, 18));
		lbTitulo.setForeground(Color.white);
		lbTitulo.setText(titulos[0] + " (Página " + idxTela + " de " + totalTelas + ")");
		
		btHelp = new JButton(new ImageIcon("images/ajuda20.png"));
		btHelp.setText("Ajuda");
		btHelp.setToolTipText("Ajuda");
		
		pNorte.add(lbTitulo);
		pNorte.add(btHelp);
		
		btAnterior = new JButton("< Anterior");
		btAnterior.setEnabled(false);
		btProximo = new JButton("Próximo >");
		if (totalTelas == 1) {
			btProximo.setEnabled(false);
		}
		btFinalizar = new JButton("Fechar");
		
		pSul.add(btAnterior);
		pSul.add(btProximo);
		pSul.add(btFinalizar);
		
		cont.add(pNorte, BorderLayout.NORTH);
		cont.add(pCentro, BorderLayout.CENTER);
		cont.add(pSul, BorderLayout.SOUTH);
		
		if (pCentro instanceof PanelSelecionaAudienciaTable) {
			if (((PanelSelecionaAudienciaTable) pCentro).numAudiencias < 1) {
				btProximo.setEnabled(false);
			}
		}
		
		if (pCentro instanceof PanelSelecionaEventoTable) {
			if (((PanelSelecionaEventoTable) pCentro).numAudiencias < 1) {
				btProximo.setEnabled(false);
			}
		}
		
		btHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	FrameAjuda aj = new FrameAjuda(lbTitulo.getText());
            	aj.setVisible(true);
            }
		});
		
		btFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Object[] options = { "Sim", "Não" };
            	int opt = JOptionPane.showOptionDialog(null, "Deseja realmente sair?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            	// Sim
            	if (opt == 0) {
            		if (pCentro instanceof FramePlayAudiencia) {
                       	((FramePlayAudiencia) vetPanel.elementAt(idxTela-1)).finalizaMedia();
                	}
                	if (pCentro instanceof PanelDetectaDispositivos) {
                		int ret = ((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).getVisualizando(); 
                		if (ret == 1)
                			((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).finalizaMedia();
                		else if (ret == 2)
                			((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).finalizaFilmagem();
                	}
            		dispose();
            	}
            }
        });

		btAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (pCentro instanceof FramePlayAudiencia) {
                   	//((FramePlayAudiencia) vetPanel.elementAt(idxTela-1)).setDadosProcesso();
            		((FramePlayAudiencia) vetPanel.elementAt(idxTela-1)).finalizaMedia();
            	}

            	if (pCentro instanceof PanelDetectaDispositivos) {
            		int ret = ((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).getVisualizando(); 
            		if (ret == 1)
            			((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).finalizaMedia();
            		else if (ret == 2)
            			((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).finalizaFilmagem();
            	}

            	idxTela--;
            	if (idxTela <= 1) {
            		idxTela = 1;
            		btAnterior.setEnabled(false);
            	}
            	if (idxTela < totalTelas) {
            		btProximo.setEnabled(true);
            	}
            	lbTitulo.setText(titulos[idxTela-1] + " (Página " + idxTela + " de " + totalTelas + ")");
            	cont.remove(pCentro);
            	pCentro = (JPanel) vetPanel.elementAt(idxTela-1);            	
            	cont.add(pCentro, BorderLayout.CENTER);
            	pCentro.updateUI();
            	if (pCentro instanceof FrameGravaAudiencia) {
                   	((FrameGravaAudiencia) vetPanel.elementAt(idxTela-1)).setNumProcesso();
            	}
            	if (pCentro instanceof PanelPreVinculacao) {
                   	((PanelPreVinculacao) vetPanel.elementAt(idxTela-1)).setNumProcesso();
                   	((PanelPreVinculacao) vetPanel.elementAt(idxTela-1)).limpaAssuntos();
            	}
            	if (pCentro instanceof PanelExportaAudiencia) {
                   	((PanelExportaAudiencia) vetPanel.elementAt(idxTela-1)).setNumProcesso();
            	}
        		if (pCentro instanceof PanelSelecionaAudienciaTable) {
        			if (((PanelSelecionaAudienciaTable) pCentro).numAudiencias < 1) {
        				btProximo.setEnabled(false);
        			}
        		}
        		if (pCentro instanceof PanelSelecionaEventoTable) {
        			if (((PanelSelecionaEventoTable) pCentro).numAudiencias < 1) {
        				btProximo.setEnabled(false);
        			}
        		}
            }
        });

		btProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	
            	if (idxTela < totalTelas && 
            		(JPanel) vetPanel.elementAt(idxTela) instanceof FramePlayAudiencia &&
            		Sessao.numProcesso == null) { 
            		if ((JPanel) vetPanel.elementAt(idxTela-1) instanceof PanelSelecionaEventoTable)
            			JOptionPane.showMessageDialog(null, "Selecione um Evento antes de prosseguir.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
            		else
            			JOptionPane.showMessageDialog(null, "Selecione uma Audiência antes de prosseguir.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
            	}
            	else {            	
            		if (pCentro instanceof PanelDetectaDispositivos) {
                		int ret = ((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).getVisualizando(); 
                		if (ret == 1)
                			((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).finalizaMedia();
                		else if (ret == 2)
                			((PanelDetectaDispositivos) vetPanel.elementAt(idxTela-1)).finalizaFilmagem();
                	}
	            	idxTela++;
	            	if (idxTela >= totalTelas) {
	            		idxTela = totalTelas;
	            		btProximo.setEnabled(false);                	
	            	}
	            	if (idxTela > 1) {
	            		btAnterior.setEnabled(true);
	            	}
	            	lbTitulo.setText(titulos[idxTela-1] + " (Página " + idxTela + " de " + totalTelas + ")");
	            	cont.remove(pCentro);
	            	pCentro = (JPanel) vetPanel.elementAt(idxTela-1);            	
	            	cont.add(pCentro, BorderLayout.CENTER);
	            	pCentro.updateUI();
	            	if (pCentro instanceof FrameGravaAudiencia) {
	                   	((FrameGravaAudiencia) vetPanel.elementAt(idxTela-1)).setNumProcesso();
	                   	((FrameGravaAudiencia) vetPanel.elementAt(idxTela-1)).carregaJuizes();  	
	            	}
	            	if (pCentro instanceof PanelPreVinculacao) {
	                   	((PanelPreVinculacao) vetPanel.elementAt(idxTela-1)).setNumProcesso();
	                   	((PanelPreVinculacao) vetPanel.elementAt(idxTela-1)).limpaAssuntos();
	            	}
	            	if (pCentro instanceof FramePlayAudiencia) {
	                   	((FramePlayAudiencia) vetPanel.elementAt(idxTela-1)).setDadosProcesso();
	            	}
	            	if (pCentro instanceof PanelExportaAudiencia) {
	                   	((PanelExportaAudiencia) vetPanel.elementAt(idxTela-1)).setNumProcesso();
	            	}
	        		if (pCentro instanceof PanelSelecionaAudienciaTable) {
	        			if (((PanelSelecionaAudienciaTable) pCentro).numAudiencias < 1) {
	        				btProximo.setEnabled(false);
	        			}
	        		}
	        		if (pCentro instanceof PanelSelecionaEventoTable) {
	        			if (((PanelSelecionaEventoTable) pCentro).numAudiencias < 1) {
	        				btProximo.setEnabled(false);
	        			}
	        		}
            	}
            }
        });		
	}
	
	public void setTituloTela(String t) {
		lbTitulo.setText(t);
	}
	
}
