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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class MoveableBlock extends Rectangle {
	private static final long	serialVersionUID	= 4199245975719637289L;
	private boolean				moveable				= false;
	private Point					relMouseLocation	= new Point();
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public MoveableBlock(Dimension size) {
		super(size);
	}
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public MoveableBlock(Point location, Dimension size) {
		super(location, size);
	}
	
	
	@Override
	public void setLocation(Point p) {
		Point abs = new Point(p.x + relMouseLocation.x, p.y + relMouseLocation.y);
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
	
}
