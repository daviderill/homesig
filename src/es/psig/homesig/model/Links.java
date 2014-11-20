package es.psig.homesig.model;

public class Links {
	
	private String id;
	private String name;
	private String path;
	private String imagesrc;
	private String htmlcode;
	
	public Links(String id, String name, String path, String imagesrc) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
		this.imagesrc = imagesrc;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getImagesrc() {
		return imagesrc;
	}

	public void setImagesrc(String imagesrc) {
		this.imagesrc = imagesrc;
	}

	public String getHtmlcode() {
		return htmlcode;
	}

	public void setHtmlcode(String htmlcode) {
		this.htmlcode = htmlcode;
	}
	
	
}
