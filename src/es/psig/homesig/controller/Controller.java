package es.psig.homesig.controller;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import es.psig.homesig.dao.Model;
import es.psig.homesig.gui.View;
import es.psig.homesig.model.Links;
import es.psig.homesig.model.Node;
import es.psig.homesig.util.Utils;


/**
 * Classe que té accés a la vista i al model. Ella crea els listeners, que assigna a la vista
 * @author Roger Erill Carrera
 *
 */
public class Controller {
	
	protected View view;
	protected Model model;
	protected static String rootPath;
	
	
	public Controller(View view, Model model) {
		
		this.view = view;
		this.model = model;
		this.view.addFileListener(new FileListener());
		this.view.addBreadcrumbListener(new BreadCrumbListener());
		this.view.addLinkListener(new LinkListener());
		this.view.setController(this);
		initializeProperties();
		initalizeFirstLevel();
		initializeLinks();
		initializeNews();
		getRootPath();
		
	}
	
	
	// Parent of current appPath
    public static String getRootPath() {
    	
    	if (rootPath == null) {
	    	CodeSource codeSource = Utils.class.getProtectionDomain().getCodeSource();
	    	File jarFile;
	    	try {
	    		jarFile = new File(codeSource.getLocation().toURI().getPath());
	    	   	rootPath = jarFile.getParentFile().getParent()+File.separator;
	    	}
	    	catch (URISyntaxException e) {
	    		JOptionPane.showMessageDialog(null, e.getMessage(), "getRootPath Error", JOptionPane.ERROR_MESSAGE);
	    	}
    	}
    	return rootPath;
    	
    }	

	
	public void insertLog(String msg) {
		model.logInfo(msg);
	}
	
	
	private void initializeProperties() {
		
		Color defaultColor = Color.BLACK;
		view.setTitleIcon(model.getTitleIcon());
		view.setBackgroundColor(model.getColorParam("backgroundColor", defaultColor));
		view.setBackFillAriadnaColor(model.getColorParam("backFillAriadnaColor", defaultColor));
		view.setBackTextBoxColor(model.getColorParam("backTextBoxColor", defaultColor));
		view.setBackMain(model.getColorParam("backMain", defaultColor));
		view.setBackFilesColor(model.getColorParam("backFilesColor", defaultColor));
		view.setBackCreditsColor(model.getColorParam("backCreditsColor", Color.WHITE));
		
		view.setTitlesColor(model.getColorParam("titlesColor", defaultColor));
		view.setIniciFontColor(model.getColorParam("iniciFontColor", defaultColor));
		view.setBreadcrumbFontColor(model.getColorParam("breadcrumbFontColor", defaultColor));	
		view.setUpperLogo(model.getUpperLogo());
		view.setShowPanelInfo(model.getIntegerParam("showPanelInfo"));
		
		// Cap�alera
		view.setWindowTitle(model.getWindowTitle());
		view.setTitle2(model.getTitle());
		view.setTitle3(model.getSubtitle());
		
		// Valors per defecte
		view.setDefaultFontName(model.getStringParam("defaultFontName"));
		view.setDefaultFontSize(model.getIntegerParam("defaultFontSize"));
		view.setDefaultFontStyle(model.getIntegerParam("defaultFontStyle"));
		view.setDefaultFontColor(model.getColorParam("defaultFontColor"));
		
		// Not�cies
		view.setNewsFontName(model.getStringParam("newsFontName"));
		view.setNewsFontSize(model.getIntegerParam("newsFontSize"));
		view.setNewsFontStyle(model.getIntegerParam("newsFontStyle"));
		view.setNewsFontColor(model.getColorParam("newsFontColor"));
		view.setNewsFont();
		
		// Arxius
		view.setFilesFontName(model.getStringParam("filesFontName"));
		view.setFilesFontSize(model.getIntegerParam("filesFontSize"));
		view.setFilesFontStyle(model.getIntegerParam("filesFontStyle"));
		view.setFilesFontColor(model.getColorParam("filesFontColor"));
		view.setFilesFont();
		
		// Links
		view.setLinksFontName(model.getStringParam("linksFontName"));
		view.setLinksFontSize(model.getIntegerParam("linksFontSize"));
		view.setLinksFontStyle(model.getIntegerParam("linksFontStyle"));
		view.setLinksFontColor(model.getColorParam("linksFontColor"));
		view.setLinksFont();
		
		// Crèdits
		view.setCreditsFontName(model.getStringParam("creditsFontName"));
		view.setCreditsFontSize(model.getIntegerParam("creditsFontSize"));
		view.setCreditsFontStyle(model.getIntegerParam("creditsFontStyle"));
		view.setCreditsFontColor(model.getColorParam("creditsFontColor"));
		view.setCreditsFont();
		
		view.setAddress(model.getAddress());
		view.setTelephone(model.getTelephone());
		view.setFax(model.getFax());
		view.setEmail(model.getEmail());
		view.setConsultor(model.getConsultor());
		view.setWebDesign(model.getWebDesign());
		view.setCredits(model.getStringParam("credits"));
		
	}
	
	
	private void initalizeFirstLevel() {
		model.createTree();
		ArrayList<Node> files = model.getFirstLevel();
		view.drawChildren(files);
		view.drawBreadcrumb(model.drawPath());
	}
	
	
	private void initializeNews() {
		model.createNews();
		ArrayList<String> news = model.createHtml();
		view.drawNews(news);
	}
	
	
	private void initializeLinks() {
		model.createLinks();
		ArrayList<Links> links = model.createLinksHtml();
		view.drawLinks(links);
	}
	
	
	protected void drawDirectory(Node n) {
		ArrayList<Node> files = n.getChildren();
		String parent_name = null;
		if (n.getParent() != null) parent_name = n.getParent().getName();
		model.createPathOf(n.getName(), parent_name);
		view.drawChildren(files);
		view.drawBreadcrumb(model.drawPath());
	}
	
	
	private boolean isDangerousExtension(String s) {
		if (s != null) {
			int pos = s.lastIndexOf('.');
			String ext = s.substring(pos+1);
			return ext.equals("exe") || ext.equals("jar");
		}
		return false;
	}
	
	
	protected void openFile(Node n) {
		
		model.insertLog(n.getName(), n.getLink());
		Utils.getLogger().info("Obrim el node "+n.getName()+" que te enllaç "+n.getLink());
		String filePath = rootPath+n.getLink();
		System.out.println(filePath);
		File file = new File(filePath);
		try {
			if (isDangerousExtension(file.getName())) {
				int reply = JOptionPane.showConfirmDialog(null, "Aquest fitxer té una extensió que pot ser perillosa. Obrir el fitxer?"
					, "Confirmació d'obertura de fitxer", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					Desktop.getDesktop().open(file);
				}
			}
			else {
				Desktop.getDesktop().open(file);
			}
		} 
		catch (IOException e1) {
			Utils.getLogger().warning("Error al obrir el fitxer: "+e1.getMessage()+"\n" +
				"Pot ser que no tinguis cap aplicació associada a l'extensió ."+n.getExtension_id()+"?");
			view.showErrorFileNotOpeneable(n.getLink());
		}
		catch (IllegalArgumentException e2) {
			Utils.getLogger().warning("No hi ha cap fitxer a la ruta "+n.getLink());	
			view.showErrorFileNotFound(n.getLink());	
		}
		catch (UnsupportedOperationException e3) {
			Utils.getLogger().warning("El sistema no accepta aquesta operaci�");
		}
		
	}
	
	
	/**
	 * Classe que fa de listener pels fitxers
	 * @author Roger Erill Carrera
	 *
	 */
	class FileListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			// Doble click amb el botó esquerre
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				String source = e.getComponent().getClass().getName();
				if (source.equals("javax.swing.JLabel")) {
					JLabel label = (JLabel)e.getSource();		
					String label_text = label.getText();
					// Si el label cont� html perqu� el seu nom s'ha de truncar, li treiem
					if (label_text.contains("<html>") && !label_text.contains("<br>")) {
						label_text = label_text.substring(14, label_text.length() - 16);
					}
					else if (label_text.contains("<br><br>")) {
						label_text = label_text.substring(14, label_text.length() - 24);
					}
					else if (label_text.contains("<br>")) {
						label_text = label_text.substring(14, label_text.length() - 20);
					}
					int s = model.getCurrentPath().size();
					String pare = null;
					if (s > 0) {
						pare = model.getCurrentPath().get(s-1).getName();
					}
					Node n = model.getNodeNamed(label_text, pare);
					if (n.getExtension_id() == null || n.getExtension_id().trim().isEmpty()) {
						drawDirectory(n);
					}
					else {
						openFile(n);
					}
				}
				else {
					// Hem clicat al panel_files
				}
			}
			
			// Un click amb el botó dret, pugem un nivell de directori
			else if (e.getButton() == MouseEvent.BUTTON3) {
				model.removeLastPathNode();
				Node newParent = model.getCurrentParent();
				ArrayList<Node> files;
				if (newParent == null) {
					files = model.getFirstLevel();
				}
				else {
					files = newParent.getChildren();
				}
				view.drawChildren(files);
				view.drawBreadcrumb(model.drawPath());
			}
			
			// Un clic de botó esquerre, seleccionem el fitxer
			else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
				String source = e.getComponent().getClass().getName();
				if (source.equals("javax.swing.JLabel")) {
					JLabel label = (JLabel)e.getSource();
					String label_text = label.getText();
					// Si el label cont� html perqu� el seu nom s'ha de truncar, li treiem
					if (label_text.contains("<html>") && !label_text.contains("<br>")) {
						label_text = label_text.substring(14, label_text.length() - 16);
						label.setText(label_text);
					}
					else if (label_text.contains("<br><br>")) {
						label_text = label_text.substring(14, label_text.length() - 24);
						label.setText(label_text);
					}
					else if (label_text.contains("<br>")) {
						label_text = label_text.substring(14, label_text.length() - 20);
						label.setText(label_text);
					}
					view.paintComponent(label);
				}
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	
	
	/**
	 * Classe que fa de listener pels labels del breadcrumb
	 * @author Roger Erill Carrera
	 *
	 */
	class BreadCrumbListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			String source = e.getComponent().getClass().getName();
			if (source.equals("javax.swing.JLabel")) {
				JLabel label = (JLabel)e.getSource();
				if (label.getText().equals("Inici ")) {
					ArrayList<Node> files = model.getFirstLevel();
					view.drawChildren(files);
					view.drawBreadcrumb(model.drawEmptyPath());
				}
				else {
					ArrayList<Node> path = model.getCurrentPath();
					int s = path.size();
					String pare = null;
					for (int i=0; i < s; ++i) {
						if (path.get(i).getName().equals(label.getText())) {
							if (i > 0) {
								pare = path.get(i-1).getName();
							}
						}
					}
					Node n = model.getNodeNamed(label.getText(),pare);
					if (n.getExtension_id() == null || n.getExtension_id().trim().isEmpty()) {
						drawDirectory(n);
					}
					
				}
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	
	
	/**
	 * Classe que fa de listener pels links dins de les not�cies
	 * @author Roger Erill Carrera
	 *
	 */
	class LinkListener implements HyperlinkListener {

		@Override
		public void hyperlinkUpdate(HyperlinkEvent hle) {  
			
			if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {  
				
				String filePath = rootPath+hle.getDescription();
				File file = new File(filePath);
				try {
					
					if (file.isFile()) {
						Utils.getLogger().info("Considerem "+hle.getDescription()+" un fitxer");
						Node n = model.getNodeWithLink(hle.getDescription());
						if (n == null) {
							Utils.getLogger().warning("No s'ha trobat el fitxer "+hle.getDescription());
							view.showErrorFileNotFound(hle.getDescription());
						}
						else {
							try {
								Desktop.getDesktop().open(file);
							}
							catch (IOException e) {
								Utils.getLogger().warning("No es pot obrir el fitxer "+file.getCanonicalPath());
							}
						}
					}
					
					else if (hle.getDescription().contains("http")) {
						try {
							Utils.getLogger().info("Considerem "+hle.getDescription()+ " un enlla� extern");
							Desktop.getDesktop().browse(hle.getURL().toURI());
						} catch (URISyntaxException e) {
							Utils.getLogger().warning("Error en obrir la URL " + hle.getDescription());
							Utils.getLogger().warning("Motiu: " + e.getMessage());
						}
					}
					else {
						Utils.getLogger().info("Considerem "+hle.getDescription()+" un directori o fitxer inexistent");
						Node n = model.getNodeDirectoryNamed(file.getName());
						if (n != null){
							drawDirectory(n);
						}
						else {
							Utils.getLogger().info(file.getName()+"no �s un directori, mirem si aconseguim obrir com a fitxer");
							n = model.getNodeWithLink(hle.getDescription());
							if (n != null) {
								view.setSelectedLabel(n.getName());
								drawDirectory(n.getParent());
								Desktop.getDesktop().open(file);
							}
							else {
								Utils.getLogger().warning("No existeix cap fitxer o directori a "+hle.getDescription());
								view.showErrorFileNotFound(hle.getDescription());
							}
						}
					}
				} 
				catch (IOException e1) {
					Utils.getLogger().warning(e1.getMessage());						
				}
				catch (IllegalArgumentException e2) {
					view.showErrorFileNotFound(hle.getDescription());	
				}
			}  
		}
		
	}

	
}