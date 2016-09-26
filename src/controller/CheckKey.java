package controller;

public class CheckKey {
	private int dataValidade;
	private native int decripto(String data);

	public CheckKey(String sHoje)	{
		System.loadLibrary("lib/check");
		dataValidade = decripto(sHoje);		
	}

	public int getDataValidade() {
		return (dataValidade);
	}
}
