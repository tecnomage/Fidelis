package controller;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import view.Progress;

public class Transcricao extends Thread {
	
	private JButton button;
	
	public Transcricao(JButton b) {
		button = b;
	}
	
	public void run() {
    	//setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		button.setEnabled(false);
    	Progress wp = new Progress("Fase 1: Isolando �udio da Audi�ncia ...");
    	wp.setVisible(true);
		try {
			sleep(5000);
		} catch (InterruptedException e1) {					
			e1.printStackTrace();
		}
			
    	wp.setVisible(false);
    	wp.dispose();
    	
    	wp = new Progress("Fase 2: Transcrevendo �udio para arquivo de texto ...");
    	wp.setVisible(true);

		try {
			sleep(5000);
		} catch (InterruptedException e1) {					
			e1.printStackTrace();
		}

    	wp.setVisible(false);
    	wp.dispose();
    	//setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	
    	JOptionPane.showMessageDialog(null, "Transcri��o da Audi�ncia para Texto Conclu�da", "Transcri��o de Audi�ncia", JOptionPane.INFORMATION_MESSAGE);
    	
    	try {
			Runtime.getRuntime().exec("notepad.exe " + System.getProperty("user.dir") + "/images/Transcricao.txt");
		} catch (IOException e) {			
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao carregar o programa NOTEPAD.EXE!", "Transcri��o", JOptionPane.ERROR_MESSAGE);
		}
    	
    	button.setEnabled(true);
	}

}
