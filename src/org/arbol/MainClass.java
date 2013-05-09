package org.arbol;

public class MainClass {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		View window = new View();
		Model model = new Model();
		Controller controller = new Controller(window,model);
		window.frame.setVisible(true);
	}

}