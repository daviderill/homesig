package es.psig.homesig.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class DatabaseScript {

	private final File fFile;
	private static String PATH_TO_FILE = "config/SSadurniLT_gvsig_aj.opr";
	private static String PATH_TO_DB = "config/BDProva.sqlite";
	private Connection conn = null;
	private String id = null;
	private String name = null;
	private String tooltip = null;
	private String link = null;
	private String parentId = null;

	
	public DatabaseScript() {
		fFile = new File(PATH_TO_FILE);
		setConnection();
	}
	

	public boolean setConnection() {

		try {
			Class.forName("org.sqlite.JDBC");
			File file = new File(PATH_TO_DB);
			if (file.exists()) {
				conn = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
				return true;
			} else {
				Utils.showError("File not found", PATH_TO_DB, "Arbol");
				return false;
			}
		} catch (SQLException e) {
			Utils.showError("Database Error Connection", e.getMessage(), "Arbol");
			return false;
		} catch (ClassNotFoundException e) {
			Utils.showError("Database Error Connection",
					"ClassNotFoundException", "Arbol");
			return false;
		}
	}

	/** Template method that calls {@link #processLine(String)}. */
	public final void processLineByLine() {
		// Note that FileReader is used, not File, since File is not Closeable
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = "DELETE from main";
			try {
				stmt.executeUpdate(sql);
			} catch (NullPointerException e) {
				Utils.getLogger().warning("Error a l'executar el statement. La connexió pot ser nul·la?");
			}

		} catch (SQLException e) {
			Utils.getLogger().warning("Error de SQL " + e.getMessage());
		}

		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(fFile));
		} catch (FileNotFoundException e1) {
			Utils.getLogger().warning("Fitxer no trobat");
		}
		int line = 0;
		try {
			while (scanner.hasNextLine() && line < 23) {
				scanner.nextLine();
				++line;
			}
			while (scanner.hasNextLine()) {
				processLine(scanner.nextLine());
			}
		} finally {
			// ensure the underlying stream is always closed
			// this only has any effect if the item passed to the Scanner
			// constructor implements Closeable (which it does in this case).
			scanner.close();
			try {
				conn.close();
			} catch (SQLException e) {
				Utils.getLogger().warning("Error de SQL " + e.getMessage());
			}
		}
	}

	protected void processLine(String aLine) {

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(aLine);
		if (aLine.startsWith("[")) {
			// resetegem tot, per a un nou registre
			id = null;
			name = null;
			tooltip = null;
			link = null;
			parentId = null;

			// Afegim el id
			if (aLine.length() > 3)
				id = aLine.substring(3, aLine.length() - 1);
			else
				id = "0";

			// Afegim id del pare
			String[] id_parts = id.split("_");
			parentId = null;
			if (id_parts.length > 1) {
				parentId = id_parts[0];
			}
			for (int i = 1; i < id_parts.length - 1; ++i) {
				parentId += "_" + id_parts[i];
			}
		} else {
			scanner.useDelimiter("=");
			if (scanner.hasNext()) {
				String name = scanner.next();
				String value = scanner.next();
				assignValue(name, value);
			} else {
				// S'ha acabat, guardem a BD
				if (!id.equals("0"))
					insertEntry(id, name, tooltip, link, parentId);
			}
		}

	}

	
	private void assignValue(String field, String value) {
		if (field.equals("nom"))
			name = value;
		else if (field.equals("enllaç"))
			link = value;
		else if (field.equals("tooltip"))
			tooltip = value;
	}

	
	public void insertEntry(String id, String name, String tooltip, String link, String parent_id) {
		
		PreparedStatement stat = null;
		String sql = "INSERT into main (id,name,tooltip,link,parent_id) VALUES (?,?,?,?,?);";
		try {
			stat = conn.prepareStatement(sql);
			stat.setString(1, id);
			stat.setString(2, name);
			stat.setString(3, tooltip);
			stat.setString(4, link);
			stat.setString(5, parent_id);
			stat.executeUpdate();
			Utils.getLogger().info("Insertat registre: " + id + ", " + name + ", " + link + ", " + parent_id);
		} catch (SQLException e) {
			Utils.getLogger().warning("Error de SQL " + e.getMessage());
		}
	}
	
	
}