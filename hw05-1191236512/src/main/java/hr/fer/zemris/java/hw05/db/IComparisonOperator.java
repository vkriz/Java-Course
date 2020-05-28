package hr.fer.zemris.java.hw05.db;

/**
 * Strategija koja služi za uspoređivanje dvaju stingova
 * sa zadanim operatorom uspoređivanja.
 * 
 * @author Valentina Križ
 *
 */
public interface IComparisonOperator {
	/**
	 * Metoda koja uspoređuje dva stringa.
	 * 
	 * @param value1 prvi string
	 * @param value2 drugi sting
	 * @return true vrijedi value1 ? value2, gdje je ? operator uspoređivanja,
	 * 			false inače
	 */
	public boolean satisfied(String value1, String value2);
}
