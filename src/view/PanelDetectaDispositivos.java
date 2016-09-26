package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.DSJCaptura;
import controller.DSJTools;
import controller.Sessao;
import controller.ThreadPreview;
import controller.ThreadVisualiza;
import de.humatic.dsj.DSFiltergraph;

@SuppressWarnings("serial")
public class PanelDetectaDispositivos extends JPanel {

	public String usuarioLogado = null;
	private JComboBox cbVideo = null;
	private JComboBox cbAudio = null;
	private JPanel pVideo;
	private JButton btTestar;
	private JButton btAjusteVideo;
	private JButton btVisualizar;
	private JPanel pBotoesVideo;
	private JSlider jsVolume;
	private JLabel lbTempo;
	
	private DSFiltergraph graph;
	private DSJCaptura cap;
	
	public PanelDetectaDispositivos(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
		
		JPanel borda = new JPanel();
		borda.setLayout(null);
		borda.setBorder(javax.swing.BorderFactory.createTitledBorder("Selecione o dispositivo de VÍDEO"));
		borda.setBounds(8, 8, 350, 100);
		
		JPanel bordaAudio = new JPanel();
		bordaAudio.setLayout(null);
		bordaAudio.setBorder(javax.swing.BorderFactory.createTitledBorder("Selecione o dispositivo de ÁUDIO"));
		bordaAudio.setBounds(8, 130, 350, 100);	
		
		DSJTools dsjt = new DSJTools();		
		cbVideo = new JComboBox(dsjt.dsjDetectaVideo());
		cbVideo.setBounds(20, 25, 300, 20);
		cbVideo.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				Sessao.nomeDispositivoVideo = cbVideo.getSelectedItem().toString();						
			}		
		});		
		
		cbAudio = new JComboBox(dsjt.dsjDetectaAudio());
		cbAudio.setBounds(20, 25, 300, 20);
		cbAudio.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				Sessao.nomeDispositivoAudio = cbAudio.getSelectedItem().toString();						
			}		
		});	
		
		borda.add(cbVideo);	
		bordaAudio.add(cbAudio);
		this.setLayout(null);
		this.add(borda);
		this.add(bordaAudio);		
		
		Sessao.nomeDispositivoVideo = cbVideo.getSelectedItem().toString();
		Sessao.nomeDispositivoAudio = cbAudio.getSelectedItem().toString();
		
		montaPainelVideo();		
		this.add(pVideo);
		this.add(pBotoesVideo);
		
		cap = new DSJCaptura(pVideo, graph);	
	}
	
	
	public void montaPainelVideo() {
		// Componentes de Video
		pVideo = new JPanel();
		pVideo.setBounds(370, 16, 355, 260);
		pVideo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		pBotoesVideo = new JPanel();
		pBotoesVideo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pBotoesVideo.setBounds(370, 280, 355, 70);
		pBotoesVideo.setLayout(null);
		
		lbTempo = new JLabel("Gravando 20 seg.");
		lbTempo.setForeground(Color.blue);
		lbTempo.setBounds(10, 40, 105, 15);
		lbTempo.setVisible(false);
		
		JLabel lbVolume = new JLabel("Volume: ");
		lbVolume.setBounds(10, 10, 60, 20);
		
		jsVolume = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
		jsVolume.setBounds(78, 10, 60, 25);
		
		btTestar = new JButton("Testar");
		btTestar.setEnabled(true);
		btTestar.setBounds(158, 6, 90, 25);
		
		btAjusteVideo = new JButton("Ajustar Vídeo");	
		btAjusteVideo.setBounds(158, 38, 188, 25);
		btAjusteVideo.setEnabled(false);
		
		btVisualizar = new JButton("Visualizar");
		btVisualizar.setBounds(256, 6, 90, 25);
		btVisualizar.setEnabled(false);
		
		pBotoesVideo.add(lbTempo);
		pBotoesVideo.add(lbVolume);
		pBotoesVideo.add(jsVolume);
		pBotoesVideo.add(btTestar);
		pBotoesVideo.add(btAjusteVideo);
		pBotoesVideo.add(btVisualizar);
		
		btTestar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            	if (cap.iniciaFilmagem(System.getProperty("user.dir") + "/tmp/teste.asf")) {
            		jsVolume.setValue(0);
            		btTestar.setEnabled(false);
            		btVisualizar.setEnabled(false);
            		btAjusteVideo.setEnabled(true);
                	ThreadPreview tp = new ThreadPreview(cap, btTestar, btVisualizar, btAjusteVideo, lbTempo);
                	tp.start();
            	}
            	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });	
		
		btAjusteVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            	if (cap != null) {
            		AjusteVideo ajv = new AjusteVideo(cap);
            		ajv.setVisible(true);
            	}
            	else
            		JOptionPane.showMessageDialog(null, "Vídeo inexistente. Ajuste impossível.", "Atenção", JOptionPane.WARNING_MESSAGE);
            	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });	
		
		btVisualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            	btAjusteVideo.setEnabled(false);
            	btVisualizar.setEnabled(false);
            	btTestar.setEnabled(false);
            	cap.visualizaVideo(System.getProperty("user.dir") + "/tmp/teste.asf");
            	jsVolume.setValue(100); 
            	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            	ThreadVisualiza tv = new ThreadVisualiza(cap, btTestar, btVisualizar);
            	tv.start();
            }
        });
		
		jsVolume.addChangeListener(new ChangeListener() {				
			public void stateChanged(ChangeEvent e) {
				JSlider comp = (JSlider) e.getSource();
				if (!comp.getValueIsAdjusting()) {
					cap.setVolume(Float.valueOf(jsVolume.getValue())/100);					
				}
			}
		});		
	}	
	
	public void finalizaMedia() {
		if (cap != null) {
			cap.pararVideo();
			cap.finalizaAudio();
		}
	}
	
	public void finalizaFilmagem() {
		if (cap != null) {
			cap.finalizaFilmagem();
		}
	}
	
	public int getVisualizando() {
		if (cap != null)
			if (cap.isPlaying)
				return 1; // visualizando
			else if (cap.isRecording)			
				return 2; // gravando
			else
				return 0;
		else
			return 0; // parado
	}
}
