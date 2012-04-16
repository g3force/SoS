/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 14, 2012
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.sos.course;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import edu.dhbw.sos.course.lecture.Lecture;
import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;

/**
 * This class is a data holder for the data belonging to the course
 * 
 * @author DirkK
 */
public class Course {
	
	IPlace[][] students;
	Influence influence;
	Lecture lecture;
	HashMap<Integer, IPlace[][]> historyStates;
	
	public Course() {
		students = new IPlace[5][5];
		for (int i=0; i < 5;i++) {
			for (int j = 0; j < 5;j++) {
				if(i==3||j==4) {
					students[i][j] = new EmptyPlace();
				} else {
					LinkedList<String> l = new LinkedList<String>();
					l.add("Tireness");
					l.add("Loudness");
					l.add("Attention");
					l.add("Quality");
					students[i][j] = new Student(l);
				}
			}
		}
		influence = new Influence();
		historyStates = new HashMap<Integer, IPlace[][]>();
		lecture = new Lecture(new Date(), 200);
	}
	
	/**
	 * adds a new state to the history states
	 * @param time
	 * @param currentState
	 * @author dirk
	 */
	public void addHistoryState(int time, IPlace[][] currentState) {
		historyStates.put(time, currentState);
	}
	
	/**
	 * takes a start and end time
	 * if there was an interaction from the don in this time period the latest interaction will be returned
	 * otherwise null will be returned 
	 * @param start time in milliseconds
	 * @param end time in milliseconds
	 * @return 
	 * @author dirk
	 */
	public Entry<Integer, IPlace[][]> historyStateInInterval(int start, int end) {
		Entry<Integer, IPlace[][]> latest = null;
		for(Entry<Integer, IPlace[][]> historyState : historyStates.entrySet())
		{
			if(historyState.getKey()>start && historyState.getKey()<end) {
				if(latest==null || latest.getKey()<historyState.getKey())
					latest = historyState;
			}
		}
		return latest;
	}
	
	public void simulationStep(int currentTime, int speed) {
		// check if there was an interaction from the don
			Entry<Integer, IPlace[][]> donInteraction = historyStateInInterval(currentTime - speed, currentTime);
			if (donInteraction != null) {
				students = donInteraction.getValue();
			}
			
			IPlace[][] newState = new IPlace[students.length][students[0].length];
			
			// iterate over all students
			for (int i = 0; i < students.length; i++) {
				for (int j = 0; j < students[i].length; j++) {
					Student newStudent = ((Student) students[i][j]).clone();
					newState[i][j] = (IPlace) newStudent;
					// calculation
				}
			}
	}
	
	// --- GETTERS and SETTERS ---
	
	public IPlace[][] getStudents() {
		return students;
	}

	public void setStudents(IPlace[][] students) {
		this.students = students;
	}

	public Influence getInfluence() {
		return influence;
	}

	public void setInfluence(Influence influence) {
		this.influence = influence;
	}

	public Lecture getLecture() {
		return lecture;
	}

	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}
}
