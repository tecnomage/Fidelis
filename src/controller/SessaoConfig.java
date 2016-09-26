package controller;

import java.sql.Connection;

import javax.swing.JFrame;

public class SessaoConfig {	
	public static String TipoLog = null;
	public static boolean isTransmissaoServerUp = false;
	public static String usuarioLogado = null;
	public static boolean isAdmin = false;
	public static JFrame mainFrame = null;	
	public static Connection conexao = null;
	public static Connection conexaoRemota = null;
	public static String vara = null;
	public static String videoPath = null;
}
