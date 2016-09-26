package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.sql.Time;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Ferramentas;

@SuppressWarnings("serial")
public class PlayVideo extends JFrame implements ControllerListener {	
	Player player = null;
	JPanel pDadosProc = null;
	JPanel p2 = null;
	JButton btStop = new JButton(new ImageIcon("C:\\Users\\s3curity\\Desktop\\VideoCap\\workspace\\VideoCap\\src\\images\\Stop.png"));
	JButton btStart = new JButton (new ImageIcon("C:\\Users\\s3curity\\Desktop\\VideoCap\\workspace\\VideoCap\\src\\images\\Start.png"));
	JSlider js = null;
	JLabel lbTime = null;
	Component comp1, comp2;
	Ferramentas tmFmt = null;
	
	PlayVideo(String fileName) {
		setTitle("Videocap - PlayVideo");
		setSize(520, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		Container cont = getContentPane();
		cont.setLayout(new BorderLayout());

		pDadosProc = new JPanel();
		pDadosProc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		
		p2 = new JPanel();
		p2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		
		cont.add(pDadosProc, BorderLayout.CENTER);
		cont.add(p2, BorderLayout.SOUTH);

		js = new JSlider(SwingConstants.HORIZONTAL, 0, 360, 0);
		js.setBounds(5, 200, 50, 15);

		tmFmt = new Ferramentas();
			
		lbTime = new JLabel(tmFmt.formataHora(0));
		lbTime.setBounds(5, 310, 100, 15);

		String mediaFile = fileName;
		MediaLocator ml = new MediaLocator(mediaFile);

		btStop.setToolTipText("Parar/Pausar");
		btStart.setToolTipText("Iniciar");
		p2.add(btStop);
		p2.add(btStart);
		p2.add(js);
		p2.add(lbTime);

		try {
			System.out.print("Carregando video..");
			player = Manager.createRealizedPlayer(ml);
			//BarraProgressaoPlayVideo barraProg = new BarraProgressaoPlayVideo(player, js, lbTime);			
			player.addControllerListener(this);
			System.out.println("pronto");
			
			js.setMaximum((int) player.getDuration().getSeconds());	
			
			if ((comp1 = player.getVisualComponent()) != null)
				pDadosProc.add(comp1, BorderLayout.CENTER);
			
			player.start();
			//barraProg.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		btStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	player.stop();            	
            }
        });		
		
		btStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	player.start();            
            }
        });		
		
		js.addChangeListener(new ChangeListener() {				
			public void stateChanged(ChangeEvent e) {
				JSlider comp = (JSlider) e.getSource();
				if (!comp.getValueIsAdjusting()) {
					int tempoBarra = js.getValue();
					int tempoVideo = (int) player.getMediaTime().getSeconds();
					if (tempoBarra > tempoVideo + 5 || tempoBarra < tempoVideo - 5) {
						player.setMediaTime(new Time(new Double(js.getValue())));
						lbTime.setText(tmFmt.formataHora(js.getValue()));
					}
				}
			}
		});
		
		pack();
	}
	
	public synchronized void controllerUpdate(ControllerEvent event) {		
		if (event instanceof EndOfMediaEvent) {
			player.setMediaTime(new Time(0));
			player.stop();
			js.setValue(0);
			validate();
		}		
	}

	public static void main(String [] args) {
		PlayVideo gv = new PlayVideo("file://C:/Temp/limerick_180x120x56k_q45_11KHz.avi");
		gv.setVisible(true);		
	}
	
}
