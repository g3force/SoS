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
 * This enum provides different Block types for a lecture. <br>
 * For each type a vektor called simFormulaFactors is set. This is used for simulation formula.<br>
 * 
 * @author andres
 * 
 */
public enum BlockType {
	theory(0),
	group(1),
	exercise(1),
	pause(2);
	
	private int	simFormulaFactors; 
	
	
	BlockType(int simFormulaFactors) {
		this.setSimFormulaFactors(simFormulaFactors);
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
}
