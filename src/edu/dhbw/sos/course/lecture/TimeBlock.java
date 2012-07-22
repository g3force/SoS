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


/**
 * The TimeBlock class is a single Block in a Lecture. It provides Information about the length and the type
 * 
 * @author andres
 * 
 */
public class TimeBlock {
	// private static final Logger logger = Logger.getLogger(TimeBlock.class);
	private int					len;
	private BlockType			type;
	public static final int	MIN_LEN	= 10;
	public static final int	STEP		= 5;
	
	
	/**
	 * Constructor with type and length.
	 * @param _len Length of TimeBlock
	 * @param _type type of TimeBlock (--> Enum BlockType)
	 * @author NicolaiO, andres
	 */
	public TimeBlock(int _len, BlockType _type) {
		setLen(_len);
		setType(_type);
	}
	
	
	/**
	 * @return the length
	 */
	public int getLen() {
		return len;
	}
	
	
	/**
	 * @param len the length to set
	 */
	public void setLen(int len) {
		if (len < MIN_LEN) {
			this.len = MIN_LEN;
			new AssertionError("Tried to set len to " + len + ", but MIN_LEN is " + MIN_LEN
					+ ". This should be catched earlier!", null);
		}
		this.len = len;
	}
	
	
	/**
	 * @return the type
	 */
	public BlockType getType() {
		return type;
	}
	
	
	/**
	 * @param type the type to set
	 */
	public void setType(BlockType type) {
		this.type = type;
	}
}
