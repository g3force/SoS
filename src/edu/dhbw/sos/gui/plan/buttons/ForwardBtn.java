/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 23, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan.buttons;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;


/**
 * Live Button
 * 
 * @author andres
 * 
 */
public class ForwardBtn extends JButton {
	private static final long	serialVersionUID	= -7859117948085381042L;
	
	
	/**
	 * Live Button Constructor with icon.
	 * @author andres
	 */
	public ForwardBtn() {
		super(">>");
		URL iconUrlLive = getClass().getResource("/res/icons/forward.png");
		if (iconUrlLive != null) {
			ImageIcon icon = new ImageIcon(iconUrlLive);
			Image img = icon.getImage();
			Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			ImageIcon newIcon = new ImageIcon(newimg);
			this.setIcon(newIcon);
			this.setText("");
			this.setBorderPainted(false);
		}
	}
}
