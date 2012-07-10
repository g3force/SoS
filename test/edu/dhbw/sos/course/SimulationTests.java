/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: May 14, 2012
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.sos.course;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.dhbw.sos.simulation.SimControllerTest;


/**
 * this suite contains all tests concerning the simulation
 * 
 * @author dirk
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ CourseTest.class, SimControllerTest.class })
public class SimulationTests {
	
}
