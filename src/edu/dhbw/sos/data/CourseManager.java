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
import java.io.FileWriter;

import javax.xml.stream.*;


/**
 * Manages the stored courses
 * @author SebastianN
 * 
 */
public class CourseManager {
	private Course[] courses;
	private String savepath = ".\\data\\courses.xml";
	
	public void saveCourses() {
		/*
		 * <courses>
		 * 	<course name="KURSNAME">
		 * 		foreach<student>
		 * 			<attribute value="...">
		 * 			<attribute value="...">
		 * 			<..........>
		 * 		</student>
		 * 	</course>
		 * </courses>
		 */
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			XMLStreamWriter writer = factory.createXMLStreamWriter(new FileWriter(savepath));
			if(writer!=null) {
				writer.writeStartDocument();
				writer.writeStartElement("courses");
				if(courses!=null) {
					for(int i=0;i<courses.length;i++) {
						writer.writeStartElement("course");	
//						writer.writeAttribute("name", courses[i].getName());
						Student[] students = null; //courses[i].getStudents();
						for(int j=0;j<students.length;j++) {
							writer.writeStartElement("student");
							for(int k=0;k<students[j].getSs().length;k++) {
								writer.writeStartElement("attribute");
								writer.writeAttribute("value", students[j].getSs()[k].toString());
								writer.writeEndElement(); //attribute 
							}
							writer.writeEndElement(); //student
						}
						writer.writeEndElement(); //course
					}
				}
				writer.writeEndElement(); //Courses
				writer.writeEndDocument();
				writer.flush();
				writer.close();
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	public void loadCourses() {
		
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
