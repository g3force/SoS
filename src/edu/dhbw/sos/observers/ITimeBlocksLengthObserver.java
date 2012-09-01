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
 * This Observer interface is used for notifying all interested objects of the total length of the lecture.
 * It will be triggered whenever the length of a lecture changed.
 * 
 * @author andres
 * 
 */

// currently used for PPaintAreaV2 to redraw the timemarkers and the PlanPanel to update the end time
// used also to verify the position of the timemarkerblock
public interface ITimeBlocksLengthObserver {
	void lengthChanged(int newLengthMin);
}
