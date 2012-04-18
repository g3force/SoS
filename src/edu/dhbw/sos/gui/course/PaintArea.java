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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;


/**
 * Paint Area for CoursePanel
 * This will draw the students, etc.
 * 
 * @author NicolaiO
 * 
 */
public class PaintArea extends JPanel implements MouseListener, MouseMotionListener {
	private static final long		serialVersionUID	= 5194596384018441495L;
	private static final Logger	logger				= Logger.getLogger(PaintArea.class);
	// space between circles
	private float						spacing;
	/*
	 * Offset in whole Panel
	 * Will contain the border and any extra space that can not be filled,
	 * because the circles should be real circles.
	 * At least one of the offsets should be exactly equal to border
	 */
	private float						offset_x, offset_y;
	// hover indicates the x and y index within the studentArray for the student
	// that should be highlighted currently
	private StudentCircle			hoveredStudent		= null;
	private static final Font		sanSerifFont		= new Font("SanSerif", Font.PLAIN, 12);
	// how much circle is scaled, when hovered
	public static final float		SCALE_HOVER			= 1.5f;
	// scaling for space between circles
	public static final float		SCALE_SPACING		= 1.2f;
	// student circles - not final, rather for testing
	private StudentCircle[][]		studentCircles;
	// border size around the border of the panel
	private float						border;
	
	
	/**
	 * Create a new PaintArea
	 * 
	 * @author NicolaiO
	 */
	public PaintArea() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		studentCircles = new StudentCircle[0][0];
	}
	
	
	/**
	 * Set and update all important values for the student circles
	 * 
	 * @author NicolaiO
	 */
	public void updateStudentCircles(IPlace[][] students) {
		// get available size
		Dimension p = this.getSize();
		if (p.height == 0 || p.width == 0) {
			logger.warn("Dimension has a zero value");
			return;
		}
		// get num of student rows and cols from students array
		int numStudy = students.length;
		if (numStudy == 0) {
			logger.error("there are no students!");
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
			size = (p.height) / (SCALE_HOVER - 1 + numStudy + (numStudy + 1) * (SCALE_SPACING - 1) / 2);
			spacing = size * (SCALE_SPACING - 1) / 2;
			border = (size * (SCALE_HOVER - 1)) / 2;
			offset_x = (p.width - (numStudx * (size + spacing) + spacing)) / 2;
			offset_y = border;
		} else {
			size = (p.width) / (SCALE_HOVER - 1 + numStudx + (numStudx + 1) * (SCALE_SPACING - 1) / 2);
			spacing = size * (SCALE_SPACING - 1) / 2;
			border = (size * (SCALE_HOVER - 1)) / 2;
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
				} else {
					studentCircles[y][x] = new StudentCircle(students[y][x]);
				}
			}
		}
		repaint();
	}
	
	
	/**
	 * Update any data that uses the properties
	 * this is:
	 * * pizzas of studentCircles
	 * 
	 * @param properties list of properties
	 * @author NicolaiO
	 */
	public void updateProperties(LinkedList<String> properties) {
		for (int y = 0; y < studentCircles.length; y++) {
			for (int x = 0; x < studentCircles[0].length; x++) {
				studentCircles[y][x].initPizza(properties);
			}
		}
		repaint();
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
				float offset = (float) (hoveredStudent.getBounds2D().getWidth() * (PaintArea.SCALE_HOVER - 1)) / 2;
				g.drawString(pp.getFirstLetter(), (int) (hoveredStudent.getBounds2D().getX() - offset - (w / 2)
						+ hoveredStudent.getWidth() * SCALE_HOVER / 2 + x), (int) (hoveredStudent.getBounds2D().getY()
						- offset + (h / 4) + hoveredStudent.getHeight() * SCALE_HOVER / 2 + y));
			}
		}
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
		} else {
			// calculate position so that the correct student can be selected
			float ratiox = ((float) p.x - offset_x - spacing) / ((float) this.getSize().width - 2 * offset_x - spacing);
			float ratioy = ((float) p.y - offset_y - spacing) / ((float) this.getSize().height - 2 * offset_y - spacing);
			int x = (int) (ratiox * (float) studentCircles[0].length);
			int y = (int) (ratioy * (float) studentCircles.length);
			hoveredStudent = studentCircles[y][x];
			hoveredStudent.update();
		}
		this.repaint();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// check if current student is a real student and not an empty place, etc.
		StudentCircle studC = hoveredStudent;
		if (studC != null && studC.getStudent() instanceof Student) {
			// find pizza piece that mouse clicked on
			for (PizzaPiece pizza : studC.getPizza()) {
				if (pizza.contains(e.getPoint())) {
					int index = (int) (pizza.getAngleStart() / 360 * studC.getPizza().size());
					int value = 10;
					// right click
					if (e.getButton() == MouseEvent.BUTTON3)
						value *= -1;
					((Student) studC.getStudent()).donInput(index, value);
					studC.update();
					this.repaint();
					break;
				}
			}
		}
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
		this.repaint();
	}
}