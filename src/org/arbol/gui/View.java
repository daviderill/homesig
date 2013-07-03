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
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import net.miginfocom.swing.MigLayout;

import org.arbol.domain.Node;
import org.arbol.util.Utils;

/**
 * Classe que mostra un explorador de fitxers, i una secció de notícies i una d'enllaços
 * @author Roger Erill Carrera
 *
 */
public class View extends JFrame{

	private static final long serialVersionUID = 6859743538922139519L;
	public JFrame frame;
	private JPanel panel_files;
	private JPanel panel_breadcrumb;
	private JPanel panel_news_content;
	private JLabel upperLogoLabel;
	
	private JEditorPane news1;
	private JEditorPane news2;
	private JEditorPane news3;
	private JEditorPane links1;
	private JEditorPane links2;
	
	private String iconPath = "res/ico_";
	private String upperLogoPath;
	private String selectedLabel;
	
	private MouseListener listener;
	private MouseListener breadcrumb_listener;
	private HyperlinkListener link_listener;
	
	private ArrayList<Node> currentFiles;
	private static final int LABEL_WIDTH = 150;
	private static final int LABEL_HEIGHT = 80;
	private static final int FONT_SIZE = 11;
	private static final Font FONT = new Font("Georgia", Font.PLAIN, FONT_SIZE);

	
	public View() {
		initialize();
		frame.setLocationRelativeTo(null);
		setLookAndFeel();
		createFakeLinks();
	}
	
	
	private void setLookAndFeel() {
		
		List<Image> icons  = new ArrayList<Image>();
		icons.add(new ImageIcon("res/icon256.png").getImage());
	    icons.add(new ImageIcon("res/icon48.png").getImage());
	    icons.add(new ImageIcon("res/icon32.png").getImage());
	    icons.add(new ImageIcon("res/icon24.png").getImage());
	    icons.add(new ImageIcon("res/icon16.png").getImage());
	    frame.setIconImages(icons);
		try	{
		    JFrame.setDefaultLookAndFeelDecorated(true);
		    JDialog.setDefaultLookAndFeelDecorated(true);
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    Utils.getLogger().warning(e.getMessage());
		}
		
	}

	private void createFakeLinks() {
		
	    String bodyRule = "body { font-family: " + FONT.getFamily() + "; " +
	            "font-size: " + FONT.getSize() + "pt; }";
		
		/*String text = "S'ha creat el mapa guia, accés a tota" +
				" la cartografia en un sòl mapa" 
				+ " <a href='QGIS_projectes\\1_Mapa_guia_v2.qgs'>Mapa guia</a>";

		((HTMLDocument)news1.getDocument()).getStyleSheet().addRule(bodyRule);
		news1.setContentType("text/html");
		news1.setText(text);
		news1.setEditable(false);  
		news1.setOpaque(false);*/
		
		String text = "<a href='http://www.santsadurni.cat'> Web de l'ajuntament </a>";

		((HTMLDocument)links1.getDocument()).getStyleSheet().addRule(bodyRule);
		links1.setContentType("text/html");
		links1.setText(text);
		links1.setEditable(false);  
		links1.setOpaque(false);
		
		((HTMLDocument)links2.getDocument()).getStyleSheet().addRule(bodyRule);
		links2.setText("<a href='http://oslo.geodata.es/stsadurnia/planejament.php'> Web de planejament </a>");
		links2.setOpaque(false);
		links2.setEditable(false);
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
		links1.addHyperlinkListener(linkListener);
		links2.addHyperlinkListener(linkListener);
	}
	
	public void drawNews(ArrayList<String> news) {
		if (news.size() > 0) {
			setNews(news1,news.get(0));
			if (news.size() > 1) {
				setNews(news2,news.get(1));
				if (news.size() > 2) {
					setNews(news2,news.get(2));
				}
			}
		}
	}
	
	private void setNews(JEditorPane newspanel, String text) {
		String bodyRule = "body { font-family: " + FONT.getFamily() + "; " +
	            "font-size: " + FONT.getSize() + "pt; }";
		((HTMLDocument)news1.getDocument()).getStyleSheet().addRule(bodyRule);
		newspanel.setContentType("text/html");
		newspanel.setEditable(false);  
		newspanel.setOpaque(false);
		newspanel.setText(text);
	}

	
	/**
	 * Donats uns nodes files, dibuixar-los en el panell panel_files
	 * @param files - Llista de nodes a dibuixar
	 */
	public void drawChildren(ArrayList<Node> files) {
		
		currentFiles = files;
		panel_files.removeAll();
		panel_files.updateUI();
		
		for (int i=0; i < files.size(); ++i) {
			
			final Node file = files.get(i);
			JLabel label_file = new JLabel(file.getName(), SwingConstants.CENTER);
			label_file.setHorizontalTextPosition(SwingConstants.CENTER);
			label_file.setVerticalTextPosition(JLabel.BOTTOM);
			label_file.setFont(FONT);
			
			String extension = file.getExtension_id();
			if (extension == null) {
				extension = "dir";		
			}
			String path = iconPath + extension + ".png";
			
			// Creem una icona per defecte, i si el path de la imatge existeix, l'apliquem
			ImageIcon icon = new ImageIcon(iconPath + "default.png");	
			if (new File(path).isFile()) {
				icon = new ImageIcon(path);
			}
			label_file.setIcon(icon);
			
			// Medim l'ample de la part escrita al label per saber si cal truncar-lo
			FontMetrics fontMetrics = label_file.getFontMetrics(label_file.getFont());
			int text_lenght = fontMetrics.stringWidth(label_file.getText());
			
			if (label_file.getText().equals(selectedLabel)) {
				Border border = BorderFactory.createLineBorder(Color.gray);
				label_file.setBorder(border);
				label_file.setBackground(new Color(200,200,200));
				label_file.setOpaque(true);
			}
			// Si és massa llarg, trunquem fent servir html, que trunca pel millor lloc
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
			if (i == drawPath.length - 1) {
				drawInBreadCrumb(drawPath[i],true);
			}
			else {
				drawInBreadCrumb(drawPath[i],false);
			}
		}
	}
	
	
	/**
	 * Dibuixar el string s en el breadcrumb
	 * @param s - String a pintar al breadcrumb
	 * @param bold - Si s'ha de pintar en negreta
	 */
	private void drawInBreadCrumb(String s, boolean bold) {
		
		JLabel bread = new JLabel(s, SwingConstants.LEFT);		
		if (s.equals("Inici ")) bread.setForeground(Color.blue);
		bread.addMouseListener(breadcrumb_listener);
		JLabel separator = new JLabel(" > ", SwingConstants.LEFT);
		if (bold) {
			bread.setFont(new Font("Georgia", Font.BOLD, FONT_SIZE));
			separator.setFont(new Font("Georgia", Font.BOLD, FONT_SIZE));
		}
		else {
			bread.setFont(FONT);
			separator.setFont(FONT);
		}
		panel_breadcrumb.add(bread);
		panel_breadcrumb.add(separator);
		
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
		panel_files.setBackground(new Color(245, 245, 245));
		panel_files.setAlignmentX(Component.LEFT_ALIGNMENT);
		JScrollPane editorScroll = new JScrollPane(panel_files);
		editorScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		panel_files.setPreferredSize(new Dimension(777,600));
		editorScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
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
		panel_info.setFont(new Font("Georgia", Font.PLAIN, FONT_SIZE-2));
		
		panel_news_content = new JPanel();
		panel_news_content.setBackground(Color.WHITE);
		
		JPanel panel_links_content = new JPanel();
		panel_links_content.setBackground(Color.WHITE);
		
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
										.addComponent(panel_news_content, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_news_title, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
									.addGap(18)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(editorScroll, GroupLayout.PREFERRED_SIZE, 781, Short.MAX_VALUE)
										.addComponent(panel_breadcrumb, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 781, Short.MAX_VALUE)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(panel_title, GroupLayout.PREFERRED_SIZE, 588, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)))))))
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
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel_news_title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_breadcrumb, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_links_title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_news_content, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
						.addComponent(panel_links_content, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(editorScroll, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(27)
					.addComponent(panel_info, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		links1 = new JEditorPane();
		links1.setContentType("text/html");
		
		links2 = new JEditorPane();
		links2.setContentType("text/html");
		GroupLayout gl_panel_links_content = new GroupLayout(panel_links_content);
		gl_panel_links_content.setHorizontalGroup(
			gl_panel_links_content.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_links_content.createSequentialGroup()
					.addGroup(gl_panel_links_content.createParallelGroup(Alignment.LEADING)
						.addComponent(links1, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
						.addComponent(links2, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(29, Short.MAX_VALUE))
		);
		gl_panel_links_content.setVerticalGroup(
			gl_panel_links_content.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_links_content.createSequentialGroup()
					.addContainerGap()
					.addComponent(links1, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(links2, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(316, Short.MAX_VALUE))
		);
		panel_links_content.setLayout(gl_panel_links_content);
		
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
				.addGroup(Alignment.LEADING, gl_panel_news_content.createSequentialGroup()
					.addGroup(gl_panel_news_content.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(news3, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(news2, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(news1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 187, Short.MAX_VALUE))
					.addContainerGap(155, Short.MAX_VALUE))
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
					.addContainerGap(124, Short.MAX_VALUE))
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
		panel_info.add(lblNewLabel_3, "cell 8 0,aligny top");
		
		JLabel lblNewLabel_9 = new JLabel("Disseny web: mm!");
		lblNewLabel_9.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNewLabel_9, "cell 10 0,aligny top");
		
		JLabel lblNewLabel_7 = new JLabel("Av\u00EDs legal");
		lblNewLabel_7.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblNewLabel_7, "flowx,cell 0 1");
		
		JLabel lblNewLabel_8 = new JLabel("Inici");
		lblNewLabel_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_8.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblNewLabel_8, "cell 2 1,alignx center");
		
		JLabel lblMapaWeb = new JLabel("Mapa web");
		lblMapaWeb.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblMapaWeb, "cell 4 1");
		panel_breadcrumb.setLayout(new MigLayout("", "[4px]", "[14px]"));
		panel_title.setLayout(new MigLayout("", "[450px]", "[41px][31px]"));
		
		JLabel lblNewLabel_1 = new JLabel("Sistema d'informaci\u00F3 territorial");
		lblNewLabel_1.setForeground(Color.GRAY);
		panel_title.add(lblNewLabel_1, "cell 0 0,alignx left,aligny top");
		lblNewLabel_1.setBackground(Color.LIGHT_GRAY);
		lblNewLabel_1.setFont(new Font("Georgia", Font.PLAIN, 35));
		
		JLabel lblNewLabel_2 = new JLabel("SIG dels Serveis T\u00E8cnics");
		lblNewLabel_2.setForeground(Color.GRAY);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2.setFont(new Font("Georgia", Font.PLAIN, 25));
		panel_title.add(lblNewLabel_2, "cell 0 1,alignx left,aligny top");
		
		upperLogoLabel = new JLabel("");
		//lblNewLabel.setIcon(new ImageIcon("res\\logo2.png"));
		panel_top_logo.add(upperLogoLabel);
		frame.getContentPane().setLayout(groupLayout);
		
	}

	
	public void showErrorFileNotFound(String path) {
		JOptionPane.showMessageDialog(this.frame, "El fitxer " + path + " no existeix", "Arbol", JOptionPane.WARNING_MESSAGE);
	}
	
	public void showErrorFileNotOpeneable(String path) {
		JOptionPane.showMessageDialog(this.frame, "El fitxer " + path + " no es pot obrir. És possible que no hi hagi cap aplicació configurada " +
				"per obrir aquest tipus de fitxer", "Arbol", JOptionPane.WARNING_MESSAGE);
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


	public void setUpperLogo(String path) {
		upperLogoPath = path;
		upperLogoLabel.setIcon(new ImageIcon(upperLogoPath));
	}
}