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
import edu.dhbw.sos.course.statistics.IStatisticsObserver;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class Courses implements Iterable<Course> {
	private static final Logger										logger	= Logger.getLogger(Courses.class);
	private Course															curCourse;
	
	// observers
	private transient LinkedList<IStudentsObserver>				studentsObservers;
	private transient LinkedList<ISelectedStudentObserver>	selectedCourseObservers;
	private transient LinkedList<IStatisticsObserver>			statisticsObservers;
	private transient LinkedList<ICurrentCourseObserver>		currentCourseOberservers;
	private transient LinkedList<ICoursesListObserver>			coursesListOberservers;
	private String															savepath	= "";
	private LinkedList<Course>											courses;
	

	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public Courses(String savepath) {
		courses = new LinkedList<Course>();
		this.savepath = savepath;
		
		studentsObservers = new LinkedList<IStudentsObserver>();
		selectedCourseObservers = new LinkedList<ISelectedStudentObserver>();
		statisticsObservers = new LinkedList<IStatisticsObserver>();
		currentCourseOberservers = new LinkedList<ICurrentCourseObserver>();
		coursesListOberservers = new LinkedList<ICoursesListObserver>();

		if (courses.size() == 0) {
			logger.fatal("There are no courses. This should not happened");
		} else if (courses.size() == 1) {
			curCourse = courses.get(0);
		} else {
			// TODO has to be handled yet
			curCourse = courses.get(0);
		}
		
	}
	
	
	/**
	 * notify all subscribers of the students array
	 * 
	 * @author dirk
	 */
	public void notifyStudentsObservers() {
		for (IStudentsObserver so : studentsObservers) {
			so.updateStudents();
		}
	}
	
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public void notifySelectedStudentObservers() {
		for (ISelectedStudentObserver so : selectedCourseObservers) {
			so.updateSelectedStudent();
		}
	}
	
	
	/**
	 * notify all subscribers of the statistics
	 * 
	 * @author andres
	 */
	public void notifyStatisticsObservers() {
		for (IStatisticsObserver so : statisticsObservers) {
			so.updateStatistics();
		}
	}
	
	
	/**
	 * 
	 * objects interested in the students field can subscribe here
	 * the object will be notified if the field changes
	 * 
	 * @param so the object which needs to be informed
	 * @author dirk
	 */
	public void subscribeStudents(IStudentsObserver so) {
		studentsObservers.add(so);
	}
	
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param so
	 * @author NicolaiO
	 */
	public void subscribeSelectedStudent(ISelectedStudentObserver so) {
		selectedCourseObservers.add(so);
	}
	
	
	/**
	 * 
	 * objects interested in the statistics field can subscribe here
	 * the object will be notified if the field changes
	 * 
	 * @param so the object which needs to be informed
	 * @author andres
	 */
	public void subscribeStatistics(IStatisticsObserver so) {
		statisticsObservers.add(so);
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
}
