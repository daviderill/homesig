package es.psig.homesig.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;


public class Utils {

	private static final ResourceBundle BUNDLE_TEXT = ResourceBundle.getBundle("text"); //$NON-NLS-1$
    private static Logger logger;
    private static Logger mapLogger;
    private static final String LOG_FOLDER = "log/";

    
    public static Logger getLogger() {

    	if (logger == null) {
        	String logFile = createPath("log_");
            logger = Logger.getLogger(logFile);
            logger.addHandler(createHandler(logFile));
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
    
    public static String createPath(String prefix) {
    	String folderRoot = Utils.getAppPath();                	
        String folder = folderRoot + LOG_FOLDER;
        String logFile = folder + prefix + getCurrentTimeStamp() + ".log";
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
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
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


    public static void showMessage(String msg, String param, String title) {
    	try{
    		JOptionPane.showMessageDialog(null, BUNDLE_TEXT.getString(msg) + "\n" + param,
        		BUNDLE_TEXT.getString(title), JOptionPane.PLAIN_MESSAGE);
    		if (logger != null) {
    			logger.info(BUNDLE_TEXT.getString(msg) + "\n" + param);
    		}
    	} catch (MissingResourceException e){
    		JOptionPane.showMessageDialog(null, msg + "\n" + param,	title, JOptionPane.PLAIN_MESSAGE);
    		if (logger != null) {
    			logger.info(msg + "\n" + param);
    		}    		
    	}
    }    

    
    public static void showError(String msg, String param, String title) {
    	try{
    		JOptionPane.showMessageDialog(null, BUNDLE_TEXT.getString(msg) + "\n" + param,
    			BUNDLE_TEXT.getString(title), JOptionPane.WARNING_MESSAGE);
    		if (logger != null) {
    			logger.warning(BUNDLE_TEXT.getString(msg) + "\n" + param);
    		}
    	} catch (MissingResourceException e){
    		JOptionPane.showMessageDialog(null, msg + "\n" + param,	title, JOptionPane.WARNING_MESSAGE);
    		if (logger != null) {
    			logger.warning(msg + "\n" + param);
    		}    		
    	}        
    }

    
    public static void showError(Exception e) {
    	String errorInfo = getErrorInfo();
    	try{
    		JOptionPane.showMessageDialog(null, e.getMessage(), BUNDLE_TEXT.getString("inp_descr"), JOptionPane.WARNING_MESSAGE);
    		if (logger != null) {
    			//logger.warning(e.getMessage() + "\n" + e.toString() + "\n" + errorInfo);
    			logger.warning(e.toString() + "\n" + errorInfo);
    		}
    	} catch (MissingResourceException e1){
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Error information", JOptionPane.WARNING_MESSAGE);
    		if (logger != null) {
    			logger.warning(e.toString() + "\n" + errorInfo);
    		}    		
    	}   
    }    
    
    
    public static void showError(Exception e, String param) {
    	String errorInfo = getErrorInfo();
    	try{
    		JOptionPane.showMessageDialog(null, e.getMessage(), BUNDLE_TEXT.getString("inp_descr"), JOptionPane.WARNING_MESSAGE);
    		if (logger != null) {
    			logger.warning(e.toString() + "\n" + errorInfo + "\n" + param);
    		}
    	} catch (MissingResourceException e1){
    		JOptionPane.showMessageDialog(null, e.getMessage(), "Error information", JOptionPane.WARNING_MESSAGE);
    		if (logger != null) {
    			logger.warning(e.toString() + "\n" + errorInfo + "\n" + param);
    		}    		
    	}        
    }     
    
    
    public static void logError(Exception e, String param) {
    	String errorInfo = getErrorInfo();
		if (logger != null) {
			logger.warning(e.toString() + "\n" + errorInfo + "\n" + param);
		}
    }         
    
    
    private static String getErrorInfo(){
    	StackTraceElement[] ste = Thread.currentThread().getStackTrace();
    	String aux = ste[3].toString();
    	return aux;
    }
    
    
    public static int confirmDialog(String msg, String title) {
    	int reply;
    	try{
	    	reply = JOptionPane.showConfirmDialog(null, BUNDLE_TEXT.getString(msg),
	    		BUNDLE_TEXT.getString(title), JOptionPane.YES_NO_OPTION);
	        if (logger != null) {
	            logger.warning(BUNDLE_TEXT.getString(msg));
	        }
    	} catch (MissingResourceException e){
	    	reply = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
    		if (logger != null) {
    			logger.info(msg);
    		}    		
    	}
        return reply;    	
    }        
    


    /**
     * Returns the class name of the installed LookAndFeel with a name
     * containing the name snippet or null if none found.
     * 
     * @param nameSnippet a snippet contained in the Laf's name
     * @return the class name if installed, or null
     */
    public static String getLookAndFeelClassName(String nameSnippet) {
        LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo info : plafs) {
            if (info.getName().contains(nameSnippet)) {
                return info.getClassName();
            }
        }
        return null;
    }    

    
}