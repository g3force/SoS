/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 21, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
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
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class Courses implements Iterable<Course> {
	private static final Logger	logger	= Logger.getLogger(Courses.class);
	private Course						curCourse;
	private LinkedList<Course>		courses;
	
	
	/**
	 * Create new Courses
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public Courses() {
		courses = new LinkedList<Course>();
	}
	
	
	/**
	 * Add a new course to the list
	 * 
	 * @param course new course
	 * @return true, if successful
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public boolean add(Course course) {
		boolean res = courses.add(course);
		CourseSaver.saveCourses(this, SuperFelix.coursepath);
		Observers.notifyCoursesList();
		return res;
	}
	
	
	/**
	 * 
	 * Removal of a course object
	 * 
	 * @param course
	 * @return True if removal was successful. Otherwise false.
	 * @author SebastianN
	 */
	public boolean remove(Object course) {
		if (courses.size() <= 1) // We must NOT have an empty list.
			return false;
		boolean changeCurCourse = false;
		// If we want to delete the currently seleceted course, flag it.
		if (curCourse.equals(course)) {
			changeCurCourse = true;
		}
		boolean res = courses.remove(course);
		CourseSaver.removeFile((Course) course, SuperFelix.coursepath);
		if (changeCurCourse) {
			// If flagged, get the first course.
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
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
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
