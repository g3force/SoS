/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 24, 2012
 * Author(s): bene
 * 
 * *********************************************************
 */
package edu.dhbw.sos.helper;

/**
 * This is only a helper object to make the stored xml code of Suggestion objects better human readable.
 * 
 * @author bene
 * 
 */
public class XMLParam implements Cloneable {
	private String	name;
	private float	influence;
	private float	rangeLowerEnd;
	private float	rangeUpperEnd;
	
	
	public XMLParam(String name, float influence, float rangeLowerEnd, float rangeUpperEnd) {
		this.name = name;
		this.influence = influence;
		this.rangeLowerEnd = rangeLowerEnd;
		this.rangeUpperEnd = rangeUpperEnd;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public float getInfluence() {
		return influence;
	}
	
	
	public float getRangeLowerEnd() {
		return rangeLowerEnd;
	}
	
	
	public float getRangeUpperEnd() {
		return rangeUpperEnd;
	}
}
