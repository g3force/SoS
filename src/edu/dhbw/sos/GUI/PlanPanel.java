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
	
	
	public PlanPanel(GUIData data) {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}


	@Override
	public void update() {
		// TODO NicolaiO Auto-generated method stub
		
	}
	
}
