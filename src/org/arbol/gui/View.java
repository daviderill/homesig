package org.arbol.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import net.miginfocom.swing.MigLayout;

import org.arbol.domain.Node;

public class View extends JFrame{

	public JFrame frame;
	private JPanel panel_files;
	private JPanel panel_breadcrumb;
	private JPanel panel_news_content;
	private JEditorPane news1;
	private JEditorPane news2;
	private JEditorPane news3;
	private String iconPath = "res\\ico_";
	private MouseListener listener;
	private MouseListener breadcrumb_listener;
	private HyperlinkListener link_listener;
	private String selectedLabel;
	private ArrayList<Node> currentFiles;
	private static final int LABEL_WIDTH = 180;
	private static final int LABEL_HEIGHT = 90;

	public View() {
		initialize();
		setLookAndFeel();
		createFakeNews();
	}
	
	private void setLookAndFeel() {
		List<Image> icons  = new ArrayList<Image>();
		icons.add(new ImageIcon("res\\icon256.png").getImage());
	    icons.add(new ImageIcon("res\\icon48.png").getImage());
	    icons.add(new ImageIcon("res\\icon32.png").getImage());
	    icons.add(new ImageIcon("res\\icon24.png").getImage());
	    icons.add(new ImageIcon("res\\icon16.png").getImage());
	    frame.setIconImages(icons);
		try
		{
		    JFrame.setDefaultLookAndFeelDecorated(true);
		    JDialog.setDefaultLookAndFeelDecorated(true);
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
	}

	private void createFakeNews() {
		
		Font font = new Font("Georgia", Font.PLAIN, 13);
	    String bodyRule = "body { font-family: " + font.getFamily() + "; " +
	            "font-size: " + font.getSize() + "pt; }";
		
		String text = "Aquí tenim una notícia de prova amb un " +
				"enllaç al fitxer <a href='doc\\cadastre.pdf'> Cadastre ";

		((HTMLDocument)news1.getDocument()).getStyleSheet().addRule(bodyRule);
		news1.setContentType("text/html");
		news1.setText(text);
		news1.setEditable(false);  
		news1.setOpaque(false);
		
		((HTMLDocument)news2.getDocument()).getStyleSheet().addRule(bodyRule);
		text = "Aquí tenim un text de prova amb un " +
				"enllaç al directori <a href='Documentació'> Documentació ";
		news2.setContentType("text/html");
		news2.setText(text);
		news2.setEditable(false);  
		news2.setOpaque(false);
		
		
		
	    ((HTMLDocument)news3.getDocument()).getStyleSheet().addRule(bodyRule);
		text = "Aquesta és una notícia sense cap enllaç però que mostra <b>negreta</b> i <i>cursiva</i> per " +
				"ressaltar paraules";
		news3.setContentType("text/html");
		news3.setText(text);
		news3.setEditable(false);  
		news3.setOpaque(false);
		
		
	}
	
	public void addFileListener(MouseListener listenForFileClick){
		listener = listenForFileClick;
		panel_files.addMouseListener(listener);
	}

	public void addBreadcrumbListener(MouseListener listenForFileClick){
		breadcrumb_listener = listenForFileClick;
	}
	
	public void addLinkListener(HyperlinkListener linkListener) {
		link_listener = linkListener;
		news1.addHyperlinkListener(link_listener);
		news2.addHyperlinkListener(link_listener); 
		news3.addHyperlinkListener(link_listener); 
	}
	
	public void drawChildren(ArrayList<Node> files) {
		currentFiles = files;
		panel_files.removeAll();
		panel_files.updateUI();
		for (int i=0; i < files.size(); ++i) {
			final Node file = files.get(i);
			JLabel label_file = new JLabel(file.getName(),SwingConstants.CENTER);
			label_file.setHorizontalTextPosition(SwingConstants.CENTER);
			label_file.setVerticalTextPosition(JLabel.BOTTOM);
			Font f = new Font("Georgia", Font.PLAIN, 13);
			label_file.setFont(f);
			
			String extension = file.getExtension_id();
			if (extension == null) extension = "dir";		
			String path = iconPath + extension;
			if (label_file.getText().equals(selectedLabel)) {
				path += "_sel";
			}
			path += ".png";
			ImageIcon icon = new ImageIcon(iconPath + "default.png");	
			if (new File(path).isFile()) {
				icon = new ImageIcon(path);
			}
			label_file.setIcon(icon);

			FontMetrics fontMetrics = label_file.getFontMetrics(label_file.getFont());
			int text_lenght = fontMetrics.stringWidth(label_file.getText());
		
			if (label_file.getText().equals(selectedLabel)) {
				Border border = BorderFactory.createLineBorder(Color.gray);
				label_file.setBorder(border);
				label_file.setBackground(new Color(200,200,200));
				label_file.setOpaque(true);
			}
			if (text_lenght > LABEL_WIDTH) {
				int additional_lines = ((text_lenght - LABEL_WIDTH) / LABEL_WIDTH) + 1;
				label_file.setText("<html><center>" + label_file.getText() + "</center></html>");
				label_file.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT+fontMetrics.getHeight()*additional_lines));
			}
			else {
				label_file.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
			}
			
			label_file.addMouseListener(listener);

			if (file.getTooltip() != null) {
				label_file.setToolTipText(file.getTooltip());
			}

			panel_files.add(label_file);
		}
		
	}
	
	public void drawBreadcrumb(String[] drawPath) {
		panel_breadcrumb.removeAll();
		panel_breadcrumb.updateUI();
		for (int i=0; i < drawPath.length; ++i) {
			JLabel bread = new JLabel(drawPath[i], SwingConstants.LEFT);	
			bread.addMouseListener(breadcrumb_listener);
			bread.setFont(new Font("Georgia", Font.PLAIN, 12));
			JLabel separator = new JLabel(" > ", SwingConstants.LEFT);
			separator.setFont(new Font("Georgia", Font.PLAIN, 12));
			if (i == drawPath.length - 1) {
				bread.setFont(new Font("Georgia", Font.BOLD, 12));
				separator.setFont(new Font("Georgia", Font.BOLD, 12));
			}
			panel_breadcrumb.add(bread);
			panel_breadcrumb.add(separator);
		}
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Roger\\workspace\\arbol2\\res\\pdf-petit-seleccionat.png"));
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
		
		JPanel panel_info = new JPanel();
		panel_info.setFont(new Font("Gentium Book Basic", Font.PLAIN, 10));
		
		panel_news_content = new JPanel();
		panel_news_content.setBackground(Color.WHITE);
		
		JPanel panel_links_content = new JPanel();
		
		panel_breadcrumb = new JPanel();
		panel_breadcrumb.setBorder(null);
		panel_breadcrumb.setBackground(SystemColor.controlHighlight);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_top_logo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(21)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_info, GroupLayout.PREFERRED_SIZE, 899, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_news_title, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_news_content, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
									.addGap(30)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_breadcrumb, GroupLayout.PREFERRED_SIZE, 458, Short.MAX_VALUE)
										.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
											.addComponent(panel_title, GroupLayout.PREFERRED_SIZE, 576, GroupLayout.PREFERRED_SIZE)
											.addComponent(panel_files, GroupLayout.PREFERRED_SIZE, 751, Short.MAX_VALUE)))))))
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
						.addComponent(panel_news_content, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
						.addComponent(panel_links_content, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
						.addComponent(panel_files, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
					.addGap(67)
					.addComponent(panel_info, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		news1 = new JEditorPane();
		news1.setContentType("text/html");
		
		news2 = new JEditorPane();
		news2.setText("Aqu\u00ED tenim una not\u00EDcia de prova amb un enlla\u00E7 al fitxer <a href='doc\\cadastre.pdf'> Cadastre ");
		news2.setOpaque(false);
		news2.setFont(new Font("Georgia", Font.PLAIN, 13));
		news2.setEditable(false);
		news2.setContentType("text/html");
		
		news3 = new JEditorPane();
		news3.setText("Aqu\u00ED tenim una not\u00EDcia de prova amb un enlla\u00E7 al fitxer <a href='doc\\cadastre.pdf'> Cadastre ");
		news3.setOpaque(false);
		news3.setFont(new Font("Georgia", Font.PLAIN, 13));
		news3.setEditable(false);
		news3.setContentType("text/html");
		GroupLayout gl_panel_news_content = new GroupLayout(panel_news_content);
		gl_panel_news_content.setHorizontalGroup(
			gl_panel_news_content.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_news_content.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_news_content.createParallelGroup(Alignment.LEADING)
						.addComponent(news1, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
						.addComponent(news2, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
						.addComponent(news3, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel_news_content.setVerticalGroup(
			gl_panel_news_content.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_news_content.createSequentialGroup()
					.addContainerGap()
					.addComponent(news1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(news2, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(news3, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(64, Short.MAX_VALUE))
		);
		panel_news_content.setLayout(gl_panel_news_content);
		panel_info.setLayout(new MigLayout("", "[55px][5px][40px][5px][30px][8px][90px][6px][46px][6px][]", "[14px][]"));
		
		JLabel lblNewLabel_4 = new JLabel("Pla\u00E7a Ajuntament, 1  08770 Sant Sadurn\u00ED d'Anoia");
		lblNewLabel_4.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNewLabel_4, "cell 0 0 5 1,alignx left,aligny top");
		
		JLabel lblNewLabel_5 = new JLabel("Tel +34 938 910 325   Fax +34 938 183 470  E-mail: ajuntament@santsadurni.cat");
		lblNewLabel_5.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNewLabel_5, "cell 6 0,alignx left,aligny top");
		
		JLabel lblNewLabel_3 = new JLabel("Consultor SIG: Carlos L\u00F3pez");
		lblNewLabel_3.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNewLabel_3, "cell 8 0");
		
		JLabel lblNewLabel_9 = new JLabel("Disseny web: mm!");
		lblNewLabel_9.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNewLabel_9, "cell 10 0");
		
		JLabel lblNewLabel_7 = new JLabel("Av\u00EDs legal");
		lblNewLabel_7.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblNewLabel_7, "flowx,cell 0 1");
		
		JLabel lblNewLabel_8 = new JLabel("Inici");
		lblNewLabel_8.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblNewLabel_8, "cell 2 1");
		
		JLabel lblMapaWeb = new JLabel("Mapa web");
		lblMapaWeb.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblMapaWeb, "cell 4 1");
		panel_breadcrumb.setLayout(new MigLayout("", "[4px]", "[14px]"));
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

	public void showErrorFileNotFound(String path) {
		JOptionPane.showMessageDialog(this.frame, "El fitxer " + path + " no existeix", "Error", JOptionPane.ERROR_MESSAGE);
	}
	public void paintComponent(JLabel label) {
		for (Component c : panel_files.getComponents()) {
			if (c.equals(label)) {
				selectedLabel = label.getText();
				drawChildren(currentFiles);
			}
		}
	}

	public void setSelectedLabel(String text) {
		selectedLabel = text;
	}
	
}
