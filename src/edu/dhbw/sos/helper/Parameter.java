/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 6, 2012
 * Author(s): bene
 * 
 * *********************************************************
 */
package edu.dhbw.sos.helper;

/**
 * The class Parameter is used to map a string (which gives the type of parameter of the student like fatigue,
 * attention etc.) to the corresponding double value
 * 
 * @author bene
 * 
 */
public class Parameter {
	private String	type;
	private float	value;
	
	
	public Parameter(String type, float value) {
		this.type = type;
		this.value = value;
	}
	
	
	public String getType() {
		return type;
	}
	
	
	public void setValue(float value) {
		this.value = value;
	}
	
	
	public float getValue() {
		return value;
	}
}