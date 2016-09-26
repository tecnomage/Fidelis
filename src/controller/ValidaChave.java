package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ValidaChave {
	
	private String chaveDecriptografada = null;
	private String vara;
	private String hoje;
	private String chaveGenerica;
	
	public ValidaChave(String hj) {
		hoje = hj;
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/fidelis.key"));
			char [] bChar = new char[22];
			char [] bCharT = new char[812];
			br.read(bCharT, 0, 812);
			if (br.read(bChar, 0, 22) == 22) {
				Criptografia cr = new Criptografia();
				String strChave = new String(bChar);
				chaveDecriptografada = cr.decriptografar(strChave);
			}
			
			if (br.read(bChar, 0, 22) == 22) {
				Criptografia cr = new Criptografia();
				String strChave = new String(bChar);
				vara = cr.decriptografar(strChave);
			}

			if (br.read(bChar, 0, 22) == 22) {
				Criptografia cr = new Criptografia();
				String strChave = new String(bChar);
				chaveGenerica = cr.decriptografar(strChave);
			}			
			
			br.close();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		finally {
			
		}
	}
	
	public boolean isChaveValida() {
		if (chaveDecriptografada == null)
			return false;
		
		for(int i=0; i<chaveDecriptografada.length(); i++)
			if (new String("0123456789").lastIndexOf(chaveDecriptografada.charAt(i)) < 0) 
				return false;
		
		if (chaveGenerica == null)
			return false;
		
		if (!chaveGenerica.equals("11111"))
			if (!vara.equals(SessaoConfig.vara))
				return false;
	
		if (Integer.valueOf(chaveDecriptografada) < Integer.valueOf(hoje))
			return false;
		else
			return true;
	}
}
