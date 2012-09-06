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
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.Courses;


/**
 * TODO SebastianN, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author SebastianN
 * 
 */
public class CourseSaver {
	private static final Logger	logger	= Logger.getLogger(CourseSaver.class);
	
	
	/**
	 * Removal of a file.
	 * 
	 * @param course - the course you want to delete
	 * @param savepath - the path the course is saved
	 * @author SebastianN
	 */
	public static void removeFile(Course course, String savepath) {
		try {
			// Try to find the file we want to delete
			File fh = new File(savepath + course.getName() + ".xml");
			if (fh.exists()) { // If it exists, delete it.
				fh.delete();
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	
	/**
	 * Saving a given course.
	 * 
	 * @param course - the course you want to save
	 * @param savepath - the path you want the course to be saved to.
	 * @author SebastianN
	 */
	public static void saveCourse(Course course, String savepath) {
		try {
			// We read the destinated folder-path. If the path doesn't exist, create it.
			File path = new File(new File(savepath).getParent());
			if (!path.isDirectory()) {
				if (!path.mkdirs()) {
					// If creating the necessary path fails, we log the occurance.
					logger.error("Could not create " + path.getPath() + " for saving.");
					return;
				}
			}
			XStream xstream = new XStream();
			String xml = xstream.toXML(course);
			try {
				// Create a file writer which dumps the xml-infos.
				// Afterwards, we close the file.
				FileWriter fw = new FileWriter(savepath + course.getName() + ".xml", false);
				fw.write(xml);
				fw.flush();
				fw.close();
			} catch (IOException io) {
				io.printStackTrace();
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	
	
	/**
	 * Saving every course.
	 * 
	 * @param courses
	 * @param savepath
	 * @author SebastianN
	 */
	public static void saveCourses(Courses courses, String savepath) {
		// XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			/*
			 * For some reason this malfunctions occasionally.
			 * We disable it for time being.
			 * 
			 * for (int i = 0; i < courses.size(); i++) {
			 * saveCourse(courses.get(i),savepath);
			 * }
			 */
			File path = new File(savepath);
			if (!path.isDirectory()) {
				if (!path.mkdirs()) {
					logger.error("Could not create " + path.getPath() + " for saving.");
					return;
				}
			}
			XStream xstream = new XStream();
			for (int i = 0; i < courses.size(); i++) {
				String xml = xstream.toXML(courses.get(i));
				try {
					FileWriter fw = new FileWriter(savepath + courses.get(i).getName() + ".xml", false);
					fw.write(xml);
					fw.flush();
					fw.close();
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
			
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}
}
