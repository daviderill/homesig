package es.psig.homesig.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.JOptionPane;


public class Utils {

	private static final ResourceBundle BUNDLE_TEXT = ResourceBundle.getBundle("text");
    private static Logger logger;
    private static Logger mapLogger;
    private static String username;
	private static String hostname;
    private static String hostaddress;
    private static final String LOG_FOLDER = "log/";

    
    public static Logger getLogger() {

    	if (logger == null) {
        	String logFile = createPath("log_");
            logger = Logger.getLogger(logFile);
            logger.addHandler(createHandler(logFile));
    		// Get user name and machine
            username = System.getProperty("user.name");
            try {
				hostname = InetAddress.getLocalHost().getHostName();
				hostaddress = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				logger.warning(e.getMessage());
			}
        }
        return logger;

    }
    
    
    public static Logger getMapLogger() {

    	if (mapLogger == null) {
    		String logFile = createPath("maplog_");
            mapLogger = Logger.getLogger(logFile);
            mapLogger.addHandler(createHandler(logFile));
        }
        return mapLogger;

    }
    
    
    public static String getUsername() {
		return username;
	}

	public static String getHostname() {
		return hostname;
	}

	public static String getHostaddress() {
		return hostaddress;
	}
    
    
    public static String createPath(String prefix) {
        
    	String folderRoot = Utils.getAppPath();                	
        String logFolder = folderRoot + LOG_FOLDER;
        File folderFile = new File(logFolder);
        folderFile.mkdirs();
        if (!folderFile.exists()) {
        	JOptionPane.showMessageDialog(null, "No s'ha pogut crear la carpeta de log", "Log creation", JOptionPane.ERROR_MESSAGE);                	
        }
        String logFile = logFolder + prefix + getCurrentTimeStamp() + ".log";
        
        return logFile;
        
    }
    
    
    public static FileHandler createHandler(String path) {
    	
    	FileHandler fh = null;
		try {
			fh = new FileHandler(path, true);
			LogFormatter lf = new LogFormatter();
	        fh.setFormatter(lf);
	        return fh;
		} catch (SecurityException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error al crear el fitxer de log, excepció de seguretat", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error al crear el fitxer de log", JOptionPane.ERROR_MESSAGE);
		}
        return null;
        
    }
    
    
    public static String getAppPath(){
    	
    	CodeSource codeSource = Utils.class.getProtectionDomain().getCodeSource();
    	File jarFile;
    	String appPath = "";
    	try {
    		jarFile = new File(codeSource.getLocation().toURI().getPath());
    	   	appPath = jarFile.getParentFile().getPath() + File.separator;  
    	}
    	catch (URISyntaxException e) {
    		e.printStackTrace();
    	}
    	return appPath;
    	
    }    
    
    
	public static ResourceBundle getBundleText() {
    	return BUNDLE_TEXT;
	}    
    
	
	public static String getBundleString(ResourceBundle bundle, String key){
		try{
			return bundle.getString(key);
		} catch (Exception e){
			return key;	
		}
	}
	
	
	
    public static String getCurrentTimeStamp() {
        return getCurrentTimeStamp("yyyyMMdd");
    }
	
    public static String getCurrentTimeStamp(String format) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(format);
        Date now = new Date();
        String date = sdfDate.format(now);
        return date;
    }


    public static String dateToString(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        String parsedDate = sdfDate.format(date);
        return parsedDate;
    }


    public static void copyFile(String srFile, String dtFile) {

        try {

            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            // For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    
    // Logger functions
    public static void logInfo(String msg) {
    	logInfo(msg, null);		
    }    
    
    public static void logInfo(String msg, String param) {
    	
    	if (logger == null) getLogger();
    	String aux = username+"@"+hostname+"("+hostaddress+")\n" + msg;
    	if (param != null) {
    		aux+= "\n"+param;
    	}
		logger.info(aux);
		
    }    
    

    public static void showMessage(String msg, String param, String title) {
    	
    	if (logger == null) getLogger();
    	try {
    		JOptionPane.showMessageDialog(null, BUNDLE_TEXT.getString(msg) + "\n" + param,
        		BUNDLE_TEXT.getString(title), JOptionPane.PLAIN_MESSAGE);
    		logger.info(BUNDLE_TEXT.getString(msg) + "\n" + param);
    	} catch (MissingResourceException e){
    		JOptionPane.showMessageDialog(null, msg + "\n" + param,	title, JOptionPane.PLAIN_MESSAGE);
    		logger.info(msg + "\n" + param); 		
    	}
    	
    }    

    
    public static void showError(String msg, String param) {
    	showError(msg, param, BUNDLE_TEXT.getString("title"));  
    }
    
    
    public static void showError(String msg, String param, String title) {
    	
    	if (logger == null) getLogger();
    	try {
    		JOptionPane.showMessageDialog(null, BUNDLE_TEXT.getString(msg) + "\n" + param,
    			BUNDLE_TEXT.getString(title), JOptionPane.WARNING_MESSAGE);
    		logger.warning(BUNDLE_TEXT.getString(msg) + "\n" + param);
    	} catch (MissingResourceException e){
    		JOptionPane.showMessageDialog(null, msg + "\n" + param,	title, JOptionPane.WARNING_MESSAGE);
    		logger.warning(msg + "\n" + param);		
    	}      
    	
    }

    
    public static void showError(Exception e) {
    	
    	if (logger == null) getLogger();
    	String errorInfo = getErrorInfo();
    	try {
    		JOptionPane.showMessageDialog(null, e.getMessage(), BUNDLE_TEXT.getString("title"), JOptionPane.WARNING_MESSAGE);
    		logger.warning(e.toString() + "\n" + errorInfo);
    	} catch (MissingResourceException e1){
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Error information", JOptionPane.WARNING_MESSAGE);
    		logger.warning(e.toString() + "\n" + errorInfo); 		
    	}   
    	
    }    
    
    
    public static void showError(Exception e, String param) {
    	
    	if (logger == null) getLogger();
    	String errorInfo = getErrorInfo();
    	try{
    		JOptionPane.showMessageDialog(null, e.getMessage(), BUNDLE_TEXT.getString("title"), JOptionPane.WARNING_MESSAGE);
    		logger.warning(e.toString() + "\n" + errorInfo + "\n" + param);
    	} catch (MissingResourceException e1){
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Error information", JOptionPane.WARNING_MESSAGE);
    		logger.warning(e.toString() + "\n" + errorInfo + "\n" + param); 		
    	}        
    	
    }     
    
    
    public static void logError(Exception e, String param) {
    	if (logger == null) getLogger();
    	String errorInfo = getErrorInfo();
		logger.warning(e.toString()+"\n"+errorInfo+"\n"+param);
    }         
    
    
    private static String getErrorInfo(){
    	StackTraceElement[] ste = Thread.currentThread().getStackTrace();
    	String aux = ste[3].toString();
    	return aux;
    }
    
    
    public static int confirmDialog(String msg, String title) {
    	
    	if (logger == null) getLogger();
    	int reply;
    	try {
	    	reply = JOptionPane.showConfirmDialog(null, BUNDLE_TEXT.getString(msg),
	    		BUNDLE_TEXT.getString(title), JOptionPane.YES_NO_OPTION);
	        logger.warning(BUNDLE_TEXT.getString(msg));
    	} catch (MissingResourceException e){
	    	reply = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
    		logger.info(msg); 		
    	}
        return reply;    	
        
    }        

    
}