package org.arbol.domain;

import java.util.ArrayList;

public class Node {
	
	private String id;
	private String name;
	private String link;
	private String tooltip;
	private int level;
	private int position;
	private String parent_id;
	private String extension_id;
	private ArrayList<Node> children;
	private Node parent;
		
	
	public Node(String id, String name, String link, String tooltip) {
		super();
		System.out.println("--- TRACTAMENT DEL NODE: " + id + " ---");
		this.id = id;
		this.name = name;
		this.tooltip = tooltip;
		extractInfoFromId(id);
		extractInfoFromLink(link);
		children = new ArrayList<Node>();
		
		
	}
	private void extractInfoFromId(String id) {
		String[] id_parts = id.split("_");	
		level = id_parts.length;
		System.out.println("NIVELL: " + level);
		position = Integer.valueOf(id_parts[id_parts.length - 1]);
		System.out.println("POSICIO: " + position);
		String parentId = null;
		if (id_parts.length > 1) {
			parentId = id_parts[0];
		}
		for (int i=1; i < id_parts.length - 1; ++i) {
			parentId += "_" + id_parts[i];
		}
		System.out.println("PARE: " + parentId);
		parent_id = parentId;
	}
	
	private void extractInfoFromLink(String name) {
		link = name;
		System.out.println("LINK: " + link);
		if (name != null) {
			String[] path = name.split(":?\\\\");
			String file = path[path.length-1];
			System.out.println("FILE: " + file);
			String[] file_parts = file.split("\\.");
			extension_id = file_parts[file_parts.length - 1];
			System.out.println("EXTENSIO: " + extension_id);
		}
	}
	
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
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
		children.add(child);
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
