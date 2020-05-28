package hr.fer.zemris.java.custom.collections;

/**
 * Interface predstavlja listu objekata i nudi
 * standardne metode za ubacivanje, brisanje i dohvaćanje
 * elemenata.
 * 
 * @author Valentina Križ
 *
 */
public interface List extends Collection {
	Object get(int index);
	void insert(Object value, int position);
	int indexOf(Object value);
	void remove(int index);
}
