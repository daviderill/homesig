package org.arbol;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Model {
	private Connection conn = null;
	// Llista on guardem tots els fitxers que llegirem
	private ArrayList<Node> nodes;
	private ArrayList<Node> currentPath;
	
	public Model() {
		conn = DataBaseConnection.connect("config/arbol2.sqlite");
		nodes = new ArrayList<Node>();
		currentPath = new ArrayList<Node>();
	}
	
	
	public ArrayList<Node> getCurrentPath() {
		return currentPath;
	}

	public Node getCurrentParent() {
		int n = currentPath.size();
		if (n > 0) {
			return currentPath.get(n-1);
		}
		return null;
	}

	public void addToPath(Node n) {
		currentPath.add(n);
	}
	
	public void removeLastPathNode() {
		int n = currentPath.size();
		if (n > 0) {
			currentPath.remove(n-1);
		}
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
	
	public ArrayList<Node> getFirstLevel() {
		ArrayList<Node> result = new ArrayList<Node>();
		for (int i=0; i < nodes.size(); ++i) {
			if (nodes.get(i).getLevel() == 1) {
				result.add(nodes.get(i));
			}
		}
		return result;
	}
	
	private void assignParent(Node children) {
		for (int i=0; i < nodes.size(); ++i) {
			if (nodes.get(i).getId() == children.getParent_id()) {
				nodes.get(i).addChildren(children.getPosition(),children);
				children.setParent(nodes.get(i));
			}
		}
	}
	
	public ArrayList<Node> getAll() {
		return nodes;
	}

	public ArrayList<Node> getChildrenOf(String fileName) {
		Node parent = getNodeNamed(fileName);
		return parent.getChildren();
	}

	private Node getNodeNamed(String fileName) {
		for (int i=0; i < nodes.size(); ++i) {
			if (nodes.get(i).getName().equals(fileName)) return nodes.get(i);
		}
		return null;
	}

	public void addToPath(String fileName) {
		if (currentPath.isEmpty()) {
			ArrayList<Node> candidatesToAdd = getFirstLevel();
			for (int i=0; i < candidatesToAdd.size(); ++i) {
				if (candidatesToAdd.get(i).getName().equals(fileName)) {
					currentPath.add(candidatesToAdd.get(i));
				}
			}
		}
		else {
			Node lastParent = currentPath.get(currentPath.size()-1);
			for (int i=0; i < lastParent.getChildrenSize(); ++i) {
				if (lastParent.getChildren().get(i).getName().equals(fileName)) {
					currentPath.add(lastParent.getChildren().get(i));
				}
			}
		}
	}


	public String drawPath() {
		String path = "/";
		for (int i=0; i < currentPath.size(); ++i) {
			path += currentPath.get(i).getName() + "/";
		}
		return path;
	}


}
