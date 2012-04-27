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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import org.apache.log4j.Logger;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.Courses;
import edu.dhbw.sos.course.ICurrentCourseObserver;
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
public class PlanPanel extends JPanel implements ComponentListener, ISpeedObserver, ICurrentCourseObserver {
	private static final Logger	logger				= Logger.getLogger(PlanPanel.class);
	private static final long		serialVersionUID	= -1665784555881941508L;
	// paintArea is the part of the Panel, where some drawings have to be done
	private final PPaintArea		paintArea;
	// label where speed of playback is shown
	private JLabel						lblSpeed;
	// reference to the timeblocks to display
	// private TimeBlocks timeBlocks;
	
	private JFormattedTextField	txtFrom;
	

	/**
	 * Initialize the PlanPanel with GUIData
	 * 
	 * @param data general GUIData object with needed information for GUI
	 * @author NicolaiO
	 */
	public PlanPanel(SimController simController, Courses courses) {
		Course course = courses.getCurrentCourse();
		// get data
		// timeBlocks = new TimeBlocks(course.getLecture().getTimeBlocks());
		
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
		paintArea = new PPaintArea(course);
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
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String start = timeFormat.format(course.getLecture().getStart());
		Date endTime = new Date();
		endTime.setTime(course.getLecture().getStart().getTime() + course.getLecture().getLength() * 60 * 1000);
		String end = timeFormat.format(endTime);

		JLabel lblFrom = new JLabel(Messages.getString("Lecture.FROM"), SwingConstants.LEFT);
		JLabel lblTo = new JLabel(Messages.getString("Lecture.TO"), SwingConstants.LEFT);
		try {
			txtFrom = new JFormattedTextField(new MaskFormatter("##:##"));
		} catch (ParseException err) {
			// TODO andres Auto-generated catch block
			err.printStackTrace();
		}
		// new RegexFormatter("(([0-1][0-9])|(2[0-3])):([0-5][0-9])")
		txtFrom = new JFormattedTextField();

		txtFrom.setText(start);
		txtFrom.setColumns(5);
		JTextField txtTo = new JTextField(end, 5);
		txtTo.setEditable(false);
		txtFrom.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("HH:mm"))));
		txtFrom.addPropertyChangeListener("value", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(txtFrom.getValue());

			}
		});
		
		// txtFrom.addActionListener(new StartTextFieldListener());
		//
		// txtFrom.setInputVerifier(new InputVerifier() {
		// @Override
		// public boolean verify(JComponent input) {
		// if ((input instanceof JFormattedTextField) && ((JFormattedTextField) input).isEditValid()) {
		// ((JFormattedTextField) input).getFormatter();
		// ((JFormattedTextField) input).setFocusLostBehavior(JFormattedTextField.COMMIT);
		// return true;
		// }
		// ((JFormattedTextField) input).setFocusLostBehavior(JFormattedTextField.REVERT);
		// return false;
		// }
		//
		//
		// @Override
		// public boolean shouldYieldFocus(javax.swing.JComponent input) {
		// if (!verify(input)) {
		// input.setForeground(java.awt.Color.RED);
		// return false;
		// } else {
		// input.setForeground(java.awt.Color.BLACK);
		// return true;
		// }
		// }
		// });

		lblFrom.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblTo.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtFrom.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtTo.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtFrom.setMaximumSize(new Dimension(40, 0));
		txtTo.setMaximumSize(new Dimension(40, 0));
		JPanel timePanel = new JPanel();
		timePanel.add(lblFrom);
		timePanel.add(txtFrom);
		timePanel.add(lblTo);
		timePanel.add(txtTo);
		sidePanel.add(timePanel);
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
		lblSpeed.setText(speed + "x");
	}
	
	private class StartTextFieldListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {

		}
	}
	
	
	@Override
	public void updateCurrentCourse(Course course) {
		// TODO andres Auto-generated method stub
		
	}
}
