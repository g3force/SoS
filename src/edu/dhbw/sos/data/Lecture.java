/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 11, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.data;

import java.util.Date;
import java.util.LinkedList;


/**
 * This class holds the information about a lecture. It contains a start date, a length of the lecture and a list of
 * several timeblocks
 * 
 * @author andres
 * 
 */
public class Lecture {
	
	private Date						start;
	private long						length;
	private LinkedList<TimeBlock>	timeBlocks;
	
	
	Lecture(Date start, long length) {
		this.start = start;
		this.length = length;
		this.timeBlocks = new LinkedList<TimeBlock>();
	}
	
	
	Lecture(Date start, LinkedList<TimeBlock> timeBlocks) {
		this.start = start;
		setTimeBlocks(timeBlocks);
	}
	
	
	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}
	
	
	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}
	
	
	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}
	
	
	/**
	 * @param length the length to set
	 */
	public void setLength(long length) {
		this.length = length;
	}
	
	
	/**
	 * @return the timeBlocks
	 */
	public LinkedList<TimeBlock> getTimeBlocks() {
		return timeBlocks;
	}
	
	
	/**
	 * @param timeBlocks the timeBlocks to set
	 */
	public void setTimeBlocks(LinkedList<TimeBlock> timeBlocks) {
		// this.timeBlocks = timeBlocks;
		length = 0;
		for (TimeBlock timeBlock : timeBlocks) {
			addTimeBlock(timeBlock);
		}
	}
	
	
	/**
	 * Adds a TimeBlock at the end and append length of lecture
	 * 
	 * @param timeBlock the timeBlock to add
	 */
	public void addTimeBlock(TimeBlock timeBlock) {
		this.timeBlocks.add(timeBlock);
		setLength(length + timeBlock.getLen());
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
		int length = timeBlock.getLen();
		timeBlocks.add(index, timeBlock);
		trimTBlen(index, timeBlocks.get(index).getLen());
		setLength(length + timeBlock.getLen());
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
		if (timeBlocks.size() == (index + 1)) {
			length += lengthToTrim;
			return;
		}
		int lengthNext = timeBlocks.get(index + 1).getLen();
		if (lengthNext - lengthToTrim >= 0) {
			// wenn der verschobene Block - neuer Block größer als 0 min ist, dann soll der verschobene Block verkürzt
			// werden
			timeBlocks.get(index + 1).setLen(lengthNext - lengthToTrim);
		} else {
			// Wenn verschobene Block Länge - neue Block Länge kleiner 0 ist wird der verschobene Blcok gelöscht
			timeBlocks.remove(index + 1);
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
	public int getIndexAtPos(int pos) {
		int index = 0;
		int time = timeBlocks.get(index).getLen();
		while (pos > time) {
			index++;
			time += timeBlocks.get(index).getLen();
		}
		return index;
	}
	
	/**
	 * 
	 * Get the TimeBlock for a specific time position. 
	 * 
	 * @param pos
	 * @return
	 * @author andres
	 */
	public TimeBlock getTimeBlock(int pos) {
		return timeBlocks.get(getIndexAtPos(pos));
	}
	
}
