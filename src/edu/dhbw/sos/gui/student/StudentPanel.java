/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.IStudentsObserver;


/**
 * The StudentPanel is the bottom left Panel.
 * It displays statistics of the currently selected student and property.
 * If no student is selected, an average is shown instead.
 * 
 * @author NicolaiO
 * 
 */
public class StudentPanel extends JPanel implements IStudentsObserver {
	private static final long	serialVersionUID	= 722304874911423036L;
	private final SPaintArea	paintArea;
	private Course					course;
	
	
	/**
	 * Initialize StudentPanel with GUIData
	 * 
	 * @param data
	 * @author NicolaiO
	 */
	public StudentPanel(Course course) {
		paintArea = new SPaintArea();
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setPreferredSize(new Dimension(200, 150));
		this.setLayout(new BorderLayout());
		this.course = course;
		this.add(paintArea, BorderLayout.CENTER);
		course.subscribeStudents(this);
	}
	
	
	@Override
	public void updateStudents() {
		paintArea.update(course.getSelectedStudent(), course.getSelectedProperty());
	}
	
}
