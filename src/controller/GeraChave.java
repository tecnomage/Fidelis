package controller;

// ATENCAO: O CODIGO DO GERACHAVE JAH ESTAH ALTERADO PARA GERAR CHAVE GENERICA.
// POREM O VALIDACHAVE AINDA NAO FOI ALTERADO.

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GeraChave {
	
	public static void main(String [] args) {
		
		if (args.length < 2) {
			System.out.println("Gerador de chaves do Videocap");
			System.out.println("Sintaxe Exemplo: GeraChave 20090430 0109123 [00000 | 111111]\n\nOnde 20090430 eh a data de validade da chave.\nE 0109123 eh chave da estacao no formato (JJRRVVV)\n11111 indica chave geral.");
			System.exit(1);			
		}
		
		Criptografia crp = new Criptografia();		
		Random rd = new Random();
		byte [] byte_buffer = new byte [1024];
		for (int i=0; i < 1024; i++) {
			byte_buffer[i] = (byte) rd.nextInt(255);
		}
		String strBuffer = new String(byte_buffer);
		
		String strAux = strBuffer.substring(0, 812);
		strAux += crp.criptografar(args[0]);
		strAux += crp.criptografar(args[1]);
		strAux += crp.criptografar(args[2]);
		strAux += strBuffer.substring(878);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("fidelis.key"));
			bw.write(strAux);
			bw.close();
			System.out.println("Chave gerada com SUCESSO!!");
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
}
