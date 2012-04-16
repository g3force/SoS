/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.dhbw.sos.GUI.components.MovableBlock;
import edu.dhbw.sos.data.GUIData;
import edu.dhbw.sos.data.TimeBlock;
import edu.dhbw.sos.data.TimeBlocks;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class PlanPanel extends JPanel implements IUpdateable {
	private static final long			serialVersionUID	= -1665784555881941508L;
	private final PlanPanelPaintArea	paintArea;
	private JLabel							lblSpeed;
	private TimeBlocks					timeBlocks;
	private LinkedList<MovableBlock>	movableBlocks = new LinkedList<MovableBlock>();
	private MovableBlock					moveBlock = null;
	
	
	public PlanPanel(GUIData data) {
		timeBlocks = new TimeBlocks(data.getLecture().getTimeBlocks());
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new BorderLayout());
		paintArea = new PlanPanelPaintArea();
		this.add(paintArea, BorderLayout.CENTER);
		
		// create sidePanel
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
		sidePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		this.add(sidePanel, BorderLayout.EAST);
		
		// control panel (play, pause, etc.)
		JPanel controlPanel = new JPanel();
		JButton btnPlay = new JButton("Pl");
		JButton btnPause = new JButton("Pa");
		JButton btnLive = new JButton("L");
		controlPanel.add(btnPlay);
		controlPanel.add(btnPause);
		controlPanel.add(btnLive);
		sidePanel.add(controlPanel);
		
		// spacer
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
		JTextField txtFrom = new JTextField("8:00", 5);
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
	
	
	private void initMovableBlocks() {
		movableBlocks = new LinkedList<MovableBlock>();
		int start = 20;
		float scaleRatio = (paintArea.getWidth() - start) / timeBlocks.getTotalLength();
		System.out.println("sr:"+scaleRatio+"paw:"+paintArea.getWidth());
		for (TimeBlock tb : timeBlocks) {
			Point location;
			Color color;
			switch (tb.getType()) {
				case pause:
					location = new Point(start, 10);
					color = Color.red;
					break;
				case exercise:
					location = new Point(start, 40);
					color = Color.green;
					break;
				case group:
					location = new Point(start, 70);
					color = Color.blue;
					break;
				case theory:
					location = new Point(start, 100);
					color = Color.yellow;
					break;
				default:
					location = new Point(start, 130);
					color = Color.gray;
			}
			MovableBlock mb = new MovableBlock(location, new Dimension((int) (tb.getLen() * scaleRatio), 30), color, tb);
			movableBlocks.add(mb);
			System.out.println("start:" + start + " location:" + location + " type:" + tb.getType());
			start += tb.getLen() * scaleRatio;
		}
	}
	
	
	@Override
	public void update() {
		initMovableBlocks();
		paintArea.repaint(); // FIXME should not be needed
	}
	
	private class PlanPanelPaintArea extends JPanel implements MouseListener, MouseMotionListener {
		private static final long	serialVersionUID	= 5194596384018441495L;
		
		
		// private MovableBlock r;
		
		
		/**
		 * TODO NicolaiO, add comment!
		 * 
		 * @author NicolaiO
		 */
		public PlanPanelPaintArea() {
			// r = new MovableBlock(new Point(42, 42), new Dimension(100, 30));
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}
		
		
		public void paint(Graphics g) {
			// initialize
			Graphics2D ga = (Graphics2D) g;
			ga.clearRect(5, 5, PlanPanel.this.getWidth() - 10, PlanPanel.this.getHeight() - 10);
			
			// draw sinus
			ga.setPaint(Color.green);
			ga.setStroke(new BasicStroke(2F));
			int x1 = 0, x2 = 50, y1 = 0, y2 = 100;
			for (int i = 0; i < 200; i++) {
				x1 = i * 2 + 50;
				y1 = (int) (Math.sin((double) i) * 70 + 70);
				ga.drawLine(x2, y2, x1, y1);
				x2 = x1;
				y2 = y1;
			}
			
			// draw block
			for (MovableBlock mb : movableBlocks) {
//				if (mb == moveBlock) {
//					
//				} else {
					ga.setPaint(mb.getColor());
					ga.fill(mb);
//				}
			}
		}
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		
		
		@Override
		public void mousePressed(MouseEvent e) {
			for (MovableBlock mb : movableBlocks) {
				if (mb.contains(e.getPoint())) {
					Point relML = new Point(mb.x - e.getPoint().x, mb.y - e.getPoint().y);
					mb.setRelMouseLocation(relML);
					mb.setMoveable(true); // obsolete
					moveBlock = mb;
				}
			}
		}
		
		
		@Override
		public void mouseReleased(MouseEvent e) {
//			r.setMoveable(false); // obsolete
			moveBlock = null;
		}
		
		
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		
		
		@Override
		public void mouseExited(MouseEvent e) {
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (moveBlock != null) {
				moveBlock.setLocation(e.getPoint());
				this.repaint();
			}
		}
		
		
		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}
	
}
