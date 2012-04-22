/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 22, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.dhbw.sos.SuperFelix;
import edu.dhbw.sos.helper.Messages;


/**
 * About Dialog
 * 
 * 
 */
public class AboutDlg extends JDialog {
	/**  */
	private static final long	serialVersionUID	= 7396599427889050631L;
	private ImageIcon				icon;
	
	
	public AboutDlg() {
		this.setTitle(Messages.getString("AboutDlg.TITLE"));
		
		JLabel iconLbl = new JLabel();
		icon = new ImageIcon(getClass().getResource("/res/icons/sos_logo.png"));
		if (icon != null) {
			iconLbl = new JLabel(icon);
		}
		
		
		JLabel titleLbl = new JLabel("<html>" + Messages.getString("AboutDlg.TITLELONG") + "</html>");
		titleLbl.setAlignmentX(CENTER_ALIGNMENT);
		titleLbl.setAlignmentY(CENTER_ALIGNMENT);
		JLabel descriptionLbl = new JLabel("<html>" + Messages.getString("AboutDlg.DESC1") + "<br>"
				+ Messages.getString("AboutDlg.DESC2") + "<br>" + Messages.getString("AboutDlg.DESC3") + "<br>"
				+ Messages.getString("AboutDlg.VERSION") +": "+  SuperFelix.VERSION + "</html>");
		JLabel authorLbl = new JLabel("<html>"
				+ "Benedikt Zirbes, Dirk Klostermann, Sebastian Nickel, Nicolai Ommer, Daniel Andres Lopez" + "<html>");
		
		
		JPanel centerPnl = new JPanel();
		centerPnl.add(iconLbl, BorderLayout.WEST);
		centerPnl.add(descriptionLbl, BorderLayout.EAST);
		
		JPanel southPnl = new JPanel();
		southPnl.add(authorLbl, BorderLayout.WEST);
		
		
		this.add(titleLbl, BorderLayout.NORTH);
		this.add(centerPnl, BorderLayout.CENTER);
		this.add(southPnl, BorderLayout.SOUTH);
		
		this.pack();
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
}
