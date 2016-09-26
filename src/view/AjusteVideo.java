package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;

import controller.DSJCaptura;

@SuppressWarnings("serial")
public class AjusteVideo extends JDialog {
	
	private JSlider jsContraste;
	private JSlider jsBrilho;
	private JSlider jsCor;
	
	public AjusteVideo(DSJCaptura c) {
		jsContraste = (JSlider) c.getComponenteContraste();
		jsBrilho = (JSlider) c.getComponenteBrilho();
		jsCor = (JSlider) c.getComponenteCor();
		
		JPanel pPrincipal = new JPanel();
		pPrincipal.setLayout(new GridLayout(3, 1, 5, 5));
		
		JPanel pContraste = new JPanel();
		pContraste.setBorder(javax.swing.BorderFactory.createTitledBorder("Contraste: "));
		pContraste.add(jsContraste);
		pPrincipal.add(pContraste);
		
		JPanel pBrilho = new JPanel();
		pBrilho.setBorder(javax.swing.BorderFactory.createTitledBorder("Brilho: "));
		pBrilho.add(jsBrilho);
		pPrincipal.add(pBrilho);
				
		JPanel pCor = new JPanel();
		pCor.setBorder(javax.swing.BorderFactory.createTitledBorder("Cor: "));
		pCor.add(jsCor);
		pPrincipal.add(pCor);
		
		this.add(pPrincipal);
		this.pack();
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		this.setTitle("Ajuste de Vídeo");
		
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
        this.setLocation((tela.width - this.getSize().width) / 2, (tela.height - this.getSize().height) / 2); 
	}

}
