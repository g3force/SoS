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

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.Courses;



/**
 * This static class loads the course informations
 * 
 * @author SebastianN
 * 
 */
public class CourseLoader {
	private static final Logger	logger	= Logger.getLogger(CourseLoader.class);
	
	
	// private static Document doc;
	
	
	/**
	 * 
	 * Loads the entire course-structure and the vectors
	 * 
	 * @param savepath
	 * @return
	 * @author SebastianN
	 */
	
	public static Courses loadCourses(String savepath) {
		/*
		 * DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 * DocumentBuilder dBuilder;
		 */
		XStream xstream = new XStream();
		File dir = new File(savepath);
		Courses allCourses = new Courses(savepath);
		
		System.out.println("Savepath: " + savepath);
		
		if (dir.list() != null) {
			for (int i = 0; i < dir.list().length; i++) {
				String curFile = dir.list()[i];
				String ext = curFile.substring(curFile.lastIndexOf(".") + 1, curFile.length());
				if (ext.contentEquals("xml")) {
					File xmlfile = new File(savepath + dir.list()[i]);
					if (xmlfile.length() > 0) {
						try{
							/*
							 * BufferedReader fr;
							 * try {
							 * fr = new BufferedReader(new FileReader(xmlfile));
							 * String tmp = "";
							 * while ((tmp = fr.readLine()) != null) {
							 * System.out.println("File: " + tmp);
							 * }
							 * } catch (IOException err) {
							 * // TODO SebastianN Auto-generated catch block
							 * err.printStackTrace();
							 * }
							 */
							Course newCourse = (Course) xstream.fromXML(xmlfile);
							
							allCourses.add(newCourse);
						} catch (CannotResolveClassException e) {
							logger.fatal("ALARM! ALARM! ALARM! Basti mach das heile");
						}
					}
				}
			}
		}
		if (allCourses.size() == 0) {
			allCourses.add(new Course("Dummy course"));
		}
		allCourses.setCurrentCourse(allCourses.get(0));
		logger.info("CoursesSize: " + allCourses.size() + ", setCourse: " + allCourses.getCurrentCourse());
		return allCourses;
		/*
		 * try {
		 * dBuilder = dbFactory.newDocumentBuilder();
		 * doc = dBuilder.parse(savepath);
		 * doc.getDocumentElement().normalize();
		 * } catch (ParserConfigurationException err) {
		 * logger.error("Could not initialize dBuilder");
		 * err.printStackTrace();
		 * courses.add(createDummyCourse());
		 * return courses;
		 * } catch (SAXException err) {
		 * logger.error("(SAX:)Could not parse document");
		 * err.printStackTrace();
		 * courses.add(createDummyCourse());
		 * return courses;
		 * } catch (IOException err) {
		 * logger.error("(IO:)Could not parse document");
		 * err.printStackTrace();
		 * courses.add(createDummyCourse());
		 * return courses;
		 * }
		 * 
		 * courses = loadCoursesFromXML();
		 * float[][] pMatrix = loadMatrixFromXML("p"); // pMatrix, pRow
		 * float[][] eMatrix = loadMatrixFromXML("e"); // eMatrix, eRow
		 * Influence infl = new Influence(pMatrix, eMatrix);
		 * 
		 * for (int i = 0; i < courses.size(); i++) {
		 * courses.get(i).setInfluence(infl);
		 * }
		 */
	}


	/*
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
	*/
}
