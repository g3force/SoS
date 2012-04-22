/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.student;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JPanel;

import edu.dhbw.sos.course.student.EmptyPlace;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.gui.Diagram;


/**
 * PaintArea for the PlanPanel.
 * This will draw the timeBlocks, etc.
 * 
 * @author NicolaiO
 * 
 */
public class SPaintArea extends JPanel {
	private static final long	serialVersionUID	= 1L;
	private Diagram				diagram;
	
	
	// private static final Logger logger = Logger.getLogger(PaintArea.class);
	
	
	/**
	 * Initialize PaintArea
	 * 
	 * @author NicolaiO
	 */
	public SPaintArea() {
		diagram = new Diagram(new LinkedList<Float>());
		diagram.setLocation(new Point(10, 10));
	}
	
	
	/**
	 * Will be called by JPanel.
	 * Will do all the drawing.
	 * Is called frequently, e.g. by repaint or if JPanel resizes, etc.
	 */
	public void paint(Graphics g) {
		// initialize
		Graphics2D ga = (Graphics2D) g;
		ga.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		// draw diagram
		ga.setColor(Color.black);
		diagram.draw(ga);
	}
	
	
	public void update(IPlace student, int parameterIndex) {
		diagram.setHeight(this.getHeight() - 20);
		diagram.setWidth(this.getWidth() - 20);
		LinkedList<Float> newData = new LinkedList<Float>();
		if (student != null || student instanceof EmptyPlace) {
			for (int key : student.getHistoryStates().keySet()) {
				newData.add(student.getHistoryStates().get(key).getValueAt(parameterIndex));
			}
		} else {
			// dummy data
			double last = 50;
			for (int i = 0; i < 50; i++) {
				last = last + ((Math.random() - 0.5) * 30.0);
				if (last < 0)
					last = 0;
				newData.add((float) last);
			}
		}
		diagram.setData(newData);
		repaint();
	}
}
