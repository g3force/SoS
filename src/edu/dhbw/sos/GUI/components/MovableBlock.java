/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 15, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.GUI.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import edu.dhbw.sos.data.TimeBlock;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class MovableBlock extends Rectangle {
	private static final long	serialVersionUID	= 4199245975719637289L;
	private boolean				moveable				= false;
	private Point					relMouseLocation	= new Point();
	private TimeBlock				timeBlock;
	private Color					color;
	private boolean				moveHorizontal		= false;
	private boolean				moveVertical		= true;
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public MovableBlock(Dimension size, Color _color) {
		super(size);
		color = _color;
	}
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public MovableBlock(Point location, Dimension size, Color _color) {
		super(location, size);
		color = _color;
	}
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public MovableBlock(Point location, Dimension size, Color _color, TimeBlock tb) {
		super(location, size);
		color = _color;
		timeBlock = tb;
	}
	
	
	@Override
	public void setLocation(Point p) {
		int x = this.getLocation().x;
		int y = this.getLocation().y;
		if (moveVertical) {
			x = p.x + relMouseLocation.x;
		}
		if (moveHorizontal) {
			y = p.y + relMouseLocation.y;
		}
		Point abs = new Point(x, y);
		super.setLocation(abs);
	}
	
	
	public boolean isMoveable() {
		return moveable;
	}
	
	
	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
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
	
}
