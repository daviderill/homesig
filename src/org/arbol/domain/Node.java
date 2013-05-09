package org.arbol.domain;

import java.util.ArrayList;

public class Node {
	
	private int id;
	private String name;
	private String link;
	private int level;
	private int position;
	private int parent_id;
	private String extension_id;
	private ArrayList<Node> children;
	private Node parent;
		
	
	public Node(int id, String name, String link, int level, int position,
			int parent_id, String extension_id) {
		super();
		this.id = id;
		this.name = name;
		this.link = link;
		this.level = level;
		this.position = position;
		this.parent_id = parent_id;
		this.extension_id = extension_id;
		children = new ArrayList<Node>();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public String getExtension_id() {
		return extension_id;
	}
	public void setExtension_id(String extension_id) {
		this.extension_id = extension_id;
	}
	public ArrayList<Node> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}
	
	public void addChildren(int position, Node child) {
		children.add(position-1, child);
	}
	
	public int getChildrenSize() {
		return children.size();
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	
}
