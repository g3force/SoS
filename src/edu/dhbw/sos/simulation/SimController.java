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
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.gui.plan.ForwardBtn;
import edu.dhbw.sos.gui.plan.LiveBtn;
import edu.dhbw.sos.gui.plan.PlayBtn;
import edu.dhbw.sos.gui.plan.RewindBtn;
import edu.dhbw.sos.gui.right.IEditModeObserver;


/**
 * controls the simulation
 * simulates new students states out of old ones
 * 
 * @author DirkK
 * 
 */

public class SimController implements ActionListener, MouseListener, IEditModeObserver, ITimeObserver {
	private static final Logger			logger			= Logger.getLogger(SimController.class);
	
	private Course								course;
	private int									currentTime;															// in
																																// milliseconds
																																// from
																																// "begin"
	private int									speed;																	// in
																																// milliseconds
	private int									interval;
	private transient Timer					pulse				= new Timer();
	private boolean							run				= false;
	
	private LinkedList<ISpeedObserver>	speedObservers	= new LinkedList<ISpeedObserver>();
	private LinkedList<ITimeObserver>	timeObservers	= new LinkedList<ITimeObserver>();
	

	public SimController(Course course) {
		this.course = course;
		currentTime = 0;
		speed = 1;
		interval = 1000;
	}
	
	
	public void notifySpeedObservers() {
		for (ISpeedObserver so : speedObservers) {
			so.speedChanged(speed);
		}
	}
	
	
	public void subscribeSpeed(ISpeedObserver so) {
		speedObservers.add(so);
	}
	
	
	public void notifyTimeObservers() {
		int timeInMin = currentTime / 60000;
		for (ITimeObserver to : timeObservers) {
			to.timeChanged(timeInMin);
		}
	}
	
	
	public void subscribeTime(ITimeObserver to) {
		timeObservers.add(to);
	}
	
	
	public boolean toggle() {
		if (run)
			stop();
		else
			run();
		run = !run;
		return true;
	}
	
	
	public void run() {
		pulse = new Timer();
		TimerTask simulation = new TimerTask() {
			@Override
			public void run() {
				simulationStep();
			}
		};
		pulse.scheduleAtFixedRate(simulation, 0, interval);
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
		setCurrentTime(currentTime + 1000);
		logger.info("Simulation Step at " + currentTime);
		course.simulationStep(currentTime, interval);
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
		notifyTimeObservers();
	}
	
	
	public int getSpeed() {
		return speed;
	}
	
	
	public void setSpeed(int speed) {
		if (speed > 64)
			speed = 64;
		if (speed < 1)
			speed = 1;
		this.speed = speed;
		this.interval = 1000 / speed;
		if (run) {
			stop();
			run();
		}
	}
	
	
	// --- action listeners ---
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof LiveBtn) {
			// FIXME Not implemented
		} else if (e.getSource() instanceof PlayBtn) {
			if (toggle()) {
				((PlayBtn) e.getSource()).toggle();
			}
		} else if (e.getSource() instanceof ForwardBtn) {
			setSpeed(getSpeed() * 2);
			notifySpeedObservers();
		} else if (e.getSource() instanceof RewindBtn) {
			setSpeed(getSpeed() / 2);
			notifySpeedObservers();
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		float value = 100;
		// right click
		if (e.getButton() == MouseEvent.BUTTON3)
			value *= -1;
		if (course.getSelectedStudent() != null) {
			course.donInput(course.getSelectedProperty(), value, currentTime);
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
	
	
	@Override
	public void timeChanged(int time) {
		setCurrentTime(time);
	}
}
