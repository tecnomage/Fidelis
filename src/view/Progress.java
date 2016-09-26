package view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class Progress extends JDialog {   
	
    public Progress(String strMsg) {
    	setTitle("Por Favor, aguarde ...");
    	setSize(450, 130);
        JPanel pCentro = new JPanel();
        //pCentro.setBackground(Color.white);
        
        JProgressBar progress = new JProgressBar();   
        progress.setIndeterminate(true);
        progress.setBounds(80, 40, 280, 20);
        
        JLabel lb = new JLabel(strMsg);    
        lb.setBounds(80, 10, 320, 15);
        
        JLabel img = new JLabel(new ImageIcon("images/relogio.gif"));
        img.setBounds(20, 10, 25, 25);

        pCentro.setLayout(null);
        pCentro.add(lb);
        pCentro.add(progress);
        pCentro.add(img);
        this.add(pCentro);
        
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
        this.setLocation((tela.width - this.getSize().width) / 2,   
             (tela.height - this.getSize().height) / 2);   
        
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    }   
} 