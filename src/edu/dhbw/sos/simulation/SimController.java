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

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import edu.dhbw.sos.SuperFelix;
import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.gui.MainFrame;


/**
 * controls the simulation
 * simulates new students states out of old ones
 * 
 * @author DirkK
 * 
 */

public class SimController {
	
	private Course	course;
	private MainFrame mainFrame;
	private int		currentTime;			// in milliseconds from "begin"
	private int		speed;					// in milliseconds
	private Timer		pulse	= new Timer();
	private static SimController instance = null;
	private boolean run = false;
	private static final Logger	logger	= Logger.getLogger(SimController.class);
	
	
	private SimController(Course course, MainFrame mf) {
		this.course = course;
		this.mainFrame = mf;
		currentTime = 0;
		speed = 1000;
	}
	
	public static void init(Course course, MainFrame mf) {
		instance = new SimController(course, mf);
	}
	
	public static SimController getInstance() throws Exception {
		if(instance == null) {
			throw new NotInitializedException("Please call the init function first. Thank you.");
		} else {
			return instance;
		}
	}
	
	public void toggle() {
		if(run)
			stop();
		else
			run();
		run = !run;
	}
	
	
//	public static void main(String[] args) {
//		new SimController(new Course(), new MainFrame(new GUIData()));
//	}
	
	
	public void run() {
		pulse = new Timer();
		TimerTask simulation = new TimerTask() {
			public void run() {
				simulationStep();
			}
		};
		pulse.scheduleAtFixedRate(simulation, 0, speed);
	}
	
	
	public void stop() {
		pulse.cancel();
		pulse.purge();
	}
	
	
	/**
	 * calculates for each student a new state
	 * @author dirk
	 */
	private void simulationStep() {
		currentTime += speed;
		logger.info("Simulation Step at "+currentTime);
		course.simulationStep(currentTime, speed);
		mainFrame.update();
	}
	
	
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
	}
	
	
	public int getSpeed() {
		return speed;
	}
	
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
