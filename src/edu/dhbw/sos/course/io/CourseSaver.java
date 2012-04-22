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

import java.io.FileWriter;
import java.util.LinkedList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.course.influence.*;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class CourseSaver {
	public static void saveCourses(LinkedList<Course> courses, String savepath) {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			FileWriter fw = new FileWriter(savepath, false);
			XMLStreamWriter writer = factory.createXMLStreamWriter(fw);
			if (writer != null) {
				writer.writeStartDocument();
				writer.writeStartElement("savefile");
				
				if (courses != null) {
					writer.writeStartElement("courses");
					writer.writeAttribute("count", String.valueOf(courses.size()));
					
					for (int i = 0; i < courses.size(); i++) {
						// Write for each course --> <course name="NAME" students="COUNT">
						writer.writeStartElement("course");
						writer.writeAttribute("name", courses.get(i).getName());
						
						IPlace[][] students = courses.get(i).getStudents();
						writer.writeAttribute("x", String.valueOf(students[i].length));
						writer.writeAttribute("y", String.valueOf(students.length));
						
						for (int j = 0; j < students.length; j++) {
							for (int k = 0; k < students[j].length; k++) {
								// For each student --> <student>
								writer.writeStartElement("student");
								
								if (students[j][k].getClass().getName().contains("EmptyPlace")) {
									writer.writeAttribute("isempty", "1"); // if it's empty --> no further information
									writer.writeAttribute("paramcount", String.valueOf(students[j][k].getActualState().size()));
								} else {
									writer.writeAttribute("isempty", "0");
									writer.writeAttribute("paramcount", String.valueOf(students[j][k].getActualState().size()));
									Student currentStudent = (Student) students[j][k];
									for (int m = 0; m < currentStudent.getActualState().size(); m++) {
										// <attribute value="VALUE">
										writer.writeStartElement("sAttribute");
										/**
										 * FIXME: Influence-name
										 */
										// writer.writeAttribute("name", courses[i].getInfluence(). );
										writer.writeAttribute("value",
												String.valueOf(currentStudent.getActualState().getValueAt(m)));
										writer.writeEndElement();
										// </attribute>
									}
								}
								
								writer.writeEndElement();
								// </student>
							}
						}
						writer.writeEndElement();
						// </course>
					}
					writer.writeEndElement();
					// </courses>
					
					if (courses.get(0).getInfluence() != null && courses.get(0).getInfluence().getParameterMatrix() != null) {
						writer.writeStartElement("pMatrix");
						float[][] parMatrix = courses.get(0).getInfluence().getParameterMatrix();
						writer.writeAttribute("rows_columns", String.valueOf(parMatrix));
						
						for (int row = 0; row < parMatrix.length; row++) {
							writer.writeStartElement("pRow");
							for (int col = 0; col < parMatrix[row].length; col++) {
								writer.writeStartElement("pAttribute");
								writer.writeAttribute("value", String.valueOf(parMatrix[row][col]));
								writer.writeEndElement();
							}
							writer.writeEndElement();
							// </change>
						}
						writer.writeEndElement();
						// </changevector>
						
						writer.writeStartElement("eMatrix");
/**
 * FIXME: NON-HARDCODED CALCVECTOR-AMOUNT
 */
						writer.writeAttribute("eRows", "3");
						writer.writeAttribute("eCols", String.valueOf(courses.get(0).getInfluence().getEnvironmentVector( Influence.getInfluenceTypeById(0) ).size()) );
						for(int x=0;x<3;x++) {
							CalcVector envVector = courses.get(0).getInfluence().getEnvironmentVector( Influence.getInfluenceTypeById(x) );
							writer.writeStartElement("eRow");
							for(int j=0;j<envVector.size();j++) {
								writer.writeStartElement("eAttribute");
								writer.writeAttribute("value", String.valueOf( envVector.getValueAt(j) ) );
								writer.writeEndElement(); //</eAttribute>
							}
							writer.writeEndElement(); //</eRow>
						}
						writer.writeEndElement(); //</eMatrix>
					}
				}
			}
			writer.writeEndElement();
			writer.writeEndDocument();
			
			writer.flush();
			writer.close();
			fw.close();
			writer = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
