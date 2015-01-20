package es.psig.homesig.controller;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
 * Classe que t� acc�s a la vista i al model. Ella crea els listeners, que assigna a la vista
 * @author Roger Erill Carrera
 *
 */
public class Controller {
	
	private View myView;
	private Model myModel;
	
	
	public Controller(View v, Model m) {
		
		myView = v;
		myModel = m;
		myView.addFileListener(new FileListener());
		myView.addBreadcrumbListener(new BreadCrumbListener());
		myView.addLinkListener(new LinkListener());
		initializeProperties();
		initalizeFirstLevel();
		initializeLinks();
		initializeNews();
		
	}

	
	private void initializeProperties() {
		
		myView.setWindowTitle(myModel.getWindowTitle());
		myView.setTitleIcon(myModel.getTitleIcon());
		myView.setBackgroundColor(myModel.getBackground());
		myView.setDarkGrey(myModel.getDarkGrey());
		myView.setLightGrey(myModel.getLightGrey());
		myView.setTitlesForeground(myModel.getTitlesForeground());
		myView.setIniciForeground(myModel.getIniciForeground());
		myView.setBreadcrumbForeground(myModel.getBreadcrumbForeground());	
		myView.setUpperLogo(myModel.getUpperLogoPath());
		myView.setTitle(myModel.getTitle());
		myView.setSubtitle(myModel.getSubtitle());
		myView.setAddress(myModel.getAddress());
		myView.setTelephone(myModel.getTelephone());
		myView.setFax(myModel.getFax());
		myView.setEmail(myModel.getEmail());
		myView.setConsultor(myModel.getConsultor());
		myView.setWebDesign(myModel.getWebDesign());
		
	}
	
	
	private void initalizeFirstLevel() {
		myModel.createTree();
		ArrayList<Node> files = myModel.getFirstLevel();
		myView.drawChildren(files);
		myView.drawBreadcrumb(myModel.drawPath());
	}
	
	
	private void initializeNews() {
		myModel.createNews();
		ArrayList<String> news = myModel.createHtlm();
		myView.drawNews(news);
	}
	
	
	private void initializeLinks() {
		myModel.createLinks();
		ArrayList<Links> links = myModel.createLinksHtlm();
		myView.drawLinks(links);
	}
	
	
	private void drawDirectory(Node n) {
		ArrayList<Node> files = n.getChildren();
		String parent_name = null;
		if (n.getParent() != null) parent_name = n.getParent().getName();
		myModel.createPathOf(n.getName(), parent_name);
		myView.drawChildren(files);
		myView.drawBreadcrumb(myModel.drawPath());
	}
	
	
	private boolean isMap(String s) {
		if (s != null) {
			int pos = s.lastIndexOf('.');
			String ext = s.substring(pos+1);
			return ext.equals("gvp");
		}
		return false;
	}
	
	
	private boolean isDangerousExtension(String s) {
		if (s != null) {
			int pos = s.lastIndexOf('.');
			String ext = s.substring(pos+1);
			return ext.equals("exe") || ext.equals("jar");
		}
		return false;
	}
	
	
	private void openFile(Node n) {
		
		Utils.getLogger().info("Obrim el node " + n.getName() + " que te enlla� " + n.getLink());
		File file = new File(n.getLink());
		try {
			if (isMap(file.getName())) {
				Utils.getMapLogger().info("Obrim el mapa " + file.getName());
			}
			else if (isDangerousExtension(file.getName())) {
				int reply = JOptionPane.showConfirmDialog(null, "Aquest fitxer t� una extensi� que pot ser perillosa. Obrir el fitxer?"
						, "Confirmaci� d'obertura de fitxer", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					Desktop.getDesktop().open(file);
				}
			}
			else {
				Desktop.getDesktop().open(file);
			}
		} 
		catch (IOException e1) {
			Utils.getLogger().warning("Error al obrir el fitxer: " + e1.getMessage() + "\n" +
					"Pot ser que no tinguis cap aplicaci� associada a l'extensi� ." + n.getExtension_id() + "?");
			myView.showErrorFileNotOpeneable(n.getLink());
		}
		catch (IllegalArgumentException e2) {
			Utils.getLogger().warning("No hi ha cap fitxer a la ruta " + n.getLink());	
			myView.showErrorFileNotFound(n.getLink());	
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
			
			//Doble click amb el bot� esquerre
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
					int s = myModel.getCurrentPath().size();
					String pare = null;
					if (s > 0) {
						pare = myModel.getCurrentPath().get(s-1).getName();
					}
					Node n = myModel.getNodeNamed(label_text, pare);
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
			
			//Un click amb el bot� dret, pugem un nivell de directori
			else if (e.getButton() == MouseEvent.BUTTON3) {
				myModel.removeLastPathNode();
				Node newParent = myModel.getCurrentParent();
				ArrayList<Node> files;
				if (newParent == null) {
					files = myModel.getFirstLevel();
				}
				else {
					files = newParent.getChildren();
				}
				myView.drawChildren(files);
				myView.drawBreadcrumb(myModel.drawPath());
			}
			
			// Un clic de bot� esquerre, seleccionem el fitxer
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
					myView.paintComponent(label);
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
					ArrayList<Node> files = myModel.getFirstLevel();
					myView.drawChildren(files);
					myView.drawBreadcrumb(myModel.drawEmptyPath());
				}
				else {
					ArrayList<Node> path = myModel.getCurrentPath();
					int s = path.size();
					String pare = null;
					for (int i=0; i < s; ++i) {
						if (path.get(i).getName().equals(label.getText())) {
							if (i > 0) {
								pare = path.get(i-1).getName();
							}
						}
					}
					Node n = myModel.getNodeNamed(label.getText(),pare);
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
				File file = new File(hle.getDescription());
				try {
					
					if (file.isFile()) {
						Utils.getLogger().info("Considerem " + hle.getDescription() + " un fitxer");
						Node n = myModel.getNodeWithLink(hle.getDescription());
						if (n == null) {
							Utils.getLogger().warning("No s'ha trobat el fitxer " + hle.getDescription());
							myView.showErrorFileNotFound(hle.getDescription());
						}
						else {
							myView.setSelectedLabel(n.getName());
							drawDirectory(n.getParent());
							try {
								Desktop.getDesktop().open(file);
							}
							catch (IOException e) {
								Utils.getLogger().warning("No es pot obrir el fitxer " + file.getCanonicalPath());
							}
						}
					}
					else if (hle.getDescription().contains("http")) {
						try {
							Utils.getLogger().info("Considerem " + hle.getDescription() + " un enlla� extern");
							Desktop.getDesktop().browse(hle.getURL().toURI());
						} catch (URISyntaxException e) {
							Utils.getLogger().warning("Error en obrir la URL " + hle.getDescription());
							Utils.getLogger().warning("Motiu: " + e.getMessage());
						}
					}
					else {
						Utils.getLogger().info("Considerem " + hle.getDescription() + " un directori o fitxer inexistent");
						Node n = myModel.getNodeDirectoryNamed(file.getName());
						if (n != null){
							drawDirectory(n);
						}
						else {
							Utils.getLogger().info(file.getName() + "No �s un directori, mirem si aconseguim obrir com a fitxer");
							n = myModel.getNodeWithLink(hle.getDescription());
							if (n != null) {
								myView.setSelectedLabel(n.getName());
								drawDirectory(n.getParent());
								Desktop.getDesktop().open(file);
							}
							else {
								Utils.getLogger().warning("No existeix cap fitxer o directori a " + hle.getDescription());
								myView.showErrorFileNotFound(hle.getDescription());
							}
						}
					}
				} 
				catch (IOException e1) {
					Utils.getLogger().warning(e1.getMessage());						
				}
				catch (IllegalArgumentException e2) {
					myView.showErrorFileNotFound(hle.getDescription());	
				}
			}  
		}
		
	}
	
	
}