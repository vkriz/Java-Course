package hr.fer.zemris.java.custom.collections;

/**
 * Interface predstavlja listu objekata i nudi
 * standardne metode za ubacivanje, brisanje i dohvaćanje
 * elemenata.
 * 
 * @author Valentina Križ
 *
 */
public interface List<T> extends Collection<T> {
	T get(int index);
	void insert(T value, int position);
	int indexOf(Object value);
	void remove(int index);
}
