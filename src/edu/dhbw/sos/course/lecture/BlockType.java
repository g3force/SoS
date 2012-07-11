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
	
	private static final int	SIZE	= 4;
	private int		simFormulaFactors;
	private Color	color;
	
	
	BlockType(int simFormulaFactors, Color c) {
		this.setSimFormulaFactors(simFormulaFactors);
		this.setColor(c);
		
	}
	
	
	public int getYLocation(int maxHeight) {
		return maxHeight / SIZE * this.ordinal();
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
	 *         bei Beschwerden bitte an mich wenden ;)
	 * @author dirk
	 */
	public EInfluenceType getEinfluenceType() {
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
