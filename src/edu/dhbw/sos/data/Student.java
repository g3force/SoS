/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 14, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.data;

/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class Student {
	// just for testing, can not be done this way, because it should be dynamic
	private StudentState[]	ss	= { StudentState.LOUD, StudentState.DISTRACTED };
	
	
	public StudentState[] getSs() {
		return ss;
	}
	
	
	public void setSs(StudentState[] ss) {
		this.ss = ss;
	}
}
