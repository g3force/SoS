/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.course;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.CourseController;
import edu.dhbw.sos.course.Courses;
import edu.dhbw.sos.course.ICurrentCourseObserver;
import edu.dhbw.sos.course.IStudentsObserver;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;
import edu.dhbw.sos.gui.MainFrame;
import edu.dhbw.sos.simulation.SimController;


/**
 * The CoursePanel is the biggest part of the GUI.
 * It contains the students.
 * joa
 * @author NicolaiO
 * 
 */
public class CoursePanel extends JPanel implements ComponentListener, ICurrentCourseObserver, IStudentsObserver,
		MouseListener, MouseMotionListener {
	private static final long		serialVersionUID	= 5542875796802944785L;
	private static final Logger	logger				= Logger.getLogger(CoursePanel.class);
	// private static final Logger logger = Logger.getLogger(CoursePanel.class);
	private final CPaintArea		paintArea;
	private Course						course;
	// space between circles
	private float						spacing;
	/*
	 * Offset in whole Panel
	 * Will contain the border and any extra space that can not be filled,
	 * because the circles should be real circles.
	 * At least one of the offsets should be exactly equal to border
	 */
	private float						offset_x, offset_y;
	// border size around the border of the panel
	private float						border;
	// student that should be highlighted at the moment
	private StudentCircle			hoveredStudent		= null;
	private StudentCircle[][]		studentCircles		= new StudentCircle[0][0];					;
	
	
	/**
	 * @brief Initialize the CoursePanel
	 * 
	 * @param courseController
	 * @param courses
	 * @author NicolaiO
	 */
	public CoursePanel(SimController simController, CourseController courseController, Courses courses) {
		this.setBorder(MainFrame.COMPOUND_BORDER);
		this.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		this.addComponentListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(simController);
		
		this.course = courses.getCurrentCourse();
		courses.subscribeCurrentCourse(this);
		courses.getCurrentCourse().subscribeStudents(this);
		paintArea = new CPaintArea();
		this.add(paintArea, BorderLayout.CENTER);
	}
	
	
	/**
	 * Set and update all important values for the student circles
	 * 
	 * @author NicolaiO
	 */
	public void calcStudentCircles(IPlace[][] students) {
		// get available size
		Dimension p = this.getSize();
		if (p.height == 0 || p.width == 0) {
			// nothing to do really because Panel not ready yet 
			studentCircles = new StudentCircle[0][0];
			return;
		}
		// get num of student rows and cols from students array
		int numStudy = students.length;
		if (numStudy == 0) {
			logger.warn("there are no students!");
			studentCircles = new StudentCircle[0][0];
			return;
		}
		int numStudx = students[0].length;
		// compare ratio of x/y from number of circles and from available size
		// thus we can decide, if the offset has to be vertically or horizontally
		float ratioA = (float) numStudx / (float) numStudy;
		float ratioB = (float) p.width / (float) p.height;
		float size;
		if (ratioA < ratioB) {
			// Formula, received by doing some equation calculations
			size = (p.height)
					/ (CPaintArea.SCALE_HOVER - 1 + numStudy + (numStudy + 1) * (CPaintArea.SCALE_SPACING - 1) / 2);
			spacing = size * (CPaintArea.SCALE_SPACING - 1) / 2;
			border = (size * (CPaintArea.SCALE_HOVER - 1)) / 2;
			offset_x = (p.width - (numStudx * (size + spacing) + spacing)) / 2;
			offset_y = border;
		} else {
			size = (p.width)
					/ (CPaintArea.SCALE_HOVER - 1 + numStudx + (numStudx + 1) * (CPaintArea.SCALE_SPACING - 1) / 2);
			spacing = size * (CPaintArea.SCALE_SPACING - 1) / 2;
			border = (size * (CPaintArea.SCALE_HOVER - 1)) / 2;
			offset_x = border;
			offset_y = (p.height - (numStudy * (size + spacing) + spacing)) / 2;
		}
		
		// set the shapes (the circles) according to the values calculated above
		studentCircles = new StudentCircle[numStudy][numStudx];
		for (int y = 0; y < studentCircles.length; y++) {
			for (int x = 0; x < studentCircles[0].length; x++) {
				if (students[y][x] instanceof Student) {
					studentCircles[y][x] = new StudentCircle(students[y][x], offset_x + x * (size) + (x + 1.0f) * spacing,
							offset_y + y * (size) + (y + 1) * spacing, size, size);
					studentCircles[y][x].initPizza(course.getProperties());
				} else {
					studentCircles[y][x] = new StudentCircle(students[y][x]);
				}
			}
		}
	}
	
	
	@Override
	public void updateStudents() {
		calcStudentCircles(course.getStudents());
		paintArea.updateStudentCircles(studentCircles);
		paintArea.updateHoveredStudent(hoveredStudent);
	}
	
	
	@Override
	public void updateCurrentCourse(Course course) {
		hoveredStudent = null;
		this.course = course;
		updateStudents();
	}
	
	
	@Override
	public void componentResized(ComponentEvent e) {
		updateStudents();
	}
	
	
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	
	@Override
	public void componentHidden(ComponentEvent e) {
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		
		// catch mouse points that are not in the correct range
		if (p.x < offset_x || p.y < offset_y || p.x > (this.getSize().width - offset_x)
				|| p.y > (this.getSize().height - offset_y)) {
			return;
		}
		
		// catch empty studentCircles
		if (studentCircles.length == 0) {
			hoveredStudent = null;
			course.setSelectedStudent(null);
		} else {
			// calculate position so that the correct student can be selected
			float ratiox = ((float) p.x - offset_x - spacing) / ((float) this.getSize().width - 2 * offset_x - spacing);
			float ratioy = ((float) p.y - offset_y - spacing) / ((float) this.getSize().height - 2 * offset_y - spacing);
			int x = (int) (ratiox * (float) studentCircles[0].length);
			int y = (int) (ratioy * (float) studentCircles.length);
			hoveredStudent = studentCircles[y][x];
			hoveredStudent.update();
			course.setSelectedStudent(hoveredStudent.getStudent());
			
			// check if current student is a real student and not an empty place, etc.
			if (hoveredStudent.getStudent() instanceof Student) {
				// find pizza piece that mouse clicked on
				for (PizzaPiece pizza : hoveredStudent.getPizza()) {
					if (pizza.contains(e.getPoint())) {
						int index = (int) (pizza.getAngleStart() / 360 * hoveredStudent.getPizza().size());
						course.setSelectedProperty(index);
						break;
					}
				}
			}
		}
		paintArea.updateHoveredStudent(hoveredStudent);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
		hoveredStudent = null;
		course.setSelectedStudent(null);
		paintArea.updateHoveredStudent(hoveredStudent);
		paintArea.updateStudentCircles(studentCircles);
	}
}
