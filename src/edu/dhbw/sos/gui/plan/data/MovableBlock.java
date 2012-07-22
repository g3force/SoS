/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 15, 2012
 * Author(s): NicolaiO
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
 * @author NicolaiO
 * 
 */
public class MovableBlock {
	@SuppressWarnings("unused")
	private static final Logger	logger			= Logger.getLogger(MovableBlock.class);
	private static final int		AREA_OFFSET		= 8;
	
	private final Rectangle			rectangle;
	// save the point, where the mouse holds the block, relative to the block itself
	// private Point relMouseLocation = new Point();
	// corresponding timeblock object for this block
	// private final TimeBlock timeBlock;
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
	 * @author NicolaiO
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
	 * @author NicolaiO
	 */
	public MovableBlock(Point location, Dimension size, Color color) {
		this.rectangle = new Rectangle(location, size);
		this.color = color;
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
	// public MovableBlock(Point location, Dimension size, Color _color, TimeBlock tb) {
	// super(location, size);
	// color = _color;
	// timeBlock = tb;
	// }
	
	
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
	
	
	public void addWidth(int width) {
		this.rectangle.width += width;
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
	
	public void setWidth(int width) {
		this.rectangle.width = width;
	}
	
	
	public void setX(int x) {
		this.setLocation(new Point(x, this.rectangle.y));
	}
	
	
	public void setY(int y) {
		this.setLocation(new Point(this.rectangle.x, y));
	}
	
	
	// public Point getRelMouseLocation() {
	// return relMouseLocation;
	// }
	//
	//
	// public void setRelMouseLocation(Point relMouseLocation) {
	// this.relMouseLocation = relMouseLocation;
	// }
	
	
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