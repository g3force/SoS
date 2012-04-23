/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 23, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan;

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
public class PlayBtn extends JButton {
	
	private static final long	serialVersionUID	= -3859433203503408752L;
	private Mode					mode					= Mode.PAUSE;
	
	
	/**
	 * Live Button Constructor with icon.
	 * @author andres
	 */
	public PlayBtn() {
		super("PL");
		toggle();
	}
	
	private enum Mode {
		PLAY,
		PAUSE
	}
	
	
	private void setPlay() {
		URL iconUrlLive = getClass().getResource("/res/icons/play.png");
		if (iconUrlLive != null) {
			ImageIcon icon = new ImageIcon(iconUrlLive);
			Image img = icon.getImage();
			Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			ImageIcon newIcon = new ImageIcon(newimg);
			this.setIcon(newIcon);
			this.setText("");
			this.setBorderPainted(false);
		} else {
			this.setText("PL");
		}
		mode = Mode.PAUSE;
	}
	
	
	private void setPause() {
		URL iconUrlLive = getClass().getResource("/res/icons/pause.png");
		if (iconUrlLive != null) {
			ImageIcon icon = new ImageIcon(iconUrlLive);
			Image img = icon.getImage();
			Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			ImageIcon newIcon = new ImageIcon(newimg);
			this.setIcon(newIcon);
			this.setText("");
			this.setBorderPainted(false);
		} else {
			this.setText("PA");
		}
		mode = Mode.PLAY;
	}
	
	
	public void toggle() {
		switch (mode) {
			case PLAY:
				setPause();
				break;
			case PAUSE:
				setPlay();
				break;
			default:
				break;
		}
	}
}
