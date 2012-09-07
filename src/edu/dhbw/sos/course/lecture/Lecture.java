/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 11, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course.lecture;

import java.util.Calendar;
import java.util.Date;


/**
 * This class holds the information about a lecture. It contains a start date, a length of the lecture and a list of
 * several timeblocks
 * 
 * @author andres
 * 
 */
public class Lecture {
	
	private Date			start;
	private long			timeInMiliSec;
	private TimeBlocks	timeBlocks;
	
	
	public Lecture(Date start, TimeBlocks timeBlocks) {
		setStart(start);
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
		setStartInMilis();
	}
	
	
	/**
	 * Returns the start time in miliseconds. This is the time without any Days and months, etc.Only Hours, Minutes,
	 * Seconds and Miliseconds.
	 * @return startInMilis
	 * @author andres
	 */
	public long getStartInMilis() {
		return timeInMiliSec;
	}
	
	
	private void setStartInMilis() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		long hoursInMiliSec = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;
		long minsInMiliSec = cal.get(Calendar.MINUTE) * 60 * 1000;
		long secsInMiliSec = cal.get(Calendar.SECOND) * 1000;
		long miliSec = cal.get(Calendar.MILLISECOND);
		
		timeInMiliSec = hoursInMiliSec + minsInMiliSec + secsInMiliSec + miliSec;
	}
	
	
	/**
	 * @return the length
	 */
	public int getLength() {
		return timeBlocks.getTotalLength();
	}
	
	
	/**
	 * @return the timeBlocks
	 */
	public TimeBlocks getTimeBlocks() {
		return timeBlocks;
	}
	
	
	/**
	 * @param timeBlocks the timeBlocks to set
	 */
	public void setTimeBlocks(TimeBlocks timeBlocks) {
		this.timeBlocks = timeBlocks;
		// for (TimeBlock timeBlock : timeBlocks) {
		// addTimeBlock(timeBlock);
		// }
	}
	
	
}
