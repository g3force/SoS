/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 24, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.observers;


/**
 * This interface is used to inform interested objects by changes of the currentTime, if it is changed by a internal
 * object like the SimController. <br>
 * See also ITimeGUIObserver for time changes made by GUI components.
 * @author andres
 * 
 */
public interface ITimeObserver {
	void timeChanged(int time);
}
