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

import edu.dhbw.sos.helper.Parameter;


/**
 * Manages the stored courses
 * @author SebastianN
 * 
 */
public class CourseManager {
	private static Course[] courses;
	private static String savepath = System.getProperty("user.dir") + "//courses.xml";

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
public static void saveCourses() {
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
				

				
			writer.writeStartElement("changevector");
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
	
public static void loadCourses() {
	XMLInputFactory factory = XMLInputFactory.newInstance();
	try {
		XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(savepath));
		reader.getEventType(); //START
		
		int courseIdx = -1;
		IPlace[][] students = null;
		int x, y = 0;
		int max_x, max_y = 0;
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
						max_x = Integer.parseInt(reader.getAttributeValue(1));
						max_y = Integer.parseInt(reader.getAttributeValue(2));
						students = new EmptyPlace[max_y][max_x];
						
					} else if(tagname.contentEquals("student")) {
						if(Integer.parseInt(reader.getAttributeValue(0))==0) {
/**
 * FIXME:				students[y][x] = new Student();
 */
							x++;
							if(x>=max_x) { //
								x = 0;
								y++;
							}
						}
					} else if(tagname.contentEquals("attribute")) {
						Student curStudent = (Student)students[y][x];
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
	
	public static Course getCourseById( int id ) {
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
