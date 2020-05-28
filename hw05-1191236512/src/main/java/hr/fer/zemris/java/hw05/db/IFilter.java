package hr.fer.zemris.java.hw05.db;

/**
 * Sučelje koji služi za filtriranje zapisa u bazi.
 * 
 * @author Valentina Križ
 *
 */
public interface IFilter {
	/**
	 * Metoda za filtriranje zapisa.
	 * 
	 * @param record zapis iz baze
	 * @return true ako zapis zadovoljava uvjete filtera,
	 * 			false inače
	 */
	public boolean accepts(StudentRecord record);
}
