/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 22, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.right;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import edu.dhbw.sos.observers.ISimulation;


/**
 * This is an Edit Button
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class EditBtn extends JToggleButton implements ISimulation {
	private static final long	serialVersionUID	= 1L;
	
	
	/**
	 * New Edit Button
	 * 
	 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
	 */
	public EditBtn() {
		super("edit");
		URL editIconUrl = getClass().getResource("/res/icons/pencil.png");
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
