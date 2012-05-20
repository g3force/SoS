/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: May 20, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.right;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import edu.dhbw.sos.observers.ISimulation;


/**
 * This is an abstract button for the RighPanel.
 * It handles some features like the picture and reacts on the simulation
 * 
 * @author NicolaiO
 * 
 */
public abstract class ABtn extends JButton implements ISimulation {
	private static final long	serialVersionUID	= 1L;
	
	
	/**
	 * Create a new Button with given icon or text, if the icon could not be loaded
	 * 
	 * @author NicolaiO
	 */
	public ABtn(String text, String iconUrl) {
		super(text);
		URL editIconUrl = getClass().getResource(iconUrl);
		if (editIconUrl != null) {
			ImageIcon icon = new ImageIcon(editIconUrl);
			Image img = icon.getImage();
			Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			ImageIcon newIcon = new ImageIcon(newimg);
			this.setIcon(newIcon);
			this.setText("");
			this.setBorderPainted(false);
		}
	}
	
	
	@Override
	public void simulationStopped() {
		this.setEnabled(true);
	}

	
	@Override
	public void simulationStarted() {
		this.setEnabled(false);
	}
}
