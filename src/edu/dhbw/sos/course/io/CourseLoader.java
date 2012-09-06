/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 21, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course.io;

import java.io.File;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter.UnknownFieldException;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.Course.ECourseType;
import edu.dhbw.sos.course.Courses;


/**
 * This static class loads the course informations
 * 
 * @author SebastianN
 * 
 */
public class CourseLoader {
	private static final Logger	logger	= Logger.getLogger(CourseLoader.class);
	
	
	/**
	 * Loads the entire course-structure and the vectors
	 * 
	 * @param savepath
	 * @return Returns every loaded course (as Courses)
	 * @author SebastianN
	 */
	public static Courses loadCourses(String savepath) {
		XStream xstream = new XStream();
		File dir = new File(savepath);
		Courses allCourses = new Courses();
		
		// If the dir isn't empty, parse through every entry.
		if (dir.list() != null) {
			for (int i = 0; i < dir.list().length; i++) {
				String curFile = dir.list()[i];
				
				// Get extension of the file
				String ext = curFile.substring(curFile.lastIndexOf(".") + 1, curFile.length());
				
				// If the extension = "XML", open it (if it's not empty)
				if (ext.contentEquals("xml")) {
					File xmlfile = new File(savepath + dir.list()[i]);
					if (xmlfile.length() > 0) {
						try {
							// Add the course from XML to our "Courses"-list
							Course course = (Course) xstream.fromXML(xmlfile);
							allCourses.add(course);
						} catch (CannotResolveClassException e) {
							// shouldn't happen anymore. Suggestions.xml != course_file. Taken care of.
							logger.fatal("ALARM! ALARM! ALARM! Basti mach das heile");
						} catch (UnknownFieldException e) {
							logger.warn("Course could not be loaded because of unknown field.");
							xmlfile.delete(); // deletes the file in case there's an unknown field.
						}
					}
				}
			}
		}
		// In case there were no previous courses, a dummy course is created.
		if (allCourses.size() == 0) {
			logger.info("No courses found. Creating dummy course instead.");
			Course holiday = new Course("Holiday", ECourseType.HOLIDAY);
			allCourses.add(holiday);
			Course group = new Course("Group work", ECourseType.GROUP);
			allCourses.add(group);
			Course normal = new Course("Normal lesson", ECourseType.NORMAL);
			allCourses.add(normal);
			Course theory = new Course("Theory lesson", ECourseType.THEORY);
			allCourses.add(theory);
			allCourses.setCurrentCourse(theory);
		}
		// For now, we'll just load the first course.
		allCourses.setCurrentCourse(allCourses.get(0));
		logger.info("CoursesSize: " + allCourses.size() + ", setCourse: " + allCourses.getCurrentCourse());
		return allCourses;
	}
}