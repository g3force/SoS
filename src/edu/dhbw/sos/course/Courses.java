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

import edu.dhbw.sos.SuperFelix;
import edu.dhbw.sos.course.io.CourseSaver;
import edu.dhbw.sos.observers.Observers;


/**
 * Contains a list of all courses, handles the current course
 * 
 * @author NicolaiO
 * 
 */
public class Courses implements Iterable<Course> {
	private static final Logger	logger	= Logger.getLogger(Courses.class);
	private Course						curCourse;
	private LinkedList<Course>		courses;
	
	
	/**
	 * Create new Courses
	 * 
	 * @author NicolaiO
	 */
	public Courses() {
		courses = new LinkedList<Course>();
	}
	
	
	/**
	 * Add a new course to the list
	 * 
	 * @param course new course
	 * @return true, if successful
	 * @author NicolaiO
	 */
	public boolean add(Course course) {
		boolean res = courses.add(course);
		CourseSaver.saveCourses(this, SuperFelix.coursepath);
		Observers.notifyCoursesList();
		return res;
	}
	
	
	/**
	 * 
	 * TODO BastiN, add comment!
	 * 
	 * @param course
	 * @return
	 * @author BastiN
	 */
	public boolean remove(Object course) {
		if (courses.size() < 2)
			return false;
		boolean changeCurCourse = false;
		if (curCourse.equals(course)) {
			changeCurCourse = true;
		}
		boolean res = courses.remove(course);
		CourseSaver.removeFile((Course) course, SuperFelix.coursepath);
		if (changeCurCourse) {
			setCurrentCourse(courses.get(0));
		}
		CourseSaver.saveCourses(this, SuperFelix.coursepath);
		Observers.notifyCoursesList();
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
	 * Return the index of the given course
	 * 
	 * @param currentCourse
	 * @return index of currentCourse
	 * @author NicolaiO
	 */
	public int indexOf(Course currentCourse) {
		return courses.indexOf(currentCourse);
	}
	
	
	public Course get(int i) {
		return courses.get(i);
	}
	
	
	public Course getCurrentCourse() {
		return curCourse;
	}
	
	
	public void setCurrentCourse(Object newCurrent) {
		// if we receive a Course, just set it
		if (newCurrent instanceof Course) {
			curCourse = (Course) newCurrent;
			Observers.notifyCurrentCourse(curCourse);
		} else if (newCurrent instanceof String) {
			// else we try casting it to a String and get the course from name
			for (Course c : courses) {
				if (c.getName().equals((String) newCurrent)) {
					curCourse = c;
					Observers.notifyCurrentCourse(curCourse);
					break;
				}
			}
		} else {
			logger.warn("Could not find course \"" + newCurrent + "\". Cannot set as current.");
		}
	}
	
	
	public boolean contains(String name) {
		for (Course course : courses) {
			if (name.equals(course.getName())) {
				return true;
			}
		}
		return false;
	}
}
