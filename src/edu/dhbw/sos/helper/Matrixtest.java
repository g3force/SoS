/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 7, 2012
 * Author(s): bene
 *
 * *********************************************************
 */
package edu.dhbw.sos.helper;

import java.util.LinkedList;

/**
 * TODO bene, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author bene
 * 
 */
public class Matrixtest {
	
	/**
	 * TODO bene, add comment!
	 * 
	 * @param args
	 * @author bene
	 */
	public static void main(String[] args) {
		int[][] array = new int[4][4];
		for (int i=0; i < array.length;i++) {
			for (int j = 0; j < array[i].length;j++) {
				array[i][j]=Integer.parseInt((i+1)+""+(j+1));
			}
		}
		LinkedList<String> l = new LinkedList<String>();
		l.add("P1");
		l.add("P2");
		l.add("P3");
		l.add("P4");
		CalcVector v = new CalcVector(l);
		Matrix m = new Matrix(l, array);
		v.printCalcVector();
		v = v.multiplyWithMatrix(m);
		System.out.println();
		v.printCalcVector();
	}
	
}
