package hr.fer.zemris.java.hw05.db;

/**
 * Razred predstavlja novu vrstu iznimke implementirane za potrebe razreda
 * QueryParser koja nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class QueryParserException extends RuntimeException {
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public QueryParserException() {
		super();
	}
	
	/**
	 * Konstruktor koji ispisuje zadanu poruku.
	 * 
	 * @param msg poruka
	 */
	public QueryParserException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}
