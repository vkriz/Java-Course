package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred koji nasljeđuje razred Element i predstavlja
 * integer konstantu.
 * 
 * @author Valentina Križ
 *
 */
public class ElementConstantInteger extends Element {
	// read-only
	private int value;
	
	/**
	 * Konstruktor koji prima int vrijednost varijable value.
	 * 
	 * @param value vrijednost varijable
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Getter za varijablu value.
	 * 
	 * @return vrijednost varijable
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
