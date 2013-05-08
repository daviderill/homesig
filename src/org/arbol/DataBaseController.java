package org.arbol;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBaseController {
	
	private Connection conn = null;
	// Llista on guardem tots els fitxers que llegirem
	private ArrayList<Node> nodes;
	
	public DataBaseController(Connection conn) {
		this.conn = conn;
		nodes = new ArrayList<Node>();
	}
	
	public void createTree() {
		Statement stat = null;
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select * from main;");
			
		    while (rs.next()) {
		    	// llegim el fitxer i l'afegim a la nostra llista de fitxers
		    	Node n = new Node(rs.getInt("id"),rs.getString("name"),rs.getString("link"),
		    			rs.getInt("level"),rs.getInt("position"),rs.getInt("parent_id"),
		    			rs.getString("extension_id"));
		    	nodes.add(n);
		    	
		    	// Assignem el fill a la llista de fills del seu pare
		    	if (n.getParent_id() != 0) {
		    		assignParent(n);
		    	}
		    }
		    rs.close();
		    conn.close();
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
	}

	// Per debugar i veure que les relacions pare-fill s'han fet bé
	public void drawFamily() {
		for (int i=0; i < nodes.size(); ++i) {
			System.out.println("I'm node id: " + nodes.get(i).getId() + " My children are:");
			for (int j=0; j < nodes.get(i).getChildrenSize(); ++j) {
				System.out.println("    " + nodes.get(i).getChildren().get(j).getId());
			}
		}
	}

	// Pinta a consola com quedaria l'arbre
	public void drawTree() {
		for (int i=0; i < nodes.size(); ++i) {
			if (nodes.get(i).getLevel() == 1) {
				System.out.print("--");
				System.out.println(nodes.get(i).getId());
				for (int j=0; j < nodes.get(i).getChildrenSize(); ++j) {
					drawTreeForChildren(1,nodes.get(i).getChildren().get(j));
				}
			}
		}
		
	}
	
	public ArrayList<Node> getFirstLevel() {
		ArrayList<Node> result = new ArrayList<Node>();
		for (int i=0; i < nodes.size(); ++i) {
			if (nodes.get(i).getLevel() == 1) {
				result.add(nodes.get(i));
			}
		}
		return result;
	}

	// Mètode auxiliar de drawTree
	private void drawTreeForChildren(int level,Node node) {
		for (int j=0; j < level+1; ++j) {
			System.out.print("--");
		}
		System.out.println(node.getId());
		for (int i=0; i < node.getChildrenSize(); ++i) {
			drawTreeForChildren(level+1, node.getChildren().get(i));
		}
	}

	private void assignParent(Node children) {
		for (int i=0; i < nodes.size(); ++i) {
			if (nodes.get(i).getId() == children.getParent_id()) {
				nodes.get(i).addChildren(children.getPosition(),children);
			}
		}
	}
	
	public ArrayList<Node> getAll() {
		return nodes;
	}
}
