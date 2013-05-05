package org.arbol;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
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

public class View {

	public JFrame frame;
	private JPanel panel_files;
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
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1126, 617);
		frame.setTitle("Sistema d'informaci\u00F3 territorial");
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		
		panel_files = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel_files.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		
		JLabel lblEnllaos = new JLabel("Enlla\u00E7os");
		lblEnllaos.setForeground(Color.GRAY);
		lblEnllaos.setHorizontalAlignment(SwingConstants.LEFT);
		lblEnllaos.setFont(new Font("Constantia", Font.PLAIN, 25));
		panel_2.add(lblEnllaos);
		
		JPanel panel_3 = new JPanel();
		
		JLabel lblNotcies = new JLabel("Not\u00EDcies");
		lblNotcies.setForeground(Color.GRAY);
		lblNotcies.setHorizontalAlignment(SwingConstants.LEFT);
		lblNotcies.setFont(new Font("Constantia", Font.PLAIN, 25));
		panel_3.add(lblNotcies);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		panel_4.setForeground(Color.WHITE);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
					.addGap(72)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 498, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(178, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(21)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
							.addGap(22)
							.addComponent(panel_files, GroupLayout.PREFERRED_SIZE, 676, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
							.addGap(26))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_files, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("res\\logopie.gif"));
		panel_4.add(lblNewLabel_3);
		
		JLabel lblNewLabel_1 = new JLabel("Sistema d'informaci\u00F3 territorial");
		lblNewLabel_1.setForeground(Color.GRAY);
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setBackground(Color.LIGHT_GRAY);
		lblNewLabel_1.setFont(new Font("Constantia", Font.PLAIN, 33));
		
		JLabel lblNewLabel_2 = new JLabel("SIG dels Serveis T\u00E8cnics");
		lblNewLabel_2.setForeground(Color.GRAY);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2.setFont(new Font("Constantia", Font.PLAIN, 25));
		panel_1.add(lblNewLabel_2);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("res\\logo2.gif"));
		panel.add(lblNewLabel);
		frame.getContentPane().setLayout(groupLayout);
	}



	public void drawChildren(ArrayList<Node> files) {
		panel_files.removeAll();
		panel_files.updateUI();
		System.out.println(files.size());
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
			label_file.setFont(new Font("Constantia", Font.PLAIN, 18));
			label_file.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					if (e.getClickCount() == 2) {
						System.out.println("Double click");
						drawChildren(file.getChildren());
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
}
