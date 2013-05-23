package org.arbol.controller;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.arbol.dao.Model;
import org.arbol.domain.Node;
import org.arbol.gui.View;

/**
 * Classe que té accés a la vista i al model. Ella crea els listeners, que assigna a la vista
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
		initalizeFirstLevel();
	}

	private void initalizeFirstLevel() {
		myModel.createTree();
		ArrayList<Node> files = myModel.getFirstLevel();
		myView.drawChildren(files);
		myView.drawBreadcrumb(myModel.drawPath());
	}
	
	private void drawDirectory(Node n) {
		ArrayList<Node> files = n.getChildren();
		myModel.createPathOf(n.getName());
		myView.drawChildren(files);
		myView.drawBreadcrumb(myModel.drawPath());
	}
	
	private void openFile(Node n) {
		File file = new File(n.getLink());
		try {
			Desktop.getDesktop().open(file);
		} 
		catch (IOException e1) {
			e1.printStackTrace();							
		}
		catch (IllegalArgumentException e2) {
			myView.showErrorFileNotFound(n.getLink());	
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
			
			//Doble click amb el botó esquerre
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				String source = e.getComponent().getClass().getName();
				if (source.equals("javax.swing.JLabel")) {
					JLabel label = (JLabel)e.getSource();		
					String label_text = label.getText();
					// Si el label conté html perquè el seu nom s'ha de truncar, li treiem
					if (label_text.contains("<html>")) {
						label_text = label_text.substring(14, label_text.length() - 16);
					}
					Node n = myModel.getNodeNamed(label_text);
					if (n.getExtension_id() == null) {
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
			//Un click amb el botó dret, pugem un nivell de directori
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
			// Un clic de botó esquerre, seleccionem el fitxer
			else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
				String source = e.getComponent().getClass().getName();
				if (source.equals("javax.swing.JLabel")) {
					JLabel label = (JLabel)e.getSource();
					String label_text = label.getText();
					// Si el label conté html perquè el seu nom s'ha de truncar, li treiem
					if (label_text.contains("<html>")) {
						label_text = label_text.substring(14, label_text.length() - 16);
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
					Node n = myModel.getNodeNamed(label.getText());
					if (n.getExtension_id() == null) {
						drawDirectory(n);
					}
					else {
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
	 * Classe que fa de listener pels links dins de les notícies
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
						Node n = myModel.getNodeWithLink(hle.getDescription());
						myView.setSelectedLabel(n.getName());
						drawDirectory(n.getParent());
						Desktop.getDesktop().open(file);
					}
					else {
						Node n = myModel.getNodeNamed(file.getName());
						if (n != null){
							drawDirectory(n);
						}
					}
				} 
				catch (IOException e1) {
					e1.printStackTrace();							
				}
				catch (IllegalArgumentException e2) {
					myView.showErrorFileNotFound(hle.getDescription());	
				}
			}  
		}
		
	}
	
}