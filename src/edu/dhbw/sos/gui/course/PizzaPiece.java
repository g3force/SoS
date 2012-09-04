/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.course;

import java.awt.Color;
import java.awt.geom.Arc2D;


/**
 * PizzaPiece is part of a circle in the form of a piece of pizza.
 * It is used for splitting the student circle for each property.
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class PizzaPiece extends Arc2D.Double {
	private static final long	serialVersionUID	= -8649478385736408674L;
	private Color					color;
	private String					text;
	
	
	/**
	 * Create a new Pizza Piece with given text and init the
	 * Arc2D.Double with the rest of the args
	 * 
	 * @param _text Text of the property
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param start
	 * @param extent
	 * @param type
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public PizzaPiece(String _text, double x, double y, double w, double h, double start, double extent, int type) {
		super(x, y, w, h, start, extent, type);
		text = _text;
	}
	
	
	public String getFirstLetter() {
		if (text.isEmpty()) {
			return "";
		}
		return text.substring(0, 1).toUpperCase();
	}
	
	
	public Color getColor() {
		return color;
	}
	
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	public String getText() {
		return text;
	}
	
	
	public void setText(String text) {
		this.text = text;
	}
	
}
