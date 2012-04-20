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

import java.util.LinkedList;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.influence.Influence;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.helper.Parameter;
import edu.dhbw.sos.simulation.SimController;


/**
 * TODO bene, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author bene
 * 
 */
public class Student implements IPlace, Cloneable {
	private CalcVector	actualState;
	private CalcVector	changeVector;
	private boolean		isEmpty;
	private static final Logger	logger	= Logger.getLogger(Student.class);
	
	
	public Student(LinkedList<String> params) {
		this.actualState = new CalcVector(params);
		this.changeVector = new CalcVector(params);
		this.isEmpty = false;
		
	}
	
	
	public Student(int vectorInitSize) {
		this.actualState = new CalcVector(vectorInitSize);
		this.changeVector = new CalcVector(vectorInitSize);
		this.isEmpty = false;
	}
	
	
	public Student(LinkedList<String> params, boolean empty) {
		this.actualState = new CalcVector(params);
		this.changeVector = new CalcVector(params);
		this.isEmpty = empty;
		
	}
	
	
	public Student(int vectorInitSize, boolean empty) {
		this.actualState = new CalcVector(vectorInitSize);
		this.changeVector = new CalcVector(vectorInitSize);
		this.isEmpty = empty;
	}
	
	
	private Student() {
		
	}
	
	
	/**
	 * Only for testing yet. Should be tested and discussed
	 * 
	 * @param index index in vector
	 * @param value value to add (negative to sub)
	 * @author NicolaiO
	 */
	public void donInput(int index, int value) {
		CalcVector cv = new CalcVector(4);
		cv.setValueAt(index, value);
		this.addToStateVector(cv, 0, 0);
		this.addToChangeVector(cv, 0, 0);
	}
	
	/**
	 * calculates the next state for the actual state vector
	 * @param changeVector
	 * @param influence
	 * @author dirk
	 */
	public void calcNextSimulationStep(CalcVector changeVector, Influence influence, int x, int y) {
		
		// - - parameter
		double parameterInf = 0.001;
		changeVector.addCalcVector(influence.getInfluencedParameterVector(this.getActualState().clone(), parameterInf));
		
		if(y==0 && x==0)
			changeVector.printCalcVector("matrix influenced");
		
		// - usual behavior of the student -> usualBehav * timeInf
		double behaviorInf = 0.001;
		changeVector.addCalcVector(this.getChangeVector().clone().multiplyWithDouble(behaviorInf));
		
		if(y==0 && x==0)
			changeVector.printCalcVector("student influenced");
		
		//time depending
		//TODO: bring all values to an average value by time
		
		this.addToStateVector(changeVector, x, y);
	}
	
	public void addToStateVector(CalcVector addVector, int x, int y) {
		if(y==0 && x==0)
			addVector.printCalcVector("ADD Vector");
		if(y==0 && x==0)
			actualState.printCalcVector("Actual State");
		for(int i=0; i<addVector.size(); i++) {
			double sValue = actualState.getValueAt(i);
			double vValue = addVector.getValueAt(i);
			//if the add value is positive, take the percentage missing to 100, and multiply it with 2
			//i.e. acutalState = 30, addVector = 20 -> (100-30)*2/100 -> 1,4*20 = 28 -> 58
			//i.e. acutalState = 80, addVector = 20 -> (100-80)*2/100 -> 0,4*20 = 8 -> 88
			//i.e. acutalState = 95, addVector = 20 -> (100-95)*2/100 -> 0,1*20 = 2 -> 97
			//i.e. acutalState = 98, addVector = 20 -> (100-98)*2/100 -> 0,04*20 = 0.8 -> 98.8
			if(vValue>0) {
				actualState.setValueAt(i, actualState.getValueAt(i) + (int)(vValue*((100-sValue)*2/100)));
			} else {
				actualState.setValueAt(i, actualState.getValueAt(i) + (int)(vValue*((sValue)*2/100)));
			}
			if(actualState.getValueAt(i)<0) {
				actualState.setValueAt(i, 0);
			}
			if(actualState.getValueAt(i)>100) {
				actualState.setValueAt(i, 100);
			}	
		}
	}
	
	public void addToChangeVector(CalcVector addVector, int x, int y) {
		//TODO check limits keep in normal values
		changeVector.addCalcVector(addVector);
	}
	
	public void printAcutalState() {
		String out = "Students state: ";
		for(int i=0; i<actualState.size(); i++)
			out += actualState.getValueAt(i)+", ";
		out = out.substring(0, out.length()-2);
		logger.info(out);
	}
	
	/**
	 * another interface that is needed but should be reconsidered
	 * 
	 * @return
	 * @author NicolaiO
	 */
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
	 * Adds the parameter to the actualState and changeVector of this student.
	 * 
	 * NOTE: This means it has the same value in both vectors!
	 * 
	 * @param p
	 * @author bene
	 */
	public void addParamToStudent(Parameter p) {
		this.actualState.addParamToVector(p);
		this.changeVector.addParamToVector(p);
	}
	
	
	/**
	 * Returns the actualState of this student to allow further access to it.
	 * 
	 * @return
	 * @author bene
	 */
	public CalcVector getActualState() {
		return this.actualState;
	}
	
	/**
	 * Sets the actualState vector
	 * 
	 * @return
	 * @author dirk
	 */
	public void setActualState(CalcVector cv) {
		this.actualState = cv;
	}
	
	
	/**
	 * Returns the changeVector of this student to allow further access to it.
	 * 
	 * NOTE: this is only for testing. Should be replaced by a separate method in Student.
	 * 
	 * @return
	 * @author bene
	 */
	public CalcVector getChangeVector() {
		return this.changeVector;
	}
	
	
	/**
	 * Returns whether the position in course which is represented by this student object is empty or not.
	 * 
	 * @return
	 * @author bene
	 */
	public boolean isEmpty() {
		return this.isEmpty;
	}
	
	
	/**
	 * Sets the isEmpty to value
	 * 
	 * @param value
	 * @author bene
	 */
	public void setEmpty(boolean value) {
		this.isEmpty = value;
	}
	
	
	/**
	 * Adds value to the value of the parmeter with name paramName. Returns true if successful and false if a parameter
	 * with name paramName was not in the vector.
	 * 
	 * @param paramName
	 * @param value
	 * @return
	 * @author bene
	 */
	public boolean addValueToChangeVector(String paramName, int value) {
		boolean ret = false;
		for (int i = 0; i < changeVector.size(); i++) {
			if (paramName.compareTo(changeVector.getTypeAt(i)) == 0) {
				changeVector.setValueAt(i, changeVector.getValueAt(i) + value);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	
	/**
	 * Adds value to the value of the parmeter at position index.
	 * 
	 * @param index
	 * @param value
	 * @return
	 * @author bene
	 */
	public void addValueToChangeVector(int index, int value) {
		if (index >= changeVector.size()) {
			throw new IndexOutOfBoundsException();
		}
		changeVector.setValueAt(index, changeVector.getValueAt(index) + value);
	}
	
	/**
	 * Adds value to the value of the parmeter at position index.
	 * 
	 * @param index
	 * @param value
	 * @return
	 * @author bene
	 */
	public void addValueToStateVector(int index, int value) {
		if (index >= changeVector.size()) {
			throw new IndexOutOfBoundsException();
		}
		actualState.setValueAt(index, actualState.getValueAt(index) + value);
	}
	
	
	
	
	/**
	 * Returns a string representation of this student object for saving it to a file. Note that only the actualState
	 * vector and the attribute isEmpty are saved.
	 * 
	 * @return
	 * @author bene
	 */
	public String getSaveableString() {
		String ret = "<student isEmpty=";
		if (this.isEmpty) {
			ret += "\"1\">";
		} else {
			ret += "\"0\">";
		}
		
		for (int i = 0; i < this.actualState.size(); i++) {
			ret += "<attribute name=\"" + this.actualState.getTypeAt(i) + "\" value=\"" + this.actualState.getValueAt(i)
					+ "\"></attribute>";
		}
		ret += "</student>";
		return ret;
	}
	
	
	/**
	 * Creates an exact clone of this student object. All values are copied into a completely new object (no references!)
	 */
	public Student clone() {
		Student ret = new Student();
		ret.actualState = this.actualState.clone();
		ret.changeVector = this.changeVector.clone();
		ret.isEmpty = this.isEmpty;
		return ret;
	}
}
