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
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class StudentPanel extends JPanel implements IUpdateable {
	private static final long	serialVersionUID	= 722304874911423036L;
	private final PaintArea		paintArea;
	
	
	public StudentPanel(GUIData data) {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setPreferredSize(new Dimension(200, 150));
		this.setLayout(new BorderLayout());
		
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
