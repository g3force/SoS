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

import edu.dhbw.sos.course.lecture.TimeBlock;


/**
 * A movable block is a rectangle in a paintarea, that has some attributes for supporting
 * move operations.
 * It does not actually move itself, but it has some flags and attributes for controlling
 * movement.
 * 
 * @author NicolaiO
 * 
 */
public class MovableBlock extends Rectangle {
	private static final long		serialVersionUID	= 4199245975719637289L;
	private static final Logger	logger				= Logger.getLogger(MovableBlock.class);
	
	// save the point, where the mouse holds the block, relative to the block itself
	private Point						relMouseLocation	= new Point();
	// corresponding timeblock object for this block
	private TimeBlock					timeBlock;
	// color of the block
	private Color						color;
	// flags for enabling/disabling movement
	private boolean					moveHorizontal		= true;
	private boolean					moveVertical		= true;
	
	// Position in LinkedList
	private int							index;
	// private double X = 0;
	// private double Y = 0;
	private double						width					= 0;
	private double						height				= 0;
	
	
	/**
	 * Initialize a new block with given size and color
	 * 
	 * @param size size of the block
	 * @param _color color of the block
	 * @author NicolaiO
	 */
	public MovableBlock(Dimension size, Color _color) {
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
	public MovableBlock(Point location, Dimension size, Color _color) {
		super(location, size);
		color = _color;
	}
	
	
	/**
	 * Initialize a new block with given size and color
	 * Additionally set the location within the parent of this block
	 * (this is given to the constructor of the rectangle
	 * Additionally set the corresponding timeblock
	 * 
	 * @param location location within parent
	 * @param size size of the block
	 * @param _color color of the block
	 * @param tb reference to the corresponding timeblock
	 * @author NicolaiO
	 */
	public MovableBlock(Point location, Dimension size, Color _color, TimeBlock tb) {
		super(location, size);
		color = _color;
		timeBlock = tb;
	}
	
	
	/**
	 * Sets the location within parent
	 * This method respects the movement flags moveVertical and moveHorizontal
	 * @param p new location
	 */
	@Override
	public void setLocation(Point p) {
		// default values
		int x = this.getLocation().x;
		int y = this.getLocation().y;
		if (moveHorizontal) {
			x = p.x;
		}
		if (moveVertical) {
			y = p.y;
		}
		Point abs = new Point(x, y);
		super.setLocation(abs);
	}
	
	
	// @Deprecated
	// public void setLocationRelMouse(Point p) {
	// // default values
	// int x = this.getLocation().x;
	// int y = this.getLocation().y;
	// if (moveHorizontal) {
	// x = p.x - relMouseLocation.x;
	// }
	// if (moveVertical) {
	// y = p.y - relMouseLocation.y;
	// }
	// Point abs = new Point(x, y);
	// super.setLocation(abs);
	// }
	
	
	public void setLocation(double x, double y) {
		// this.X = x;
		// this.Y = y;
		this.setLocation(new Point((int) x, (int) y));
	}
	
	
	public void setX(double x) {
		setLocation(x, this.getY());
	}
	
	
	public void setY(double y) {
		setLocation(this.getX(), y);
	}
	

	public Point getRelMouseLocation() {
		return relMouseLocation;
	}
	
	
	public void setRelMouseLocation(Point relMouseLocation) {
		this.relMouseLocation = relMouseLocation;
	}
	
	
	public TimeBlock getTimeBlock() {
		return timeBlock;
	}
	
	
	public void setTimeBlock(TimeBlock timeBlock) {
		this.timeBlock = timeBlock;
	}
	
	
	public Color getColor() {
		return color;
	}
	
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	public int getIndex() {
		return index;
	}
	
	
	public void setIndex(int index) {
		this.index = index;
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
	
	
	public void setWidth(double width) {
		this.width = width;
		super.width = (int) width;
	}
	
	
	public void addWidth(double width) {
		this.setWidth(this.getWidth() + width);
	}
	

	public void printMbTb(int index, String pos) {
		logger.trace(pos + "; MB.index=" + index + "; TB.len=" + timeBlock.getLen() + "; MB.width=" + this.width
				+ "; MB.X=" + this.x);
	}
	
	
	public Areas containsArea(Point p) {
		Areas area = null;
		if (p.getY() >= this.getY() && p.getY() < this.getY() + this.getHeight()) {
			if (p.getX() >= this.getX()) {
				if (p.getX() < this.getX() + 8)
					area = Areas.BorderLeft;
				else if (p.getX() < this.getX() + this.getWidth() - 8)
					area = Areas.InArea;
				else if (p.getX() < this.getX() + this.getWidth())
					area = Areas.BorderRight;
				else
					area = Areas.NotInArea;
			} else
				area = Areas.NotInArea;
		} else
			area = Areas.NotInArea;
		return area;
	}
	
	static public enum Areas {
		BorderLeft,
		BorderRight,
		NotInArea,
		InArea
	};
	
	
	public void draw(Graphics2D ga) {
		ga.setPaint(this.getColor());
		ga.fill(this);
	}

	
	@Override
	public double getX() {
		return x;
	}
	
	
	@Override
	public double getY() {
		return y;
	}

}