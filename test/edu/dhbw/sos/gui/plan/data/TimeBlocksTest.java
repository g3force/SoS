/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Jul 10, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 *
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan.data;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;


/**
 * Tests the class TimeBlocks
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class TimeBlocksTest {
	@Before
	public void beforeEachTest() {
	}
	
	
	@Test
	public void testMoveTimeBlock() {
		TimeBlocks timeBlocks = new TimeBlocks();
		TimeBlock t1 = new TimeBlock(100, BlockType.exercise);
		TimeBlock t2 = new TimeBlock(200, BlockType.group);
		TimeBlock t3 = new TimeBlock(300, BlockType.pause);
		timeBlocks.add(t1);
		timeBlocks.add(t2);
		timeBlocks.add(t3);
		
		assertTrue(timeBlocks.get(0) == t1);
		assertTrue(timeBlocks.get(1) == t2);
		assertTrue(timeBlocks.get(2) == t3);
		
		timeBlocks.moveTimeBlock(t3, t1);
		
		assertTrue(timeBlocks.get(0) == t3);
		assertTrue(timeBlocks.get(1) == t1);
		assertTrue(timeBlocks.get(2) == t2);
	}
}
