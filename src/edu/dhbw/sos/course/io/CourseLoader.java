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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;

import edu.dhbw.sos.SuperFelix;
import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.influence.Influence;
import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.Lecture;
import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;
import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.CalcVector;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This static class loads the course informations
 * 
 * @author SebastianN
 * 
 */
public class CourseLoader {
	
	private static final Logger	logger	= Logger.getLogger(SuperFelix.class);
	private static Document			doc;
	
	
	private static Course createDummyCourse() {
		IPlace[][] students = new IPlace[5][7];
		LinkedList<String> properties = new LinkedList<String>();
		properties.add("Tireness");
		properties.add("Loudness");
		properties.add("Attention");
		properties.add("Quality");
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 7; x++) {
				if (y == 3 && x == 4) {
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
		
		return dummy;
	}
	
	
	private static int getIntFromTag(String tagname, int idx) {
		NodeList nList = doc.getElementsByTagName(tagname);
		try {
			if (nList.getLength() > 0) {
				for(int i=0;i<nList.getLength();i++) {
					for(int j=0;j<nList.item(i).getAttributes().getLength();j++)
						logger.debug("Elements("+j+") from " + tagname + ": " + nList.item(i).getAttributes().item(j).getTextContent());
				}
				return Integer.parseInt(nList.item(0).getAttributes().item(idx).getTextContent());
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		return 0;
	}
	
	
	private static String getStringFromTag(String tagname, int idx) {
		NodeList nList = doc.getElementsByTagName(tagname);
		try {
			if (nList.getLength() > 0) {
				return nList.item(0).getAttributes().item(idx).getTextContent();
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		return "";
	}
	
	private static BlockType getBlockTypeById( int id ) {
		switch(id) {
			case 0:
				return BlockType.theory;
			case 1:
				return BlockType.group;
			case 2:
				return BlockType.exercise;
			case 3:
				return BlockType.pause;
		}
		return BlockType.theory;
	}
	
	
	private static LinkedList<Course> loadCoursesFromXML() {
		LinkedList<Course> courses = new LinkedList<Course>();
		for (int i = 0; i < getIntFromTag("courses", 0); i++) {
			// <course>
			Course newCourse = new Course(getStringFromTag("course", 0));
			int rows = getIntFromTag("course", 1);
			int cols = getIntFromTag("course", 2);
			logger.debug("Rows: " + rows + ", cols: " + cols);
			newCourse.setStudents(new IPlace[rows][cols]);
			
			NodeList students = doc.getElementsByTagName("course");
			students = students.item(0).getChildNodes(); // <student>....</student>
			for (int j = 0; j < students.getLength(); j++) {
				if(students.item(j).getNodeName().contentEquals("lecture")) {
					//lecture stuff
					NodeList nLecture = students.item(j).getChildNodes();
					//Data
					System.out.println("REALDATE: "+nLecture.item(0).getAttributes().item(0).getTextContent());
					System.out.println("OTHERDATE: " + new Date());
					@SuppressWarnings("deprecation")
					SimpleDateFormat x = new SimpleDateFormat( "dd.MM.yyyy, hh:mm");
					Date newDate = new Date();
					try {
						newDate = x.parse( nLecture.item(0).getAttributes().item(0).getTextContent() );
					} catch (DOMException err) {
						err.printStackTrace();
					} catch (ParseException err) {
						err.printStackTrace();
					}
					TimeBlocks tbs = new TimeBlocks();
					
					for(int k=1;k<nLecture.getLength();k++) {
						int length = Integer.parseInt(nLecture.item(k).getAttributes().item(0).getTextContent());
						int blockType = Integer.parseInt(nLecture.item(k).getAttributes().item(1).getTextContent());
						TimeBlock tb = new TimeBlock(length, getBlockTypeById(blockType));
						tbs.add(tb);
					}
					
					Lecture newLecture = new Lecture( newDate, tbs );
					
					newCourse.setLecture( newLecture );
					
				} else {
					int isEmpty = Integer.parseInt(students.item(j).getAttributes().item(0).getTextContent());
					IPlace curStudent = newCourse.getStudents()[j / cols][j % cols];
					if (isEmpty == 1) {
						curStudent = new EmptyPlace(Integer.parseInt(students.item(j).getAttributes().item(1).getTextContent()));
					} else { // not empty
						curStudent = new Student(Integer.parseInt(students.item(j).getAttributes().item(1).getTextContent()));
						NodeList attributes = students.item(j).getChildNodes();
						if (attributes.getLength() > 0) {
							
							float[] vector = new float[attributes.getLength()];
							for (int k = 0; k < attributes.getLength(); k++) {
								vector[k] = Float.parseFloat(attributes.item(k).getAttributes().item(0).getTextContent());
							}
							CalcVector cv = new CalcVector(vector);
							curStudent.setActualState(cv);
						}
					}
					newCourse.setPlace(j/cols, j%cols, curStudent);
					System.out.println("Student("+j+")=" + isEmpty);
				}
				
			}
			courses.add(newCourse);
		}
		return courses;
	}
	
	
	private static float[][] loadMatrixFromXML(String letter) {
		float[][] result = new float[1][1]; // ...?
		System.out.println("Matrixname: " + letter + "Matrix");
		try {
			NodeList pMat = doc.getElementsByTagName(letter + "Matrix");
			NodeList pRows = pMat.item(0).getChildNodes();
			result = new float[pRows.getLength()][pRows.item(0).getChildNodes().getLength()];
			
			for (int i = 0; i < pRows.getLength(); i++) {
				NodeList pAttributes = pRows.item(i).getChildNodes();
				for (int j = 0; j < pAttributes.getLength(); j++) {
					result[i][j] = Float.parseFloat(pAttributes.item(j).getAttributes().item(0).getTextContent());
					System.out.println("result["+i+"]["+j+"]=" + result[i][j]);
				}
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 
	 * Loads the entire course-structure and the vectors
	 * 
	 * @param savepath
	 * @return
	 * @author SebastianN
	 */
	
	public static LinkedList<Course> loadCourses(String savepath) {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		LinkedList<Course> courses = new LinkedList<Course>();
		File file = new File(savepath);
		if (!file.exists() || file.length() == 0) {// file is empty or not-existent
			Course dummy = createDummyCourse();
			courses.add(dummy);
			return courses;
		}
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(savepath);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException err) {
			logger.error("Could not initialize dBuilder");
			err.printStackTrace();
			courses.add(createDummyCourse());
			return courses;
		} catch (SAXException err) {
			logger.error("(SAX:)Could not parse document");
			err.printStackTrace();
			courses.add(createDummyCourse());
			return courses;
		} catch (IOException err) {
			logger.error("(IO:)Could not parse document");
			err.printStackTrace();
			courses.add(createDummyCourse());
			return courses;
		}
		
		courses = loadCoursesFromXML();
		float[][] pMatrix = loadMatrixFromXML("p"); // pMatrix, pRow
		float[][] eMatrix = loadMatrixFromXML("e"); // eMatrix, eRow
		Influence infl = new Influence(pMatrix, eMatrix);
		
		for (int i = 0; i < courses.size(); i++) {
			courses.get(i).setInfluence(infl);
		}
		return courses;
	}
	
	
}
