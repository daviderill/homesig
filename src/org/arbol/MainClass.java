package org.arbol;

import org.arbol.controller.Controller;
import org.arbol.dao.Model;
import org.arbol.gui.View;


public class MainClass {

	
	public static void main(String[] args) {
	
		View window = new View();
		Model model = new Model();
		new Controller(window, model);
		window.frame.setVisible(true);
		
	}

	
}