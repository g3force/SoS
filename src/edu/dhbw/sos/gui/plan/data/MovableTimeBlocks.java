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

import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;
import edu.dhbw.sos.observers.Observers;


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
	public static final int		BLOCK_HEIGHT		= 30;
	// private final MovableBlock movableBlock;
	private final TimeBlocks	timeBlocks;
	
	// buffer before two blocks swap place (for the TimeBlock.MIN_LEN limit)
	private SwapBuffer			swapBuffer			= new SwapBuffer();
	
	// some essential information about the block that is currently moved
	private GrabbedBlock			grabbedBlock;

	/**
	 * Data container for storing the left and right swap buffer value
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * 
	 */
	private class SwapBuffer {
		/** right */
		public int	r	= 0;
		/** left */
		public int	l	= 0;
		
		
		/** both (sum of left and right) */
		public int b() {
			return r + l;
		}
		
		
		@Override
		public String toString() {
			return "SwapBuffer [r=" + r + ", l=" + l + "]";
		}
	}

	/**
	 * Data container for a grabbed block.
	 * Saves the length of left, right and central timeBlock
	 * and the offset from left edge of timeBlock to mouse pos
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 * 
	 */
	private final class GrabbedBlock {
		/** block offset from left block edge to mouse pos (in GUI length, not TimeBlock-length) */
		private final int			xOffset;
		/** length of right and left neighbors */
		private final int			lenRightBlock;
		private final int			lenLeftBlock;
		private final int			lenTimeBlock;
		
		private final TimeBlock	timeBlock;
		
		
		public GrabbedBlock(int xOffset, int lenRightBlock, int lenLeftBlock, int lenTimeBlock, TimeBlock timeBlock) {
			this.xOffset = xOffset;
			this.lenLeftBlock = lenLeftBlock;
			this.lenRightBlock = lenRightBlock;
			this.lenTimeBlock = lenTimeBlock;
			this.timeBlock = timeBlock;
		}
		
		
		@Override
		public String toString() {
			return "GrabbedBlock [xOffset=" + xOffset + ", lenRightBlock=" + lenRightBlock + ", lenLeftBlock="
					+ lenLeftBlock + "]";
		}
	}


	/**
	 * Instantiate this object with the given timeBlocks
	 * 
	 * @param timeBlocks A timeBlocks object with timeBlocks
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public MovableTimeBlocks(TimeBlocks timeBlocks) {
		this.timeBlocks = timeBlocks;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	
	/**
	 * The scaleRatio is a conversion ratio between timeBlock length and GUI width
	 * Example:<br/>
	 * realwidth = len * getScaleRatio() <br/>
	 * len = realwidth / getScaleRatio()
	 * 
	 * @return scaleRatio
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public double getScaleRatio() {
		return (double) this.getWidth() / (double) timeBlocks.getTotalLength();
	}
	
	
	/**
	 * Returns the timeBlock occording to the mouseLocation (in GUI width).
	 * This will convert the mouseLoction to timeBlock-length and
	 * check timeBlocks for the timeBlock at this total length
	 * 
	 * @param mouseLocation MouseLocation as e.g. returned by MouseEvent.getPoint()
	 * @return the TimeBlock
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
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
	
	
	public void addNewTimeBlock(BlockType bt) {
		System.out.println("blubb");
		timeBlocks.add(new TimeBlock(30, bt));
		this.repaint();
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
		if (grabbedBlock == null) {
			return;
		}
		
		// to avoid buggy behavior, do nothing when the cursor is not in our area
		if (e.getX() < 0 || e.getX() >= this.getWidth() || e.getY() < 0 || e.getY() >= this.getHeight()) {
			return;
		}

		// save total length to ensure that we do not change it after the move operation
		int totalLen = timeBlocks.getTotalLength();
		
		// 1. Determine direction
		// 2. check if neighbor block can emit some width (without shrinking lower than limit)
		// 2.1. true: set width of left and right neighbor accordingly
		// 2.2. false: check if block was moved behind neighbor, so that they can switch place
		// 2.2.1. true: switch place, restore sizes
		// 2.2.2. false: fill swapBuffer

		// get references to main timeBlock and left and right neighbors
		TimeBlock timeBlock = getTimeBlockByMouseLocation(e.getPoint());
		TimeBlock rightTimeBlock = timeBlocks.get(timeBlocks.indexOf(timeBlock) + 1);
		TimeBlock leftTimeBlock = timeBlocks.get(timeBlocks.indexOf(timeBlock) - 1);
		
		if (timeBlock != grabbedBlock.timeBlock) {
			return;
		}
		assert timeBlock == grabbedBlock.timeBlock : "Timeblocks not the same!";


		// process vertical movement
		if (e.getY() >= 0 && e.getY() < this.getHeight()) {
			timeBlock.setType(BlockType.getInstance((int) ((e.getY() * BlockType.SIZE) / (this.getHeight()))));
			this.repaint();
		}
		

		// calculate a movement value in timeBlock-len size (not GUI size)
		// movement neg, when moving left
		int movement = ((int) ((e.getX() - grabbedBlock.xOffset) / getScaleRatio())) - timeBlocks.getAddedLen(timeBlock);
		if (movement == 0) {
			// why proceed when we do not move?
			return;
		}
		// else {
		// System.out.println("(" + e.getX() + " - " + grabbedBlock.xOffset + ") / " + getScaleRatio() + " = "
		// + (int) ((e.getX() - grabbedBlock.xOffset) / getScaleRatio()));
		// System.out.println("movement=" + movement + "=" + (int) ((e.getX() - grabbedBlock.xOffset) / getScaleRatio())
		// + " - " + timeBlocks.getAddedLen(timeBlock));
		// }

		// check for borders and non existing neighbors
		if (rightTimeBlock == TimeBlocks.NULL_TIMEBLOCK && leftTimeBlock == TimeBlocks.NULL_TIMEBLOCK) {
			// special case: there is only one block, so no neighbors
			// actually, in this case we can not move anything, so lets just finish here
			return;
		}
		if (leftTimeBlock == TimeBlocks.NULL_TIMEBLOCK) {
			// special case: left Block is null
			// Instead of resizing the most left block, do nothing.
			// this is safer...
			if (e.getX() > grabbedBlock.timeBlock.getLen() * getScaleRatio() + grabbedBlock.xOffset) {
				timeBlocks.moveTimeBlock(timeBlock, rightTimeBlock);
			}
			return;
		}
		if (rightTimeBlock != TimeBlocks.NULL_TIMEBLOCK) {
			int len = rightTimeBlock.getLen() - movement - swapBuffer.r;
			if (len < TimeBlock.MIN_LEN) {
				swapBuffer.r += movement;
			} else {
				swapBuffer.r = 0;
				if (swapBuffer.l == 0) {
					rightTimeBlock.setLen(len);
				}
			}
		}
		if (leftTimeBlock != TimeBlocks.NULL_TIMEBLOCK) {
			int len = leftTimeBlock.getLen() + movement - swapBuffer.l;
			if (len < TimeBlock.MIN_LEN) {
				swapBuffer.l -= movement;
			} else {
				swapBuffer.l = 0;
				if (swapBuffer.r == 0) {
					leftTimeBlock.setLen(len);
				}
			}
		}
		// else {
		// // special case: left block is NULL
		// // any movement of the timeBlock results in an change of the grabbedBlock.xOffset
		// // xOffset will change to current mouse location
		// grabbedBlock = new GrabbedBlock(grabbedBlock.xOffset + movement, grabbedBlock.lenRightBlock,
		// grabbedBlock.lenLeftBlock, grabbedBlock.lenTimeBlock);
		// }
		
		// if there is only one neighbor, only that has changed length. timeBlock needs to compensate the rest
		int offsetLen = totalLen - timeBlocks.getTotalLength(); // negative, when there is too much length
		timeBlock.setLen(timeBlock.getLen() + offsetLen);
		
		// now, it might be that timeBlock is too small... we can fix that!
		if (timeBlock.getLen() < TimeBlock.MIN_LEN) {
			if (rightTimeBlock != TimeBlocks.NULL_TIMEBLOCK) {
				rightTimeBlock.setLen(rightTimeBlock.getLen() - (TimeBlock.MIN_LEN - timeBlock.getLen()));
				timeBlock.setLen(TimeBlock.MIN_LEN);
			} else {
				leftTimeBlock.setLen(leftTimeBlock.getLen() - (TimeBlock.MIN_LEN - timeBlock.getLen()));
				timeBlock.setLen(TimeBlock.MIN_LEN);
			}
		}
		
		// handle swapping of timeBlocks
		if (swapBuffer.b() >= TimeBlock.MIN_LEN) {
			if (movement > 0) {
				// right movement
				timeBlocks.moveTimeBlock(timeBlock, rightTimeBlock);
			} else {
				// left movement
				timeBlocks.moveTimeBlock(timeBlock, leftTimeBlock);
			}
			rightTimeBlock.setLen(grabbedBlock.lenRightBlock);
			leftTimeBlock.setLen(grabbedBlock.lenLeftBlock);
			timeBlock.setLen(grabbedBlock.lenTimeBlock);
			grabbedBlock = new GrabbedBlock(grabbedBlock.xOffset, timeBlocks.get(timeBlocks.indexOf(timeBlock) + 1)
					.getLen(), timeBlocks.get(timeBlocks.indexOf(timeBlock) - 1).getLen(), timeBlock.getLen(),
					grabbedBlock.timeBlock);
			swapBuffer.r = 0;
			swapBuffer.l = 0;
		}
		
		// System.out.println(this.toString());
		
		// there should never be an increase in the total length while moving!
		assert timeBlocks.getTotalLength() == totalLen : "Length changed from " + totalLen + " to "
				+ timeBlocks.getTotalLength();

		this.repaint();
	}


	@Override
	public void mouseMoved(MouseEvent e) {
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		// Change length of a Block
		if (e.getButton() == MouseEvent.BUTTON1) { // left click
			TimeBlock timeBlock = getTimeBlockByMouseLocation(e.getPoint());
			int newLength = timeBlock.getLen() + TimeBlock.STEP;
			timeBlock.setLen(newLength);
			Observers.notifyTimeBlocksLength();
		} else if (e.getButton() == MouseEvent.BUTTON3) { // right click
			TimeBlock timeBlock = getTimeBlockByMouseLocation(e.getPoint());
			int newLength = timeBlock.getLen() - TimeBlock.STEP;
			if (newLength > TimeBlock.MIN_LEN) {
				timeBlock.setLen(newLength);
			} else if (newLength == 0 && timeBlocks.size() > 1) {
				timeBlocks.remove(timeBlock);
			} else {
				timeBlock.setLen(TimeBlock.MIN_LEN);
			}
		}
		Observers.notifyTimeBlocksLength();
		this.repaint();
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		swapBuffer.r = 0;
		swapBuffer.l = 0;
		TimeBlock timeBlock = getTimeBlockByMouseLocation(e.getPoint());
		
		// get references to left and right neighbors
		TimeBlock rightTimeBlock = timeBlocks.get(timeBlocks.indexOf(timeBlock) + 1);
		TimeBlock leftTimeBlock = timeBlocks.get(timeBlocks.indexOf(timeBlock) - 1);
		grabbedBlock = new GrabbedBlock(e.getX() - (int) (timeBlocks.getAddedLen(timeBlock) * getScaleRatio()),
				rightTimeBlock.getLen(), leftTimeBlock.getLen(), timeBlock.getLen(), timeBlock);
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		swapBuffer.r = 0;
		swapBuffer.l = 0;
		grabbedBlock = null;
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	
	@Override
	public String toString() {
		return "MovableTimeBlocks [timeBlocks=" + timeBlocks + ", swapBuffer=" + swapBuffer + ", grabbedBlock="
				+ grabbedBlock + "]";
	}
}
