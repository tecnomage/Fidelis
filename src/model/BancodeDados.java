package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import controller.Criptografia;
import controller.GravaLog;
import controller.SessaoConfig;

public class BancodeDados {	
	private Connection con = null;
	private Connection conRemota = null;
	
	/*
	public BancodeDados(String sgbd) {
	    try {
	        // Load the JDBC driver
	        String driverName = "oracle.jdbc.driver.OracleDriver";
	        Class.forName(driverName);
	    
	        // Create a connection to the database
	        String serverName = "127.0.0.1";
	        String portNumber = "1521";
	        String sid = "XE";
	        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
	        String username = "system";
	        String password = "sql";
	        con = DriverManager.getConnection(url, username, password);	         
	    }
	    catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
	}
	*/
	
	public BancodeDados() {		
		if (SessaoConfig.conexao != null)
			con = SessaoConfig.conexao;
		else {
			String strSGBD [] = getSGBDData();
			
			if (strSGBD[0].equals("Microsoft Access")) {		
		        try {
		            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
		            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
		            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
		            database += filename.trim() + ";DriverID=22;READONLY=true}";
		            con = DriverManager.getConnection(database , strSGBD[5], strSGBD[6]); 
		            SessaoConfig.conexao = con;
		        }
		        catch (Exception e) {
		            e.printStackTrace();
		            JOptionPane.showMessageDialog(null, "N�o foi poss�vel conectar com o Banco de Dados (" + strSGBD[0] + ").\nMensagem: " + e.getMessage(), "ATEN��O", JOptionPane.ERROR_MESSAGE);
		        }
			}
			
			if (strSGBD[0].equals("Oracle Express")) {
			    try {
			        String driverName = "oracle.jdbc.driver.OracleDriver";
			        Class.forName(driverName);
			        
			        String serverName = strSGBD[1]; //"127.0.0.1";
			        String portNumber = strSGBD[2]; //"1521";
			        String sid = strSGBD[3]; //"XE";
			        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
			        String username = strSGBD[5]; // "system";
			        String password = strSGBD[6]; //"sql";
			        //System.out.println(username + "|" + password);
			        con = DriverManager.getConnection(url, username, password);	
			        SessaoConfig.conexao = con;
			    } catch (SQLException e) {
			        e.printStackTrace();
			        JOptionPane.showMessageDialog(null, "N�o foi poss�vel conectar com o Banco de Dados (" + strSGBD[0] + ").\nMensagem: " + e.getMessage(), "ATEN��O", JOptionPane.ERROR_MESSAGE);
			    } catch (ClassNotFoundException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "N�o foi poss�vel conectar com o Banco de Dados (" + strSGBD[0] + ").\nMensagem: " + e.getMessage(), "ATEN��O", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	public BancodeDados(String servidor, String porta, String psid, String user, String senha) {
		if (SessaoConfig.conexaoRemota != null)
			conRemota = SessaoConfig.conexaoRemota;
		else {
		    try {
		        String driverName = "oracle.jdbc.driver.OracleDriver";
		        Class.forName(driverName);
		        
		        String serverName = servidor; //"127.0.0.1";
		        String portNumber = porta; //"1521";
		        String sid = psid; //"XE";
		        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
		        String username = user; // "system";
		        String password = senha; //"sql";
		        conRemota = DriverManager.getConnection(url, username, password);	
		        SessaoConfig.conexaoRemota = conRemota;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "N�o foi poss�vel conectar com o Banco de Dados Remoto.\nMensagem: " + e.getMessage(), "ATEN��O", JOptionPane.ERROR_MESSAGE);
		    } catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "N�o foi poss�vel conectar com o Banco de Dados Remoto.\nMensagem: " + e.getMessage(), "ATEN��O", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public BancodeDados(String flag, String servidor, String porta, String psid, String user, String senha) {
		// Parametro flag diferencia esta construtura da construtura de conexao remota.
		// Esta tb eh uma conexao remota porem utilizara o con ao inves do conRemoto no player.
		if (SessaoConfig.conexao != null)
			con = SessaoConfig.conexao;
		else {
		    try {
		        String driverName = "oracle.jdbc.driver.OracleDriver";
		        Class.forName(driverName);
		        
		        String serverName = servidor; //"127.0.0.1";
		        String portNumber = porta; //"1521";
		        String sid = psid; //"XE";
		        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
		        String username = user; // "system";
		        String password = senha; //"sql";
		        con = DriverManager.getConnection(url, username, password);	
		        SessaoConfig.conexao = con;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "N�o foi poss�vel conectar com o Banco de Dados Remoto.\nMensagem: " + e.getMessage(), "ATEN��O", JOptionPane.ERROR_MESSAGE);
		    } catch (ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "N�o foi poss�vel conectar com o Banco de Dados Remoto.\nMensagem: " + e.getMessage(), "ATEN��O", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public String [] getSGBDData() {
		String [] strRet = new String[7];
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/sgbd.ini"));
			
			strRet[0] = br.readLine(); // nome
			strRet[1] = br.readLine(); // servidor
			strRet[2] = br.readLine(); // porta
			strRet[3] = br.readLine(); // sid
			strRet[4] = br.readLine(); // database
			strRet[5] = br.readLine(); // usuario
			char [] charBuffer = new char[22];
			if (br.read(charBuffer) > 21) {			
				Criptografia crp = new Criptografia();
				strRet[6] = crp.decriptografar(new String(charBuffer, 0, 22)); // senha
			}
			else 
				strRet[6] = "";
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// Banco de Dados default, caso nao possa ler o arquivo SGBD.XML
			String [] ret = new String[7];
			ret[0] = "Microsoft Access";
			ret[1] = "";
			ret[2] = "";
			ret[3] = "";
			ret[4] = "videocap";
			ret[5] = "";
			ret[6] = "";
			return ret; 
		} catch (IOException e) {
			e.printStackTrace();
			// Banco de Dados default, caso nao possa ler o arquivo SGBD.XML
			String [] ret = new String[7];
			ret[0] = "Microsoft Access";
			ret[1] = "";
			ret[2] = "";
			ret[3] = "";
			ret[4] = "videocap";
			ret[5] = "";
			ret[6] = "";
			return ret; 
		}
		return strRet;
	}
	
/*	
	public String [] getSGBDDataxml() {
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();		
			Document doc = builder.parse(new File(System.getProperty("user.dir") + "/sgbd.xml"));
	
			NodeList nodes = doc.getElementsByTagName("sgbd");
			String retorno[] = new String[7];
			
			for (int i = 0; i < nodes.getLength(); i++) {				
				Element elemento = (Element) nodes.item(i);
				
				NodeList node = elemento.getElementsByTagName("nome");
				Element linha = (Element) node.item(0);
				retorno[0] = linha.getTextContent();
				
				node = elemento.getElementsByTagName("servidor");
				linha = (Element) node.item(0);
				retorno[1] = linha.getTextContent();
				
				node = elemento.getElementsByTagName("porta");
				linha = (Element) node.item(0);
				retorno[2] = linha.getTextContent();
				
				node = elemento.getElementsByTagName("sid");
				linha = (Element) node.item(0);
				retorno[3] = linha.getTextContent();
	
				node = elemento.getElementsByTagName("database");
				linha = (Element) node.item(0);
				retorno[4] = linha.getTextContent();
	
				node = elemento.getElementsByTagName("usuario");
				linha = (Element) node.item(0);
				retorno[5] = linha.getTextContent();
	
				node = elemento.getElementsByTagName("senha");
				linha = (Element) node.item(0);
				retorno[6] = linha.getTextContent();
				
				return retorno;
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Banco de Dados default, caso nao possa ler o arquivo SGBD.XML
		String [] ret = new String[7];
		ret[0] = "Microsoft Access";
		ret[1] = "";
		ret[2] = "";
		ret[3] = "";
		ret[4] = "videocap";
		ret[5] = "";
		ret[6] = "";
		return ret; 

	}
*/	

	public void exportaHelp() {
		String sql = "select chave, linha1, linha2, linha3, linha4, linha5 from vc_ajuda";
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			String ret = null;
			while(rs.next()) {
				ret = "INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values (" +
				      "'" + rs.getString("chave") + "'," +
				      "'" + rs.getString("linha1") + "'," +
				      "'" + rs.getString("linha2") + "'," +
				      "'" + rs.getString("linha3") + "'," +
				      "'" + rs.getString("linha4") + "'," +
				      "'" + rs.getString("linha5") + "')" ;
				System.out.println(ret);
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {}
	}

	public Connection getConexao() {
		return con;
	}
	
//	public void fechaConexao() {
//		try {
//			con.close();
//		} 
//		catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}	
	
	/**
	 * Cria as tabelas do sistema.
	 * 
	 */	
	public boolean criaTabelas() {		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Nenhuma conexao com o banco de dados foi estabelecida.", "Banco de Dados", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {
			try {
				GravaLog log = new GravaLog();
				String sql_vc_tipo_audiencias = "CREATE TABLE vc_tipo_audiencias (codigo int not null, descricao varchar(100) not null)";
				
				String sql_vc_motivo_finalizacoes = "CREATE TABLE vc_motivo_finalizacoes (codigo int not null, descricao varchar(100) not null)";
				
				String sql_vc_audiencias = "CREATE TABLE vc_audiencias (numprocesso char(25) not null, data char(10) not null, hora char(8) not null, nomejuiz varchar(70) null, cod_tipo_audiencia int null, cod_motivo_finalizacao int null, mediahash char(40) null)";
				
				String sql_vc_temas = "CREATE TABLE vc_temas (numprocesso char(25) not null, descricao varchar(100) not null, temapai varchar(100) null)";
				
				String sql_vc_qualif_dep = "CREATE TABLE vc_qualif_dep (numprocesso char(25) not null, descricao varchar(100) not null, qualificacao varchar(100) null)";
				
				String sql_vc_marcacoes = "CREATE TABLE vc_marcacoes (numprocesso char(25) not null, data char(10) not null, hora char(8) not null, tempo_marcacao char(8) not null, tema varchar(100) null, subtema varchar(100) null, qualificacao varchar(100) null, depoente varchar(100) null)";
				
				String sql_vc_perfil = "CREATE TABLE vc_perfil (codigo int not null, perfil varchar(50) not null)";
				
				String sql_vc_usuarios = "CREATE TABLE vc_usuarios (nome varchar(30) not null, senha varchar(40) not null, cod_perfil int not null)";
				
				String sql_vc_config = "CREATE TABLE vc_config (tipo_log char(1) not null, tipo_login char(1) not null, cabecalho varchar(250), rodape varchar(250), fundo varchar(250), diretorio_servidor varchar(250), endereco_servidor varchar(100), porta varchar(6), expurgo varchar(3), horario_inicial varchar(5), horario_final varchar(5))";
				
				String sql_vc_ajuda = "CREATE TABLE vc_ajuda (chave varchar(100) not null, linha1 varchar(255), linha2 varchar(255), linha3 varchar(255), linha4 varchar(255), linha5 varchar(255))";

				
				Statement st = con.createStatement();					
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_tipo_audiencias");
				st.execute(sql_vc_tipo_audiencias);
				System.out.println("Tabela VC_TIPO_AUDIENCIAS criada ..");				

				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_motivo_finalizacoes");
				st.execute(sql_vc_motivo_finalizacoes);
				System.out.println("Tabela VC_MOTIVO_FINALIZACOES criada ..");
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_audiencias");
				st.execute(sql_vc_audiencias);
				System.out.println("Tabela VC_AUDIENCIAS criada ..");
	
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_temas");
				st.execute(sql_vc_temas);
				System.out.println("Tabela VC_TEMAS criada ..");

				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_qualif_dep");
				st.execute(sql_vc_qualif_dep);
				System.out.println("Tabela VC_QUALIF_DEP criada ..");

				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_marcacoes");
				st.execute(sql_vc_marcacoes);
				System.out.println("Tabela VC_MARCACOES criada ..");
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_perfil");
				st.execute(sql_vc_perfil);
				System.out.println("Tabela VC_PERFIL criada ..");
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_usuarios");
				st.execute(sql_vc_usuarios);
				System.out.println("Tabela VC_USUARIOS criada ..");
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_config");
				st.execute(sql_vc_config);
				System.out.println("Tabela VC_CONFIG criada ..");
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Criando tabela vc_ajuda");
				st.execute(sql_vc_ajuda);
				System.out.println("Tabela VC_AJUDA criada ..");
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Inserindo na tabela vc_config");
				st.execute("INSERT INTO vc_config VALUES ('2', '1', '', '','', '', '', '', '80', '22:00', '23:59')");
				System.out.println("dados inseridos em VC_CONFIG");
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Inserindo na tabela vc_perfil");
				st.execute("INSERT INTO vc_perfil VALUES (1, 'ADMINISTRADOR')");			
				System.out.println("dados inseridos em VC_PERFIL");
				
				log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Inserindo na tabela vc_usuarios");
				st.execute("INSERT INTO vc_usuarios VALUES ('ADMIN', 'd033e22ae348aeb5660fc2140aec35850c4da997', 1)");			
				System.out.println("dados inseridos em VC_USUARIOS");
				
				//log.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, "Inserindo na tabela vc_ajuda");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Detec��o de Dispositivos de �udio e V�deo','               GRAVA��O DE AUDI�NCIAS=====================================Selecione o dispositivo de V�DEO e o dispositivo de �UDIO para a correta grava��o e degrava��o da audi�ncia.Clique em \"PR�XIMO>\" para iniciar a grava��o.Para sair desta op��o',', clique em \"FECHAR\". Esta op��o sai da grava��o de audi�ncias sem registrar informa��es da mesma.','','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Grava��o da Audi�ncia','               GRAVA��O DE AUDI�NCIAS=====================================Informe o n�mero do processo, tipo de audi�ncia e nome do juiz.Ap�s a inclus�o dos dados, clique em \"VALIDAR\".Nos quadros TEMAS/SUB-TEMAS e QUALIFICA��O-DEPOENTES, informe os',' assuntos e as pessoas que participar�o da audi�ncia. Para inclus�o das informa��es, utilize o bot�o direito do mouse.Ap�s a inclus�o das informa��es, clique em \"GRAVAR\" para in�cio da grava��o.Para incluir marca��es na audi�ncia, selecione um TEMA/S','EMA e uma QUALIFICA��O/DEPOENTE. Depois clique em \"MARCAR V�DEO\". Confirme os dados da marca��o e clique em \"OK\".Para interromper a grava��o, informe o MOTIVO DA INTERRUP��O e clique em \"INTERROMPER GRAVA��O\" para posterior concluo da mesma.Para concl','uir a audi�ncia, clique em \"CONCLUIR\".','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Sele��o da Audi�ncia','             VISUALIZA��O DE AUDI�NCIAS=====================================Selecione um dos processos listados e clique em \"PR�XIMO>\" para a visualiza��o da audi�ncia.','','','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Visualiza��o da Audi�ncia','             VISUALIZA��O DE AUDI�NCIAS=====================================Para visualizar uma audi�ncia, clique em \"INICIAR\" (bot�o PLAY) para que a mesma seja reproduzida desde o in�cio.Para ir direto para uma das marca��es, selecione a mesma da l','ista de marca��es e clique em \"IR PARA MARCA\".Pode-se igualmente utilizar a barra logo abaixo do v�deo para controlar o ponto de reprodu��o da audi�ncia.Para finalizar uma visualiza��o, clique em \"PARAR\" (bot�o STOP).Em caso de solicita��o, para exp','r uma audi�ncia, clique em \"EXPORTAR\".Para a transcri��o do �udio do procso (transforma��o do �udio em texto), clique em \"TRANSCREVER\"','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Detec��o de Dispositivos de �udio e V�deo','               GRAVA��O DE AUDI�NCIAS=====================================Selecione o dispositivo de V�DEO e o dispositivo de �UDIO para a correta grava��o e degrava��o da audi�ncia.Clique em \"PR�XIMO>\" para iniciar a grava��o.Para sair desta op��o',', clique em \"FECHAR\". Esta op��o sai da grava��o de audi�ncias sem registrar informa��es da mesma.','','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Cadastro de Usu�rios','                CADASTRO DE USU�RIOS=====================================Para a cria��o de usu�rios, clique com o bot�o direito do mouse.Para a inclus�o de um novo usu�rio, selecione INCLUIR.Para a exclus�o de um usu�rio, selecione EXCLUIR.Para a',' atualiza��o dos dados de um usu�rio, selecione a coluna/dado a ser atualizado (duplo-clique sobre a coluna/dado), informe o novo valor e selecione ATUALIZAR com o bot�o direito do mouse.','','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Cadastro de Tipos de Audi�ncias','        CADASTRO DE TIPOS DE AUDI�NCIA=====================================Para a atualiza��o dos tipos de audi�ncia, clique com o bot�o direito do mouse sobre o primeiro n�vel (tabela: TIPOS DE AUDI�NCIAS).Para inclus�o de um novo tipo, selecione IN','CLUIR.Para exclus�o de um tipo j� cadastrado, clique com o bot�o direito sobre o tipo e selecione EXCLUIR.','','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Cadastro de Motivos de Finaliza��es de Audi�ncias','  CADASTRO DE MOTIVOS DE FINALIZA��ES=====================================Para a atualiza��o dos motivos de finaliza��o da audi�ncia, clique com o bot�o direito do mouse sobre o primeiro n�vel (tabela: MOTIVO DE FINALIZA��ES).Para inclus�o de um novo',' motivo, selecione INCLUIR.Para exclus�o de um motivo j� cadastrado, clique com o bot�o direito sobre o motivo e selecione EXCLUIR.','','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Cadastro de Perfil de Usu�rios','        CADASTRO DE PERFIS DE USU�RIOS=====================================Para a atualiza��o dos perfis de usu�rio, clique com o bot�o direito do mouse sobre o primeiro n�vel (tabela: PERFIL DE USU�RIOS).Para inclus�o de um novo perfil, selecione IN','CLUIR.Para exclus�o de um perfil j� cadastrado, clique com o bot�o direito sobre o perfil e selecione EXCLUIR.IMPORTANTE:===========A a��o est� preparada para suportar 2 tipos de usu�rio: ADMINISTRADOR e OPERADOR.O perfil ADMINISTRADOR tem a','sso a todas as op��es/menus da aplica��o.O perfil OPERADOR tem acesso ao menu \"era��o\" no menu principal.','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Configura��es Gerais','                CONFIGURA��ES GERAIS=====================================Configure aqui os seguintes itens:1) Login integrado (busca usu�rio/perfil do Windows/Linux) ou uso do cadastro interno de usu�rios/perfis2) Formato do arquivo de log: em XML,',' TXT ou ambos3) Percentual para in�cio do expurgo de audi�ncias em caso de disco cheio4) Padr�o de cabe�alho, fundo e rodap� para a confec��o de relat�rios5) Diret�rio para receber as audi�ncias transmitidas, caso a aplica��o tamb�m fa�a esta recep�','6)ndere�o e Porta IP aonde o m�dulo de recep��o de audi�ncias est� sendo executado para a recep��o do movimento7) Hor�rio para in�cio e fim das transmiss�es do movimento de audi�nciasPara cada configura��o acima, clique no respectivo \"SALVAR\" para e','fetiv�-la.','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Sele��o de Audi�ncia Pendente','               GRAVA��O DE AUDI�NCIAS=====================================Selecione o processo pendente para continua��o da grava��o da audi�ncia. Para a grava��o da mesma, clique em \"PR�XMO>\".Para finalizar sem continuar com a grava��o de uma audi','�ncia pendente, clique em \"FECHAR\".','','','')");
				//st.execute("INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values ('Grava��o da Audi�ncia','               GRAVA��O DE AUDI�NCIAS=====================================Informe o n�mero do processo, tipo de audi�ncia e nome do juiz.Ap�s a inclus�o dos dados, clique em \"VALIDAR\".Nos quadros TEMAS/SUB-TEMAS e QUALIFICA��O-DEPOENTES, informe os',' assuntos e as pessoas que participar�o da audi�ncia. Para inclus�o das informa��es, utilize o bot�o direito do mouse.Ap�s a inclus�o das informa��es, clique em \"GRAVAR\" para in�cio da grava��o.Para incluir marca��es na audi�ncia, selecione um TEMA/S','EMA e uma QUALIFICA��O/DEPOENTE. Depois clique em \"MARCAR V�DEO\". Confirme os dados da marca��o e clique em \"OK\".Para interromper a grava��o, informe o MOTIVO DA INTERRUP��O e clique em \"INTERROMPER GRAVA��O\" para posterior concluo da mesma.Para concl','uir a audi�ncia, clique em \"CONCLUIR\".','')");
				//System.out.println("dados inseridos em VC_AJUDA");
				
				st.close();
				return true;
			}
			catch (SQLException e) {
				GravaLog logErro = new GravaLog();
				logErro.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, e.getMessage());
				e.printStackTrace();
				return false;
			}
		}		
	}
	
	
	/**
	 * Remove as tabelas do sistema.
	 * 
	 */	
	public boolean removeTabelas() {		
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Nenhuma conexao com o banco de dados foi estabelecida.", "Banco de Dados", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {
			try {
				GravaLog log = new GravaLog("INFO");
				log.grava("fidelis.model.BancodeDados.removeTabelas()", SessaoConfig.usuarioLogado, "Removendo as tabelas do sistema.");
				
				Statement st = con.createStatement();				
				
				st.execute("DROP TABLE vc_tipo_audiencias");
				System.out.println("Tabela VC_TIPO_AUDIENCIAS removida ..");

				st.execute("DROP TABLE vc_motivo_finalizacoes");
				System.out.println("Tabela VC_MOTIVO_FINALIZACOES removida ..");

				st.execute("DROP TABLE vc_audiencias");
				System.out.println("Tabela VC_AUDIENCIAS removida ..");
	
				st.execute("DROP TABLE vc_temas");
				System.out.println("Tabela VC_TEMAS removida ..");

				st.execute("DROP TABLE vc_qualif_dep");
				System.out.println("Tabela VC_QUALIF_DEP removida ..");

				st.execute("DROP TABLE vc_marcacoes");
				System.out.println("Tabela VC_MARCACOES removida ..");
				
				st.execute("DROP TABLE vc_perfil");
				System.out.println("Tabela VC_PERFIL removida ..");
				
				st.execute("DROP TABLE vc_usuarios");
				System.out.println("Tabela VC_USUARIOS removida ..");
				
				st.execute("DROP TABLE vc_config");
				System.out.println("Tabela VC_CONFIG removida ..");
				
				st.execute("DROP TABLE vc_ajuda");
				System.out.println("Tabela VC_AJUDA removida ..");
				
				st.close();
				return true;
			}
			catch (SQLException e) {
				GravaLog logErro = new GravaLog();
				logErro.grava("fidelis.model.BancodeDados.criaTabelas()", SessaoConfig.usuarioLogado, e.getMessage());
				e.printStackTrace();
				return false;
			}
		}		
	}
	
	public boolean copiaAjuda() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select chave, linha1, linha2, linha3, linha4, linha5 from vc_ajuda";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			while(rs.next()) {				
				String linha[] = new String[5];
				for (int j=0; j<5; j++) {
					linha[j] = rs.getString("linha" + (j+1));
					if (linha[j] == null || linha[j].equals(""))
						linha[j] = " ";
				}				
				
				for (int i=0; i<5; i++) {
					if (linha[i].length() > 235) {
						String excedente = linha[i].substring(235);
						String strAux = linha[i].substring(0, 235);
						linha[i] = strAux;
						if (i < 4) {
							strAux = linha[i + 1];						
							linha[i + 1] = excedente + strAux;
						}
					}
				}
				
				ret = "INSERT INTO VC_AJUDA (chave, linha1, linha2, linha3, linha4, linha5) values (" +
				      "'" + rs.getString("chave") + "'," +
				      "'" + linha[0] + "'," +
				      "'" + linha[1] + "'," +
				      "'" + linha[2] + "'," +
				      "'" + linha[3] + "'," +
				      "'" + linha[4] + "')" ;

				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}
	
	public boolean copiaVCTipoAudiencias() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select codigo, descricao from vc_tipo_audiencias";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			while(rs.next()) {				
				ret = "INSERT INTO VC_TIPO_AUDIENCIAS (codigo, descricao) values (" +
				      rs.getInt("codigo") + "," +
				      "'" + rs.getString("descricao") + "')" ;

				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}

	public boolean copiaVCMotivoFinalizacoes() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select codigo, descricao from vc_motivo_finalizacoes";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			while(rs.next()) {				
				ret = "INSERT INTO VC_MOTIVO_FINALIZACOES (codigo, descricao) values (" +
				      rs.getInt("codigo") + "," +
				      "'" + rs.getString("descricao") + "')" ;
				
				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}

	
	public boolean copiaVCAudiencias() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select numprocesso, data, hora, nomejuiz, cod_tipo_audiencia, cod_motivo_finalizacao, mediahash from vc_audiencias";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			while(rs.next()) {				
				ret = "INSERT INTO VC_AUDIENCIAS (numprocesso, data, hora, nomejuiz, cod_tipo_audiencia, cod_motivo_finalizacao, mediahash) values (" +
				      "'" + rs.getString("numprocesso") + "'," +
				      "'" + rs.getString("data") + "'," +
				      "'" + rs.getString("hora") + "'," +
				      "'" + rs.getString("nomejuiz") + "'," +
				      rs.getInt("cod_tipo_audiencia") + "," +
				      rs.getInt("cod_motivo_finalizacao") + "," +
				      "'" + rs.getString("mediahash") + "')" ;
				
				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}

	public boolean copiaVCTemas() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select numprocesso, descricao, temapai from vc_temas";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			while(rs.next()) {				
				ret = "INSERT INTO VC_TEMAS (numprocesso, descricao, temapai) values (" +
				      "'" + rs.getString("numprocesso") + "'," +
				      "'" + rs.getString("descricao") + "'," +
				      "'" + rs.getString("temapai") + "')" ;
				
				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}
	
	public boolean copiaVCQualif_Dep() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select numprocesso, descricao, qualificacao from vc_qualif_dep";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			while(rs.next()) {				
				ret = "INSERT INTO VC_QUALIF_DEP (numprocesso, descricao, qualificacao) values (" +
				      "'" + rs.getString("numprocesso") + "'," +
				      "'" + rs.getString("descricao") + "'," +
				      "'" + rs.getString("qualificacao") + "')" ;
				
				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}
	
	public boolean copiaVCMarcacoes() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select numprocesso, data, hora, tempo_marcacao, tema, subtema, qualificacao, depoente from vc_marcacoes";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;			
			while(rs.next()) {				
				ret = "INSERT INTO VC_MARCACOES (numprocesso, data, hora, tempo_marcacao, tema, subtema, qualificacao, depoente) values (" +
				      "'" + rs.getString("numprocesso") + "'," +
				      "'" + rs.getString("data") + "'," +
				      "'" + rs.getString("hora") + "'," +
				      "'" + rs.getString("tempo_marcacao") + "'," +
				      "'" + rs.getString("tema") + "'," +
				      "'" + rs.getString("subtema") + "'," +
				      "'" + rs.getString("qualificacao") + "'," +
				      "'" + rs.getString("depoente") + "')" ;
				
				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}
	
	public boolean copiaVCPerfil() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select codigo, perfil from vc_perfil";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;	
			System.out.println("delete from vc_perfil");
			stOra.execute("delete from vc_perfil");
			while(rs.next()) {				
				ret = "INSERT INTO VC_PERFIL (codigo, perfil) values (" +
				      rs.getInt("codigo") + "," +
				      "'" + rs.getString("perfil") + "')" ;
				
				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}
	
	public boolean copiaVCUsuarios() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select nome, senha, cod_perfil from vc_usuarios";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;	
			System.out.println("delete from vc_usuarios");
			stOra.execute("delete from vc_usuarios");
			while(rs.next()) {				
				ret = "INSERT INTO VC_USUARIOS (nome, senha, cod_perfil) values (" +
				      "'" + rs.getString("nome") + "'," +
				      "'" + rs.getString("senha") + "'," +
				      rs.getString("cod_perfil") + ")" ;
				
				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}
	
	public boolean copiaVCConfig() {
		Connection conAccess;
		
		try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");	            
            String filename = System.getProperty("user.dir") + "/DB/fidelis.mdb"; 
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";DriverID=22;READONLY=true}";
            conAccess = DriverManager.getConnection(database , "", ""); 
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return false;            
        }
		
		String sql = "select tipo_log, tipo_login, cabecalho, rodape, fundo, diretorio_servidor, endereco_servidor, porta, expurgo, horario_inicial, horario_final from vc_config";
		
		try {
			BancodeDados dbOra = new BancodeDados();
			Connection conOra = dbOra.getConexao();
			
			Statement stAccess = conAccess.createStatement();
			Statement stOra = conOra.createStatement();
			ResultSet rs = stAccess.executeQuery(sql);
			String ret = null;
			System.out.println("delete from vc_config");
			stOra.execute("delete from vc_config");
			while(rs.next()) {				
				ret = "INSERT INTO VC_CONFIG (tipo_log, tipo_login, cabecalho, rodape, fundo, diretorio_servidor, endereco_servidor, porta, expurgo, horario_inicial, horario_final) VALUES (" +
				      "'" + rs.getString("tipo_log") + "'," +
				      "'" + rs.getString("tipo_login") + "'," +
				      "'" + rs.getString("cabecalho") + "'," +
				      "'" + rs.getString("rodape") + "'," +
				      "'" + rs.getString("fundo") + "'," +
				      "'" + rs.getString("diretorio_servidor") + "'," +
				      "'" + rs.getString("endereco_servidor") + "'," +
				      "'" + rs.getString("porta") + "'," +
				      "'" + rs.getString("expurgo") + "'," +
				      "'" + rs.getString("horario_inicial") + "'," +
				      "'" + rs.getString("horario_final") + "')" ;
				
				System.out.println(ret);
				stOra.execute(ret);
			}
			stOra.close();
			rs.close();			
			stAccess.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;		
	}


}
