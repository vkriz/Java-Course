package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Razred koji predstavlja naredbu za promjenu
 * boje kojom kornjača crta, a implementira 
 * sučelje command.
 * 
 * @author Valentina Križ
 *
 */
public class ColorCommand implements Command {
	private Color color;
	
	/**
	 * Konstruktor koji prima boju.
	 * 
	 * @param color
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}
	
	/**
	 * Metoda koja izvršava naredbu.
	 * 
	 * @param ctx
	 * @param painter
	 */
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}
}
