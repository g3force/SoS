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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;

import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.CalcVector;


/**
 * This class tests the Course class
 * 
 * @author dirk
 * 
 */
public class CourseTest {
	Course	course	= null;
	
	
	@Before
	public void beforeEachTest() {
		course = new Course("test");
	}
	
	
	/**
	 * Test method for {@link edu.dhbw.sos.course.Course#donInputQueue(int, float)}.
	 * test dummy course
	 */
	@Test
	public void testCourse() {
		// test each student
		for (IPlace[] row : course.getStudents()) {
			for (IPlace is : row) {
				if (is instanceof Student) {
					Student s = (Student) is;
					for (int i = 0; i < s.getActualState().size(); i++) { // check correct actual state and change vector
						if (s.getActualState().getValueAt(i) < 0 || s.getActualState().getValueAt(i) > 100)
							fail("actual state not in range after initialization " + s.getActualState().getValueAt(i));
						if (s.getChangeVector().getValueAt(i) < 0.5 || s.getChangeVector().getValueAt(i) > 1.5)
							fail("change vector not in range after initialization " + s.getChangeVector().getValueAt(i));
						assertEquals("existing history states", s.getHistoryStates().size(), 0);
					}
				}
			}
		}
		// lecture
		assertNotNull("lecture", course.getLecture());
		assertNotNull("lecture blocks", course.getLecture().getTimeBlocks());
		assertNotNull("lecture actual block", course.getLecture().getTimeBlocks().getTimeBlockAtTime(0));
	}
	
	
	/**
	 * Test method for {@link edu.dhbw.sos.course.Course#donInputQueue(int, float)}.
	 */
	@Test
	public void testDonInputQueue() {
		fail("Not yet implemented");
	}
	
	
	/**
	 * Test method for {@link edu.dhbw.sos.course.Course#suggestionInput(edu.dhbw.sos.helper.CalcVector)}.
	 * Process:
	 * 1. save student (0,0) as example student
	 * 2. clone its vectors
	 * 3. simulate an input of 1 (for all values)
	 * 4. check if the vectors changed
	 * 5. check if the vectors changed not too much
	 * 6. simulate a lot of arbitrary inputs
	 * 7. check if the values are still in range
	 */
	@Test
	public void testSuggestionInput() {
		Student s = (Student) course.getPlace(0, 0);
		CalcVector as = s.getActualState().clone();
		CalcVector cv = s.getChangeVector().clone();
		CalcVector testInput = new CalcVector(as.size());
		
		// test one single suggestion input
		for (int i = 0; i < testInput.size(); i++)
			testInput.setValueAt(i, 1);
		course.suggestionInput(testInput);
		for (int i = 0; i < testInput.size(); i++) {
			assertEquals("after 1 input, actual state should only change by max of 2", as.getValueAt(i), s
					.getActualState().getValueAt(i), 2);
			assertNotSame("after 1 input, suggestion Input should change the actual state", as.getValueAt(i), s
					.getActualState().getValueAt(i));
			assertEquals("after 1 input, change vector should only change by max of 0.1", cv.getValueAt(i), s
					.getChangeVector().getValueAt(i), 0.1);// expected: increased by 0.1
			assertNotSame("after 1 input, suggestion Input should change the change vector", cv.getValueAt(i), s
					.getChangeVector().getValueAt(i));
		}
		
		// test a lot of suggestion inputs
		for (int j = 0; j < 1000; j++) {
			for (int i = 0; i < testInput.size(); i++)
				testInput.setValueAt(i, (float) Math.random() * 10); // random between 0 and 10
			course.suggestionInput(testInput);
		}
		for (int i = 0; i < testInput.size(); i++) {
			assertEquals("after 1000, actual state has only changed by a max of 100", as.getValueAt(i), s.getActualState()
					.getValueAt(i), 100);
			if (s.getActualState().getValueAt(i) < 0 || s.getActualState().getValueAt(i) > 100)
				fail("actual state not in range after 1000 suggestions");
			
			assertEquals("after 1000, change vector has only changed by a max of 100", cv.getValueAt(i), s
					.getChangeVector().getValueAt(i), 1);
			if (s.getChangeVector().getValueAt(i) < 0.5 || s.getChangeVector().getValueAt(i) > 1.5)
				fail("change vector not in range after 1000 suggestions");
		}
	}
	
	
	/**
	 * Test method for {@link edu.dhbw.sos.course.Course#donInput(int, float)}.
	 */
	@Test
	public void testDonInput() {
		fail("Not yet implemented");
	}
	
	
	/**
	 * Test method for {@link edu.dhbw.sos.course.Course#simulationStep(int)}.
	 */
	@Test
	public void testSimulationStep() {
		fail("Not yet implemented");
	}
	
	
	/**
	 * Test method for {@link edu.dhbw.sos.course.Course#setTime(int, int)}.
	 * Process:
	 * 1. simulate 1 step (time = 0)
	 * 2. set time to 5
	 * 3. simulate 1 step (time = 5)
	 * 4. check (2 history states, both not null and not the same for each student)
	 * 5. set time to 4
	 * 6. check (1 history state, time of only state is 0 and same as vector as before, actual state is a copy of history
	 * state (time = 0))
	 */
	@Test
	public void testSetTime() {
		course.simulationStep(0);
		course.setTime(0, 5);
		course.simulationStep(5);
		CalcVector[][] start = new CalcVector[course.getStudents().length][course.getStudents()[0].length];
		int x = 0;
		int y = 0;
		for (IPlace[] row : course.getStudents()) {
			for (IPlace s : row) {
				if (s instanceof Student) {
					LinkedHashMap<Integer, CalcVector> hs = s.getHistoryStates();
					assertEquals("History length (2)", 2, hs.size());
					assertNotNull("History state 0", hs.get(0));
					assertNotNull("History state 5", hs.get(5));
					assertNotSame("History state 0 and 5 are the same", hs.get(0), hs.get(5));
					start[y][x] = hs.get(0);
				}
				x++;
			}
			x = 0;
			y++;
		}
		course.setTime(5, 4); // last history state has to be deleted (is in future)
		
		y = 0;
		x = 0;
		for (IPlace[] row : course.getStudents()) {
			for (IPlace s : row) {
				if (s instanceof Student) {
					LinkedHashMap<Integer, CalcVector> hs = s.getHistoryStates();
					assertEquals("History length (1)", 1, hs.size()); // only one history state left (time = 0)
					assertNotNull("History state 0", hs.get(0)); // the first state should not be null
					assertNull("History state 5", hs.get(5)); // the fifth state should be deleted, so it should be null
					assertSame("state 0 is still state 0", start[y][x], hs.get(0)); // the first state should be still the
																											// same
					assertNotSame("Actual State equals state 0", start[y][x], s.getActualState()); // has to be clone
					for (int i = 0; i < start[y][x].size(); i++) {
						assertEquals(start[y][x].getValueAt(i), s.getActualState().getValueAt(i), 0);// acutal state should be
																																// generated by cloning
																																// hist state vector
					}
				}
				x++;
			}
			x = 0;
			y++;
		}
	}
}
