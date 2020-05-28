package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Razred koji predstavlja naredbu za crtanje
 * linije zadane duljine, a implementira 
 * sučelje command.
 * 
 * @author Valentina Križ
 *
 */
public class DrawCommand implements Command {
	private double step;
	
	/**
	 * Konstruktor koji prima broj koraka.
	 * 
	 * @param step
	 */
	public DrawCommand(double step) {
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
		Vector2D oldPosition = currentState.getPosition();
		Vector2D newPosition = oldPosition.translated(currentState.getDirection().scaled(step * currentState.getStep()));
				
		painter.drawLine(oldPosition.getX(),
							oldPosition.getY(),
							newPosition.getX(),
							newPosition.getY(),
							currentState.getColor(),
							1f);
		currentState.setPosition(newPosition);
	}
}
