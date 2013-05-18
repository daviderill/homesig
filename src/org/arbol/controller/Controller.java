package org.arbol.controller;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.arbol.dao.Model;
import org.arbol.domain.Node;
import org.arbol.gui.View;


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
	}
	
	class FileListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			//Doble click amb el botó esquerre
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				String source = e.getComponent().getClass().getName();
				if (source.equals("javax.swing.JLabel")) {
					JLabel label = (JLabel)e.getSource();
					Node n = myModel.getNodeNamed(label.getText());
					if (n.getExtension_id() == null) {
						ArrayList<Node> files = n.getChildren();
						myModel.createPathOf(label.getText());
						myView.drawChildren(files);
						myView.drawBreadcrumb(myModel.drawPath());
					}
					else {
						File file = new File(n.getLink());
						System.out.println(file.getAbsolutePath());
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
					
					
				}
				else {
					// Hem clicat al panel_files
				}
			}
			//Un click amb el botó dret
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
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class BreadCrumbListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			String source = e.getComponent().getClass().getName();
			if (source.equals("javax.swing.JLabel")) {
				JLabel label = (JLabel)e.getSource();
				if (label.getText().equals("INICI ")) {
					ArrayList<Node> files = myModel.getFirstLevel();
					myView.drawChildren(files);
					myView.drawBreadcrumb(myModel.drawEmptyPath());
				}
				else {
					Node n = myModel.getNodeNamed(label.getText());
					if (n.getExtension_id() == null) {
						ArrayList<Node> files = n.getChildren();
						myModel.createPathOf(label.getText());
						myView.drawChildren(files);
						myView.drawBreadcrumb(myModel.drawPath());
					}
					else {
						System.out.println("Es un fitxer!");
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class LinkListener implements HyperlinkListener {

		@Override
		public void hyperlinkUpdate(HyperlinkEvent hle) {  
			if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {  
				File file = new File(hle.getDescription());
				try {
					System.out.println(hle.getDescription());
					Desktop.getDesktop().open(file);
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
