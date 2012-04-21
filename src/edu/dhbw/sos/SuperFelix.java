package edu.dhbw.sos;

import java.net.URL;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.data.CourseManager;
import edu.dhbw.sos.gui.GUIData;
import edu.dhbw.sos.gui.MainFrame;
import edu.dhbw.sos.simulation.SimController;


public class SuperFelix {
	private static final Logger	logger	= Logger.getLogger(SuperFelix.class);
	private static String			datapath;
	private final GUIData			data;
	private final MainFrame			mainFrame;
	
	
	public SuperFelix() {
		/*
		 * initialize log4j, a logger from apache.
		 * See http://logging.apache.org/log4j/1.2/manual.html for more details
		 * Log Levels: TRACE, DEBUG, INFO, WARN, ERROR and FATAL
		 * 
		 * configuration is stored in a config file. If it does not exist, use basic config
		 */
		URL logUrl = getClass().getResource("/res/log4j.conf");
		if (logUrl != null) {
			PropertyConfigurator.configure(logUrl);
		} else {
			// basic config with only a console appender
			BasicConfigurator.configure();
			logger.setLevel(Level.ALL);
		}
		
		// load datapath
		// works for Windows and Linux... so the data is stored in the systems userdata folder...
		datapath = System.getProperty("user.home") + "/.sos";
		
		// Locale.setDefault(new Locale("en", "EN"));
		Locale.setDefault(new Locale("de", "DE"));
		
		// create object for the data to be displayed in GUI
		// the references will be used to update it afterwards
		data = new GUIData();
		CourseManager courseManager = new CourseManager();
		courseManager.loadCourses();
		courseManager.saveCourses();
		mainFrame = new MainFrame(data);
		mainFrame.update();
		
		SimController.init(data.getCourse(), mainFrame);
		logger.info("Sim of Students started.");
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
