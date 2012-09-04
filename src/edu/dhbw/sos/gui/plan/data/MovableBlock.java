/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 15, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.apache.log4j.Logger;


/**
 * A movable block is a rectangle in a paintarea, that has some attributes for supporting
 * move operations.
 * It does not actually move itself, but it has some flags and attributes for controlling.
 * movement.
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class MovableBlock {
	@SuppressWarnings("unused")
	private static final Logger	logger			= Logger.getLogger(MovableBlock.class);
	private static final int		AREA_OFFSET		= 8;
	
	// rectangle is the base of the block
	private final Rectangle			rectangle;
	// color of the block
	private Color						color;
	// flags for enabling/disabling movement
	private boolean					moveHorizontal	= true;
	private boolean					moveVertical	= true;
	
	static public enum Areas {
		BorderLeft,
		BorderRight,
		NotInArea,
		InArea
	};
	

	/**
	 * Initialize a new block with given size and color
	 * 
	 * @param size size of the block
	 * @param _color color of the block
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public MovableBlock(Dimension size, Color color) {
		this(new Point(0, 0), size, color);
	}
	
	
	/**
	 * Initialize a new block with given size and color
	 * Additionally set the location within the parent of this block
	 * (this is given to the constructor of the rectangle
	 * @param location location within parent
	 * @param size size of the block
	 * @param _color color of the block
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public MovableBlock(Point location, Dimension size, Color color) {
		this.rectangle = new Rectangle(location, size);
		this.color = color;
	}
	
	
	/**
	 * Draw the block into the given Graphics object
	 * 
	 * @param ga
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void draw(Graphics2D ga) {
		ga.setPaint(this.getColor());
		ga.fill(this.rectangle);
		ga.setPaint(Color.black);
		ga.draw(this.rectangle);
	}


	/**
	 * Sets the location within parent
	 * This method respects the movement flags moveVertical and moveHorizontal
	 * @param p new location
	 */
	public void setLocation(Point p) {
		// default values
		int x = this.rectangle.getLocation().x;
		int y = this.rectangle.getLocation().y;
		if (moveHorizontal) {
			x = p.x;
		}
		if (moveVertical) {
			y = p.y;
		}
		Point abs = new Point(x, y);
		this.rectangle.setLocation(abs);
	}
	
	
	/**
	 * Check, if given point is in a special area
	 * 
	 * @param p point to be checked
	 * @return the Area, p is in
	 * @author andres
	 */
	public Areas containsArea(Point p) {
		if (p.y >= this.rectangle.y && p.y < this.rectangle.y + this.rectangle.height) {
			if (p.x >= this.rectangle.x) {
				if (p.x < this.rectangle.x + AREA_OFFSET)
					return Areas.BorderLeft;
				else if (p.x < this.rectangle.x + this.rectangle.width - AREA_OFFSET)
					return Areas.InArea;
				else if (p.x < this.rectangle.x + this.rectangle.width)
					return Areas.BorderRight;
			}
		}
		return Areas.NotInArea;
	}
	
	
	/**
	 * Add width to the width of the block
	 * 
	 * @param width
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public void addWidth(int width) {
		this.rectangle.width += width;
	}

	
	public void setWidth(int width) {
		this.rectangle.width = width;
	}
	
	
	public void setX(int x) {
		this.setLocation(new Point(x, this.rectangle.y));
	}
	
	
	public void setY(int y) {
		this.setLocation(new Point(this.rectangle.x, y));
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
	
	
	@Override
	public String toString() {
		return "MoveableBlock: rectangle=" + rectangle.toString() + " color=" + color.toString() + " moveVertical="
				+ moveVertical + " moveHorizontal=" + moveHorizontal;
	}
	
}