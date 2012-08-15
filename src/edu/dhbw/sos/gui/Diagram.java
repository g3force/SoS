/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 20, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.AbstractList;
import java.util.LinkedList;


/**
 * A class for drawing a diagram.
 * It will use an float list for data and is used by calling the draw() method with a
 * Graphics2D object.
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class Diagram {
	private AbstractList<Float>	data;
	private int							height	= 0;
	private int							width		= 0;
	private Point						location	= new Point();
	/*
	 * If rescale is true, the diagram will always take the whole
	 * available space.
	 * If false, it will use maxY and maxX. This should not be zero then ;)
	 */
	private boolean					rescaleY	= true;
	private boolean					rescaleX	= true;
	private float						maxY		= 0;
	private float						maxX		= 0;
	private boolean					drawAxis	= false;
	private Polygon					arrowHead;
	private AffineTransform			aTransf	= new AffineTransform();	;
	
	
	/**
	 * Initialize the diagram with the given data
	 * You should also call some other methods like
	 * setHeight/Width, because otherwise the diagram has
	 * a zero size.
	 * However, this should be done on every update, so
	 * that the diagram can be resized, when the GUI was resized
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public Diagram(LinkedList<Float> _data) {
		data = _data;
		rescaleY = true;
		rescaleX = true;
		
		arrowHead = new Polygon();
		arrowHead.addPoint(0, 5);
		arrowHead.addPoint(-5, -5);
		arrowHead.addPoint(5, -5);
	}
	
	
	/**
	 * Draws the diagram into the paint area given by the ga object
	 * It only draws Line2D.Float lines.
	 * Things like define color and strength of line can be done
	 * before calling this method
	 * 
	 * @param ga Graphics2D object for drawing
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void draw(Graphics2D ga) {
		if (data.size() == 0 || width <= 0 || height <= 0)
			return;
		ga.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ga.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		float scalex = (float) (width - 10) / getMaxX();
		float scaley = (float) (height - 10) / getMaxY();
		
		float lastValue = data.get(0);
		float value = 0;
		int[] xPoints = new int[data.size() - 1];
		int[] yPoints = new int[data.size() - 1];
		for (int i = 1; i < data.size(); i++) {
			value = data.get(i);
			float x1 = location.x + (i - 1) * scalex;
			float y1 = (height + location.y) - lastValue * scaley;
			lastValue = value;
			xPoints[i - 1] = (int) x1;
			yPoints[i - 1] = (int) y1;
			
		}
		
		if (drawAxis) {
			Color oldColor = ga.getColor();
			ga.setColor(Color.black);
			// x axis
			Line2D.Double xAxis = new Line2D.Double(location.x, height + location.y, width + location.x, height
					+ location.y);
			ga.draw(xAxis);
			drawArrowHead(ga, xAxis);
			
			// y axis
			Line2D.Double yAxis = new Line2D.Double(location.x, height + location.y, location.x, location.y);
			ga.draw(yAxis);
			drawArrowHead(ga, yAxis);
			ga.setColor(oldColor);
		}
		
		ga.drawPolyline(xPoints, yPoints, data.size() - 1);
	}
	
	
	/**
	 * Draw an arrow at the end of the given line with the given graphics reference
	 * 
	 * @param g2d graphics to draw with
	 * @param line line of the arrow for positioning
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	private void drawArrowHead(Graphics2D g2d, Line2D.Double line) {
		aTransf.setToIdentity();
		double angle = Math.atan2(line.y2 - line.y1, line.x2 - line.x1);
		aTransf.translate(line.x2, line.y2);
		aTransf.rotate((angle - Math.PI / 2d));
		
		Graphics2D g = (Graphics2D) g2d.create();
		g.setTransform(aTransf);
		g.fill(arrowHead);
		g.dispose();
	}
	
	
	/**
	 * Return the maximum Y depending on rescaleY
	 * 
	 * @return maximum Y
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	private float getMaxY() {
		if (rescaleY) {
			float max = 1f;
			for (float val : data) {
				if (val > max)
					max = val;
			}
			return max;
		} else {
			return maxY;
		}
	}
	
	
	/**
	 * Return the maximum X depending on rescaleX
	 * 
	 * @return maximum X
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	private float getMaxX() {
		if (rescaleX) {
			return data.size();
		} else {
			return maxX;
		}
	}
	
	
	public LinkedList<Float> getData() {
		return (LinkedList<Float>) data;
	}
	
	
	public void setData(AbstractList<Float> data) {
		this.data = data;
	}
	
	
	public int getHeight() {
		return height;
	}
	
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public int getWidth() {
		return width;
	}
	
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	
	public Point getLocation() {
		return location;
	}
	
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	
	public boolean isRescaleY() {
		return rescaleY;
	}
	
	
	public void setRescaleY(boolean rescaleY) {
		this.rescaleY = rescaleY;
	}
	
	
	public boolean isRescaleX() {
		return rescaleX;
	}
	
	
	public void setRescaleX(boolean rescaleX) {
		this.rescaleX = rescaleX;
	}
	
	
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
	
	
	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}
	
	
	/**
	 * @return the drawAxis
	 */
	public boolean isDrawAxis() {
		return drawAxis;
	}
	
	
	/**
	 * @param drawAxis the drawAxis to set
	 */
	public void setDrawAxis(boolean drawAxis) {
		this.drawAxis = drawAxis;
	}
}
