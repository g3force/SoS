/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 15, 2012
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.sos.course.student;

import edu.dhbw.sos.helper.CalcVector;

/**
 * this is an empty place in the class
 * @author dirk
 * 
 */
public class EmptyPlace implements IPlace {
	
	public int paramCount;
	CalcVector actualState;
	
	public EmptyPlace(int paramCount) {
		this.paramCount = paramCount;
		
		//creates an empty state vector
		actualState = new CalcVector(paramCount);
		for(int i=0; i<paramCount; i++)
			actualState.setValueAt(i, 0);
	}
	public CalcVector getActualState() {
		
		return actualState;
	}
	
	public void setActualState(CalcVector acutalState) {
		
	}
}
