package edu.dhbw.sos;

import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.dhbw.sos.GUI.MainFrame;


public class SuperFelix {
	private static final Logger	logger					= Logger.getLogger(SuperFelix.class);
	private static String			datapath;
	
	public SuperFelix() {
		/*
		 * initialize log4j, a logger from apache.
		 * See http://logging.apache.org/log4j/1.2/manual.html for more details
		 * Log Levels: TRACE, DEBUG, INFO, WARN, ERROR and FATAL
		 * 
		 * configuration is stored in a config file. If it does not exist, use basic config
		 */
		URL logUrl = getClass().getResource("/res/log4j.conf"); //$NON-NLS-1$
		if (logUrl != null) {
			PropertyConfigurator.configure(logUrl);
		} else {
			// basic config with only a console appender
			BasicConfigurator.configure();
			logger.setLevel(Level.ALL);
		}
		
		// load datapath
		// works for Windows and Linux... so the data is stored in the systems userdata folder...
		datapath = System.getProperty("user.home") + "/.t10keyboard"; //$NON-NLS-1$ //$NON-NLS-2$
		
		new MainFrame();
		logger.info("Keyboard started.");
	}
	
	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		new SuperFelix();
	}
	
	public static String getDatapath() {
		return datapath;
	}
	
	public static void setDatapath(String datapath) {
		SuperFelix.datapath = datapath;
	}

}
