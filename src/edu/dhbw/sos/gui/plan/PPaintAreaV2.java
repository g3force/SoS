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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.gui.Diagram;
import edu.dhbw.sos.gui.plan.data.MovableTimeBlocks;
import edu.dhbw.sos.observers.ISimUntilObserver;
import edu.dhbw.sos.observers.IStatisticsObserver;
import edu.dhbw.sos.observers.Observers;


/**
 * TODO Nicolai Ommer <nicolai.ommer@gmail.com>, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author Nicolai Ommer <nicolai.ommer@gmail.com>
 * 
 */
public class PPaintAreaV2 extends JPanel implements MouseListener, MouseMotionListener, IStatisticsObserver,
		ISimUntilObserver {
	private static final long	serialVersionUID	= -3407230660397557204L;
	
	// list of all movable blocks
	// private MovableBlocks movableBlocks;
	private TimeMarkerBlock		timeMarkerBlock;
	// private Course course;
	private Diagram				timeDiagram;
	
	
	public PPaintAreaV2(Course course) {
		// this.setBorder(BorderFactory.createLineBorder(Color.green));
		this.setLayout(new BorderLayout());
		// this.addComponentListener(this);

		// this.addMouseListener(this);
		// this.addMouseMotionListener(this);
		timeMarkerBlock = new TimeMarkerBlock();
		Observers.subscribeTime(timeMarkerBlock);
		// this.course = course;
		MovableTimeBlocks movableTimeBlocks = new MovableTimeBlocks(course.getLecture().getTimeBlocks());
		this.add(movableTimeBlocks);
		
		timeDiagram = new Diagram(new LinkedList<Float>());
		timeDiagram.setLocation(new Point(5, 10));
		timeDiagram.setRescaleY(false);
		timeDiagram.setMaxY(100);
	}


	@Override
	public void updateSimUntil(boolean state) {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}


	@Override
	public void updateStatistics() {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Nicolai Ommer <nicolai.ommer@gmail.com> Auto-generated method stub
		
	}
	
}
