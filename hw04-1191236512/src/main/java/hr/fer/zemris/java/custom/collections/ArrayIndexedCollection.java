package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Razred koji predstavlja kolekciju objekata u obliku arraya,
 * a nasljeđuje razred Collection.
 * 
 * @author Valentina Križ
 *
 * @param <T> tip objekata u kolekciji
 */
public class ArrayIndexedCollection<T> implements List<T> {
	// trenutni broj elemenata u kolekciji
	private int size;
	// objekti koji se nalaze u kolekciji
	private T[] elements;
	// brojač promjena kolekcije
	private long modificationCount;
	
	/**
	 * Razred čija je zadaća korisniku vraćati element po
	 * element kolekcije, na korisnikov zahtjev.
	 * 
	 * @param <S> tip objekata u kolekciji
	 */
	private static class ArrayElementsGetter<S> implements ElementsGetter<S> {
		private int currentIndex;
		private ArrayIndexedCollection<S> collection;
		private long savedModificationCount;
		
		public ArrayElementsGetter(ArrayIndexedCollection<S> col) {
			currentIndex = -1;
			collection = col;
			savedModificationCount = collection.modificationCount;
		}
		
		public boolean hasNextElement() {
			if(savedModificationCount != collection.modificationCount) {
				throw new ConcurrentModificationException();
			}
			return currentIndex + 1 < collection.size;
		}
		
		public S getNextElement() {
			if(!hasNextElement()) {
				throw new NoSuchElementException();
			}
			
			currentIndex += 1;
			return collection.get(currentIndex);
		}
	}
	
	/**
	 * Konstruktor koji stvara kolekciju zadanog kapaciteta koja sadrži sve elemente iz neke druge kolekcije.
	 * Ukoliko zadana kolekcija sadrži više elemenata od predanog kapaciteta,
	 * kapacitet nove kolekcije se postavlja na veličinu predane kolekcije.
	 * 
	 * @param other kolekcija čiji se objekti ubacuju u novu kolekciju
	 * @param initialCapacity kapacitet kolekcije
	 * @throws NullPointerException ako je predana kolekcija null
	 */
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(Collection<T> other, int initialCapacity) {
		if(other == null) {
			throw new NullPointerException("Nije dopušteno predati konstruktoru null referencu.");
		}
		if(initialCapacity < 1) {
			throw new IllegalArgumentException("Minimalan kapacitet kolekcije je 1.");
		}
		
		size = 0;
		elements = (T[])new Object[Math.max(initialCapacity, other.size())];
		modificationCount = 0;
		addAll(other);
	}
	
	/**
	 * Konstruktor koji stvara kolekciju kapaciteta 16 koja sadrži sve elemente iz neke druge kolekcije.
	 * 
	 * @param other kolekcija čiji se objekti ubacuju u novu kolekciju
	 */
	public ArrayIndexedCollection(Collection<T> other) {
		this(other, 16);
	}
	
	/**
	 * Konstruktor koji stvara praznu kolekciju čiji je kapacitet određen predanim parametrom.
	 * 
	 * @param initialCapacity kapacitet kolekcije
	 * @throws IllegalArgumentException ako je predan kapacitet manji od 1
	 */
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException("Minimalan kapacitet kolekcije je 1.");
		}
		
		size = 0;
		elements = (T[]) new Object[initialCapacity];
		modificationCount = 0;
	}
	
	/**
	 * Konstruktor koji stvara praznu kolekciju kapaciteta 16.
	 */
	public ArrayIndexedCollection() {
		this(16);
	}
	
	/**
	 * Metoda koja vraća broj objekata u kolekciji.
	 * 
	 * @return broj objekata
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Metoda koja vraća kapacitet kolekcije (za potrebe testiranja).
	 * 
	 * @return kapacitet
	 */
	public int capacity() {
		return elements.length;
	}
	
	/**
	 * Metoda koja u kolekciju ubacuje zadani objekt na zadanu poziciju.
	 * Ukoliko je kolekcija puna, njen kapacitet se povećava na dupli
	 * stari kapacitet.
	 * 
	 * @param value objekt koji korisnik želi ubaciti u kolekciju
	 * @param position indeks na koji želi ubaciti objekt
	 */
	@SuppressWarnings("unchecked")
	public void insert(T value, int position) {
		if(value == null) {
			throw new NullPointerException("U kolekciju nije dopušteno ubacivanje null referenci.");
		}
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException("Indeks izvan granica kolekcije.");
		}
		
		boolean realocated = false;
		// ako je kolekcija puna, povećaj kapacitet
		if(size == elements.length) {
			T[] newElements = (T[]) new Object[2 * elements.length];
			for(int i = 0; i < size; ++i) {
				newElements[i] = elements[i];
			}
			elements = newElements;
			modificationCount += 1;
			realocated = true;
		}
		
		// ako već nisi (zbog realokacije), a došlo je do shifta elemenata, povećaj modificationCount
		if(!realocated && position != size) {
			modificationCount += 1;
		}
		
		// shift elemenata udesno
		for(int i = size; i > position; --i) {
			elements[i] = elements[i - 1];
		}
		elements[position] = value;
		size += 1;
	}
	
	/**
	 * Metoda koja u kolekciju dodaje zadani objekt.
	 * Ukoliko je kolekcija puna, njen kapacitet se povećava na dupli kapacitet.
	 * 
	 * @param value objekt koji korisnik želi ubaciti u kolekciju
	 */
	@Override
	public void add(T value) {
		// ubacuje element za prvo slobodno mjesto (s indexom size)
		// složenost ostaje O(1) jer je složenost metode insert(value, position) O(1) za position = size
		insert(value, size);
	}
	
	/**
	 * Metoda vraća objekt koji se nalazi u kolekciji na zadanom indeksu.
	 * 
	 * @param index indeks koji korisnika zanima
	 * @return objekt koji se nalazi na zadanom indeksu
	 * @throws IndexOutOfBoundsException ako indeks nije u dopuštenom rasponu
	 */
	public T get(int index) {
		if(index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException("Indeks izvan granica kolekcije.");
		}
		return elements[index];
	}
	
	/**
	 * Metoda koja izbacuje sve elemente iz kolekcije.
	 */
	@Override
	public void clear() {
		for(int i = 0; i < size; ++i) {
			elements[i] = null;
		}
		size = 0;
		modificationCount += 1;
	}
	
	/**
	 * Metoda koja vraća indeks prvog pojavljivanja zadanog objekta u kolekciji.
	 * 
	 * @param value objekt za koji korisnik želi saznati poziciju
	 * @return pozicija objekta u kolekciji
	 */
	public int indexOf(Object value) {
		for(int i = 0; i < size; ++i) {
			if(elements[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Metoda koja vraća nalazi li se zadani objekt u kolekciji.
	 * 
	 * @param value objekt koji tražimo
	 * @return true ako se nalazi, false inače
	 */
	@Override
	public boolean contains(Object value) {
		if(indexOf(value) != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Metoda koja sve objekte iz kolekcije sprema u novi array objekata.
	 * 
	 * @return novonastali array objekata
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		for(int i = 0; i < size; ++i) {
			array[i] = elements[i];
		}
		return array;
	}
	
	/**
	 * Metoda koja briše objekt koji se nalazi na zadanoj poziciji u kolekciji.
	 * 
	 * @param index pozicije s koje korisnik želi obrisati objekt
	 * @throws IndexOutOfBounds Exception ako indeks nije u dopuštenom rasponu
	 */
	public void remove(int index) {
		if(index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException("Indeks izvan granica kolekcije.");
		}
		// modificationCount se ne povećava ako je obrisan zadnji element jer nema strukturne modifikacije
		if(index != size - 1) {
			modificationCount += 1;
		}
		for(int i = index; i < size - 1; ++i) {
			elements[i] = elements[i + 1];
		}
		elements[size - 1] = null;
		size -= 1;
	}
	
	/**
	 * Metoda koja briše jedno pojavljivanje zadanog objekta iz kolekcije.
	 * 
	 * @param objekt koji korisnik želi obrisati
	 * @return true ako je uspješno izbrisan, false inače
	 */
	@Override
	public boolean remove(Object value) {
		// ako je predana null referenca, znamo da se ona ne nalazi u kolekciji
		if(value == null) {
			return false;
		}
		
		int index = indexOf(value);
		if(index != -1) {
			remove(index);
			return true;
		}
		return false;
	}
	
	public ElementsGetter<T> createElementsGetter() {
		return new ArrayElementsGetter<T>(this);
	}
}
