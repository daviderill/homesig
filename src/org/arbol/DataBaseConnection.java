package org.arbol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DataBaseConnection {
 
 /**
  * Connecta a una base de dades amb la ruta passada com a argument
  * Si el arxiu no existeix, el crea
  * @param ruta - La ruta de la DB a connectar.
  * @return La connexió a la ruta.
  */
	public static Connection connect(String ruta){
		try {
			//Carreguem el driver de SQLite
			Class.forName("org.sqlite.JDBC");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	  
		//Declarem la connexió
		Connection conn = null;
	
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + ruta);
			System.out.println("Connexió a la base de dades feta correctament a la ruta: " + ruta);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	 }
}

