package controller;

import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.humatic.dsj.CaptureDeviceControls;
import de.humatic.dsj.DSCapture;
import de.humatic.dsj.DSEnvironment;
import de.humatic.dsj.DSFilterInfo;
import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSJException;
import de.humatic.dsj.DSJUtils;
import de.humatic.dsj.DSMediaType;
import de.humatic.dsj.DSMovie;
import de.humatic.dsj.sink.FileSink;

public class DSJCaptura implements java.beans.PropertyChangeListener {
	
	private DSFiltergraph graph;
	private JPanel pVideo;
	public boolean isPlaying = false;
	public boolean isMicAtivo = false;
	public boolean isRecording = false;
	
	public DSJCaptura(JPanel p, DSFiltergraph dsfg) {
		graph = dsfg;
		pVideo = p;
		DSEnvironment.unlockDLL("jefferson@iostech.com.br", -693016, 1784737, 0);
	}
		
	public boolean iniciaFilmagem(String strNomeVideo) {	
		try {
			
			if (Sessao.nomeDispositivoVideo.equals("nenhum")) {
				JOptionPane.showMessageDialog(null, "Nenhum dispositivo de vídeo foi selecionado. Não é possível prosseguir com a filmagem.","DSJCaptura.iniciaFilmagem()", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (Sessao.nomeDispositivoAudio.equals("nenhum")) {
				JOptionPane.showMessageDialog(null, "Nenhum dispositivo de áudio foi selecionado. Não é possível prosseguir com a filmagem.","DSJCaptura.iniciaFilmagem()", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			DSJTools dsjt = new DSJTools();
			DSFilterInfo dsf = dsjt.getDevVideo(Sessao.nomeDispositivoVideo);
			
			DSMediaType[] mf = dsf.getDownstreamPins()[0].getFormats();
			for (int i = 0; i < mf.length; i++) {
				if (mf[i].getDisplayString().contains("320"))
					dsf.getDownstreamPins()[0].setPreferredFormat(i);
			}
			
			graph = new DSCapture(DSFiltergraph.DD7 | DSFiltergraph.INIT_PAUSED, dsf, false, dsjt.getDevAudio(Sessao.nomeDispositivoAudio), this);

			/*
			DSFilter[] filters = graph.listFilters();
			int idx1 = -1, idx2 = -1;			
			for (int i = 0; i < filters.length; i++) {
				if (filters[i].toString().equals(Sessao.nomeDispositivoAudio))
					idx1 = i;
				
				if (filters[i].toString().startsWith("Smart Tee") && idx1 > -1)
					idx2 = i;
			}
			graph.insertSampleAccessFilter(filters[idx1], null, filters[idx2], 0);
			*/
			
			pVideo.add(java.awt.BorderLayout.CENTER, graph.asComponent()); 
			
			FileSink fileSink = new FileSink(strNomeVideo);
	    	//FIXME alterado de uma versão da biblio para outra
			fileSink.enablePreview();
	    	
	    	//try { Thread.sleep(1000); } catch (InterruptedException ex) { }
	
	        int result = graph.connectSink(fileSink);
	    	//graph.connectSink(fileSink);
	        
	        if (result == de.humatic.dsj.sink.Sink.CONTROLABLE) {
	        	((DSCapture) graph).record();        
	        	pVideo.updateUI();    
	        	isRecording = true;
	        }
	        else {
	        	JOptionPane.showMessageDialog(null, "O Dispositivo escolhido não pode ser controlado.", "Detecção de Dispositivos", JOptionPane.ERROR_MESSAGE);
	        }
	        return true;
		}
		catch (Exception e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("DSJCaptura.finalizaFilmagem()", e);
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao iniciar a captura do vídeo.\nPor favor verifique a log de erros.","DSJCaptura.iniciaFilmagem()", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}	
	
	public void finalizaFilmagem() {		
		try {
			((DSCapture)graph).setPreview();
			
			if (graph != null) {
				graph.dispose();
			}
			
			pVideo.updateUI();
			isRecording = false;
		}
		catch (Exception e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("DSJCaptura.finalizaFilmagem()", e);
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar a captura do vídeo.\nPor favor verifique a log de erros.","DSJCaptura.finalizaFilmagem()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public JComponent getComponenteBrilho() {
		if (graph != null)
			return ((DSCapture)graph).getActiveVideoDevice().getControls().getController(CaptureDeviceControls.BRIGHTNESS, 0, false);
		else
			return null;
	}

	public JComponent getComponenteContraste() {
		if (graph != null)
			return ((DSCapture)graph).getActiveVideoDevice().getControls().getController(CaptureDeviceControls.CONTRAST, 0, false);
		else
			return null;
	}

	public JComponent getComponenteCor() {
		if (graph != null)
			return ((DSCapture)graph).getActiveVideoDevice().getControls().getController(CaptureDeviceControls.SATURATION, 0, false);
		else
			return null;
	}
	
	public void propertyChange(PropertyChangeEvent pe) {
		if (DSJUtils.getEventType(pe) == DSFiltergraph.DONE && isPlaying) {
			isPlaying = false;	
			pVideo.remove(graph.asComponent());
			graph.dispose();
			pVideo.updateUI();
		}

		//System.out.println("code: " + DSJUtils.getEventType(pe));
		
		//if (Integer.valueOf(pe.getNewValue().toString()).intValue() == DSFiltergraph.SAMPLE_BUFFER_FILLED) {
		//if (DSJUtils.getEventType(pe) == DSFiltergraph.SAMPLE_BUFFER_FILLED) {
			//DSSampleBuffer buffer = (DSSampleBuffer)(pe.getOldValue());
			//if (buffer.getSampleLength() > 44100)
		//	isMicAtivo = true;
		//}
		//else
		//	isMicAtivo = false;
	}
	
	public void setVolume(float vlr) {
		if (graph != null && graph.getActive())
			graph.setVolume(vlr);
	}
	
	public float getVolume() {
		if (graph != null && graph.getActive())
			return (graph.getVolume());
		else
			return 0;
	}
	
	public int getSegundos() {
		if (graph != null && graph.getActive()) {
			return (graph.getTime() / 1000);
		}
		else
			return 0;		
	}
	
	public long getMiliSeg() {
		if (graph != null && graph.getActive())
			return graph.getTime();
		else
			return 0;
	}
	
	public void setTempo(int vlr) {
		if (graph != null && graph.getActive()) {
			graph.setTimeValue(vlr * 1000);
		}
	}
	
	public int getTempoTotal() {
		if (graph != null && graph.getActive()) {
			return (graph.getDuration() / 1000);
		}
		else
			return 0;
	}
	
	public int getMiliTempoTotal() {
		if (graph != null && graph.getActive())
			return graph.getDuration();
		else
			return 0;
	}

	
	public void visualizaVideo(String strNomeVideo) {
		try {
			graph = new DSMovie(strNomeVideo, DSFiltergraph.DD7 | DSMovie.INIT_PAUSED, this);		
			pVideo.add(java.awt.BorderLayout.CENTER, graph.asComponent());
			isPlaying = true;
			graph.play();
	       	pVideo.updateUI();
		}
		catch(Exception e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("DSJCaptura.visualizaVideo()", e);
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao visualizar o vídeo.\nPor favor verifique a log de erros.","DSJCaptura.visualizaVideo()", JOptionPane.ERROR_MESSAGE);
		}
        return;
	}
	
	public void pararVideo() {
		try {
			if (graph != null && graph.getActive())
				graph.stop();
		}
		catch (Exception e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("DSJCaptura.pararVideo()", e);
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao parar a visualização.\nPor favor verifique a log de erros.","DSJCaptura.pararAudio()", JOptionPane.ERROR_MESSAGE);	
		}
        return;
	}
	
	public void play() {
		try {
			graph.play();
		}
		catch (Exception e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("DSJCaptura.play()", e);
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao iniciar a visualização.\nPor favor verifique a log de erros.","DSJCaptura.play()", JOptionPane.ERROR_MESSAGE);	
		}
        return;
	}
	
	public boolean gravaAudio(String strNomeAudio) {	
		try {
			DSJTools dsjt = new DSJTools();
			
			graph = new DSCapture(DSFiltergraph.DD7 | DSFiltergraph.INIT_PAUSED,
					  DSFilterInfo.doNotRender(),
					  false,
					  dsjt.getDevAudio(Sessao.nomeDispositivoAudio),
					  this);
			

			/*
			DSFilter[] filters = graph.listFilters();
			for (int i = 0; i < filters.length; i++)
				System.out.println(filters[i]);
			graph.insertSampleAccessFilter(filters[1], null, filters[2], 0);
			*/
			
			
			/*
			DSFilter[] filters = graph.listFilters();
			int idx1 = -1, idx2 = -1;			
			for (int i = 0; i < filters.length; i++) {
				if (filters[i].toString().equals(Sessao.nomeDispositivoAudio))
					idx1 = i;
				
				if (filters[i].toString().startsWith("Smart Tee") && idx1 > -1)
					idx2 = i;
			}
			graph.insertSampleAccessFilter(filters[idx1], null, filters[idx2], 0);
			*/
			
			((DSCapture) graph).setCaptureFile(strNomeAudio, DSFilterInfo.doNotRender(), DSFilterInfo.doNotRender(), true);
			((DSCapture) graph).record();
			((DSCapture) graph).setVolume(0);
			return true;
		}
		catch (DSJException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("DSJCaptura.gravaAudio()", e);
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao iniciar a captura de áudio.\nPor favor verifique a log de erros.","DSJCaptura.gravaAudio()", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public void finalizaAudio() {		
		try {
			if (graph != null) {
				graph.dispose();
			}
		}
		catch (Exception e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("DSJCaptura.finalizaAudio()", e);
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar a captura do áudio.\nPor favor verifique a log de erros.","DSJCaptura.finalizaAudio()", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public DSCapture getDSCaptura() {
		return (DSCapture) graph;
	}

	public boolean getIsShowing() {
		return graph.isShowing();
	}
}
