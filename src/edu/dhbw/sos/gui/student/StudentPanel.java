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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.dhbw.sos.gui.GUIData;
import edu.dhbw.sos.gui.IUpdateable;


/**
 * The StudentPanel is the bottom left Panel.
 * It displays statistics of the currently selected student and property.
 * If no student is selected, an average is shown instead.
 * 
 * @author NicolaiO
 * 
 */
public class StudentPanel extends JPanel implements IUpdateable {
	private static final long	serialVersionUID	= 722304874911423036L;
	private final PaintArea		paintArea;
	
	/**
	 * Initialize StudentPanel with GUIData
	 * 
	 * @param data
	 * @author NicolaiO
	 */
	public StudentPanel(GUIData data) {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setPreferredSize(new Dimension(200, 150));
		this.setLayout(new BorderLayout());
		
		// dummy data
		LinkedList<Integer> diaData = new LinkedList<Integer>();
		double last = 50;
		for (int i = 0; i < 50; i++) {
			last = last + ((Math.random() - 0.5) * 30.0);
			if(last<0) last = 0;
			diaData.add((int) last);
			
		}
		paintArea = new PaintArea(diaData);
		this.add(paintArea, BorderLayout.CENTER);
	}
	
	
	@Override
	public void update() {
		paintArea.update();
	}
	
}
