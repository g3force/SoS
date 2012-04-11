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
	
	
	Lecture(Date start, long length, LinkedList<TimeBlock> timeBlocks) {
		this.start = start;
		this.length = length;
		this.timeBlocks = timeBlocks;
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
		this.timeBlocks = timeBlocks;
	}
	
	/**
	 * @param timeBlock the timeBlock to add
	 * @param 
	 */
	public void addTimeBlock(TimeBlock timeBlock) {
		this.timeBlocks.add(timeBlock);
	}
	
	
}
