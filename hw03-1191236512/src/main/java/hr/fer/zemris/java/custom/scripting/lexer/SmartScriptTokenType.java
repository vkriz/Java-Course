package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeracija za potrebe razreda SmartScriptLexer, odnosno SmartScriptToken, koja
 * predstavlja tip tokena.
 * 
 * @author Valentina Križ
 *
 */
public enum SmartScriptTokenType {
	EOF,
	VARIABLE,
	FUNCTION,
	OPERATOR,
	INTEGER,
	DOUBLE,
	TAGNAME,
	TAGCLOSING,
	STRING,
	TEXT
}
