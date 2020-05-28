package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Razred koji predstavlja naredbu za promjenu položaja
 * kornjače za zadani broj koraka bez iscrtavanja linije.
 * 
 * @author Valentina Križ
 *
 */
public class SkipCommand implements Command {
	private double step;
	
	/**
	 * Konstrukor koji prima broj koraka za koji
	 * treba pomaknuti kornjaču.
	 * 
	 * @param step
	 */
	public SkipCommand(double step) {
		this.step = step;
	}
	
	/**
	 * Metoda koja izvršava naredbu.
	 * 
	 * @param ctx
	 * @param painter
	 */
	public void execute(Context ctx, Painter painter) {
		TurtleState currentState = ctx.getCurrentState();
		Vector2D newPosition = currentState.getPosition().translated(currentState.getDirection().scaled(step * currentState.getStep()));
		
		currentState.setPosition(newPosition);
	}
}
