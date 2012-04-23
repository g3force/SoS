/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.lecture.TimeBlocks;
import edu.dhbw.sos.helper.Messages;
import edu.dhbw.sos.simulation.ISpeedObserver;
import edu.dhbw.sos.simulation.SimController;


/**
 * The PlanPanel displays information about the lecture.
 * This is especially the timeblocks and whole time
 * 
 * It also contains some controls
 * 
 * @author NicolaiO
 * 
 */
public class PlanPanel extends JPanel implements ComponentListener, ISpeedObserver {
	private static final long	serialVersionUID	= -1665784555881941508L;
	// paintArea is the part of the Panel, where some drawings have to be done
	private final PaintArea		paintArea;
	// label where speed of playback is shown
	private JLabel					lblSpeed;
	// reference to the timeblocks to display
	private TimeBlocks			timeBlocks;
	
	
	/**
	 * Initialize the PlanPanel with GUIData
	 * 
	 * @param data general GUIData object with needed information for GUI
	 * @author NicolaiO
	 */
	public PlanPanel(SimController simController, Course course) {
		// get data
		timeBlocks = new TimeBlocks(course.getLecture().getTimeBlocks());
		
		// init this Panel
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new BorderLayout());
		this.addComponentListener(this);
		
		// create Labels for pPaintArea
		JPanel lPanel = new JPanel();
		lPanel.setLayout(new BoxLayout(lPanel, BoxLayout.Y_AXIS));
		JLabel lBreak = new JLabel(Messages.getString("BlockType.BREAK"));
		JLabel lExercise = new JLabel(Messages.getString("BlockType.EXERCISE"));
		JLabel lGroup = new JLabel(Messages.getString("BlockType.GROUP"));
		JLabel lTheroy = new JLabel(Messages.getString("BlockType.THEORY"));
		// lPanel.setSize(40, 10);
		lBreak.setPreferredSize(new Dimension(80, 40));
		lBreak.setVerticalTextPosition(JLabel.BOTTOM);
		lExercise.setPreferredSize(new Dimension(80, 40));
		lExercise.setVerticalTextPosition(JLabel.BOTTOM);
		lGroup.setPreferredSize(new Dimension(80, 40));
		lGroup.setVerticalTextPosition(JLabel.BOTTOM);
		lTheroy.setPreferredSize(new Dimension(80, 60));
		lTheroy.setVerticalTextPosition(JLabel.BOTTOM);
		
		lPanel.add(Box.createVerticalGlue());
		
		lPanel.add(lBreak);
		lPanel.add(lExercise);
		lPanel.add(lGroup);
		lPanel.add(lTheroy);
		lPanel.setPreferredSize(new Dimension(60, 120));
		this.add(lPanel, BorderLayout.WEST);
		
		// init paintArea
		paintArea = new PaintArea(course);
		this.add(paintArea, BorderLayout.CENTER);
		// paintArea.initMovableBlocks();
		
		// create sidePanel
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
		sidePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		this.add(sidePanel, BorderLayout.EAST);
		
		// control panel (play, pause, etc.)
		JPanel controlPanel = new JPanel();
		PlayBtn btnPlay = new PlayBtn();
		LiveBtn btnLive = new LiveBtn();
		btnPlay.addActionListener(simController);
		btnLive.addActionListener(simController);
		
		
		controlPanel.add(btnPlay);
		controlPanel.add(btnLive);
		sidePanel.add(controlPanel);
		
		// spacer (BoxLayout will be told to put any left space here
		sidePanel.add(Box.createVerticalGlue());
		
		// speed controls
		lblSpeed = new JLabel("1x");
		ForwardBtn btnPlus = new ForwardBtn();
		RewindBtn btnMinus = new RewindBtn();
		JPanel speedPanel = new JPanel();
		speedPanel.add(btnMinus);
		speedPanel.add(lblSpeed);
		speedPanel.add(btnPlus);
		sidePanel.add(speedPanel);
		btnPlus.addActionListener(simController);
		btnMinus.addActionListener(simController);

		
		// time
		JLabel lblFromTo = new JLabel(Messages.getString("Lecture.FROMTO"), SwingConstants.LEFT);
		JTextField txtFrom = new JTextField("08:00", 5);
		JTextField txtTo = new JTextField("11:00", 5);
		lblFromTo.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtFrom.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtTo.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtFrom.setMaximumSize(new Dimension(40, 0));
		txtTo.setMaximumSize(new Dimension(40, 0));
		sidePanel.add(lblFromTo);
		sidePanel.add(txtFrom);
		sidePanel.add(txtTo);
	}
	
	
	@Override
	public void componentResized(ComponentEvent e) {
		paintArea.initMovableBlocks();
		paintArea.repaint();
	}
	
	
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	
	@Override
	public void componentHidden(ComponentEvent e) {
	}


	@Override
	public void speedChanged(int speed) {
		lblSpeed.setText(speed+"x");
	}
}
