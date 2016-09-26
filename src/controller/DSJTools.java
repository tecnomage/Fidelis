package controller;

import java.util.Vector;

import de.humatic.dsj.DSCapture;
import de.humatic.dsj.DSEnvironment;
import de.humatic.dsj.DSFilterInfo;

public class DSJTools {

	public DSJTools() {
		DSEnvironment.unlockDLL("jefferson@iostech.com.br", -693016, 1784737, 0);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public Vector dsjDetectaAudio() {
		DSFilterInfo[][] dsi = DSCapture.queryDevices();
		Vector dsiRet = new Vector();

		for (int i = 0; i < dsi.length; i++) {
			if (dsi[i][0].getType() == DSCapture.CaptureDevice.AUDIO) {
				dsiRet.addElement(dsi[i][0].getName());
			}
		}
		dsiRet.addElement((String) "nenhum");
		return dsiRet;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public Vector dsjDetectaVideo() {
		DSFilterInfo[][] dsi = DSCapture.queryDevices();
		Vector dsiRet = new Vector();

		for (int i = 0; i < dsi.length; i++) {
			if (dsi[i][0].getType() == DSCapture.CaptureDevice.VIDEO) {
				dsiRet.addElement(dsi[i][0].getName());
			}
		}
		dsiRet.addElement((String) "nenhum");
		return dsiRet;
	}

	@SuppressWarnings({ "deprecation", "restriction" })
	public DSFilterInfo getDevVideo(String strNomeCam) {
		DSFilterInfo[][] dsi = DSCapture.queryDevices(1);
		DSFilterInfo dsiRet = null;

		for (int i = 0; i < dsi.length; i++) {
			if (dsi[i][0].getType() == DSCapture.CaptureDevice.VIDEO && dsi[i][0].getName().equals(strNomeCam)) {
				dsiRet = dsi[i][0];
			}
		}

		return dsiRet;
	}

	@SuppressWarnings("deprecation")
	public DSFilterInfo getDevAudio(String strNomeMic) {
		DSFilterInfo[][] dsi = DSCapture.queryDevices(1);
		DSFilterInfo dsiRet = null;

		for (int i = 0; i < dsi.length; i++) {
			if (dsi[i][0].getType() == DSCapture.CaptureDevice.AUDIO && dsi[i][0].getName().equals(strNomeMic)) {
				dsiRet = dsi[i][0];
			}
		}

		return dsiRet;
	}
}
