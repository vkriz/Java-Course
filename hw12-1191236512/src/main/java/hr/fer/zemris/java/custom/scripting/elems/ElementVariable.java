package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred koji nasljeđuje razred Element i predstavlja
 * varijablu.
 * 
 * @author Valentina Križ
 *
 */
public class ElementVariable extends Element {
	// read-only
	private String name;
	
	/**
	 * Konstruktor koji prima ime varijable.
	 * 
	 * @param value ime funkcije
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	/**
	 * Getter za varijablu name.
	 * 
	 * @return vrijednost varijable
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return name;
	}
}
