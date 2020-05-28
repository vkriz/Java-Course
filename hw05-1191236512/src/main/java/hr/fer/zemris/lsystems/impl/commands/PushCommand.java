package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Razred koji predstavlja naredbu za kopiranje
 * stanja s vrha stoga i stavljanje te kopije 
 * na stog.
 * 
 * @author Valentina Križ
 *
 */
public class PushCommand implements Command {
	/**
	 * Metoda koja izvršava naredbu.
	 * 
	 * @param ctx
	 * @param painter
	 */
	public void execute(Context ctx, Painter painter) {
		TurtleState newState = ctx.getCurrentState().copy();
		ctx.pushState(newState);
	}
}
