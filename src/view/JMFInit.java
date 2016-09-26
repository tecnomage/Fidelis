package view;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;
import javax.media.*;
import javax.media.Renderer;
import javax.media.format.AudioFormat;
import com.sun.media.ExclusiveUse;

@SuppressWarnings("serial")
public class JMFInit extends JFrame implements Runnable {
    private TextArea textBox;
    private boolean done = false;
    
	@SuppressWarnings("static-access")
	public JMFInit(String [] args) {
		super("JMF - Iniciando Dispositivos de Áudio/Vídeo");	
		createGUI();	

		Thread detectThread = new Thread(this);
		detectThread.run();
	
		int slept = 0;
		while (!done && slept < 30000) {
		    try {
			Thread.currentThread().sleep(500);
		    } catch (InterruptedException ie) {
		    }
		    slept += 500;
		}
		
		if (!done) {
		    message("Abortando detecção!");
		}
	
		try {
		    Thread.currentThread().sleep(2000);
		} catch (InterruptedException ie) {
		}
    }

    public void run() {
    	detectDirectAudio();
    	detectS8DirectAudio();
    	detectCaptureDevices();
    	done = true;
    }
   
    
    @SuppressWarnings("unchecked")
	private void detectCaptureDevices() {
		// check if JavaSound capture is available
		message("Procurando hardware de captura de audio");
		Class dsauto = null;
		try { 
		    dsauto = Class.forName("DirectSoundAuto");
		    dsauto.newInstance();
		    message("Finalizando a detecção do DirectSound capturer");
		} catch (ThreadDeath td) {
		    throw td;
		} catch (Throwable t ) {
		}
	
		Class jsauto = null;
		try { 
		    jsauto = Class.forName("JavaSoundAuto");
		    jsauto.newInstance();
		    message("Finalizando a detecção do javasound capturer");
		} catch (ThreadDeath td) {
		    throw td;
		} catch (Throwable t ) {
		    message("Falha na detecção do JavaSound capturer!");
		}
	
		// Check if VFWAuto or SunVideoAuto is available
		message("Procurando por dispositivos de captura de video");
		message("Por favor, aguarde um instante..");
		Class auto = null;
	    Class autoPlus = null;
		try {
		    auto = Class.forName("VFWAuto");
		} catch (Exception e) {
		}
		if (auto == null) {
		    try {
			auto = Class.forName("SunVideoAuto");
		    } catch (Exception ee) {
		    }
		    try {
		   	   autoPlus = Class.forName("SunVideoPlusAuto");
		    }
		    catch (Exception ee) {
		    }
		}
		
		try {
		    //@SuppressWarnings("unused")
			@SuppressWarnings("unused")
			Object instance = auto.newInstance();
	            if (autoPlus != null) {
	                //@SuppressWarnings("unused")
					@SuppressWarnings("unused")
					Object instancePlus = autoPlus.newInstance();
	            }
		    message("Finalizando a detecção de dispositivos de captura de video");
		} catch (ThreadDeath td) {
		    throw td;
		} catch (Throwable t) {
		    message("Falha na detecção de dispositivos de video!");
		}
		
    }

    @SuppressWarnings("unchecked")
	private void detectDirectAudio() {
		Class cls;
		int plType = PlugInManager.RENDERER;
		String dar = "com.sun.media.renderer.audio.DirectAudioRenderer";
		try {
		    // Check if this is the Windows Performance Pack - hack
		    cls = Class.forName("VFWAuto");
		    // Check if DS capture is supported, otherwise fail DS renderer
		    // since NT doesn't have capture
		    cls = Class.forName("com.sun.media.protocol.dsound.DSound");
		    // Find the renderer class and instantiate it.
		    cls = Class.forName(dar);
		    
		    Renderer rend = (Renderer) cls.newInstance();
		    try {
			// Set the format and open the device
			AudioFormat af = new AudioFormat(AudioFormat.LINEAR,
							 44100, 16, 2);
			rend.setInputFormat(af);
			rend.open();
			Format [] inputFormats = rend.getSupportedInputFormats();
			// Register the device
			PlugInManager.addPlugIn(dar, inputFormats, new Format[0],
						plType);
			// Move it to the top of the list
			Vector rendList =
			    PlugInManager.getPlugInList(null, null, plType);
			int listSize = rendList.size();
			if (rendList.elementAt(listSize - 1).equals(dar)) {
			    rendList.removeElementAt(listSize - 1);
			    rendList.insertElementAt(dar, 0);
			    PlugInManager.setPlugInList(rendList, plType);
			    PlugInManager.commit();
			    //System.err.println("registered");
			}
			rend.close();
		    } catch (Throwable t) {
			//System.err.println("Error " + t);
		    }
		} catch (Throwable tt) {
		}
    }

    @SuppressWarnings("unchecked")
	private void detectS8DirectAudio() {
		Class cls;
		int plType = PlugInManager.RENDERER;
		String dar = "com.sun.media.renderer.audio.DirectAudioRenderer";
		try {
		    // Check if this is the solaris Performance Pack - hack
		    cls = Class.forName("SunVideoAuto");
	
		    // Find the renderer class and instantiate it.
		    cls = Class.forName(dar);
		    
		    Renderer rend = (Renderer) cls.newInstance();
	
		    if ( rend instanceof ExclusiveUse &&
	                 !((ExclusiveUse)rend).isExclusive()) {
				// sol8+, DAR supports mixing
				Vector rendList = PlugInManager.getPlugInList(null, null, plType);
				int listSize = rendList.size();
				boolean found = false;
				String rname = null;
		
				for ( int i = 0; i < listSize; i++) {
				    rname = (String)(rendList.elementAt(i));
				    if ( rname.equals(dar) ) { // DAR is in the registry
					found = true;
					rendList.removeElementAt(i);
					break;
				    }
				}
				
				if ( found ) {
				    rendList.insertElementAt(dar, 0);
				    PlugInManager.setPlugInList(rendList, plType);
				    PlugInManager.commit();
				}
		    }
		} catch (Throwable tt) {
		}
    }

    private void message(String mesg) {
    	textBox.append(mesg + "\n");
    }

    private void createGUI() {
		textBox = new TextArea(5, 50);
		add("Center", textBox);
		textBox.setEditable(false);
		addNotify();
		setVisible(true);
		pack();
    }
    
    public static void main(String [] args) {
		JMFInit init = new JMFInit(args);		
		init.setVisible(false);
		init.dispose();
	}
}

