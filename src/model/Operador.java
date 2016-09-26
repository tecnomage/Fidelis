package model;

public class Operador {

   private String nome;

   public Operador() { 
      nome = System.getProperty("user.name").toUpperCase();
	   
	   //com.sun.security.auth.module.NTSystem NTSystem = new com.sun.security.auth.module.NTSystem();   
	   //String [] group = NTSystem.getGroupIDs();
	   //nome = NTSystem.getUserSID();
	   //for (int i = 0; i < group.length; i++) {
	//	   System.out.println(group[i]);
	  // }
	   
	   //System.out.println("impersonate:" + NTSystem.getImpersonationToken());
	  
   }

   public String getOperador() {
      return nome;
   }

}

