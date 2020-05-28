package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Primjerci ovog razreda omogućavaju izvođenje postupka prikazivanja fraktala 
 * te nude stog na koji je moguće stavljati i dohvaćati stanja kornjače.
 * Trenutno odnosno aktivno stanje kornjače je ono koje se nalazi na vrhu stoga
 * 
 * @author Valentina Križ
 *
 */
public class Context {
	private ObjectStack<TurtleState> stack;
	
	/**
	 * Konstruktor koji prima stog sa stanjima kornjače.
	 */
	public Context() {
		stack = new ObjectStack<TurtleState>();
	}
	
	/**
	 * Metoda vraća trenutno stanje kornjače.
	 * 
	 * @return stanje kornjače
	 */
	public TurtleState getCurrentState() {
		return stack.peek();
	}
	
	/**
	 * Metoda na stog stavlja novo trenutno stanje kornjače.
	 * 
	 * @param state novo stanje
	 */
	public void pushState(TurtleState state) {
		stack.push(state);
	}
	
	/**
	 * Metoda sa stoga skida trenutno stanje kornjače.
	 */
	public void popState() {
		stack.pop();
	}
}
