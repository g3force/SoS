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
import edu.dhbw.sos.course.influence.Influence;
import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;

/**
 * This static class loads the course informations
 * 
 * @author SebastianN
 * 
 */
public class CourseLoader {
	public static LinkedList<Course> loadCourses(String savepath) {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		LinkedList<Course> courses = null;
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(savepath));
			reader.getEventType(); //START
			
			int aIdx = 0;							//AttributeIDX
			int sRows = 0, sColumns = 0; 		//Student-attribute
			
			Influence loadedInfl = new Influence( loadParameterMatrix( savepath ), loadEnvironmentMatrix( savepath ) );
			
			while(reader.hasNext()) {
				reader.next(); //Next element
				
				if(reader.hasName()) {
					String tagname = reader.getName().toString();
					try {
						if(reader.getEventType()==2)
							continue;
						if(tagname.contentEquals("courses")) {
							courses = new LinkedList<Course>();
								
						} else if(tagname.contentEquals("course")) {
							
							Course newCourse = new Course();
							newCourse.setName( reader.getAttributeValue(0) ); 

							int tmpRows = Integer.parseInt( reader.getAttributeValue(1) );
							int tmpCols = Integer.parseInt( reader.getAttributeValue(2) );
							
							newCourse.setStudents( new IPlace[tmpRows][tmpCols] );
							newCourse.setInfluence(loadedInfl);
							courses.add( newCourse );
							
						} else if(tagname.contentEquals("student")) {
							@SuppressWarnings("unused")
							//Isn't really unused, but whatever.
							IPlace curStudent = courses.getLast().getStudents()[sRows][sColumns];

							//isEmpty="0"
							if(Integer.parseInt(reader.getAttributeValue(0))==0) {
								curStudent = new Student( Integer.parseInt(reader.getAttributeValue(1)) );
							} else { //isEmpty="1"
								curStudent = new EmptyPlace( Integer.parseInt(reader.getAttributeValue(1)) );
							}
							aIdx=0;
						} else if(tagname.contentEquals("sAttribute")) {
							Student curStudent = (Student)courses.getLast().getStudents()[sRows][sColumns];
							curStudent.addValueToStateVector(aIdx, Float.parseFloat(reader.getAttributeValue(0)));
							aIdx++;
							if(aIdx == curStudent.getActualState().size()) {
								sColumns++;
								//if counted columns >= MAX_COLUMNS
								if(sColumns>=courses.getLast().getStudents()[0].length) { //
									sColumns = 0;
									sRows++;
								}
							}
						}
					} catch( Exception ex ) {
						ex.printStackTrace();
					}
				}
			}
			reader.close();
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
		return courses;
	}
	
	public static float[][] loadParameterMatrix( String savepath ) {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		float[][] matVals = null;
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(savepath));
			if(reader!=null) {
				reader.getEventType(); //START
					
				int mRows = -1, mColumns = 0; 	//Matrix
	
				while(reader.hasNext()) {
					reader.next(); //Next element
					
					if(reader.hasName()) {
						String tagname = reader.getName().toString();
						try {
							if(reader.getEventType()==2)
								continue;
							if(tagname.contentEquals("pMatrix")) {
								int size = Integer.parseInt(reader.getAttributeValue(0));
								matVals = new float[size][size];
								for(int i=0;i<size;i++)
									for(int j=0;j<size;j++)
										matVals[i][j]=0.00f;
									
							 } else if(tagname.contentEquals("pRow")) {
									mRows++;
									mColumns=0;
										
							 } else if(tagname.contentEquals("pAttribute")) {
								 matVals[mRows][mColumns] = Float.parseFloat(reader.getAttributeValue(0)); //Integer value
								 mColumns++;
							 }
						} catch( Exception ex ) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
		return matVals;
	}
	//End loadInfluenceMatrix()
	
	public static float[][] loadEnvironmentMatrix( String savepath ) {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		float[][] matVals = null;
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(savepath));
			if(reader!=null) {
				reader.getEventType(); //START
					
				int eRows = -1, eColumns = 0; 	//Matrix
	
				while(reader.hasNext()) {
					reader.next(); //Next element
					
					if(reader.hasName()) {
						String tagname = reader.getName().toString();
						try {
							if(reader.getEventType()==2)
								continue;
							 if(tagname.contentEquals("eMatrix")) {
								int row_size = Integer.parseInt(reader.getAttributeValue(0));
								int col_size =  Integer.parseInt(reader.getAttributeValue(1));
								matVals = new float[row_size][col_size];
								for(int i=0;i<row_size;i++)
									for(int j=0;j<col_size;j++)
										matVals[i][j]=0.00f;
								 
							 } else if(tagname.contentEquals("eRow")) {
								 eRows++;
								 eColumns=0;
								 
							 } else if(tagname.contentEquals("eAttribute")) {
								 matVals[eRows][eColumns] = Float.parseFloat( reader.getAttributeValue(0) );
								 eColumns++;
								 
							 }
						} catch( Exception ex ) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
		return matVals;
	}
}
