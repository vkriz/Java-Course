package hr.fer.zemris.java.hw05.db;

/**
 * Strategija koja služi za dohvaćanje vrijednosti
 * atributa sa zadanim imenom.
 * 
 * @author Valentina Križ
 *
 */
public interface IFieldValueGetter {
	/**
	 * Metoda koja dohvaća vrijednost atributa.
	 * 
	 * @param record zapis u bazi
	 * @return vrijednost atributa
	 */
	public String get(StudentRecord record);
}
