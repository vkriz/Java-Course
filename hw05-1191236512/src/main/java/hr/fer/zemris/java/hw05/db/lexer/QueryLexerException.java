package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Razred predstavlja novu vrstu iznimke implementirane za potrebe razreda
 * QueryLexer koja nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class QueryLexerException extends RuntimeException {
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public QueryLexerException() {
		super();
	}
	
	/**
	 * Konstruktor koji prima poruku koju treba ispisati.
	 * 
	 * @param msg
	 */
	public QueryLexerException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
