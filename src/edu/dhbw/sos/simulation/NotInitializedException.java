/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): dirk
 * 
 * *********************************************************
 */
package edu.dhbw.sos.simulation;

/**
 * This Exception should be used for Singleton classes, which do not initialize themselves.
 * 
 * If getInstance is called without having the init function called, this Exception should be thrown.
 * @author dirk
 * 
 */
public class NotInitializedException extends RuntimeException {
	private static final long	serialVersionUID	= -3099897806604277720L;
	
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * Calls constructor from super class (RuntimeException).
	 * 
	 * @author dirk, but copied from DanielAl
	 */
	public NotInitializedException() {
		super();
	}
	
	
	/**
	 * 
	 * Calls constructor from super class (RuntimeException) with a String to set the Exception Message.
	 * 
	 * @param s
	 * @author dirk, but copied from DanielAl
	 */
	public NotInitializedException(String s) {
		super(s);
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
