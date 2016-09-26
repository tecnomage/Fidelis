package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.text.Format;
import java.util.Vector;

import javax.annotation.processing.Processor;
import javax.sound.sampled.AudioFormat;
import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CapturaVideo extends JFrame implements ControllerListener, DataSinkListener {	
	
	Player player = null;
	MediaLocator ml = null;
	CaptureDeviceInfo webCamDeviceInfo = null;
	Container cont = null;
	DataSink sink = null;
	DataSink sink2 = null;
	JPanel vi = null;
	JPanel pa = null;
	JPanel norte = null;
	DataSource ds = null;
	Processor procGrava = null;
	Processor procMain = null;
	Processor procAudio = null;
	JComboBox cbAudiencias = null;
	
	String outputFileName = null;
	
	DataSource dsCloneable = null;
	DataSource dsClone1 = null;
	DataSource dsClone2 = null;
	
	CapturaVideo(String fileName, String numProcesso) {
		setTitle("Videocap: Filmador");
		setSize(350, 325);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		cont = getContentPane();
		cont.setLayout(new BorderLayout());
		
		outputFileName = fileName;
		
		vi = new JPanel();
		pa = new JPanel();
		norte = new JPanel();
		final JButton btIniciar = new JButton("Filmar");
		final JButton btParar = new JButton("Concluir");
		final JButton btGravar = new JButton("Gravar");
		final JButton btSair = new JButton("Sair");
		btParar.setEnabled(false);
		
		JLabel l = new JLabel("Processo Nr.: ");
		cbAudiencias = new JComboBox();
		cbAudiencias.addItem(numProcesso);
		norte.add(l);
		norte.add(cbAudiencias);
		
		pa.add(btIniciar);
		pa.add(btGravar);
		pa.add(btParar);
		pa.add(btSair);
		cont.add(norte, BorderLayout.NORTH);
		cont.add(vi, BorderLayout.CENTER);
		cont.add(pa, BorderLayout.SOUTH);

		btIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	//ligaCamera();
            	btIniciar.setEnabled(false);
            }
        });		
		
		btParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	paraTudo();           
            	btParar.setEnabled(false);
            }
        });		
		
		btGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	gravaVideo();   
            	btGravar.setEnabled(false);
            	btParar.setEnabled(true);
            }
        });		
		
		btSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	paraTudo();
            	setVisible(false);
            	dispose();
            }
        });		

	}
	
	public void paraTudo() {
		if (procGrava != null) {			
			synchronized(this) {      		
				try {
					sink.stop();
					sink2.stop();
					wait(1000);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			procGrava.close();
			procAudio.close();
			System.out.println("paraTudo(): Fim da gravacao ...");
		}
		else {
			if (player != null) {
				synchronized(this) {
					player.stop();
					try {
						wait(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
				
				player.close();
				player.deallocate();
				System.out.println("paraTudo(): Player finalizado ...");
			}
		}
	}
	
/*	public void ligaCamera() {
		
		if (ds == null) {
			webCamDeviceInfo = detectaCameras();
			
			if (webCamDeviceInfo != null) {
				ml = webCamDeviceInfo.getLocator();	
			}
			try {
				ds = Manager.createDataSource(ml);
			 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (ds != null) {
			try {								
				procMain = Manager.createProcessor(procGrava.getDataOutput());
				synchronized(this) {
					procMain.configure();
					wait(1000);
				}

	
				////player = Manager.createPlayer(ds);
	            procMain.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.RAW));	            
				TrackControl track[] = procMain.getTrackControls();
				for (int i = 0; i < track.length; i++) {
					if (track[i] instanceof FormatControl) {	
						Dimension vSize = new Dimension(320, 240);
						track[i].setFormat(new VideoFormat(VideoFormat.RGB, vSize, Format.NOT_SPECIFIED, null, Format.NOT_SPECIFIED));
					}
				} 				
	            synchronized(this) {
	            	procMain.realize();	            	
	            	wait(1000);
	            }	
	            	            
	            player = Manager.createRealizedPlayer(procMain.getDataOutput());
	            
	            Component visualComponent = player.getVisualComponent();
	            if (visualComponent != null) {
	                vi.add (visualComponent, BorderLayout.CENTER);
	            }
	            else {
	            	System.out.println("ligaCamera(): VisualComponent retornou nulo ...");
	            }
	         
	            player.start();
	            //procMain.start();
	            pack();
	            //vi.updateUI();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("ligaCamera(): Nao foi possivel ligar a camera ...");
		}
	}
*/	
	
	public void teste() {
		
	}
	
	
	public void gravaVideo() {
		
		DataSource [] dsAudioVideo = new DataSource [2];
		
		webCamDeviceInfo = detectaCameras();		
		
		ml = webCamDeviceInfo.getLocator();
		try {
			dsAudioVideo[0] = Manager.createDataSource(ml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CaptureDeviceInfo webAudioDeviceInfo = detectaAudio();
		try {
			 dsAudioVideo[1] = Manager.createDataSource(webAudioDeviceInfo.getLocator());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		

		try {
			ds = Manager.createMergingDataSource(dsAudioVideo);
		} catch (IncompatibleSourceException e1) {
			e1.printStackTrace();
		}

		try {

			dsCloneable = Manager.createCloneableDataSource(ds);
			dsClone1 = ((SourceCloneable) dsCloneable).createClone();
			dsClone2 = ((SourceCloneable) dsCloneable).createClone();
			
			procGrava = Manager.createProcessor(dsCloneable);
			
			procAudio = Manager.createProcessor(dsClone2);

			player = Manager.createPlayer(dsClone1);
			synchronized(this) {
				player.realize();
				wait(1000);
			}
            
            Component visualComponent = player.getVisualComponent();
            if (visualComponent != null) {
                vi.add (visualComponent, BorderLayout.CENTER);
            }
            else {
            	System.out.println("ligaCamera(): VisualComponent retornou nulo ...");
            }

            synchronized(this) {
            	procGrava.configure();
            	wait(2000);
            }

            synchronized(this) {
            	procAudio.configure();
            	wait(2000);
            }

            	            
            procGrava.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.MSVIDEO));	            
			TrackControl track[] = procGrava.getTrackControls();
			
			for (int i = 0; i < track.length; i++) {
				if (track[i].getFormat() instanceof VideoFormat) {	
					Dimension vSize = new Dimension(320, 240);
					//track[i].setFormat(new VideoFormat(VideoFormat.MJPG));
					track[i].setFormat(new VideoFormat(VideoFormat.INDEO50, vSize, Format.NOT_SPECIFIED, null, Format.NOT_SPECIFIED));
					System.out.println("gravaVideo(): Video Formato configurado ...");
				}
				if (track[i].getFormat() instanceof AudioFormat) {
					track[i].setFormat(new AudioFormat(AudioFormat.LINEAR, 8000, 16, 2));
					System.out.println("gravaVideo(): Audio Formato configurado ...");
				}
			}
			
			procAudio.setContentDescriptor(new ContentDescriptor(FileTypeDescriptor.WAVE));	            
			TrackControl trackAudio[] = procAudio.getTrackControls();
			
			for (int i = 0; i < trackAudio.length; i++) {
				if (trackAudio[i].getFormat() instanceof AudioFormat) {
					trackAudio[i].setFormat(new AudioFormat(AudioFormat.LINEAR, 8000, 16, 2));
					System.out.println("gravaVideo(): Wave Formato configurado ...");
				}
				else {
					trackAudio[i].setEnabled(false);
				}
			}
			
            synchronized(this) {
            	procGrava.realize();
            	wait(2000);
            }	

            synchronized(this) {
            	procAudio.realize();
            	wait(2000);
            }	

            outputFileName = "file://" + outputFileName + cbAudiencias.getSelectedItem().toString() + ".avi";
            	            
            MediaLocator dest = new MediaLocator(outputFileName);	         
            try {
            	sink = Manager.createDataSink(procGrava.getDataOutput(), dest);
	            sink.open();
	            sink.start();		           
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }
            
            String outputFileName2 = "file://c:/temp/"+ cbAudiencias.getSelectedItem().toString() + ".wav";
            
            MediaLocator destAudio = new MediaLocator(outputFileName2);	         
            try {
            	sink2 = Manager.createDataSink(procAudio.getDataOutput(), destAudio);
	            sink2.open();
	            sink2.start();
            } 
            catch (Exception e) {
            	e.printStackTrace();
            }		 

            
            procGrava.start();
            procAudio.start();
            player.start();
            
    		procGrava.setMediaTime(player.getMediaTime());
    		System.out.println("mediatime set up ...");
            
            
            GainControl gc = player.getGainControl();
            if (gc == null) {
            	System.out.println("GainControl eh nulo ....");
            }
            else {
            	gc.setMute(true);
            }
            	
            pack();            
            
            System.out.println("gravaVideo(): Gravando ...");
		}
		catch (Exception e) {
			System.err.println("gravaVideo(): Erro ao iniciar gravacao do video ...");
			e.printStackTrace();
		}
	}
		
	
		
    @SuppressWarnings("unchecked")
	public CaptureDeviceInfo detectaCameras() {
    	
        Vector lista = CaptureDeviceManager.getDeviceList(null);
        CaptureDeviceInfo devInfo = null;
 
        if (lista != null) {
            String nome;
             for (int i=0; i<lista.size(); i++ ) {
                devInfo = (CaptureDeviceInfo) lista.elementAt(i);
                nome = devInfo.getName();
                
                System.out.println(nome.toString());
 
                if (nome.startsWith ("vfw:")) {
                    break;
                }
            } 
        }
        return (devInfo);        
    }

    @SuppressWarnings("unchecked")
	public static CaptureDeviceInfo detectaAudio() {
    	
        Vector lista = CaptureDeviceManager.getDeviceList(null);
        CaptureDeviceInfo devInfo = null;
 
        if (lista != null) {
             for (int i=0; i<lista.size(); i++ ) {
                devInfo = (CaptureDeviceInfo) lista.elementAt(i);
                if (devInfo.getName().startsWith("JavaSound")) {
                	System.out.println("Dispositivo de audio selecionado: " + devInfo.getName());
                	break;
                }                
            } 
        }
        return (devInfo);        
    } 
    
    
	public static void main(String [] args) {
		CapturaVideo gv = new CapturaVideo("c:/Temp/", "video");
		gv.setVisible(true);	
	}

	@Override
	public void controllerUpdate(ControllerEvent arg0) {
		// TODO Auto-generated method stub
		
		procGrava.setMediaTime(player.getMediaTime());
		System.out.println("mediatime set up ...");
		
	}

	@Override
	public void dataSinkUpdate(DataSinkEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
