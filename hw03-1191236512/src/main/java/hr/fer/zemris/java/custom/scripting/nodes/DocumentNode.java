package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Razred koji predstavlja cijeli dokument.
 * Nasljeđuje razred Node.
 * 
 * @author Valentina Križ
 *
 */
public class DocumentNode extends Node {
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0, size = numberOfChildren(); i < size; ++i) {
			sb.append(getChild(i).toString());
		}
		return sb.toString();
	}
}
