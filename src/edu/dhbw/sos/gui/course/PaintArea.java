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
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
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
	private int							hoveredStudent_x	= -1;
	private int							hoveredStudent_y	= -1;
	private static final Font		sanSerifFont		= new Font("SanSerif", Font.PLAIN, 12);
	// how much circle is scaled, when hovered
	private static final float		scaleHover			= 1.5f;
	// scaling for space between circles
	private static final float		scaleSpacing		= 1.2f;
	// student circles - not final, rather for testing
	private StudentCircle[][]		studentCircles;
	// diameter of a circle
	private float						size;
	// border size around the border of the panel
	private float						border;
	private LinkedList<String>		properties;															// FIXME maybe rather in
																															// CoursePanel
																															
																															
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public PaintArea(IPlace[][] students) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		assert(students != null);
		this.updateStudentCircles(students);
	}
	
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
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
		if (ratioA < ratioB) {
			// Formula, received by doing some equation calculations
			size = (p.height) / (scaleHover - 1 + numStudy + (numStudy + 1) * (scaleSpacing - 1) / 2);
			spacing = size * (scaleSpacing - 1) / 2;
			border = (size * (scaleHover - 1)) / 2;
			offset_x = (p.width - (numStudx * (size + spacing) + spacing)) / 2;
			offset_y = border;
		} else {
			size = (p.width) / (scaleHover - 1 + numStudx + (numStudx + 1) * (scaleSpacing - 1) / 2);
			spacing = size * (scaleSpacing - 1) / 2;
			border = (size * (scaleHover - 1)) / 2;
			offset_x = border;
			offset_y = (p.height - (numStudy * (size + spacing) + spacing)) / 2;
		}
		
		// set the shapes (the circles) according to the values calculated above
		studentCircles = new StudentCircle[numStudy][numStudx];
		for (int x = 0; x < studentCircles[0].length; x++) {
			for (int y = 0; y < studentCircles.length; y++) {
				if (students[y][x] instanceof Student) {
					studentCircles[y][x] = new StudentCircle(students[y][x], offset_x + x * (size) + (x + 1.0f) * spacing,
							offset_y + y * (size) + (y + 1) * spacing, size, size);
					int avgVal = ((Student) students[y][x]).getAverageState();
					studentCircles[y][x].setColor(StudentCircle.getColorFromValue(avgVal, 100)); // FIXME
				} else {
					studentCircles[y][x] = new StudentCircle(students[y][x]);
				}
			}
		}
	}
	
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param g
	 * @author NicolaiO
	 */
	public void paint(Graphics g) {
		Graphics2D ga = (Graphics2D) g;
		ga.clearRect(0, 0, this.getWidth(), this.getHeight());
		if(studentCircles == null || studentCircles.length == 0)
			return;
		for (int x = 0; x < studentCircles[0].length; x++) {
			for (int y = 0; y < studentCircles.length; y++) {
				ga.setPaint(studentCircles[y][x].getColor());
				ga.fill(studentCircles[y][x]);
			}
		}
		if (hoveredStudent_x < 0 || hoveredStudent_x >= studentCircles[0].length || hoveredStudent_y < 0
				|| hoveredStudent_y >= studentCircles.length) {
			// logger.warn("paint: x or y not within index range: " + hover_x + "|" + hover_y);
			hoveredStudent_x = -1;
			hoveredStudent_y = -1;
		} else {
			Rectangle2D rect = studentCircles[hoveredStudent_y][hoveredStudent_x].getBounds2D();
			float offset = ((float) rect.getWidth() * scaleHover - size) / 2;
			
			int count = properties.size();
			int arc = 360 / 2 / count;
			int i = 0;
			for (PizzaPiece pp : studentCircles[hoveredStudent_y][hoveredStudent_x].getPizza()) {
				// draw pizza piece
				ga.setColor(pp.getColor());
				ga.fill(pp);
				ga.setColor(Color.black);
				ga.draw(pp);
				
				// write text into pizza piece
				g.setFont(sanSerifFont);
				String letter = properties.get(i).substring(0, 1).toUpperCase();
				FontMetrics fm = g.getFontMetrics();
				int w = fm.stringWidth(letter);
				int h = fm.getAscent();
				
				int x = (int) (Math.cos((double) arc * Math.PI / 180) * (size * scaleHover / 4));
				int y = -(int) (Math.sin((double) arc * Math.PI / 180) * (size * scaleHover / 4));
				// System.out.println("x:"+x+" y:"+y+"arc: "+arc+" letter:"+letter);
				arc += 360 / count;
				g.drawString(letter, (int) (rect.getX() - offset - (w / 2) + size * scaleHover / 2 + x), (int) (rect.getY()
						- offset + (h / 4) + size * scaleHover / 2 + y));
				
				// increment index
				i++;
			}
		}
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		if (p.x < offset_x || p.y < offset_y || p.x > (this.getSize().width - offset_x)
				|| p.y > (this.getSize().height - offset_y)) {
			return;
		}
		float ratiox = ((float) p.x - offset_x - spacing) / ((float) this.getSize().width - 2 * offset_x - spacing);
		float ratioy = ((float) p.y - offset_y - spacing) / ((float) this.getSize().height - 2 * offset_y - spacing);
		hoveredStudent_x = (int) (ratiox * (float) studentCircles[0].length);
		hoveredStudent_y = (int) (ratioy * (float) studentCircles.length);
		
		Rectangle2D rect = studentCircles[hoveredStudent_y][hoveredStudent_x].getBounds2D();
		float offset = ((float) rect.getWidth() * scaleHover - size) / 2;
		studentCircles[hoveredStudent_y][hoveredStudent_x].initPizza(properties, offset);
		
		this.repaint();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// check if current student is a real student and not an empty place, etc.
		StudentCircle studC = studentCircles[hoveredStudent_y][hoveredStudent_y];
		if (studC != null && studC.isExistent()) {
			// find pizza piece that mouse clicked on
			for (PizzaPiece pizza : studC.getPizza()) {
				if (pizza.contains(e.getPoint())) {
					int index = (int) (pizza.getAngleStart() / 360 * studC.getPizza().size());
					int value = 10;
					// right click
					if (e.getButton() == MouseEvent.BUTTON3)
						value *= -1;
					((Student) studC.getStudent()).donInput(index, value);
					studC.updatePizza();
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
		hoveredStudent_x = -1;
		hoveredStudent_y = -1;
		this.repaint();
	}
	
	
	public LinkedList<String> getProperties() {
		return properties;
	}
	
	
	public void setProperties(LinkedList<String> properties) {
		this.properties = properties;
	}
}