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
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import edu.dhbw.sos.course.io.CourseLoader;
import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.Parameter;


/**
 * Manages the stored courses
 * @author SebastianN
 * 
 */
public class CourseController implements ActionListener {
	private Courses	courses;
	
	
	public CourseController(Courses courses) {
		this.courses = courses;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// handle add, modify, delete course
	}
}
