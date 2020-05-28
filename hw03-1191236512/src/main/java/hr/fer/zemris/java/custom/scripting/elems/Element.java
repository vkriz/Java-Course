package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred koji služi za reprezentaciju izraza pri parsiranju dokumenata.
 * 
 * @author Valentina Križ
 *
 */
public class Element {
	/**
	 * Metoda koji element prikazuje u obliku stringa.
	 * 
	 * @return string reprezentacija elementa
	 */
	public String asText() {
		return new String(); 
	}
	
	/**
	 * Metoda za provjeru jednakosti dvaju elemenata.
	 * 
	 * @param e2 element s kojim uspoređujemo
	 * @return true ako su jednaki, false inače
	 */
	public boolean equals(Element e2) {
		return asText().equals(e2.asText());
	}
}
