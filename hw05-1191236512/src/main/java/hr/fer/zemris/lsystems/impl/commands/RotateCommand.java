package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Razred koji predstavlja naredbu za rotiranje
 * kornjače za zadani kut u pozitivnom smjeru.
 * 
 * @author Valentina Križ
 *
 */
public class RotateCommand implements Command {
	private double angle;
	
	/**
	 * Konstruktor koji prima kut za koji treba
	 * rotirati kornjaču. 
	 * 
	 * @param angle
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}
	
	/**
	 * Metoda koja izvršava naredbu.
	 * 
	 * @param ctx
	 * @param painter
	 */
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setDirection(ctx.getCurrentState().getDirection().rotated(angle));
	}
}
