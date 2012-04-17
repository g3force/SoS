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

import java.util.LinkedList;
import java.util.Vector;


/**
 * This Vector object contains the influencing factors needed during the simulation.
 * 
 * @author bene
 * 
 */
public class CalcVector implements Cloneable {
	private Vector<Parameter>	vector;
													
													
	/**
	 * Creates a new CalcVector object with all the Parameters defined by the List of Strings.
	 * The initial values of the Parameter objects are all set to 1.
	 * 
	 * @param arguments A List of all Parameters
	 * @author bene
	 */
	public CalcVector(LinkedList<String> arguments) {
		this.vector = new Vector<Parameter>(arguments.size());
		for (int i = 0; i < arguments.size(); i++) {
			this.vector.add(i, new Parameter(arguments.get(i), 1));
		}
	}
	public CalcVector(int initSize) {
		this.vector = new Vector<Parameter>(initSize);
	}
	/**
	 * Appends the Parameter object p to the end of this vector.
	 * 
	 * @param p
	 * @return
	 * @author bene
	 */
	public boolean addParamToVector(Parameter p) {
		return this.vector.add(p);
	}
	
	
	/**
	 * Returns the value of the Parameter object at the specified index.
	 * 
	 * @param index
	 * @return The value of the Parameter object at index
	 * @author bene
	 */
	public int getValueAt(int index) {
		return this.vector.get(index).getValue();
	}
	public void setValueAt(int index, int value) {
		this.vector.get(index).setValue(value);
	}
	
	
	/**
	 * Returns the type of the Parameter object at the specified index.
	 * 
	 * @param index
	 * @return The type of the Parameter object at index
	 * @author bene
	 */
	public String getTypeAt(int index) {
		return this.vector.get(index).getType();
	}
	
	
	/**
	 * Returns the size (number of items) of this CalcVector.
	 * 
	 * @return
	 * @author bene
	 */
	public int size() {
		return this.vector.size();
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
			this.vector.get(i).setValue(this.getValueAt(i) * constant);
		}
		return this;
	}
	
	
	/**
	 * Multiplies this CalcVector object with a constant of type double.
	 * 
	 * @param constant
	 * @return
	 * @author bene
	 */
	public CalcVector multiplyWithDouble(double constant) {
		for (int i = 0; i < this.size(); i++) {
			this.vector.get(i).setValue((int) (this.getValueAt(i) * constant));
		}
		return this;
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
			this.vector.get(i).setValue((int) (this.getValueAt(i) * v.getValueAt(i)));
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
	public CalcVector multiplyWithMatrix(Matrix m) {
		if (m.size() != this.size()) {
			throw new IllegalArgumentException("Cannot multiply vectors of different sizes.");
		}
		for (int i = 0; i < this.size(); i++) {
			int value = 0;
			for (int j = 0; j < this.size(); j++) {
				value += this.getValueAt(i) * m.getElementAt(i, j).getValue();
			}
			this.vector.get(i).setValue(value);
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
			this.vector.get(i).setValue(this.getValueAt(i) + v.getValueAt(i));
		}
		return this;
	}
	
	
	/**
	 * Creates an exact clone of this CalcVector with the same values.
	 */
	public CalcVector clone() {
		CalcVector result = new CalcVector(this.size());
		for (int i = 0; i < this.size(); i++) {
			result.vector.add(i, new Parameter(this.getTypeAt(i), this.getValueAt(i)));
		}
		return result;
	}
	
	
	// debug method
	public void printCalcVector() {
		for (int i = 0; i < this.size(); i++) {
			System.out.println(this.vector.get(i).getType() + ": " + this.vector.get(i).getValue());
		}
	}
}
