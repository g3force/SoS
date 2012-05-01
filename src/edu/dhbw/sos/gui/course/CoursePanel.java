/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.course;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.CourseController;
import edu.dhbw.sos.course.Courses;
import edu.dhbw.sos.gui.MainFrame;
import edu.dhbw.sos.observers.ICurrentCourseObserver;
import edu.dhbw.sos.observers.IEditModeObserver;
import edu.dhbw.sos.observers.IStudentsObserver;
import edu.dhbw.sos.observers.Observers;
import edu.dhbw.sos.simulation.SimController;


/**
 * The CoursePanel is the biggest part of the GUI.
 * It contains the students.
 * joa
 * @author NicolaiO
 * 
 */
public class CoursePanel extends JPanel implements ComponentListener, ICurrentCourseObserver, IStudentsObserver,
		IEditModeObserver {
	private static final long		serialVersionUID	= 5542875796802944785L;
	// private static final Logger logger = Logger.getLogger(CoursePanel.class);
	private final CPaintArea		paintArea;
	// student that should be highlighted at the moment
	private LinkedList<JLabel>	editBtns;
	
	
	/**
	 * @brief Initialize the CoursePanel
	 * 
	 * @param courseController
	 * @param courses
	 * @author NicolaiO
	 */
	public CoursePanel(SimController simController, CourseController courseController, Courses courses) {
		this.setBorder(MainFrame.COMPOUND_BORDER);
		this.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		this.addComponentListener(this);
		this.addMouseListener(simController);
		
		this.editBtns = new LinkedList<JLabel>();
		Observers.subscribeCurrentCourse(this);
		Observers.subscribeStudents(this);
		paintArea = new CPaintArea();
		this.add(paintArea, BorderLayout.CENTER);
		paintArea.setCourse(courses.getCurrentCourse());
		paintArea.addMouseListener(simController);
		
		JLabel leftBtn = new JLabel("Add", JLabel.CENTER);
		JLabel rightBtn = new JLabel("Add", JLabel.CENTER);
		JLabel topBtn = new JLabel("Hinzufügen / Entfernen", JLabel.CENTER);
		JLabel bottomBtn = new JLabel("Hinzufügen / Entfernen", JLabel.CENTER);
		leftBtn.setName("left");
		rightBtn.setName("right");
		topBtn.setName("top");
		bottomBtn.setName("bottom");
		editBtns.add(leftBtn);
		editBtns.add(rightBtn);
		editBtns.add(topBtn);
		editBtns.add(bottomBtn);
		for (JLabel btn : editBtns) {
			btn.setVisible(false);
			btn.addMouseListener(courseController);
		}
		
		this.add(leftBtn, BorderLayout.WEST);
		this.add(rightBtn, BorderLayout.EAST);
		this.add(topBtn, BorderLayout.NORTH);
		this.add(bottomBtn, BorderLayout.SOUTH);
	}
	
	
	@Override
	public void updateStudents() {
		paintArea.updateStudentCircles();
	}
	
	
	@Override
	public void updateCurrentCourse(Course course) {
		paintArea.setCourse(course);
		updateStudents();
	}
	
	
	@Override
	public void componentResized(ComponentEvent e) {
		updateStudents();
	}
	
	
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	
	@Override
	public void componentHidden(ComponentEvent e) {
	}
	
	
	@Override
	public void enterEditMode() {
		for (JLabel btn : editBtns) {
			btn.setVisible(true);
		}
		paintArea.setEditMode(true);
		validate();
		updateStudents();
	}
	
	
	@Override
	public void exitEditMode() {
		for (JLabel btn : editBtns) {
			btn.setVisible(false);
		}
		paintArea.setEditMode(false);
		validate();
		updateStudents();
	}
}
