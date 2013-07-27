package org.arbol;

import java.util.Properties;

import org.arbol.controller.Controller;
import org.arbol.dao.Model;
import org.arbol.gui.View;
import org.arbol.util.DatabaseScript;


public class MainClass {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		/*DatabaseScript dbs = new DatabaseScript();
		dbs.processLineByLine();*/
		
		View window = new View();
		Model model = new Model();
		new Controller(window, model);
		window.frame.setVisible(true);
		
		
	}

}