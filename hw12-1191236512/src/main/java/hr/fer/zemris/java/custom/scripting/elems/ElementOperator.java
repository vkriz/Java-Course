package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred koji nasljeđuje razred Element i predstavlja
 * operator.
 * 
 * @author Valentina Križ
 *
 */
public class ElementOperator extends Element {
	// read-only
	private String symbol;
	
	/**
	 * Konstruktor koji prima simbol koji predstavlja
	 * operator.
	 * 
	 * @param value simbol operatora
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Getter za varijablu symbol.
	 * 
	 * @return vrijednost varijable
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
}
