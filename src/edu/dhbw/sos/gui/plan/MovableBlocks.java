/* 
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): andres
 *
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;


/**
 * TODO andres, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author andres
 * 
 */
public class MovableBlocks extends LinkedList<MovableBlock> {
	
	/**  */
	private static final long	serialVersionUID	= -2058135588292238015L;
	
	
	/**
	 * TODO andres, add comment!
	 * 
	 * @author andres
	 */
	public MovableBlocks() {
		super();
	}


	@Override
	public boolean add(MovableBlock mb){
		mb.setIndex(this.size()-1);
		return super.add(mb);
	} 
	
	
	public double init(TimeBlocks tbs, int start, int width) {
		double scaleRatio = ((double) width - (double) start)
				/ (tbs.getTotalLength() != 0 ? (double) tbs.getTotalLength() : 1.0);
		for (TimeBlock tb : tbs) {
			Point location;
			Color color;
			switch (tb.getType()) {
				case pause:
					location = new Point(start, 10);
					color = BlockType.pause.getColor();
					break;
				case exercise:
					location = new Point(start, 40);
					color = BlockType.exercise.getColor();
					break;
				case group:
					location = new Point(start, 70);
					color = BlockType.group.getColor();
					break;
				case theory:
					location = new Point(start, 100);
					color = BlockType.theory.getColor();
					break;
				default:
					location = new Point(start, 130);
					color = Color.gray;
			}
			MovableBlock mb = new MovableBlock(location, new Dimension((int) (tb.getLen() * scaleRatio), 30), color, tb);
			this.add(mb);
			// System.out.println("start:" + start + " location:" + location + " type:" + tb.getType());
			start += (tb.getLen() * scaleRatio);
		}
		return scaleRatio;
	}


	@Override
	public void add(int index, MovableBlock mb){
		mb.setIndex(index);
		super.add(index,mb);
	}
	
	
	/**
	 * 
	 * Swaps the position of indicies mb1 and mb2.
	 * 
	 * @param mb1
	 * @param mb2
	 * @author andres
	 */
	protected int swap(int mb1, int mb2) {
		this.add(mb1, this.remove(mb2));
		return mb2;
	}

}
