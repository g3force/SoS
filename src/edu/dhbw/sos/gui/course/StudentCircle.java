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
import edu.dhbw.sos.gui.IUpdateable;


/**
 * A StudentCircle extends a Circle and contains a reference to the actual student.
 * It furthermore stores and handles the color and the hovering pizza
 * 
 * @author NicolaiO
 * 
 */
public class StudentCircle extends Ellipse2D.Float implements IUpdateable {
	private static final long			serialVersionUID	= 6295891457962405015L;
	private IPlace							student;
	private Color							color;
	private LinkedList<PizzaPiece>	pizza;
	
	
	/**
	 * Create a new StudentCircle object with a reference to the actual student
	 * 
	 * @param _student
	 * @author NicolaiO
	 */
	public StudentCircle(IPlace _student) {
		super();
		student = _student;
		pizza = new LinkedList<PizzaPiece>();
		update();
	}
	
	
	/**
	 * Create a new StudentCircle object with a reference to the actual student.
	 * Additionally initialize Ellipse2D.Float
	 * 
	 * @param _student
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @author NicolaiO
	 */
	public StudentCircle(IPlace _student, float x, float y, float w, float h) {
		super(x, y, w, h);
		student = _student;
		pizza = new LinkedList<PizzaPiece>();
		update();
	}
	
	
	/**
	 * Set the pizza (opening circle with separate areas for each property)
	 * 
	 * @param properties
	 * @param offset
	 * @author NicolaiO
	 */
	public void initPizza(LinkedList<String> properties) {
		float offset = (float) (this.getBounds2D().getWidth() * (PaintArea.SCALE_HOVER - 1)) / 2;
		Rectangle2D rect = this.getBounds2D();
		pizza.clear();
		int count = properties.size();
		for (int i = 0; i < count; i++) {
			PizzaPiece pizzaPiece = new PizzaPiece(properties.get(i), rect.getX() - offset, rect.getY() - offset,
					rect.getWidth() + 2 * offset, rect.getHeight() + 2 * offset, (float) i / (float) count * 360,
					360 / count, Arc2D.PIE);
			pizzaPiece.setColor(getColorFromValue(student.getActualState().getValueAt(i), 100));
			pizza.add(pizzaPiece);
		}
	}
	
	/**
	 * Update the color of the pizza
	 * 
	 * @author NicolaiO
	 */
	private void updatePizza() {
		for (PizzaPiece pizzaPiece : pizza) {
			pizzaPiece.setColor(getColorFromValue(student.getActualState().getValueAt(pizza.indexOf(pizzaPiece)), 100));
		}
	}
	
	
	@Override
	public void update() {
		this.setColor(getColorFromValue(student.getAverageState(), 100)); 
		this.updatePizza();
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
}
