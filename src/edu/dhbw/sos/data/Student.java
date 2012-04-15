/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 6, 2012
 * Author(s): Benedikt Zirbes
 *
 * *********************************************************
 */
package edu.dhbw.sos.data;

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
public class Student {
	private CalcVector actualState;
	private CalcVector changeVector;
	
	public Student(LinkedList<String> params) {
		this.actualState = new CalcVector(params);
		this.changeVector = new CalcVector(params);
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
	
	public CalcVector getActualState() {
		return this.actualState;
	}
	public CalcVector getChangeVector() {
		return this.changeVector;
	}
	public boolean addValueToChangeVector(String paramName, int value) {
		boolean ret = false;
		for(int i = 0; i < changeVector.size(); i++) {
			if (paramName.compareTo(changeVector.getTypeAt(i)) == 0) {
				changeVector.setValueAt(i, changeVector.getValueAt(i) + value);
				ret = true;
				break;
			}
		}
		return ret;
	}
	
}
