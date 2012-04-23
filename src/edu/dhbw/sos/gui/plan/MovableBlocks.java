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

import java.util.Collection;
import java.util.LinkedList;

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
		// TODO andres Auto-generated constructor stub
	}
	
	
	/**
	 * TODO andres, add comment!
	 * 
	 * @param c
	 * @author andres
	 */
	public MovableBlocks(Collection<? extends MovableBlock> c) {
		super(c);
		// TODO andres Auto-generated constructor stub
	}
	
	public boolean add(MovableBlock mb){
		mb.setIndex(this.size()-1);
		return super.add(mb);
	} 
	public void add(int index, MovableBlock mb){
		mb.setIndex(index);
		super.add(index,mb);
	}
	
}
