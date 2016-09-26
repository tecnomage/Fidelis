package view;

import java.util.Vector;

import de.humatic.dsj.DSCapture;
import de.humatic.dsj.DSEnvironment;
import de.humatic.dsj.DSFilter;
import de.humatic.dsj.DSFilterInfo;
import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSJUtils;

public class FileSinks implements java.beans.PropertyChangeListener {

	private DSFiltergraph graph;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static Vector dsjDetectaAudio() {
		DSFilterInfo[][] dsi = DSCapture.queryDevices(1);		
		Vector dsiRet = new Vector();
		
		for (int i = 0; i < dsi.length; i++) {			
			if (dsi[i][0].getType() == DSCapture.CaptureDevice.AUDIO) {
				dsiRet.addElement((DSFilterInfo)dsi[i][0]); 
			}			
		}
		return dsiRet;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static Vector dsjDetectaVideo() {
		DSFilterInfo[][] dsi = DSCapture.queryDevices(1);		
		Vector dsiRet = new Vector();
		
		for (int i = 0; i < dsi.length; i++) {			
			if (dsi[i][0].getType() == DSCapture.CaptureDevice.VIDEO) {
				dsiRet.addElement((DSFilterInfo)dsi[i][0]); 
			}			
		}
		return dsiRet;
	}

	
	
	
	
	
	@SuppressWarnings("static-access")
	public FileSinks() {
		DSFilterInfo[][] dsi = DSCapture.queryDevices(1);			
		
//		for (int i = 0; i < dsi.length; i++) {
//			System.out.println("Nome: " + dsi[i][0].getName());				
//		}

		//graph = new DSCapture(DSFiltergraph.DD7, dsi[0][0], false, dsi[1][0], null);
		

		graph = new DSCapture(DSFiltergraph.RENDER_NATIVE | DSFiltergraph.INIT_PAUSED,
							  DSFilterInfo.doNotRender(),
							  false,
							  dsi[1][0],
							  this
							  );
		
		int[] audioProps = graph.getAudioProperties();

		System.out.println("Samplerate: "+audioProps[0]+"\n"+
						   "SampleSize: "+audioProps[1]+"\n"+
						   "Channels: "+audioProps[2]);
		
		

		DSFilter[] filters = graph.listFilters();

		for (int i = 0; i < filters.length; i++) System.out.println(filters[i]);
		System.out.println("inserting between: "+filters[0].getName()+" & "+filters[1].getName());

		graph.insertSampleAccessFilter(filters[0], null, filters[1], 0);

		
		javax.swing.JFrame f = new javax.swing.JFrame("dsj - filesinks");
		//f.getContentPane().add(java.awt.BorderLayout.CENTER, graph.asComponent());
		//f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		//FileSink fileSink = null;
		//fileSink = new FileSink("/Temp/Sink2.asf");
		//fileSink.enablePreview();
        //int result = graph.connectSink(fileSink);
		
		((DSCapture) graph).setCaptureFile("/Temp/monitorTest.asf", DSFilterInfo.doNotRender(), DSFilterInfo.doNotRender(), true);
		
        ((DSCapture) graph).record();
        //((DSCapture) graph).setVolume(0);
		f.pack();
		
		System.out.println(graph.getDataSize());

		try{ Thread.currentThread().sleep(5000); } catch (Exception e){}

		System.out.println(graph.getInfo());
		
		graph.dispose();
		f.repaint();
		System.exit(0);

   	}



    public void propertyChange(java.beans.PropertyChangeEvent pe) {

		if (DSJUtils.getEventType(pe) == DSFiltergraph.DONE ||
			DSJUtils.getEventType(pe) == DSFiltergraph.EXPORT_DONE ||
			DSJUtils.getEventType(pe) == DSFiltergraph.SAVE_DONE) {}

	}

    public static void main(String args[]) {

		DSEnvironment.unlockDLL("jefferson@iostech.com.br", -693016, 1784737, 0);

    	@SuppressWarnings("unused")
		FileSinks fs = new FileSinks();
		//dsjDetectaAudio();

	}

}

