/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 15, 2012
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.sos.course.influence;

import java.util.HashMap;
import java.util.LinkedList;

import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.helper.Matrix;

/**
 * TODO dirk, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author dirk
 * 
 */
public class Influence {
	
	Matrix parameterInfl; 
	HashMap<EInfluenceType, CalcVector> environmentalInfl;
	
	public Influence() {
		int[][] array = new int[4][4];
		for (int i=0; i < array.length;i++) {
			for (int j = 0; j < array[i].length;j++) {
				array[i][j]=(int)(Math.random()*200)-100;
			}
		}
		LinkedList<String> l = new LinkedList<String>();
		l.add("Tireness");
		l.add("Loudness");
		l.add("Attention");
		l.add("Quality");
		parameterInfl = new Matrix(l, array);
		environmentalInfl = new HashMap<EInfluenceType, CalcVector>();
		CalcVector cv1 = new CalcVector(l);
		CalcVector cv2 = new CalcVector(l);
		CalcVector cv3 = new CalcVector(l);
		for(int i=0; i<4; i++) {
			cv1.setValueAt(i, (int)(Math.random()*200)-100);
			cv2.setValueAt(i, (int)(Math.random()*200)-100);
			cv3.setValueAt(i, (int)(Math.random()*200)-100);
		}
		environmentalInfl.put(EInfluenceType.NEIGHBOR , cv1);
		environmentalInfl.put(EInfluenceType.BREAK_REACTION , cv2);
		environmentalInfl.put(EInfluenceType.TIME_DEPENDING , cv3);
		
	}
	
	public CalcVector getEnvironmentVector(EInfluenceType type, double times) {
		return getEnvironmentVector(type).multiplyWithDouble(times);
	}
	
	public CalcVector getEnvironmentVector(EInfluenceType type) {
		CalcVector temp =  environmentalInfl.get(type).clone();
		return temp;
	}
	
	public CalcVector getInfluencedParameterVector(CalcVector toInfluence, double times) {
		return getInfluencedParameterVector(toInfluence).multiplyWithDouble(times);
	}
	
	public CalcVector getInfluencedParameterVector(CalcVector toInfluence) {
		return toInfluence.multiplyWithMatrix(parameterInfl);
	}
	
	public Matrix getParameterMatrix() {
		return parameterInfl;
	}
	
	public void setParameterMatrix(Matrix newMatrix) {
		this.parameterInfl = newMatrix;
	}
	
	
}
