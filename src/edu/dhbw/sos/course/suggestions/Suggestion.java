/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 23, 2012
 * Author(s): bene
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course.suggestions;

import edu.dhbw.sos.helper.CalcVector;


/**
 * The Suggestion object represents one suggestion.
 * Together with the suggestion text, a name, its priority, the parameter ranges and the influence vector for simulation
 * step calculations.
 * 
 * Note: this class has a natural ordering that is inconsistent with equals meaning just because compareTo() returns 0
 * does not imply that the two objects compared are equal. It only states that both Suggestion objects have the same
 * priority.
 * 
 * @author bene
 * 
 */
public class Suggestion implements Comparable<Suggestion> {
	private String			message;
	private float[][]		range;		// [parametercount][2] to store low [i][0] and high [i][1] value
	private int				priority;
	private CalcVector	influence;
	
	
	/**
	 * Creates a new Suggestion object.
	 * 
	 * @param range The array which contains the information for which ranges of course parameters this Suggestion should
	 *           be displayed.
	 * @param message The message actually displayed in the GUI
	 * @param priority The priority of the Suggestion (required for sorting)
	 * @param influenceValues The values that determine how executing this Suggestion affects the simulation.
	 * @author bene
	 */
	public Suggestion(float[][] range, String message, int priority, float[] influenceValues) {
		this.message = message;
		this.range = range.clone();
		this.priority = priority;
		this.influence = new CalcVector(influenceValues);
	}
	
	
	/**
	 * Returns the message text of this Suggestion.
	 * 
	 * @return Message text of this Suggestion.
	 * @author bene
	 */
	public String getMessage() {
		return message;
	}
	
	
	/**
	 * Returns the low end of the range at the specified index.
	 * 
	 * @param index Index of the parameter.
	 * @return Low end of the range.
	 * @author bene
	 */
	public float getLowValueAt(int index) {
		if (index >= this.range.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return this.range[index][0];
	}
	
	
	/**
	 * Returns the high end of the range at the specified index.
	 * 
	 * @param index Index of the parameter.
	 * @return High end of the range.
	 * @author bene
	 */
	public float getHighValueAt(int index) {
		if (index >= this.range.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return this.range[index][1];
	}
	
	
	/**
	 * Returns true if paramValue is in the range at paramIndex or false if not.
	 * 
	 * @param paramIndex Index of the parameter.
	 * @param paramValue Value of the parameter for comparison to the range values.
	 * @return true if paramValue is in the range at paramIndex or false if not
	 * @author bene
	 */
	public boolean paramIsInRange(int paramIndex, float paramValue) {
		if (paramIndex >= this.range.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return this.range[paramIndex][0] <= paramValue && this.range[paramIndex][1] >= paramValue;
	}
	
	
	/**
	 * Returns the priority of this Suggestion.
	 * 
	 * @return Priority of this Suggestion
	 * @author bene
	 */
	public int getPriority() {
		return this.priority;
	}
	
	
	/**
	 * Returns the influence vector of this suggestion.
	 * 
	 * @return Influence CalcVector for use in simulation.
	 * @author bene
	 */
	public CalcVector getInfluenceVector() {
		return this.influence;
	}
	
	
	/**
	 * Compares this Suggestions priority with the Suggestion s' priority. Returns 0 if both are equal, a negative value
	 * if this Suggestions priority is lower than that of s and a positive value if this Suggestions priority is higher
	 * than that of s.
	 */
	@Override
	public int compareTo(Suggestion s) {
		return this.priority - s.priority;
	}
}
