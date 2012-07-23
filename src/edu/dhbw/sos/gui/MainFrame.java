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
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

import edu.dhbw.sos.SuperFelix;
import edu.dhbw.sos.course.CourseController;
import edu.dhbw.sos.course.Courses;
import edu.dhbw.sos.course.suggestions.SuggestionManager;
import edu.dhbw.sos.gui.course.CoursePanel;
import edu.dhbw.sos.gui.plan.buttons.PlanPanel;
import edu.dhbw.sos.gui.right.RightPanel;
import edu.dhbw.sos.gui.status.StatusBar;
import edu.dhbw.sos.gui.student.StudentPanel;
import edu.dhbw.sos.helper.Messages;
import edu.dhbw.sos.observers.Observers;
import edu.dhbw.sos.simulation.SimController;


/**
 * This is the main window that contains all the elements, that are needed...
 * It adds the different panels and organize the layout.
 * 
 * @author NicolaiO
 * 
 */
public class MainFrame extends JFrame implements WindowListener {
	private static final long		serialVersionUID	= -1401997967192989464L;
	private static final Logger	logger				= Logger.getLogger(MainFrame.class);
	
	// standard borders
	public static final Border		BLACK_BOARDER		= BorderFactory.createLineBorder(Color.black);
	public static final Border		EMPTY_BORDER		= BorderFactory.createEmptyBorder(5, 5, 5, 5);
	public static final Border		COMPOUND_BORDER	= BorderFactory.createCompoundBorder(BLACK_BOARDER, EMPTY_BORDER);
	
	
	/**
	 * Initialize GUI with given data.
	 * 
	 * @param data
	 * @author NicolaiO
	 */
	public MainFrame(SimController simController, CourseController courseController, Courses courses,
			SuggestionManager sm) {
		logger.debug("Initializing...");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.setTitle(Messages.getString("MainFrame.TITLE"));
		this.setVisible(true);
		this.addWindowListener(this);
		this.setPreferredSize(new Dimension(900, 700));
		
		// load icon
		logger.debug("load icon now");
		URL iconUrl = getClass().getResource("/res/icons/sos_logo.png");
		if (iconUrl != null) {
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconUrl));
		}
		
		CoursePanel coursePanel = new CoursePanel(simController, courseController, courses);
		RightPanel rightPanel = new RightPanel(courseController, courses, sm);
		StatusBar statusBar = new StatusBar();
		StudentPanel studentPanel = new StudentPanel(courses.getCurrentCourse());
		PlanPanel planPanel = new PlanPanel(simController, courses);
		
		// some subscriptions
		Observers.subscribeEditMode(simController);
		Observers.subscribeEditMode(coursePanel);
		Observers.subscribeCurrentCourse(studentPanel);
		Observers.subscribeCurrentCourse(planPanel);
		Observers.subscribeSpeed(planPanel);
		
		// put BottomPanel and StatusBar in a new Panel
		JPanel bsPanel = new JPanel();
		bsPanel.setLayout(new BorderLayout(5, 5));
		bsPanel.add(studentPanel, BorderLayout.WEST);
		bsPanel.add(planPanel, BorderLayout.CENTER);
		bsPanel.add(statusBar, BorderLayout.SOUTH);
		
		// add everything to contentPane
		logger.debug("add content now");
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.add(coursePanel, BorderLayout.CENTER);
		contentPane.add(rightPanel, BorderLayout.EAST);
		contentPane.add(bsPanel, BorderLayout.SOUTH);
		
		// build GUI
		logger.debug("pack() now");
		pack();
		
		logger.debug("Initialized.");
	}
	
	
	@Override
	public void windowOpened(WindowEvent e) {
	}
	
	
	@Override
	public void windowClosing(WindowEvent e) {
	}
	
	
	@Override
	public void windowClosed(WindowEvent e) {
		SuperFelix.close();
	}
	
	
	@Override
	public void windowIconified(WindowEvent e) {
	}
	
	
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	
	
	@Override
	public void windowActivated(WindowEvent e) {
	}
	
	
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
