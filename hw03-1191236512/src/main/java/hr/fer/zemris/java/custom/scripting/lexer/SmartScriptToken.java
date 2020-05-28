package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred koji predstavlja jedan token u leksičkoj analizi.
 * 
 * @author Valentina Križ
 *
 */
public class SmartScriptToken {
	private SmartScriptTokenType type;
	private Object value;
	
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public SmartScriptTokenType getType() {
		return type;
	}
}
