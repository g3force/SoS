/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 15, 2012
 * Author(s): SebastianN
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import edu.dhbw.sos.gui.right.AddBtn;
import edu.dhbw.sos.gui.right.DelBtn;
import edu.dhbw.sos.gui.right.EditBtn;


/**
 * Manages the stored courses
 * @author SebastianN
 * 
 */
public class CourseController implements ActionListener, ItemListener {
	// private static final Logger logger = Logger.getLogger(CourseController.class);
	private Courses	courses;
	
	
	public CourseController(Courses courses) {
		this.courses = courses;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// handle add, modify, delete course
		if (e.getSource() instanceof AddBtn) {
			Course newC = new Course("New Profile " + courses.size());
			courses.add(newC);
			courses.setCurrentCourse(newC);
		} else if (e.getSource() instanceof EditBtn) {
			// TODO trigger edit mode
		} else if (e.getSource() instanceof DelBtn) {
			courses.remove(courses.getCurrentCourse());
		}
	}
	
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getItem() instanceof String) {
				courses.getCurrentCourse().setName((String) e.getItem());
				courses.notifyCoursesListObservers();
			}
			courses.setCurrentCourse(e.getItem());
		}
	}
}
