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
import edu.dhbw.sos.observers.Observers;
import edu.dhbw.sos.simulation.SimController;


public class SuperFelix {
	public static String				VERSION	= "0.5";
	public static final String		savepath	= System.getProperty("user.home") + "/.sos/";
	public static final String		coursepath	= savepath + "/courses/";
	private static final Logger	logger	= Logger.getLogger(SuperFelix.class);
	private static Courses			courses;

	
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
		Locale.setDefault(new Locale("en", "EN"));
		
		// create object for the data to be displayed in GUI
		// the references will be used to update it afterwards
		
		courses = CourseLoader.loadCourses(coursepath);
		CourseController courseController = new CourseController(courses);
		SuggestionManager sugMngr = new SuggestionManager();
		SimController simController = new SimController(courses.getCurrentCourse(), sugMngr);
		Observers.subscribeCurrentCourse(simController);
		Observers.subscribeEditMode(simController);
		MainFrame mainFrame = new MainFrame(simController, courseController, courses, sugMngr);
		mainFrame.pack();
		logger.info("Sim of Students started.");
		// Observers.print();
	}
	
	
	public static void close() {
		CourseSaver.saveCourses(courses, coursepath);
		System.exit(0);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SuperFelix();
	}
}
