/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 21, 2012
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.sos.course.io;

import java.io.FileReader;
import java.util.LinkedList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;

/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class CourseLoader {
	public static LinkedList<Course> loadCourses(String savepath) {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(savepath));
			reader.getEventType(); //START
			
			int cIdx = -1;					//CourseIDX
			int aIdx = 0;					//AttributeIDX
			IPlace[][] students = null;
			
			int sRows = 0, sColumns = 0; 	//Student-attribute
			int mRows = 0, mColumns = 0; 	//Matrix
			
			float[][] matVals = null;
			
			while(reader.hasNext()) {
				reader.next(); //Next element
				
				if(reader.hasName()) {
					String tagname = reader.getName().toString();
					try {
						
						if(tagname.contentEquals("courses")) {
							courses = new Course[Integer.parseInt(reader.getAttributeValue(0))];
							students = null;
							
						} else if(tagname.contentEquals("course")) {
							cIdx++;

							courses[cIdx].setName( reader.getAttributeValue(0) );

							students = allocStudents( Integer.parseInt(reader.getAttributeValue(1)), Integer.parseInt(reader.getAttributeValue(2)));
							
						} else if(tagname.contentEquals("student")) {
							//isEmpty="0"
							if(Integer.parseInt(reader.getAttributeValue(0))==0) {
								students[sRows][sColumns] = new Student( Integer.parseInt(reader.getAttributeValue(1)) );
							} else { //isEmpty="1"
								students[sRows][sColumns] = new EmptyPlace( Integer.parseInt(reader.getAttributeValue(1)) );
							}
							sColumns++;
							if(sColumns>=students[0].length) { //
								sColumns = 0;
								sRows++;
							}
							aIdx=0;
						} else if(tagname.contentEquals("sAttribute")) {
							Student curStudent = (Student)students[sRows][sColumns];
							curStudent.addValueToStateVector(aIdx, Float.parseFloat(reader.getAttributeValue(1)));
							aIdx++;
						} else if(tagname.contentEquals("changematrix")) {
							
							int size = Integer.parseInt(reader.getAttributeValue(0));
							matVals = new float[size][size];
							
						} else if(tagname.contentEquals("mat_row")) {
							mRows++;
							mColumns=0;
							
						} else if(tagname.contentEquals("mAttribute")) {
							matVals[mRows][mColumns] = Float.parseFloat(reader.getAttributeValue(0)); //Integer value
							mColumns++;
						} else if(tagname.contentEquals("influencevector")) {
							
						}
					} catch( Exception ex ) {
						//...Useless exceptions for every little thing.
					}
				}
			}
			reader.close();
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	private static IPlace[][] allocStudents( int rows, int columns ) {
		return new IPlace[rows][columns];
	}
}
