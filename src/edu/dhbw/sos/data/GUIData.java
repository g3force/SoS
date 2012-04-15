/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 14, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.data;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;


/**
 * This class stores all objects, that are given to the GUI to display the corresponding data.
 * If you want to display something on the GUI, you should store an object here.
 * The GUI will receive this object (the reference!), so that it can be refreshed when calling update()
 * 
 * @author NicolaiO
 * 
 */
public class GUIData {
	private Vector<String>				profiles		= new Vector<String>();
	private LinkedHashMap<String, String>	statistics	= new LinkedHashMap<String, String>();
	private LinkedList<String>			suggestions	= new LinkedList<String>();
	
	
	public GUIData() {
		// dummy data
		profiles.add("Profile0");
		profiles.add("Profile1");
		
		for (int i = 0; i < 5; i++) {
			statistics.put("Test" + i, "" + i * 42);
		}
		
		suggestions.add("Sug1");
		suggestions.add("Sug2");
		suggestions.add("Sug3");
		suggestions.add("Sug4");
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
}
