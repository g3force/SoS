/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.student;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.dhbw.sos.gui.GUIData;
import edu.dhbw.sos.gui.IUpdateable;



/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class StudentPanel extends JPanel implements IUpdateable {
	
	/**  */
	private static final long	serialVersionUID	= 722304874911423036L;
	
	
	public StudentPanel(GUIData data) {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setPreferredSize(new Dimension(200, 150));
	}


	@Override
	public void update() {
		// TODO NicolaiO Auto-generated method stub
		
	}
	
}
