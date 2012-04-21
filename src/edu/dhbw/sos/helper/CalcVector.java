/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 6, 2012
 * Author(s): bene
 * 
 * *********************************************************
 */
package edu.dhbw.sos.helper;

import org.apache.log4j.Logger;


/**
 * This Vector object contains the influencing factors needed during the simulation.
 * 
 * @author bene
 * 
 */
public class CalcVector implements Cloneable {
	private float[]	vector;
	private static final Logger	logger	= Logger.getLogger(CalcVector.class);
			
	
	/**
	 * Creates a new CalcVector with the size specified by initSize and all values set to 0.
	 * 
	 * @param initSize
	 * @author dirk, bene
	 */
	public CalcVector(int initSize) {
		this.vector = new float[initSize];
		for(int i=0; i<initSize; i++)
			this.vector[i] = 0;
	}
	/**
	 * Creates a new CalcVector with the values specified by values.
	 * 
	 * @param values
	 * @author bene
	 */
	public CalcVector(float[] values) {
		this.vector = new float[values.length];
		for(int i=0; i < this.vector.length; i++)
			this.vector[i] = values[i];
	}
	
	/**
	 * Returns the value at the specified index. (First element at index 0, last at size()-1)
	 * 
	 * @param index
	 * @return The value at index
	 * @author bene
	 */
	public float getValueAt(int index) {
		if (index >= this.vector.length) {
			throw new IllegalArgumentException("Cannot access index outside of vector");
		}
		return this.vector[index];
	}
	/**
	 * Sets the value at the specified index. (First element at index 0, last at size()-1)
	 * 
	 * @param index
	 * @author bene
	 */
	public void setValueAt(int index, float value) {
		if (index >= this.vector.length) {
			throw new IllegalArgumentException("Cannot access index outside of vector");
		}
		this.vector[index] = value;
	}
	
	/**
	 * Returns the size (number of items) of this CalcVector.
	 * 
	 * @return
	 * @author bene
	 */
	public int size() {
		return this.vector.length;
	}
	
	
	/**
	 * Multiplies this CalcVector object with a constant of type int.
	 * 
	 * @param constant
	 * @return
	 * @author bene
	 */
	public CalcVector multiplyWithInteger(int constant) {
		for (int i = 0; i < this.size(); i++) {
			this.vector[i] *= constant;
		}
		return this;
	}
	
	
	/**
	 * Multiplies this CalcVector object with a constant of type float.
	 * 
	 * @param constant
	 * @return
	 * @author bene
	 */
	public CalcVector multiplyWithFloat(float constant) {
		for (int i = 0; i < this.size(); i++) {
			this.vector[i] *= constant;
		}
		return this;
	}
	/**
	 * Multiplies this CalcVector object with a constant of type double.
	 * NOTE: this allows only calling this function with double. Calculations and result are float.
	 * 
	 * @param constant
	 * @return
	 * @author bene
	 */
	public CalcVector multiplyWithDouble(double constant) {
		return this.multiplyWithFloat((float)constant);
	}
	/**
	 * Multiplies this CalcVector object with another CalcVector.
	 * 
	 * @param constant
	 * @return
	 * @author bene
	 */
	public CalcVector multiplyWithVector(CalcVector v) {
		if (v.size() != this.size()) {
			throw new IllegalArgumentException("Cannot multiply vectors of different sizes.");
		}
		for (int i = 0; i < this.size(); i++) {
			this.vector[i] *= v.vector[i];
		}
		return this;
	}
	
	/**
	 * Multiplies this CalcVector object with a Matrix object.
	 * 
	 * @param constant
	 * @return
	 * @author bene
	 */
	public CalcVector multiplyWithArray(float[][] a) {
		if (a.length != this.size() || a[0].length != this.size()) {
			throw new IllegalArgumentException("Cannot multiply vectors of different sizes.");
		}
		for (int i = 0; i < this.size(); i++) {
			float value = 0;
			for (int j = 0; j < this.size(); j++) {
				value += this.getValueAt(i) * a[i][j];
			}
			this.vector[i] = value;
		}
		return this;
	}
	
	
	/**
	 * Adds the CalcVector v to this CalcVector object.
	 * 
	 * @param v
	 * @return
	 * @author bene
	 */
	public CalcVector addCalcVector(CalcVector v) {
		if (v.size() != this.size()) {
			throw new IllegalArgumentException("Can not add vectors with different sizes.");
		}
		for (int i = 0; i < this.size(); i++) {
			this.vector[i] += v.getValueAt(i);
		}
		return this;
	}
	
	
	/**
	 * Creates an exact clone of this CalcVector with the same values.
	 */
	public CalcVector clone() {
		CalcVector result = new CalcVector(vector.length);
		for (int i = 0; i < this.size(); i++) {
			result.vector[i] = this.vector[i];
		}
		return result;
	}
	
	
	// debug method
	public void printCalcVector() {
		printCalcVector("");
	}
	
// debug method
	public void printCalcVector(String message) {
		String out=message+": ";
		for (int i = 0; i < this.size(); i++) {
			out+=this.vector[i]+" / ";
		}
		logger.info(out.subSequence(0, out.length()-3));
	}
}
