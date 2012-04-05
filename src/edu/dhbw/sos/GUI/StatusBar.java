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
public class StatusBar extends JPanel {

	/**  */
	private static final long	serialVersionUID	= -781636966151974445L;
	
	public StatusBar() {
		this.add(new JLabel("This is the statusbar. It looks really ugly, but that`s Daniels problem :)"));
	}
	
}
