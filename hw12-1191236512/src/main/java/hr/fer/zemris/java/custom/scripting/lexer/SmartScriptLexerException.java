package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred predstavlja novu vrstu iznimke implementirane za potrebe razreda
 * SmartSciptLexer koja nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class SmartScriptLexerException extends RuntimeException {
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public SmartScriptLexerException() {
		super();
	}
	
	public SmartScriptLexerException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
