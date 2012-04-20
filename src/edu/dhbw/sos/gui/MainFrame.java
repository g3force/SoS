/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Mar 30, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

import edu.dhbw.sos.gui.course.CoursePanel;
import edu.dhbw.sos.gui.plan.PlanPanel;
import edu.dhbw.sos.gui.right.RightPanel;
import edu.dhbw.sos.gui.status.StatusBar;
import edu.dhbw.sos.gui.student.StudentPanel;
import edu.dhbw.sos.helper.Messages;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class MainFrame extends JFrame implements IUpdateable, WindowStateListener {
	private static final long			serialVersionUID	= -1401997967192989464L;
	private static final Logger		logger				= Logger.getLogger(MainFrame.class);
	
	public static final Border blackBorder = BorderFactory.createLineBorder(Color.black);
	public static final Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	public static final Border compoundBorder = BorderFactory.createCompoundBorder(blackBorder, emptyBorder);
	
	private JPanel							contentPane;
	private CoursePanel					coursePanel;
	private RightPanel					rightPanel;
	private StatusBar						statusBar;
	private StudentPanel					studentPanel;
	private PlanPanel						planPanel;
	private LinkedList<IUpdateable>	components			= new LinkedList<IUpdateable>();
	
	
	public MainFrame(GUIData data) {
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
		
		coursePanel = new CoursePanel(data);
		components.add(coursePanel);
		rightPanel = new RightPanel(data);
		components.add(rightPanel);
		statusBar = new StatusBar(data);
		components.add(statusBar);
		studentPanel = new StudentPanel(data);
		components.add(studentPanel);
		planPanel = new PlanPanel(data);
		components.add(planPanel);
		
		// put BottomPanel and StatusBar in a new Panel
		JPanel bsPanel = new JPanel();
		bsPanel.setLayout(new BorderLayout(5, 5));
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
		logger.debug("update() and pack() now");
		pack();
		
		logger.debug("Initialized.");
	}
	
	
	@Override
	public void windowStateChanged(WindowEvent e) {
	}
	
	
	@Override
	public void update() {
		logger.info("Update GUI data");
		for (IUpdateable comp : components) {
			comp.update();
		}
	}
	
}
