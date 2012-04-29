package edu.dhbw.sos;

import java.net.URL;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.dhbw.sos.course.CourseController;
import edu.dhbw.sos.course.Courses;
import edu.dhbw.sos.course.io.CourseLoader;
import edu.dhbw.sos.course.io.CourseSaver;
import edu.dhbw.sos.course.suggestions.SuggestionManager;
import edu.dhbw.sos.gui.MainFrame;
import edu.dhbw.sos.simulation.SimController;


public class SuperFelix {
	public static String					VERSION	= "0.5";
	private static final Logger		logger	= Logger.getLogger(SuperFelix.class);
	private static Courses				courses;
	private static String				savepath;
	private static SuggestionManager	sugMngr;

	
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
		
		// Locale.setDefault(new Locale("en", "EN"));
		Locale.setDefault(new Locale("de", "DE"));
		
		// load datapath
		// works for Windows and Linux... so the data is stored in the systems userdata folder...
		savepath = System.getProperty("user.home") + "/.sos/";
		
		
		// create object for the data to be displayed in GUI
		// the references will be used to update it afterwards
		
		courses = CourseLoader.loadCourses(savepath);
		CourseController courseController = new CourseController(courses);
		sugMngr = new SuggestionManager();
		SimController simController = new SimController(courses.getCurrentCourse(), sugMngr);
		courses.subscribeCurrentCourse(simController);
		MainFrame mainFrame = new MainFrame(simController, courseController, courses, sugMngr);
		mainFrame.pack();
		logger.info("Sim of Students started.");
	}
	
	
	public static void close() {
		CourseSaver.saveCourses(courses, savepath);
		System.exit(0);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SuperFelix();
	}
}
