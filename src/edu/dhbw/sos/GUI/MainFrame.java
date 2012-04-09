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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.sos.helper.Messages;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class MainFrame extends JFrame implements WindowStateListener {
	private static final long		serialVersionUID	= -1401997967192989464L;
	private static final Logger	logger				= Logger.getLogger(MainFrame.class);
	private JPanel						contentPane;
	private CoursePanel				coursePanel;
	private RightPanel				rightPanel;
	private StatusBar					statusBar;
	private StudentPanel				studentPanel;
	private PlanPanel					planPanel;
	
	
	public MainFrame() {
		logger.debug("Initializing...");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.setTitle(Messages.getString("MainFrame.1"));
		this.setVisible(true);
		this.addWindowStateListener(this);
		this.setPreferredSize(new Dimension(900, 700));
		
		// load icon
		// logger.debug("load icon now");
		// URL iconUrl = getClass().getResource("/res/icons/useacc_logo.png");
		// if (iconUrl != null) {
		// this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconUrl));
		// }
		
		coursePanel = new CoursePanel();
		rightPanel = new RightPanel();
		statusBar = new StatusBar();
		studentPanel = new StudentPanel();
		planPanel = new PlanPanel();
		
		// put BottomPanel and StatusBar in a new Panel
		JPanel bsPanel = new JPanel();
		bsPanel.setLayout(new BorderLayout(5,5));
		bsPanel.add(studentPanel, BorderLayout.WEST);
		bsPanel.add(planPanel, BorderLayout.CENTER);
		bsPanel.add(statusBar, BorderLayout.SOUTH);
		
		// add everything to contentPane
		logger.debug("add content now");
		contentPane = (JPanel) getContentPane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.add(coursePanel, BorderLayout.CENTER);
		contentPane.add(rightPanel, BorderLayout.EAST);
		contentPane.add(bsPanel, BorderLayout.SOUTH);
		
		// build GUI
		logger.debug("pack() now");
		pack();
		
		logger.debug("Initialized."); //$NON-NLS-1$
	}
	
	
	@Override
	public void windowStateChanged(WindowEvent e) {
		// TODO NicolaiO Auto-generated method stub
		
	}
	
}
