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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.Messages;
import edu.dhbw.sos.observers.ICurrentCourseObserver;
import edu.dhbw.sos.observers.ISelectedStudentObserver;
import edu.dhbw.sos.observers.Observers;


/**
 * The StudentPanel is the bottom left Panel.
 * It displays statistics of the currently selected student and property.
 * If no student is selected, an average is shown instead.
 * 
 * @author NicolaiO
 * 
 */
public class StudentPanel extends JPanel implements ISelectedStudentObserver, ICurrentCourseObserver {
	private static final Logger	logger				= Logger.getLogger(StudentPanel.class);
	private static final long		serialVersionUID	= 722304874911423036L;
	private final SPaintArea		paintArea;
	private Course						course;
	private JLabel						lblParameterName;
	
	
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
		Observers.subscribeSelectedStudent(this);
		
		lblParameterName = new JLabel("", JLabel.CENTER);
		lblParameterName.setFont(new Font("Book Antiqua", Font.BOLD, 30));
		this.add(lblParameterName, BorderLayout.NORTH);
	}
	
	
	@Override
	public void updateSelectedStudent() {
		if (course.getSelectedStudent() != null && course.getSelectedStudent() instanceof Student) {
			if (course.getProperties().size() == 0) {
				logger.warn("There are no parameters!!");
				lblParameterName.setText("");
			} else {
				lblParameterName.setText(course.getProperties().get(course.getSelectedProperty()));
			}
			paintArea.update(course.getSelectedStudent(), course.getSelectedProperty());
		} else {
			lblParameterName.setText(Messages.getString("StudentPanel.AVG"));
			paintArea.update(course);
		}
	}
	
	
	@Override
	public void updateCurrentCourse(Course course) {
		this.course = course;
		updateSelectedStudent();
	}
	
}
