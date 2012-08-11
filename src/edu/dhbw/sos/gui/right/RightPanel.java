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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.dhbw.sos.course.Course;
import edu.dhbw.sos.course.CourseController;
import edu.dhbw.sos.course.Courses;
import edu.dhbw.sos.course.suggestions.SuggestionManager;
import edu.dhbw.sos.gui.MainFrame;
import edu.dhbw.sos.helper.Messages;
import edu.dhbw.sos.observers.ICoursesListObserver;
import edu.dhbw.sos.observers.ICurrentCourseObserver;
import edu.dhbw.sos.observers.IEditModeObserver;
import edu.dhbw.sos.observers.ISimulation;
import edu.dhbw.sos.observers.IStatisticsObserver;
import edu.dhbw.sos.observers.ISuggestionsObserver;
import edu.dhbw.sos.observers.Observers;


/**
 * This Panel contains everything that is on the right.
 * This is the course management and some information
 * 
 * @author NicolaiO
 * 
 */
public class RightPanel extends JPanel implements ICurrentCourseObserver, ICoursesListObserver, IStatisticsObserver,
		ISuggestionsObserver, IEditModeObserver, ISimulation {
	private static final long	serialVersionUID	= -6879799823225506209L;
	// private static final Logger logger = Logger.getLogger(RightPanel.class);
	
	// width of panel
	private static final int	PREF_SIZE			= 200;
	// margin left and right
	private static final int	MARGIN_LR			= 5;
	
	// child elements
	private JPanel					statsPanel;
	private JPanel					suggestionPanel;
	private JComboBox<Course>	courseList;
	
	private Courses				courses;
	private SuggestionManager	sugMngr;
	
	
	/**
	 * Initialize...
	 * 
	 * @param courseController
	 * @param courses
	 * @param sm
	 * @author NicolaiO
	 */
	public RightPanel(CourseController courseController, Courses courses, SuggestionManager sm) {
		this.setBorder(MainFrame.COMPOUND_BORDER);
		this.setPreferredSize(new Dimension(PREF_SIZE, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.courses = courses;
		this.sugMngr = sm;
		
		// #############################################################################
		// drop down list
		JPanel courseListPanel = new JPanel();
		courseListPanel.setBorder(MainFrame.COMPOUND_BORDER);
		courseListPanel.setLayout(new BorderLayout(5, 5));
		courseListPanel.setMaximumSize(new Dimension(PREF_SIZE - MARGIN_LR * 2, 10));
		this.add(courseListPanel);
		
		courseList = new JComboBox<Course>();
		courseList.setEditable(true);
		courseList.addItemListener(courseController);
		updateCoursesList();
		courseListPanel.add(courseList, BorderLayout.NORTH);
		
		// #############################################################################
		// edit button
		EditBtn editBtn = new EditBtn();
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((EditBtn) e.getSource()).isSelected())
					Observers.notifyEditModeEntered();
				else
					Observers.notifyEditModeExited();
			}
		});
		courseListPanel.add(editBtn, BorderLayout.WEST);
		Observers.subscribeSimulation(editBtn);
		
		// #############################################################################
		// add button
		AddBtn addBtn = new AddBtn();
		addBtn.addActionListener(courseController);
		courseListPanel.add(addBtn, BorderLayout.CENTER);
		Observers.subscribeSimulation(addBtn);
		
		// #############################################################################
		// delete button
		DelBtn delBtn = new DelBtn();
		delBtn.addActionListener(courseController);
		courseListPanel.add(delBtn, BorderLayout.EAST);
		Observers.subscribeSimulation(delBtn);
		
		// #############################################################################
		// statistics
		statsPanel = new JPanel();
		statsPanel.setBorder(MainFrame.COMPOUND_BORDER);
		statsPanel.setLayout(new GridLayout(0, 2, 5, 5));
		statsPanel.setMaximumSize(new Dimension(PREF_SIZE - MARGIN_LR * 2, 200));
		this.add(Box.createVerticalStrut(10));
		this.add(statsPanel);
		updateStatistics();
		
		
		// #############################################################################
		// suggestions
		suggestionPanel = new JPanel();
		suggestionPanel.setBorder(MainFrame.COMPOUND_BORDER);
		suggestionPanel.setLayout(new GridLayout(7, 1, 5, 5));
		suggestionPanel.setMaximumSize(new Dimension(PREF_SIZE - MARGIN_LR * 2, 150));
		suggestionPanel.setPreferredSize(new Dimension(PREF_SIZE - MARGIN_LR * 2, 150));
		this.add(Box.createVerticalStrut(10));
		this.add(suggestionPanel);
		updateSuggestions();
		
		// #############################################################################
		// fill the rest of the space
		this.add(Box.createVerticalGlue());
		
		// subscribe to events
		Observers.subscribeCoursesList(this);
		Observers.subscribeCurrentCourse(this);
		Observers.subscribeStatistics(this);
		Observers.subscribeSuggestions(this);
		Observers.subscribeSimulation(this);
	}
	
	
	@Override
	public void updateCoursesList() {
		// course list
		courseList.removeAllItems();
		for (Course course : courses) {
			courseList.addItem(course);
		}
		updateCurrentCourse(courses.getCurrentCourse());
	}
	
	
	@Override
	public void updateCurrentCourse(Course course) {
		if (courses.size() > 0) {
			courseList.setSelectedIndex(courses.indexOf(courses.getCurrentCourse()));
		}
	}
	
	
	@Override
	public void updateSuggestions() {
		if (suggestionPanel.getComponentCount() == 1 && this.sugMngr.getSuggestionNames().size() == 0) {
			// in current Panel, there is only the title
			// there are no suggs to be displayed
			// => nothing to update
			return;
		}
		// boolean found1 = false;
		// for (String sugg : this.sugMngr.getSuggestionNames()) {
		// boolean found = false;
		// for(int i=1; i < suggestionPanel.getComponentCount(); i++) {
		// JLabel lbl = (JLabel) suggestionPanel.getComponent(i);
		// if (sugg.equals(lbl.getText())) {
		// found = true;
		// break;
		// }
		// }
		// if(found) {
		// // sugg should be in Panel and is in Panel.
		// // no need for update yet
		// found1 = true;
		// }
		// }
		suggestionPanel.removeAll();
		suggestionPanel.add(new JLabel(Messages.getString("suggestions"), SwingConstants.CENTER));
		for (String sugg : this.sugMngr.getSuggestionNames()) {
			// System.out.println(sugg);
			JLabel lblSug = new JLabel(sugg);
			lblSug.addMouseListener(this.sugMngr);
			suggestionPanel.add(lblSug);
		}
		suggestionPanel.repaint(); // not the best way, but everything else had no effect
	}
	
	
	@Override
	public void updateStatistics() {
		// statistics
		if (statsPanel.getComponentCount() == 0
				|| statsPanel.getComponentCount() - 2 != courses.getCurrentCourse().getStatistics().size() * 2) {
			statsPanel.removeAll();
			statsPanel.add(new JLabel(Messages.getString("statistics")));
			statsPanel.add(new JLabel());
			for (Map.Entry<String, String> entry : courses.getCurrentCourse().getStatistics().entrySet()) {
				JLabel lblKey = new JLabel(entry.getKey());
				JLabel lblValue = new JLabel(entry.getValue(), JLabel.CENTER);
				statsPanel.add(lblKey);
				statsPanel.add(lblValue);
			}
		} else {
			int i = 0;
			for (Map.Entry<String, String> entry : courses.getCurrentCourse().getStatistics().entrySet()) {
				synchronized (statsPanel.getTreeLock()) {
					((JLabel) statsPanel.getComponent(i + 2)).setText(entry.getKey());
					((JLabel) statsPanel.getComponent(i + 3)).setText(entry.getValue());
					i += 2;
				}
			}
		}
		
		this.validate();
	}
	
	
	@Override
	public void enterEditMode() {
	}
	
	
	@Override
	public void exitEditMode() {
		
	}
	
	
	@Override
	public void simulationStopped() {
		courseList.setEnabled(true);
	}
	
	
	@Override
	public void simulationStarted() {
		courseList.setEnabled(false);
	}
}
