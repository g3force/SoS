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
	private IPlace	selectedStudent	= null;
	private int		selectedProperty	= 0;
	
	
	public GUIData() {
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
