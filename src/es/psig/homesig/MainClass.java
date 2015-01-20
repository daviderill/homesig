package es.psig.homesig;

import es.psig.homesig.controller.Controller;
import es.psig.homesig.dao.Model;
import es.psig.homesig.gui.View;


public class MainClass {

	
	public static void main(String[] args) {
	
		// Create view
		View window = new View();
		
		// Create model and controller. 
		Model model = new Model();
		new Controller(window, model);
		
		// Get version number
		String versionCode = MainClass.class.getPackage().getImplementationVersion();
		String msg = "Application started";
		if (versionCode != null) {
			msg+= "\nVersion: " + versionCode;
			window.setVersion("Version: " + versionCode);
		}
		model.logInfo(msg);			
		
		// Make window visible
		window.frame.setVisible(true);
		
	}

	
}