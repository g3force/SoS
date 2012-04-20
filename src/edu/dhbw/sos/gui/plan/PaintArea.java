/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;


/**
 * PaintArea for the PlanPanel.
 * This will draw the timeBlocks, etc.
 * 
 * @author NicolaiO
 * 
 */
public class PaintArea extends JPanel implements MouseListener, MouseMotionListener {
	private static final long			serialVersionUID	= 5194596384018441495L;
	private static final Logger		logger				= Logger.getLogger(PaintArea.class);
	
	// list of all movable blocks
	private LinkedList<MovableBlock>	movableBlocks		= new LinkedList<MovableBlock>();
	// this block is set, when a block is moved by dragging with the mouse.
	// it is set to the reference to the moving block
	// it should be null, if no block is moved
	private MovableBlock					moveBlock			= null;
	private TimeBlocks					tbs;
	
	
	/**
	 * Initialize PaintArea
	 * 
	 * @author NicolaiO
	 */
	public PaintArea(TimeBlocks tbs) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.tbs = tbs;
		this.initMovableBlocks();
	}
	
	
	/**
	 * Create movableBlocks from timeBlocks
	 * * set location
	 * * set size
	 * * set color
	 * 
	 * @param tbs TimeBlocks from which to initialize
	 * @author NicolaiO
	 */
	public void initMovableBlocks() {
		movableBlocks = new LinkedList<MovableBlock>();
		int start = 20;
		float scaleRatio = (this.getWidth() - start) / (tbs.getTotalLength() != 0 ? tbs.getTotalLength() : 1);
		for (TimeBlock tb : tbs) {
			Point location;
			Color color;
			switch (tb.getType()) {
				case pause:
					location = new Point(start, 10);
					color = Color.red;
					break;
				case exercise:
					location = new Point(start, 40);
					color = Color.green;
					break;
				case group:
					location = new Point(start, 70);
					color = Color.blue;
					break;
				case theory:
					location = new Point(start, 100);
					color = Color.yellow;
					break;
				default:
					location = new Point(start, 130);
					color = Color.gray;
			}
			MovableBlock mb = new MovableBlock(location, new Dimension((int) (tb.getLen() * scaleRatio), 30), color, tb);
			movableBlocks.add(mb);
			// System.out.println("start:" + start + " location:" + location + " type:" + tb.getType());
			start += tb.getLen() * scaleRatio;
		}
	}
	
	
	/**
	 * Will be called by JPanel.
	 * Will do all the drawing.
	 * Is called frequently, e.g. by repaint or if JPanel resizes, etc.
	 */
	public void paint(Graphics g) {
		// initialize
		Graphics2D ga = (Graphics2D) g;
		ga.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		// draw sinus
		ga.setPaint(Color.green);
		ga.setStroke(new BasicStroke(2F));
		int x1 = 0, x2 = 50, y1 = 0, y2 = 100;
		for (int i = 0; i < 200; i++) {
			x1 = i * 2 + 50;
			y1 = (int) (Math.sin((double) i) * 70 + 70);
			ga.drawLine(x2, y2, x1, y1);
			x2 = x1;
			y2 = y1;
		}
		
		// draw block
		for (MovableBlock mb : movableBlocks) {
			ga.setPaint(mb.getColor());
			ga.fill(mb);
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		// Check if mouse clicked on a block and wants to drag
		for (MovableBlock mb : movableBlocks) {
			if (mb.contains(e.getPoint())) {
				Point relML = new Point(mb.x - e.getPoint().x, mb.y - e.getPoint().y);
				mb.setRelMouseLocation(relML);
				moveBlock = mb;
			}
		}
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		moveBlock = null;
		tbs.clear();
		for (MovableBlock mb : movableBlocks) {
			tbs.addTimeBlock(mb.getTimeBlock());
		}
		return;
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// while mouse is pressed and moving, this will move the button
		if (moveBlock != null) {
			// Calculate the movement in x. Negative Value means to
			// the left and positive to the right.
			int mmt_X = (int) Math.floor(e.getPoint().getX() + moveBlock.getRelMouseLocation().getX() - moveBlock.getX());
			logger.debug("Movement in x " + mmt_X);
			logger.debug("e.getPoint().getX " + e.getPoint().getX());
			logger.debug("getRelMouseLocation " + moveBlock.getRelMouseLocation().getX());
			logger.debug("moveBlock.getX() " + moveBlock.getX());
			logger.debug("moveBlock.getWidth() " + moveBlock.getWidth());

			// Any block must exist only one time in the list
			int index = movableBlocks.indexOf(moveBlock);
			
			// calculate new position of moveBlock
			Point movPos = new Point();
			double x = e.getPoint().getX();
			if (x < 0)
				e.getPoint().setLocation(0, e.getPoint().getY());
			else if (x > this.getWidth())
				e.getPoint().setLocation(this.getWidth(), e.getPoint().getY());
			movPos.setLocation(x, e.getPoint().getY());
			moveBlock.setLocation(movPos);
			
			// Calculate width of left block
			if (index - 1 >= 0) {
				movableBlocks.get(index - 1).width = movableBlocks.get(index - 1).width + mmt_X;
			}
			
			// Calculate width of right block and new position
			if (movableBlocks.size() > index + 1 && index >= 0) {
				movableBlocks.get(index + 1).width = movableBlocks.get(index + 1).width - mmt_X;
				// FIXME BLock verschwindet nach links ??!?!?
				Point movPos_P1 = new Point();
				 double x_P1 = movableBlocks.get(index + 1).getLocation().getX() + mmt_X;
				 if(x_P1 <= 0)
					 	x_P1 = 0;
				 else if(x_P1 >= this.getWidth())
					 x_P1 = this.getWidth();
				// x_P1 = x + moveBlock.getWidth();
				movPos_P1.setLocation(x_P1, movableBlocks.get(index + 1).getLocation().getX());
				((Rectangle) movableBlocks.get(index + 1)).setLocation(movPos_P1);
			}
			
			this.repaint();
		}
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
