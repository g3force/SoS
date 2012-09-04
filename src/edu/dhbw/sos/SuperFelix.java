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


/**
 * SuperFelix is the main entrance class. It initializes the logging system
 * and creates the most essential objects.
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class SuperFelix {
	public static final String		VERSION		= "0.5";
	public static final String		savepath		= System.getProperty("user.home") + "/.sos/";
	public static final String		coursepath	= savepath + "/courses/";
	
	private static final Logger	logger		= Logger.getLogger(SuperFelix.class);
	private static Courses			courses;
	
	
	/**
	 * Initialize logger and main objects
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
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
		
		// initialize language system
		Locale.setDefault(new Locale("en", "EN"));
		
		// load saved courses
		courses = CourseLoader.loadCourses(coursepath);
		// load courseController, that manages changes of the current course
		CourseController courseController = new CourseController(courses);
		// load SuggestionManager, that monitors the course state and create suggestions
		SuggestionManager sugMngr = new SuggestionManager();
		// load controller for simulation
		SimController simController = new SimController(courses.getCurrentCourse(), sugMngr);
		
		// subscribe sim controller to some events
		Observers.subscribeCurrentCourse(simController);
		Observers.subscribeEditMode(simController);
		
		// create GUI
		MainFrame mainFrame = new MainFrame(simController, courseController, courses, sugMngr);
		mainFrame.pack();
		logger.debug("Sim of Students started.");
	}
	
	
	/**
	 * Close the software.
	 * This will save the courses before exiting.
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
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
