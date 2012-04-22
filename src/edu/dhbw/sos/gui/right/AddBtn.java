/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 22, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.right;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class AddBtn extends JButton {
	private static final long	serialVersionUID	= 1L;
	
	
	/**
	 * TODO NicolaiO, add comment!
	 * 
	 * @author NicolaiO
	 */
	public AddBtn() {
		super("add");
		URL editIconUrl = getClass().getResource("/res/icons/add.png");
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
}
