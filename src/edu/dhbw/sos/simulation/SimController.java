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

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import edu.dhbw.sos.GUI.MainFrame;
import edu.dhbw.sos.data.Course;
import edu.dhbw.sos.data.GUIData;
import edu.dhbw.sos.data.IPlace;
import edu.dhbw.sos.data.StudentState;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.helper.Matrix;

/**
 * controls the simulation
 * simulates new students states out of old ones
 * 
 * @author DirkK
 * 
 */

public class SimController {
	
	Course course;
	int currentTime; //in milliseconds from "begin"
	int speed; //in milliseconds  
	Timer pulse = new Timer();
	public SimController(Course course, MainFrame mf) {
		currentTime = 0;
		speed = 1000;
		run();
	}
	
	public StudentState nextState(StudentState oldState) {
		return null;
		
	}
	public static void main(String[] args) {
		new SimController(new Course(), new MainFrame(new GUIData()));
	}
	public void run() {
		TimerTask simulation = new TimerTask(){
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
	
	private void simulationStep() {
		currentTime += speed;
		Entry<Integer, IPlace[][]> donInteraction = course.historyStateInInterval(currentTime-speed, currentTime);
		if(donInteraction != null) {
			course.setStudents(donInteraction.getValue());
		}
	}
	
	public void jumpTo(int time) {
		currentTime = time;
		//fetch start state from history
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



