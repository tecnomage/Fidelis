package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AutoFillField extends UpperField {
	
	private String [] strLista;
	
	public AutoFillField() { }

	@SuppressWarnings("unchecked")
	public AutoFillField(Vector lista) {
		setPropriedades(lista);
	}
	
	@SuppressWarnings("unchecked")
	public void setPropriedades(Vector lista) {
		String [] tmpLista = new String[lista.size()];
		for (int i=0; i<lista.size(); i++) {
			tmpLista[i] = (String) lista.elementAt(i);
		}
		
		strLista = tmpLista;
		final JTextField comp = this; 
		
		this.addKeyListener(new java.awt.event.KeyAdapter() {			
			
			public void keyReleased(java.awt.event.KeyEvent key) {				
				if (Character.isLetter(key.getKeyChar())) {					
					JPopupMenu popup = new JPopupMenu();
					ActionListener menuListener = new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							comp.setText(event.getActionCommand().toString());
						}
					};

					int contador = 0;
					//int pos = getCaretPosition();
					//setText(getText().toUpperCase());
					//setCaretPosition(pos);
					for (int i=0; i<strLista.length; i++) {						
						if (strLista[i].length() >= getText().length() && strLista[i].substring(0, getText().length()).equals(getText().toUpperCase())) {
							JMenuItem item = new JMenuItem();
							popup.add(item = new JMenuItem(strLista[i]));
						    item.addActionListener(menuListener);
						    contador++;
						}						
					}
					if (contador > 0)
						popup.show(comp, comp.getX(), comp.getY());
					
				}
			}
		});
	}
}
