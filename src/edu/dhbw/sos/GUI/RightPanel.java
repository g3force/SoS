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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class RightPanel extends JPanel {
	
	/**  */
	private static final long	serialVersionUID	= -6879799823225506209L;
	
	
	public RightPanel() {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setPreferredSize(new Dimension(250, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
		// drop down list
		JPanel courseListPanel = new JPanel();
		this.add(courseListPanel);
		String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
		
		// Create the combo box, select item at index 4.
		// Indices start at 0, so 4 specifies the pig.
		JComboBox courseList = new JComboBox(petStrings);
		courseList.setSelectedIndex(4);
		// petList.addActionListener(this);
		courseListPanel.add(courseList);
		
		// statistics
		JPanel statsPanel = new JPanel(new GridLayout(0, 2));
		
		for (int i = 0; i < 5; i++) {
			statsPanel.add(new JLabel("Test" + i));
			statsPanel.add(new JLabel("" + i * 2));
		}
		this.add(statsPanel);
		// suggestions
		
	}
}
