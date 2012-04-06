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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.log4j.Logger;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class CoursePanel extends JPanel {
	private static final long		serialVersionUID	= 5542875796802944785L;
	private static final Logger	logger				= Logger.getLogger(CoursePanel.class);
	private Shape[][]					studentArray;
	
	
	public CoursePanel() {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	
	public void setStudentArray() {
		int testx = 6, testy = 5;
		float border = 6;
		float size = 100;
		float offset_x = 0, offset_y = 0;
		Dimension p = this.getSize();
		logger.debug(p);
		if (p.height == 0 || p.width == 0) {
			logger.warn("Dimension has a zero value");
			return;
		}
		float ratioA = (float) testx / (float) testy;
		float ratioB = (float) p.width / (float) p.height;
		if (ratioA < ratioB) {
			// TODO
			size = p.height / testy - (testy + 1) / testy * border;
			offset_x = (p.width - (testx * (size + border) + border)) / 2;
		} else {
			size = p.width / testx - (testx + 1) / testx * border;
			offset_y = (p.height - (testy * (size + border) + border)) / 2;
		}
		
		studentArray = new Shape[testy][testx];
		for (int x = 0; x < studentArray[0].length; x++) {
			for (int y = 0; y < studentArray.length; y++) {
				studentArray[y][x] = new Ellipse2D.Float(offset_x + x * (size) + (x + 1.0f) * border, offset_y + y * (size)
						+ (y + 1) * border, size, size);
			}
		}
	}
	
	
	public void paint(Graphics g) {
		setStudentArray();
		Graphics2D ga = (Graphics2D) g;
		for (int x = 0; x < studentArray[0].length; x++) {
			for (int y = 0; y < studentArray.length; y++) {
				ga.setPaint(Color.green);
				ga.draw(studentArray[y][x]);
				ga.fill(studentArray[y][x]);
			}
		}
	}
	
}
