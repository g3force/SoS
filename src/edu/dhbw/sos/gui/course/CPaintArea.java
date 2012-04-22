/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.course;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import edu.dhbw.sos.course.student.Student;


/**
 * Paint Area for CoursePanel
 * This will draw the students, etc.
 * 
 * @author NicolaiO
 * 
 */
public class CPaintArea extends JPanel {
	private static final long	serialVersionUID	= 5194596384018441495L;
	// private static final Logger logger = Logger.getLogger(CPaintArea.class);
	private static final Font	sanSerifFont		= new Font("SanSerif", Font.PLAIN, 12);
	// how much circle is scaled, when hovered
	public static final float	SCALE_HOVER			= 1.5f;
	// scaling for space between circles
	public static final float	SCALE_SPACING		= 1.2f;
	// student circles
	private StudentCircle[][]	studentCircles;
	// student that should be highlighted at the moment
	private StudentCircle		hoveredStudent		= null;
	
	
	/**
	 * Create a new PaintArea
	 * 
	 * @author NicolaiO
	 */
	public CPaintArea() {
		studentCircles = new StudentCircle[0][0];
	}
	
	
	public void updateStudentCircles(StudentCircle[][] studentCircles) {
		this.studentCircles = studentCircles;
		this.repaint();
	}
	
	
	public void updateHoveredStudent(StudentCircle hoveredStudent) {
		this.hoveredStudent = hoveredStudent;
		if (this.hoveredStudent != null)
			this.hoveredStudent.update();
		this.repaint();
	}
	
	
	/**
	 * Paint will do all the drawing of the component
	 * Data should only be used here, not modified, if it is possible
	 * 
	 * @param g
	 * @author NicolaiO
	 */
	public void paint(Graphics g) {
		// get a 2D graphics object for better drawing
		Graphics2D ga = (Graphics2D) g;
		
		// Initialize
		ga.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		// draw all students
		for (int y = 0; y < studentCircles.length; y++) {
			for (int x = 0; x < studentCircles[0].length; x++) {
				if (studentCircles[y][x].getStudent() instanceof Student) {
					ga.setPaint(studentCircles[y][x].getColor());
					ga.fill(studentCircles[y][x]);
				}
			}
		}
		
		// if a students is hovered by mouse, draw the pizza
		if (hoveredStudent != null && hoveredStudent.getPizza().size() != 0) {
			// set the angle of pizza pieces
			int angle = 360 / 2 / hoveredStudent.getPizza().size();
			for (PizzaPiece pp : hoveredStudent.getPizza()) {
				// draw pizza piece
				ga.setColor(pp.getColor());
				ga.fill(pp);
				ga.setColor(Color.black);
				ga.draw(pp);
				
				// write text into pizza piece
				ga.setFont(sanSerifFont);
				FontMetrics fm = g.getFontMetrics();
				int w = fm.stringWidth(pp.getFirstLetter());
				int h = fm.getAscent();
				
				int x = (int) (Math.cos((double) angle * Math.PI / 180) * (hoveredStudent.getWidth() * SCALE_HOVER / 4));
				int y = -(int) (Math.sin((double) angle * Math.PI / 180) * (hoveredStudent.getHeight() * SCALE_HOVER / 4));
				// System.out.println("x:"+x+" y:"+y+"arc: "+arc+" letter:"+letter);
				angle += 360 / hoveredStudent.getPizza().size();
				float offset = (float) (hoveredStudent.getBounds2D().getWidth() * (CPaintArea.SCALE_HOVER - 1)) / 2;
				g.drawString(pp.getFirstLetter(), (int) (hoveredStudent.getBounds2D().getX() - offset - (w / 2)
						+ hoveredStudent.getWidth() * SCALE_HOVER / 2 + x), (int) (hoveredStudent.getBounds2D().getY()
						- offset + (h / 4) + hoveredStudent.getHeight() * SCALE_HOVER / 2 + y));
			}
		}
	}
}