package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Razred koji predstavlja for-loop konstrukt.
 * Nasljeđuje razred Node.
 * 
 * @author Valentina Križ
 *
 */
public class ForLoopNode extends Node {
	private ElementVariable variable;
	private Element startExpression;
	private Element endExpression;
	private Element stepExpression;
	
	/**
	 * Getter za varijablu variable.
	 * 
	 * @return vrijednost varijable
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Setter za varijablu variable.
	 * 
	 * @param variable nova vrijednost varijable
	 */
	public void setVariable(ElementVariable variable) {
		if(variable == null) {
			throw new NullPointerException();
		}
		this.variable = variable;
	}
	
	/**
	 * Getter za varijablu startExpression.
	 * 
	 * @return vrijednost varijable
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Setter za varijablu startExpression.
	 * 
	 * @param variable nova vrijednost varijable
	 */
	public void setStartExpression(Element startExpression) {
		if(startExpression == null) {
			throw new NullPointerException();
		}
		this.startExpression = startExpression;
	}
	
	/**
	 * Getter za varijablu endExpression.
	 * 
	 * @return vrijednost varijable
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Setter za varijablu endExpression.
	 * 
	 * @param variable nova vrijednost varijable
	 */
	public void setEndExpression(Element endExpression) {
		if(endExpression == null) {
			throw new NullPointerException();
		}
		this.endExpression = endExpression;
	}
	
	/**
	 * Getter za varijablu stepExpression.
	 * 
	 * @return vrijednost varijable
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * Setter za varijablu stepExpression.
	 * 
	 * @param variable nova vrijednost varijable
	 */
	public void setStepExpression(Element stepExpression) {
		this.stepExpression = stepExpression;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$ FOR ");
		sb.append(variable.asText());
		sb.append(" ");
		sb.append(startExpression.asText());
		sb.append(" ");
		sb.append(endExpression.asText());
		sb.append(" ");
		if(stepExpression != null) {
			sb.append(stepExpression.asText());
		}
		sb.append("$}");
		
		for(int i = 0, size = numberOfChildren(); i < size; ++i) {
			sb.append(getChild(i).toString());
		}
		sb.append("{$END$}");
		
		return sb.toString();
	}
	
	/**
	 * Metoda koja služi za provjeru jednakosti dvaju čvorova.
	 * 
	 * @param node čvor s kojim uspoređujemo
	 * @return true ako su jednaki, false inače
	 */
	public boolean equals(ForLoopNode node) {
		if(!compareChildren(node)) {
			return false;
		}
		
		if(variable.equals(node.getVariable())
				&& startExpression.equals(node.getStartExpression())
				&& endExpression.equals(node.getEndExpression())
				&& ((stepExpression == null && node.getStepExpression() == null)
						|| stepExpression.equals(node.getStepExpression()))) {
			return true;
		}
		
		return false;
	}
}
