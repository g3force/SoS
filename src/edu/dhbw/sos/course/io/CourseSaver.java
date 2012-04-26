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
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.Courses;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class CourseSaver {
	private static final Logger	logger	= Logger.getLogger(CourseSaver.class);

	
	public static void removeFile(Course course, String savepath) {
		try {
			File fh = new File(savepath + course.getName() + ".xml");
			if (fh.exists()) {
				fh.delete();
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}
	public static void saveCourse(Course course, String savepath) {
		try {
			File path = new File(new File(savepath).getParent());
			if (!path.isDirectory()) {
				if (!path.mkdirs()) {
					logger.error("Could not create " + path.getPath() + " for saving.");
					return;
				}
			}
			XStream xstream = new XStream();
			String xml = xstream.toXML(course);
			try {
				FileWriter fw = new FileWriter(savepath + course.getName() + ".xml", false);
				fw.write(xml);
				fw.flush();
				fw.close();
			} catch (IOException io) {
				io.printStackTrace();
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}


	public static void saveCourses(Courses courses, String savepath) {
		// XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			File path = new File(new File(savepath).getParent());
			if (!path.isDirectory()) {
				if(!path.mkdirs()) {
					logger.error("Could not create " + path.getPath() + " for saving.");
					return;
				}
			}
			XStream xstream = new XStream();
			for (int i = 0; i < courses.size(); i++) {
				String xml = xstream.toXML(courses.get(i));
				try {
					FileWriter fw = new FileWriter(savepath + courses.get(i).getName() + ".xml", false);
					fw.write(xml);
					fw.flush();
					fw.close();
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
			/*
			 * FileWriter fw = new FileWriter(savepath, false);
			 * XMLStreamWriter writer = factory.createXMLStreamWriter(fw);
			 * if (writer != null) {
			 * writer.writeStartDocument();
			 * writer.writeStartElement("savefile");
			 * 
			 * if (courses != null) {
			 * writer.writeStartElement("courses");
			 * writer.writeAttribute("count", String.valueOf(courses.size()));
			 * 
			 * for (int i = 0; i < courses.size(); i++) {
			 * // Write for each course --> <course name="NAME" students="COUNT">
			 * writer.writeStartElement("course");
			 * writer.writeAttribute("name", courses.get(i).getName());
			 * 
			 * IPlace[][] students = courses.get(i).getStudents();
			 * if(students.length>0) {
			 * writer.writeAttribute("x", String.valueOf(students[i].length));
			 * writer.writeAttribute("y", String.valueOf(students.length));
			 * }
			 * 
			 * Lecture lecture = courses.get(i).getLecture();
			 * //<lecture>
			 * // <date val="13.03.2012"></date>
			 * // <timeblock val="..."></timeblock>
			 * // <timeblock val="..."></timeblock>
			 * for (int j = 0; j < students.length; j++) {
			 * for (int k = 0; k < students[j].length; k++) {
			 * // For each student --> <student>
			 * writer.writeStartElement("student");
			 * 
			 * if (students[j][k].getClass().getName().contains("EmptyPlace")) {
			 * writer.writeAttribute("isempty", "1"); // if it's empty --> no further information
			 * writer.writeAttribute("paramcount", String.valueOf(students[j][k].getActualState().size()));
			 * } else {
			 * writer.writeAttribute("isempty", "0");
			 * writer.writeAttribute("paramcount", String.valueOf(students[j][k].getActualState().size()));
			 * Student currentStudent = (Student) students[j][k];
			 * for (int m = 0; m < currentStudent.getActualState().size(); m++) {
			 * // <attribute value="VALUE">
			 * writer.writeStartElement("sAttribute");
			 * // writer.writeAttribute("name", courses[i].getInfluence(). );
			 * writer.writeAttribute("value", String.valueOf(currentStudent.getActualState().getValueAt(m)));
			 * writer.writeEndElement();
			 * // </attribute>
			 * }
			 * }
			 * 
			 * writer.writeEndElement();
			 * // </student>
			 * }
			 * }
			 * writer.writeStartElement("lecture");
			 * 
			 * String date = String.valueOf(lecture.getStart().getDate()) + "." +
			 * String.valueOf(lecture.getStart().getMonth()) + "." + String.valueOf(lecture.getStart().getYear());
			 * date += ", " + String.valueOf(lecture.getStart().getHours()) + ":" +
			 * String.valueOf(lecture.getStart().getMinutes());
			 * writer.writeStartElement("date");
			 * writer.writeAttribute("start", date);
			 * writer.writeEndElement();
			 * 
			 * for(int j=0;j<lecture.getTimeBlocks().size();j++) {
			 * writer.writeStartElement("timeblocks");
			 * writer.writeAttribute("length", String.valueOf(lecture.getTimeBlocks().get(i).getLen()));
			 * writer.writeAttribute("type",
			 * String.valueOf(getLectureType(lecture.getTimeBlocks().get(i).getType().toString())) );
			 * writer.writeEndElement();
			 * }
			 * writer.writeEndElement();
			 * // </course>
			 * }
			 * writer.writeEndElement();
			 * // </courses>
			 * 
			 * if (courses.get(0).getInfluence() != null && courses.get(0).getInfluence().getParameterMatrix() != null) {
			 * writer.writeStartElement("pMatrix");
			 * float[][] parMatrix = courses.get(0).getInfluence().getParameterMatrix();
			 * writer.writeAttribute("rows_columns", String.valueOf(parMatrix.length));
			 * 
			 * for (int row = 0; row < parMatrix.length; row++) {
			 * writer.writeStartElement("pRow");
			 * for (int col = 0; col < parMatrix[row].length; col++) {
			 * writer.writeStartElement("pAttribute");
			 * writer.writeAttribute("value", String.valueOf(parMatrix[row][col]));
			 * writer.writeEndElement();
			 * }
			 * writer.writeEndElement();
			 * // </change>
			 * }
			 * writer.writeEndElement();
			 * // </changevector>
			 * 
			 * writer.writeStartElement("eMatrix");
			 * writer.writeAttribute("eRows", "3");
			 * writer.writeAttribute(
			 * "eCols",
			 * String.valueOf(courses.get(0).getInfluence()
			 * .getEnvironmentVector(Influence.getInfluenceTypeById(0)).size()));
			 * for (int x = 0; x < 3; x++) {
			 * CalcVector envVector = courses.get(0).getInfluence()
			 * .getEnvironmentVector(Influence.getInfluenceTypeById(x));
			 * writer.writeStartElement("eRow");
			 * for (int j = 0; j < envVector.size(); j++) {
			 * writer.writeStartElement("eAttribute");
			 * writer.writeAttribute("value", String.valueOf(envVector.getValueAt(j)));
			 * writer.writeEndElement(); // </eAttribute>
			 * }
			 * writer.writeEndElement(); // </eRow>
			 * }
			 * writer.writeEndElement(); // </eMatrix>
			 * }
			 * }
			 * }
			 * writer.writeEndElement();
			 * writer.writeEndDocument();
			 * 
			 * writer.flush();
			 * writer.close();
			 * fw.close();
			 * writer = null;
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * private static int getLectureType( String type ) {
	 * if(type.contentEquals("theory"))
	 * return 0;
	 * if(type.contentEquals("group"))
	 * return 1;
	 * if(type.contentEquals("exercise"))
	 * return 2;
	 * return 3;
	 * }
	 */
}
