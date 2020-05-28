package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred koji nasljeđuje razred Element i predstavlja
 * funkciju.
 * 
 * @author Valentina Križ
 *
 */
public class ElementFunction extends Element {
	// read-only
	private String name;
	
	/**
	 * Konstruktor koji prima ime funkcije.
	 * 
	 * @param value ime funkcije
	 */
	public ElementFunction(String name) {
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
		return "@" + name;
	}
}
