package fidelis.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fidelis.controller.DBDealer;
import fidelis.controller.Sessao;


@SuppressWarnings("serial")
public class PanelSelecionaAudienciaPendente extends JPanel {

	public String usuarioLogado = null;
	private JComboBox cbNumProcessos = null;
	private Vector<String> vtNumProcesso = new Vector<String>();
	private Vector<String> vtData = new Vector<String>();
	private Vector<String> vtHora = new Vector<String>();
	private Vector<String> vtTipo = new Vector<String>();
	private Vector<String> vtJuiz = new Vector<String>();
	
	private JLabel lbTipoProcessoVlr = null;
	private JLabel lbJuizVlr = null;
	private JLabel lbDataVlr = null;
	private JLabel lbHoraVlr = null;
	
	@SuppressWarnings("unchecked")
	public PanelSelecionaAudienciaPendente(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
		
		JPanel borda = new JPanel();
		borda.setLayout(null);
		borda.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Sobre as Audiências Pendentes"));
		borda.setBounds(8, 8, 420, 220);
		
		JPanel pSelecao = new JPanel();
		pSelecao.setLayout(null);
		pSelecao.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		//pSelecao.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		pSelecao.setBounds(20, 30, 380, 50);
		
		JPanel pFixo = new JPanel();
		pFixo.setLayout(null);
		pFixo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		//pFixo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		pFixo.setBounds(20, 100, 380, 100);
		
		JLabel lbNumProcesso = new JLabel("Número do Processo: ");
		lbNumProcesso.setBounds(10, 15, 130, 20);
		
		final DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS");
		Vector vtRetorno = dealer.getAudienciasPendentes();
		String [] strRetorno = new String[5];
		for (int i = 0; i < vtRetorno.size(); i++) {
			strRetorno = (String[]) vtRetorno.elementAt(i);
			vtNumProcesso.addElement(strRetorno[0]);	
			vtData.addElement(strRetorno[1]);
			vtHora.addElement(strRetorno[2]); 
			vtTipo.addElement(strRetorno[3]); 
			vtJuiz.addElement(strRetorno[4]); 
		}
		
		cbNumProcessos = new JComboBox(vtNumProcesso);
		cbNumProcessos.setBounds(160, 15, 200, 20);
		cbNumProcessos.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				showAudienciaDados(dealer);
			}		
		});
		
		JLabel lbTipoProcesso = new JLabel("Tipo: ");
		lbTipoProcesso.setBounds(10, 10, 60, 20);
		lbTipoProcesso.setAlignmentY(RIGHT_ALIGNMENT);
		lbTipoProcessoVlr = new JLabel("");
		lbTipoProcessoVlr.setBounds(75, 10, 100, 20);
		lbTipoProcessoVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		JLabel lbJuiz = new JLabel("Juiz: ");
		lbJuiz.setBounds(10, 40, 60, 20);
		lbJuiz.setAlignmentY(RIGHT_ALIGNMENT);
		lbJuizVlr = new JLabel("");
		lbJuizVlr.setBounds(75, 40, 200, 20);
		lbJuizVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JLabel lbData = new JLabel("Data: ");
		lbData.setBounds(10, 70, 60, 20);
		lbData.setAlignmentY(RIGHT_ALIGNMENT);
		lbDataVlr = new JLabel("");
		lbDataVlr.setBounds(75, 70, 100, 20);
		lbDataVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JLabel lbHora= new JLabel("Hora: ");
		lbHora.setBounds(180, 70, 60, 20);
		lbHora.setAlignmentY(RIGHT_ALIGNMENT);
		lbHoraVlr = new JLabel("");
		lbHoraVlr.setBounds(245, 70, 100, 20);
		lbHoraVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
	
		pFixo.add(lbTipoProcesso);
		pFixo.add(lbTipoProcessoVlr);
		pFixo.add(lbJuiz);
		pFixo.add(lbJuizVlr);
		pFixo.add(lbData);
		pFixo.add(lbDataVlr);
		pFixo.add(lbHora);
		pFixo.add(lbHoraVlr);

		pSelecao.add(lbNumProcesso);
		pSelecao.add(cbNumProcessos);
		
		borda.add(pSelecao);
		borda.add(pFixo);
	
		this.setLayout(null);
		this.add(borda);		
		showAudienciaDados(dealer);
	}
	
	public void showAudienciaDados(DBDealer d) {
		int i = cbNumProcessos.getSelectedIndex();
		if (i > -1) {
			lbTipoProcessoVlr.setText(d.getDescTipoAudiencia(new Integer(vtTipo.elementAt(i))).toString());
			lbJuizVlr.setText(vtJuiz.elementAt(i));
			lbDataVlr.setText(vtData.elementAt(i));
			lbHoraVlr.setText(vtHora.elementAt(i));
			
			Sessao.numProcesso = (String) cbNumProcessos.getSelectedItem();
			Sessao.tipo = lbTipoProcessoVlr.getText();
			Sessao.juiz = lbJuizVlr.getText();
		}
		
		return;
	}
	
}
