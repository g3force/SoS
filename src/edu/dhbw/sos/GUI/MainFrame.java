/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Mar 30, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.GUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.SuperFelix;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class MainFrame extends JFrame implements WindowStateListener {
	private static final long	serialVersionUID	= -1401997967192989464L;
	private static final Logger	logger	= Logger.getLogger(MainFrame.class);
	private JPanel						contentPane;
	
	public MainFrame() {
		logger.debug("Initializing..."); 
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.setTitle("SoS"); 
		this.setVisible(true);
		this.addWindowStateListener(this);
		this.setSize(500, 300);
		
		// load icon
//		logger.debug("load icon now"); 
//		URL iconUrl = getClass().getResource("/res/icons/useacc_logo.png"); 
//		if (iconUrl != null) {
//			this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconUrl));
//		}

		// get a reference to the content pane
		logger.debug("add content now"); 
		contentPane = (JPanel) getContentPane();
//		contentPane.add(mainPanel);
//		contentPane.add(statusPane, java.awt.BorderLayout.SOUTH);
		
	// build GUI
		logger.debug("pack() now");
		pack();
		
		logger.debug("Initialized.");
	}
	
	
	@Override
	public void windowStateChanged(WindowEvent e) {
		// TODO NicolaiO Auto-generated method stub
		
	}
	
}
