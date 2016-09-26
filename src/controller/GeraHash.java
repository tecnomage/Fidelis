package controller;

import java.security.*;
import java.io.*;

public class GeraHash {

	public String getHashFromString(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(s.getBytes());
			return stringHexa(md.digest());
		} catch (NoSuchAlgorithmException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.GeraHash.getHashFromString()", e);
			e.printStackTrace();
			// JOptionPane.showMessageDialog(null,
			// "fidelis.controller.GeraHash.getHashFromString() gerou uma
			// exception!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	public String getHashFromFile(String filename) {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			File f = new File(filename);
			FileInputStream fs = new FileInputStream(f);

			byte[] b = new byte[4096];

			while (fs.read(b) > -1) {
				md.update(b);
			}
			fs.close();

			return stringHexa(md.digest());
		} catch (NoSuchAlgorithmException e) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.GeraHash.getHashFromFile()", e);
			e.printStackTrace();
			// JOptionPane.showMessageDialog(null,
			// "fidelis.controller.GeraHash.getHashFromFile() gerou uma
			// exception!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (IOException ioe) {
			GravaLog log = new GravaLog("ERRO");
			log.gravaExcep("fidelis.controller.GeraHash.getHashFromFile()", ioe);
			ioe.printStackTrace();
			// JOptionPane.showMessageDialog(null,
			// "fidelis.controller.GeraHash.getHashFromFile() gerou uma
			// exception!", "ATENÇÃO", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	private String stringHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0)
				s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}
}
