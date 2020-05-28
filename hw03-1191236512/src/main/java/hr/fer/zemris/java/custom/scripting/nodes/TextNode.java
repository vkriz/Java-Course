package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Razred koji predstavlja tekstualan podatak.
 * Nasljeđuje razred Node.
 * 
 * @author Valentina Križ
 *
 */
public class TextNode extends Node {
	private String text;
	
	/**
	 * Konstruktor koji prima tekst.
	 * 
	 * @param text
	 */
	public TextNode(Object text) {
		this.text = text.toString();
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	/**
	 * Metoda koja služi za provjeru jednakosti dvaju čvorova.
	 * 
	 * @param node čvor s kojim uspoređujemo
	 * @return true ako su jednaki, false inače
	 */
	public boolean equals(TextNode node) {
		if(!compareChildren(node)) {
			return false;
		}
		return toString().equals(node.toString());
	}
}
