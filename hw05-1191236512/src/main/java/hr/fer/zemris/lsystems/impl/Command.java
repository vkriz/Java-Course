package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Sučelje koje predstavlja naredbu. 
 * 
 * @author Valentina Križ
 *
 */
public interface Command {
	/**
	 * Metoda za izvođenje naredbe sa zadanim painterom
	 *  i kontekstom.
	 * 
	 * @param ctx
	 * @param painter
	 */
	void execute(Context ctx, Painter painter);
}
