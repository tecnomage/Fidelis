package view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.Criptografia;
import controller.SessaoConfig;
import model.BancodeDados;

@SuppressWarnings("serial")
public class PanelConfigDB extends JPanel {
	
	//private String usuarioLogado;
	private JLabel lbServidor;
	private JLabel lbPorta;
	private JLabel lbXE;
	private JLabel lbDatabase;
	private JLabel lbUsuario;
	private JLabel lbSenha;
	
	public JTextField tfServidor;
	public JTextField tfPorta;
	public JTextField tfXE;
	public JTextField tfDatabase;
	public JTextField tfUsuario;
	public JPasswordField pfSenha;
	
	public JComboBox cbDB;
	public JButton btDB;
	public JPanel pDB;
	
	public PanelConfigDB(String strUsuarioLogado) {
		//usuarioLogado = strUsuarioLogado;
		
		pDB = new JPanel();
		pDB.setLayout(null);
		pDB.setBorder(javax.swing.BorderFactory.createTitledBorder("Escolha do SGBD"));
		pDB.setBounds(8, 8, 300, 280);
		
		String [] strSGBD = {"Microsoft Access", "Oracle Express"};
		cbDB = new JComboBox(strSGBD);
		cbDB.setBounds(15, 25, 270, 25);
		
		lbServidor = new JLabel("Servidor:");
		lbServidor.setBounds(15, 60, 70, 20);
		tfServidor = new JTextField();
		tfServidor.setBounds(90, 60, 195, 20);
		
		lbPorta = new JLabel("Porta:");
		lbPorta.setBounds(15, 90, 70, 20);
		tfPorta = new JTextField();
		tfPorta.setBounds(90, 90, 195, 20);
		
		lbXE = new JLabel("SID:");
		lbXE.setBounds(15, 120, 70, 20);
		tfXE = new JTextField();
		tfXE.setBounds(90, 120, 195, 20);
		
		lbDatabase = new JLabel("Database:");
		lbDatabase.setBounds(15, 150, 70, 20);
		tfDatabase = new JTextField();
		tfDatabase.setBounds(90, 150, 195, 20);
		
		lbUsuario = new JLabel("Usuário:");
		lbUsuario.setBounds(15, 180, 70, 20);
		tfUsuario = new JTextField();
		tfUsuario.setBounds(90, 180, 195, 20);
		
		lbSenha = new JLabel("Senha:");
		lbSenha.setBounds(15, 210, 70, 20);
		pfSenha = new JPasswordField();
		pfSenha.setBounds(90, 210, 195, 20);

		btDB = new JButton("Salvar");
		btDB.setBounds(205, 240, 80, 25);
		btDB.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gravaIniDb((String) cbDB.getSelectedItem());				
			}
		});		
		pDB.add(cbDB);
		pDB.add(lbServidor);
		pDB.add(tfServidor);
		pDB.add(lbPorta);
		pDB.add(tfPorta);
		pDB.add(lbXE);
		pDB.add(tfXE);
		pDB.add(lbDatabase);
		pDB.add(tfDatabase);
		pDB.add(lbUsuario);
		pDB.add(tfUsuario);
		pDB.add(lbSenha);
		pDB.add(pfSenha);
		pDB.add(btDB);
		this.setLayout(null);
		this.add(pDB);
		
		getDbConfig();
	}
	
/*	public void getDbConfig() {
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();		
			Document doc = builder.parse(new File(System.getProperty("user.dir") + "/sgbd.xml"));
	
			NodeList nodes = doc.getElementsByTagName("sgbd");
			
			for (int i = 0; i < nodes.getLength(); i++) {				
				Element elemento = (Element) nodes.item(i);
				
				NodeList node = elemento.getElementsByTagName("nome");
				Element linha = (Element) node.item(0);
				cbDB.setSelectedItem((String) linha.getTextContent());
				
				node = elemento.getElementsByTagName("servidor");
				linha = (Element) node.item(0);
				tfServidor.setText(linha.getTextContent());
				
				node = elemento.getElementsByTagName("porta");
				linha = (Element) node.item(0);
				tfPorta.setText(linha.getTextContent());
				
				node = elemento.getElementsByTagName("sid");
				linha = (Element) node.item(0);
				tfXE.setText(linha.getTextContent());
	
				node = elemento.getElementsByTagName("database");
				linha = (Element) node.item(0);
				tfDatabase.setText(linha.getTextContent());
	
				node = elemento.getElementsByTagName("usuario");
				linha = (Element) node.item(0);
				tfUsuario.setText(linha.getTextContent());
	
				Criptografia crp = new Criptografia();
				node = elemento.getElementsByTagName("senha");
				linha = (Element) node.item(0);
				pfSenha.setText(crp.decriptografar(linha.getTextContent()));
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/	
	
	public String [] getDbConfig() {
		String [] strRet = new String[7];
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/sgbd.ini"));

			cbDB.setSelectedItem((String) br.readLine());
			tfServidor.setText(br.readLine());
			tfPorta.setText(br.readLine());
			tfXE.setText(br.readLine());
			tfDatabase.setText(br.readLine());
			tfUsuario.setText(br.readLine());
			
			char [] charBuffer = new char[22];
			if (br.read(charBuffer) > 21) {			
				Criptografia crp = new Criptografia();
				pfSenha.setText(crp.decriptografar(new String(charBuffer, 0, 22))); // senha
			}
			else
				pfSenha.setText("");

			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return strRet;
	}
	
	public void gravaIniDb(String strSGBD) {
		
		try {
			Criptografia crp = new Criptografia();
			BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/sgbd.ini"));
			/*
			bw.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"); bw.newLine();
			bw.write("<sgbd>");	bw.newLine();
			bw.write("   <nome>" + strSGBD + "</nome>"); bw.newLine();
			bw.write("   <servidor>" + tfServidor.getText() + "</servidor>"); bw.newLine();
			bw.write("   <porta>" + tfPorta.getText() + "</porta>"); bw.newLine();
			bw.write("   <sid>" + tfXE.getText() + "</sid>"); bw.newLine();
			bw.write("   <database>" + tfDatabase.getText() + "</database>"); bw.newLine();
			bw.write("   <usuario>" + tfUsuario.getText() + "</usuario>"); bw.newLine();
			bw.write("   <senha>" + crp.criptografar(String.valueOf(pfSenha.getPassword())) + "</senha>"); bw.newLine();
			bw.write("</sgbd>"); bw.newLine();
			*/
			bw.write(strSGBD); bw.newLine();
			bw.write(tfServidor.getText()); bw.newLine();
			bw.write(tfPorta.getText()); bw.newLine();
			bw.write(tfXE.getText()); bw.newLine();
			bw.write(tfDatabase.getText()); bw.newLine();
			bw.write(tfUsuario.getText()); bw.newLine();
			bw.write(crp.criptografar(String.valueOf(pfSenha.getPassword()))); bw.newLine();
			bw.flush();
			bw.close();
			JOptionPane.showMessageDialog(null, "Escolha do SGBD configurada com sucesso.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
			
			if (!strSGBD.equals("Microsoft Access")) {
				Object[] options = { "Sim", "Não" };
	        	int confirma = JOptionPane.showOptionDialog(null, "Deseja criar as tabelas/objetos do sistema no " + strSGBD + "?\nCUIDADO: respondendo SIM todas as tabelas, caso existam, serão removidas e criadas novamente.", "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	            
	            if (confirma == 0) {
	            	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	            	SessaoConfig.conexao = null;
	            	BancodeDados newdb = new BancodeDados();
	            	if (newdb.getConexao() != null) {
	            		newdb.removeTabelas();
	            		if (newdb.criaTabelas()) {
	            			newdb.copiaAjuda();
	            			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));	            				            			
	            			confirma = JOptionPane.showOptionDialog(null, "Tabelas/Objetos criados com SUCESSO.\nDeseja COPIAR os dados do BD antigo para o BD novo?", "ATENÇÃO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	            			
	            			if (confirma == 0) {
	            				if (!newdb.copiaVCTipoAudiencias() ||
	            				    !newdb.copiaVCMotivoFinalizacoes() ||
	            				    !newdb.copiaVCAudiencias() ||
	            			        !newdb.copiaVCTemas() ||
	            			        !newdb.copiaVCQualif_Dep() ||
	            			        !newdb.copiaVCMarcacoes() ||
	            			        !newdb.copiaVCPerfil() ||
	            			        !newdb.copiaVCUsuarios() ||
	            			        !newdb.copiaVCConfig())
	            					JOptionPane.showMessageDialog(null, "Erro ao copiar dados do BD antigo, verifique as mensagens de output.\nRemova o arquivo SGBD.INI para iniciar o sistema novamente pelo MS Access!", "Atenção", JOptionPane.ERROR_MESSAGE);
	            			}
	            			
	            			JOptionPane.showMessageDialog(null, "Configuração do Banco de Dados CONCLUÍDA.\nO sistema precisa ser fechado para que a alteração tenha efeito.", "Atenção", JOptionPane.INFORMATION_MESSAGE);	            			
	            			System.exit(0);
	            		}
	            		else {
	            			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		            		JOptionPane.showMessageDialog(null, "Erro ao criar as tabelas no Banco de Dados!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
	            		}
	            	}
	            	else {
	            		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	            		JOptionPane.showMessageDialog(null, "A conexão com o banco de dados não foi estabelecida!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
	            	}
	            }
	            else {
	            	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	            	JOptionPane.showMessageDialog(null, "O sistema precisa ser fechado para que a alteração tenha efeito.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
	            	System.exit(0);
	            }
			}
			else {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(null, "O sistema precisa ser fechado para que a alteração tenha efeito.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            	System.exit(0);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
