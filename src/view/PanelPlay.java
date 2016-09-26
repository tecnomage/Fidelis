package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.sql.Time;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import controller.BarraProgressaoPlayVideo;
import controller.Ferramentas;


@SuppressWarnings("serial")
public class PanelPlay extends JPanel implements ControllerListener {
	
		Player player = null;
		JPanel pDadosProc = null;
		JPanel p2 = null;
		JPanel pListagem = null;
		JPanel pNorte = null;
		JButton btStop = new JButton(new ImageIcon("C:\\Temp\\images\\Stop.png"));
		JButton btStart = new JButton (new ImageIcon("C:\\Temp\\images\\Start.png"));
		JButton btIrMarca = new JButton("Ir Para Marca");
		JButton btSair = new JButton("Sair");
		JSlider js = null;
		JLabel lbTime = null;
		Component comp1, comp2;
		Ferramentas tmFmt = null;
		private DefaultTableModel tablemodel;
		private JTable tbMarca;
		private JScrollPane sp;
		
		BarraProgressaoPlayVideo barraProg = null;
		
		PanelPlay(String fileName) {

			pDadosProc = new JPanel();
			//pDadosProc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
			
			p2 = new JPanel();
			p2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
			
			pListagem = new JPanel();
			//pListagem.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
			
			pNorte = new JPanel();
			pNorte.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
			
			this.setLayout(new BorderLayout());
			this.add(pDadosProc, BorderLayout.CENTER);
			this.add(p2, BorderLayout.SOUTH);
			this.add(pListagem, BorderLayout.EAST);
			this.add(pNorte, BorderLayout.NORTH);

			js = new JSlider(SwingConstants.HORIZONTAL, 0, 360, 0);
			js.setBounds(5, 200, 50, 15);

			tmFmt = new Ferramentas();
				
			lbTime = new JLabel(tmFmt.formataHora(0));
			lbTime.setBounds(5, 310, 100, 15);
			
			
			JLabel l = new JLabel("Processo: ");
			JComboBox cbAudiencias = new JComboBox();
			cbAudiencias.addItem("12345 2008 000 01 02 3");
			cbAudiencias.addItem("54321 2009 001 01 10 1");
			pNorte.add(l);
			pNorte.add(cbAudiencias);
			
			// Monta a JTable dos marcadores
			tablemodel = new DefaultTableModel();
			tablemodel.setColumnIdentifiers(new String[] {"Tempo", "Descricao"});		
			tbMarca = new JTable(tablemodel);
			tbMarca.setModel(tablemodel);		
			// Redimenciona o tamanho da coluna TEMPO
			javax.swing.table.TableColumnModel colmod = tbMarca.getColumnModel();
			colmod.getColumn(0).setMaxWidth(70);
	        sp = new JScrollPane(tbMarca);     
	        pListagem.add(sp);
			//pListagem.add(tbMarca);
	        
	        tablemodel.addRow(new String[] {null, null});
			tbMarca.setValueAt("00:00:10", 0, 0);
			tbMarca.setValueAt("DEPOIMENTO DO REU", 0, 1);

	        tablemodel.addRow(new String[] {null, null});
			tbMarca.setValueAt("00:00:30", 1, 0);
			tbMarca.setValueAt("ADVOGADO DA COMPANHIA", 1, 1);

	        tablemodel.addRow(new String[] {null, null});
			tbMarca.setValueAt("00:00:50", 2, 0);
			tbMarca.setValueAt("SENTENCA DO JUIZ", 2, 1);

			String mediaFile = fileName;
			MediaLocator ml = new MediaLocator(mediaFile);

			btStop.setToolTipText("Parar/Pausar");
			btStart.setToolTipText("Iniciar");
			p2.add(btStop);
			p2.add(btStart);
			p2.add(js);
			p2.add(lbTime);
			p2.add(btIrMarca);
			p2.add(btSair);

			try {
				System.out.print("Carregando video..");
				player = Manager.createRealizedPlayer(ml);
				//barraProg = new BarraProgressaoPlayVideo(player, js, lbTime);			
				System.out.println("pronto");
				player.addControllerListener(this);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				
				js.setMaximum((int) player.getDuration().getSeconds());	
				
				if ((comp1 = player.getVisualComponent()) != null)
					pDadosProc.add(comp1, BorderLayout.CENTER);
				
				
				//player.start();
				//barraProg.start();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			btStop.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	//GravaLog log = new GravaLog();
	            	//log.grava("FramePlayAudiencia(): Video Parado.");
	            	//log.fecha();
	            	player.stop();            	
	            }
	        });		
			
			btStart.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	if (!barraProg.isAlive()) {
	            		barraProg.start();
	            	}
	            	player.start();
	            	//GravaLog log = new GravaLog();
	            	//log.grava("FramePlayAudiencia(): Video Carregado.");
	            	//log.fecha();
	            }
	        });
			
			btSair.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	//GravaLog log = new GravaLog();
	            	//log.grava("FramePlayAudiencia(): Modulo Finalizado.");
	            	//log.fecha();
	            }
	        });
			
			
			btIrMarca.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	if (tbMarca.getSelectedRow() >= 0) {
	            		Ferramentas seg = new Ferramentas();
	            		js.setValue(seg.getSegundos((String) tbMarca.getValueAt(tbMarca.getSelectedRow(), 0)));
	            	}
	            	else {
	            		JOptionPane.showMessageDialog(null, "Selecione uma Marcação !!", "VideoCap", JOptionPane.WARNING_MESSAGE);            		
	            	}
	            }
	        });		
		
			
			js.addChangeListener(new ChangeListener() {				
				public void stateChanged(ChangeEvent e) {
					JSlider comp = (JSlider) e.getSource();
					if (!comp.getValueIsAdjusting()) {
						int tempoBarra = js.getValue();
						int tempoVideo = (int) player.getMediaTime().getSeconds();
						if (tempoBarra > tempoVideo + 2 || tempoBarra < tempoVideo - 2) {
							player.setMediaTime(new Time(new Double(js.getValue())));
							lbTime.setText(tmFmt.formataHora(js.getValue()));
						}
					}
				}
			});
			
		}
		
		public synchronized void controllerUpdate(ControllerEvent event) {		
			if (event instanceof EndOfMediaEvent) {
				player.stop();
				player.setMediaTime(new Time(0));
				js.setValue(0);
				validate();
			}		
		}
		
}
