/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 9, 2012
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.sos.simulation;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import edu.dhbw.sos.data.StudentState;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.helper.Matrix;

/**
 * controls the simulation
 * simulates new students states out of old ones
 * 
 * @author DirkK
 * 
 */

public class SimController {
	
	
	Timer pulse = new Timer();
	public SimController() {
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
		run();
	}
	
	public StudentState nextState(StudentState oldState) {
		return null;
		
	}
	public static void main(String[] args) {
		new SimController();
	}
	public void run() {
		TimerTask simulation = new TimerTask(){
			public void run() {
				simulationStep();
			}
		};
		pulse.scheduleAtFixedRate(simulation, 0, 100);
		
	}
	
	public void stop() {
		pulse.cancel();
		pulse.purge();
	}
	
	private void simulationStep() {
		
	}
}



