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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.TimeBlocks;
import edu.dhbw.sos.course.statistics.IStatisticsObserver;
import edu.dhbw.sos.gui.Diagram;
import edu.dhbw.sos.gui.plan.MovableBlock.Areas;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.simulation.ITimeObserver;


/**
 * PaintArea for the PlanPanel.
 * This will draw the timeBlocks, etc.
 * 
 * @author NicolaiO
 * 
 */
public class PPaintArea extends JPanel implements MouseListener, MouseMotionListener, IStatisticsObserver {
	private static final long				serialVersionUID	= 5194596384018441495L;
	private static final Logger			logger				= Logger.getLogger(PPaintArea.class);
	
	// list of all movable blocks
	private MovableBlocks					movableBlocks		= new MovableBlocks();
	// this block is set, when a block is moved by dragging with the mouse.
	// it is set to the reference to the moving block
	// it should be null, if no block is moved
	private MovableBlock						moveBlock			= null;
	private int									index					= -1;
	private int									widthLeft			= -1;
	private int									widthRight			= -1;
	private TimeBlocks						tbs;
	private Course								course;
	
	private TimeMarkerBlock					tmb;
	
	double										scaleRatio			= 1;
	int											start;
	
	private Diagram							attDia;
	
	private Mode								mode					= null;
	private Areas								area					= null;
	
	private LinkedList<ITimeObserver>	timeObservers		= new LinkedList<ITimeObserver>();


	/**
	 * Initialize PaintArea
	 * 
	 * @author NicolaiO
	 */
	public PPaintArea(Course course) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		course.subscribeStatistics(this);
		this.tbs = course.getLecture().getTimeBlocks();
		this.course = course;
		this.initMovableBlocks();
		
		tmb = new TimeMarkerBlock(tbs.getTotalLength());
		course.getSimController().subscribeTime(tmb);
		subscribeTime(course.getSimController());
		attDia = new Diagram(new LinkedList<Float>());
		attDia.setLocation(new Point(5, 10));
	}
	
	
	/**
	 * TODO andres, add comment!
	 * 
	 * @param simController
	 * @author andres
	 */
	private void subscribeTime(ITimeObserver to) {
		timeObservers.add(to);
	}
	
	
	public void notifyTimeObservers() {
		int timeInMilSec = tmb.getTime() * 60000;
		for (ITimeObserver to : timeObservers) {
			to.timeChanged(timeInMilSec);
		}
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
		start = 0;
		movableBlocks = new MovableBlocks();
		scaleRatio = movableBlocks.init(tbs, start, this.getWidth());
	}
	
	
	/**
	 * Will be called by JPanel.
	 * Will do all the drawing.
	 * Is called frequently, e.g. by repaint or if JPanel resizes, etc.
	 */
	@Override
	public void paint(Graphics g) {
		// initialize
		Graphics2D ga = (Graphics2D) g;
		ga.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		// ga.drawString("Pause", 5, 30);
		// ga.drawString("Übung", 5, 60);
		// ga.drawString("Gruppe", 5, 90);
		// ga.drawString("Theorie", 5, 120);
		// draw sinus
		// ga.setPaint(Color.green);
		// ga.setStroke(new BasicStroke(2F));
		// int x1 = 0, x2 = 50, y1 = 0, y2 = 100;
		// for (int i = 0; i < 200; i++) {
		// x1 = i * 2 + 50;
		// y1 = (int) (Math.sin((double) i) * 70 + 70);
		// ga.drawLine(x2, y2, x1, y1);
		// x2 = x1;
		// y2 = y1;
		// }

		// draw block
		for (MovableBlock mb : movableBlocks) {
			ga.setPaint(mb.getColor());
			ga.fill(mb);
		}
		if (moveBlock != null) {
			ga.setPaint(Color.black);
			ga.draw(moveBlock);
		}
		
		
		// draw Timeline
		ga.setPaint(Color.blue);
		ga.drawLine(start, 140, this.getWidth() - start, 140);
		
		// Timemarkers for each 60 min
		double mi = 60.0;
		double timemarkers = scaleRatio * mi;
		// logger.debug(timemarkers + "");
		if (timemarkers > 0.0) {
			int time = 8;
			for (int i = start; i < this.getWidth(); i += (int) timemarkers) {
				ga.drawLine(i, 135, i, 145);
				if (time % 2 == 0)
					ga.drawString(time + ":00", i + 2, 139);
				time++;
			}
		}
		
		// TimeMarkerBlock
		tmb.draw(ga);

		// draw diagram
		// updateDiagram();
		ga.setColor(Color.black);
		attDia.draw(ga);
	}
	
	
	private void updateDiagram() {
		attDia.setHeight(this.getHeight() - 20);
		attDia.setWidth(this.getWidth() - 20);
		LinkedList<Float> newData = new LinkedList<Float>();
		
		for (Entry<Integer, CalcVector> stat : course.getHistStatState().entrySet()) {
			newData.add(stat.getValue().getValueAt(0));
		}
		attDia.setData(newData);
		this.repaint();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (tmb.contains(e.getPoint())) {
			mode = Mode.Time;
			return;
		}

		// Check if mouse clicked on a block and wants to drag
		for (MovableBlock mb : movableBlocks) {
			if (mb.containsArea(e.getPoint()) == MovableBlock.Areas.InArea) {
				Point relML = new Point(mb.x - e.getPoint().x, mb.y - e.getPoint().y);
				mb.setRelMouseLocation(relML);
				moveBlock = mb;
				// Any block must exist only one time in the list
				index = movableBlocks.indexOf(moveBlock);
				logger.error("index:" + index);
				mb.printMbTb(index, "M");
				widthLeft = (index > 0) ? movableBlocks.get(index - 1).width : -1;
				widthRight = (index + 1 < movableBlocks.size()) ? movableBlocks.get(index + 1).width : -1;
				// this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				mode = Mode.Move;
			} else if (mb.containsArea(e.getPoint()) == MovableBlock.Areas.BorderLeft
					|| mb.containsArea(e.getPoint()) == MovableBlock.Areas.BorderRight) {
				Point relML = new Point(mb.x - e.getPoint().x, mb.y - e.getPoint().y);
				mb.setRelMouseLocation(relML);
				moveBlock = mb;
				// Any block must exist only one time in the list
				index = movableBlocks.indexOf(moveBlock);
				logger.error("index:" + index);
				mb.printMbTb(index, "M");
				widthLeft = (index > 0) ? movableBlocks.get(index - 1).width : -1;
				widthRight = (index + 1 < movableBlocks.size()) ? movableBlocks.get(index + 1).width : -1;
				// this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				mode = Mode.Resize;
				area = mb.containsArea(e.getPoint());
			}
		}
	}
	
	
	private enum Mode {
		Move,
		Resize,
		Time
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (moveBlock != null) {
			moveBlock = null;
			index = widthLeft = widthRight = -1;
			tbs.clear();
			for (MovableBlock mb : movableBlocks) {
				tbs.addTimeBlock(mb.getTimeBlock());
				// mb.setRelMouseLocation(new Point(0, 0));
			}
			// this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			area = null;
			
			initMovableBlocks();
			this.repaint();
		}
		if (mode == Mode.Time)
			notifyTimeObservers();
		mode = null;
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
		if (mode != null) {
			switch (mode) {
				case Move:
					dAndDMove(e.getPoint());
					break;
				case Resize:
					dAndDResize(e.getPoint());
					break;
				case Time:
					dAndDTime(e.getPoint());
					break;
			}
		}
		this.repaint();
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		MovableBlock movB = null;
		for (MovableBlock mb : movableBlocks) {
			if (mb.contains(e.getPoint())) {
				movB = mb;
				break;
			}
		}
		if (movB != null) {
			switch (movB.containsArea(e.getPoint())) {
				case InArea:
					this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					break;
				case BorderLeft:
					this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
					break;
				case BorderRight:
					this.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
					break;
				case NotInArea:
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					break;
			}
		} else
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	/**
	 * TODO andres, add comment!
	 * 
	 * @param point
	 * @author andres
	 */
	private void dAndDTime(Point e) {
		int mmt_X = (int) Math.floor(e.getX() + tmb.getRelMouseLocation().getX() - tmb.getX());
		// calculate new position of moveBlock
		double x_mb = tmb.getLocation().getX();
		double paWidth = this.getWidth();
		if (x_mb < 0 && mmt_X < 0) {
			// e.getPoint().setLocation(0, e.getPoint().getY());
			return;
		} else if ((x_mb + tmb.getWidth()) >= paWidth && mmt_X >= 0) {
			// e.getPoint().setLocation(this.getWidth(), e.getPoint().getY());
			return;
		}
		
		double x = e.getX();
		if (x < 0) {
			// e.getPoint().setLocation(0, 0);
			x = 0.0;
		} else if (x > paWidth) {
			// e.getPoint().setLocation(paWidth, 0);
			x = paWidth;
		}
		tmb.timeChanged((int) x + 5);
		tmb.printTmb();
	}


	private void dAndDMove(Point e) {
		// while mouse is pressed and moving, this will move the button
		if (moveBlock != null) {
			// Calculate the movement in x. Negative Value means to
			// the left and positive to the right.
			int mmt_X = (int) Math.floor(e.getX() + moveBlock.getRelMouseLocation().getX() - moveBlock.getX());
			int mmt_Y = (int) Math.floor(e.getY() + moveBlock.getRelMouseLocation().getY());
			
			
			// Vertical Movement
			
			// TODO ausgelagerte FUnktion nutzen...
			// calcMBY(mmt_Y);
			
			// FIXME other blocks are displayed randomly at other positions, after update() they are displayed corrct
			if (mmt_Y >= 1 && mmt_Y < 40) {
				mmt_Y = 10;
				moveBlock.getTimeBlock().setType(BlockType.pause);
				moveBlock.setColor(BlockType.pause.getColor());
			} else if (mmt_Y >= 41 && mmt_Y < 70) {
				mmt_Y = 40;
				moveBlock.getTimeBlock().setType(BlockType.exercise);
				moveBlock.setColor(BlockType.exercise.getColor());
			} else if (mmt_Y >= 71 && mmt_Y < 100) {
				mmt_Y = 70;
				moveBlock.getTimeBlock().setType(BlockType.group);
				moveBlock.setColor(BlockType.group.getColor());
			} else if (mmt_Y >= 101 && mmt_Y < 130) {
				mmt_Y = 100;
				moveBlock.getTimeBlock().setType(BlockType.theory);
				moveBlock.setColor(BlockType.theory.getColor());
			} else {
				mmt_Y = (int) moveBlock.getY();
			}
			// moveBlock.setLocation(moveBlock.getX(), mmt_Y);
			// Horizontal Movement
			
			double paWidth = this.getWidth();
			
			if (!calcMBX(e, mmt_X, mmt_Y)) {
				return;
			}
			
			// Calculate width of left block
			if (index - 1 >= 0) {
				// logger.debug("index - 1 >= 0");
				// logger.debug(movableBlocks.get(index - 1).width);
				movableBlocks.get(index - 1).width += mmt_X;
				movableBlocks.get(index - 1).getTimeBlock().setLen((int) (movableBlocks.get(index - 1).width / scaleRatio));
				movableBlocks.get(index - 1).printMbTb(index - 1, "L");
			}
			
			// Calculate width of right block and new position
			if (index + 1 < movableBlocks.size() && index >= 0) {
				// logger.debug("index + 1 < movableBlocks.size() && index >= 0");
				
				double x_P1 = movableBlocks.get(index + 1).getLocation().getX() + mmt_X;
				if (x_P1 <= 0) {
					x_P1 = 0;
					mmt_X = 0;
				} else if (x_P1 >= paWidth)
					x_P1 = paWidth - movableBlocks.get(index + 1).getWidth();
				movableBlocks.get(index + 1).setLocation(x_P1, movableBlocks.get(index + 1).getY());
				
				movableBlocks.get(index + 1).width -= mmt_X;
				movableBlocks.get(index + 1).getTimeBlock().setLen((int) (movableBlocks.get(index + 1).width / scaleRatio));
				movableBlocks.get(index + 1).printMbTb(index + 1, "R");
				
			}
			
			// Checks wether the width of left and right Blocks are lower or equal then 0
			if (index > 0 && movableBlocks.get(index - 1).width <= 0) {
				int newIndex = 0;
				
				// logger.debug("index > 1 && movableBlocks.get(index - 1).width <= 0");
				
				if (index + 1 < movableBlocks.size()) {
					movableBlocks.get(index + 1).width = widthRight;
					movableBlocks.get(index + 1).getTimeBlock().setLen((int) (widthRight / scaleRatio));
					movableBlocks.get(index + 1).printMbTb(index + 1, "R");
				}
				movableBlocks.get(index - 1).width = widthLeft;
				movableBlocks.get(index - 1).getTimeBlock().setLen((int) (widthLeft / scaleRatio));
				movableBlocks.get(index - 1).printMbTb(index - 1, "L");
				
				movableBlocks.get(index - 1).setLocation(moveBlock.getLocation().getX() + moveBlock.width,
						movableBlocks.get(index - 1).getLocation().getY());
				
				movableBlocks.swap(index, index - 1);
				newIndex = index - 1;
				// }
				
				widthRight = movableBlocks.get(newIndex + 1).width;
				if (newIndex + 2 < movableBlocks.size()) {
					movableBlocks.get(newIndex + 2).setLocation(
							movableBlocks.get(newIndex + 1).getX() + movableBlocks.get(newIndex + 1).width,
							movableBlocks.get(newIndex + 1).getY());
				}
				if (newIndex > 0) {
					widthLeft = movableBlocks.get(newIndex - 1).width;
				} else
					widthLeft = -1;
				index = newIndex;
				
			} else if (index + 1 < movableBlocks.size() && movableBlocks.get(index + 1).width <= 0) {
				int newIndex = 0;
				
				// logger.debug("index + 2 < movableBlocks.size() && movableBlocks.get(index + 1).width <= 0");
				if (index > 0) {
					movableBlocks.get(index - 1).width = widthLeft;
					movableBlocks.get(index - 1).getTimeBlock().setLen((int) (widthLeft / scaleRatio));
					movableBlocks.get(index - 1).printMbTb(index - 1, "L");
				}
				
				// if (index + 1 < movableBlocks.size()) {
				movableBlocks.get(index + 1).width = widthRight;
				movableBlocks.get(index + 1).getTimeBlock().setLen((int) (widthRight / scaleRatio));
				movableBlocks.get(index + 1).printMbTb(index + 1, "R");
				
				
				movableBlocks.get(index + 1).setLocation(moveBlock.getLocation().getX() + moveBlock.width,
						movableBlocks.get(index + 1).getLocation().getY());
				
				
				movableBlocks.swap(index, index + 1);
				newIndex = index + 1;
				// }
				
				widthLeft = movableBlocks.get(newIndex - 1).width;
				
				movableBlocks.get(newIndex - 1).setLocation(
						movableBlocks.get(newIndex).getX() - movableBlocks.get(newIndex - 1).width,
						movableBlocks.get(newIndex).getY());
				
				if (newIndex + 1 < movableBlocks.size()) {
					widthRight = movableBlocks.get(newIndex + 1).width;
				} else
					widthRight = -1;
				index = newIndex;
				
			}
			
			// movableBlocks.descendingIterator();
			logger.trace("Dragged finished: index now:" + index);
		}
	}
	
	
	private void dAndDResize(Point e) {
		if (moveBlock != null) {
			// 4 Possibilites: left area with and without neighbour; right area with and without neighbour
			
			// left area without neighbour
			if (area == Areas.BorderLeft && index == 0) {
				// Calculate the movement in x. Negative Value means to
				// the left and positive to the right.
				int mmt_X = (int) Math.floor(e.getX() + moveBlock.getRelMouseLocation().getX() - moveBlock.getX());
				if (moveBlock.width + mmt_X < 20) {
					moveBlock.width = 20;
					mmt_X = 0;
					moveBlock.setLocation(moveBlock.getX(), moveBlock.getY());
				} else {
					moveBlock.width += mmt_X;
					moveBlock.setLocation(moveBlock.getX() + mmt_X, moveBlock.getY());
				}
				
			}
			// left area with neighbour
			else if (area == Areas.BorderLeft && index > 0) {
				
			}
			// right area without neighbour
			else if (area == Areas.BorderRight && index + 1 == movableBlocks.size()) {
				
			}
			// right area with neighbour
			else if (area == Areas.BorderRight && index + 1 < movableBlocks.size()) {
				
			}
			
			
		}
		
		
		// if (moveBlock != null) {
		// // Calculate the movement in x. Negative Value means to
		// // the left and positive to the right.
		// int mmt_X = (int) Math.floor(e.getX() + moveBlock.getRelMouseLocation().getX() - moveBlock.getX());
		//
		// if (area == Areas.BorderLeft) {
		//
		//
		// if (index > 0) {
		// // if (moveBlock.width > 20 && movableBlocks.get(index - 1).width > 20) {
		// movableBlocks.get(index - 1).width += mmt_X;
		// moveBlock.width -= mmt_X;
		// // }
		// if (movableBlocks.get(index - 1).width <= 20) {
		// moveBlock.width -= 20 - movableBlocks.get(index - 1).width;
		// movableBlocks.get(index - 1).width = 20;
		// } else if (moveBlock.width <= 20) {
		// movableBlocks.get(index - 1).width -= 20 - moveBlock.width;
		// moveBlock.width = 20;
		// }
		// movableBlocks.get(index - 1).getTimeBlock()
		// .setLen((int) (movableBlocks.get(index - 1).width / scaleRatio));
		// movableBlocks.get(index - 1).printMbTb(index - 1, "L");
		//
		//
		// moveBlock.getTimeBlock().setLen((int) (moveBlock.width / scaleRatio));
		// moveBlock.printMbTb(index, "M");
		// // TODO setLocation of moveBlock
		// moveBlock.setLocation(moveBlock.getX() - mmt_X, moveBlock.getY());
		// }
		// }
		// if (area == Areas.BorderRight) {
		//
		//
		// if (index + 1 < movableBlocks.size()) {
		// moveBlock.width += mmt_X;
		// if (moveBlock.width < 20) {
		// mmt_X -= moveBlock.width;
		// moveBlock.width = 20;
		// return;
		// }
		// moveBlock.getTimeBlock().setLen((int) (moveBlock.width / scaleRatio));
		//
		// movableBlocks.get(index + 1).width -= mmt_X;
		// if (movableBlocks.get(index + 1).width < 20)
		// movableBlocks.get(index + 1).width = 20;
		// movableBlocks.get(index + 1).getTimeBlock()
		// .setLen((int) (movableBlocks.get(index + 1).width / scaleRatio));
		// movableBlocks.get(index + 1).printMbTb(index - 1, "R");
		// // TODO setLocation of Block index+1
		// movableBlocks.get(index + 1).setLocation(movableBlocks.get(index + 1).getX() + mmt_X,
		// movableBlocks.get(index + 1).getY());
		// }
		// }
		// }
	}
	
	
	private boolean calcMBX(Point p, int moveX, int moveY) {
		// calculate new position of moveBlock
		double x_mb = moveBlock.getLocation().getX();
		double paWidth = this.getWidth();
		if (x_mb < 0 && moveX < 0) {
			// e.getPoint().setLocation(0, e.getPoint().getY());
			return false;
		} else if ((x_mb + moveBlock.getWidth()) >= paWidth && moveX >= 0) {
			// e.getPoint().setLocation(this.getWidth(), e.getPoint().getY());
			return false;
		}
		
		double x = p.getX();
		if (x < 0) {
			// e.getPoint().setLocation(0, 0);
			x = 0.0;
		} else if (x > paWidth) {
			// e.getPoint().setLocation(paWidth, 0);
			x = paWidth;
		}
		moveBlock.setLocation(x, moveY);
		moveBlock.printMbTb(index, "M");
		return true;
	}
	
	
	private void calcMBY(int moveY) {
		// FIXME other blocks are displayed randomly at other positions, after update() they are displayed corrct
		if (moveY >= 1 && moveY < 40) {
			moveY = 10;
			moveBlock.getTimeBlock().setType(BlockType.pause);
			moveBlock.setColor(BlockType.pause.getColor());
		} else if (moveY >= 41 && moveY < 70) {
			moveY = 40;
			moveBlock.getTimeBlock().setType(BlockType.exercise);
			moveBlock.setColor(BlockType.exercise.getColor());
		} else if (moveY >= 71 && moveY < 100) {
			moveY = 70;
			moveBlock.getTimeBlock().setType(BlockType.group);
			moveBlock.setColor(BlockType.group.getColor());
		} else if (moveY >= 101 && moveY < 130) {
			moveY = 100;
			moveBlock.getTimeBlock().setType(BlockType.theory);
			moveBlock.setColor(BlockType.theory.getColor());
		} else {
			moveY = (int) moveBlock.getY();
		}
		moveBlock.setLocation(moveBlock.getX(), moveY);
	}


	@Override
	public void updateStatistics() {
		updateDiagram();
	}
}
