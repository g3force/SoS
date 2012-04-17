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

import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.helper.Parameter;


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
	 * NOTE: this is only for testing. Should be replaced by a separate method in Student.
	 * 
	 * @return
	 * @author bene
	 */
	public CalcVector getActualState() {
		return this.actualState;
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
