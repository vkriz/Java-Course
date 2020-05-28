package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

/**
 * Razred koji predstavlja trenutno stranje kornjače:
 * poziciju, smjer u kojem kornjača gleda, boju kojom crta
 * i trenutnu efektivnu duljinu pomaka. 
 * 
 * @author Valentina Križ
 *
 */
public class TurtleState {
	private Vector2D position;
	private Vector2D direction;
	private Color color;
	private double step;
	
	/**
	 * Konstruktor koji prima potrebne parametre za stanje kornjače.
	 * 
	 * @param position
	 * @param direction
	 * @param color
	 * @param step
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double step) {
		this.position = position;
		this.direction = direction;
		this.color = color;
		this.step = step;
	}
	
	/**
	 * Metoda koja vraća novi objekt s kopijom trenutnog stanja
	 * 
	 * @return kopija stanja
	 */
	public TurtleState copy() {
		return new TurtleState(position, direction, color, step);
	}
	
	/**
	 * Setter za varijablu color.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Setter za varijablu step.
	 * 
	 * @param step
	 */
	public void setStep(double step) {
		this.step = step;
	}
	
	/**
	 * Setter za varijablu position.
	 * 
	 * @param position
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}
	
	/**
	 * Setter za varijablu direction.
	 * 
	 * @param direction
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}
	
	/**
	 * Getter za varijablu position.
	 * 
	 * @return vrijednost varijable
	 */
	public Vector2D getPosition() {
		return position;
	}
	
	/**
	 * Getter za varijablu direction.
	 * 
	 * @return vrijednost varijable
	 */
	public Vector2D getDirection() {
		return direction;
	}
	
	/**
	 * Getter za varijablu color.
	 * 
	 * @return vrijednost varijable
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Getter za varijablu step.
	 * 
	 * @return vrijednost varijable
	 */
	public double getStep() {
		return step;
	}
}
