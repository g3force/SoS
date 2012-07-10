/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Jun 2, 2012
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.sos.simulation;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.LinkedList;

import org.junit.Test;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.Lecture;
import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.course.suggestions.SuggestionManager;
import edu.dhbw.sos.helper.CalcVector;


/**
 * This class tests functions from the SimController class
 * 
 * @author dirk
 */
public class SimControllerTest {
	
	/**
	 * Test method for {@link edu.dhbw.sos.simulation.SimController#simulationStep()}.
	 */
	@Test
	public void testSimulationStep() {
		SuggestionManager sugMngr = new SuggestionManager();
		for(int i=0; i<10; i++) { //test for 10 different dummy courses
			
			LinkedList<TimeBlock> tblist = new LinkedList<TimeBlock>();
			tblist.add(new TimeBlock(2000000, BlockType.theory));
			tblist.add(new TimeBlock(2000000, BlockType.exercise));
			tblist.add(new TimeBlock(2000000, BlockType.group));
			tblist.add(new TimeBlock(2000000, BlockType.pause));
			
			for(int j=0; j<4; j++) {
				Course course = new Course("test");
				SimController simCon = new SimController(course, sugMngr);
				LinkedList<TimeBlock> tempList = new LinkedList<TimeBlock>();
				tempList.add(tblist.get(j));
				course.setLecture(new Lecture(new Date(), new TimeBlocks(tempList)));
				// save the state to the beginning
				CalcVector[][] start = new CalcVector[course.getStudents().length][course.getStudents()[0].length];
				int x = 0;
				int y = 0;
				for (IPlace[] row : course.getStudents()) {
					for (IPlace s : row) {
						if (s instanceof Student) {
							start[y][x] = s.getActualState().clone();
						}
						x++;
					}
					x = 0;
					y++;
				}
				assertEquals("Start time is 0", simCon.getCurrentTime(), 0);
				while (simCon.getCurrentTime() < 1000 * 60 * 30) { // half an hour
					simCon.simulationStep();
				}
				assertEquals("Time block at the end does not match", course.getLecture().getTimeBlocks()
						.getTimeBlockAtTime(simCon.getCurrentTime()).getType().toString(), tblist.get(j).getType().toString());
				// compare
				x = 0;
				y = 0;
				for (IPlace[] row : course.getStudents()) {
					for (IPlace s : row) {
						if (s instanceof Student) {
							switch (j) {
								case 0: // theory
									double[] exspected = { 45, 15, -30, -30 };
									double[] delta = { 15, 5, 10, 10 };
									testAllAttributes("Theory", start[y][x], s.getActualState(), exspected, delta);
									break;
								case 1: // exercise
									double[] exspected2 = { -15, -25, 45, 20 };
									double[] delta2 = { 5, 5, 15, 10 };
									testAllAttributes("Exercise", start[y][x], s.getActualState(), exspected2, delta2);
									break;
								case 2: // group
									double[] exspected3 = { -40, 15, 35, 37 };
									double[] delta3 = { 10, 5, 15, 13 };
									testAllAttributes("Group", start[y][x], s.getActualState(), exspected3, delta3);
									break;
								case 3: // pause
									double[] exspected4 = { -62, -20, 45, 40 };
									double[] delta4 = { 13, 10, 15, 10 };
									testAllAttributes("Pause", start[y][x], s.getActualState(), exspected4, delta4);
									break;
							}
						}
						x++;
					}
					x = 0;
					y++;
				}
			}
		}
	}
	
	
	private void testAllAttributes(String type, CalcVector start, CalcVector actual, double[] exspected, double[] delta) {
		
		for (int i = 0; i < 4; i++) {
			double addValue = exspected[i];
			double oldState = start.getValueAt(i);
			if (addValue > 0) {
				exspected[i] = oldState + (float) (addValue * ((100 - oldState) * 2 / 100));
			} else {
				exspected[i] = oldState + (float) (addValue * ((oldState) * 2 / 100));
			}
			if (exspected[i] < 0) {
				exspected[i] = 0;
			}
			if (exspected[i] > 100) {
				exspected[i] = 100;
			}
		}

		// if (start.getValueAt(k) + exspected[k] < 0)
		// exspected[k] = 0;
		// else if (start.getValueAt(k) + exspected[k] > 100)
		// exspected[k] = 100;
		// else
		// exspected[k] = start.getValueAt(k) + exspected[k];
		String info = " start: " + start.toString() + " but was: " + actual.toString();
		assertEquals(type + " test (Tiredness)" + info, exspected[0], actual.getValueAt(0), delta[0]);
		assertEquals(type + " test (Loudness)" + info, exspected[1], actual.getValueAt(1), delta[1]);
		assertEquals(type + " test (Attention)" + info, exspected[2], actual.getValueAt(2), delta[2]);
		assertEquals(type + " test (Quality)" + info, exspected[3], actual.getValueAt(3), delta[3]);
	}
}
