/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 6, 2012
 * Author(s): bene
 * 
 * *********************************************************
 */
package edu.dhbw.sos.data;

/**
 * This enum type offers states a student can have. Every state has a value assigned of type integer.
 * 
 * Positive values stand for the state while negative values stand for the exact opposite e.g.the state LOUD with a
 * positive value indicates a loud student while with a negative value it indicates a quiet student.
 * 
 * @author bene
 * 
 */
public enum StudentState {
	LOUD(0), // student is loud (+) or quiet (-)
	DISTRACTED(0), // student is distracted (+) or attentive (-)
	TIRED(0); // student is tired (+) or awake (-)
	
	private int	value;
	
	
	StudentState(int value) {
		this.value = value;
	}
	
	
	public int getValue() {
		return this.value;
	}
	
	
	public void setValue(int value) {
		this.value = value;
	}
	
	
	public void addToValue(int amount) {
		this.value += amount;
	}
}
