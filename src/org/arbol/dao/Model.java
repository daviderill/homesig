package org.arbol.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.arbol.domain.Node;
import org.arbol.util.DataBaseConnection;

public class Model {
	private Connection conn = null;
	// Llista on guardem tots els fitxers que llegirem
	private ArrayList<Node> nodes;
	private ArrayList<Node> currentPath;
	
	public Model() {
		conn = DataBaseConnection.connect("config/arbol2-new.sqlite");
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
			ResultSet rs = stat.executeQuery("select * from main2;");
			
		    while (rs.next()) {
		    	// llegim el fitxer i l'afegim a la nostra llista de fitxers
		    	Node n = new Node(rs.getString("id"),rs.getString("name"),rs.getString("link"),
		    			rs.getString("tooltip"));
		    	nodes.add(n);
		    	
		    	// Assignem el fill a la llista de fills del seu pare
		    	
		    }
		    for (int i=0; i < nodes.size(); ++i) {
		    	if (nodes.get(i).getLevel() > 1) {
		    		assignParent(nodes.get(i));
		    	}
		    }
		    sortChildren();
		    rs.close();
		    conn.close();
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
	}
	
	private void sortChildren() {
		Collections.sort(nodes, new NodeComparator());
		for (int i=0; i < nodes.size(); ++i) {
			Collections.sort(nodes.get(i).getChildren(), new NodeComparator());
		}
	}

	public class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node n1, Node n2) {
			return n1.getId().compareTo(n2.getId());
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
			if (nodes.get(i).getId().equals(children.getParent_id()) ) {
				nodes.get(i).addChildren(children.getPosition(),children);
				children.setParent(nodes.get(i));
			}
		}
	}
	
	public ArrayList<Node> getAll() {
		return nodes;
	}
	
	public Node getNodeNamed(String fileName) {
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


	/*public String drawPath() {
		String path = "/";
		for (int i=0; i < currentPath.size(); ++i) {
			path += currentPath.get(i).getName() + "/";
		}
		return path;
	}*/

	public String[] drawPath() {
		String[] res = new String[currentPath.size()+1];
		res[0] = "INICI ";
		for (int i=0; i < currentPath.size(); ++i) {
			res[i+1] = currentPath.get(i).getName();
		}
		return res;
	}


	public void createPathOf(String text) {
		Node n = getNodeNamed(text);
		currentPath.clear();
		createPathRecursively(n);
	}


	private void createPathRecursively(Node n) {
		if (n.getLevel() == 1) {
			currentPath.add(n);
		}
		else {
			createPathRecursively(n.getParent());
			currentPath.add(n);
		}
	}


	public String[] drawEmptyPath() {
		currentPath.clear();
		String[] res = new String[1];
		res[0] = "INICI ";
		return res;
	}

}
