package hr.fer.zemris.java.hw05.db;

/**
 * Razred koji predstavlja kondicionalan izraz.
 * Sastoji se od IFieldValueGetter-a, stringa s kojim se
 * uspoređuje i operatora uspoređivanja.
 * 
 * @author Valentina Križ
 *
 */
public class ConditionalExpression {
	IFieldValueGetter fieldGetter;
	String stringLiteral;
	IComparisonOperator comparisonOperator;

	/**
	 * Getter za varijablu fieldGetter.
	 * 
	 * @return fieldGetter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Getter za varijablu stringLiteral.
	 * 
	 * @return stringLiteral
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Getter za varijablu comparisonOperator.
	 * 
	 * @return comparisonOperator
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

	/**
	 * Konstruktor koji prima vrijednosti fieldGetter-a, stringLiteral-a i comparisonOperator-a.
	 * 
	 * @param fieldGetter
	 * @param stringLiteral
	 * @param comparisonOperator
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}
}
