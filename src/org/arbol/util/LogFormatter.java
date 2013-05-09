package org.arbol.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;


public class LogFormatter extends SimpleFormatter{
 
    @Override
    public String format(LogRecord record) {

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        long now = record.getMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);

        String output = formatter.format(calendar.getTime()) + " (" + record.getLevel() + "): "
            + record.getSourceClassName() + " " + record.getSourceMethodName() + "\n"
            + record.getMessage() + "\n\n";
        
        return output;
        
   }
    
}