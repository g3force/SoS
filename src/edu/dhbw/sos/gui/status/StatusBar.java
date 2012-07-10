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

import edu.dhbw.sos.gui.AboutDlg;
import edu.dhbw.sos.helper.Messages;


/**
 * The StatusBar should show some helpful information like tool tips
 * TODO ToolTips not implemented yet
 * 
 * @author NicolaiO
 * 
 */
public class StatusBar extends JPanel {
	private static final long	serialVersionUID	= -781636966151974445L;
	
	
	/**
	 * Initialize the status bar with a left and right area.
	 * The right area will contain the copyright label that will
	 * open the About Dlg on click
	 * 
	 * @author NicolaiO
	 */
	public StatusBar() {
		this.setLayout(new BorderLayout(5, 0));
		JLabel lblStatusText = new JLabel("");
		lblStatusText.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(lblStatusText, BorderLayout.CENTER);
		
		JLabel lblcopyright = new JLabel("© SimSoft");
		lblcopyright.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(lblcopyright, BorderLayout.EAST);
		lblcopyright.setToolTipText(Messages.getString("AboutDlg.TITLE"));
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
				new AboutDlg();
			}
		});
	}
}
