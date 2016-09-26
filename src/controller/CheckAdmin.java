package controller;

public class CheckAdmin {
	
	public boolean isAdmin = false;
	public native int IsAdmin();

	public CheckAdmin()	{
		System.loadLibrary("lib/CapInfra");
		if (IsAdmin() == 1) 
			isAdmin = true;
		else
			isAdmin = false;		
	}

	public boolean getAdmin() {
		return isAdmin;
	}

}