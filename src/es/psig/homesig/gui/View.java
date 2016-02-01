package es.psig.homesig.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import net.miginfocom.swing.MigLayout;
import es.psig.homesig.controller.Controller;
import es.psig.homesig.model.Links;
import es.psig.homesig.model.Node;
import es.psig.homesig.util.Utils;

/**
 * Classe que mostra un explorador de fitxers, una secció de notícies i una d'enllaços
 * @author Roger Erill Carrera
 *
 */
public class View extends JFrame {

	private JPanel panel_files;
	private JPanel panel_breadcrumb;
	private JPanel panel_news_content;
	private JPanel panel_news_title;
	private JPanel panel_links_content;
	private JPanel panel_links_title;
	private JPanel panel_title;
	private JPanel panel_top_logo;
	private JLabel upperLogoLabel;
	private JPanel panel_info;
	
	private JLabel lblEmailFixed;
	private JLabel lblEmail;
	private JLabel lblFaxFixed;
	private JLabel lblFax;
	private JLabel lblConsultorFixed;
	private JLabel lblConsultor;
	private JLabel lblDissenyFixed;
	private JLabel lblDisseny;
	private JLabel lblCredits;
	private JLabel lblNumberFixed;
	private JLabel lblNumber;
	private JLabel lblAdreca;
	private JLabel lblTitle2;
	private JLabel lblTitle3;
	private JLabel lblEnllacos;
	private JLabel lblNoticies;
	private JLabel lblVersion;
	
	private String iconPath = "res/ico_";
	private String titleIcon;
	private String upperLogoPath;
	private String selectedLabel;
	
	private MouseListener listener;
	private MouseListener breadcrumb_listener;
	private HyperlinkListener link_listener;
	private ArrayList<Node> currentFiles;
	
	private Color backMain;
	private Color backFilesColor;
	private Color backCreditsColor;
	private Color inici_color = Color.blue;
	private Color breadcrumb_color = Color.black;
	private Controller controller;
	
	private String defaultFontName = FONT_NAME;
	private int defaultFontSize = Font.PLAIN;
	private int defaultFontStyle = FONT_SIZE;
	private Color defaultFontColor = Color.BLACK;
	
	private String newsFontName;
	private int newsFontSize;
	private int newsFontStyle;
	private Color newsFontColor;
	private Font newsFont;
	
	private String filesFontName;
	private int filesFontSize;
	private int filesFontStyle;
	private Color filesFontColor = Color.BLACK;
	private Font filesFont;
	
	private String linksFontName;
	private int linksFontSize;
	private int linksFontStyle;
	private Color linksFontColor = Color.BLACK;
	private Font linksFont;
	
	private String creditsFontName;
	private int creditsFontSize;
	private int creditsFontStyle;
	private Color creditsFontColor = Color.BLACK;
	private Font creditsFont;
	
	private static final int LABEL_WIDTH = 150;
	private static final int LABEL_HEIGHT = 88;
	private static final String FONT_NAME = "Georgia";
	private static final int FONT_SIZE = 11;
	private static final String CURRENT_VERSION = "Version 1.2.128";
	
	
	public View() {
		initialize();
	}
	

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	
	private void setLookAndFeel() {
		
		List<Image> icons  = new ArrayList<Image>();
		// Si no troba la icona adient, posa la de java per default
		icons.add(new ImageIcon(titleIcon + "_128.png").getImage());
	    icons.add(new ImageIcon(titleIcon + "_64.png").getImage());
	    icons.add(new ImageIcon(titleIcon + "_32.png").getImage());
	    icons.add(new ImageIcon(titleIcon + "_16.png").getImage());
	    setIconImages(icons);
		try	{
		    JFrame.setDefaultLookAndFeelDecorated(true);
		    JDialog.setDefaultLookAndFeelDecorated(true);
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    Utils.getLogger().warning(e.getMessage());
		}
		
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
	}
	
	
	public void drawNews(ArrayList<String> news) {
		
		int index = 1;
		for (int i=0; i < news.size(); ++i) {
			createHtmlElement(index, news.get(i), panel_news_content, newsFont, newsFontColor);
			++index;
		}
		
	}
	
	
	public void drawLinks(ArrayList<Links> links) {
		
		int index = 1;
		for (int i=0; i < links.size(); ++i) {
			Links l = links.get(i);
			if (l.getImagesrc() != null) {
				JLabel image = new JLabel();
				ImageIcon icon = new ImageIcon(l.getImagesrc());
				image.setIcon(icon);
				String pos = String.valueOf(index);
				Object constraint = "cell 1 " + pos + ",grow";
				panel_links_content.add(image, constraint);
				++index;
			} 
			createHtmlElement(index, l.getHtmlcode(), panel_links_content, linksFont, linksFontColor);
			++index;
		}
		
	}
	
	
	private void createHtmlElement(int position, String htmltext, JPanel parent, Font font, Color foreColor) {

		String auxColor = "color: rgb("+foreColor.getRed()+","+foreColor.getGreen()+","+foreColor.getBlue()+")";
		JEditorPane element = new JEditorPane();
		String bodyRule = "body {font-family: "+font.getFamily()+"; " +
			"font-size: "+font.getSize()+"pt; "+auxColor+";}";
		element.setContentType("text/html");
		((HTMLDocument)element.getDocument()).getStyleSheet().addRule(bodyRule);
		element.setText(htmltext);
		element.setEditable(false);		
		element.addHyperlinkListener(link_listener);
		element.setBackground(backMain);
		String pos = String.valueOf(position);
		Object constraint = "cell 1 "+pos+",grow";
		parent.add(element, constraint);
		
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
			
			String extension = file.getExtension_id();
			if (extension == null || extension.trim().isEmpty()) {
				extension = "dir";		
			}
			String path = iconPath+extension+".png";
			
			// Creem una icona per defecte, i si el path de la imatge existeix, l'apliquem
			ImageIcon icon = new ImageIcon(iconPath+"default.png");	
			if (new File(path).isFile()) {
				icon = new ImageIcon(path);
			}
			label_file.setIcon(icon);
			
			// Medim l'ample de la part escrita al label per saber si cal truncar-lo
			FontMetrics fontMetrics = label_file.getFontMetrics(label_file.getFont());
			int text_lenght = fontMetrics.stringWidth(label_file.getText());
			
			label_file.setFont(filesFont);
			label_file.setForeground(filesFontColor);
			if (label_file.getText().equals(selectedLabel)) {
				label_file.setBackground(backFilesColor);
				label_file.setOpaque(true);
			}
			
			// Si és massa llarg, trunquem fent servir html, que trunca pel millor lloc
			int additional_lines;
			if (text_lenght < LABEL_WIDTH) additional_lines = 0;
			else additional_lines = ((text_lenght - LABEL_WIDTH) / LABEL_WIDTH) + 1;
			if (additional_lines == 0) {
				label_file.setText("<html><center>"+label_file.getText()+"</center><br><br></html>");
			}
			else if (additional_lines == 1) {
				label_file.setText("<html><center>"+label_file.getText()+"</center><br></html>");
			}
			else {
				label_file.setText("<html><center>"+label_file.getText()+"</center></html>");
			}
			label_file.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT+fontMetrics.getHeight()*additional_lines));
		
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
		bread.setForeground(breadcrumb_color);
		if (s.equals("Inici ")) bread.setForeground(inici_color);
		bread.addMouseListener(breadcrumb_listener);
		JLabel separator = new JLabel(" > ", SwingConstants.LEFT);
		separator.setForeground(breadcrumb_color);
		if (bold) {
			Font aux = filesFont.deriveFont(Font.BOLD);
			bread.setFont(aux);
			separator.setFont(aux);
		}
		else {
			bread.setFont(filesFont);
			separator.setFont(filesFont);
		}
		panel_breadcrumb.add(bread);
		panel_breadcrumb.add(separator);
		
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setBounds(0, 0, 1267, 700);
		getContentPane().setBackground(new Color(255, 255, 255));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		
		panel_top_logo = new JPanel();
		panel_top_logo.setBackground(new Color(255, 255, 255));
		
		FlowLayout fl_panel_files = new FlowLayout(FlowLayout.LEFT);
		panel_files = new JPanel(fl_panel_files);
		panel_files.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel_files.setBackground(new Color(245, 245, 245));
		panel_files.setAlignmentX(Component.LEFT_ALIGNMENT);
		JScrollPane editorScroll = new JScrollPane(panel_files);
		editorScroll.setBorder(BorderFactory.createEmptyBorder());
		editorScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		panel_files.setPreferredSize(new Dimension(777, 400));
		editorScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel_title = new JPanel();
		
		panel_links_title = new JPanel();
		
		lblEnllacos = new JLabel("Enlla\u00E7os");
		lblEnllacos.setHorizontalAlignment(SwingConstants.LEFT);
		lblEnllacos.setFont(new Font("Georgia", Font.PLAIN, 25));
		panel_links_title.add(lblEnllacos);
		
		panel_news_title = new JPanel();
		
		lblNoticies = new JLabel("Not\u00EDcies");
		lblNoticies.setHorizontalAlignment(SwingConstants.LEFT);
		lblNoticies.setFont(new Font("Georgia", Font.PLAIN, 25));
		panel_news_title.add(lblNoticies);
		
		panel_info = new JPanel();
		panel_info.setFont(new Font("Georgia", Font.PLAIN, FONT_SIZE-2));
		
		panel_news_content = new JPanel();
		panel_news_content.setBackground(Color.WHITE);
		
		panel_links_content = new JPanel();
		panel_links_content.setBackground(Color.WHITE);
		
		panel_breadcrumb = new JPanel();
		panel_breadcrumb.setBorder(null);
		panel_breadcrumb.setBackground(SystemColor.controlHighlight);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
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
								.addComponent(panel_info, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_news_content, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_news_title, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
									.addGap(18)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(editorScroll, GroupLayout.PREFERRED_SIZE, 800, Short.MAX_VALUE)
										.addComponent(panel_breadcrumb, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 800, Short.MAX_VALUE)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(panel_title, GroupLayout.PREFERRED_SIZE, 588, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)))))))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_links_title, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_links_content, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_top_logo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_title, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(panel_news_title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(panel_breadcrumb, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_links_title, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_news_content, GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_links_content, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
								.addComponent(editorScroll, GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(27)
					.addComponent(panel_info, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panel_news_content.setLayout(new MigLayout("", "[3px][165px][3px]", "[5px][50px][50px][50px][50px][50px][50px]"));
		panel_links_content.setLayout(new MigLayout("", "[3px][165px][3px]", "[5px][50px][50px][50px][50px][50px][50px]"));
		panel_info.setLayout(new MigLayout("", "[55px][5px][40px][5px][][8px][][][8px][][][8px][][][8px][][][8px][][]", "[14px][]"));
		
		lblAdreca = new JLabel("Pla\u00E7a Ajuntament, 1  08770 Sant Sadurn\u00ED d'Anoia");
		lblAdreca.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblAdreca, "cell 0 0 5 1,alignx left,aligny top");
		
		lblNumberFixed = new JLabel("Tel: ");
		lblNumberFixed.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNumberFixed, "cell 6 0");
		
		lblNumber = new JLabel("NUMBER");
		lblNumber.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblNumber, "cell 7 0");
		
		lblFaxFixed = new JLabel("Fax: ");
		lblFaxFixed.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblFaxFixed, "cell 9 0");
		
		lblFax = new JLabel("FAX_NUMBER");
		lblFax.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblFax, "cell 10 0");
		
		lblEmailFixed = new JLabel("E-mail: ");
		lblEmailFixed.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblEmailFixed, "cell 12 0");
		
		lblEmail = new JLabel("EMAIL");
		lblEmail.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblEmail, "cell 13 0");
		
		lblConsultorFixed = new JLabel("Consultor SIG: ");
		lblConsultorFixed.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblConsultorFixed, "cell 15 0");
		
		lblConsultor = new JLabel("CONSULTOR");
		lblConsultor.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblConsultor, "cell 16 0");
		
		lblDissenyFixed = new JLabel("Disseny web: ");
		lblDissenyFixed.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblDissenyFixed, "cell 18 0");
		
		lblDisseny = new JLabel("DISSENY_WEB");
		lblDisseny.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblDisseny, "cell 19 0");
		
		lblCredits = new JLabel("Cr\u00E8dits");
		lblCredits.setFont(new Font("Georgia", Font.BOLD, 10));
		panel_info.add(lblCredits, "flowx,cell 0 1 17 1");
		
		lblVersion = new JLabel(CURRENT_VERSION);
		lblVersion.setFont(new Font("Georgia", Font.PLAIN, 10));
		panel_info.add(lblVersion, "cell 18 1 2 1,alignx right");
		
		panel_breadcrumb.setLayout(new MigLayout("", "[4px]", "[14px]"));
		panel_title.setLayout(new MigLayout("", "[450px]", "[41px][31px]"));
		
		lblTitle2 = new JLabel("TITLE2");
		lblTitle2.setForeground(Color.GRAY);
		panel_title.add(lblTitle2, "cell 0 0,alignx left,aligny top");
		lblTitle2.setBackground(Color.LIGHT_GRAY);
		lblTitle2.setFont(new Font("Georgia", Font.PLAIN, 35));
		
		lblTitle3 = new JLabel("TITLE3");
		lblTitle3.setForeground(Color.GRAY);
		lblTitle3.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle3.setFont(new Font("Georgia", Font.PLAIN, 25));
		panel_title.add(lblTitle3, "cell 0 1,alignx left,aligny top");
		
		upperLogoLabel = new JLabel("");
		panel_top_logo.add(upperLogoLabel);
		getContentPane().setLayout(groupLayout);
		
		this.addWindowListener(new WindowAdapter() {
			 @Override
			 public void windowClosing(WindowEvent e) {
			     closeApp();
			 }
		 });	
		
	}

	
	public void closeApp() {
		controller.insertLog("Application closed");
	}
	
	
	public void showErrorFileNotFound(String path) {
		JOptionPane.showMessageDialog(this, "El fitxer " + path + " no existeix", "Homesig", JOptionPane.WARNING_MESSAGE);
	}
	
	public void showErrorFileNotOpeneable(String path) {
		JOptionPane.showMessageDialog(this, "El fitxer " + path + " no es pot obrir. és possible que no hi hagi cap aplicació configurada " +
				"per obrir aquest tipus de fitxer", "Homesig", JOptionPane.WARNING_MESSAGE);
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

	public void setTitleIcon(String path) {
		titleIcon = path;
		setLookAndFeel();
	}
	
	public void setUpperLogo(String path) {
		upperLogoPath = path;
		upperLogoLabel.setIcon(new ImageIcon(upperLogoPath));
	}

	public void setBackgroundColor(Color background) {
		getContentPane().setBackground(background);
		panel_top_logo.setBackground(background);
	}

	public void setBackFillAriadnaColor(Color c) {
		panel_breadcrumb.setBackground(c);
	}
	
	public void setBackTextBoxColor(Color c) {
		panel_news_title.setBackground(c);
		panel_links_title.setBackground(c);
		panel_title.setBackground(c);
	}
	
	public void setBackMain(Color c) {
		backMain = c;
		panel_news_content.setBackground(c);
		panel_files.setBackground(c);
		panel_links_content.setBackground(c);
	}
	
	public void setBackFilesColor(Color c) {
		backFilesColor = c;
	}
	
	public void setBackCreditsColor(Color c) {
		backCreditsColor = c;
	}
	
	public void setIniciFontColor(Color c) {
		inici_color = c;
	}
	
	public void setBreadcrumbFontColor(Color c) {
		breadcrumb_color = c;
	}
	
	public void setTitlesColor(Color c) {
		lblNoticies.setForeground(c);
		lblEnllacos.setForeground(c);
		lblTitle2.setForeground(c);
		lblTitle3.setForeground(c);
	}
	
	public void setTitle2(String s) {
		lblTitle2.setText(s);
	}
	
	public void setWindowTitle(String s) {
		setTitle(s);
	}
	
	public void setTitle3(String s) {
		lblTitle3.setText(s);
	}
	
	public void setShowPanelInfo(int value) {
		if (value == 0) {
			panel_info.setVisible(false);
		}
	}
	
	
	// Crèdits
	public void setAddress(String s) {
		lblAdreca.setText(s);
	}
	
	public void setTelephone(String telephone) {
		lblNumber.setText(telephone);
	}
	
	public void setFax(String s) {
		lblFax.setText(s);
	}
	
	public void setEmail(String s) {
		lblEmail.setText(s);
	}
	
	public void setConsultor(String s) {
		lblConsultor.setText(s);
	}
	
	public void setWebDesign(String s) {
		lblDisseny.setText(s);
	}
	
	public void setCredits(String s) {
		lblCredits.setText(s);
	}
	
	public void setVersion(String s) {
		if (!s.equals("")) {
			lblVersion.setText(s);
		}
	}
	
	
	public void setDefaultFontName(String name) {
		if (!name.equals("-1")) {
			defaultFontName = name;
		}
	}
	
	public void setDefaultFontSize(int size) {
		if (size != -1) {
			defaultFontSize = size;
		}		
	}
	
	public void setDefaultFontStyle(int style) {
		if (style != -1) {
			defaultFontStyle = style;
		}
	}
	
	public void setDefaultFontColor(Color color) {
		if (color != null) {
			defaultFontColor = color;
		}
	}
	
	
	public void setNewsFontName(String name) {
		newsFontName = defaultFontName;
		if (!name.equals("-1")) {
			newsFontName = name;
		}
	}
	
	public void setNewsFontSize(int size) {
		newsFontSize = defaultFontSize;
		if (size != -1) {
			newsFontSize = size;
		}
	}
	
	public void setNewsFontStyle(int style) {
		newsFontStyle = defaultFontStyle;
		if (style != -1) {
			newsFontStyle = style;
		}
	}
	
	public void setNewsFontColor(Color color) {
		newsFontColor = defaultFontColor;
		if (color != null) {
			newsFontColor = color;
		}
	}
	
	public void setNewsFont() {
		newsFont = new Font(newsFontName, newsFontStyle, newsFontSize);
	}
	
	
	public void setFilesFontName(String name) {
		filesFontName = defaultFontName;
		if (!name.equals("-1")) {
			filesFontName = name;
		}
	}
	
	public void setFilesFontSize(int size) {
		filesFontSize = defaultFontSize;
		if (size != -1) {
			filesFontSize = size;
		}
	}
	
	public void setFilesFontStyle(int style) {
		filesFontStyle = defaultFontStyle;
		if (style != -1) {
			filesFontStyle = style;
		}
	}
	
	public void setFilesFontColor(Color color) {
		filesFontColor = defaultFontColor;
		if (color != null) {
			filesFontColor = color;
		}
	}
	
	public void setFilesFont() {
		filesFont = new Font(filesFontName, filesFontStyle, filesFontSize);
	}
	
	
	public void setLinksFontName(String name) {
		linksFontName = defaultFontName;
		if (!name.equals("-1")) {
			linksFontName = name;
		}
	}
	
	public void setLinksFontSize(int size) {
		linksFontSize = defaultFontSize;
		if (size != -1) {
			linksFontSize = size;
		}
	}
	
	public void setLinksFontStyle(int style) {
		linksFontStyle = defaultFontStyle;
		if (style != -1) {
			linksFontStyle = style;
		}
	}
	
	public void setLinksFontColor(Color color) {
		linksFontColor = defaultFontColor;
		if (color != null) {
			linksFontColor = color;
		}
	}
	
	public void setLinksFont() {
		linksFont = new Font(linksFontName, linksFontStyle, linksFontSize);
	}
	
	
	public void setCreditsFontName(String name) {
		creditsFontName = defaultFontName;
		if (!name.equals("-1")) {
			creditsFontName = name;
		}
	}
	
	public void setCreditsFontSize(int size) {
		creditsFontSize = defaultFontSize;
		if (size != -1) {
			creditsFontSize = size;
		}
	}
	
	public void setCreditsFontStyle(int style) {
		creditsFontStyle = defaultFontStyle;
		if (style != -1) {
			creditsFontStyle = style;
		}
	}
	
	public void setCreditsFontColor(Color color) {
		creditsFontColor = defaultFontColor;
		if (color != null) {
			creditsFontColor = color;
		}
	}
	
	public void setCreditsFont() {
		
		creditsFont = new Font(creditsFontName, creditsFontStyle, creditsFontSize);
		
		lblAdreca.setFont(creditsFont);
		lblNumberFixed.setFont(creditsFont);
		lblNumber.setFont(creditsFont);
		lblFaxFixed.setFont(creditsFont);
		lblFax.setFont(creditsFont);
		lblEmailFixed.setFont(creditsFont);
		lblEmail.setFont(creditsFont);
		lblConsultorFixed.setFont(creditsFont);
		lblConsultor.setFont(creditsFont);
		lblDissenyFixed.setFont(creditsFont);
		lblDisseny.setFont(creditsFont);
		lblCredits.setFont(creditsFont);
		lblVersion.setFont(creditsFont);
		
		lblAdreca.setForeground(creditsFontColor);
		lblNumberFixed.setForeground(creditsFontColor);
		lblNumber.setForeground(creditsFontColor);
		lblFaxFixed.setForeground(creditsFontColor);
		lblFax.setForeground(creditsFontColor);
		lblEmailFixed.setForeground(creditsFontColor);
		lblEmail.setForeground(creditsFontColor);
		lblConsultorFixed.setForeground(creditsFontColor);
		lblConsultor.setForeground(creditsFontColor);
		lblDissenyFixed.setForeground(creditsFontColor);
		lblDisseny.setForeground(creditsFontColor);
		lblCredits.setForeground(creditsFontColor);
		lblVersion.setForeground(creditsFontColor);

		panel_info.setBackground(backCreditsColor);
		
	}

	
}