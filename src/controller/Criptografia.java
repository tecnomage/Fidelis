package controller;

public class Criptografia {

	private String chave = "\\1X2#&1Z2_17M28@39A6";

	public String criptografar(String senha) {
		int j = 0;
		byte [] byteF = new byte [22];
		String strB1, strB2;
		
		if (senha == null || senha.length() < 2)
			return "";

		if (senha.length() > 20)
			return "";

		if (senha.length() < 10) {
			strB1 = "0";
			strB2 = Integer.toString(senha.length());
		}
		else {
			strB1 = Integer.toString(senha.length()).substring(0,1);
			strB2 = Integer.toString(senha.length()).substring(1);
		}

		byteF[0]  = (byte) ((int) strB2.charAt(0));
		byteF[21] = (byte) ((int) strB1.charAt(0) + 1);
			
		for (int i=1; i<20; i++) {
			if (j >= senha.length())
				j = 0;

			int byte1 = (int) senha.charAt(j);
			int byte2 = (int) chave.charAt(i);
			j++;
			

			byteF[i] = (byte) (byte1 - byte2);		
		}

		String novaSenha = new String(byteF);
		return (novaSenha);
	}

	public String decriptografar(String senhaCriptografada) {
		int j = 1;
		byte [] byteF = new byte [22];
		
		if (senhaCriptografada.length() < 22)
			return null;

		String strTamanho = senhaCriptografada.substring(21) + senhaCriptografada.substring(0, 1);
		
		if (new String("0123456789").lastIndexOf(senhaCriptografada.substring(21)) < 0)
			return null;
		
		if (new String("0123456789").lastIndexOf(senhaCriptografada.substring(0, 1)) < 0)
			return null;

		for (int i=0; i<Integer.valueOf(strTamanho).intValue() - 10; i++) {
			if (j >= senhaCriptografada.length())
				j = 1;

			int byte1 = (int) senhaCriptografada.charAt(j);
			int byte2 = (int) chave.charAt(i+1);
			j++;
			
			byteF[i] = (byte) (byte1 + byte2);
		}

		String novaSenha = new String(byteF);
		return (novaSenha.substring(0, Integer.valueOf(strTamanho).intValue() - 10));
	}

}
