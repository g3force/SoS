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
import edu.dhbw.sos.gui.IEditModeObserver;


/**
 * controls the simulation
 * simulates new students states out of old ones
 * 
 * @author DirkK
 * 
 */

public class SimController implements ActionListener, MouseListener, IEditModeObserver {
	
	private Course						course;
	private int							currentTime;													// in milliseconds from "begin"
	private int							speed;															// in milliseconds
	private Timer						pulse		= new Timer();
	private boolean					run		= false;
	private static final Logger	logger	= Logger.getLogger(SimController.class);
	
	
	public SimController(Course course) {
		this.course = course;
		currentTime = 0;
		speed = 1000;
	}
	
	
	public void toggle() {
		if (run)
			stop();
		else
			run();
		run = !run;
	}
	
	
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
		logger.info("Simulation Step at " + currentTime);
		synchronized(getClass()) {
			course.simulationStep(currentTime, speed);
		}
		logger.info("History states: " + course.getPlace(0, 0).getHistoryStates().size());
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
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		if(e.getSource() instanceof LiveBtn) {
//			
//		} else if(e.getSource() instanceof PlayBtn) {
//			toggle();
//		} else if(e.getSource() instanceof ForwardBtn) {
//			toggle();
//		} else if(e.getSource() instanceof RewindBtn) {
//			toggle();
//		}
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		float value = 100;
		// right click
		if (e.getButton() == MouseEvent.BUTTON3)
			value *= -1;
		if (course.getSelectedStudent() != null) {
			synchronized(getClass()) {
				course.donInput(course.getSelectedProperty(), value, currentTime);
			}
		}
		course.notifyStudentsObservers();
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
		// TODO NicolaiO Auto-generated method stub
		
	}
	
	
	@Override
	public void exitEditMode() {
		// TODO NicolaiO Auto-generated method stub
		
	}
}
