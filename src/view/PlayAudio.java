package view;

import java.util.Vector;

import javax.sound.sampled.AudioFormat;

public class PlayAudio {
	
	@SuppressWarnings("unchecked")
	public static void main (String [] args) {
		CaptureDeviceInfo di = null;
		Vector deviceList;
		
		try {
			deviceList = CaptureDeviceManager.getDeviceList(new AudioFormat("LINEAR", 32000, 16, 2));
			//deviceList = CaptureDeviceManager.getDeviceList(new VideoFormat(null));
			for (int i = 0; i < deviceList.size(); i++) {
				di = (CaptureDeviceInfo)deviceList.elementAt(i);
				System.out.println("Devicename: " + di.getName());
			}
			System.out.println("Fim da lista");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
//	try{
//		Player p = Manager.createPlayer(di.getLocator());
//		p.start();
//	}  catch (NoPlayerException ex) {ex.printStackTrace();}
//	catch (IOException e) {e.printStackTrace();}
//	}
	}
}
