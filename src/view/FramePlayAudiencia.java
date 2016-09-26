package view;

import java.awt.Color;
import java.awt.Cursor;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import controller.BarraProgressaoPlayVideo;
import controller.DBDealer;
import controller.DSJCaptura;
import controller.ExportarAudiencia;
import controller.Ferramentas;
import controller.GeraHash;
import controller.GravaLog;
import controller.Sessao;
import controller.SessaoConfig;
import controller.Transcricao;
import de.humatic.dsj.DSFiltergraph;
import model.Audiencia;
import model.DBCorp;

@SuppressWarnings("serial")
public class FramePlayAudiencia extends JPanel {	

	private JPanel pVideo = null;
	private JPanel pControle = null;
	private JPanel pListagem = null;
	private JPanel pVinculacao = null;
	private JButton btStop = new JButton(new ImageIcon(System.getProperty("user.dir") + "/images/Stop.png"));
	private JButton btStart = new JButton (new ImageIcon(System.getProperty("user.dir") + "/images/Start.png"));
	private JButton btExportar = new JButton("Exportar");
	private JButton btTranscricao = new JButton("Transcrever");
	private JButton btIrMarca = new JButton("Ir Para Marca");
	private JButton btVincular = new JButton("Vincular");
	private JSlider js = null;
	private JSlider jsVolume;
	private JLabel lbTime = null;
	private Ferramentas tmFmt = null;
	private DefaultTableModel tablemodel;
	private JTable tbMarca;
	private JScrollPane sp;
	
	private DefaultTableModel tablemodelVinculacao;
	private JTable tbMarcaVinculacao;
	private JScrollPane spVinculacao;
	
	private JLabel lbNumProcessoVlr;
	private JLabel lbTipoProcessoVlr;
	private JLabel lbJuizVlr;
	private JLabel lbDataVlr;
	private JLabel lbHoraVlr;
	
	private JLabel lbNumProcesso;
	private JLabel lbTipoProcesso;
	private JLabel lbJuiz;
	private JLabel lbData;
	private JLabel lbHora;
	
	private JLabel lbMensagem;	
	private BarraProgressaoPlayVideo barraProg = null;
	private String usuarioLogado = null;	
	private DSFiltergraph dsfg;
	DSJCaptura cap = null;
	private boolean isVinculacao;
	private boolean fPlayer;
	private String mediaPath;
	
	
	public FramePlayAudiencia(String strUsuarioLogado, boolean vincular) {
		usuarioLogado = strUsuarioLogado;
		isVinculacao = vincular;
		
		pVideo = new JPanel();
		pVideo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pVideo.setBounds(5, 105, 340, 255);
		
		pControle = new JPanel();
		pControle.setLayout(null);
		pControle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		pControle.setBounds(5, 365, 340, 75);
		
		pListagem = new JPanel();
		pListagem.setLayout(null);
		if (isVinculacao)
			pListagem.setBounds(355, 105, 500, 255);
		else
			pListagem.setBounds(355, 105, 500, 400);
		//pListagem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		if (isVinculacao) {
			pVinculacao = new JPanel();
			pVinculacao.setLayout(null);
			pVinculacao.setBounds(355, 365, 500, 185);
		}
		
		JPanel pDados = new JPanel();
		pDados.setLayout(null);
		pDados.setBounds(5, 5, 840, 95);
		pDados.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		btExportar.setBounds(5, 446, 165, 30);
		btExportar.setEnabled(false);
		
		btTranscricao.setBounds(5, 481, 165, 30);
		btTranscricao.setEnabled(false);
		//if (!SessaoConfig.isAdmin) {
			btTranscricao.setVisible(false);
		//}
		
		btIrMarca.setBounds(179, 446, 165, 30);
		btIrMarca.setEnabled(false);
		
		btVincular.setBounds(179, 481, 165, 30);
		
		lbMensagem = new JLabel("");
		lbMensagem.setBounds(5, 513, 300, 20);

		this.setLayout(null);
		this.add(pDados);
		this.add(pVideo);
		this.add(pControle);
		this.add(pListagem);
		if (!isVinculacao) {
			this.add(btExportar);
			this.add(btTranscricao);
		}
		else {
			this.add(btVincular);
			this.add(pVinculacao);
		}
		
		this.add(btIrMarca);		
		this.add(lbMensagem);		

		lbNumProcesso = new JLabel("Nr. Processo: ");
		lbNumProcesso.setBounds(10, 8, 100, 20);
		lbNumProcesso.setAlignmentY(RIGHT_ALIGNMENT);
		lbNumProcessoVlr = new JLabel(Sessao.numProcesso);
		lbNumProcessoVlr.setBounds(120, 8, 200, 20);
		//lbNumProcessoVlr.setFont(new Font("Monospaced", Font.PLAIN & Font.BOLD, 12));
		lbNumProcessoVlr.setForeground(Color.GRAY);
		
		lbTipoProcesso = new JLabel("Tipo: ");
		lbTipoProcesso.setBounds(10, 38, 60, 20);
		lbTipoProcesso.setAlignmentY(RIGHT_ALIGNMENT);
		lbTipoProcessoVlr = new JLabel(Sessao.tipo);
		lbTipoProcessoVlr.setBounds(120, 38, 100, 20);
		//lbTipoProcessoVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lbTipoProcessoVlr.setForeground(Color.GRAY);
		
		lbJuiz = new JLabel("Juiz: ");
		lbJuiz.setBounds(270, 38, 60, 20);
		lbJuiz.setAlignmentY(RIGHT_ALIGNMENT);
		lbJuizVlr = new JLabel(Sessao.juiz);
		lbJuizVlr.setBounds(335, 38, 200, 20);
		//lbJuizVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lbJuizVlr.setForeground(Color.GRAY);

		lbData = new JLabel("Data: ");
		lbData.setBounds(10, 68, 60, 20);
		lbData.setAlignmentY(RIGHT_ALIGNMENT);
		lbDataVlr = new JLabel(Sessao.data);
		lbDataVlr.setBounds(120, 68, 100, 20);
		//lbDataVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lbDataVlr.setForeground(Color.GRAY);

		lbHora= new JLabel("Hora: ");
		lbHora.setBounds(270, 68, 60, 20);
		lbHora.setAlignmentY(RIGHT_ALIGNMENT);
		lbHoraVlr = new JLabel(Sessao.hora);
		lbHoraVlr.setBounds(335, 68, 100, 20);
		//lbHoraVlr.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lbHoraVlr.setForeground(Color.GRAY);
		
		pDados.add(lbNumProcesso);
		pDados.add(lbNumProcessoVlr);
		pDados.add(lbTipoProcesso);
		pDados.add(lbTipoProcessoVlr);
		pDados.add(lbJuiz);
		pDados.add(lbJuizVlr);
		pDados.add(lbData);
		pDados.add(lbDataVlr);
		pDados.add(lbHora);
		pDados.add(lbHoraVlr);

		js = new JSlider(SwingConstants.HORIZONTAL, 0, 360, 0);		
		
		jsVolume = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
		jsVolume.setBounds(269, 35, 60, 25);
		jsVolume.setValue(100);

		tmFmt = new Ferramentas();
			
		lbTime = new JLabel(tmFmt.formataHora(0));
		lbTime.setBounds(5, 310, 100, 15);
		
		String fp = tmFmt.getPropriedades("player");
		if (fp == null || !fp.equals("1")) {
			fPlayer = false;
		}
		else {
			fPlayer = true;
			//mediaPath = tmFmt.getPropriedades("videos");
			mediaPath = SessaoConfig.videoPath;
		}
				
        setDadosProcesso();
		
		btStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (cap != null) {
            		cap.pararVideo();
            		btIrMarca.setEnabled(false);
            	}
            }
        });		
		
		btStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (cap != null) 
            		cap.play();
            	else {
	        		if (carregaVideo())            	
	        			if (!barraProg.isAlive()) {
	        				barraProg.start();
	        			}            	
            	}
            	
            	btIrMarca.setEnabled(true);
            }
        });		
		
		btIrMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (tbMarca.getSelectedRow() >= 0) {
            		Ferramentas seg = new Ferramentas();
            		js.setValue(seg.getSegundos((String) tbMarca.getValueAt(tbMarca.getSelectedRow(), 0)));
            	}
            	else {
            		JOptionPane.showMessageDialog(null, "Selecione uma Marcação !!", "VideoCap", JOptionPane.WARNING_MESSAGE);            		
            	}
            }
        });		
		
		btVincular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {            	
            	if (tbMarca.getSelectedRow() < 0)
            		JOptionPane.showMessageDialog(null, "Selecione uma marcação!", "Atenção", JOptionPane.ERROR_MESSAGE);
            	else 
            		if (tbMarcaVinculacao.getSelectedRow() < 0)
            			JOptionPane.showMessageDialog(null, "Selecione um assunto CNJ!", "Atenção", JOptionPane.ERROR_MESSAGE);
            		else {            			
            			int linhaMarca   = tbMarca.getSelectedRow();
            			int linhaAssunto = tbMarcaVinculacao.getSelectedRow();
            	    	String strMsg = "Dados da Vinculação:\n";
            	    	strMsg += "\nTema: " + tbMarca.getValueAt(linhaMarca, 1);
            	    	strMsg += "\n\nAssunto: " + tbMarcaVinculacao.getValueAt(linhaAssunto, 0);
            	    	strMsg += "\n\nTempo na gravação: " + tbMarca.getValueAt(linhaMarca, 0);
            	    	strMsg += "\n\nDeseja efetivar esta vinculação?";
            	    	
            			Object[] options = { "Sim", "Não" };
                    	int resposta = JOptionPane.showOptionDialog(null, strMsg, "Vinculação de Assuntos CNJ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                		
            	    	if (resposta == 0) {
	            			int linha = tbMarca.getSelectedRow();
	            			tablemodel.insertRow(linha + 1, new String[] {null, null, null, null, null});
	            			tbMarca.setValueAt(tbMarca.getValueAt(linha, 0), linha + 1, 0);
	            			tbMarca.setValueAt(tbMarca.getValueAt(linha, 1), linha + 1, 1);
	            			tbMarca.setValueAt(tbMarcaVinculacao.getValueAt(tbMarcaVinculacao.getSelectedRow(), 0), linha + 1, 2);
	            			tbMarca.setValueAt(tbMarca.getValueAt(linha, 3), linha + 1, 3);
	            			tbMarca.setValueAt(tbMarca.getValueAt(linha, 4), linha + 1, 4);
	            			
	            			GravaLog logInfo = new GravaLog("INFO");
	            			
	            			DBDealer deal = new DBDealer(usuarioLogado, "MARCACOES", Sessao.numProcesso, Sessao.data, Sessao.hora);
	        	    		deal.incluir((String)tbMarca.getValueAt(linha + 1, 0),(String)tbMarca.getValueAt(linha + 1, 1), (String)tbMarca.getValueAt(linha + 1, 2), (String)tbMarca.getValueAt(linha + 1, 3), (String)tbMarca.getValueAt(linha + 1, 4));
	        	    		logInfo.grava("fidelis.view.FramePlayAudiencia.btVincular()", usuarioLogado, "Tempo de Marcação: " + (String)tbMarca.getValueAt(linha + 1, 0) + " - Assunto Vinculado: " + (String)tbMarca.getValueAt(linha + 1, 2));
	        	    		
	        	    		DBDealer dealer = new DBDealer(usuarioLogado, "AUDIENCIAS", Sessao.numProcesso, Sessao.data, Sessao.hora);
	        	    		if (dealer.mudaStatus("VINCULADO")) {
	        	    			logInfo.grava("fidelis.view.FramePlayAudiencia.btVincular()", usuarioLogado, "Status do Processo " + Sessao.numProcesso + " alterado para VINCULADO!");
	        	    		}
            			}
            		}
            	
            }
        });
	
		btExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            	if (Sessao.numProcesso != null && !Sessao.numProcesso.equals("0000000-00.0000.0.00.0000"))
            		lbMensagem.setText("Exportando audiência. Aguarde ...");
            	else
            		lbMensagem.setText("Exportando evento. Aguarde ...");
            	lbMensagem.setForeground(Color.RED);
    			GravaLog log = new GravaLog("INFO");
    			log.grava("fidelis.view.FramePlayAudiencia.btExportar()", usuarioLogado, "Processo: " + Sessao.numProcesso + " sendo exportado.");
            	ExportarAudiencia export = new ExportarAudiencia(usuarioLogado, Sessao.numProcesso, Sessao.data, Sessao.hora);
            	if (export.escolheDiretorio()) {
                	lbMensagem.setText("");
                	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                	if (Sessao.numProcesso != null && !Sessao.numProcesso.equals("0000000-00.0000.0.00.0000"))
                		JOptionPane.showMessageDialog(null, "Exportação da Audiência Concluída", "Exportar Audiência", JOptionPane.INFORMATION_MESSAGE);
                	else
                		JOptionPane.showMessageDialog(null, "Exportação do Evento Concluída", "Exportar Evento", JOptionPane.INFORMATION_MESSAGE);
            	}
            	else {
                	lbMensagem.setText("");
                	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                	if (Sessao.numProcesso != null && !Sessao.numProcesso.equals("0000000-00.0000.0.00.0000"))                		
                		JOptionPane.showMessageDialog(null, "Exportação da Audiência NÃO Concluída", "Exportar Audiência", JOptionPane.ERROR_MESSAGE);
                	else
                		JOptionPane.showMessageDialog(null, "Exportação do Evento NÃO Concluída", "Exportar Evento", JOptionPane.ERROR_MESSAGE);
            	}
            }
        });		
		
		btTranscricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Transcricao tr = new Transcricao(btTranscricao);
            	tr.start();
            }
        });	
			
		jsVolume.addChangeListener(new ChangeListener() {				
			public void stateChanged(ChangeEvent e) {
				JSlider comp = (JSlider) e.getSource();
				if (!comp.getValueIsAdjusting()) {		
					if (cap != null)
						cap.setVolume(Float.valueOf(jsVolume.getValue())/100);
				}
			}
		});	
		

	}
	
	public void finalizaMedia() {
		if (cap != null) {
			cap.pararVideo();
			cap.finalizaAudio();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setDadosProcesso() {
		if (cap != null) {
			cap.finalizaAudio();
		}
		cap = null;
		
		if (barraProg != null) {
			barraProg.parar();
		}
		
		if (sp != null) {
			pListagem.remove(sp);
		}
		
		if (spVinculacao != null) {
			pVinculacao.remove(spVinculacao);
		}
		
		btIrMarca.setEnabled(false);
		        
		// Monta a JTable dos marcadores
		tablemodel = new DefaultTableModel(){   
			public boolean isCellEditable(int rowIndex, int mColIndex) {   
				return false;   
			}   
		};
		
		if (Sessao.numProcesso != null && !Sessao.numProcesso.equals("0000000-00.0000.0.00.0000"))
			tablemodel.setColumnIdentifiers(new String[] {"Tempo", "Assunto", "SubTema", "Qualificação", "Depoente"});
		else
			tablemodel.setColumnIdentifiers(new String[] {"Tempo", "Assunto", "SubTema", "Qualificação", "Orador"});		
		tbMarca = new JTable(tablemodel);
		tbMarca.setModel(tablemodel);
		tbMarca.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//tbMarca.setBounds(1, 1, 500, 390);
		// Redimenciona o tamanho da coluna TEMPO
		javax.swing.table.TableColumnModel colmod = tbMarca.getColumnModel();
		colmod.getColumn(0).setMaxWidth(60);

        sp = new JScrollPane(tbMarca);
        if (isVinculacao)
        	sp.setBounds(0, 0, 490, 255);
        else
        	sp.setBounds(0, 0, 490, 373);
        pListagem.add(sp);
        
        DBDealer dealer = new DBDealer(usuarioLogado, "MARCACOES", Sessao.numProcesso, Sessao.data, Sessao.hora);
        Vector vtRetorno = dealer.listar();
        
		String [] strRetorno = new String[5];
		for (int i = 0; i < vtRetorno.size(); i++) {
			strRetorno = (String[]) vtRetorno.elementAt(i);
			
	        tablemodel.addRow(new String[] {null, null, null, null, null});
			tbMarca.setValueAt(strRetorno[0], i, 0);
			tbMarca.setValueAt(strRetorno[1], i, 1);
			tbMarca.setValueAt(strRetorno[2], i, 2);
			tbMarca.setValueAt(strRetorno[3], i, 3);
			tbMarca.setValueAt(strRetorno[4], i, 4);
		}		
		
		if (isVinculacao) {
			tablemodelVinculacao = new DefaultTableModel(){   
				public boolean isCellEditable(int rowIndex, int mColIndex) {   
					return false;   
				}   
			};
			
			tablemodelVinculacao.setColumnIdentifiers(new String[] {"Assunto CNJ - Descrição"});		
			tbMarcaVinculacao = new JTable(tablemodelVinculacao);
			tbMarcaVinculacao.setModel(tablemodelVinculacao);
			tbMarcaVinculacao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	        spVinculacao = new JScrollPane(tbMarcaVinculacao);
	        spVinculacao.setBounds(0, 0, 490, 185);
	        pVinculacao.add(spVinculacao);
	        
	        while(tablemodelVinculacao.getRowCount() > 0)
	        	tablemodelVinculacao.removeRow(0);	
	        
	        if (Sessao.numProcesso != null) {
		        DBCorp corp = new DBCorp(usuarioLogado);
		        Vector vtRetAssunto = corp.getAssuntos(Sessao.numProcesso);		   
		        //corp.fechaConexao();

		        for (int i = 0; i < vtRetAssunto.size(); i++) {				
			        tablemodelVinculacao.addRow(new String[] {null});
					tbMarcaVinculacao.setValueAt((String) vtRetAssunto.elementAt(i), i, 0);
		        }		        
	        }
		}				
		
		btStop.setToolTipText("Parar/Pausar");
		btStart.setToolTipText("Iniciar");
		
		lbTime.setBounds(10, 10, 60, 15);
		js.setBounds(70, 10, 260, 20);
		
		btStop.setBounds(10, 35, 30, 30);
		btStart.setBounds(45, 35, 30, 30);
		
		JLabel lbVolume = new JLabel("Volume: ");
		lbVolume.setBounds(215, 35, 70, 20);
		
		pControle.add(btStop);
		pControle.add(btStart);
		pControle.add(lbVolume);
		pControle.add(jsVolume);
		pControle.add(js);
		pControle.add(lbTime);
		
		if (Sessao.numProcesso != null && !Sessao.numProcesso.equals("0000000-00.0000.0.00.0000")) {
			lbNumProcesso.setVisible(true);
			lbJuiz.setVisible(true);
			lbTipoProcesso.setText("Tipo:");
			lbNumProcessoVlr.setText(Sessao.numProcesso);
			lbTipoProcessoVlr.setText(Sessao.tipo);
			lbJuizVlr.setText(Sessao.juiz);
			lbDataVlr.setText(Sessao.data);
			lbHoraVlr.setText(Sessao.hora);
		}
		else {
			// Monta o display de Eventos dinamicamente aproveitando a posicao dos labels do processo
			lbNumProcesso.setVisible(false);
			lbJuiz.setVisible(false);
			lbTipoProcesso.setText("Evento:");			
			lbTipoProcessoVlr.setText(Sessao.juiz);
			lbTipoProcessoVlr.setSize(400, 20);
			lbDataVlr.setText(Sessao.data);
			lbHoraVlr.setText(Sessao.hora);			
		}
		
		if (Sessao.numProcesso != null) {
			GravaLog log = new GravaLog("INFO");
			log.grava("fidelis.view.FramePlayAudiencia.setDadosProcesso()", usuarioLogado, "Processo: " + Sessao.numProcesso + " sendo visualizado.");
			btExportar.setEnabled(true);
			btTranscricao.setEnabled(true);
		}
		
		return;
	}
	
	public boolean carregaVideo() {
		String mediaFile = null;
		
		if (Sessao.numProcesso == null || Sessao.numProcesso.equals("")) { 
			mediaFile = "";
			return false;
		}
		else {
			if (fPlayer)
				mediaFile = mediaPath + "/" + Sessao.fileName(Sessao.numProcesso, Sessao.data, Sessao.hora) + ".asf";
			else					
				mediaFile = System.getProperty("user.dir") + "/videos/" + Sessao.fileName(Sessao.numProcesso, Sessao.data, Sessao.hora) + ".asf";
		}
		/**
		 *  Verifica se o arquivo que serah transmitido eh uma audiencia/evento integro.
		 */
		GeraHash gh = new GeraHash();
		String hashArqTmp = gh.getHashFromFile(mediaFile);
		
		Audiencia aud = new Audiencia(Sessao.numProcesso, Sessao.data, Sessao.hora);
		String hashProcesso = aud.getHashProcesso();
		
		if (hashArqTmp == null || !hashArqTmp.equals(hashProcesso)) {
			GravaLog log = new GravaLog("INFO");
			log.grava("fidelis.controller.FramePlayAudiencia.carregaVideo()", usuarioLogado, "Media (.asf) do Processo: " + Sessao.numProcesso + ", " + Sessao.data + ", " + Sessao.hora + " está corrompido. Checagem de HASH não validou o arquivo de média. Contacte o Administrador.");
			JOptionPane.showMessageDialog(null, "O HASH do arquivo de Áudio/Vídeo NÃO é válido ou íntegro. Contate o Administrador do Sistema!", "Arquivo de Áudio/Vídeo Corrompido", JOptionPane.ERROR_MESSAGE);
			return false;
		}
			
		/**/
		
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			cap = new DSJCaptura(pVideo, dsfg);			
			cap.visualizaVideo(mediaFile);
			cap.isPlaying = false;
			
			barraProg = new BarraProgressaoPlayVideo(cap, js, lbTime, tbMarca);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			js.setMaximum((int) cap.getTempoTotal());	
			//js.setMaximum((int) cap.getMiliTempoTotal());
			
		}
		catch (Exception e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.view.FramePlayAudiencia.carregaVideo()", e);			
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao carregar o vídeo. Informação gravadas na log.\n" + e.getMessage(), "Visualizar Audiência/Evento", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		js.addChangeListener(new ChangeListener() {				
			public void stateChanged(ChangeEvent e) {
				JSlider comp = (JSlider) e.getSource();
				if (!comp.getValueIsAdjusting()) {
					if (cap != null) {
						int tempoBarra = js.getValue();
						int tempoVideo = cap.getSegundos();
						if (tempoBarra > tempoVideo + 2 || tempoBarra < tempoVideo - 2) {
							cap.setTempo(js.getValue());
							//cap.setTempo(js.getValue() / 1000);
							lbTime.setText(tmFmt.formataHora(js.getValue()));
							//lbTime.setText(tmFmt.formataHora(js.getValue() / 1000));
						}
					}
					else {
						js.setValue(0);
						lbTime.setText(tmFmt.formataHora(js.getValue()));
					}
						
				}
			}
		});		

		return true;
	}
}
