package hr.fer.zemris.java.hw05.db.lexer;


/**
 * Enumeracija za potrebe razreda QueryLexer, odnosno QueryToken, koja
 * predstavlja tip tokena.
 * 
 * @author Valentina Križ
 *
 */
public enum QueryTokenType {
	ATTRIBUTE_NAME,
	COMPARISON_OPERATOR,
	STRING_LITERAL,
	AND,
	EOL
}
