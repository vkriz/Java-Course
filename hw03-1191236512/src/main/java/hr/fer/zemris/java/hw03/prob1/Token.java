package hr.fer.zemris.java.hw03.prob1;

/**
 * Razred koji predstavlja jedan token u leksičkoj analizi.
 * 
 * @author Valentina Križ
 *
 */
public class Token {
	private TokenType type;
	private Object value;
	
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public TokenType getType() {
		return type;
	}
}
