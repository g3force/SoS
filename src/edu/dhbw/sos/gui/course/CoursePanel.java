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

import javax.swing.JPanel;

import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.gui.GUIData;
import edu.dhbw.sos.gui.IUpdateable;
import edu.dhbw.sos.gui.MainFrame;


/**
 * The CoursePanel is the biggest part of the GUI.
 * It contains the students.
 * joa
 * @author NicolaiO
 * 
 */
public class CoursePanel extends JPanel implements IUpdateable {
	private static final long				serialVersionUID	= 5542875796802944785L;
//	private static final Logger			logger				= Logger.getLogger(CoursePanel.class);
	private final PaintArea	paintArea;
	private IPlace[][] students;
	
	
	
	/**
	 * @brief Initialize the CoursePanel
	 * 
	 * @param data GUIData
	 * @author NicolaiO
	 */
	public CoursePanel(GUIData data) {
		this.setBorder(MainFrame.compoundBorder);
		this.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		students = data.getCourse().getStudents();
		paintArea = new PaintArea(data.getCourse().getStudents());
		this.add(paintArea, BorderLayout.CENTER);
		paintArea.setProperties(data.getCourse().getProperties());
	}
	
	
	@Override
	public void update() {
		paintArea.updateStudentCircles(students);
	}
	
	
}
