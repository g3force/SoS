/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class BottomPanel extends JPanel {
	
	/**  */
	private static final long	serialVersionUID	= -5110684283558655687L;
	private StudentPanel			studentPanel;
	private PlanPanel				planPanel;
	
	
	public BottomPanel() {
		this.setPreferredSize(new Dimension(0, 150));
		this.setLayout(new BorderLayout(5, 5));
		
		studentPanel = new StudentPanel();
		planPanel = new PlanPanel();
		this.add(studentPanel, BorderLayout.WEST);
		this.add(planPanel, BorderLayout.CENTER);
	}
}
