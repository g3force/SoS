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

/**
 * The TiomeBlock class is a single Block in a Lecture. It provides Information about the length and the type
 * 
 * @author andres
 * 
 */
public class TimeBlock {
	private int			len;
	private BlockType	type;
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public TimeBlock(int _len, BlockType _type) {
		len = _len;
		type = _type;
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
