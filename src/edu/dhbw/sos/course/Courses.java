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

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.io.CourseSaver;


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
	private static final Logger						logger							= Logger.getLogger(Courses.class);
	private Course											curCourse;
	private LinkedList<ICurrentCourseObserver>	currentCourseOberservers	= new LinkedList<ICurrentCourseObserver>();
	private LinkedList<ICoursesListObserver>		coursesListOberservers		= new LinkedList<ICoursesListObserver>();
	private String savepath = "";
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public Courses(String savepath) {
		super();
		this.savepath = savepath;/*
										 * if (this.size() == 0) {
										 * logger.fatal("There are no courses. This should not happened");
										 * } else if (this.size() == 1) {
										 * curCourse = this.get(0);
										 * } else {
										 * // TODO has to be handled yet
										 * curCourse = this.get(0);
										 * }
										 */
	}
	
	
	@Override
	public boolean add(Course course) {
		boolean res = super.add(course);
		CourseSaver.saveCourses(this, savepath);
		notifyCoursesListObservers();
		return res;
	}
	
	
	@Override
	public boolean remove(Object course) {
		if(this.size()<2)
			return false;
		boolean changeCurCourse = false;
		if(curCourse.equals(course)) {
			changeCurCourse = true;
		}
		boolean res = super.remove(course);
		CourseSaver.removeFile((Course) course, savepath);
		if(changeCurCourse) {
			setCurrentCourse(this.get(0));
		}
		CourseSaver.saveCourses(this, savepath);
		notifyCoursesListObservers();
		return res;
	}
	
	
	public void notifyCoursesListObservers() {
		for (ICoursesListObserver clo : coursesListOberservers) {
			clo.updateCoursesList();
		}
	}
	
	
	public void notifyCurrentCourseObservers() {
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
	
	
	public void setCurrentCourse(Object newCurrent) {
		if (newCurrent instanceof Course) {
			curCourse = (Course) newCurrent;
			notifyCurrentCourseObservers();
			return;
		}
		for (Course c : this) {
			if (c.getName().equals((String) newCurrent)) {
				curCourse = c;
				notifyCurrentCourseObservers();
				return;
			}
		}
		logger.warn("Could not find course \"" + newCurrent + "\". Cannot set as current.");
	}
}
