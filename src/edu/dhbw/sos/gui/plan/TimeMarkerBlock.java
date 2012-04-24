/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 15, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.apache.log4j.Logger;

import edu.dhbw.sos.simulation.ITimeObserver;


/**
 * A movable block is a rectangle in a paintarea, that has some attributes for supporting
 * move operations.
 * It does not actually move itself, but it has some flags and attributes for controlling
 * movement.
 * 
 * @author NicolaiO
 * 
 */
public class TimeMarkerBlock extends Rectangle implements ITimeObserver {
	private static final long		serialVersionUID	= -1455862988755481811L;
	private static final Logger	logger				= Logger.getLogger(TimeMarkerBlock.class);
	
	// save the point, where the mouse holds the block, relative to the block itself
	private Point						relMouseLocation	= new Point();

	// color of the block
	private Color						color;
	// flags for enabling/disabling movement
	private boolean					moveHorizontal		= true;
	private boolean					moveVertical		= false;
	
	private int							time;
	private int							length;


	/**
	 * Initialize a new block with given size and color
	 * 
	 * @param size size of the block
	 * @param _color color of the block
	 * @author NicolaiO
	 */
	private TimeMarkerBlock(Dimension size, Color _color) {
		super(size);
		color = _color;
	}
	
	
	/**
	 * Initialize a new block with given size and color
	 * Additionally set the location within the parent of this block
	 * (this is given to the constructor of the rectangle
	 * @param location location within parent
	 * @param size size of the block
	 * @param _color color of the block
	 * @author NicolaiO
	 */
	private TimeMarkerBlock(Point location, Dimension size, Color _color) {
		super(location, size);
		color = _color;
	}
	
	
	public TimeMarkerBlock(int length) {
		super(new Point(-5, 135), new Dimension(10, 10));
		setColor(Color.GRAY);
		setTime(0);
		setLength(length);
	}
	

	/**
	 * Sets the location within parent
	 * This method respects the movement flags moveVertical and moveHorizontal
	 * @param p new location
	 */
	@Override
	public void setLocation(Point p) {
		int x = this.getLocation().x;
		int y = this.getLocation().y;
		if (moveHorizontal) {
			x = p.x + relMouseLocation.x;
		}
		if (moveVertical) {
			// y = p.y + relMouseLocation.y;
			y = p.y;
		}
		Point abs = new Point(x, y);
		super.setLocation(abs);
	}
	
	
	public void setLocation(double x, double y) {
		Point p = new Point();
		p.setLocation(x, y);
		setLocation(p);
	}
	
	
	public Point getRelMouseLocation() {
		return relMouseLocation;
	}
	
	
	public void setRelMouseLocation(Point relMouseLocation) {
		this.relMouseLocation = relMouseLocation;
	}
	
	
	public Color getColor() {
		return color;
	}
	
	
	public void setColor(Color color) {
		this.color = color;
	}


	public boolean isMoveHorizontal() {
		return moveHorizontal;
	}
	
	
	public void setMoveHorizontal(boolean moveHorizontal) {
		this.moveHorizontal = moveHorizontal;
	}
	
	
	public boolean isMoveVertical() {
		return moveVertical;
	}
	
	
	public void setMoveVertical(boolean moveVertical) {
		this.moveVertical = moveVertical;
	}
	
	
	public void printTbb() {
		logger.trace("TBB.time" + time + "; TMB.X=" + this.x);
	}


	/**
	 * @return the time in minutes
	 */
	public int getTime() {
		return time;
	}


	/**
	 * @param time the time to set in minutes
	 */
	public void setTime(int time) {
		this.time = time;
	}
	
	
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	
	
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}


	@Override
	public void timeChanged(int time) {
		setTime(time);
		setLocation(time - 5, this.getY());
	}
	
	
	/**
	 * TODO andres, add comment!
	 * 
	 * @author andres
	 */
	public void draw(Graphics2D ga) {
		ga.setPaint(getColor());
		ga.fill(this);
		ga.draw(this);
		ga.drawLine(getTime(), 10, getTime(), 135);
		printTbb();
	}
}
