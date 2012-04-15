/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 14, 2012
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.sos.data;

import java.util.Date;
import java.util.LinkedList;

/**
 * This class is a data holder for the data belonging to the course
 * 
 * @author DirkK
 */
public class Course {
	IPlace[][] students;
	Influence influence;
	Lecture lecture;
	LinkedList<StudentState> historyStates;
	public Course() {
		students = new Student[5][5];
		for (int i=0; i < 5;i++) {
			for (int j = 0; j < 5;j++) {
				if(i==4||j==5)
					students[i][j]=new EmptyPlace();
				else
					students[i][j]=new Student();
			}
		}
		influence = new Influence();
		historyStates = new LinkedList<StudentState>();
		lecture = new Lecture(new Date(), 200);
	}
}
