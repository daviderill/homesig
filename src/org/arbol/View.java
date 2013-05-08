package org.arbol;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SpringLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.SystemColor;

public class View {

	public JFrame frame;
	private JPanel panel_files;
	ArrayList<Node> files;
	ImageIcon icon_folder = new ImageIcon("res\\Imagen 3.png");
	ImageIcon icon_pdf = new ImageIcon("res\\pdf.png");

	public View() {
		initialize();
	}
	
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} 
		else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
}
	public void drawChildren(ArrayList<Node> files) {
		panel_files.removeAll();
		panel_files.updateUI();
		for (int i=0; i < files.size(); ++i) {
			final Node file = files.get(i);
			JLabel label_file;
			if (file.getExtension_id().equals("dir")) {
				label_file = new JLabel(file.getName(), icon_folder, SwingConstants.LEFT);
			}
			else {
				label_file = new JLabel(file.getName(), icon_pdf, SwingConstants.LEFT);
			}
			label_file.setHorizontalTextPosition(SwingConstants.CENTER);
			label_file.setVerticalTextPosition(JLabel.BOTTOM);
			label_file.setFont(new Font("Georgia", Font.PLAIN, 15));
			label_file.setPreferredSize(new Dimension(141, 141));
			label_file.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					if (e.getClickCount() == 2) {
						System.out.println("Double click");
						drawChildren(file.getChildren());
					}
					else if (e.getButton() == MouseEvent.BUTTON3) {
						int id = file.getParent_id();
						Node parent = getParentById(id);
						if (parent != null) {
							id = parent.getParent_id();
							Node grandparent = getParentById(id);
							if (grandparent != null) {
								drawChildren(grandparent.getChildren());
							}
							else drawChildren(getFirstLevel());
						} 
						else drawChildren(getFirstLevel());
					}
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			panel_files.add(label_file);
		}
		
	}
	
	public ArrayList<Node> getFirstLevel() {
		ArrayList<Node> result = new ArrayList<Node>();
		for (int i=0; i < files.size(); ++i) {
			if (files.get(i).getLevel() == 1) {
				result.add(files.get(i));
			}
		}
		return result;
	}
	
	public void setFiles(ArrayList<Node> f) {
		files = f;
	}

	private Node getParentById(int id) {
		for (int i=0; i < files.size(); ++i) {
			if (files.get(i).getId() == id) {
				return files.get(i);
			}
		}
		return null;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1240, 700);
		frame.setTitle("Sistema d'informaci\u00F3 territorial");
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel_top_logo = new JPanel();
		panel_top_logo.setBackground(new Color(255, 255, 255));
		
		FlowLayout fl_panel_files = new FlowLayout(FlowLayout.LEFT);
		panel_files = new JPanel(fl_panel_files);
		panel_files.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel panel_title = new JPanel();
		
		JPanel panel_links_title = new JPanel();
		
		JLabel lblEnllaos = new JLabel("Enlla\u00E7os");
		lblEnllaos.setForeground(Color.GRAY);
		lblEnllaos.setHorizontalAlignment(SwingConstants.LEFT);
		lblEnllaos.setFont(new Font("Georgia", Font.PLAIN, 25));
		panel_links_title.add(lblEnllaos);
		
		JPanel panel_news_title = new JPanel();
		
		JLabel lblNotcies = new JLabel("Not\u00EDcies");
		lblNotcies.setForeground(Color.GRAY);
		lblNotcies.setHorizontalAlignment(SwingConstants.LEFT);
		lblNotcies.setFont(new Font("Georgia", Font.PLAIN, 25));
		panel_news_title.add(lblNotcies);
		
		JPanel panel_bottom_logo = new JPanel();
		panel_bottom_logo.setBackground(Color.WHITE);
		panel_bottom_logo.setForeground(Color.WHITE);
		
		JPanel panel_info = new JPanel();
		panel_info.setFont(new Font("Gentium Book Basic", Font.PLAIN, 10));
		
		JPanel panel_news_content = new JPanel();
		
		JPanel panel_links_content = new JPanel();
		
		JPanel panel_breadcrumb = new JPanel();
		panel_breadcrumb.setBorder(null);
		panel_breadcrumb.setBackground(SystemColor.controlHighlight);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(21)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_news_title, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_news_content, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_bottom_logo, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_info, GroupLayout.PREFERRED_SIZE, 458, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_breadcrumb, GroupLayout.PREFERRED_SIZE, 458, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addComponent(panel_title, GroupLayout.PREFERRED_SIZE, 576, GroupLayout.PREFERRED_SIZE)
									.addComponent(panel_files, GroupLayout.PREFERRED_SIZE, 751, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_top_logo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_links_content, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_links_title, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_top_logo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_title, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(4)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_links_title, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(50)
								.addComponent(panel_breadcrumb, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(38)
								.addComponent(panel_news_title, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_links_content, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
						.addComponent(panel_files, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
						.addComponent(panel_news_content, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
					.addGap(67)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_bottom_logo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_info, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_info.setLayout(new MigLayout("", "[55px][40px][30px][90px][46px]", "[14px][][][]"));
		
		JLabel lblNewLabel_4 = new JLabel("Pla\u00E7a Ajuntament, 1  08770 Sant Sadurn\u00ED d'Anoia");
		lblNewLabel_4.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNewLabel_4, "cell 0 0 3 1,alignx left,aligny top");
		
		JLabel lblNewLabel_5 = new JLabel("Tel +34 938 910 325   Fax +34 938 183 470  E-mail: ajuntament@santsadurni.cat");
		lblNewLabel_5.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNewLabel_5, "cell 0 1 4 1,alignx left,aligny top");
		
		JLabel lblNewLabel_6 = new JLabel("Disseny web");
		lblNewLabel_6.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNewLabel_6, "cell 0 2");
		
		JLabel lblNewLabel_7 = new JLabel("Av\u00EDs legal");
		lblNewLabel_7.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblNewLabel_7, "flowx,cell 0 3");
		
		JLabel lblNewLabel_8 = new JLabel("Inici");
		lblNewLabel_8.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblNewLabel_8, "cell 1 3");
		
		JLabel lblMapaWeb = new JLabel("Mapa web");
		lblMapaWeb.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblMapaWeb, "cell 2 3");
		panel_breadcrumb.setLayout(new SpringLayout());
		
		JLabel lblNewLabel_3 = new JLabel("");
		//lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\Roger\\workspace\\Arbre\\res\\logopie.gif"));
		lblNewLabel_3.setIcon(new ImageIcon("res\\logopie.gif"));
		panel_bottom_logo.add(lblNewLabel_3);
		panel_title.setLayout(new MigLayout("", "[450px]", "[41px][31px]"));
		
		JLabel lblNewLabel_1 = new JLabel("Sistema d'informaci\u00F3 territorial");
		lblNewLabel_1.setForeground(Color.GRAY);
		panel_title.add(lblNewLabel_1, "cell 0 0,alignx left,aligny top");
		lblNewLabel_1.setBackground(Color.LIGHT_GRAY);
		lblNewLabel_1.setFont(new Font("Georgia", Font.PLAIN, 33));
		
		JLabel lblNewLabel_2 = new JLabel("SIG dels Serveis T\u00E8cnics");
		lblNewLabel_2.setForeground(Color.GRAY);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2.setFont(new Font("Georgia", Font.PLAIN, 25));
		panel_title.add(lblNewLabel_2, "cell 0 1,alignx left,aligny top");
		
		JLabel lblNewLabel = new JLabel("");
		//lblNewLabel.setIcon(new ImageIcon("C:\\Users\\Roger\\workspace\\arbol2\\res\\logo2.png"));
		lblNewLabel.setIcon(new ImageIcon("res\\logo2.png"));
		panel_top_logo.add(lblNewLabel);
		frame.getContentPane().setLayout(groupLayout);
	}

	

	
}
