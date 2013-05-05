package org.arbol;

import java.awt.EventQueue;
import java.sql.Connection;

public class MainClass {

	private static DataBaseController dataBaseController;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
					Connection conn = DataBaseConnection.connect("arbol2.sqlite");
					dataBaseController = new DataBaseController(conn);
					dataBaseController.createTree();
					dataBaseController.drawTree();
					window.drawChildren(dataBaseController.getFirstLevel());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
