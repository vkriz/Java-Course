package hr.fer.zemris.java.gui.layouts;

/**
 * Razred predstavlja novu vrstu iznimke implementirane za potrebe razreda
 * CalcLayout koja nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class CalcLayoutException extends RuntimeException {
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public CalcLayoutException() {
		super();
	}
	
	/**
	 * Konstruktor koji prima poruku koju treba ispisati.
	 * 
	 * @param msg
	 */
	public CalcLayoutException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
