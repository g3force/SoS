/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;

import edu.dhbw.sos.course.lecture.BlockType;
import edu.dhbw.sos.course.lecture.TimeBlock;
import edu.dhbw.sos.course.lecture.TimeBlocks;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
@Deprecated
public class MovableBlocks {
	private final LinkedList<MovableBlock>	moveableblocks;
	private final double							scaleRatio;
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public MovableBlocks(TimeBlocks tbs, int start, int width) {
		moveableblocks = new LinkedList<MovableBlock>();
		scaleRatio = ((double) width - (double) start)
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
			MovableBlock mb = new MovableBlock(location, new Dimension((int) (tb.getLen() * scaleRatio), 30), color);
			this.add(mb);
			// System.out.println("start:" + start + " location:" + location + " type:" + tb.getType());
			start += (tb.getLen() * scaleRatio);
		}
	}
	
	
	public void add(MovableBlock mb) {
		moveableblocks.add(mb);
	}


	public void add(int index, MovableBlock mb) {
		moveableblocks.add(index, mb);
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
		this.add(mb1, moveableblocks.remove(mb2));
		return mb2;
	}
	
	
	public double getScaleRatio() {
		return scaleRatio;
	}
	
}
