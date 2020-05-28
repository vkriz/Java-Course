package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Razred koji predstavlja jedan token u leksičkoj analizi.
 * 
 * @author Valentina Križ
 *
 */
public class QueryToken {
	private QueryTokenType type;
	private Object value;
	
	/**
	 * Konstruktor koji prima tip i vrijednost tokena.
	 * 
	 * @param type tip tokena
	 * @param value vrijednost tokena
	 */
	public QueryToken(QueryTokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Getter za varijablu value.
	 * 
	 * @return value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Getter za varijablu type.
	 * 
	 * @return type
	 */
	public QueryTokenType getType() {
		return type;
	}
}
