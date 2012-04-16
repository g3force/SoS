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
				array[i][j]=Integer.parseInt((i+1)+""+(j+1));
			}
		}
		LinkedList<String> l = new LinkedList<String>();
		l.add("Tireness");
		l.add("Loudness");
		l.add("Attention");
		l.add("Quality");
		parameterInfl = new Matrix(l, array);
		environmentalInfl = new HashMap<EInfluenceType, CalcVector>();
		environmentalInfl.put(EInfluenceType.NEIGHBOR , new CalcVector(l));
		environmentalInfl.put(EInfluenceType.BREAK_REACTION , new CalcVector(l));
		environmentalInfl.put(EInfluenceType.TIME_DEPENDING , new CalcVector(l));
	}
	
	public CalcVector getEnvironmentVector(EInfluenceType type, double times) {
		return getEnvironmentVector(type).multiplyWithDouble(times);
	}
	
	public CalcVector getEnvironmentVector(EInfluenceType type) {
		return environmentalInfl.get(type).clone();
	}
	
	public CalcVector getInfluencedParameterVector(CalcVector toInfluence, double times) {
		return getInfluencedParameterVector(toInfluence).multiplyWithDouble(times);
	}
	
	public CalcVector getInfluencedParameterVector(CalcVector toInfluence) {
		return toInfluence.multiplyWithMatrix(parameterInfl);
	}
	
	
}
