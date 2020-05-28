package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Razred koji predstavlja naredbu za micanje
 * trenutnog stanja kornjače sa stoga.
 * 
 * @author Valentina Križ
 *
 */
public class PopCommand implements Command {
	/**
	 * Metoda koja izvršava naredbu.
	 * 
	 * @param ctx
	 * @param painter
	 */
	public void execute(Context ctx, Painter painter) {
		ctx.popState();
	}
}
