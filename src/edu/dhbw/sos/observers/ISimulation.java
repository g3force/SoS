/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 28, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.observers;

/**
 * Observer Interface for starting/stopping simulation
 * 
 * @author NicolaiO
 * 
 */
public interface ISimulation {
	void simulationStopped();
	
	
	void simulationStarted();
}
