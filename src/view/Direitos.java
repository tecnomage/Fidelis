package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Direitos extends JFrame {

	public Direitos() {
		//setSize(405, 225);
		setUndecorated(true);		
		Container cont = getContentPane();
		cont.setLayout(new BorderLayout());
		
		JPanel p = new JPanel();
		File f = new File("direitos.jpg");
		if (f.exists()) {
			JLabel lbImg = new JLabel(new ImageIcon("direitos.jpg"));					
			p.add(lbImg);
		}
		else {
			JLabel lb1 = new JLabel("         Tribunal Regional do Trabalho - 9a. Região - Paraná       ");			
			JLabel lb2 = new JLabel("                Gravação e Visualização de Audiências              ");			
			JLabel lb3 = new JLabel("(C) 2009, TRT 9a. Região - Paraná - Todos os Direitos Reservados");
			JLabel lb4 = new JLabel("");
			JLabel lb5 = new JLabel("");
			
			p.setLayout(new GridLayout(5,1));
			p.add(lb1);
			p.add(lb4);
			p.add(lb2);
			p.add(lb5);
			p.add(lb3);
			
		}
		
		
		cont.add(p, BorderLayout.CENTER);
		pack();
	}
	
}
