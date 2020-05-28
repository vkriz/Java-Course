package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Razred koji predstavlja naredbu za ažuriranje
 * efektivne duljine pomaka u trenutnom stanju skaliranjem
 * s danim faktorom.
 * 
 * @author Valentina Križ
 *
 */
public class ScaleCommand implements Command {
	private double factor;
	
	/**
	 * Konstrukor koji prima vrijednost faktora skaliranja.
	 * 
	 * @param factor
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}
	
	/**
	 * Metoda koja izvršava naredbu.
	 * 
	 * @param ctx
	 * @param painter
	 */
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setStep(ctx.getCurrentState().getStep() * factor);
	}
}
