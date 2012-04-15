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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.dhbw.sos.data.GUIData;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class PlanPanel extends JPanel implements IUpdateable {
	
	/**  */
	private static final long	serialVersionUID	= -1665784555881941508L;
	private final PlanPanelPaintArea paintArea;
	
	public PlanPanel(GUIData data) {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new BorderLayout());
		paintArea = new PlanPanelPaintArea();
		this.add(paintArea, BorderLayout.CENTER);
	}


	@Override
	public void update() {
		
	}
	
	private class PlanPanelPaintArea extends JPanel {
		private static final long	serialVersionUID	= 5194596384018441495L;

		public void paint(Graphics g) {
			Graphics2D ga = (Graphics2D) g;
			ga.clearRect(5, 5, PlanPanel.this.getWidth()-10, PlanPanel.this.getHeight()-10);
			ga.setPaint(Color.green);
			ga.setStroke(new BasicStroke(4F));
			int x1=0,x2=50,y1=0,y2=100;
			for(int i = 0; i < 40; i++) {
				x1=i*8+50;
				y1 = (int) (Math.sin((double)i)*70+70);
				ga.drawLine(x2, y2, x1, y1);
				x2=x1;
				y2=y1;
			}
		}
		
	}
	
}
