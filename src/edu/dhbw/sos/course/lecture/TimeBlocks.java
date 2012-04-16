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
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class TimeBlocks extends LinkedList<TimeBlock> {
	private static final long	serialVersionUID	= -5463421486049533791L;
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public TimeBlocks(LinkedList<TimeBlock> timeblocks) {
		super(timeblocks);
	}
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public TimeBlocks() {
		super();
	}
	
	public int getTotalLength() {
		int sum = 0;
		for(TimeBlock tb : this) {
			sum += tb.getLen();
		}
		return sum;
	}
	
}
