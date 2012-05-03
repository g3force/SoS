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

import edu.dhbw.sos.observers.ITimeObserver;


/**
 * TODO andres
 * 
 * @author andres
 * 
 */
public class TimeMarkerBlock extends Rectangle implements ITimeObserver {
	private static final long		serialVersionUID	= -1455862988755481811L;
	
	// save the point, where the mouse holds the block, relative to the block itself
	private Point						relMouseLocation	= new Point();

	// color of the block
	private Color						color;
	// flags for enabling/disabling movement
	private boolean					moveHorizontal;
	private boolean					moveVertical;
	
	private int							time;

	
	public TimeMarkerBlock() {
		super(new Point(-5, 135), new Dimension(10, 10));
		moveHorizontal = true;
		moveVertical = false;
		setColor(Color.GRAY);
		setTime(0);
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
			y = p.y + relMouseLocation.y;
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
	
	
	@Override
	public String toString() {
		return "TBB.time" + time + "; TMB.X=" + this.x;
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
	 * @param time time in milliseconds
	 */
	@Override
	public void timeChanged(int time) {
		setTime(time / 60000);
	}
	
	
	/**
	 * Draw the TimeMarker
	 * 
	 * @param Graphics2D
	 * @author andres
	 */
	public void draw(Graphics2D ga, double scaleRatio) {
		setLocation((int) (getTime() * scaleRatio) - 5, this.getY());
		ga.setPaint(getColor());
		ga.fill(this);
		ga.drawLine((int) (getTime() * scaleRatio), 10, (int) (getTime() * scaleRatio), 135); // FIXME andres constants
	}
}
