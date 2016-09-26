package view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Sessao;

@SuppressWarnings("serial")
public class PanelDadosAudiencia extends JPanel {

	public JLabel lbNumProcessoVlr;
	public JLabel lbTipoProcessoVlr;
	public JLabel lbJuizVlr;
	public JLabel lbDataVlr;
	public JLabel lbHoraVlr;

	public PanelDadosAudiencia() {
		this.setLayout(null);
		this.setBounds(5, 5, 500, 95);
		this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
	
		JLabel lbNumProcesso = new JLabel("Nr. Processo: ");
		lbNumProcesso.setBounds(10, 8, 100, 20);
		lbNumProcesso.setAlignmentY(RIGHT_ALIGNMENT);
		lbNumProcessoVlr = new JLabel(Sessao.numProcesso);
		lbNumProcessoVlr.setBounds(120, 8, 200, 20);
		//lbNumProcessoVlr.setFont(new Font("Monospaced", Font.PLAIN & Font.BOLD, 12));
		lbNumProcessoVlr.setForeground(Color.blue);
		
		JLabel lbTipoProcesso = new JLabel("Tipo: ");
		lbTipoProcesso.setBounds(10, 38, 60, 20);
		lbTipoProcesso.setAlignmentY(RIGHT_ALIGNMENT);
		lbTipoProcessoVlr = new JLabel(Sessao.tipo);
		lbTipoProcessoVlr.setBounds(120, 38, 100, 20);
		//lbTipoProcessoVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lbTipoProcessoVlr.setForeground(Color.blue);
		
		JLabel lbJuiz = new JLabel("Juiz: ");
		lbJuiz.setBounds(270, 38, 60, 20);
		lbJuiz.setAlignmentY(RIGHT_ALIGNMENT);
		lbJuizVlr = new JLabel(Sessao.juiz);
		lbJuizVlr.setBounds(335, 38, 200, 20);
		//lbJuizVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lbJuizVlr.setForeground(Color.blue);

		JLabel lbData = new JLabel("Data: ");
		lbData.setBounds(10, 68, 60, 20);
		lbData.setAlignmentY(RIGHT_ALIGNMENT);
		lbDataVlr = new JLabel(Sessao.data);
		lbDataVlr.setBounds(120, 68, 100, 20);
		//lbDataVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lbDataVlr.setForeground(Color.blue);

		JLabel lbHora= new JLabel("Hora: ");
		lbHora.setBounds(270, 68, 60, 20);
		lbHora.setAlignmentY(RIGHT_ALIGNMENT);
		lbHoraVlr = new JLabel(Sessao.hora);
		lbHoraVlr.setBounds(335, 68, 100, 20);
		//lbHoraVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lbHoraVlr.setForeground(Color.blue);
		
		this.add(lbNumProcesso);
		this.add(lbNumProcessoVlr);
		this.add(lbTipoProcesso);
		this.add(lbTipoProcessoVlr);
		this.add(lbJuiz);
		this.add(lbJuizVlr);
		this.add(lbData);
		this.add(lbDataVlr);
		this.add(lbHora);
		this.add(lbHoraVlr);
		return;
	}

	public void setDados() {
		lbNumProcessoVlr.setText(Sessao.numProcesso);
		lbTipoProcessoVlr.setText(Sessao.tipo);
		lbJuizVlr.setText(Sessao.juiz);
		lbDataVlr.setText(Sessao.data);
		lbHoraVlr.setText(Sessao.hora);
	}
}
