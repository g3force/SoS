/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 5, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.right;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.dhbw.sos.gui.GUIData;
import edu.dhbw.sos.gui.IUpdateable;
import edu.dhbw.sos.gui.MainFrame;
import edu.dhbw.sos.helper.Messages;


/**
 * This Panel contains everything that is on the right.
 * This is the course management and some information
 * 
 * @author NicolaiO
 * 
 */
public class RightPanel extends JPanel implements IUpdateable, ActionListener {
	private static final long					serialVersionUID	= -6879799823225506209L;
	// width of panel
	private static final int					PREF_SIZE			= 200;
	// margin left and right
	private static final int					MARGIN_LR			= 5;
	
	// child elements
	private JPanel									statsPanel;
	private JPanel									suggestionPanel;
	private JComboBox								courseList;
	
	// some data - might be replaced by original data
	private Vector<String>						profiles;
	private LinkedHashMap<String, String>	statistics;
	private LinkedList<String>					suggestions;
	
	
	public RightPanel(GUIData data) {
		this.profiles = data.getProfiles();
		this.statistics = data.getStatistics();
		this.suggestions = data.getSuggestions();
		
		this.setBorder(MainFrame.compoundBorder);
		this.setPreferredSize(new Dimension(PREF_SIZE, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// #############################################################################
		// drop down list
		JPanel courseListPanel = new JPanel();
		courseListPanel.setBorder(MainFrame.compoundBorder);
		courseListPanel.setLayout(new BorderLayout(5, 5));
		courseListPanel.setMaximumSize(new Dimension(PREF_SIZE - MARGIN_LR * 2, 10));
		this.add(courseListPanel);
		
		courseList = new JComboBox();
		courseListPanel.add(courseList, BorderLayout.CENTER);
		
		// #############################################################################
		// edit button
		JButton editBtn = new JButton("edit");
		URL editIconUrl = getClass().getResource("/res/icons/pencil.png");
		if (editIconUrl != null) {
			ImageIcon icon = new ImageIcon(editIconUrl);
			Image img = icon.getImage();
			Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			ImageIcon newIcon = new ImageIcon(newimg);
			editBtn = new JButton(newIcon);
			editBtn.setBorderPainted(false);
			editBtn.addActionListener(this);
		}
		courseListPanel.add(editBtn, BorderLayout.EAST);
		
		// #############################################################################
		// statistics
		statsPanel = new JPanel();
		statsPanel.setBorder(MainFrame.compoundBorder);
		statsPanel.setLayout(new GridLayout(0, 2, 5, 5));
		statsPanel.setMaximumSize(new Dimension(PREF_SIZE - MARGIN_LR * 2, 200));
		this.add(Box.createVerticalStrut(10));
		this.add(statsPanel);
		
		
		// #############################################################################
		// suggestions
		suggestionPanel = new JPanel();
		suggestionPanel.setBorder(MainFrame.compoundBorder);
		suggestionPanel.setLayout(new GridLayout(0, 1, 5, 5));
		suggestionPanel.setMaximumSize(new Dimension(PREF_SIZE - MARGIN_LR * 2, 100));
		this.add(Box.createVerticalStrut(10));
		this.add(suggestionPanel);
		
		// #############################################################################
		// fill the rest of the space
		this.add(Box.createVerticalGlue());
	}
	
	
	@Override
	public void update() {
		updateData();
	}
	
	
	private void updateData() {
		// course list
		if (courseList.getItemCount() != profiles.size()) {
			courseList.removeAllItems();
			for (String profile : profiles) {
				courseList.addItem(profile);
			}
		}
		
		if (profiles.size() > 0)
			courseList.setSelectedIndex(0);
		
		// statistics
		if (statsPanel.getComponentCount() != statistics.size()) {
			statsPanel.removeAll();
			for (Map.Entry<String, String> entry : statistics.entrySet()) {
				JLabel lblKey = new JLabel(entry.getKey());
				JLabel lblValue = new JLabel(entry.getValue(), JLabel.CENTER);
				statsPanel.add(lblKey);
				statsPanel.add(lblValue);
			}
		}
		
		// suggestions
		if (suggestionPanel.getComponentCount() != suggestions.size() + 1) {
			suggestionPanel.removeAll();
			suggestionPanel.add(new JLabel(Messages.getString("suggestions")));
			for (String sugg : suggestions) {
				JLabel lblSug = new JLabel(sugg);
				lblSug.addMouseListener(new MouseListener() {
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
						JLabel me = (JLabel) e.getSource();
						if (me != null) {
							suggestions.remove(me.getText());
							suggestionPanel.remove(me);
							suggestionPanel.updateUI();
						}
					}
				});
				suggestionPanel.add(lblSug);
			}
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		profiles.add("Profile" + profiles.size());
		update();
	}
}
