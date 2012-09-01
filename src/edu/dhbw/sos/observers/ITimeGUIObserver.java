/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: May 1, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.observers;

/**
 * This interface is used to inform interested objects by changes of the currentTime, if it is changed by a GUI
 * object like the TImeMarkerBlock. <br>
 * See also ITimeObserver for time changes made by other components.
 * @author andres
 * 
 */
public interface ITimeGUIObserver {
	void timeChanged(int time);
}
