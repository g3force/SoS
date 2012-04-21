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

/**
 * 
 * this class is a data holder for all influence types
 * this influences are used for the simulation
 * 
 * @author dirk
 */
public class Influence {
	
	float[][] parameterInfl;
	HashMap<EInfluenceType, CalcVector> environmentalInfl;
	
	public Influence() {
		float[][] array = new float[4][4];
		for (int i=0; i < array.length;i++) {
			for (int j = 0; j < array[i].length;j++) {
				array[i][j]=(float)(Math.random()*200)-100;
			}
		}
		LinkedList<String> l = new LinkedList<String>();
		l.add("Tireness");
		l.add("Loudness");
		l.add("Attention");
		l.add("Quality");
		parameterInfl = array.clone();
		environmentalInfl = new HashMap<EInfluenceType, CalcVector>();
		CalcVector cv1 = new CalcVector(l.size());
		CalcVector cv2 = new CalcVector(l.size());
		CalcVector cv3 = new CalcVector(l.size());
		for(int i=0; i<4; i++) {
			cv1.setValueAt(i, (float)(Math.random()*200)-100);
			cv2.setValueAt(i, (float)(Math.random()*200)-100);
			cv3.setValueAt(i, (float)(Math.random()*200)-100);
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
		return toInfluence.multiplyWithArray(parameterInfl);
	}
	
	public float[][] getParameterMatrix() {
		return parameterInfl;
	}
	
	public void setParameterMatrix(float[][] newMatrix) {
		this.parameterInfl = newMatrix;
	}
	
	
}
