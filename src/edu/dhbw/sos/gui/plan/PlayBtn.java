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

import edu.dhbw.sos.course.ISimulation;
import edu.dhbw.sos.gui.right.IEditModeObserver;


/**
 * Live Button
 * 
 * @author andres
 * 
 */
public class PlayBtn extends JButton implements ISimulation, IEditModeObserver {
	
	private static final long	serialVersionUID	= -3859433203503408752L;
	
	
	// private Mode mode = Mode.PAUSE;
	
	
	/**
	 * Live Button Constructor with icon.
	 * @author andres
	 */
	public PlayBtn() {
		super("PL");
		setPause();
	}
	
	
	// private enum Mode {
	// PLAY,
	// PAUSE
	// }
	
	
	private void setPlay() {
		setIconAndText("/res/icons/pause.png", "PL");
		// mode = Mode.PLAY;
	}
	
	
	private void setPause() {
		setIconAndText("/res/icons/play.png", "PA");
		// mode = Mode.PAUSE;
	}
	
	
	private void setIconAndText(String url, String text) {
		URL iconUrlLive = getClass().getResource(url);
		if (iconUrlLive != null) {
			ImageIcon icon = new ImageIcon(iconUrlLive);
			Image img = icon.getImage();
			Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			ImageIcon newIcon = new ImageIcon(newimg);
			this.setIcon(newIcon);
			this.setText("");
			this.setBorderPainted(false);
		} else {
			this.setText(text);
		}
	}
	
	
	@Deprecated
	public void toggle() {
		// switch (mode) {
		// case PLAY:
		// setPause();
		// break;
		// case PAUSE:
		// setPlay();
		// break;
		// default:
		// break;
		// }
	}
	
	
	@Override
	public void simulationStopped() {
		setPause();
	}
	
	
	@Override
	public void simulationStarted() {
		setPlay();
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
