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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import edu.dhbw.sos.gui.right.AddBtn;
import edu.dhbw.sos.gui.right.DelBtn;
import edu.dhbw.sos.observers.Observers;


/**
 * Handles action events concerning the courses.
 * 
 * The course controller handles:
 * * the change of the current course
 * * adding of a new course
 * * deleting a course
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class CourseController implements ActionListener, MouseListener, ItemListener {
	// private static final Logger logger = Logger.getLogger(CourseController.class);
	private Courses	courses;
	
	
	public CourseController(Courses courses) {
		this.courses = courses;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// handle add, modify, delete course
		if (e.getSource() instanceof AddBtn) {
			String name = "";
			int i = 1;
			do {
				name = "New Course " + i;
				i++;
			} while (courses.contains(name));
			//
			Course newC = new Course(name);
			courses.add(newC);
			courses.setCurrentCourse(newC);
		} else if (e.getSource() instanceof DelBtn) {
			// In case the delete button was pressed, show a confirmation dialog.
			int result = JOptionPane.showConfirmDialog(null, "Do you really want to delete the course '"
					+ courses.getCurrentCourse().getName() + "'?", "Confirmation", JOptionPane.YES_NO_OPTION);
			
			// If user clicked 'Yes'
			if (result == 0)
				courses.remove(courses.getCurrentCourse());
			
			// Otherwise nothing happens.
		}
	}
	
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getItem() instanceof String) {
				String newName = (String) e.getItem();
				if (!courses.contains(newName)) {
					courses.getCurrentCourse().setName(newName);
				}
				Observers.notifyCoursesList();
			}
			courses.setCurrentCourse(e.getItem());
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof JLabel) {
			JLabel editBtn = (JLabel) e.getSource();
			// the action command contains left, right, top or bottom. Give it directly to the next method
			if (e.getButton() == MouseEvent.BUTTON3) {
				courses.getCurrentCourse().subStudents(editBtn.getName());
			} else {
				courses.getCurrentCourse().addStudents(editBtn.getName());
			}
		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
