/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 6, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.GUI;

import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class GStudent extends JPanel implements MouseMotionListener {
	private Shape					shape;
	
	/**  */
	private static final long	serialVersionUID	= 7050897029375634745L;
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public GStudent(float size) {
		shape = new Ellipse2D.Float(0, 0, size, size);
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO NicolaiO Auto-generated method stub
		
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO NicolaiO Auto-generated method stub
		
	}
	
}
