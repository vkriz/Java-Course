package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred koji nasljeđuje razred Element i predstavlja
 * string.
 * 
 * @author Valentina Križ
 *
 */
public class ElementString extends Element {
	// read-only
	private String value;
	
	/**
	 * Konstruktor koji prima vrijednost stringa.
	 * 
	 * @param value vrijednost stringa
	 */
	public ElementString(String value) {
		this.value = value;
	}
	
	/**
	 * Getter za varijablu value.
	 * 
	 * @return vrijednost varijable
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		// generiranje potrebnih escapeova kako bi se na tekstu ponovno mogla provesti leksička analiza
		return "\"" + value.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r").replace("\"", "\\\"") + "\"";
	}
}
