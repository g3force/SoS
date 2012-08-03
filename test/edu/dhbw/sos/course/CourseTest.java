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
	 * Test method for {@link edu.dhbw.sos.course.Course#setTime(int, int)}.
	 * Process:<br>
	 * simuliere 1 Schritt (time = 0) <br>
	 * speichere den Schritt zwischen<br>
	 * setze die Zeit auf 5<br>
	 * die fehlenden Schritte zwischen 0 und 5 werden automatisch simuliert (Anzahl der gespeicherten Zustände werden
	 * überprüft)<br>
	 * simuliere 1 Schritt (time = 6)<br>
	 * Überprüfe: 6 gespeicherte Zustände<br>
	 * Setze die Zeit auf 4<br>
	 * Überprüfe: 4 gespeicherte Zustände<br>
	 * Setze die Zeit auf 0<br>
	 * Überprüfe: 1 gespeicherter Zustand und dieser ist identisch mit dem ersten
	 */
	@Test
	public void testSetTime() {
		// simuliere 1 Schritt (time = 0), speichere den Schritt zwischen
		course.simulationStep(0);
		
		// setze die Zeit auf 5
		course.setTime(0, 5000);
		
		// die fehlenden Schritte zwischen 0 und 5 werden automatisch simuliert (Anzahl der gespeicherten Zustände werden
		// überprüft)
		int x = 0;
		int y = 0;
		for (IPlace[] row : course.getStudents()) {
			for (IPlace s : row) {
				if (s instanceof Student) {
					LinkedHashMap<Integer, CalcVector> hs = s.getHistoryStates();
					String log = "";
					for (int i : hs.keySet())
						log += i + " ";
					assertEquals("History length (5) values: " + log, 5, hs.size()); // 0, 1, 2, 3, 4, 5
				}
				x++;
			}
			x = 0;
			y++;
		}
		// simuliere 1 Schritt (time = 6), wird auf 5 gestzt, da 0 auch ein Zeitpunkt ist
		course.simulationStep(5000);
		
		// Überprüfe: 6 gespeicherte Zustände
		CalcVector[][] start = new CalcVector[course.getStudents().length][course.getStudents()[0].length];
		y = 0;
		x = 0;
		for (IPlace[] row : course.getStudents()) {
			for (IPlace s : row) {
				if (s instanceof Student) {
					LinkedHashMap<Integer, CalcVector> hs = s.getHistoryStates();
					String log = "";
					for (int i : hs.keySet())
						log += i + " ";
					assertEquals("History length (6) values: " + log, 6, hs.size()); // 0, 1, 2, 3, 4, 5
					assertNotNull("History state 0", hs.get(0));
					assertNotNull("History state 5", hs.get(5000));
					assertNotSame("History state 0 and 5 are the same", hs.get(0), hs.get(5000));
					start[y][x] = hs.get(0);
				}
				x++;
			}
			x = 0;
			y++;
		}

		// Setze die Zeit auf 4
		course.setTime(5000, 3000); // last history state has to be deleted (is in future)
		
		// Überprüfe: 4 gespeicherte Zustände
		y = 0;
		x = 0;
		for (IPlace[] row : course.getStudents()) {
			for (IPlace s : row) {
				if (s instanceof Student) {
					LinkedHashMap<Integer, CalcVector> hs = s.getHistoryStates();
					String log = "";
					for (int i : hs.keySet())
						log += i + " ";
					assertEquals("History length (4) values: " + log, 4, hs.size()); // 0, 1, 2, 3, 4
					assertNotNull("History state 0", hs.get(0)); // the first state should not be null
					assertNull("History state 4 is not null", hs.get(4000)); // the fifth state should be deleted, so it
																								// should be null // state
																						
				}
				x++;
			}
			x = 0;
			y++;
		}
		
		// Setze die Zeit auf 0
		course.setTime(4000, 0); // last history states has to be deleted (is in future)
		
		// Überprüfe: 1 gespeicherter Zustand und dieser ist identisch mit dem ersten
		y = 0;
		x = 0;
		for (IPlace[] row : course.getStudents()) {
			for (IPlace s : row) {
				if (s instanceof Student) {
					LinkedHashMap<Integer, CalcVector> hs = s.getHistoryStates();
					assertEquals("History length (1)", 1, hs.size()); // only one history state left (time = 0)
					assertNotNull("History state 0", hs.get(0)); // the first state should not be null
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
