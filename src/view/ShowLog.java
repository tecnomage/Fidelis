package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class ShowLog extends JDialog {
	
	public ShowLog() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(700, 500);
		setLocation(80, 70);
		setResizable(false);
		setModal(true);		
		setTitle("Videocap: Visualiza Log do Sistema");		

		Container cont = getContentPane();
		cont.setLayout(new BorderLayout());
		
		JPanel pSul = new JPanel();
		JPanel pCentro = new JPanel();
		
		JButton btFechar = new JButton("Fechar");
		pSul.add(btFechar);
		
		cont.add(pCentro, BorderLayout.CENTER);
		cont.add(pSul, BorderLayout.SOUTH);		
		
		btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	//GravaLog log = new GravaLog();
            	//log.grava("ShowLog(): Modulo Finalizado.");
            	//log.fecha();
            	dispose();
            }
        });
		
		
		// Monta a JTable dos marcadores
		DefaultTableModel tablemodel = new DefaultTableModel();
		tablemodel.setColumnIdentifiers(new String[] {"Descricao"});		
		JTable tbMarca = new JTable(tablemodel);
		tbMarca.setModel(tablemodel);		
        JScrollPane sp = new JScrollPane(tbMarca);     
        pCentro.add(sp);
        
        try {
			//FileReader fr = new FileReader("c:/Temp/fidelis.log");
			//BufferedReader is = new BufferedReader(fr);
        	Scanner f = new Scanner(new File("c:/Temp/fidelis.log"));
			int i = 0;
			while (f.hasNextLine()) {
				tablemodel.addRow(new String[] {null});
				tbMarca.setValueAt(f.nextLine(), i++, 0);
			}
			f.close();			
		} 
        catch (FileNotFoundException e) {			
		}
        pack();
	}
}
