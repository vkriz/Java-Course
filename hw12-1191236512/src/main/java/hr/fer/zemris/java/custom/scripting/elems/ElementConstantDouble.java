package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred koji nasljeđuje razred Element i predstavlja
 * double konstantu.
 * 
 * @author Valentina Križ
 *
 */
public class ElementConstantDouble extends Element {
	// read-only
	private double value;
	
	/**
	 * Konstruktor koji prima double vrijednost varijable value.
	 * 
	 * @param value vrijednost varijable
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * Getter za varijablu value.
	 * 
	 * @return vrijednost varijable
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return Double.toString(value);
	}
}
