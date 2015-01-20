package es.psig.homesig;

import es.psig.homesig.controller.Controller;
import es.psig.homesig.dao.Model;
import es.psig.homesig.gui.View;
import es.psig.homesig.util.Utils;


public class MainClass {

	
	public static void main(String[] args) {
	
		// Create view
		View window = new View();
		
		// Get version number
		String versionCode = MainClass.class.getPackage().getImplementationVersion();
		String msg = "Application started";
		if (versionCode != null) {
			msg+= "\nVersion: " + versionCode;
			window.setVersion("Version: " + versionCode);
		}
		Utils.getLogger().info(msg);	
		
		// Create model and controller. Make window visible
		Model model = new Model();
		new Controller(window, model);
		window.frame.setVisible(true);
		
	}

	
}