/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 23, 2012
 * Author(s): bene
 * 
 * *********************************************************
 */
package edu.dhbw.sos.course.suggestions;

import java.util.LinkedList;

import com.thoughtworks.xstream.XStream;


/**
 * This class manages available and displayed suggestions. It also allows removing executed Suggestions and offers the
 * corresponding influence vectors to the simulation.
 * 
 * @author bene
 * 
 */
public class SuggestionManager implements ISuggestionsObserver {
	private static final String		suggestionFile	= System.getProperty("user.home") + "/.sos/suggestions.xml";

	private LinkedList<Suggestion>	availableSuggestions;
	private LinkedList<Suggestion>	currentSuggestions;
	private LinkedList<String>			params;
	private int								paramCount;
	
	
	public SuggestionManager(LinkedList<String> params) {
		this.params = params;
		this.paramCount = params.size();
		availableSuggestions = new LinkedList<Suggestion>();
		currentSuggestions = new LinkedList<Suggestion>();
	}
	
	
	public boolean removeSuggestion(Suggestion s) {
		return true;
	}
	
	
	@Override
	public void updateSuggestions() {
		// TODO bene Auto-generated method stub
		
	}
	
	
	private void loadSuggestionsFromFile() {
		XStream xstream = new XStream();

	}
}
