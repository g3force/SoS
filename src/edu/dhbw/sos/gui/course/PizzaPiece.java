/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.course;

import java.awt.Color;
import java.awt.geom.Arc2D;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class PizzaPiece extends Arc2D.Double {
	private static final long	serialVersionUID	= -8649478385736408674L;
	private Color					color;
	private String					text;
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @author NicolaiO
	 */
	public PizzaPiece(String _text, double arg0, double arg1, double arg2, double arg3, double arg4, double arg5,
			int arg6) {
		super(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
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
