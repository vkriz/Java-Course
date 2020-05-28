package hr.fer.zemris.java.hw06.shell;

/**
 * Razred predstavlja novu vrstu iznimke implementirane za potrebe razreda
 * Shell koja nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class ShellIOException extends RuntimeException {
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public ShellIOException() {
		super();
	}
	
	/**
	 * Konstruktor koji prima poruku koju treba ispisati.
	 * 
	 * @param msg
	 */
	public ShellIOException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
