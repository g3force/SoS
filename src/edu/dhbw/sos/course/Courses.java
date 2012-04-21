/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 21, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course;

import java.util.LinkedList;

import edu.dhbw.sos.course.io.CourseLoader;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class Courses extends LinkedList<Course> {
	private static final long							serialVersionUID				= 1L;
	private Course											curCourse;
	private LinkedList<ICurrentCourseObserver>	currentCourseOberservers	= new LinkedList<ICurrentCourseObserver>();
	private LinkedList<ICoursesListObserver>		coursesListOberservers		= new LinkedList<ICoursesListObserver>();
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public Courses(String savepath) {
		super();
		this.addAll(CourseLoader.loadCourses(savepath));
	}
	
	
	private void notifyCoursesListObservers() {
		for (ICoursesListObserver clo : coursesListOberservers) {
			clo.updateCoursesList();
		}
	}
	
	
	private void notifyCurrentCourseObservers() {
		for (ICurrentCourseObserver cco : currentCourseOberservers) {
			cco.updateCurrentCourse(curCourse);
		}
	}
	
	
	public void subscribeCurrentCourse(ICurrentCourseObserver cco) {
		currentCourseOberservers.add(cco);
	}
	
	
	public void subscribeCoursesList(ICoursesListObserver clo) {
		coursesListOberservers.add(clo);
	}
	
	
	public Course getCurrentCourse() {
		return curCourse;
	}
	
	
	public void setCurrentCourse(Course newCurrent) {
		curCourse = newCurrent;
		notifyCurrentCourseObservers();
	}
	
	
	public void addCourse(Course course) {
		this.add(course);
		notifyCoursesListObservers();
	}
	
	
	public void removeCourse(Course course) {
		this.remove(course);
		notifyCoursesListObservers();
	}
}
