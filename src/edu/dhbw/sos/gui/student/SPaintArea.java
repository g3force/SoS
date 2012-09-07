/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.student;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.swing.JPanel;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.gui.Diagram;
import edu.dhbw.sos.helper.CalcVector;


/**
 * PaintArea for the PlanPanel.
 * This will draw the timeBlocks, etc.
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class SPaintArea extends JPanel {
	private static final long		serialVersionUID	= 1L;
	private static final Color		COLORS[]				= { Color.black, Color.blue, Color.green, Color.yellow, Color.red,
			Color.cyan, Color.magenta						};
	private LinkedList<Diagram>	diagrams				= new LinkedList<Diagram>();
	
	
	// private static final Logger logger = Logger.getLogger(PaintArea.class);
	
	
	/**
	 * Initialize PaintArea
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public SPaintArea() {
	}
	
	
	/**
	 * Will be called by JPanel.
	 * Will do all the drawing.
	 * Is called frequently, e.g. by repaint or if JPanel resizes, etc.
	 */
	@Override
	public void paint(Graphics g) {
		// initialize
		Graphics2D ga = (Graphics2D) g;
		ga.setColor(getBackground());
		ga.clearRect(0, 0, this.getWidth(), this.getHeight());
		ga.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// draw diagram
		int i = 0;
		synchronized (diagrams) {
			for (Diagram diagram : diagrams) {
				if (i < COLORS.length) {
					ga.setColor(COLORS[i]);
				} else {
					ga.setColor(Color.black);
				}
				diagram.draw(ga);
				i++;
			}
		}
	}
	
	
	/**
	 * Initialize diagrams with data from given student with given parameter.
	 * 
	 * @param student
	 * @param parameterIndex
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void update(IPlace student, int parameterIndex, Course course) {
		if (student != null) {
			Diagram diagram;
			Diagram avgDiagram;
			synchronized (diagrams) {
				if (diagrams.size() >= 1) {
					diagram = diagrams.getFirst();
				} else {
					diagram = new Diagram(new LinkedList<Float>());
					diagram.setLocation(new Point(10, 10));
					diagram.setMaxY(100.0f);
					diagram.setRescaleY(false);
					diagram.setDrawAxis(true);
				}
				diagram.setHeight(this.getHeight() - 20);
				diagram.setWidth(this.getWidth() - 20);
				
				// Diagram with average data from selected data
				if (diagrams.size() >= 2) {
					avgDiagram = diagrams.getLast();
				} else {
					avgDiagram = new Diagram(new LinkedList<Float>());
					avgDiagram.setLocation(new Point(10, 10));
					avgDiagram.setMaxY(100.0f);
					avgDiagram.setRescaleY(false);
					avgDiagram.setDrawAxis(true);
				}
				avgDiagram.setHeight(this.getHeight() - 20);
				avgDiagram.setWidth(this.getWidth() - 20);
				
				// if (diagrams.size() > 2) {
				diagrams.clear();
				diagrams.add(diagram);
				diagrams.add(avgDiagram);
				// }
			}
			
			// Data student
			LinkedList<Float> newData = new LinkedList<Float>();
			synchronized (student.getHistoryStates()) {
				for (int key : student.getHistoryStates().keySet()) {
					newData.add(student.getHistoryStates().get(key).getValueAt(parameterIndex));
				}
			}
			diagram.setData(newData);
			
			// Data average
			LinkedList<Float> avgNewData = new LinkedList<Float>();
			for (Entry<Integer, CalcVector> cv : course.getHistStatAvgStudentStates().entrySet()) {
				avgNewData.add(cv.getValue().getValueAt(parameterIndex));
			}

			avgDiagram.setData(avgNewData);
		}
		repaint();
	}
	
	
	/**
	 * Rebuild diagrams.
	 * 
	 * @param course
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void update(Course course) {
		synchronized (diagrams) {
			synchronized (course) {
				diagrams.clear();
				try {
					// For each parameter a seperate diagramm
					// int size = course.getHistStatAvgStudentStates().values().iterator().next().size();
					// LinkedList<LinkedList<Float>> newData = new LinkedList<LinkedList<Float>>();
					// for (int i = 0; i < size; i++) {
					// newData.add(new LinkedList<Float>());
					// Diagram diagram = new Diagram(new LinkedList<Float>());
					// diagram.setHeight(this.getHeight() - 20);
					// diagram.setWidth(this.getWidth() - 20);
					// diagram.setData(newData.get(i));
					// diagram.setLocation(new Point(10, 10));
					// diagram.setDrawAxis(true);
					// diagram.setMaxY(100.0f);
					// diagram.setRescaleY(false);
					// diagrams.add(diagram);
					// }
					// for (Entry<Integer, CalcVector> cv : course.getHistStatAvgStudentStates().entrySet()) {
					// for (int i = 0; i < cv.getValue().size(); i++) {
					// newData.get(i).add(cv.getValue().getValueAt(i));
					// }
					// }
					// if (newData.get(0).isEmpty()) {
					// newData.get(0).add(0f);
					// }
					
					// An average diagramm for all parameters
					LinkedList<Float> newData = new LinkedList<Float>();
					Diagram diagram = new Diagram(new LinkedList<Float>());
					diagram.setHeight(this.getHeight() - 20);
					diagram.setWidth(this.getWidth() - 20);
					diagram.setData(newData);
					diagram.setLocation(new Point(10, 10));
					diagram.setDrawAxis(true);
					diagram.setMaxY(100.0f);
					diagram.setRescaleY(false);
					diagrams.add(diagram);
					
					for (Entry<Integer, CalcVector> cv : course.getHistStatAvgStudentStates().entrySet()) {
						Float val = 0F;
						int size = cv.getValue().size();
						for (int i = 0; i < size; i++) {
							// sum values form parameters
							val += cv.getValue().getValueAt(i);
						}
						// Add average
						newData.add(val / size);
						
					}
					if (newData.isEmpty()) {
						newData.add(0f);
					}
				} catch (NoSuchElementException e) {
					// well, then no diagrams...
				}
				if (diagrams.isEmpty()) {
					
					LinkedList<Float> newData = new LinkedList<Float>();
					newData.add(0f);
					Diagram diagram = new Diagram(newData);
					diagram.setHeight(this.getHeight() - 20);
					diagram.setWidth(this.getWidth() - 20);
					diagram.setLocation(new Point(10, 10));
					diagram.setDrawAxis(true);
					diagram.setMaxY(100.0f);
					diagram.setRescaleY(false);
					diagrams.add(diagram);
				}
				repaint();
			}
		}
	}
	
	
	/**
	 * 
	 * Make average diagram with one selected parameter
	 * 
	 * @param course
	 * @param parameterIndex
	 * @author andres
	 */
	public void update(Course course, int parameterIndex) {
		if (parameterIndex < 0)
			update(course);
		synchronized (diagrams) {
			synchronized (course) {
				diagrams.clear();
				try {
					// An average diagramm for all parameters
					LinkedList<Float> newData = new LinkedList<Float>();
					Diagram diagram = new Diagram(new LinkedList<Float>());
					diagram.setHeight(this.getHeight() - 20);
					diagram.setWidth(this.getWidth() - 20);
					diagram.setData(newData);
					diagram.setLocation(new Point(10, 10));
					diagram.setDrawAxis(true);
					diagram.setMaxY(100.0f);
					diagram.setRescaleY(false);
					diagrams.add(diagram);

					for (Entry<Integer, CalcVector> cv : course.getHistStatAvgStudentStates().entrySet()) {
						newData.add(cv.getValue().getValueAt(parameterIndex));
					}
					if (newData.isEmpty()) {
						newData.add(0f);
					}
				} catch (NoSuchElementException e) {
					// well, then no diagrams...
				}
				if (diagrams.isEmpty()) {
					
					LinkedList<Float> newData = new LinkedList<Float>();
					newData.add(0f);
					Diagram diagram = new Diagram(newData);
					diagram.setHeight(this.getHeight() - 20);
					diagram.setWidth(this.getWidth() - 20);
					diagram.setLocation(new Point(10, 10));
					diagram.setDrawAxis(true);
					diagram.setMaxY(100.0f);
					diagram.setRescaleY(false);
					diagrams.add(diagram);
				}
				repaint();
			}
		}
	}
}
