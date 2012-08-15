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

import java.awt.Color;

import edu.dhbw.sos.course.influence.EInfluenceType;


/**
 * This enum provides different Block types for a lecture. <br>
 * For each type a vektor called simFormulaFactors is set. This is used for simulation formula.<br>
 * 
 * @author andres
 * 
 */
public enum BlockType {
	theory(0, Color.red),
	group(1, Color.blue),
	exercise(1, Color.yellow),
	pause(2, Color.green);
	
	public static final int		SIZE		= 4;
	private static final int	OFFSET	= 5;
	private int		simFormulaFactors;
	private Color	color;
	
	
	/**
	 * 
	 * @param simFormulaFactors sim factor for simulation calculations
	 * @param color Color of the block
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	BlockType(int simFormulaFactors, Color color) {
		this.setSimFormulaFactors(simFormulaFactors);
		this.setColor(color);
		
	}


	/**
	 * Get type of block by number
	 * 
	 * @param number number between 0 and 3
	 * @return BlockType
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public static BlockType getInstance(int number) {
		switch (number) {
			case 0:
				return theory;
			case 1:
				return group;
			case 2:
				return exercise;
			case 3:
				return pause;
		}
		System.err.println("getInstance(" + number + ") called");
		assert (false);
		return null;
	}

	
	/**
	 * Get the y location depending on the the blocktype, offset, size and maxHeight
	 * 
	 * @param maxHeight Maximal available size for all blocks
	 * @return Y location
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public int getYLocation(int maxHeight) {
		return ((maxHeight - 2 * OFFSET) / SIZE * this.ordinal()) + OFFSET;
	}
	

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	
	/**
	 * @param color the color to set
	 */
	private void setColor(Color color) {
		this.color = color;
	}
	
	
	/**
	 * @return the simFormulaFactors
	 */
	public int getSimFormulaFactors() {
		return simFormulaFactors;
	}
	
	
	/**
	 * @param refreshSpirit the refreshSpirit to set
	 */
	private void setSimFormulaFactors(int simFormulaFactors) {
		this.simFormulaFactors = simFormulaFactors;
	}
	
	
	/**
	 * @return the type to compare with EInfluence type
	 *         bei Beschwerden bitte an mich wenden ;)
	 * @author dirk
	 */
	@Override
	public String toString() {
		switch (this) {
			case exercise:
				return "Exercise";
			case group:
				return "Group";
			case pause:
				return "Pause";
			case theory:
				return "Theory";
			default:
				return "Unknown";
		}
	}
	
	
	/**
	 * @return the type to compare with EInfluence type
	 *         TODO Dirk bei Beschwerden bitte an mich wenden ;)
	 * @author dirk
	 */
	public EInfluenceType getEInfluenceType() {
		switch (this) {
			case exercise:
				return EInfluenceType.EXERCISE_REACTION;
			case group:
				return EInfluenceType.GROUP_REACTION;
			case pause:
				return EInfluenceType.BREAK_REACTION;
			case theory:
				return EInfluenceType.THEORY_REACTION;
			default:
				return EInfluenceType.UNKNOWN;
		}
	}
}
