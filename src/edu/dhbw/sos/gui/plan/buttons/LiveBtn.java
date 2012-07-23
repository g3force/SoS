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

import edu.dhbw.sos.observers.IEditModeObserver;


/**
 * Live Button
 * 
 * @author andres
 * 
 */
public class LiveBtn extends JButton implements IEditModeObserver {
	private static final long	serialVersionUID	= -7859117948085381042L;
	
	
	/**
	 * Live Button Constructor with icon.
	 * @author andres
	 */
	public LiveBtn() {
		super("L");
		URL iconUrlLive = getClass().getResource("/res/icons/record.png");
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
	
	
	@Override
	public void enterEditMode() {
		this.setEnabled(false);
	}
	
	
	@Override
	public void exitEditMode() {
		this.setEnabled(true);
	}
}
