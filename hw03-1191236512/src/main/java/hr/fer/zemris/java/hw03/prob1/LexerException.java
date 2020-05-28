package hr.fer.zemris.java.hw03.prob1;

/**
 * Razred predstavlja novu vrstu iznimke implementirane za potrebe razreda
 * Lexer koja nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class LexerException extends RuntimeException {
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public LexerException() {
		super();
	}
	
	/**
	 * Konstruktor koji prima poruku koju treba ispisati.
	 * 
	 * @param msg tekst poruke
	 */
	public LexerException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
