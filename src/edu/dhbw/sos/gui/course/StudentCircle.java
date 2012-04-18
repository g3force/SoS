/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 18, 2012
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.sos.gui.course;

import java.awt.Color;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import edu.dhbw.sos.course.student.IPlace;
import edu.dhbw.sos.course.student.Student;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class StudentCircle extends Ellipse2D.Float {
	private static final long			serialVersionUID	= 6295891457962405015L;
	private IPlace							student;
	private Color							color;
	private LinkedList<PizzaPiece>	pizza;
	private boolean						existent;
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param _student
	 * @author NicolaiO
	 */
	public StudentCircle(IPlace _student) {
		super();
		student = _student;
		existent = false;
		if (student instanceof Student) {
			existent = true;
		}
		pizza = new LinkedList<PizzaPiece>();
	}
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param _student
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @author NicolaiO
	 */
	public StudentCircle(IPlace _student, float arg0, float arg1, float arg2, float arg3) {
		super(arg0, arg1, arg2, arg3);
		student = _student;
		existent = false;
		if (student instanceof Student) {
			existent = true;
		}
		pizza = new LinkedList<PizzaPiece>();
	}
	
	
	/**
	 * Set the pizza (opening circle with separate areas for each property)
	 * TODO
	 * 
	 * @param properties
	 * @param offset
	 * @author NicolaiO
	 */
	public void initPizza(LinkedList<String> properties, float offset) {
		if (!existent) // FIXME should not be need, when IPlace is set up correct
			return;
		Rectangle2D rect = this.getBounds2D();
		pizza.clear();
		int count = properties.size();
		for (int i = 0; i < count; i++) {
			PizzaPiece pizzaPiece = new PizzaPiece(properties.get(i), rect.getX() - offset, rect.getY() - offset, rect.getWidth() + 2
					* offset, rect.getHeight() + 2 * offset, (float) i / (float) count * 360, 360 / count, Arc2D.PIE);
			pizzaPiece.setColor(getColorFromValue(((Student) student).getActualState().getValueAt(i), 100));
			pizza.add(pizzaPiece);
		}
	}
	
	// FIXME
	public void updatePizza() {
		if (!existent)
			return;
		int i=0;
		for (PizzaPiece pizzaPiece : pizza) {
			// FIXME casting
			pizzaPiece.setColor(getColorFromValue(((Student) student).getActualState().getValueAt(i), 100));
			i++;
		}
	}
	
	
	/**
	 * Generate a Color between Green and Red.
	 * A small value is greener, a high value is more red.
	 * The value should be between 0 and max. If not, 0/max will be used.
	 * value = 0 => green
	 * value = max => red
	 * 
	 * @param value value between 0 and max
	 * @param max maximal possible value
	 * @return Color between Green and Red
	 * @author NicolaiO
	 */
	public static Color getColorFromValue(int value, int max) {
		int red = (510 / max) * value;
		if (red < 0)
			red = 0;
		int green = 255;
		if (red > 255) {
			green -= red - 255;
			red = 255;
		}
		if (green < 0)
			green = 0;
		return new Color(red, green, 0);
	}
	
	
	public IPlace getStudent() {
		return student;
	}
	
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	
	public Color getColor() {
		return color;
	}
	
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	public LinkedList<PizzaPiece> getPizza() {
		return pizza;
	}
	
	
	public void setPizza(LinkedList<PizzaPiece> pizza) {
		this.pizza = pizza;
	}
	
	
	public boolean isExistent() {
		return existent;
	}
	
	
	public void setExistent(boolean existent) {
		this.existent = existent;
	}
	
	
}
