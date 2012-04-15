/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.data.GUIData;


/**
 * The CoursePanel is the biggest part of the GUI.
 * It contains the students.
 * joa
 * @author NicolaiO
 * 
 */
public class CoursePanel extends JPanel implements IUpdateable, MouseListener, MouseMotionListener {
	private static final long				serialVersionUID	= 5542875796802944785L;
	private static final Logger			logger				= Logger.getLogger(CoursePanel.class);
	private static final Font				sanSerifFont		= new Font("SanSerif", Font.PLAIN, 12);
	// how much circle is scaled, when hovered
	private static final float				scaleHover			= 1.5f;
	private static final float				scaleSpacing		= 1.2f;
	private Shape[][]							studentArray;
	// space between circles
	private float								spacing;
	// diameter of a circle
	private float								size;
	/*
	 * Offset in whole Panel
	 * Will contain the border and any extra space that can not be filled,
	 * because the circles should be real circles.
	 * At least one of the offsets should be exactly equal to border
	 */
	private float								offset_x, offset_y;
	// border size around the border of the panel
	private float								border;
	// hover indicates the x and y index within the studentArray for the student
	// that should be highlighted currently
	private int									hover_x				= -1;
	private int									hover_y				= -1;
	// dummy data
	private int									testx					= 6, testy = 5;
	// circle consisting of several pizza peaces
	private LinkedList<Arc2D.Double>		circle;
	private final CoursePanelPaintArea	paintArea;
	
	
	/**
	 * @brief Initialize the CoursePanel
	 * 
	 * @param data GUIData
	 * @author NicolaiO
	 */
	public CoursePanel(GUIData data) {
		this.setBorder(MainFrame.compoundBorder);
		this.setLayout(new BorderLayout());
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setLayout(new BorderLayout());
		circle = new LinkedList<Arc2D.Double>();
		paintArea = new CoursePanelPaintArea();
		this.add(paintArea, BorderLayout.CENTER);
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
		hover_x = (int) (ratiox * (float) testx);
		hover_y = (int) (ratioy * (float) testy);
		
		// logger.debug((int) (ratiox * (float) testx));
		// logger.debug((int) (ratioy * (float) testy));
		this.repaint();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		for (Arc2D.Double pizza : circle) {
			if (pizza.contains(e.getPoint())) {
				logger.debug("blubb" + pizza.getAngleStart() / 360 * circle.size());
				break;
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
		hover_x = -1;
		hover_y = -1;
		this.repaint();
	}
	
	
	@Override
	public void update() {
		
	}
	
	private class CoursePanelPaintArea extends JPanel {
		private static final long	serialVersionUID	= 5194596384018441495L;
		
		/**
		 * 
		 * TODO NicolaiO, add comment!
		 * 
		 * @author NicolaiO
		 */
		private void setStudentArray() {
			// get available size
			Dimension p = this.getSize();
			if (p.height == 0 || p.width == 0) {
				logger.warn("Dimension has a zero value");
				return;
			}
			// compare ratio of x/y from number of circles and from available size
			// thus we can decide, if the offset has to be vertically or horizontally
			float ratioA = (float) testx / (float) testy;
			float ratioB = (float) p.width / (float) p.height;
			if (ratioA < ratioB) {
				// Formula, received by doing some equation calculations
				size = (p.height) / (scaleHover - 1 + testy + (testy + 1) * (scaleSpacing - 1) / 2);
				spacing = size * (scaleSpacing - 1) / 2;
				border = (size * (scaleHover - 1)) / 2;
				offset_x = (p.width - (testx * (size + spacing) + spacing)) / 2;
				offset_y = border;
			} else {
				size = (p.width) / (scaleHover - 1 + testx + (testx + 1) * (scaleSpacing - 1) / 2);
				spacing = size * (scaleSpacing - 1) / 2;
				border = (size * (scaleHover - 1)) / 2;
				offset_x = border;
				offset_y = (p.height - (testy * (size + spacing) + spacing)) / 2;
			}
			
			// set the shapes (the circles) according to the values calculated above
			studentArray = new Shape[testy][testx];
			for (int x = 0; x < studentArray[0].length; x++) {
				for (int y = 0; y < studentArray.length; y++) {
					studentArray[y][x] = new Ellipse2D.Float(offset_x + x * (size) + (x + 1.0f) * spacing, offset_y + y
							* (size) + (y + 1) * spacing, size, size);
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
			setStudentArray();
			Graphics2D ga = (Graphics2D) g;
			ga.clearRect(0, 0, this.getWidth(), this.getHeight());
			for (int x = 0; x < studentArray[0].length; x++) {
				for (int y = 0; y < studentArray.length; y++) {
					ga.setPaint(Color.green);
					ga.fill(studentArray[y][x]);
				}
			}
			if (hover_x < 0 || hover_x >= studentArray[0].length || hover_y < 0 || hover_y >= studentArray.length) {
				// logger.warn("paint: x or y not within index range: " + hover_x + "|" + hover_y);
				hover_x = -1;
				hover_y = -1;
			} else {
				Rectangle2D rect = studentArray[hover_y][hover_x].getBounds2D();
				float offset = ((float) rect.getWidth() * scaleHover - size) / 2;
				
				// Arc2D.Double circlePart = new Arc2D.Double(rect.getX() - offset, rect.getY() - offset, rect.getWidth() +
				// 2
				// * offset, rect.getHeight() + 2 * offset, i / debugCount * 360, 360 / debugCount, Arc2D.PIE);
				
				circle.clear();
				String[] dummyText = { "aaaaa", "bbbbb", "ccccc", "dddddd", "eeee" };
				int tmpNumber = dummyText.length;
				int arc = 360/2/tmpNumber;
				for (float i = 0; i < tmpNumber; i++) {
					// ga.setPaint((int) i % 2 == 1 ? Color.gray : Color.BLACK);
					Arc2D.Double pizza = new Arc2D.Double(rect.getX() - offset, rect.getY() - offset, rect.getWidth() + 2
							* offset, rect.getHeight() + 2 * offset, i / tmpNumber * 360, 360 / tmpNumber, Arc2D.PIE);
					ga.setColor(Color.red);
					ga.fill(pizza);
					ga.setColor(Color.black);
					ga.draw(pizza);
					
					g.setFont(sanSerifFont);
					String letter = dummyText[(int) i].substring(0, 1).toUpperCase();  
					FontMetrics fm = g.getFontMetrics();
					int w = fm.stringWidth(letter);
					int h = fm.getAscent();
					
					int x = (int) (Math.cos((double)arc*Math.PI/180) * (size*scaleHover/4));
					int y = -(int) (Math.sin((double)arc*Math.PI/180) * (size*scaleHover/4));
//					System.out.println("x:"+x+" y:"+y+"arc: "+arc+" letter:"+letter);
					arc += 360/tmpNumber;
					g.drawString(letter, (int) (rect.getX() - offset - (w / 2) + size*scaleHover/2 + x),
							(int) (rect.getY() - offset + (h / 4) + size*scaleHover/2 + y));
					
					circle.add(pizza);
				}
			}
		}
	}
}
