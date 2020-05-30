package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Razred predstavlja novu vrstu iznimke implementirane za potrebe razreda
 * SmartScriptParser koja nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class SmartScriptParserException extends RuntimeException {
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public SmartScriptParserException() {
		super();
	}
	
	/**
	 * Konstruktor koji prima poruku koju treba ispisati.
	 * 
	 * @param msg tekst poruke
	 */
	public SmartScriptParserException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
