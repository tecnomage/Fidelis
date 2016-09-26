package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import de.humatic.dsj.DSFiltergraph;
import fidelis.controller.AutoFillField;
import fidelis.controller.ClockThread;
import fidelis.controller.DBDealer;
import fidelis.controller.DSJCaptura;
import fidelis.controller.Ferramentas;
import fidelis.controller.GeraHash;
import fidelis.controller.GravaLog;
import fidelis.controller.MonitoraEspacoThread;
import fidelis.controller.Sessao;
import fidelis.controller.SessaoConfig;
import fidelis.controller.ThreadMonitorMicrofone;
import fidelis.model.DBCorp;

@SuppressWarnings("serial")
public class FrameGravaAudiencia extends JPanel {
	
	private JFormattedTextField tfNumProcesso;
	private JComboBox cbTipAudiencia;
	private JTextField tfJuiz;
	private String data = null;
	private String hora = null;

	private JButton btIncluir = null;
	private JButton btVoltar = null;
	private JLabel lbTempo = null;
	private JLabel lbRec = null;
	private JButton btAjustarVideo = null;
	private JButton btMarcarVideo = null;
	private JButton btConclui = null;
	private JButton btInterrompe = null;
	private JComboBox cbMotivoFinalizacao = null;
	private JButton btGravarVideo = null;
	
	private DSFiltergraph dsfg;
	private DSJCaptura cap;
	
	private String usuarioLogado = null;
	private PanelTreeTemasEQualif pTemas = null;
	private PanelTreeTemasEQualif pQualifica = null;
	private JPanel pVideo = null;
	private String arquivoDeVideo = null;
	
	private JInternalFrame jifPai = null; 
	
	private JLabel lbEspacoLivre = null;
	private JLabel lbTamanhoVideo = null;
	private JLabel lbGauge0;
	private JLabel lbGauge8;
	private JLabel lbGauge20;
	private JLabel lbGauge39;
	private JLabel lbGauge40;
	private JLabel lbGauge41;
	private JCheckBox ckTipoGravacao;
	
	private DefaultTableModel tablemodelMarcacoes;
	private JTable tbMarcacoes;
	
	private boolean gravandoVideo;

	public FrameGravaAudiencia(String strUsuarioLogado) {		
		usuarioLogado = strUsuarioLogado;
		
		int MARGEM_LABEL = 12;
		int MARGEM_TF = 145;
		int Y = 12;

		this.setLayout(null);		
		
		FocusListener focusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				tfNumProcesso.setCaretPosition(0);				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
			}
		};
		
		// Componentes de dados dos Processos. Labels e textfields
		JPanel pDadosProc = new JPanel();
		pDadosProc.setLayout(null);
		pDadosProc.setBounds(5, 5, 355, 140);
		pDadosProc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		//pDadosProc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));		  
	
		JLabel lb1 = new JLabel("Número do Processo:");
		lb1.setBounds(MARGEM_LABEL, Y, 200, 20);
		JLabel lb2 = new JLabel("Tipo de Audiência:");
		lb2.setBounds(MARGEM_LABEL, Y+30, 200, 20);
		JLabel lb3 = new JLabel("Juiz da Audiência:");
		lb3.setBounds(MARGEM_LABEL, Y+60, 200, 20);
		
		tfNumProcesso = new JFormattedTextField();
		/*
		try {
			tfNumProcesso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#######-##.####.#.##.####")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		*/
		
		tfNumProcesso.addFocusListener(focusListener);
		tfNumProcesso.setBounds(MARGEM_TF, Y, 200, 20);		
		if (Sessao.numProcesso != null) {
			tfNumProcesso.setText(Sessao.numProcesso.trim());
			//tfNumProcesso.setEnabled(false);
		}
		
		DBDealer dealer = new DBDealer(usuarioLogado, "TIPOAUDIENCIAS");
		cbTipAudiencia = new JComboBox(dealer.listar());
		cbTipAudiencia.setBounds(MARGEM_TF, Y+30, 200, 20);
		if (Sessao.numProcesso != null) {
			cbTipAudiencia.setSelectedItem((String) Sessao.tipo.trim());
			//cbTipAudiencia.setEnabled(false);
		}

		//DBCorp corp = new DBCorp(usuarioLogado);
		//Vector vRet = corp.getJuizes();
		//corp.fechaConexao();
		//tfJuiz = new JTextField();
		//tfJuiz = new UpperField();
		//tfJuiz = new AutoFillField(vRet);
		tfJuiz = new AutoFillField();
		
		tfJuiz.setBounds(MARGEM_TF, Y+60, 200, 20);

		if (Sessao.numProcesso != null) {
			tfJuiz.setText(Sessao.juiz.trim());
		//	tfJuiz.setEditable(false);
		}

		
	    // Botoes de inclusao dos dados da audiencia
		btIncluir = new JButton("Validar");
		btIncluir.setBounds(MARGEM_TF, Y+90, 95, 25);
		
		btVoltar = new JButton("Voltar");
		btVoltar.setBounds(MARGEM_TF + 104, Y+90, 95, 25);
		btVoltar.setEnabled(false);
		
		// Painel de Temas/SubTemas e Qualificacoes/Depoentes
		pTemas = new PanelTreeTemasEQualif(usuarioLogado, "", "TEMAS");
		pTemas.setLocation(370, 1);
		this.add(pTemas);
		
		pQualifica = new PanelTreeTemasEQualif(usuarioLogado, "", "QUALIFICACOES");
		pQualifica.setLocation(370, 262);
		this.add(pQualifica);	
		
		JPanel pMarcaVideo = new JPanel();
		pMarcaVideo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pMarcaVideo.setBounds(372, 523, 337, 35);
		pMarcaVideo.setLayout(null);
		
		btAjustarVideo = new JButton("Ajustar Vídeo");
		btAjustarVideo.setEnabled(false);
		btAjustarVideo.setBounds(10, 5, 140, 25);
		pMarcaVideo.add(btAjustarVideo);
		
		btMarcarVideo = new JButton("Marcar Gravação");
		btMarcarVideo.setEnabled(false);		
		btMarcarVideo.setBounds(184, 5, 140, 25);
		pMarcaVideo.add(btMarcarVideo);
		this.add(pMarcaVideo);
		

		// Componentes de Video
		pVideo = new JPanel();
		pVideo.setBounds(5, 151, 355, 260);
		pVideo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		JPanel pBotoesVideo = new JPanel();
		pBotoesVideo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pBotoesVideo.setBounds(5, 418, 355, 37);
		pBotoesVideo.setLayout(null);
		lbTempo = new JLabel("00:00:00");		
		lbTempo.setBounds(8, 5, 60, 25);
		
		//ckMudo = new JCheckBox("Mudo");
		//ckMudo.setBounds(73, 5, 60, 25);
		//ckMudo.setSelected(true);
		
		lbRec = new JLabel("Rec");
		lbRec.setBounds(73, 5, 60, 25);
		lbRec.setForeground(Color.gray.darker());
		lbRec.setVisible(false);
		
		btGravarVideo = new JButton("Gravar");
		btGravarVideo.setEnabled(false);
		btGravarVideo.setBounds(158, 5, 90, 25);
		
		btConclui = new JButton("Concluir");	
		btConclui.setBounds(256, 5, 90, 25);
		btConclui.setEnabled(false);
		
		
		// Painel de Finalizacao
		JPanel pFinaliza = new JPanel();
		pFinaliza.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pFinaliza.setBounds(5, 462, 355, 96);
		pFinaliza.setLayout(null);
		
		JLabel lbMotivo = new JLabel("Motivo da Interrupção:");
		lbMotivo.setBounds(6, 6, 150, 15);
		pFinaliza.add(lbMotivo);
		
		DBDealer dealerMot = new DBDealer(usuarioLogado, "MOTIVOFINALIZACOES");
		cbMotivoFinalizacao = new JComboBox(dealerMot.listar());
		cbMotivoFinalizacao.setBounds(6, 28, 340, 23);
		cbMotivoFinalizacao.setEnabled(false);
		pFinaliza.add(cbMotivoFinalizacao);

		btInterrompe = new JButton("Interromper Gravação");
		btInterrompe.setBounds(171, 58, 175, 23);
		btInterrompe.setEnabled(false);
		pFinaliza.add(btInterrompe);
		
		
		// Painel de Monitoramento de espaco em disco
		JPanel pMonitorEspaco = new JPanel();
		pMonitorEspaco.setBounds(720, 5, 250, 80);		
		pMonitorEspaco.setBorder(javax.swing.BorderFactory.createTitledBorder("Armazenamento: "));
		pMonitorEspaco.setLayout(new GridLayout(2,2));
		
		java.io.File f = new java.io.File(System.getProperty("user.dir").substring(0,2)+ "\\");		
		long espacoTotal = f.getTotalSpace();
		long espacoLivre = f.getFreeSpace();		
		long percentualLivre = (espacoLivre * 100) / espacoTotal;
		
		lbEspacoLivre = new JLabel();

		DecimalFormat nfmb = new DecimalFormat("#,###,### MB");
		nfmb.setDecimalSeparatorAlwaysShown(false);		
		lbEspacoLivre.setText(nfmb.format((f.getFreeSpace() / 1024) / 1024) + "  (" + percentualLivre + "%)");
		lbEspacoLivre.setForeground(Color.BLUE);
		pMonitorEspaco.add(new JLabel(" Espaço livre: "));
		pMonitorEspaco.add(lbEspacoLivre);		
		
		//JPanel pMonitorTamanho = new JPanel();
		//pMonitorTamanho.setBounds(720, 60, 250, 50);
		//pMonitorTamanho.setBorder(javax.swing.BorderFactory.createTitledBorder("Tamanho do Vídeo: "));
		
		lbTamanhoVideo = new JLabel();
		DecimalFormat nfkb = new DecimalFormat("#,###,### KB");
		nfkb.setDecimalSeparatorAlwaysShown(false);		
		lbTamanhoVideo.setText(nfkb.format(0));
		lbTamanhoVideo.setForeground(Color.BLUE);
		//pMonitorTamanho.add(lbTamanhoVideo);
		pMonitorEspaco.add(new JLabel(" Tam. do Arquivo: "));
		pMonitorEspaco.add(lbTamanhoVideo);
		
		JPanel pGauge = new JPanel();
		pGauge.setBounds(720, 85, 250, 73);
		pGauge.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequência do Microfone: "));
		pGauge.setLayout(null);
		lbGauge0 = new JLabel(new ImageIcon("images/gauge/gauge0.png"));
		lbGauge0.setBounds(35, 12, 180, 60);
		lbGauge8 = new JLabel(new ImageIcon("images/gauge/gauge8.png"));   lbGauge8.setVisible(false);
		lbGauge8.setBounds(35, 12, 180, 60);
		lbGauge20 = new JLabel(new ImageIcon("images/gauge/gauge20.png")); lbGauge20.setVisible(false);
		lbGauge20.setBounds(35, 12, 180, 60);
		lbGauge39 = new JLabel(new ImageIcon("images/gauge/gauge39.png")); lbGauge39.setVisible(false);
		lbGauge39.setBounds(35, 12, 180, 60);
		lbGauge40 = new JLabel(new ImageIcon("images/gauge/gauge40.png")); lbGauge40.setVisible(false);
		lbGauge40.setBounds(35, 12, 180, 60);
		lbGauge41 = new JLabel(new ImageIcon("images/gauge/gauge41.png")); lbGauge41.setVisible(false);
		lbGauge41.setBounds(35, 12, 180, 60);

		pGauge.add(lbGauge0);
		pGauge.add(lbGauge8);
		pGauge.add(lbGauge20);
		pGauge.add(lbGauge39);
		pGauge.add(lbGauge40);
		pGauge.add(lbGauge41);
		
		JPanel pTipoGravacao = new JPanel();
		pTipoGravacao.setBounds(720, 158, 250, 55);
		pTipoGravacao.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Gravação: "));
		ckTipoGravacao = new JCheckBox("Somente Áudio");	
		ckTipoGravacao.setSelected(false);
		pTipoGravacao.add(ckTipoGravacao);		
		
		
		
		// Painel de listagem das marcacoes		
		tablemodelMarcacoes = new DefaultTableModel(){   
			public boolean isCellEditable(int rowIndex, int mColIndex) {   
				return false;   
			}   
		};  

		tablemodelMarcacoes.setColumnIdentifiers(new String[] {"Marcações"});		
		tbMarcacoes = new JTable(tablemodelMarcacoes);
		
		tbMarcacoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbMarcacoes.setModel(tablemodelMarcacoes);	
		tbMarcacoes.setRowHeight(50);
		
        JScrollPane spListaMarcacoes = new JScrollPane(tbMarcacoes);
        spListaMarcacoes.setBounds(720, 220, 250, 338);     
        
		
		
		// Funcoes de eventos dos botoes
		btIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btIncluirEvent(evt);
            }
        });

		btVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btVoltarEvent(evt);
            }
        });		
		
		btGravarVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btGravarVideoEvent(evt);
            }
        });
		
		btConclui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btConcluiEvent(evt);
            }
        });	
		
		btInterrompe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btInterrompeEvent(evt);
            }
        });	

		btMarcarVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btMarcarVideoEvent(evt);
            }
        });		
		
		btAjustarVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            	if (cap != null) {
            		AjusteVideo ajv = new AjusteVideo(cap);
            		ajv.setVisible(true);
            	}
            	else
            		JOptionPane.showMessageDialog(null, "Vídeo inexistente. Ajuste impossível.", "Atenção", JOptionPane.WARNING_MESSAGE);
            	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
		
		// Adicao dos componentes nos paineis e container		
		pDadosProc.add(lb1);
		pDadosProc.add(lb2);
		pDadosProc.add(lb3);
		pDadosProc.add(tfNumProcesso);
		pDadosProc.add(cbTipAudiencia);
		pDadosProc.add(tfJuiz);		  
		pDadosProc.add(btIncluir);
		pDadosProc.add(btVoltar);
		this.add(pDadosProc);		
        this.add(pVideo);
        pBotoesVideo.add(lbTempo);
        //pBotoesVideo.add(ckMudo);
        pBotoesVideo.add(lbRec);
        pBotoesVideo.add(btGravarVideo);
        pBotoesVideo.add(btConclui);
        this.add(pBotoesVideo);
		this.add(pFinaliza);
		this.add(pMonitorEspaco);
		//this.add(pMonitorTamanho);
		this.add(pGauge);
		this.add(pTipoGravacao);
		this.add(spListaMarcacoes);
	}
	
	public void setNumProcesso() {
		tfNumProcesso.setText(Sessao.numProcesso);
		if (Sessao.numProcesso != null) 
			tfNumProcesso.setEditable(false);
		cbTipAudiencia.setSelectedItem((String) Sessao.tipo);
		tfJuiz.setText(Sessao.juiz);
		btVoltarEvent(null);
	}
	
	public boolean validaNumProcesso(String strNumProcesso) {
		
		String strAux = "";
		for (int i=0; i<strNumProcesso.length(); i++) {
			if (new String ("0123456789").lastIndexOf(strNumProcesso.substring(i, i + 1)) > -1)
				strAux += (strNumProcesso.substring(i, i + 1)); 
		}
		strNumProcesso = strAux;

		if (tfNumProcesso.getFormatterFactory() != null && strNumProcesso.length() > 0)
			return true;
		
		if (strNumProcesso.length() == 20) {
			try {
				tfNumProcesso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#######-##.####.#.##.####")));
				tfNumProcesso.setText(strNumProcesso);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return true;
		}	
		
		if (strNumProcesso.length() == 17) {
			try {
				tfNumProcesso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##### #### ### ## ## #")));
				tfNumProcesso.setText(strNumProcesso);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return true;
		}
		else
			return false;
	}
	
	
	private void btIncluirEvent(java.awt.event.ActionEvent evt) {
		
		if (Sessao.nomeDispositivoVideo.equals("nenhum")) {
			JOptionPane.showMessageDialog(null, "Nenhum dispositivo de vídeo foi selecionado. Não é possível prosseguir com a validação.","GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (Sessao.nomeDispositivoAudio.equals("nenhum")) {
			JOptionPane.showMessageDialog(null, "Nenhum dispositivo de áudio foi selecionado. Não é possível prosseguir com a validação.","GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		tfNumProcesso.setText(tfNumProcesso.getText().trim());
		if (!validaNumProcesso(tfNumProcesso.getText())) {
			JOptionPane.showMessageDialog(null, "Número de Processo inválido!", "GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.WARNING_MESSAGE);
		}
		else {
			if (cbTipAudiencia.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(null, "Por favor informe o TIPO DE AUDIÊNCIA!", "GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.WARNING_MESSAGE);
			}
			else {
				if (tfJuiz.getText().equals("") || tfJuiz.getText() == null) {
					JOptionPane.showMessageDialog(null, "Por favor informe o NOME DO JUIZ!", "GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.WARNING_MESSAGE);
				}
				else {
					SimpleDateFormat dfData = new SimpleDateFormat("dd-MM-yyyy");
					SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm:ss");
					data = dfData.format(new Date());			
					hora = dfHora.format(new Date());
					
					DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", tfNumProcesso.getText(), data, hora);
					if (dealer.isProcessoConcluido() > 0) {						
			        	Object[] options = { "Sim", "Não" };
			        	String strMsg = "Este processo já foi CONCLUÍDO anteriormente. Deseja continuar com a gravação?"; 
			        	int resposta = JOptionPane.showOptionDialog(null, strMsg, "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			        	GravaLog logInfo = new GravaLog("INFO");
			        	logInfo.grava("fidelis.view.FrameGravaAudiencia().btIncluirEvent()", usuarioLogado, "Tentativa de gravar um vídeo para um processo(" + tfNumProcesso.getText() + ") JÁ CONCLUÍDO ANTERIORMENTE!");
			        	if (resposta == 1) {			        		
			        		logInfo.grava("fidelis.view.FrameGravaAudiencia().btIncluirEvent()", usuarioLogado, "Usuário NÃO concordou com a gravação do processo(" + tfNumProcesso.getText() + ") que havia sido concluído anteriormente!");
			        		return;
			        	}
			        	
						logInfo.grava("fidelis.view.FrameGravaAudiencia().btIncluirEvent()", usuarioLogado, "Usuário CONCORDOU com a gravação do processo(" + tfNumProcesso.getText() + ") que havia sido concluído anteriormente!");						
					}

					if (dealer.isProcInterrompidoEmD0() > 0 && !SessaoConfig.isAdmin) {
						GravaLog logInfo = new GravaLog("INFO");
						logInfo.grava("fidelis.view.FrameGravaAudiencia().btIncluirEvent()", usuarioLogado, "Tentativa de gravar um vídeo para um processo(" + tfNumProcesso.getText() + ") JÁ INTERROMPIDO PELO USUARIO NESTA DATA!");
						JOptionPane.showMessageDialog(null, "Continuação de gravação da audiência não permitida no mesmo dia.\nEsta audiência será liberada para gravação a partir de amanhã.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
					}					
					else {
						pTemas.carregaTemas(tfNumProcesso.getText());			
						pQualifica.carregaQualifica(tfNumProcesso.getText(), Sessao.autor);				
						tfNumProcesso.setEditable(false);
						cbTipAudiencia.setEnabled(false);
						tfJuiz.setEditable(false);
						btIncluir.setEnabled(false);				
						btGravarVideo.setEnabled(true);	
						btVoltar.setEnabled(true);
					}
				}
			}
		}
	}
	
	private void btVoltarEvent(java.awt.event.ActionEvent evt) {
		if (tfNumProcesso.getText().equals("")) {
			tfNumProcesso.setEditable(true);
		}
		cbTipAudiencia.setEnabled(true);
		tfJuiz.setEditable(true);
		btIncluir.setEnabled(true);
		btGravarVideo.setEnabled(false);
		btVoltar.setEnabled(false);		
		ckTipoGravacao.setEnabled(true);
	}
	
	
	
	private void gravaVideoBackOut() {
		btMarcarVideo.setEnabled(false);
		btAjustarVideo.setEnabled(false);
		cbMotivoFinalizacao.setEnabled(false);
		btInterrompe.setEnabled(false);
		btConclui.setEnabled(false);
		btGravarVideo.setEnabled(true);
		btVoltar.setEnabled(true);
	}

	
	private void btMarcarVideoEvent(java.awt.event.ActionEvent evt) {		
      	String [] strTemas = pTemas.getNodesSelecionados();
    	String [] strQualifica = pQualifica.getNodesSelecionados();
    	String strTempo = lbTempo.getText();
    	if(strTempo.equals("00:00:00")){
    		strTempo = "00:00:01";
    	}	else{
    	strTempo = lbTempo.getText();
    	}
    	String strMsg = "Dados da Marcação da Gravação:\n";
    	strMsg += "\nTema: " + strTemas[0];
    	strMsg += "\nSubTema: " + strTemas[1];
    	strMsg += "\n\nDepoente: " + strQualifica[1];
    	strMsg += "\nQualificação: " + strQualifica[0];
    	strMsg += "\n\nTempo na gravação: " + strTempo;
    	strMsg += "\n\nDeseja efetivar esta marcação?";
    	
    	if (!strTemas[0].equals("") || !strTemas[1].equals("") || !strQualifica[0].equals("") || !strQualifica[1].equals("")) {
    		
        	Object[] options = { "Sim", "Não" };
        	int resposta = JOptionPane.showOptionDialog(null, strMsg, "Marcação da Gravação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    		
	    	//int resposta = JOptionPane.showConfirmDialog(null, strMsg, "Marcação do Vídeo", JOptionPane.YES_NO_OPTION);
	    	if (resposta == 0) {
	    		DBDealer deal = new DBDealer(usuarioLogado, "MARCACOES", tfNumProcesso.getText(), data, hora);
	    		deal.incluir(strTempo, strTemas[0], strTemas[1], strQualifica[0], strQualifica[1]);
	    		
		        tablemodelMarcacoes.addRow(new String[] {null});
		 
		        if (tbMarcacoes.getRowCount() > 1) {
			        for (int i=tbMarcacoes.getRowCount()-1; i>0; i--) {
			        	tbMarcacoes.setValueAt(tbMarcacoes.getValueAt(i-1,0), i, 0);
			        }
		        }
		       
	        	tbMarcacoes.setValueAt("<html>" + strTempo + "<br>" + strTemas[0] + " / "+ strTemas[1] + "<br>" + strQualifica[0] + " / " + strQualifica[1] + "</html>", 0, 0);//tbMarcacoes.getRowCount() - 1, 0);
	    	}
    	}
    	else {
    		JOptionPane.showMessageDialog(null, "Selecione pelo menos um Tema/SubTema ou Quaificação/Depoente para marcar o vídeo.\nNenhuma MARCAÇÃO foi realizada agora.", "Marcação de Vídeo", JOptionPane.ERROR_MESSAGE);
    	}
	}	
	
	
	
	private void btGravarVideoEvent(java.awt.event.ActionEvent evt) {	
		DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", tfNumProcesso.getText(), data, hora);
		if (dealer.incluir((String)cbTipAudiencia.getSelectedItem(), tfJuiz.getText())) {
			btMarcarVideo.setEnabled(true);
			cbMotivoFinalizacao.setEnabled(true);
			btInterrompe.setEnabled(true);
			btConclui.setEnabled(true);
			btGravarVideo.setEnabled(false);
			btVoltar.setEnabled(false);
			ckTipoGravacao.setEnabled(false);
			if (ckTipoGravacao.isSelected()) {
				gravaAudio();
			}
			else {
				btAjustarVideo.setEnabled(true);
				gravaVideo();
			}
		}				
	}	
	
	
	
	private void btConcluiEvent(java.awt.event.ActionEvent evt) {
		GravaLog logInfo = new GravaLog("INFO");		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		
		
		btMarcarVideo.setEnabled(false);
		btAjustarVideo.setEnabled(false);
		cbMotivoFinalizacao.setEnabled(false);
		btInterrompe.setEnabled(false);
		btConclui.setEnabled(false);

		concluiGravacao();
		
		DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", tfNumProcesso.getText(), data, hora);
		if (dealer.mudaStatus("CONCLUIDO")) {
			logInfo.grava("fidelis.view.FrameGravaAudiencia.btConcluiEvent()", usuarioLogado, "001-Gravação da Audiência (Processo Nr: " + tfNumProcesso.getText() + ") foi CONCLUÍDA!");
		}

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));		
		JOptionPane.showMessageDialog(null, "Gravação da Audiência Finalizada!", "GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.INFORMATION_MESSAGE);
		
		Ferramentas frm = new Ferramentas();
		String strCmd = "cmd /c " + frm.getPropriedades("assinejus");
		try {
			Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + strCmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		jifPai.dispose();
	}
	
	
	
	private void btInterrompeEvent(java.awt.event.ActionEvent evt) {
		GravaLog logInfo = new GravaLog("INFO");		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		//Progress prg = new Progress("Finalizando o vídeo ...");
		//prg.setVisible(true);

		btMarcarVideo.setEnabled(false);
		btAjustarVideo.setEnabled(false);
		cbMotivoFinalizacao.setEnabled(false);
		btInterrompe.setEnabled(false);
		btConclui.setEnabled(false);

		concluiGravacao();
		
		DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", tfNumProcesso.getText(), data, hora);
		if (dealer.mudaStatus((String) cbMotivoFinalizacao.getSelectedItem())) {
			logInfo.grava("fidelis.view.FrameGravaAudiencia.btInterrompeEvent()", usuarioLogado, "Gravação da Audiência (Processo Nr: " + tfNumProcesso.getText() + ") foi INTERROMPIDA! Motivo: " + (String) cbMotivoFinalizacao.getSelectedItem());
		}
		
		//prg.setVisible(false);
		//prg.dispose();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		JOptionPane.showMessageDialog(null, "Gravação da Audiência Finalizada!", "GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.INFORMATION_MESSAGE);
		
		Ferramentas frm = new Ferramentas();
		String strCmd = "cmd /c " + frm.getPropriedades("assinejus");
		try {
			Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + strCmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		jifPai.dispose();
	}
	
	public void interrompePorErroSincronia() {
		GravaLog logInfo = new GravaLog("INFO");		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		
		btMarcarVideo.setEnabled(false);
		btAjustarVideo.setEnabled(false);
		cbMotivoFinalizacao.setEnabled(false);
		btInterrompe.setEnabled(false);
		btConclui.setEnabled(false);

		concluiGravacao();
		
		logInfo.grava("fidelis.view.FrameGravaAudiencia.interrompePorErroSincronia()", usuarioLogado, "Gravação da Audiência (Processo Nr: " + tfNumProcesso.getText() + ") foi INTERROMPIDA! Motivo: ERRO DE SINCRONIA!");
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		JOptionPane.showMessageDialog(null, "Gravação da Audiência Finalizada!", "GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.INFORMATION_MESSAGE);
		jifPai.dispose();
	}
	
	public void concluiGravacao() {
		
		Sessao.gravando = false;
		
		if (gravandoVideo)
			cap.finalizaFilmagem();
		else
			cap.finalizaAudio();
		
		lbGauge0.setVisible(true);
		lbGauge8.setVisible(false);
		lbGauge20.setVisible(false);
		lbGauge39.setVisible(false);
		lbGauge40.setVisible(false);
		lbGauge41.setVisible(false);
		
		
		//lbMic.setForeground(Color.black);
				
		GeraHash hash = new GeraHash();
		String strHashFromFile = hash.getHashFromFile(arquivoDeVideo);

		DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", tfNumProcesso.getText(), data, hora);
		dealer.gravarHash(strHashFromFile);
	}

	
	public void gravaVideo() {
		gravandoVideo = true;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        GravaLog logInfo = new GravaLog("INFO");
        GravaLog logErro = new GravaLog("ERRO");
        
		try {
            String strDataAux = data.substring(0,2) + data.substring(3,5) + data.substring(6);
            String strHoraAux = hora.substring(0,2) + hora.substring(3,5) + hora.substring(6);
            arquivoDeVideo = System.getProperty("user.dir") + "/videos/" + tfNumProcesso.getText() + " " + strDataAux + " " + strHoraAux + ".asf";	            
 
            cap = new DSJCaptura(pVideo, dsfg);
            cap.iniciaFilmagem(arquivoDeVideo);
            cap.setVolume(0);
            
            JLabel[] vlb = new JLabel[6];
            vlb[0] = lbGauge0;
            vlb[1] = lbGauge8;
            vlb[2] = lbGauge20;
            vlb[3] = lbGauge39;
            vlb[4] = lbGauge40;
            vlb[5] = lbGauge41;
            
            ThreadMonitorMicrofone mMic = new ThreadMonitorMicrofone(vlb, lbRec, this, arquivoDeVideo);
            mMic.start();
 
            ClockThread cl = new ClockThread(cap, lbTempo);
            cl.start();	          
            
            MonitoraEspacoThread esp = new MonitoraEspacoThread(System.getProperty("user.dir").substring(0,2)+ "\\", lbEspacoLivre, lbTamanhoVideo, arquivoDeVideo);
            esp.start();
            
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));	            
		}
		catch (Exception e) {				
			e.printStackTrace();
        	logErro.gravaExcep("fidelis.view.FrameGravaAudiencia.gravaLog()", e);
        	logInfo.grava("fidelis.view.FrameGravaAudiencia.gravaLog()", usuarioLogado, "O Processo de gravação de vídeo NÃO pôde ser inicializado!");
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showMessageDialog(null, "O Processo de gravação de vídeo NÃO pôde ser inicializado!", "GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.ERROR_MESSAGE);
			gravaVideoBackOut();
		}
		
	}

	public void gravaAudio() {
		gravandoVideo = false;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		GravaLog logInfo = new GravaLog("INFO");
		GravaLog logErro = new GravaLog("ERRO");
		logInfo.grava("fidelis.view.FrameGravaAudiencia.gravaAudio()", usuarioLogado, "Gravando Áudio ...");

		try {
	            
			JLabel lbMsg = new JLabel("... Áudio sendo capturado ...");
			lbMsg.setForeground(Color.blue);
			pVideo.add(lbMsg, BorderLayout.CENTER);

            String strDataAux = data.substring(0,2) + data.substring(3,5) + data.substring(6);
            String strHoraAux = hora.substring(0,2) + hora.substring(3,5) + hora.substring(6);
            arquivoDeVideo = System.getProperty("user.dir") + "/videos/" + tfNumProcesso.getText() + " " + strDataAux + " " + strHoraAux + ".asf";	            
            
            cap = new DSJCaptura(pVideo, dsfg);
            cap.gravaAudio(arquivoDeVideo);
            cap.setVolume(0);
            
            JLabel[] vlb = new JLabel[6];
            vlb[0] = lbGauge0;
            vlb[1] = lbGauge8;
            vlb[2] = lbGauge20;
            vlb[3] = lbGauge39;
            vlb[4] = lbGauge40;
            vlb[5] = lbGauge41;
            
            ThreadMonitorMicrofone mMic = new ThreadMonitorMicrofone(vlb, lbRec, this, arquivoDeVideo);
            mMic.start();

            ClockThread cl = new ClockThread(cap, lbTempo);
            cl.start();	       

            MonitoraEspacoThread esp = new MonitoraEspacoThread(System.getProperty("user.dir").substring(0,2)+ "\\", lbEspacoLivre, lbTamanhoVideo, arquivoDeVideo);
            esp.start();
            
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));	            
		}
		catch (Exception e) {				
			e.printStackTrace();
        	logErro.gravaExcep("fidelis.view.FrameGravaAudiencia.gravaLog()", e);
        	logInfo.grava("fidelis.view.FrameGravaAudiencia.gravaLog()", usuarioLogado, "O Processo de gravação de áudio NÃO pôde ser inicializado!");
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showMessageDialog(null, "O Processo de gravação de áudio NÃO pôde ser inicializado!", "GRAVAÇÃO DE AUDIÊNCIA", JOptionPane.ERROR_MESSAGE);
			gravaVideoBackOut();
		}		
	}

	public void close(JInternalFrame f) {
		jifPai = f;		
	}
	
	@SuppressWarnings("unchecked")
	public void carregaJuizes() {
		//DBCorp corp = new DBCorp(usuarioLogado);
		DBCorp corp = new DBCorp(usuarioLogado, "Local");
		Vector vRet = corp.getJuizes();
		//corp.fechaConexao();
		((AutoFillField)tfJuiz).setPropriedades(vRet);
	}
}
