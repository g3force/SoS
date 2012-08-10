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
	
	float[][]									parameterInfl;
	HashMap<EInfluenceType, CalcVector>	environmentalInfl;
	
	
	public Influence(float[][] paraInfl, float[][] envInfl) {
		parameterInfl = paraInfl.clone();
		
		LinkedList<String> l = new LinkedList<String>();
		l.add("Awakeness");
		l.add("Silence");
		l.add("Attention");
		l.add("Quality");
		environmentalInfl = new HashMap<EInfluenceType, CalcVector>();
		for (int i = 0; i < envInfl.length; i++) {
			CalcVector cv = new CalcVector(l.size());
			for (int j = 0; j < envInfl[i].length; j++) {
				cv.setValueAt(i, envInfl[i][j]);
			}
			environmentalInfl.put(getInfluenceTypeById(i), cv);
		}
	}
	
	
	public static EInfluenceType getInfluenceTypeById(int i) {
		if (i == 0)
			return EInfluenceType.NEIGHBOR;
		if (i == 1)
			return EInfluenceType.BREAK_REACTION;
		return EInfluenceType.TIME_DEPENDING;
	}
	
	
	/**
	 * Constructs influence with dummy data
	 * 
	 * @author NicolaiO
	 */
	public Influence() {
		float[][] array = { { 0f, 10f, -20f, -20f }, { 20f, 0f, -20f, -20f }, { -20f, 10f, 0f, 20f },
				{ 20f, 10f, 20f, 0f } }; // new float[4][4];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				array[i][j] = (float) (Math.random() * 200) - 100;
			}
		}
		parameterInfl = array.clone();
		
		LinkedList<String> l = new LinkedList<String>();
		l.add("Awakeness");
		l.add("Silence");
		l.add("Attention");
		l.add("Quality");
		environmentalInfl = new HashMap<EInfluenceType, CalcVector>();
		CalcVector cv1 = new CalcVector(l.size());
		for (int i = 0; i < 4; i++) {
			cv1.setValueAt(i, (float) (Math.random() * 200) - 100);
		}
		environmentalInfl.put(EInfluenceType.NEIGHBOR, cv1); // not used atm
		
		float[] breakReaction = { 62.0f, 20.0f, 45.0f, 40.0f };
		environmentalInfl.put(EInfluenceType.BREAK_REACTION, new CalcVector(breakReaction));
		
		float[] timeReaction = { -20.0f, -10.0f, -10.0f, -20.0f };
		environmentalInfl.put(EInfluenceType.TIME_DEPENDING, new CalcVector(timeReaction));
		
		float[] exerciseReaction = { 15.0f, -25.0f, 45.0f, 15.0f };
		environmentalInfl.put(EInfluenceType.EXERCISE_REACTION, new CalcVector(exerciseReaction));
		
		float[] groupReaction = { 40.0f, -15.0f, 35.0f, 37.0f };
		environmentalInfl.put(EInfluenceType.GROUP_REACTION, new CalcVector(groupReaction));
		
		float[] theoryReaction = { -40.0f, -15.0f, -30.0f, -30.0f };
		environmentalInfl.put(EInfluenceType.THEORY_REACTION, new CalcVector(theoryReaction));
	}
	
	
	/**
	 * 10 20 30
	 * 10 20 30
	 * 10 20 30
	 * 10 20 30
	 */
	
	public CalcVector getEnvironmentVector(EInfluenceType type, double times) {
		return getEnvironmentVector(type).multiply(times);
	}
	
	
	public CalcVector getEnvironmentVector(EInfluenceType type) {
		CalcVector temp = environmentalInfl.get(type).clone();
		return temp;
	}
	
	
	public CalcVector getInfluencedParameterVector(CalcVector toInfluence, double times) {
		return getInfluencedParameterVector(toInfluence).multiply(times);
	}
	
	
	public CalcVector getInfluencedParameterVector(CalcVector toInfluence) {
		return toInfluence.multiply(parameterInfl);
	}
	
	
	public float[][] getParameterMatrix() {
		return parameterInfl;
	}
	
	
	public void setParameterMatrix(float[][] newMatrix) {
		this.parameterInfl = newMatrix;
	}
	
	
}
