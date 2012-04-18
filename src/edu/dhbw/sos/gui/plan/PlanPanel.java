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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.dhbw.sos.course.lecture.TimeBlocks;
import edu.dhbw.sos.gui.GUIData;
import edu.dhbw.sos.gui.IUpdateable;
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
public class PlanPanel extends JPanel implements IUpdateable {
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
	public PlanPanel(GUIData data) {
		// get data
		timeBlocks = new TimeBlocks(data.getLecture().getTimeBlocks());
		
		// init this Panel
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new BorderLayout());
		
		// init paintArea
		paintArea = new PaintArea(timeBlocks);
		this.add(paintArea, BorderLayout.CENTER);
		
		// create sidePanel
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
		sidePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		this.add(sidePanel, BorderLayout.EAST);
		
		// control panel (play, pause, etc.)
		JPanel controlPanel = new JPanel();
		JButton btnPlay = new JButton("Pl");
		btnPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SimController.getInstance().toggle();
			}
		});
		JButton btnLive = new JButton("L");
		controlPanel.add(btnPlay);
		controlPanel.add(btnLive);
		sidePanel.add(controlPanel);
		
		// spacer (BoxLayout will be told to put any left space here
		sidePanel.add(Box.createVerticalGlue());
		
		// speed controls
		lblSpeed = new JLabel("1x");
		JButton btnPlus = new JButton("+");
		JButton btnMinus = new JButton("-");
		JPanel speedPanel = new JPanel();
		speedPanel.add(lblSpeed);
		speedPanel.add(btnPlus);
		speedPanel.add(btnMinus);
		sidePanel.add(speedPanel);
		
		// time
		JLabel lblFromTo = new JLabel("Von/Bis:", SwingConstants.LEFT);
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
	public void update() {
		paintArea.initMovableBlocks();
		paintArea.repaint();
	}
}
