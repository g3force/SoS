/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.status;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.dhbw.sos.gui.IUpdateable;



/**
 * The StatusBar should show some helpful information like tool tips
 * 
 * @author NicolaiO
 * 
 */
public class StatusBar extends JPanel implements IUpdateable {
	
	/**  */
	private static final long	serialVersionUID	= -781636966151974445L;
	
	
	public StatusBar() {
		this.setLayout(new BorderLayout(5,0));
		JLabel lblStatusText = new JLabel("This is the statusbar. It looks really ugly, but that`s Daniels problem :)");
		lblStatusText.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(lblStatusText, BorderLayout.CENTER);

		JLabel lblcopyright = new JLabel("© SimSoft");
		lblcopyright.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(lblcopyright, BorderLayout.EAST);
		lblcopyright.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// new JDialog((Frame) StatusBar.this.getTopLevelAncestor(),"About");
				// new JDialog(SwingUtilities.getWindowAncestor(StatusBar.this.getTopLevelAncestor()),"About");
			}
		});
	}


	@Override
	public void update() {
		
	}
	
}
