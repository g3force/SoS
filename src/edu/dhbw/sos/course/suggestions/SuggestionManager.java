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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

import edu.dhbw.sos.SuperFelix;
import edu.dhbw.sos.helper.CalcVector;
import edu.dhbw.sos.helper.XMLParam;
import edu.dhbw.sos.observers.Observers;


/**
 * SuggestionManager is used to manage the suggestions shown to the lecturer.
 * At program startup it uses the file .sos/suggestions.xml to load the suggestions that will be available during
 * runtime. Only suggestions that match the parameters used in the current course will be loaded.
 * 
 * If there is no suggestions.xml file SuggestionManager will create one with some sample suggestions.
 * If the file already exists but contains errors or defines attributes that cannot be mapped to the suggestion object,
 * SuggestionManager will copy the suggestions.xml file to a file named suggestions.xml_corrupted and create a new
 * suggestions.xml file like if there had not been one in the first place.
 * 
 * During runtime SuggestionManager manages the displayed suggestions by returning their display text to the gui and
 * handling the effects of a clicked suggestion by returning the influence vectors to the simulation.
 * 
 * @author bene
 * 
 */
public class SuggestionManager implements MouseListener {
	private static final Logger		logger				= Logger.getLogger(SuggestionManager.class);
	
	
	private static final String		SUGGESTION_FILE	= SuperFelix.savepath + "/suggestions.xml";
	/**
	 * Stores all available suggestions that were loaded from the xml file.
	 */
	private LinkedList<Suggestion>	availableSuggestions;
	/**
	 * Stores all currently displayed suggestions.
	 */
	private LinkedList<Suggestion>	currentSuggestions;
	private LinkedList<String>			courseParams;
	private XStream						xs;
	/**
	 * Buffer for the CalcVector objects of clicked Suggestions.
	 */
	private LinkedList<CalcVector>	influences;
	
	
	public SuggestionManager() {
		availableSuggestions = new LinkedList<Suggestion>();
		currentSuggestions = new LinkedList<Suggestion>();
		influences = new LinkedList<CalcVector>();
		
		
		// init xml writer/reader
		xs = new XStream();
		// aliases are not required for functionality but for improving readability of the generated xml file.
		xs.alias("parameters", XMLParam[].class);
		xs.alias("param", XMLParam.class);
		xs.alias("suggestion", Suggestion.class);
	}
	
	
	private void loadXML() {// TODO @bene ladeprozess korrigieren
		// try loading the suggestions from file
		int retCode = loadSuggestionsFromFile();
		if (retCode <= 0) { // file was not (retCode==0) found or had errors (retCode<0)
			if (writeDummySuggestions(retCode < 0)) {
				if (loadSuggestionsFromFile() != 1) {
					logger.error("Cannot create a suggestions.xml file. Please check the permission of \"" + SUGGESTION_FILE
							+ "\" and the surrounding folder.");
				}
			}
		}
	}
	
	
	public void reset(LinkedList<String> params) {
		this.courseParams = params;
		availableSuggestions.clear();
		currentSuggestions.clear();
		influences.clear();
		loadXML();
	}
	
	
	/**
	 * Removes the Suggestion object s from the list of displayed suggestions.
	 * 
	 * @param s Suggestion object to be removed from displayed list.
	 * @return true if the Suggestion could be removed or false if not.
	 * @author bene
	 */
	private boolean removeSuggestion(Suggestion s) {
		if (currentSuggestions.contains(s)) {
			logger.warn(s.getMessage() + " removed");
			currentSuggestions.remove(s);
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Loads the suggestions from the .xml file and add the ones that are needed to the availableSuggestions list.
	 * 
	 * @return 0 if the suggestions.xml file was not found, -1 if there was an error with the suggestions.xml file and 1
	 *         if everything worked as it should.
	 * @author bene
	 */
	private int loadSuggestionsFromFile() {
		try {
			ObjectInputStream in = xs.createObjectInputStream(new FileReader(SUGGESTION_FILE));
			while (true) {
				Suggestion s = (Suggestion) in.readObject();
				if (haveToAddSuggestion(s)) {
					s.removeUnusedParameters(courseParams);
					availableSuggestions.add(s);
				}
			}
		} catch (FileNotFoundException err) {
			logger.info("The suggestion file could not be found. A file with sample suggestions will be created.");
			return 0;
		} catch (ClassNotFoundException err) {
			logger.error("There are errors in your suggestions.xml file. The program created a backup copy and replaced the file with a valid one.");
			return -1;
		} catch (CannotResolveClassException err) {
			logger.error("There are errors in your suggestions.xml file. The program created a backup copy and replaced the file with a valid one.");
			return -1;
		} catch (IOException err) {
			if (err.getClass().equals(EOFException.class)) {
				return 1;
			} else {
				logger.error("There are errors in your suggestions.xml file. The program created a backup copy and replaced the file with a valid one.");
				return -1;
			}
		}
	}
	
	
	// generates suggestions with the current course parameters. If keepCorruptedFile is true, the corrupted file is
	// saved before creating a new one with dummy suggestions
	private boolean writeDummySuggestions(boolean keepCorruptedFile) {
		if (keepCorruptedFile) {
			Path source = FileSystems.getDefault().getPath(SUGGESTION_FILE);
			Path destination = FileSystems.getDefault().getPath(SUGGESTION_FILE + "_corrupted");
			try {
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException err) {
				logger.error("Error on copying corrupted XML File.");
			}
		}
		int dummySuggestions = 6;
		Suggestion[] sugArray = new Suggestion[dummySuggestions];
		Random r = new Random();
		float[][] range = new float[courseParams.size()][2];
		float[] influence = new float[courseParams.size()];
		range[0][0] = 0;
		range[0][1] = 30;
		influence[0] = 30;
		
		range[1][0] = 40;
		range[1][1] = 80;
		influence[1] = -20;
		
		range[2][0] = 0;
		range[2][1] = 40;
		influence[2] = 40;
		
		range[3][0] = 10;
		range[3][1] = 40;
		influence[3] = 50;
		sugArray[0] = new Suggestion(range, "Exercises", r.nextInt(5), influence,
				courseParams);
		
		influence = new float[courseParams.size()];
		range = new float[courseParams.size()][2];
		range[0][0] = 0;
		range[0][1] = 20;
		influence[0] = 60;
		
		range[1][0] = 0;
		range[1][1] = 40;
		influence[1] = 60;
		
		range[2][0] = 0;
		range[2][1] = 30;
		influence[2] = 70;
		
		range[3][0] = 0;
		range[3][1] = 20;
		influence[3] = 10;
		sugArray[1] = new Suggestion(range, "Mention Exam", r.nextInt(5), influence, courseParams);
		
		influence = new float[courseParams.size()];
		range = new float[courseParams.size()][2];
		range[0][0] = 10;
		range[0][1] = 60;
		influence[0] = 40;
		
		range[1][0] = 30;
		range[1][1] = 70;
		influence[1] = 20;
		
		range[2][0] = 10;
		range[2][1] = 50;
		influence[2] = 30;
		
		range[3][0] = 20;
		range[3][1] = 50;
		influence[3] = 15;
		sugArray[2] = new Suggestion(range, "Ask Question", r.nextInt(5), influence,
				courseParams);
		
		influence = new float[courseParams.size()];
		range = new float[courseParams.size()][2];
		range[0][0] = 0;
		range[0][1] = 100;
		influence[0] = 0;
		
		range[1][0] = 0;
		range[1][1] = 30;
		influence[1] = 65;
		
		range[2][0] = 0;
		range[2][1] = 80;
		influence[2] = 0;
		
		range[3][0] = 0;
		range[3][1] = 100;
		influence[3] = 0;
		sugArray[3] = new Suggestion(range, "Ask for Silence", r.nextInt(5), influence,
				courseParams);
		
		influence = new float[courseParams.size()];
		range = new float[courseParams.size()][2];
		range[0][0] = 0;
		range[0][1] = 20;
		influence[0] = 80;
		
		range[1][0] = 0;
		range[1][1] = 10;
		influence[1] = 80;
		
		range[2][0] = 0;
		range[2][1] = 20;
		influence[2] = 50;
		
		range[3][0] = 0;
		range[3][1] = 10;
		influence[3] = 80;
		sugArray[4] = new Suggestion(range, "Break", r.nextInt(5), influence, courseParams);
		
		influence = new float[courseParams.size()];
		range = new float[courseParams.size()][2];
		range[0][0] = 3;
		range[0][1] = 50;
		influence[0] = 15;
		
		range[1][0] = 0;
		range[1][1] = 100;
		influence[1] = 0;
		
		range[2][0] = 0;
		range[2][1] = 30;
		influence[2] = 60;
		
		range[3][0] = 0;
		range[3][1] = 100;
		influence[3] = 0;
		sugArray[5] = new Suggestion(range, "Switch Media", r.nextInt(5),
				influence, courseParams);


		try {
			ObjectOutputStream out = xs.createObjectOutputStream(new FileWriter(SUGGESTION_FILE), "suggestionList");
			
			for (int i = 0; i < dummySuggestions; i++) {
				out.writeObject(sugArray[i]);
			}
			
			out.close();
			return true;
		} catch (IOException err) {
			logger.error("IO Error on writing dummy suggestions.xml file.");
			return false;
		}
	}
	
	
	private boolean haveToAddSuggestion(Suggestion s) {
		String[] suggestionParamNames = s.getParamNames();
		// suggestion contains less parameters than the course and can therefore not be used
		// OR course does not have any parameters (valid suggestions cannot be determined if there are no course
		// parameters).
		if (suggestionParamNames.length < courseParams.size() || courseParams.size() == 0) {
			return false;
		}
		boolean result = true;
		for (int i = 0; i < courseParams.size(); i++) {
			String currentParamName = courseParams.get(i);
			boolean isInSuggestion = false;
			for (String suggestionParamName : suggestionParamNames) {
				if (currentParamName.compareTo(suggestionParamName) == 0) {
					isInSuggestion = true;
					break;
				}
			}
			result = result && isInSuggestion;
		}
		return result;
	}
	
	
	private Suggestion lookUpSuggestion(String text) {
		for (Suggestion s : currentSuggestions) {
			if (s.getMessage().compareTo(text) == 0) {
				return s;
			}
		}
		return null;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		String sugText = ((JLabel) e.getSource()).getText();
		logger.warn(sugText + " clicked");
		Suggestion clicked = this.lookUpSuggestion(sugText);
		if (clicked != null) {
			logger.warn(sugText + " found");
			this.influences.add(clicked.getInfluenceVector());
			this.removeSuggestion(clicked);
			Observers.notifySuggestion();
		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		// empty
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// empty
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// empty
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
		// empty
	}
	
	
	public void updateSuggestions(CalcVector averages) {
		LinkedList<Suggestion> newSuggs = new LinkedList<Suggestion>();
		for (int i = 0; i < availableSuggestions.size(); i++) {
			boolean addSuggestion = true;
			for (int j = 0; j < courseParams.size(); j++) {
				addSuggestion = addSuggestion && availableSuggestions.get(i).paramIsInRange(j, averages.getValueAt(j));
			}
			if (addSuggestion) {
				newSuggs.add(availableSuggestions.get(i));
			}
		}
		Collections.sort(newSuggs);
		if (!currentSuggestions.equals(newSuggs)) {
			currentSuggestions = newSuggs;
			Observers.notifySuggestion();
		}
	}
	
	
	public LinkedList<CalcVector> getAndClearInfluences() {
		LinkedList<CalcVector> ret = new LinkedList<CalcVector>();
		for (int i = 0; i < influences.size(); i++) {
			ret.add(i, influences.get(i));
		}
		influences.clear();
		return ret;
	}
	
	
	public LinkedList<String> getSuggestionNames() {
		LinkedList<String> ret = new LinkedList<String>();
		for (int i = 0; i < this.currentSuggestions.size(); i++) {
			ret.add(i, this.currentSuggestions.get(i).getMessage());
		}
		return ret;
	}
}
