package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import fidelis.controller.CheckAdmin;
import fidelis.controller.Ferramentas;
import fidelis.controller.GeraHash;
import fidelis.controller.GravaLog;
import fidelis.controller.SessaoConfig;
import fidelis.controller.ValidaChave;
import fidelis.model.Configuracoes;
import fidelis.model.Operador;
import fidelis.model.Usuario;

@SuppressWarnings("serial")
public class Login extends JFrame {
	
	private String usuarioLogado = null;
	private JLabel lbNovaSenha;
	private JPasswordField tfNovaSenha;
	private JLabel lbConfSenha;
	private JPasswordField tfConfSenha;
	private JButton btAlterar;
	
	public Login() {		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(405, 225);
		setTitle("FIDELIS: Acesso ao Sistema");
		
		Container cont = getContentPane();
		
		JPanel painel = new JPanel();
		painel.setLayout(null);
		painel.setBounds(1, 1, 340, 160);
		
		JLabel lbCadeado = new JLabel(new ImageIcon("images/cadeado.jpg"));
		lbCadeado.setBounds(5, 5, 110, 120);

		btAlterar = new JButton("Alterar Senha");
		btAlterar.setBounds(5, 155, 120, 25);
		
		JButton btOk = new JButton("Acessar");
		btOk.setBounds(189, 155, 90, 25);
		
		JButton btCancel = new JButton("Sair");
		btCancel.setBounds(289, 155, 90, 25);

		JLabel lbUser = new JLabel("Usuário:");
		lbUser.setBounds(168, 20, 70, 15);
		final JTextField tfUser = new JTextField(30);	
		tfUser.setBounds(230, 15, 150, 25);
		//tfUser.setText("admin");

		JLabel lbSenha = new JLabel("Senha:");
		lbSenha.setBounds(176, 55, 50, 15);
		final JPasswordField tfSenha = new JPasswordField(40);
		tfSenha.setBounds(230, 50, 150, 25);
		//tfSenha.setText("admin");
		
		lbNovaSenha = new JLabel("Nova Senha:");
		lbNovaSenha.setBounds(145, 90, 80, 15);
		lbNovaSenha.setVisible(false);
		
		tfNovaSenha = new JPasswordField(40);
		tfNovaSenha.setBounds(230, 85, 150, 25);
		tfNovaSenha.setVisible(false);
		
		lbConfSenha = new JLabel("Confirma Senha:");
		lbConfSenha.setBounds(121, 125, 105, 15);
		lbConfSenha.setVisible(false);
		
		tfConfSenha = new JPasswordField(40);
		tfConfSenha.setBounds(230, 120, 150, 25);
		tfConfSenha.setVisible(false);
		
		painel.add(lbCadeado);
		painel.add(lbUser);
		painel.add(tfUser);
		painel.add(lbSenha);
		painel.add(lbNovaSenha);
		painel.add(lbConfSenha);
		painel.add(tfSenha);
		painel.add(tfNovaSenha);
		painel.add(tfConfSenha);
		painel.add(btAlterar);
		painel.add(btOk);
		painel.add(btCancel);
		
		cont.add(painel);
	
		btOk.addActionListener(new java.awt.event.ActionListener() {			
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (tfUser.getText().toString().equals("") || tfUser.getText() == null) {
            		JOptionPane.showMessageDialog(null, "Informe o nome do usuário", "Acesso ao Sistema", JOptionPane.INFORMATION_MESSAGE);
            	}
            	else {
            		Usuario user = new Usuario();
            		GeraHash gh = new GeraHash();
            		boolean continua = true;
            		
            		tfUser.setText(tfUser.getText().toUpperCase());
            		           		
            		String strSenhaEncrip = gh.getHashFromString(String.valueOf(tfSenha.getPassword()));
            		if (user.validaLogin(tfUser.getText(), strSenhaEncrip)) {
            			
                		if (!String.valueOf(tfNovaSenha.getPassword()).equals("") && String.valueOf(tfNovaSenha.getPassword()) != null) {      
                			if (!String.valueOf(tfNovaSenha.getPassword()).equals(String.valueOf(tfConfSenha.getPassword()))) {
                				JOptionPane.showMessageDialog(null, "Confirmação da NOVA SENHA não confere.", "Acesso ao Sistema", JOptionPane.ERROR_MESSAGE);
                				continua = false;
                			}
                			else {
                				if (user.alteraSenha(tfUser.getText(), strSenhaEncrip, gh.getHashFromString(String.valueOf(tfNovaSenha.getPassword())))) {
                					GravaLog log = new GravaLog("INFO");
                					log.grava("fidelis.view.Login()", tfUser.getText(), "ALTERAÇÃO de senha realizada com sucesso.");
                					JOptionPane.showMessageDialog(null, "Senha ALTERADA com sucesso", "Acesso ao Sistema", JOptionPane.INFORMATION_MESSAGE);
                					continua = true;
                				}
                				else {
                					JOptionPane.showMessageDialog(null, "Alteração da senha não pode ser confirmada.", "Acesso ao Sistema", JOptionPane.ERROR_MESSAGE);
                					continua = false;
                				}
                			}
                		}
            			
                		if (continua) {
		            		usuarioLogado = tfUser.getText();            		
			            	setVisible(false);
			            	GravaLog log = new GravaLog("INFO");
			            	log.grava("fidelis.view.Login()", usuarioLogado, "Acesso ao sistema com sucesso.");	            	
			            	
			            	if (user.getNomePerfil(usuarioLogado).equals("ADMINISTRADOR"))
			            		SessaoConfig.isAdmin = true;
			            	else
			            		SessaoConfig.isAdmin = false;
			            	
			        		JFrame f = new FidelisMain(usuarioLogado, SessaoConfig.isAdmin);
			        		f.setVisible(true);
                		}
            		}
            		else {
            			GravaLog log = new GravaLog("INFO");
		            	log.grava("fidelis.view.Login()", tfUser.getText(), "TENTATIVA de acesso ao sistema. Usuário/Senha inválidos!!");
            			JOptionPane.showMessageDialog(null, "Usuário e/ou senha inválidos!", "Acesso ao Sistema", JOptionPane.ERROR_MESSAGE);
            		}
            	}
        	}
        });
		
		btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(1);
            }
        });
		
		btAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAlterar.setEnabled(false);
            	lbNovaSenha.setVisible(true);
            	tfNovaSenha.setVisible(true);
            	lbConfSenha.setVisible(true);
            	tfConfSenha.setVisible(true);                
            }
        });
	}
	
	public static void main(String [] args) {		
		
		Direitos frmDir = new Direitos();
		Dimension tela1 = Toolkit.getDefaultToolkit().getScreenSize();   
        frmDir.setLocation((tela1.width - frmDir.getSize().width) / 2, (tela1.height - frmDir.getSize().height) / 2);   

		frmDir.setVisible(true);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frmDir.setVisible(false);
		
		Ferramentas fr = new Ferramentas();
		SessaoConfig.vara = fr.getPropriedades("vara");
		
		if (SessaoConfig.vara == null || SessaoConfig.vara.equals("")) {
			JOptionPane.showMessageDialog(null, "Tag VARA não pôde ser lida do arquivo fidelis.propriedades.xml.\nA chave não pode ser validada sem esta informação.", "Atenção", JOptionPane.ERROR_MESSAGE);
			System.exit(-10);
		}		
		
		SimpleDateFormat dfHora = new SimpleDateFormat("yyyyMMdd");
		String sHoje = dfHora.format(new Date());		
		ValidaChave vc = new ValidaChave(sHoje);
		
		if (!vc.isChaveValida()) {
			JOptionPane.showMessageDialog(null, "A chave de acesso ao sistema não é válida ou está vencida.\nPor favor entre em contato com o TRT 9a. Região para continuar usufruindo das funcionalidades do sistema.", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}			
		
		boolean loginIntegradoAdm = false;
		if (args.length > 0 && args[0].equals("-a"))
		{
			loginIntegradoAdm = true;
		}
		
		Configuracoes conf = new Configuracoes();
		
		if (conf.getConfigParm("tipo_login").equals("1") && !loginIntegradoAdm) {
			CheckAdmin ca = new CheckAdmin();			
			SessaoConfig.isAdmin = ca.getAdmin();			
			Operador op = new Operador();
        	GravaLog log = new GravaLog("INFO");
        	log.grava("fidelis.view.Login()", op.getOperador(), "Acesso ao sistema com sucesso.");			
			JFrame f = new FidelisMain(op.getOperador(), SessaoConfig.isAdmin);
			f.setVisible(true);
		} else {
			Login log = new Login();		
			Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();   
	        log.setLocation((tela.width - log.getSize().width) / 2, (tela.height - log.getSize().height) / 2);   
	        log.setVisible(true);			
		}
	}	
}