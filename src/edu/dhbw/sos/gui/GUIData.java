/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 14, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;

import edu.dhbw.sos.course.student.IPlace;


/**
 * This class stores all objects, that are given to the GUI to display the corresponding data.
 * If you want to display something on the GUI, you should store an object here.
 * The GUI will receive this object (the reference!), so that it can be refreshed when calling update()
 * 
 * @author NicolaiO
 * 
 */
public class GUIData {
	@Deprecated
	private Vector<String>						profiles				= new Vector<String>();
	@Deprecated
	private LinkedHashMap<String, String>	statistics			= new LinkedHashMap<String, String>();
	@Deprecated
	private LinkedList<String>					suggestions			= new LinkedList<String>();
	private IPlace									selectedStudent	= null;
	private int										selectedProperty	= 0;
	
	
	public GUIData() {
		// profiles.add("Profile0");
		// profiles.add("Profile1");
		//
		// for (int i = 0; i < 5; i++) {
		// statistics.put("Test" + i, "" + i * 42);
		// }
		//
		// suggestions.add("Sug1");
		// suggestions.add("Sug2");
		// suggestions.add("Sug3");
		// suggestions.add("Sug4");
	}
	
	
	public Vector<String> getProfiles() {
		return profiles;
	}
	
	
	public void setProfiles(Vector<String> profiles) {
		this.profiles = profiles;
	}
	
	
	public LinkedHashMap<String, String> getStatistics() {
		return statistics;
	}
	
	
	public void setStatistics(LinkedHashMap<String, String> statistics) {
		this.statistics = statistics;
	}
	
	
	public LinkedList<String> getSuggestions() {
		return suggestions;
	}
	
	
	public void setSuggestions(LinkedList<String> suggestions) {
		this.suggestions = suggestions;
	}
	
	
	public IPlace getSelectedStudent() {
		return selectedStudent;
	}
	
	
	public void setSelectedStudent(IPlace selectedStudent) {
		this.selectedStudent = selectedStudent;
	}
	
	
	public int getSelectedProperty() {
		return selectedProperty;
	}
	
	
	public void setSelectedProperty(int selectedProperty) {
		this.selectedProperty = selectedProperty;
	}
}
