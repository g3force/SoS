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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class StatusBar extends JPanel implements IUpdateable {
	
	/**  */
	private static final long	serialVersionUID	= -781636966151974445L;
	
	
	public StatusBar(GUIData data) {
		this.setLayout(new BorderLayout(5,0));
		JLabel lblStatusText = new JLabel("This is the statusbar. It looks really ugly, but that`s Daniels problem :)");
		lblStatusText.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(lblStatusText, BorderLayout.CENTER);

		JLabel lblcopyright = new JLabel("© SimSoft");
		lblcopyright.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(lblcopyright, BorderLayout.EAST);
	}


	@Override
	public void update() {
		// TODO NicolaiO Auto-generated method stub
		
	}
	
}
