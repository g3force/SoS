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
 * interface for the places in the class, implemented by EmptyPlace and Student
 * 
 * @author dirk
 * 
 */
public interface IPlace {


	CalcVector getActualState();
	
	
	void setActualState(CalcVector acutalState);
	
	
	int getAverageState();
	
	
	void printAcutalState();
	
	
	void donInput(int index, float value);
	
	
	void addToStateVector(CalcVector addVector);
	
	
	void reset();
	
	
	public LinkedHashMap<Integer, CalcVector> getHistoryStates();
}
