/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 9, 2012
 * Author(s): dirk
 * 
 * *********************************************************
 */
package edu.dhbw.sos.simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.suggestions.SuggestionManager;
import edu.dhbw.sos.gui.plan.ForwardBtn;
import edu.dhbw.sos.gui.plan.LiveBtn;
import edu.dhbw.sos.gui.plan.PlayBtn;
import edu.dhbw.sos.gui.plan.RewindBtn;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.observers.ICurrentCourseObserver;
import edu.dhbw.sos.observers.IEditModeObserver;
import edu.dhbw.sos.observers.ITimeGUIObserver;
import edu.dhbw.sos.observers.Observers;


/**
 * controls the simulation
 * simulates new students states out of old ones
 * 
 * @author DirkK
 * 
 */
public class SimController implements ActionListener, MouseListener, IEditModeObserver, ITimeGUIObserver,
		ICurrentCourseObserver {
	private static final Logger	logger			= Logger.getLogger(SimController.class);
	
	private Course						course;
	private int							currentTime;															// in
																														// milliseconds
																														// from
																														// "begin"
	private int							speed;																	// in
																														// milliseconds
	private int							notifyStep		= 1;
	public static final int			realInterval	= 1000;
	private int							interval			= 1000;
	private transient Timer			pulse				= new Timer();
	private boolean					run				= false;
	
	
	private SuggestionManager		suggestionMgr;
	
	
	/**
	 * 
	 * TODO DirkK, add comment!
	 * 
	 * @param course
	 * @param sm
	 * @author DirkK
	 */
	public SimController(Course course, SuggestionManager sm) {
		this.suggestionMgr = sm;
		reset(course);
	}
	
	
	/**
	 * 
	 * TODO DirkK, add comment!
	 * 
	 * @author DirkK
	 */
	public void reset() {
		stop();
		course.reset();
		setCurrentTime(0);
		setSpeed(1);
		suggestionMgr.reset(course.getProperties());
		run = false;
	}
	
	
	/**
	 * 
	 * TODO DirkK, add comment!
	 * 
	 * @param course
	 * @author DirkK
	 */
	public void reset(Course course) {
		this.course = course;
		reset();
	}
	
	
	/**
	 * 
	 * TODO DirkK, add comment!
	 * 
	 * @author DirkK
	 */
	private void toggle() {
		if (run) {
			stop();
		} else {
			run();
		}
		run = !run;
	}
	
	
	/**
	 * 
	 * TODO DirkK, add comment!
	 * 
	 * @author DirkK
	 */
	public void run() {
		Observers.notifySimulationStarted();
		if (pulse != null) {
			pulse.cancel();
			pulse.purge();
		}
		pulse = new Timer();
		TimerTask simulation = new TimerTask() {
			@Override
			public void run() {
				simulationStep();
				// Check if end of simulation reached
				if (course.getLecture().getLength() * 60 < currentTime / realInterval) {
					stop();
				}
			}
		};
		pulse.scheduleAtFixedRate(simulation, 0, interval);
		logger.info("Simulation started");
	}
	
	
	/**
	 * Stop simulation by canceling the timer and notifing observers
	 * 
	 * @author dirk
	 */
	public void stop() {
		pulse.cancel();
		pulse.purge();
		Observers.notifySimulationStopped();
		logger.info("Simulation stopped");
	}
	
	
	/**
	 * calculates for each student a new state
	 * @author dirk
	 */
	private void simulationStep() {
		setCurrentTime(currentTime + realInterval);
		logger.debug("Simulation Step at " + currentTime);
		course.simulationStep(currentTime);
		
		// calculate state statistics for whole course
		course.calcStatistics(currentTime);
		// update suggestions using statistics of whole course
		suggestionMgr.updateSuggestions(course.getStatAvgStudentState());
		
		// notify GUI after simulation
		notifyStep--;
		if (notifyStep == 0) {
			notifyStep = speed;
			Observers.notifyStudents();
			Observers.notifySelectedStudent();
			Observers.notifyStatistics();
		}
		
		// handle any suggestions
		for (CalcVector cv : suggestionMgr.getAndClearInfluences()) {
			course.suggestionInput(cv);
		}
	}
	
	
	// FIXME Dirk not used
	public void jumpTo(int time) {
		currentTime = time;
		// fetch start state from history
	}
	
	
	// --- GETTERS and SETTERS ---
	
	public int getCurrentTime() {
		return currentTime;
	}
	
	
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
		Observers.notifyTime(currentTime);
	}
	
	
	public int getSpeed() {
		return speed;
	}
	
	
	public void setSpeed(int speed) {
		if (speed > 1024)
			speed = 1024;
		if (speed < 1)
			speed = 1;
		this.speed = speed;
		this.interval = realInterval / speed;
		if (interval == 0)
			interval = 1;
		if (run) {
			stop();
			run();
		}
		Observers.notifySpeed(speed);
	}
	
	
	// --- action listeners ---
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof LiveBtn) {
			// FIXME andres Not implemented
		} else if (e.getSource() instanceof PlayBtn) {
			toggle();
		} else if (e.getSource() instanceof ForwardBtn) {
			setSpeed(getSpeed() * 2);
			Observers.notifySpeed(this.getSpeed());
		} else if (e.getSource() instanceof RewindBtn) {
			setSpeed(getSpeed() / 2);
			Observers.notifySpeed(this.getSpeed());
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		float value = -20;
		// right click
		if (e.getButton() == MouseEvent.BUTTON3)
			value *= -1;
		if (course.getSelectedStudent() != null) {
			// process don input
			course.donInputQueue(course.getSelectedProperty(), value);
		}
		Observers.notifyStudents();
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	
	@Override
	public void enterEditMode() {
		reset();
	}
	
	
	@Override
	public void exitEditMode() {
		
	}
	
	
	@Override
	public void timeChanged(int time) {
		// stop the simulation to make the correct deletions or to simulate to the correct point
		stop();
		
		course.setTime(currentTime, time);
		setCurrentTime(time);
		
		// if simulation was running, set running again
		if (run)
			run();
		logger.debug(course.getPlace(0, 0).getHistoryStates().size());
	}
	
	
	@Override
	public void updateCurrentCourse(Course course) {
		this.course.reset();
		reset(course);
	}
}
