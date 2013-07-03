package org.arbol.domain;

public class News {

	private String id;
	private String title;
	private String desc;
	private String link;
	
	
	public News(String id, String title, String desc, String link) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.link = link;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
