/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Jul 7, 2012
 * Author(s): Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.plan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.JPanel;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.gui.Diagram;
import edu.dhbw.sos.gui.plan.data.MovableTimeBlocks;
import edu.dhbw.sos.gui.plan.data.TimeMarkerBlock;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.observers.ISimUntilObserver;
import edu.dhbw.sos.observers.IStatisticsObserver;
import edu.dhbw.sos.observers.ITimeBlocksLengthObserver;
import edu.dhbw.sos.observers.Observers;


/**
 * TODO Nicolai Ommer <nicolai.ommer@gmail.com>, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class PPaintArea extends JPanel implements IStatisticsObserver, ISimUntilObserver, ITimeBlocksLengthObserver {
	private static final long	serialVersionUID	= -3407230660397557204L;
	
	private static final int	TMB_START			= 0;

	// list of all movable blocks
	// private MovableBlocks movableBlocks;
	private TimeMarkerBlock		timeMarkerBlock;
	// private Course course;
	private Diagram				timeDiagram;
	
	private boolean				simulateUntil		= false;
	
	private Course					course;
	
	private MovableTimeBlocks	movableTimeBlocks;
	

	public PPaintArea(Course course) {
		// this.setBorder(BorderFactory.createLineBorder(Color.green));
		this.setLayout(new BorderLayout());
		// this.addComponentListener(this);
		this.course = course;
		// this.addMouseListener(this);
		// this.addMouseMotionListener(this);
		timeMarkerBlock = new TimeMarkerBlock();
		Observers.subscribeTime(timeMarkerBlock);
		// this.course = course;
		movableTimeBlocks = new MovableTimeBlocks(course.getLecture().getTimeBlocks());

		TimeMarkerBlockPanel timeMarkerBlockPanel = new TimeMarkerBlockPanel();
		this.add(timeMarkerBlockPanel, BorderLayout.SOUTH);
		this.addMouseMotionListener(timeMarkerBlockPanel);

		movableTimeBlocks.setPreferredSize(new Dimension(movableTimeBlocks.getPreferredSize().width,
				this.getHeight() - 200));
		this.add(movableTimeBlocks, BorderLayout.CENTER);
		
		timeDiagram = new Diagram(new LinkedList<Float>());
		timeDiagram.setLocation(new Point(5, 10));
		timeDiagram.setRescaleY(false);
		timeDiagram.setMaxY(100);
		
		// subscribe to changes of the length of all time blocks (length of lecture), this is used for redrawing the time
		// markers
		Observers.subscribeTimeBlocksLength(this);
	}
	
	
	/**
	 * Will be called by JPanel.
	 * Will do all the drawing.
	 * Is called frequently, e.g. by repaint or if JPanel resizes, etc.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// initialize
		Graphics2D ga = (Graphics2D) g;
		// ga.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		if (simulateUntil) {
			URL iconUrl = getClass().getResource("/res/icons/sos_logo.png");
			if (iconUrl != null) {
				ga.drawImage(Toolkit.getDefaultToolkit().getImage(iconUrl), 40, 15, this);
			}
			return;
		}
		
		// draw Timeline
		ga.setPaint(Color.blue);
		ga.drawLine(TMB_START, 140, this.getWidth() - TMB_START, 140);
		
		// Timemarkers
		int mi = 60;
		int totalLength = course.getLecture().getTimeBlocks().getTotalLength();

		if (totalLength < 90)
			mi = 15;
		else if (totalLength < 180)
			mi = 30;
		else if (totalLength < 360)
			mi = 60;
		else if (totalLength < 720)
			mi = 120;
		else if (totalLength < 1440)
			mi = 240;
		
		double timemarkers = movableTimeBlocks.getScaleRatio() * mi;
		// logger.debug(timemarkers + "");
		if (timemarkers > 0.0) {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			Calendar timeCal = Calendar.getInstance();
			timeCal.setTime(course.getLecture().getStart());
			for (int i = TMB_START; i < this.getWidth(); i += (int) timemarkers) {
				String time = timeFormat.format(timeCal.getTime());
				ga.drawLine(i, 135, i, 145);
				ga.drawString(time, i + 2, 139);
				timeCal.add(Calendar.MINUTE, mi);
			}
		}
		
		// TimeMarkerBlock
		timeMarkerBlock.draw(ga, movableTimeBlocks.getScaleRatio());
		
		// draw diagram
		// updateDiagram();
		ga.setColor(Color.black);
		timeDiagram.draw(ga);
	}


	@Override
	public void updateSimUntil(boolean state) {
		simulateUntil = state;
	}


	@Override
	public void updateStatistics() {
		timeDiagram.setHeight(this.getHeight() - 20);
		timeDiagram.setWidth((int) (timeMarkerBlock.getTime() * movableTimeBlocks.getScaleRatio() + timeMarkerBlock
				.getWidth() / 2));
		LinkedList<Float> newData = new LinkedList<Float>();
		
		for (Entry<Integer, CalcVector> stat : course.getHistStatAvgStudentStates().entrySet()) {
			newData.add(stat.getValue().getValueAt(0));
		}
		timeDiagram.setData(newData);
		
		this.repaint();
	}
	
	
	protected void myRepaint() {
		this.repaint();
	}

	private class TimeMarkerBlockPanel extends JPanel implements MouseMotionListener {
		/**  */
		private static final long	serialVersionUID	= -6662154284384951511L;


		/**
		 * TODO Nicolai Ommer <nicolai.ommer@gmail.com>, add comment!
		 * 
		 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
		 */
		public TimeMarkerBlockPanel() {
			this.setPreferredSize(new Dimension(this.getWidth(), 15));
			// setBackground(Color.red);
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// int time = (int) ((float) e.getX() / (float) this.getWidth())
			// * course.getLecture().getTimeBlocks().getTotalLength();
			// System.out.println(time);
			// System.out.println(course.getLecture().getTimeBlocks().getTotalLength());
			// System.out.println(this.getWidth());
			if (e.getX() >= 0 || e.getX() < this.getWidth()) {
				int time = (int) (e.getX() / movableTimeBlocks.getScaleRatio());
				// System.out.println(time);
				Observers.notifyTimeGUI(time * 60000); // Call the SimController to set the new time.
				myRepaint();
			}
		}
		
		
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		
	}
	

	@Override
	public void lengthChanged() {
		myRepaint();
	}
}
