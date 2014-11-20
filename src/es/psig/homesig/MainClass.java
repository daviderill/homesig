package es.psig.homesig;

import es.psig.homesig.controller.Controller;
import es.psig.homesig.dao.Model;
import es.psig.homesig.gui.View;


public class MainClass {

	
	public static void main(String[] args) {
	
		View window = new View();
		Model model = new Model();
		new Controller(window, model);
		window.frame.setVisible(true);
		
	}

	
}