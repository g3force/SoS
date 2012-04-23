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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import edu.dhbw.sos.SuperFelix;
import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.influence.Influence;
import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.Lecture;
import edu.dhbw.sos.course.lecture.TimeBlock;
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
	
	private static final Logger	logger	= Logger.getLogger(SuperFelix.class);
	
	/**
	 * 
	 * Loads the entire course-structure and the vectors
	 * 
	 * @param savepath
	 * @return
	 * @author SebastianN
	 */
	public static LinkedList<Course> loadCourses(String savepath) {
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		File file = new File(savepath);
		XMLStreamReader reader = null;
		LinkedList<Course> courses = new LinkedList<Course>();
		try {
			try {
				reader = factory.createXMLStreamReader(new FileReader(file));
			} catch( FileNotFoundException FnF ) {
				logger.debug("Savefile couldn't be found!");
				throw new XMLStreamException();
			}
			if(file.length()==0) {
				throw new XMLStreamException("Savefile is empty");
			}
			if(reader!=null && file.length()>0) {
			
				reader.getEventType(); //START
				
				int aIdx = 0;							//AttributeIDX 
				int sRows = 0, sColumns = 0; 	//Student-attribute
				
				
				while(reader.hasNext()) {
					reader.next(); //Next element
					
					if(reader.hasName()) {
						String tagname = reader.getName().toString();
						int event_type = reader.getEventType();
						
						//<course> was found.
						if(event_type==1) {
							if(tagname.contentEquals("course")) {
									
								Course newCourse = new Course(reader.getAttributeValue(0));
								
								int tmpRows = Integer.parseInt( reader.getAttributeValue(1) );
								int tmpCols = Integer.parseInt( reader.getAttributeValue(2) );
								
								newCourse.setStudents( new IPlace[tmpRows][tmpCols] );
								courses.add( newCourse );
								
							//<student> was found.
							} else if(tagname.contentEquals("student")) {
								//Isn't really unused, but whatever.
								if(sColumns>=courses.getLast().getStudents()[0].length) { //
									sColumns = 0;
									sRows++;
								}								
								IPlace curStudent = courses.getLast().getStudents()[sRows][sColumns];
								
								int isEmpty = Integer.parseInt(reader.getAttributeValue(0));
								
								//isEmpty="0"
								if(isEmpty==0) {
									curStudent = new Student( Integer.parseInt(reader.getAttributeValue(1)) );
								} else { //isEmpty="1"
									curStudent = new EmptyPlace( Integer.parseInt(reader.getAttributeValue(1)) );
								}
								courses.getLast().setPlace(sRows, sColumns, curStudent);
								if(isEmpty==1) { //Empty places don't have attributes. -> Skip.
									sColumns++;
								}
								aIdx=0;
							
							//<sAttribute> was found.
							} else if(tagname.contentEquals("sAttribute")) {
								Student curStudent = (Student)courses.getLast().getStudents()[sRows][sColumns];
								curStudent.addValueToStateVector(aIdx, Float.parseFloat(reader.getAttributeValue(0)));
								aIdx++;
								//if counted columns >= MAX_COLUMNS
								if(aIdx>=curStudent.getActualState().size()) { //
									sColumns++;
								}
							}
						} else { //closeTag
							if(tagname.contentEquals("course")) {
								//Load ParameterMatrix after </course>
								float[][] paramMatrix = loadParameterMatrix(reader);
								
								//load EnvironmentMatrix after </pMatrix>
								float[][] envMatrix = loadEnvironmentMatrix(reader);
								Influence infl = new Influence( paramMatrix, envMatrix );
								for(int y=0;y<courses.size();y++) {
									courses.get(y).setInfluence(infl);
								}
								break;
							}
						}
					}
				}
				reader.close();
			}
		} catch( XMLStreamException ex ) {
			logger.debug("File " + savepath + " not found. Loading dummy data.");
			//load dummy data
			
			IPlace[][] students = new IPlace[5][7];
			LinkedList<String> properties = new LinkedList<String>();
			properties.add("Tireness");
			properties.add("Loudness");
			properties.add("Attention");
			properties.add("Quality");
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 7; x++) {
					if (y == 3) {
						students[y][x] = new EmptyPlace(properties.size());
					} else {
						Student newStud = new Student(properties.size());
						
						for (int i = 0; i < 4; i++) {
							newStud.addValueToChangeVector(i, (int) (Math.random() * 100));
							newStud.addValueToStateVector(i, (int) (Math.random() * 100));
						}
						// ((Student)students[y][x]).
						students[y][x] = newStud;
					}
				}
			}
			
			@SuppressWarnings("deprecation")
			Influence influence = new Influence();
			Lecture lecture = new Lecture(new Date());
			lecture.getTimeBlocks().addTimeBlock(new TimeBlock(10, BlockType.theory));
			lecture.getTimeBlocks().addTimeBlock(new TimeBlock(20, BlockType.pause));
			lecture.getTimeBlocks().addTimeBlock(new TimeBlock(30, BlockType.exercise));
			lecture.getTimeBlocks().addTimeBlock(new TimeBlock(10, BlockType.pause));
			lecture.getTimeBlocks().addTimeBlock(new TimeBlock(30, BlockType.group));
			
			Course dummy = new Course("Your first Course");
			dummy.setLecture(lecture);
			dummy.setInfluence(influence);
			dummy.setStudents(students);
			dummy.setProperties(properties);
		
			courses.add(dummy);
		}
		return courses;
	}
	
	/**
	 * 
	 * loads the last used course. <strong>COURSES NEED TO BE LOADED FIRST!</strong>
	 * 
	 * @param courses
	 * @param savepath
	 * @return
	 * @author SebastianN
	 */
	public static Course loadCurrentCourse( LinkedList<Course> courses, String savepath ) {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = null;
		try {
			try {
				reader = factory.createXMLStreamReader(new FileReader(savepath));
			} catch( FileNotFoundException FnF ) {
				FnF.printStackTrace();
				//return null;
			}
			if(reader!=null) {
				reader.getEventType(); //START
				
				while(reader.hasNext()) {
					reader.next(); //Next element
					
					if(reader.hasName()) {
						String tagname = reader.getName().toString();
						if(reader.getEventType()==2)
							continue;
						if(tagname.contentEquals("current_course")) {
							for(int x=0;x<courses.size();x++) {
								if(courses.get(x).getName().contentEquals(reader.getAttributeValue(0))) {
									return courses.get(x);
								}
							}
						}
					}
				}
				reader.close();
			}
		} catch( XMLStreamException ex ) {
			logger.debug("XML-Reader in loadCurrentCourse()");
			ex.printStackTrace();
		}				
		return null;	
	}
	
	/**
	 * 
	 * Loads the parameter matrix via the XML-sheet.
	 * 
	 * @param savepath
	 * @return
	 * @author SebastianN
	 */
	public static float[][] loadParameterMatrix( XMLStreamReader reader ) throws XMLStreamException {
		float[][] matVals = null;
		try {
			if(reader!=null) {
				int mRows = -1, mColumns = 0; 	//Matrix
				int size = 0;
	
				while(reader.hasNext()) {
					reader.next(); //Next element
					
					int eventtype = reader.getEventType();
					
					if(reader.hasName()) {
						String tagname = reader.getName().toString();

						if(eventtype == 1) {
							if(tagname.contentEquals("pMatrix")) {
								size = Integer.parseInt(reader.getAttributeValue(0));
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
						} else if(eventtype == 2) {
							//end tag
							if(tagname.contentEquals("pMatrix")) {
								break;
							}
						}
					}
				}
			}
		} catch( XMLStreamException ex ) {
			logger.debug("XML-Reader in loadParameterMatrix()");
			ex.printStackTrace();
		}
		return matVals;
	}
	//End loadInfluenceMatrix()
	
	/**
	 * 
	 * Loads the Environment matrix via XML-sheet.
	 * 
	 * @param savepath
	 * @return
	 * @author SebastianN
	 */
	public static float[][] loadEnvironmentMatrix( XMLStreamReader reader ) {
		float[][] matVals = null;
		try {
			if(reader!=null) {
				reader.getEventType(); //START
					
				int eRows = -1, eColumns = 0; 	//Matrix
	
				while(reader.hasNext()) {
					reader.next(); //Next element
					
					if(reader.hasName()) {
						String tagname = reader.getName().toString();
						if(reader.getEventType()==1) {
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
						} else {
							if(tagname.contentEquals("eMatrix")) {
								break;
							}
						}
					}
				}
			}
		} catch( XMLStreamException ex ) {
			logger.debug("XML-Reader in loadEnvironmentMatrix()");
			ex.printStackTrace();
		}
		return matVals;
	}
}
