package hr.fer.zemris.java.parser;

/**
 * Razred predstavlja novu vrstu iznimke implementirane za potrebe razreda
 * QueryParser koja nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class ParserException extends RuntimeException {
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public ParserException() {
		super();
	}
	
	/**
	 * Konstruktor koji ispisuje zadanu poruku.
	 * 
	 * @param msg poruka
	 */
	public ParserException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
