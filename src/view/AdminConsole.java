package view;

import java.util.Vector;

public class AdminConsole {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Vector plist = PlugInManager.getPlugInList(null, null, PlugInManager.CODEC);
		System.out.println(plist.toString());

	}

}
