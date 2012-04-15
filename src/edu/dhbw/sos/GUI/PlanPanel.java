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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.dhbw.sos.GUI.components.MoveableBlock;
import edu.dhbw.sos.data.GUIData;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class PlanPanel extends JPanel implements IUpdateable {
	
	/**  */
	private static final long			serialVersionUID	= -1665784555881941508L;
	private final PlanPanelPaintArea	paintArea;
	private JLabel							lblSpeed;
	
	
	public PlanPanel(GUIData data) {
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
	
	
	@Override
	public void update() {
		
	}
	
	private class PlanPanelPaintArea extends JPanel implements MouseListener, MouseMotionListener {
		private static final long	serialVersionUID	= 5194596384018441495L;
		private MoveableBlock		r;
		
		
		/**
		 * TODO NicolaiO, add comment!
		 * 
		 * @author NicolaiO
		 */
		public PlanPanelPaintArea() {
			r = new MoveableBlock(new Point(42, 42), new Dimension(100, 30));
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
			ga.setPaint(Color.red);
			ga.fill(r);
		}
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (r.contains(e.getPoint())) {
				Point relML = new Point(r.x - e.getPoint().x, r.y - e.getPoint().y);
				r.setRelMouseLocation(relML);
				r.setMoveable(true);
			}
		}
		
		
		@Override
		public void mouseReleased(MouseEvent e) {
			r.setMoveable(false);
		}
		
		
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		
		
		@Override
		public void mouseExited(MouseEvent e) {
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (r.isMoveable()) {
				r.setLocation(e.getPoint());
				this.repaint();
			}
		}
		
		
		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}
	
}
