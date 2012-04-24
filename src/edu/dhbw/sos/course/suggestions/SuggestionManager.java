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

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import edu.dhbw.sos.helper.XMLParam;


/**
 * This class manages available and displayed suggestions. It also allows removing executed Suggestions and offers the
 * corresponding influence vectors to the simulation.
 * 
 * @author bene
 * 
 */
public class SuggestionManager implements ISuggestionsObserver {
	private static final Logger		logger				= Logger.getLogger(SuggestionManager.class);


	private static final String		SUGGESTION_FILE	= System.getProperty("user.home") + "/.sos/suggestions.xml";

	private LinkedList<Suggestion>	availableSuggestions;
	private LinkedList<Suggestion>	currentSuggestions;
	private LinkedList<String>			params;
	private int								paramCount;
	private XStream						xs;
	
	
	public SuggestionManager(LinkedList<String> params) {
		this.params = params;
		this.paramCount = params.size();
		availableSuggestions = new LinkedList<Suggestion>();
		currentSuggestions = new LinkedList<Suggestion>();
		
		// init xml writer/reader
		xs = new XStream();
		xs.alias("parameters", XMLParam[].class);
		xs.alias("param", XMLParam.class);
		xs.alias("suggestion", Suggestion.class);
		
		if (!loadSuggestionsFromFile()) {
			writeDummySuggestions();
			if (!loadSuggestionsFromFile()) {
				logger.error("Cannot create a suggestions.xml file. Please check the permission of \"" + SUGGESTION_FILE
						+ "\".");
			}
		}
	}
	
	
	public boolean removeSuggestion(Suggestion s) {
		return true;
	}
	
	
	@Override
	public void updateSuggestions() {
		// TODO bene Auto-generated method stub
		
	}
	
	
	private boolean loadSuggestionsFromFile() {
		try {
			ObjectInputStream in = xs.createObjectInputStream(new FileReader(System.getProperty("user.home")
					+ "/.SoS/suggestions.xml"));
			while (true) {
				Suggestion s = (Suggestion) in.readObject();
				// TODO add only fitting suggestions to list therefore check if all current course parameters are included
				// in the suggestion, if so load and remove unused parameters
			}
		} catch (FileNotFoundException err) {
			logger.info("The suggestion file could not be found");
			return false;
		} catch (ClassNotFoundException err) {
			logger.error("There are errors in the suggestions.xml file.");
			return false;
		} catch (IOException err) {
			if (err.getClass().equals(EOFException.class)) {
				return true;
			} else {
				logger.error("There are errors in the suggestions.xml file.");
				return false;
			}
		}
	}
	
	
	private void writeDummySuggestions() {
		float[][] range = { { 10, 30 }, { 5, 10 }, { 15, 20 }, { 25, 30 } };
		float[] influence = { 20, 10, 25, 5 };
		Suggestion sug1 = new Suggestion(range, "hello world", 5, influence, params);
		try {
			ObjectOutputStream out = xs.createObjectOutputStream(new FileWriter(SUGGESTION_FILE));
			
			// out.writeObject(new Person("Joe", "Walnes"));
			// out.writeObject(new Person("Someone", "Else"));
			out.writeObject("hello");
			out.writeInt(12345);
			
			out.close();
		} catch (IOException err) {
			logger.error("IO Error on writing dummy suggestions.xml file.");
		}
	}
}
