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

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.io.CourseSaver;
import edu.dhbw.sos.gui.right.IEditModeObserver;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class Courses implements Iterable<Course> {
	private static final Logger						logger							= Logger.getLogger(Courses.class);
	private Course											curCourse;
	private LinkedList<ICurrentCourseObserver>	currentCourseOberservers	= new LinkedList<ICurrentCourseObserver>();
	private LinkedList<ICoursesListObserver>		coursesListOberservers		= new LinkedList<ICoursesListObserver>();
	private LinkedList<IEditModeObserver>			editModeObservers				= new LinkedList<IEditModeObserver>();
	private String											savepath							= "";
	private LinkedList<Course>							courses;
	

	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public Courses(String savepath) {
		courses = new LinkedList<Course>();
		this.savepath = savepath;
		
		if (courses.size() == 0) {
			logger.fatal("There are no courses. This should not happened");
		} else if (courses.size() == 1) {
			curCourse = courses.get(0);
		} else {
			// TODO has to be handled yet
			curCourse = courses.get(0);
		}

	}
	
	
	public boolean add(Course course) {
		boolean res = courses.add(course);
		CourseSaver.saveCourses(this, savepath);
		notifyCoursesListObservers();
		return res;
	}
	
	
	public boolean remove(Object course) {
		if (courses.size() < 2)
			return false;
		boolean changeCurCourse = false;
		if (curCourse.equals(course)) {
			changeCurCourse = true;
		}
		boolean res = courses.remove(course);
		CourseSaver.removeFile((Course) course, savepath);
		if (changeCurCourse) {
			setCurrentCourse(courses.get(0));
		}
		CourseSaver.saveCourses(this, savepath);
		notifyCoursesListObservers();
		return res;
	}
	
	
	@Override
	public Iterator<Course> iterator() {
		return courses.iterator();
	}
	
	
	public int size() {
		return courses.size();
	}
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @param currentCourse
	 * @return
	 * @author NicolaiO
	 */
	public int indexOf(Course currentCourse) {
		return courses.indexOf(currentCourse);
	}
	
	
	public Course get(int i) {
		return courses.get(i);
	}
	
	
	public void subscribeEditMode(IEditModeObserver so) {
		editModeObservers.add(so);
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
		for (Course c : courses) {
			if (c.getName().equals((String) newCurrent)) {
				curCourse = c;
				notifyCurrentCourseObservers();
				return;
			}
		}
		logger.warn("Could not find course \"" + newCurrent + "\". Cannot set as current.");
	}
	
	
	public LinkedList<IEditModeObserver> getEditModeObservers() {
		return editModeObservers;
	}
}
