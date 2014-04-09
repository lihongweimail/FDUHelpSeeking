package cn.edu.fudan.se.helpseeking.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Microser
 *
 */
public class MicroLog 
{
	private final static Logger logger = Logger.getLogger("cn.edu.fudan.se.microser");
	static{
		try
		{
		logger.setLevel(Level.FINE);		
		File file = new File(CommUtil.getCurrentProjectPath() + "\\Log\\" + CommUtil.getDateTime() +
				".txt");
		if(file.exists() == false)
			file.createNewFile();		
		Handler handler = new FileHandler(file.getAbsolutePath(), 0, 10);
		logger.addHandler(handler);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void log(Level level , String message)
	{
		logger.log(level, message);		
	}
	
	
	/**
	 * Log a message, with associated Throwable information.
	 * If the logger is currently enabled for the given message level then the given arguments are stored in a LogRecord which is forwarded to all registered output handlers.
	 * Note that the thrown argument is stored in the LogRecord thrown property, rather than the LogRecord parameters property. Thus is it processed specially by output Formatters and is not treated as a formatting parameter to the LogRecord message property.	  
	 * @param level One of the message level identifiers, e.g. Level.SEVERE
	 * @param message The string message (or a key in the message catalog)
	 * @param thrown Throwable associated with log message.
	 */
	public static void log(Level level, String message, Throwable thrown)
	{
		logger.log(level,message,thrown);
		
	}
	
	public static void main(String... strings )
	{
		for(int i=0; i<100; i++)
			logger.log(Level.WARNING, "Something");
	}
	
}
