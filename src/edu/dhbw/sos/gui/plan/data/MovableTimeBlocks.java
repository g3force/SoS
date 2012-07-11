/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Jul 7, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan.data;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;


/**
 * TODO Nicolai Ommer <nicolai.ommer@gmail.com>, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class MovableTimeBlocks extends Component implements MouseMotionListener, MouseListener {
	private static final long	serialVersionUID	= -2721121301356409440L;
	private static final int	BLOCK_HEIGHT		= 30;
	// private final MovableBlock movableBlock;
	private final TimeBlocks	timeBlocks;
	
	private Point					lastMousePoint		= new Point();
	// buffer before two blocks swap place (for the TimeBlock.MIN_LEN limit)
	private int						swapBuffer			= 0;
	

	public MovableTimeBlocks(TimeBlocks timeBlocks) {
		this.timeBlocks = timeBlocks;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	
	private double getScaleRatio() {
		return (double) this.getWidth() / (double) timeBlocks.getTotalLength();
	}
	
	
	private TimeBlock getTimeBlockByMouseLocation(Point mouseLocation) {
		int pos = (int) (mouseLocation.x / getScaleRatio());
		int offset = 0;
		for (TimeBlock timeBlock : timeBlocks) {
			if (timeBlock.getLen() + offset > pos) {
				return timeBlock;
			}
			offset += timeBlock.getLen();
		}
		// we should actually find sth in the loop, so at this line, we are wrong ;)
		assert (false);
		return null;
	}


	@Override
	public void paint(Graphics g) {
		// we can not draw anything, when the total length is zero
		if (timeBlocks.getTotalLength() == 0) {
			return;
		}
		Graphics2D ga = (Graphics2D) g;
		int loc_x = 0;
		for (TimeBlock timeBlock : timeBlocks) {
			int width = (int) (timeBlock.getLen() * getScaleRatio());
			Point location = new Point(loc_x, timeBlock.getType().getYLocation(this.getHeight()));
			Dimension size = new Dimension(width, BLOCK_HEIGHT);
			MovableBlock movableBlock = new MovableBlock(location, size, timeBlock.getType().getColor());
			movableBlock.draw(ga);
			loc_x += width;
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// if (e.getButton() != MouseEvent.BUTTON1) {
		// return;
		// }
		if (lastMousePoint == null) {
			assert (false);
			return;
		}
		
		if (Math.abs(lastMousePoint.x - e.getPoint().x) <= 0) {
			return;
		}
		int movement = e.getPoint().x - lastMousePoint.x;
		TimeBlock timeBlock = getTimeBlockByMouseLocation(lastMousePoint);
		// timeBlock.setLen(timeBlock.getLen() + movement);
		
		// get references to left and right neighbors
		TimeBlock rightTimeBlock = timeBlocks.get(timeBlocks.indexOf(timeBlock) + 1);
		TimeBlock leftTimeBlock = timeBlocks.get(timeBlocks.indexOf(timeBlock) - 1);
		
		// check for borders and non existing neighbors
		// movement neg, when moving left
		if (rightTimeBlock != null) {
			int len = rightTimeBlock.getLen() - movement;
			if (len < TimeBlock.MIN_LEN) {
				swapBuffer += movement;
			} else {
				swapBuffer = 0;
				rightTimeBlock.setLen(len);
			}
		}
		if (leftTimeBlock != null) {
			int len = leftTimeBlock.getLen() + movement;
			if (len < TimeBlock.MIN_LEN) {
				swapBuffer -= movement;
			} else {
				swapBuffer = 0;
				leftTimeBlock.setLen(len);
			}
		}
		
		if (swapBuffer == 0 && (rightTimeBlock == null || leftTimeBlock == null)) {
			// decrease size of timeBlock
			timeBlock.setLen(timeBlock.getLen() - Math.abs(movement));
		}
		
		if (swapBuffer >= TimeBlock.MIN_LEN) {
			if (movement > 0) {
				// right movement
				timeBlocks.moveTimeBlock(timeBlock, rightTimeBlock);
			} else {
				// left movement
				timeBlocks.moveTimeBlock(timeBlock, leftTimeBlock);
			}
		}
		
		lastMousePoint = e.getPoint();
		this.repaint();
	}


	@Override
	public void mouseMoved(MouseEvent e) {
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		lastMousePoint = e.getPoint();
		swapBuffer = 0;
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		lastMousePoint = null;
		swapBuffer = 0;
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
