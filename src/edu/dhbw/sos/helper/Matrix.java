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

import java.util.List;
import java.util.Vector;


/**
 * The Matrix object is used to store the influence of parameters on each other.
 * 
 * @author bene
 * 
 */
public class Matrix {
	private Vector<Vector<Parameter>>	matrix;
	
	
	/**
	 * Creates a new Matrix object. The array values defines the values of the rows and columns of the matrix which are
	 * matched with the parameter names in parameters.
	 * 
	 * SoS specific: the Matrix object does not ignore values at i, j for i==j. In order to get a correct result, the
	 * values at positions with i==j in the values parameter have to be 0 (or 1 ???).
	 * 
	 * @param parameters
	 * @param values
	 * @author bene
	 */
	public Matrix(List<String> parameters, int[][] values) {
		if (parameters == null) {
			throw new NullPointerException("The list of Parameter objects is null.");
		}
		if (parameters.size() == 0 || values.length == 0 || parameters.size() != values.length
				|| parameters.size() != values[0].length) {
			throw new IllegalArgumentException("Arguments did not have the same size or were empty");
		}
		matrix = new Vector<Vector<Parameter>>(parameters.size());
		for (int i = 0; i < parameters.size(); i++) {
			matrix.add(i, new Vector<Parameter>(parameters.size()));
			for (int j = 0; j < parameters.size(); j++) {
				matrix.get(i).add(j, new Parameter(parameters.get(j), values[i][j]));
			}
		}
	}
	
	/**
	 * Returns the Parameter Object stored at the position x (=row), y (=column).
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @author bene
	 */
	public Parameter getElementAt(int x, int y) {
		if (x >= this.matrix.size() || y >= this.matrix.size()) {
			throw new IllegalArgumentException("Can not accesse value at x or y position larger than matrix size.");
		}
		return this.matrix.get(x).get(y);
	}
}
