/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 28, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.observers;

/**
 * Observer Interface for starting/stopping simulation
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public interface ISimulation {
	void simulationStopped();
	
	
	void simulationStarted();
}
