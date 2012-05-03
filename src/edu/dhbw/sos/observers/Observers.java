/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 30, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.observers;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.Course;


/**
 * This static class is the holder for all observers.
 * Observers are used for communicating between different modules.
 * Modules, that are interested in an event should subscribe to it here.
 * Modules, that want to notify about a change should call the according notify method.
 * 
 * This class should not be instantiated, but should only serve as a static global module, that all other modules have
 * access to.
 * 
 * @author NicolaiO
 * 
 */
public class Observers {
	private static final Logger									logger							= Logger.getLogger(Observers.class);

	private static LinkedList<IStudentsObserver>				studentsObservers				= new LinkedList<IStudentsObserver>();
	private static LinkedList<ISelectedStudentObserver>	selectedCourseObservers		= new LinkedList<ISelectedStudentObserver>();
	private static LinkedList<IStatisticsObserver>			statisticsObservers			= new LinkedList<IStatisticsObserver>();
	private static LinkedList<ICurrentCourseObserver>		currentCourseOberservers	= new LinkedList<ICurrentCourseObserver>();
	private static LinkedList<ICoursesListObserver>			coursesListOberservers		= new LinkedList<ICoursesListObserver>();
	private static LinkedList<IEditModeObserver>				editModeObservers				= new LinkedList<IEditModeObserver>();
	
	private static LinkedList<ISpeedObserver>					speedObservers					= new LinkedList<ISpeedObserver>();
	private static LinkedList<ITimeObserver>					timeObservers					= new LinkedList<ITimeObserver>();
	private static LinkedList<ITimeGUIObserver>				timeGUIObservers				= new LinkedList<ITimeGUIObserver>();
	private static LinkedList<ISimulation>						simulationOberservers		= new LinkedList<ISimulation>();

	// SimulateUntil Observer for showing a graphic when simulating to a point
	private static LinkedList<ISimUntilObserver>				simUntilObservers				= new LinkedList<ISimUntilObserver>();
	
	private static LinkedList<ISuggestionsObserver>			suggestionObserver			= new LinkedList<ISuggestionsObserver>();


	// ##################################################################################
	// ############################# others section #####################################
	// ##################################################################################
	
	
	private Observers() {
	}
	

	public static void print() {
		printObserverList(studentsObservers, "studentsObservers");
		printObserverList(selectedCourseObservers, "selectedCourseObservers");
		printObserverList(statisticsObservers, "statisticsObservers");
		printObserverList(currentCourseOberservers, "currentCourseOberservers");
		printObserverList(coursesListOberservers, "coursesListOberservers");
		printObserverList(editModeObservers, "editModeObservers");
		printObserverList(speedObservers, "speedObservers");
		printObserverList(timeObservers, "timeObservers");
		printObserverList(timeGUIObservers, "timeGUIObservers");
		printObserverList(simulationOberservers, "simulationOberservers");
		printObserverList(simUntilObservers, "simUntilObservers");
		printObserverList(suggestionObserver, "suggestionObserver");
	}
	
	
	private static void printObserverList(LinkedList<?> obsl, String name) {
		logger.debug("Observer: " + name);
		logger.debug("Size: " + obsl.size());
		for (Object o : obsl) {
			logger.debug(o.toString());
		}
		logger.debug("");
	}


	// ##################################################################################
	// ############################# notify section #####################################
	// ##################################################################################
	

	public static void notifyStudents() {
		for (IStudentsObserver so : studentsObservers) {
			so.updateStudents();
		}
	}
	
	
	public static void notifySelectedStudent() {
		for (ISelectedStudentObserver so : selectedCourseObservers) {
			so.updateSelectedStudent();
		}
	}
	
	
	public static void notifyStatistics() {
		for (IStatisticsObserver so : statisticsObservers) {
			so.updateStatistics();
		}
	}
	
	
	public static void notifyEditModeEntered() {
		for (IEditModeObserver o : editModeObservers) {
			o.enterEditMode();
		}
	}
	
	
	public static void notifyEditModeExited() {
		for (IEditModeObserver o : editModeObservers) {
			o.exitEditMode();
		}
	}


	public static void notifyCoursesList() {
		for (ICoursesListObserver clo : coursesListOberservers) {
			clo.updateCoursesList();
		}
	}
	
	
	public static void notifyCurrentCourse(Course curCourse) {
		for (ICurrentCourseObserver cco : currentCourseOberservers) {
			cco.updateCurrentCourse(curCourse);
		}
	}
	
	
	/**
	 * 
	 * @param speed value should be between 1 and 1024, preferably multiples of 2
	 * @author NicolaiO
	 */
	public static void notifySpeed(int speed) {
		for (ISpeedObserver so : speedObservers) {
			so.speedChanged(speed);
		}
	}
	
	
	/**
	 * FIXME Daniel specify more clearly what to notify
	 * @param time time in milliseconds
	 * @author NicolaiO
	 */
	public static void notifyTime(int time) {
		for (ITimeObserver to : timeObservers) {
			to.timeChanged(time);
		}
	}
	
	
	public static void notifyTimeGUI(int time) {
		for (ITimeGUIObserver tgo : timeGUIObservers) {
			tgo.timeChanged(time);
		}
	}

	
	public static void notifySimulationStarted() {
		for (ISimulation cco : simulationOberservers) {
			cco.simulationStarted();
		}
	}
	
	
	public static void notifySimulationStopped() {
		for (ISimulation cco : simulationOberservers) {
			cco.simulationStopped();
		}
	}
	
	
	public static void notifySimUntil(boolean state) {
		for (ISimUntilObserver suo : simUntilObservers) {
			suo.updateSimUntil(state);
		}
	}
	
	
	public static void notifySuggestion() {
		for (ISuggestionsObserver so : suggestionObserver) {
			so.updateSuggestions();
		}
	}


	// ##################################################################################
	// ############################# subscribe section ##################################
	// ##################################################################################
	
	
	public static void subscribeStudents(IStudentsObserver so) {
		studentsObservers.add(so);
	}
	
	
	public static void subscribeSelectedStudent(ISelectedStudentObserver so) {
		selectedCourseObservers.add(so);
	}
	
	
	public static void subscribeStatistics(IStatisticsObserver so) {
		statisticsObservers.add(so);
	}
	
	
	public static void subscribeEditMode(IEditModeObserver so) {
		editModeObservers.add(so);
	}
	
	
	public static void subscribeCurrentCourse(ICurrentCourseObserver cco) {
		currentCourseOberservers.add(cco);
	}
	
	
	public static void subscribeCoursesList(ICoursesListObserver clo) {
		coursesListOberservers.add(clo);
	}
	
	
	public static void subscribeTime(ITimeObserver to) {
		timeObservers.add(to);
	}
	
	
	public static void subscribeTimeGUI(ITimeGUIObserver tgo) {
		timeGUIObservers.add(tgo);
	}

	
	public static void subscribeSpeed(ISpeedObserver so) {
		speedObservers.add(so);
	}
	
	
	public static void subscribeSimulation(ISimulation cco) {
		simulationOberservers.add(cco);
	}
	
	
	public static void subscribeSimUntil(ISimUntilObserver suo) {
		simUntilObservers.add(suo);
	}
	
	
	public static void subscribeSuggestions(ISuggestionsObserver so) {
		suggestionObserver.add(so);
	}
}
