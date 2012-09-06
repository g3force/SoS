/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 6, 2012
 * Author(s): Benedikt Zirbes
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course.student;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.influence.Influence;
import edu.dhbw.sos.helper.CalcVector;


/**
 * this class represents a student
 * 
 * @author bene, dirk
 * 
 */
public class Student implements IPlace, Cloneable {
	private static final Logger									logger				= Logger.getLogger(Student.class);
	/**
	 * Consider to add new attributes to the clone() method
	 * if you want to store them persistent!
	 */
	private transient CalcVector									actualState;
	private transient LinkedHashMap<Integer, CalcVector>	historyStates;
	private CalcVector												changeVector;
	int																	noChange				= 0;
	final int															donInputNoChange	= 600;
	
	private int															historyCounter		= 0;
	

	public Student(int vectorInitSize) {
		initChangeVector(vectorInitSize);

		init(vectorInitSize);
	}
	
	
	public Student(CalcVector actualState, CalcVector changeVector) {
		this.actualState = actualState;
		this.changeVector = changeVector;
		this.historyStates = new LinkedHashMap<Integer, CalcVector>();
	}
	
	
	/**
	 * inits a new student, the changeVector has to be set before
	 * 
	 * @author dirk
	 */
	private void init(int vectorInitSize) {

		this.actualState = new CalcVector(vectorInitSize);
		for (int i = 0; i < vectorInitSize; i++) {
			float changeVectorFloat = changeVector.getValueAt(i);
			this.actualState.setValueAt(i, changeVectorFloat * 50);
		}
		this.historyStates = new LinkedHashMap<Integer, CalcVector>();
	}
	
	
	private Object readResolve() {
		init(4);
		return this;
	}
	
	
	@Override
	public void reset() {
		init(4);
	}
	
	
	/**
	 * React on an input from the user (Dozent)
	 * 
	 * @param index index in vector
	 * @param value value to add (negative to sub)
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	@Override
	public void donInput(int index, float value) {
		noChange = donInputNoChange;
		CalcVector cv = new CalcVector(4);
		cv.setValueAt(index, value);
		changeVector.printCalcVector("Don Input: preChangeVector: ");
		addToChangeVector(index, value);
		addToStateVector(cv);
		changeVector.printCalcVector("Don Input: postChangeVector: ");
	}
	
	
	/**
	 * takes a time
	 * it returns the last saved history state before the given time
	 * 
	 * @param time time in milliseconds
	 * @return
	 * @author dirk
	 */
	public Entry<Integer, CalcVector> nearestHistoryState(int time) {
		Entry<Integer, CalcVector> latest = null;
		for (Entry<Integer, CalcVector> historyState : historyStates.entrySet()) {
			if (historyState.getKey() <= time) {
				if (latest == null || latest.getKey() < historyState.getKey())
					latest = historyState;
			}
		}
		return latest;
	}
	
	
	/**
	 * deletes all history states after a given time
	 * 
	 * @param time
	 * @return
	 * @author dirk
	 */
	public void deleteHistoryStateFrom(int time) {
		LinkedList<Integer> toDelete = new LinkedList<Integer>();
		for (Entry<Integer, CalcVector> historyState : historyStates.entrySet()) {
			if (historyState.getKey() > time) {
				toDelete.add(historyState.getKey());
			}
		}
		for (Integer state : toDelete) {
			historyStates.remove(state);
		}
	}
	
	
	/**
	 * calculates the next state for the actual state vector
	 * @param addVector
	 * @param influence
	 * @author dirk
	 */
	public void calcNextSimulationStep(CalcVector addVector, Influence influence, int time) {
		if (noChange > 0) {
			noChange--;
			return;
		}

		// save the first 20 history states all, after that only every 100 simulation step
		if (historyStates.size() < 20 || historyCounter == 0) {
			saveHistoryStates(time);
		}
		historyCounter++;
		if (historyCounter == 10) {
			historyCounter = 0;
		}
		
		// parameter matrix * actual state
		// double parameterInf = 0.00000001;
		// addVector.addCalcVector(influence.getInfluencedParameterVector(this.getActualState().clone(), parameterInf));
		
		for (int i = 0; i < addVector.size(); i++) {
			float addValue = addVector.getValueAt(i);
			if (addValue > 0) {
				addVector.setValueAt(i, addValue * this.getChangeVector().getValueAt(i));
			} else {
				addVector.setValueAt(i, addValue * (2 - this.getChangeVector().getValueAt(i)));
			}
			// System.out.println(addValue + "->" + addVector.getValueAt(i) + "(" + this.getChangeVector().getValueAt(i)
			// + ")");
		}
		this.addToStateVector(addVector);
	}
	
	
	/**
	 * 
	 * adds a change vector to the state vector of a student
	 * 
	 * @param addVector
	 * @author dirk
	 */
	@Override
	public void addToStateVector(CalcVector addVector) {
		for (int i = 0; i < addVector.size(); i++) {
			float oldValue = actualState.getValueAt(i);
			float addValue = addVector.getValueAt(i);
			float newValue;
			// if (Double.isNaN(vValue))
			// vValue = 0;
			// if the add value is positive, take the percentage missing to 100, and multiply it with 2
			// i.e. acutalState = 30, addVector = 20 -> (100-30)*2/100 -> 1,4*20 = 28 -> 58
			// i.e. acutalState = 80, addVector = 20 -> (100-80)*2/100 -> 0,4*20 = 8 -> 88
			// i.e. acutalState = 95, addVector = 20 -> (100-95)*2/100 -> 0,1*20 = 2 -> 97
			// i.e. acutalState = 98, addVector = 20 -> (100-98)*2/100 -> 0,04*20 = 0.8 -> 98.8
			if (addValue > 0) {
				newValue = oldValue + (float) (addValue * ((100 - oldValue) * 2 / 100));
			} else {
				newValue = oldValue + (float) (addValue * ((oldValue) * 2 / 100));
			}
			if (newValue < 0) {
				newValue = 0;
			}
			if (newValue > 100) {
				newValue = 100;
			}
			actualState.setValueAt(i, newValue);
		}
	}
	
	
	/**
	 * modify the change vector after a don input
	 * @param addVector
	 * @author dirk
	 */
	public void addToChangeVector(int index, float value) {
		float old = changeVector.getValueAt(index);
		changeVector.setValueAt(index, changeVector.getValueAt(index) + value / 100);
		// changeVector.addCalcVector(addVector);
		for (int i = 0; i < changeVector.size(); i++) {
			if (changeVector.getValueAt(i) < 0.5)
				changeVector.setValueAt(i, (float) 0.5);
			if (changeVector.getValueAt(i) > 1.5)
				changeVector.setValueAt(i, (float) 1.5);
		}
		logger.warn("change vector changed(" + index + "): " + old + " -> " + changeVector.getValueAt(index));
	}
	
	
	/**
	 * prints the actual state of a student
	 */
	@Override
	public void printAcutalState() {
		String out = "Students state: ";
		for (int i = 0; i < actualState.size(); i++)
			out += actualState.getValueAt(i) + ", ";
		out = out.substring(0, out.length() - 2);
		logger.debug(out);
	}
	
	
	/**
	 * another interface that is needed but should be reconsidered
	 * 
	 * @return
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	@Override
	public int getAverageState() {
		int res = 0;
		for (int i = 0; i < actualState.size(); i++) {
			res += actualState.getValueAt(i);
		}
		if (actualState.size() != 0)
			res /= actualState.size();
		return res;
	}
	
	
	/**
	 * Returns the actualState of this student to allow further access to it.
	 * 
	 * @return
	 * @author bene
	 */
	@Override
	public CalcVector getActualState() {
		return this.actualState;
	}
	
	
	/**
	 * Sets the actualState vector
	 * 
	 * @return
	 * @author dirk
	 */
	@Override
	public void setActualState(CalcVector cv) {
		this.actualState = cv;
	}
	
	
	/**
	 * Returns the changeVector of this student to allow further access to it.
	 * 
	 * @return
	 * @author bene
	 */
	public CalcVector getChangeVector() {
		return this.changeVector;
	}
	
	
	/**
	 * Adds value to the value of the parmeter at position index.
	 * 
	 * @param index
	 * @param value
	 * @return
	 * @author bene
	 */
	public void addValueToStateVector(int index, float value) {
		if (index >= changeVector.size()) {
			throw new IndexOutOfBoundsException();
		}
		CalcVector add = new CalcVector(actualState.size());
		add.setValueAt(index, value);
		addToStateVector(add);
	}
	
	
	/**
	 * Returns a string representation of this student object for saving it to a file. Note that only the actualState
	 * vector and the attribute isEmpty are saved.
	 * 
	 * @return
	 * @author bene
	 */
	public String getSaveableString(LinkedList<String> params) {
		String ret = "<student isEmpty=\"0\">";
		
		for (int i = 0; i < this.actualState.size(); i++) {
			ret += "<attribute name=\"" + params.get(i) + "\" value=\"" + this.actualState.getValueAt(i)
					+ "\"></attribute>";
		}
		ret += "</student>";
		return ret;
	}
	
	
	/**
	 * Creates an exact clone of this student object. All values are copied into a completely new object (no references!)
	 */
	@Override
	public Student clone() {
		Student student = new Student(this.actualState.clone(), this.changeVector.clone());
		student.setHistoryStates(this.historyStates);
		return student;
	}
	
	
	/**
	 * adds a new state to the history states
	 * @param time
	 * @param currentState
	 * @author dirk
	 */
	public void saveHistoryStates(int time) {
		historyStates.put(time, actualState.clone());
	}
	
	
	@Override
	public LinkedHashMap<Integer, CalcVector> getHistoryStates() {
		return historyStates;
	}
	
	
	public void setHistoryStates(LinkedHashMap<Integer, CalcVector> historyStates) {
		this.historyStates = historyStates;
	}
	
	
	/**
	 * Initiates a ChangeVector
	 * 
	 * @param vectorInitSize
	 * @author SebastianN
	 */
	public void initChangeVector(int vectorInitSize) {
		float[] changeVectorF = new float[vectorInitSize];
		for (int i = 0; i < vectorInitSize; i++) {
			changeVectorF[i] = 1f;
		}
		this.changeVector = new CalcVector(changeVectorF);
	}
}
