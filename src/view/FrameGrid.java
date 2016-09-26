package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FrameGrid extends JDialog {

	public FrameGrid(String strUsuario, String strTabela) {
		this.setTitle("Manutenção de Tabelas - Grid");
		this.setSize(830, 650);
		this.setModalityType(DEFAULT_MODALITY_TYPE);
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
        this.setLocation((tela.width - this.getSize().width), (tela.height - this.getSize().height) / 2);        
        
        PanelGridManutencaoGenerica pCentro = new PanelGridManutencaoGenerica(strUsuario, strTabela);
        pCentro.setBounds(1, 5, 799, 570);        
                
        JButton btSair = new JButton("Sair");
        btSair.setBounds(375, 580, 60, 25);
        btSair.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				dispose();								
			}
		});
        
        JPanel pSul = new JPanel();
        pSul.setLayout(null);
        //pSul.setBounds(5, 565, 790, 30);
        pSul.add(btSair);
        
        this.setLayout(null);
        this.add(pCentro);//, BorderLayout.CENTER);
        this.add(btSair);//, BorderLayout.SOUTH);
	}
}
