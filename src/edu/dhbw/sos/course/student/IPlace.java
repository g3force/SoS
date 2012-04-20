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
 * TODO dirk, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author dirk
 * 
 */
public interface IPlace {
	CalcVector getActualState();
	
	
	void setActualState(CalcVector acutalState);
	
	
	int getAverageState();
	
	
	void printAcutalState();
	
	
	void donInput(int index, int value);
}
