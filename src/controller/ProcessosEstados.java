package controller;

import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.Player;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;

public class ProcessosEstados implements javax.media.ControllerListener {

	Player player = null;
	boolean configured = false;
	boolean realized = false;
	boolean failed = false;
	
	public ProcessosEstados(Player p) {
		player = p;
		p.addControllerListener(this);
	}
	
	public boolean realize(int timeOutMillis) {
		//long startTime = System.currentTimeMillis();
		int times = 0;
		synchronized (this) {
			player.realize();
			while (!realized && !failed) {
				try {
					wait(timeOutMillis);
					times++;
					if (times > 10)
						break;
				} 
				catch (InterruptedException ie) {
		    		GravaLog logErro = new GravaLog("ERRO");
		    		logErro.gravaExcep("fidelis.controller.ProcessosEstados.realize()", ie);
					return false;
				}
				//if (System.currentTimeMillis() - startTime > timeOutMillis)
				//	break;
			}
		}
		return realized;
	}
	
	public boolean configure(int timeOutMillis) {
		//long startTime = System.currentTimeMillis();
		int times = 0;
		synchronized (this) {
			if (player instanceof Processor)
				((Processor)player).configure();
			else
				return false;
			
			while (!configured && !failed) {
				try {
					wait(timeOutMillis);
					times++;
					if (times > 10)
						break;
				} 
				catch (InterruptedException ie) {
		    		GravaLog logErro = new GravaLog("ERRO");
		    		logErro.gravaExcep("fidelis.controller.Ferramentas.configure()", ie);
					return false;
				}
				//if (System.currentTimeMillis() - startTime > timeOutMillis)
				//	break;
			}
		}
		return configured;
	}
	
	@Override
	public void controllerUpdate(ControllerEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0 instanceof RealizeCompleteEvent) {
			realized = true;
		} else if (arg0 instanceof ConfigureCompleteEvent) {
			configured = true;
		} else if (arg0 instanceof ControllerErrorEvent) {
			failed = true;
		}
		//notifyAll();
	}	

}
