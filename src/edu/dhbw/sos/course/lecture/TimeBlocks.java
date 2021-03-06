/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 16, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course.lecture;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;


/**
 * This class holds is a LinkedListof type TimeBlock which hold as all TimeBlocks of a lecture and gives methods for
 * adding, moving and removing blocks.
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>, andres
 * 
 */
public class TimeBlocks implements Iterable<TimeBlock> {
	private static final Logger	logger	= Logger.getLogger(TimeBlocks.class);
	private LinkedList<TimeBlock>	timeblocks;
	public static final TimeBlock	NULL_TIMEBLOCK	= new TimeBlock(0, BlockType.exercise);
	
	
	/**
	 * Constructor with non-empty LinkedList
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>, andres
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
	
	
	public int getLastBreak(int time) {
		// get the timeBlock at given time
		TimeBlock timeBlock = getTimeBlockAtTime(time);
		int result = time - getAddedLen(timeBlock);
		
		int i = timeblocks.lastIndexOf(timeBlock) - 1;
		while (i >= 0) {
			TimeBlock tb = timeblocks.get(i);
			if (tb.getType() == BlockType.pause) {
				break;
			}
			result += tb.getLen();
			i--;
		}
		
		return result;
	}


	/**
	 * Return the index of the given timeBlock
	 * 
	 * @param timeBlock
	 * @return index of timeBlock
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public int indexOf(TimeBlock timeBlock) {
		return timeblocks.indexOf(timeBlock);
	}
	
	
	public void clear() {
		timeblocks.clear();
	}
	
	
	public TimeBlock get(int i) {
		try {
			return timeblocks.get(i);
		} catch (IndexOutOfBoundsException e) {
			return NULL_TIMEBLOCK;
		}
	}
	
	
	/**
	 * Constructor with empty LinkedList
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>, andres
	 */
	public TimeBlocks() {
		timeblocks = new LinkedList<TimeBlock>();
	}
	
	
	/**
	 * Returns the total length of all TimeBlocks of an Lecture
	 * 
	 * @return total length of all TimeBlocks
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>, andres
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
			// TODO andres deutsch?! Wenn verschobene Block Länge - neue Block Länge kleiner 0 ist wird der verschobene
			// Blcok
			// gelöscht
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
			logger.warn("No TimeBlock. Create Theory Block of length 10");
			result = new TimeBlock(10, BlockType.theory);
		}
		
		return result;
	}
	
	
	/**
	 * Move a TimeBlock from one index to another
	 * 
	 * @param from
	 * @param to
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void moveTimeBlock(TimeBlock from, TimeBlock to) {
		this.timeblocks.add(timeblocks.indexOf(to), this.timeblocks.remove(timeblocks.indexOf(from)));
	}
	
	
	/**
	 * Returns the length from first block until start of given block (not including)
	 * 
	 * @param timeBlock
	 * @return
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public int getAddedLen(TimeBlock timeBlock) {
		int sum = 0;
		for (TimeBlock tb : timeblocks) {
			if (tb == timeBlock) {
				break;
			}
			sum += tb.getLen();
		}
		return sum;
	}
}
