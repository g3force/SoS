/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 16, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course.lecture;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.Course;


/**
 * This class holds is a LinkedListof type TimeBlock which hold as all TimeBlocks of a lecture and gives methods for
 * adding, moving and removing blocks.
 * @author NicolaiO, andres
 * 
 */
public class TimeBlocks implements Iterable<TimeBlock> {
	private static final Logger	logger	= Logger.getLogger(TimeBlocks.class);
	private LinkedList<TimeBlock>	timeblocks;
	
	
	/**
	 * Constructor with non-empty LinkedList
	 * @author NicolaiO, andres
	 */
	public TimeBlocks(LinkedList<TimeBlock> timeblocks) {
		this.timeblocks = timeblocks;
	}
	
	
	public boolean add(TimeBlock timeblock) {
		return timeblocks.add(timeblock);
	}
	
	
	public boolean remove(Object timeblock) {
		return timeblocks.remove(timeblock);
	}
	
	
	@Override
	public Iterator<TimeBlock> iterator() {
		return timeblocks.iterator();
	}
	
	
	public int size() {
		return timeblocks.size();
	}
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @param currentCourse
	 * @return
	 * @author NicolaiO
	 */
	public int indexOf(Course currentCourse) {
		return timeblocks.indexOf(currentCourse);
	}
	
	
	public void clear() {
		timeblocks.clear();
	}
	

	public TimeBlock get(int i) {
		return timeblocks.get(i);
	}


	/**
	 * Constructor with empty LinkedList
	 * @author NicolaiO, andres
	 */
	public TimeBlocks() {
		timeblocks = new LinkedList<TimeBlock>();
	}
	
	
	/**
	 * Returns the total length of all TimeBlocks of an Lecture
	 * 
	 * @return total length of all TimeBlocks
	 * @author NicolaiO, andres
	 */
	public int getTotalLength() {
		int sum = 0;
		for (TimeBlock tb : timeblocks) {
			sum += tb.getLen();
		}
		return sum;
	}
	
	
	/**
	 * Adds a TimeBlock at the the given position and cuts all blocks after that are affected, so that the total length
	 * isn't changed.
	 * 
	 * @param timeBlock the timeBlock to add
	 * @param pos at which time to start (in respect to the start time)
	 */
	public void addTimeBlockBetween(TimeBlock timeBlock, int pos) {
		int index = getIndexAtPos(pos);
		// int length = timeBlock.getLen();
		timeblocks.add(index, timeBlock);
		trimTBlen(index, timeblocks.get(index).getLen());
	}
	
	
	/**
	 * This method trims the next lecture of the given index with the value of lengthToTrim. <br>
	 * If lengthToTrim is greater then the next block, it will be deleted and the method calls itself.
	 * 
	 * @param index
	 * @param lengthToTrim
	 * @author andres
	 */
	private void trimTBlen(int index, int lengthToTrim) {
		// Wenn das aktuelle Element das letzte ist, wird die verbleibende Zeit zur Gesamtlänge hinzugefügt
		if (timeblocks.size() == (index + 1)) {
			// length += lengthToTrim;
			return;
		}
		int lengthNext = timeblocks.get(index + 1).getLen();
		if (lengthNext - lengthToTrim >= 0) {
			// wenn der verschobene Block - neuer Block größer als 0 min ist, dann soll der verschobene Block verkürzt
			// werden
			timeblocks.get(index + 1).setLen(lengthNext - lengthToTrim);
		} else {
			// Wenn verschobene Block Länge - neue Block Länge kleiner 0 ist wird der verschobene Blcok gelöscht
			timeblocks.remove(index + 1);
			trimTBlen(index, lengthToTrim - lengthNext);
		}
		return;
	}
	
	
	/**
	 * Returns the index of the linked List element which is contains the time position pos.
	 * 
	 * step 0 1 2
	 * pos 12 12 12
	 * index 0 1 2
	 * time 1 10 13
	 * 
	 * @param pos
	 * @return
	 * @author andres
	 */
	private int getIndexAtPos(int pos) {
		int index = 0;
		int time = timeblocks.get(index).getLen();
		while (pos > time) {
			index++;
			time += timeblocks.get(index).getLen();
		}
		return index;
	}
	
	
	/**
	 * Get the TimeBlock for a specific time position.
	 * 
	 * @param pos
	 * @return
	 * @author andres
	 */
	public TimeBlock getTimeBlockAtTime(int pos) {
		TimeBlock result;
		try {
			result = timeblocks.get(getIndexAtPos(pos));
		} catch (IndexOutOfBoundsException e) {
			logger.warn("No TimeBlock. Create Theory Block of length 1");
			result = new TimeBlock(1, BlockType.theory);
		}
		
		return result;
	}
}
