/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 21, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.observers;

import edu.dhbw.sos.course.Course;


/**
 * Observer Interface for the current course
 * 
 * @author NicolaiO
 * 
 */
public interface ICurrentCourseObserver {
	void updateCurrentCourse(Course course);
}
