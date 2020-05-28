package hr.fer.zemris.java.hw05.db;

/**
 * Razred koji služi za dohvaćanje vrijednosti određenog
 * atributa. 
 * 
 * @author Valentina Križ
 *
 */
public class FieldValueGetters {
	/**
	 * Metoda za dohvaćanje imena.
	 */
	public static final IFieldValueGetter FIRST_NAME = (record) -> {
		return record.getFirstName();
	};
	
	/**
	 * Metoda za dohvaćanje prezimena.
	 */
	public static final IFieldValueGetter LAST_NAME = (record) -> {
		return record.getLastName();
	};
	
	/**
	 * Metoda za dohvaćanje JMBAG-a.
	 */
	public static final IFieldValueGetter JMBAG = (record) -> {
		return record.getJmbag();
	};
}
