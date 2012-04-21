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

import java.util.LinkedHashMap;

import edu.dhbw.sos.helper.CalcVector;


/**
 * this is an empty place in the class
 * @author dirk
 * 
 */
public class EmptyPlace implements IPlace {
	
	public int	paramCount;
	CalcVector	actualState;
	
	
	public EmptyPlace(int paramCount) {
		this.paramCount = paramCount;
		
		// creates an empty state vector
		actualState = new CalcVector(paramCount);
		for (int i = 0; i < paramCount; i++)
			actualState.setValueAt(i, 0);
	}
	
	
	public CalcVector getActualState() {
		return actualState;
	}
	
	
	@Override
	public void setActualState(CalcVector acutalState) {
		
	}
	
	
	@Override
	public int getAverageState() {
		return 0;
	}
	
	
	public void printAcutalState() {
		System.out.println("Empty Place");
	}
	
	
	@Override
	public void donInput(int index, float value) {
		// just do nothing
	}
	
	
	@Override
	public LinkedHashMap<Integer, CalcVector> getHistoryStates() {
		return new LinkedHashMap<Integer, CalcVector>();
	}
}
