package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Razred koji predstavlja komandu koja dinamički generira
 * tekstualni output.
 * Nasljeđuje razred Node.
 * 
 * @author Valentina Križ
 *
 */
public class EchoNode extends Node {
	private Element[] elements;
	
	/**
	 * Konstruktor koji prima polje elemenata.
	 * 
	 * @param elements
	 * @throws NullPointerException ako je predana null referenca
	 */
	public EchoNode(Element[] elements) {
		if(elements == null) {
			throw new NullPointerException();
		}
		this.elements = elements;
	}
	
	/**
	 * Getter za varijablu elements.
	 * 
	 * @return vrijednost varijable
	 */
	public Element[] getElements() {
		return elements;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$= ");
		for(Element element : elements) {
			if(element != null) {
				sb.append(element.asText());
				sb.append(" ");
			}
		}
		sb.append("$}");
		return sb.toString();
	}
	
	/**
	 * Metoda koja služi za provjeru jednakosti dvaju čvorova.
	 * 
	 * @param node čvor s kojim uspoređujemo
	 * @return true ako su jednaki, false inače
	 */
	public boolean equals(EchoNode node) {
		if(!compareChildren(node)) {
			return false;
		}
		if(elements == null && node.getElements() == null) {
			return true;
		}
		
		Element[] nodeElements = node.getElements();
		if(elements.length != nodeElements.length) {
			return false;
		}
		
		for(int i = 0, size = elements.length; i < size; ++i) {
			if(elements[i]!=null && nodeElements[i]!=null && !elements[i].equals(nodeElements[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
