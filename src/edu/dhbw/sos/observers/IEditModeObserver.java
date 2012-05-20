/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 22, 2012
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.sos.observers;

/**
 * Observer Interface for entering and exiting the edit mode
 * 
 * @author NicolaiO
 * 
 */
public interface IEditModeObserver {
	void enterEditMode();
	void exitEditMode();
}
