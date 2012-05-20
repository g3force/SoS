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
	
	
	public RightPanel(CourseController courseController, Courses courses, SuggestionManager sm) {
		this.setBorder(MainFrame.COMPOUND_BORDER);
		this.setPreferredSize(new Dimension(PREF_SIZE, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.courses = courses;
		this.sugMngr = sm;
		Observers.subscribeCoursesList(this);
		Observers.subscribeCurrentCourse(this);
		Observers.subscribeStatistics(this);
		Observers.subscribeSuggestions(this);
		Observers.subscribeSimulation(this);
		
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
		suggestionPanel.setLayout(new GridLayout(0, 1, 5, 5));
		suggestionPanel.setMaximumSize(new Dimension(PREF_SIZE - MARGIN_LR * 2, 100));
		this.add(Box.createVerticalStrut(10));
		this.add(suggestionPanel);
		updateSuggestions();
		
		// #############################################################################
		// fill the rest of the space
		this.add(Box.createVerticalGlue());
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
		// suggestions
		if (suggestionPanel.getComponentCount() != courses.getCurrentCourse().getStatistics().size() + 1) {
			suggestionPanel.removeAll();
			suggestionPanel.add(new JLabel(Messages.getString("suggestions")));
			for (String sugg : this.sugMngr.getSuggestionNames()) {
				JLabel lblSug = new JLabel(sugg);
				lblSug.addMouseListener(this.sugMngr);
				suggestionPanel.add(lblSug);
			}
		}
	}
	
	
	@Override
	public void updateStatistics() {
		// statistics
		if (statsPanel.getComponentCount() == 0
				|| statsPanel.getComponentCount() == courses.getCurrentCourse().getStatistics().size()) {
			statsPanel.removeAll();
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
					((JLabel) statsPanel.getComponent(i)).setText(entry.getKey());
					((JLabel) statsPanel.getComponent(i + 1)).setText(entry.getValue());
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
