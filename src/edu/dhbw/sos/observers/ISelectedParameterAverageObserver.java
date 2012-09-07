/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 22, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.observers;

/**
 * Observer Interface for the selected parameter average in right panel
 * 
 * @author andres
 * 
 */
public interface ISelectedParameterAverageObserver {
	void updateSelectedParameterAverage(int parameterindex);
}
