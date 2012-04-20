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
import java.awt.geom.Line2D;
import java.util.LinkedList;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class Diagram {
	private LinkedList<Integer>	data;
	private int							height	= 0;
	private int							width		= 0;
	private Point						location	= new Point();
	private boolean					rescaleY	= true;
	private boolean					rescaleX	= true;
	private int							maxY		= 0;
	private int							maxX		= 0;
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public Diagram(LinkedList<Integer> _data) {
		data = _data;
		rescaleY = true;
		rescaleX = true;
	}
	
	
	public void draw(Graphics2D ga) {
		if (data.size() == 0 || width <= 0 || height <= 0)
			return;
		
		float scalex = (float) width / (float) getMaxX();
		float scaley = (float) height / (float) getMaxY();
		
		float lastValue = data.get(0);
		float value = 0;
		for (int i = 1; i < data.size(); i++) {
			value = data.get(i);
			float x1 = location.x + (i - 1) * scalex;
			float x2 = location.x + (i) * scalex;
			float y1 = (height + location.y) - lastValue * scaley;
			float y2 = (height + location.y) - value * scaley;
//			System.out.printf("%f %f %f %f\n", x1, y1, x2, y2);
			Line2D.Float l = new Line2D.Float(x1, y1, x2, y2);
			ga.draw(l);
			lastValue = value;
		}
	}
	
	
	private int getMaxY() {
		if (rescaleY) {
			int max = 0;
			for (int val : data) {
				if (val > max)
					max = val;
			}
			return max;
		} else {
			return maxY;
		}
	}
	
	
	private int getMaxX() {
		if (rescaleX) {
			return data.size();
		} else {
			return maxX;
		}
	}
	
	
	public LinkedList<Integer> getData() {
		return data;
	}
	
	
	public void setData(LinkedList<Integer> data) {
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
