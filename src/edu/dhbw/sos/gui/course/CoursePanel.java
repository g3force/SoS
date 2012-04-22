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

import javax.swing.JPanel;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.CourseController;
import edu.dhbw.sos.course.ICurrentCourseObserver;
import edu.dhbw.sos.gui.GUIData;
import edu.dhbw.sos.gui.IUpdateable;
import edu.dhbw.sos.gui.MainFrame;
import edu.dhbw.sos.simulation.SimController;


/**
 * The CoursePanel is the biggest part of the GUI.
 * It contains the students.
 * joa
 * @author NicolaiO
 * 
 */
public class CoursePanel extends JPanel implements IUpdateable, ComponentListener, ICurrentCourseObserver {
	private static final long	serialVersionUID	= 5542875796802944785L;
	// private static final Logger logger = Logger.getLogger(CoursePanel.class);
	private final CPaintArea	paintArea;
	private Course					course;
	
	
	/**
	 * @brief Initialize the CoursePanel
	 * 
	 * @param courseController
	 * @param courses
	 * @author NicolaiO
	 */
	public CoursePanel(SimController simController, CourseController courseController, Course course, GUIData guiData) {
		this.setBorder(MainFrame.COMPOUND_BORDER);
		this.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		this.addComponentListener(this);
		this.course = course;
		paintArea = new CPaintArea(simController, guiData);
		this.add(paintArea, BorderLayout.CENTER);
	}
	
	
	@Override
	public void update() {
		paintArea.updateStudentCircles(course.getStudents());
		paintArea.updateProperties(course.getProperties());
	}
	
	
	@Override
	public void componentResized(ComponentEvent e) {
		update();
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
	public void updateCurrentCourse(Course course) {
		this.course = course;
		update();
	}
}
