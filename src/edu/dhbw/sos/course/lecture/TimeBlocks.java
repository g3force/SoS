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

import java.util.LinkedList;


/**
 * This class holds is a LinkedListof type TimeBlock wich hold as all TimeBlocks of a lecture and gives methods for
 * adding, moving and removing blocks.
 * @author NicolaiO, andres
 * 
 */
public class TimeBlocks extends LinkedList<TimeBlock> {
	private static final long	serialVersionUID	= -5463421486049533791L;
	
	
	/**
	 * Constructor with non-empty LinkedList
	 * @author NicolaiO, andres
	 */
	public TimeBlocks(LinkedList<TimeBlock> timeblocks) {
		super(timeblocks);
	}
	
	
	/**
	 * Constructor with empty LinkedList
	 * @author NicolaiO, andres
	 */
	public TimeBlocks() {
		super();
	}
	
	
	/**
	 * Returns the total length of all TimeBlocks of an Lecture
	 * 
	 * @return total length of all TimeBlocks
	 * @author NicolaiO, andres
	 */
	public int getTotalLength() {
		int sum = 0;
		for (TimeBlock tb : this) {
			sum += tb.getLen();
		}
		return sum;
	}
	
	
	/**
	 * Adds a TimeBlock at the end
	 * 
	 * @param timeBlock the timeBlock to add
	 */
	public void addTimeBlock(TimeBlock timeBlock) {
		this.add(timeBlock);
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
		this.add(index, timeBlock);
		trimTBlen(index, this.get(index).getLen());
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
		if (this.size() == (index + 1)) {
			// length += lengthToTrim;
			return;
		}
		int lengthNext = this.get(index + 1).getLen();
		if (lengthNext - lengthToTrim >= 0) {
			// wenn der verschobene Block - neuer Block größer als 0 min ist, dann soll der verschobene Block verkürzt
			// werden
			this.get(index + 1).setLen(lengthNext - lengthToTrim);
		} else {
			// Wenn verschobene Block Länge - neue Block Länge kleiner 0 ist wird der verschobene Blcok gelöscht
			this.remove(index + 1);
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
		int time = this.get(index).getLen();
		while (pos > time) {
			index++;
			time += this.get(index).getLen();
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
		return this.get(getIndexAtPos(pos));
	}
}
