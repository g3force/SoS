/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 13, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui;

/**
 * Interface for objects that are updateable.
 * This is especially for GUI components, when something changed
 * 
 * @author NicolaiO
 * 
 */
public interface IUpdateable {
	/**
	 * This method should call any methods, that will update
	 * the GUI and its objects.
	 * 
	 * @author NicolaiO
	 */
	public void update();
}
