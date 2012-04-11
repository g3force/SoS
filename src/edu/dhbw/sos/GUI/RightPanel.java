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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


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
	private static final int prefSize = 250;
	private static final int marginLR = 5;
	
	
	public RightPanel() {
		Border blackBorder = BorderFactory.createLineBorder(Color.black);
		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compoundBorder = BorderFactory.createCompoundBorder(blackBorder,emptyBorder);
		this.setBorder(compoundBorder);
		this.setPreferredSize(new Dimension(prefSize, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		
		// drop down list
		JPanel courseListPanel = new JPanel();
		courseListPanel.setBorder(compoundBorder);
		courseListPanel.setLayout(new BorderLayout(5, 5));
		courseListPanel.setMaximumSize(new Dimension(prefSize-marginLR*2, 10));
		this.add(courseListPanel);
		
		String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
		
		JComboBox courseList = new JComboBox(petStrings);
		courseList.setSelectedIndex(4);
		courseListPanel.add(courseList, BorderLayout.CENTER);

		JButton editBtn = new JButton("edit");
		URL editIconUrl = getClass().getResource("/res/icons/edit_pen.png");
		if (editIconUrl != null) {
			editBtn = new JButton(new ImageIcon(editIconUrl));
		}
		courseListPanel.add(editBtn, BorderLayout.EAST);
		
		// statistics
		JPanel statsPanel = new JPanel();
		statsPanel.setBorder(compoundBorder);
		statsPanel.setPreferredSize(new Dimension(100, 0));
		statsPanel.setLayout(new GridLayout(0, 2));
		this.add(Box.createVerticalStrut(10));
		this.add(statsPanel);
		
		for (int i = 0; i < 5; i++) {
			statsPanel.add(new JLabel("Test" + i));
			statsPanel.add(new JLabel("" + i * 2));
		}
		// suggestions
		JPanel suggestionPanel = new JPanel();
		suggestionPanel.setBorder(compoundBorder);
		this.add(Box.createVerticalStrut(10));
		this.add(suggestionPanel);
		

		this.add(Box.createVerticalGlue());
	}
}
