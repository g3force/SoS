/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 15, 2012
 * Author(s): SebastianN
 *
 * *********************************************************
 */
package edu.dhbw.sos.data;
import java.io.FileReader;
import java.io.FileWriter;

import javax.xml.stream.*;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.Parameter;


/**
 * Manages the stored courses
 * @author SebastianN
 * 
 */
public class CourseManager {
	private Course[] courses;
	private Course curCourse;
	private String savepath = System.getProperty("user.dir") + "//courses.xml";

	
	public void addCourse( Course addthis ) {
		Course[] newCourses = null;
		
		if(courses!=null) {//if courses exist already
			newCourses = new Course[courses.length+1];
			
			//"copy" the courses
			for(int i=0;i<courses.length;i++) {
				newCourses[i] = courses[i];
			}
			
			//add new course
			newCourses[courses.length]= addthis;
			
			//assign new courseslist
			courses = newCourses;
			
		} else { //courses don't exist	
			newCourses = new Course[1];
			newCourses[0] = addthis;
			courses = newCourses;
		
		}
		return;
	}
	
	/**
	 * 
	 * @brief Checks whether a place is empty of not - based on x,y
	 * 
	 * @param row
	 * @param column
	 * @return TRUE if student is an instance of "EmptyPlace", otherwise FALSE.
	 * @author SebastianN
	 */
	public boolean IsPlaceEmpty( int row, int column ) {
		IPlace[][] students = curCourse.getStudents();
		if(students!=null) {
			
			if( students[row][column] instanceof EmptyPlace ) {
				return true;
			}
			
		}
		throw new IllegalStateException();
	}
	
	/**
	 * 
	 * Gets a certain student-instance based on x- and y-coordinates
	 * 
	 * @param row
	 * @param column
	 * @return
	 * @author SebastianN
	 */
	public IPlace getPlace( int row, int column ) {
		IPlace[][] students = curCourse.getStudents();
		if(students!=null) {
			return students[row][column];
		}
		throw new IllegalStateException();
	}
	
	/**
	 * 
	 * Assigns a student-instance to a place (based on row/column)
	 * 
	 * @param row
	 * @param column
	 * @param student
	 * @return TRUE if assignment was successful, otherwise FALSE
	 * @author SebastianN
	 */
	public boolean setPlace( int row, int column, IPlace student ) {
		IPlace[][] students = curCourse.getStudents();
		if(students!=null) {
			students[row][column] = student;
			return true;
		}
		throw new IllegalStateException();
	}
	
	/* File format
	 * <savefile>
	 * 		<courses count="X">
	 * 			<course name="KURSNAME" students="X">
	 * 				foreach<student isempty="0" ODER isempty="1">
	 * 					<attribute name="Tiredness" value="...">
	 * 					<attribute name=" value="...">
	 * 					<..........>
	 * 				</student>
	 * 			</course>
	 * 		</courses>
	 * 		<changevector>
	 * 			<change val="...">
	 * 			........
	 * 		</changevector>
	 * </savefile>
	 */
public void saveCourses() {
	XMLOutputFactory factory = XMLOutputFactory.newInstance();
	try {
		FileWriter fw = new FileWriter(savepath,false);
		XMLStreamWriter writer = factory.createXMLStreamWriter(fw);
		if(writer!=null) {
			writer.writeStartDocument();
			writer.writeStartElement("savefile");
			
			writer.writeStartElement("courses");
			writer.writeAttribute("count", String.valueOf(courses.length) );
			
			for(int i=0;i<courses.length;i++) {
				//Write for each course --> <course name="NAME" students="COUNT">
					
				IPlace[][] students = courses[i].getStudents();
				
				writer.writeStartElement("course");	
/**
 * FIXME:	writer.writeAttribute("name", courses[i].getName());
 */
				writer.writeAttribute("x", String.valueOf(students[0].length));
				writer.writeAttribute("y", String.valueOf(students.length));
				
				for(int j=0;j<students.length;j++) {
					for(int k=0;k<students[j].length;k++) {
						//For each student --> <student>
						writer.writeStartElement("student");
						
						if(students[j][k].getClass().getName().contains("EmptyPlace")) {
							writer.writeAttribute("isempty", "1"); //if it's empty --> no further information
						} else {
							writer.writeAttribute("isempty", "0");
							Student currentStudent = (Student)students[j][k];
							for(int m=0;m<currentStudent.getActualState().size();m++) {
								//<attribute value="VALUE">
								writer.writeStartElement("attribute");
								writer.writeAttribute("name", currentStudent.getActualState().getTypeAt(m) );
								writer.writeAttribute("value", String.valueOf( currentStudent.getActualState().getValueAt(m) ));
								writer.writeEndElement();
								//</attribute>
							}
						}
						
						writer.writeEndElement();
						//</student>
					}
				}
				writer.writeEndElement(); 
				//</course>
			}
			writer.writeEndElement(); 
			//</courses>
				

				
			writer.writeStartElement("changematrix");
			writer.writeAttribute("count", "5");
			for(int i=0;i<5;i++) {
				writer.writeStartElement("change");
				writer.writeAttribute("name", String.valueOf(i));
				writer.writeAttribute("value", "VALUE");
				writer.writeEndElement();
			//</change>
			}
			writer.writeEndElement();
			//</changevector>
			writer.writeEndElement();
			writer.writeEndDocument();
			
			writer.flush();
			writer.close();
			writer=null;
		}
	} catch( Exception ex ) {
		ex.printStackTrace();
	}
}
	
public void loadCourses() {
	XMLInputFactory factory = XMLInputFactory.newInstance();
	try {
		XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(savepath));
		reader.getEventType(); //START
		
		int courseIdx = -1;
		IPlace[][] students = null;
		int rows = 0, columns = 0;
		int max_rows = 0, max_columns = 0;
		while(reader.hasNext()) {
			reader.next(); //Next element
			
			if(reader.hasName()) {
				String tagname = reader.getName().toString();
				try {
					
					if(tagname.contentEquals("courses")) {
						courses = new Course[Integer.parseInt(reader.getAttributeValue(0))];
						students = null;
						
					} else if(tagname.contentEquals("course")) {
						courseIdx++;
/**
 * FIXME:			courses[courseIdx].setName( reader.getAttributeValue(0) );
 */
						max_columns = Integer.parseInt(reader.getAttributeValue(1));
						max_rows = Integer.parseInt(reader.getAttributeValue(2));
						students = new EmptyPlace[max_rows][max_columns];
						
					} else if(tagname.contentEquals("student")) {
						if(Integer.parseInt(reader.getAttributeValue(0))==0) {
/**
 * FIXME:				students[y][x] = new Student();
 */
							columns++;
							if(columns>=max_columns) { //
								columns = 0;
								rows++;
							}
						}
					} else if(tagname.contentEquals("attribute")) {
						Student curStudent = (Student)students[rows][columns];
						Parameter p = new Parameter( reader.getAttributeValue(0), Integer.parseInt( reader.getAttributeValue(1) ) );
						curStudent.addParamToStudent(p);
					} else if(tagname.contentEquals("changevector")) {
						System.out.println("Changevectorcount: " + reader.getAttributeValue(0));
					} else if(tagname.contentEquals("change")) {
						System.out.println("Changename: " + reader.getAttributeValue(0));
						System.out.println("Changeval: " + reader.getAttributeValue(1));
					}
				} catch( Exception ex ) {
					//...Useless exceptions for every little thing.
				}
			} else if(reader.hasText()) {
				System.out.println("VALUE_FOUND: " + reader.getText());
			}
		}
	} catch( Exception ex ) {
		ex.printStackTrace();
	}
	return;
}

public Course getCurrentCourse() {
	return curCourse;
}

public void setCurrentCourse( Course newCurrent ) {
	curCourse = newCurrent;
}

public Course getCourseByName( String name ) {
	for(int i=0;i<courses.length;i++) {
		/**
		 * FIXME: if(name.concat(courses[i].getName()) {
			return courses[i];
		}*/
	}
	return null;
}
	
	public Course getCourseById( int id ) {
		if(courses!=null && id>=0 && id<courses.length) {
			return courses[id];
		}
		return null;
	}
	
	/*
	public Course getCourseByName( String name ) {
		if(courses!=null) {
			for(int i=0;i<courses.length;i++) {
				if(strcmp(courses[i].getName(),name)==0) {
					return courses[i];
				}
			}
		}
		return null;
	}
	*/
	
}
