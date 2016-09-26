package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.GravaLog;
import model.Configuracoes;

@SuppressWarnings("serial")
public class PanelConfigGerais extends JPanel {
	
	private String usuarioLogado;
	
	public PanelConfigGerais(String strUsuarioLogado) {
		usuarioLogado = strUsuarioLogado;
		
		// TIPO DE LOGIN
		JPanel pCfgLogin = new JPanel();
		pCfgLogin.setLayout(null);
		pCfgLogin.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Login"));
		pCfgLogin.setBounds(8, 8, 250, 90);
		
		String [] strTipoLogin = {"1-Login Integrado com S.O.", "2-Cadastro de Usuários Interno"};
		final JComboBox cbTipoLogin = new JComboBox(strTipoLogin);
		cbTipoLogin.setBounds(15, 25, 220, 25);
		Configuracoes cfg = new Configuracoes();
		cbTipoLogin.setSelectedIndex(Integer.valueOf(cfg.getConfigParm("tipo_login")) - 1);
		JButton btTipoLogin = new JButton("Salvar");
		btTipoLogin.setBounds(155, 55, 80, 25);
		btTipoLogin.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				Configuracoes cfg = new Configuracoes();
				String strTipo = ((String)cbTipoLogin.getSelectedItem()).substring(0, 1);
				if (cfg.setConfigParm("tipo_login", strTipo)) {
					GravaLog log = new GravaLog("INFO");
					log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Tipo de Login configurado com sucesso! Valor = " + strTipo);
					JOptionPane.showMessageDialog(null, "Tipo de Login configurado com sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "Tipo de Login não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);
				}
			}		
		});		
		pCfgLogin.add(cbTipoLogin);
		pCfgLogin.add(btTipoLogin);
		
		
		// TIPO DE LOG
		JPanel pCfgLog = new JPanel();
		pCfgLog.setLayout(null);
		pCfgLog.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de LOG"));
		pCfgLog.setBounds(8, 110, 250, 90);
		
		String [] strTipoLog = {"1-Arquivo XML", "2-Arquivo TXT", "3-Ambos (XML e TXT)"};
		final JComboBox cbTipoLog = new JComboBox(strTipoLog);
		cbTipoLog.setBounds(15, 25, 220, 25);
		Configuracoes cfgLog = new Configuracoes();
		cbTipoLog.setSelectedIndex(Integer.valueOf(cfgLog.getConfigParm("tipo_log")) - 1);
		JButton btTipoLog = new JButton("Salvar");
		btTipoLog.setBounds(155, 55, 80, 25);
		btTipoLog.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				Configuracoes cfg = new Configuracoes();
				String strTipo = ((String)cbTipoLog.getSelectedItem()).substring(0, 1);
				if (cfg.setConfigParm("tipo_log", strTipo)) {
					GravaLog log = new GravaLog("INFO");
					log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Tipo de LOG configurado com sucesso! Valor = " + strTipo);
					JOptionPane.showMessageDialog(null, "Tipo de LOG configurado com sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "Tipo de LOG não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);
				}
			}		
		});		
		pCfgLog.add(cbTipoLog);
		pCfgLog.add(btTipoLog);
		
		
		// EXPURGO
		JPanel pCfgExp = new JPanel();
		pCfgExp.setLayout(null);
		pCfgExp.setBorder(javax.swing.BorderFactory.createTitledBorder("Expurgo de Audiências"));
		pCfgExp.setBounds(8, 210, 250, 90);
		
		JLabel lbInfo = new JLabel("Percentual limite p/ início do expurgo:");
		lbInfo.setBounds(10, 20, 240, 15);
		
		final JTextField tfPerc = new JTextField();
		tfPerc.setBounds(10, 42, 50, 25);
		tfPerc.setText(cfg.getConfigParm("expurgo"));
		
		JLabel lbPerc = new JLabel("%");
		lbPerc.setBounds(68, 47, 20, 15);
		
		JButton btSalvaExp = new JButton("Salvar");
		btSalvaExp.setBounds(155, 55, 80, 25);
		btSalvaExp.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int iPerc = 100;
				
				try {
					iPerc = Integer.valueOf(tfPerc.getText());
				}
				catch (Exception e) {}
								
				if (iPerc > 90 || iPerc < 1) {
					JOptionPane.showMessageDialog(null, "Por favor informe um valor percentual entre 1 e 90!", "Atenção", JOptionPane.WARNING_MESSAGE);
				}
				else {
					Configuracoes cfg = new Configuracoes();				
					if (cfg.setConfigParm("expurgo", tfPerc.getText())) {
						GravaLog log = new GravaLog("INFO");
						log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Percentual de Expurgo configurado com sucesso!");
						JOptionPane.showMessageDialog(null, "Percentual de Expurgo configurado com sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "Percentual de Expurgo não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);
					}
				}
			}		
		});		
		pCfgExp.add(lbInfo);
		pCfgExp.add(tfPerc);
		pCfgExp.add(lbPerc);
		pCfgExp.add(btSalvaExp);
		
		
		
		// ARQUIVOS DE IMAGEM PARA CABECALHO E RODAPE
		JPanel pCfgCab = new JPanel();
		pCfgCab.setLayout(null);
		pCfgCab.setBorder(javax.swing.BorderFactory.createTitledBorder("Arquivos de Imagem"));
		pCfgCab.setBounds(268, 8, 410, 192);
		
		JLabel lbCab = new JLabel("Cabeçalho");
		lbCab.setBounds(13, 20, 80, 15);		
		JLabel lbRod = new JLabel("Rodapé");
		lbRod.setBounds(13, 68, 80, 15);		
		JLabel lbFun = new JLabel("Fundo");
		lbFun.setBounds(13, 116, 80, 15);
		
		final JTextField ftCab = new JTextField(cfg.getConfigParm("cabecalho"));
		ftCab.setBounds(13, 38, 350, 20);
		final JTextField ftRod = new JTextField(cfg.getConfigParm("rodape"));
		ftRod.setBounds(13, 86, 350, 20);
		final JTextField ftFun = new JTextField(cfg.getConfigParm("fundo"));
		ftFun.setBounds(13, 134, 350, 20);		
		
		JButton btSelCab = new JButton("...");
		btSelCab.setBounds(368, 37, 25, 20);
		btSelCab.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Abrir");
		        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);        
		        int res = fc.showOpenDialog(null);		        
		        if (res == JFileChooser.APPROVE_OPTION) {
		        	File f = fc.getSelectedFile();
		        	ftCab.setText(f.getPath());
		        }
			}		
		});	
		
		JButton btSelRod = new JButton("...");
		btSelRod.setBounds(368, 85, 25, 20);
		btSelRod.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Abrir");
		        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);        
		        int res = fc.showOpenDialog(null);		        
		        if (res == JFileChooser.APPROVE_OPTION) {
		        	File f = fc.getSelectedFile();
		        	ftRod.setText(f.getPath());
		        }
			}		
		});	
		
		JButton btSelFun = new JButton("...");
		btSelFun.setBounds(368, 133, 25, 20);
		btSelFun.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Abrir");
		        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);        
		        int res = fc.showOpenDialog(null);		        
		        if (res == JFileChooser.APPROVE_OPTION) {
		        	File f = fc.getSelectedFile();
		        	ftFun.setText(f.getPath());
		        }
			}		
		});	
		
		JButton btSalvaImg = new JButton("Salvar");
		btSalvaImg.setBounds(282, 158, 80, 25);
		btSalvaImg.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				Configuracoes cfg = new Configuracoes();
				if (!cfg.setConfigParm("cabecalho", ftCab.getText())) 
					JOptionPane.showMessageDialog(null, "Cabeçalho não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);

				if (!cfg.setConfigParm("rodape", ftRod.getText())) 
					JOptionPane.showMessageDialog(null, "Rodapé não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);

				if (!cfg.setConfigParm("fundo", ftFun.getText())) 
					JOptionPane.showMessageDialog(null, "Imagem de Fundo não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);
				else {
					GravaLog log = new GravaLog("INFO");
					log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Arquivos de Imagens configurados com sucesso!");
					JOptionPane.showMessageDialog(null, "Arquivos de Imagens configurados com sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}
			}		
		});		
		
		pCfgCab.add(lbCab);
		pCfgCab.add(lbRod);
		pCfgCab.add(lbFun);
		pCfgCab.add(ftCab);
		pCfgCab.add(ftRod);
		pCfgCab.add(ftFun);
		pCfgCab.add(btSelCab);
		pCfgCab.add(btSelRod);
		pCfgCab.add(btSelFun);
		pCfgCab.add(btSalvaImg);
		
		
		// TRANSMISSAO SERVER
		JPanel pCfgServ = new JPanel();
		pCfgServ.setLayout(null);
		pCfgServ.setBorder(javax.swing.BorderFactory.createTitledBorder("Transmissão: Servidor"));
		pCfgServ.setBounds(268, 210, 410, 98);
		
		JLabel lbDirServ = new JLabel("Diretório de Recepção de Arquivos");
		lbDirServ.setBounds(13, 20, 250, 15);		
		final JTextField tfDirServ = new JTextField(cfg.getConfigParm("diretorio_servidor"));
		tfDirServ.setBounds(13, 38, 350, 20);
		JButton btSelDirServ = new JButton("...");
		btSelDirServ.setBounds(368, 37, 25, 20);
		btSelDirServ.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Abrir");
		        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);        
		        int res = fc.showOpenDialog(null);		        
		        if (res == JFileChooser.APPROVE_OPTION) {
		        	File f = fc.getSelectedFile();
		        	tfDirServ.setText(f.getPath());
		        }
			}		
		});	
		JButton btSalvaServ = new JButton("Salvar");
		btSalvaServ.setBounds(282, 63, 80, 25);
		btSalvaServ.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				Configuracoes cfg = new Configuracoes();
				if (!cfg.setConfigParm("diretorio_servidor", tfDirServ.getText())) 
					JOptionPane.showMessageDialog(null, "Diretório de Recepção de Arquivos não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);
				else {
					GravaLog log = new GravaLog("INFO");
					log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Diretório de Recepção de Arquivos configurados com sucesso!");
					JOptionPane.showMessageDialog(null, "Diretório de Recepção de Arquivos configurados com sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}
			}		
		});		
		
		pCfgServ.add(lbDirServ);
		pCfgServ.add(tfDirServ);
		pCfgServ.add(btSelDirServ);
		pCfgServ.add(btSalvaServ);
		
		
		
		// TRANSMISSAO CLIENTE
		JPanel pCfgCli = new JPanel();
		pCfgCli.setLayout(null);
		pCfgCli.setBorder(javax.swing.BorderFactory.createTitledBorder("Transmissão: Cliente"));
		pCfgCli.setBounds(268, 318, 410, 150);
		
		JLabel lbEnd = new JLabel("Nome ou IP do Servidor");
		lbEnd.setBounds(13, 20, 250, 15);		
		final JTextField tfEnd = new JTextField(cfg.getConfigParm("endereco_servidor"));
		tfEnd.setBounds(13, 38, 240, 20);
		
		JLabel lbPorta = new JLabel("Porta");
		lbPorta.setBounds(263, 20, 250, 15);		
		final JTextField tfPorta = new JTextField(cfg.getConfigParm("porta"));
		tfPorta.setBounds(263, 38, 100, 20);
		
		JLabel lbJanela = new JLabel("Janela de Transmissão");
		lbJanela.setBounds(13, 63, 180, 25);
		
		JLabel lbJanelaInicio = new JLabel("Inicio");
		lbJanelaInicio.setBounds(13, 90, 40, 20);
		
		final JFormattedTextField tfJanelaInicio = new JFormattedTextField(cfg.getConfigParm("horario_inicial"));
		tfJanelaInicio.setBounds(13, 110, 50, 20);
		try {
			tfJanelaInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		
		JLabel lbJanelaFim = new JLabel("Fim");
		lbJanelaFim.setBounds(130, 90, 40, 20);
		
		final JFormattedTextField tfJanelaFim = new JFormattedTextField(cfg.getConfigParm("horario_final"));
		tfJanelaFim.setBounds(130, 110, 50, 20);
		try {
			tfJanelaFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
		} catch (ParseException e) {			
			e.printStackTrace();
		}		
		
		JButton btSalvaCli = new JButton("Salvar");
		btSalvaCli.setBounds(282, 63, 80, 25);
		btSalvaCli.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				boolean flag_erro = false;
				Configuracoes cfg = new Configuracoes();
				if (!cfg.setConfigParm("endereco_servidor", tfEnd.getText())) {
					flag_erro = true;
					JOptionPane.showMessageDialog(null, "Endereço do Servidor não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);
				}
				else {
					GravaLog log = new GravaLog("INFO");
					log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Endereço do Servidor configurado com sucesso!");
					//JOptionPane.showMessageDialog(null, "Endereço do Servidor configurado com sucesso!", "fidelis.view.PanelConfigGerais()", JOptionPane.INFORMATION_MESSAGE);
				}
				
				if (!cfg.setConfigParm("porta", tfPorta.getText())) {
					flag_erro = true;
					JOptionPane.showMessageDialog(null, "Porta do Servidor não pôde ser configurada!", "Atenção", JOptionPane.ERROR_MESSAGE);
				}
				else {
					GravaLog log = new GravaLog("INFO");
					log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Porta do Servidor configurada com sucesso!");
					//JOptionPane.showMessageDialog(null, "Porta do Servidor configurada com sucesso!", "fidelis.view.PanelConfigGerais()", JOptionPane.INFORMATION_MESSAGE);
				}

				if (!cfg.setConfigParm("horario_inicial", tfJanelaInicio.getText())) {
					flag_erro = true;
					JOptionPane.showMessageDialog(null, "Horário Inicial não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);
				}
				else {
					GravaLog log = new GravaLog("INFO");
					log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Horário Inicial configurado com sucesso!");
					//JOptionPane.showMessageDialog(null, "Endereço do Servidor configurado com sucesso!", "fidelis.view.PanelConfigGerais()", JOptionPane.INFORMATION_MESSAGE);
				}

				if (!cfg.setConfigParm("horario_final", tfJanelaFim.getText())) {
					flag_erro = true;
					JOptionPane.showMessageDialog(null, "Horário Final não pôde ser configurado!", "Atenção", JOptionPane.ERROR_MESSAGE);
				}
				else {
					GravaLog log = new GravaLog("INFO");
					log.grava("fidelis.view.PanelConfigGerais()", usuarioLogado, "Horário Final configurado com sucesso!");
					//JOptionPane.showMessageDialog(null, "Endereço do Servidor configurado com sucesso!", "fidelis.view.PanelConfigGerais()", JOptionPane.INFORMATION_MESSAGE);
				}

				if (!flag_erro) {
					JOptionPane.showMessageDialog(null, "Dados da Transmissão Cliente configurados com SUCESSO!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
				}				
			}		
		});		
		
		pCfgCli.add(lbEnd);
		pCfgCli.add(tfEnd);
		pCfgCli.add(lbPorta);
		pCfgCli.add(tfPorta);
		pCfgCli.add(lbJanela);
		pCfgCli.add(lbJanelaInicio);
		pCfgCli.add(tfJanelaInicio);
		pCfgCli.add(lbJanelaFim);
		pCfgCli.add(tfJanelaFim);
		pCfgCli.add(btSalvaCli);

		
		
		this.setLayout(null);
		this.add(pCfgLogin);
		this.add(pCfgLog);
		this.add(pCfgExp);
		this.add(pCfgCab);
		this.add(pCfgServ);
		this.add(pCfgCli);
	}

}
