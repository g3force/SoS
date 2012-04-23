/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 20, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.LinkedList;


/**
 * A class for drawing a diagram.
 * It will use an float list for data and is used by calling the draw() method with a
 * Graphics2D object.
 * 
 * @author NicolaiO
 * 
 */
public class Diagram {
	private LinkedList<Float>	data;
	private int						height	= 0;
	private int						width		= 0;
	private Point					location	= new Point();
	/*
	 * If rescale is true, the diagram will always take the whole
	 * available space.
	 * If false, it will use maxY and maxX. This should not be zero then ;)
	 */
	private boolean				rescaleY	= true;
	private boolean				rescaleX	= true;
	private float					maxY		= 0;
	private float					maxX		= 0;
	
	
	/**
	 * Initialize the diagram with the given data
	 * You should also call some other methods like
	 * setHeight/Width, because otherwise the diagram has
	 * a zero size.
	 * However, this should be done on every update, so
	 * that the diagram can be resized, when the GUI was resized
	 * 
	 * @author NicolaiO
	 */
	public Diagram(LinkedList<Float> _data) {
		data = _data;
		rescaleY = true;
		rescaleX = true;
	}
	
	
	/**
	 * Draws the diagram into the paint area given by the ga object
	 * It only draws Line2D.Float lines.
	 * Things like define color and strength of line can be done
	 * before calling this method
	 * 
	 * @param ga Graphics2D object for drawing
	 * @author NicolaiO
	 */
	public void draw(Graphics2D ga) {
		if (data.size() == 0 || width <= 0 || height <= 0)
			return;
		ga.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ga.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		float scalex = (float) width / getMaxX();
		float scaley = (float) height / getMaxY();
		
		float lastValue = data.get(0);
		float value = 0;
		int[] xPoints = new int[data.size() - 1];
		int[] yPoints = new int[data.size() - 1];
		for (int i = 1; i < data.size(); i++) {
			value = data.get(i);
			float x1 = location.x + (i - 1) * scalex;
			// float x2 = location.x + (i) * scalex;
			float y1 = (height + location.y) - lastValue * scaley;
			// float y2 = (height + location.y) - value * scaley;
			// System.out.printf("%f %f %f %f\n", x1, y1, x2, y2);
			// Line2D.Float l = new Line2D.Float(x1, y1, x2, y2);
			// ga.draw(l);
			lastValue = value;
			xPoints[i - 1] = (int) x1;
			yPoints[i - 1] = (int) y1;
			
		}
		ga.drawPolyline(xPoints, yPoints, data.size() - 1);
	}
	
	
	/**
	 * Return the maximum Y depending on rescaleY
	 * 
	 * @return maximum Y
	 * @author NicolaiO
	 */
	private float getMaxY() {
		if (rescaleY) {
			float max = 0;
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
	 * @author NicolaiO
	 */
	private float getMaxX() {
		if (rescaleX) {
			return data.size();
		} else {
			return maxX;
		}
	}
	
	
	public LinkedList<Float> getData() {
		return data;
	}
	
	
	public void setData(LinkedList<Float> data) {
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
	
	
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
}
