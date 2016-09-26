package controller;

public class Sessao {
	
	public static String numProcesso = null;
	public static String tipo = null;
	public static String juiz = null;
	public static String data = null;
	public static String hora = null;
	public static String autor = null;
	
	public static String nomeDispositivoVideo = null;
	public static String nomeDispositivoAudio = null;
	
	public static boolean gravando = false;
	
	public static String fileName(String strNumProcesso, String strData, String strHora) {
		return (strNumProcesso.trim() + " " + strData.substring(0,2) + strData.substring(3,5) + strData.substring(6) + " " + strHora.substring(0,2) + strHora.substring(3,5) + strHora.substring(6));
	}
	
}
