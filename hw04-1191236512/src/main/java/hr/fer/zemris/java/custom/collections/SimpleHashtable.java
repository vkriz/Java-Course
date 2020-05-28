package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Razred predstavlja tablicu raspršenog
 * adresiranja koja omogućava pohranu uređenih parova (ključ, vrijednost).
 * 
 * @author Valentina Križ
 *
 * @param <K> tip ključa
 * @param <V> tip vrijednosti
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	private TableEntry<K, V>[] table;
	private int size;
	private int modificationCount;
	
	/**
	 * Pomoćni razred koji predstavlja jedan slot tablice.
	 * 
	 * @author Valentina Križ
	 *
	 * @param <S> tip ključa
	 * @param <T> tip vrijednosti
	 */
	public static class TableEntry<S, T> {
		private S key;
		private T value;
		// varijabla koja pokazuje na sljedeći primjerak koji se nalazi u istom slotu tablice
		private TableEntry<S, T> next;
		
		/**
		 * Konstruktor za zadanim ključem i vrijednosti.
		 * 
		 * @param key ključ
		 * @param value vrijednost
		 */
		public TableEntry(S key, T value) {
			this.key = key;
			this.value = value;
			next = null;
		}
		
		/**
		 * Getter za varijablu key.
		 * 
		 * @return vrijednost varijable key
		 */
		public S getKey() {
			return key;
		}
		
		/**
		 * Getter za varijablu value.
		 * 
		 * @return vrijednost varijable value
		 */
		public T getValue() {
			return value;
		}
		
		/**
		 * Setter za varijablu value.
		 * 
		 * @param value nova vrijednost varijable
		 */
		public void setValue(T value) {
			this.value = value;
		}
		
		/**
		 * Metoda za prikaz uređenog para u obliku stringa.
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			if(this != null && key != null) {
				sb.append(key);
				sb.append('=');
				sb.append(value);
			}
			
			return sb.toString();
		}
	}
	
	/**
	 * Razred koji ostvaruje iterator.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		private int currentSlot;
		private TableEntry<K, V> currentPair;
		private boolean calledNext;
		private int currentModificationCount;
		
		/**
		 * Default konstruktor.
		 */
		public IteratorImpl() {
			currentSlot = 0;
			currentPair = null;
			calledNext = false;
			currentModificationCount = modificationCount;
		}
		
		/**
		 * Metoda koja provjerava postoji li u kolekciji element iza 
		 * trenutnog elementa.
		 * 
		 * @return true ako postoji sljedeći element, false inače
		 * 
		 * @throws ConcurrentModificationException ako je kolekcija modificirana izvana
		 */
		public boolean hasNext() {
			if(currentModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Kolekcija je modificirana izvana.");
			}
			
			if(currentPair != null && currentPair.next != null) {
				return true;
			}
			
			if(currentPair == null || (currentPair != null && currentPair.next == null)) {
				int nextSlot = findNextNonEmptySlot();
				if(nextSlot == table.length) {
					return false;
				} 
			}
			return true;
		}
		
		/**
		 * Vraća sljedeći element u kolekciji ako postoji.
		 *
		 * @return sljedeći element
		 * 
		 * @throws ConcurrentModificationException ako je kolekcija modificirana izvana
		 * @throws NoSuchElementException ako ne postoji sljedeći element
		 */
		public SimpleHashtable.TableEntry<K, V> next() {
			if(currentModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Kolekcija je modificirana izvana.");
			}
			
			calledNext = true;
			if(currentPair != null && currentPair.next != null) {
				currentPair = currentPair.next;
				return currentPair;
			}
			
			if(currentPair == null || (currentPair != null && currentPair.next == null)) {
				int nextSlot = findNextNonEmptySlot();
				if(nextSlot == table.length) {
					throw new NoSuchElementException("Ne postoji sljedeći element u kolekciji.");
				}
				currentPair = table[nextSlot];
				currentSlot = nextSlot;
			}
			return currentPair;
		}
		
		/**
		 * Metoda koja iz kolekcije briše trenutni element.
		 * 
		 * @throws ConcurrentModificationException ako je kolekcija modificirana izvana
		 */
		public void remove() {
			if(!calledNext) {
				throw new IllegalStateException("Metoda remove se ne može pozivati prije dohvaćanja novog elementa.");
			}
			
			if(currentModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Kolekcija je modificirana izvana.");
			}
			
			SimpleHashtable.this.remove(currentPair.key);
			++currentModificationCount;
			calledNext = false;
		}
		
		/**
		 * Pomoćna metoda koja pronalazi sljedeći neprazan slot.
		 * 
		 * @return indeks slota
		 */
		private int findNextNonEmptySlot() {
			for(int i = currentSlot + 1, len = table.length; i < len; ++i) {
				if(table[i] != null) {
					return i;
				}
			}

			// ako smo došli do kraja
			return table.length;
		}
	}
	
	
	/**
	 * Default konstruktor, stvara novu tablicu kapaciteta 16.
	 */
	public SimpleHashtable() {
		this(16);
	}
	
	/**
	 * Konstruktor koji stvara tablicu veličine koja je potencija broja 2 koja je prva veća ili jednaka predanom broju.
	 * 
	 * @param capacity željeni kapacitet
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if(capacity < 1) {
			throw new IllegalArgumentException("Početni kapacitet ne može biti manji od 1.");
		}
		
		int finalCapacity = 0; 
		
		// ako je potencija broja 2
		if(capacity > 0 && ((capacity & (capacity - 1)) == 0)) {
			finalCapacity = capacity;
		} else {
			// inače pronađi prvi veći broj koji je potencija broja 2
			int log2Cap = (int)(Math.log(capacity) / Math.log(2));
			finalCapacity = (int)Math.pow(2, log2Cap + 1);
		}
		
		table = new TableEntry[finalCapacity];
		size = 0;
		modificationCount = 0;
	}
	
	/**
	 * Pomoćna fja za pronalazak uređenog para sa zadanim ključem.
	 * 
	 * @param key vrijednost ključa
	 * @return pronađeni par ili null ako nije pronađen
	 */
	private TableEntry<K, V> findEntryByKey(Object key) {
		if(key == null) {
			return null;
		}
		
		int position = Math.abs(key.hashCode() % table.length);

		TableEntry<K, V> current = table[position];
		
		while(current != null && current.key != null && !current.key.equals(key)) {
			current = current.next;
		}
		
		// null ako smo došli do kraja slota u kojem bi trebao biti
		return current;
	}
	
	/**
	 * Pomoćna metoda koja služi za povećanje kapaciteta tablice.
	 */
	@SuppressWarnings("unchecked")
	private void increaseCapacity() {
		TableEntry<K, V>[] allEntries = null;
		allEntries = new TableEntry[size];
		
		int counter = 0;
		
		// spremi sve parove u allEntries
		for(int i = 0, len = table.length; i < len; ++i) {
			TableEntry<K, V> current = table[i];

			while(current != null) {
				allEntries[counter] = current;
				current = current.next;
				++counter;
			}	
		}
		
		// povećaj kapacitet
		table = new TableEntry[table.length * 2];
		size = 0;
		++modificationCount;
		
		// ubaci sve parove
		for(int i = 0, len = allEntries.length; i < len; ++i) {
			put(allEntries[i].key, allEntries[i].value);
		}
	}
	
	/**
	 * Metoda za ubacivanje uređenog para u tablicu.
	 * Ako već postoji uređeni par sa zadanim ključem, ne ubacuje se novi uređeni par, 
	 * nego se postojeći par ažurira novom vrijednošću.
	 * 
	 * @param key ključ
	 * @param value vrijednost
	 */
	public void put(K key, V value) {
		if(key == null) {
			throw new NullPointerException("Ključ ne može biti null referenca.");
		}
		
		int position = Math.abs(key.hashCode() % table.length);

		TableEntry<K, V> current = table[position];
		TableEntry<K, V> previous = current;
		boolean isFirstEntry = true;
		
		while(current != null  && current.key != null && !current.key.equals(key)) {
			previous = current;
			current = current.next;
			isFirstEntry = false;
		}
		
		// ako ne postoji par sa zadanim ključem napravi novi
		if(current == null) {
			current = new TableEntry<K, V>(key, value);
			++size;
			
			// ako je prvi u slotu
			if(isFirstEntry) {
				table[position] = current;
			} else {
				previous.next = current;
			}
			
			++modificationCount;
			if((double)size / table.length >= 0.75) {
				increaseCapacity();
			}
		} else {
			// ako postoji, updateaj value
			current.value = value;
		}
	}
	
	/**
	 * Metoda vraća vrijednost za zadani ključ.
	 * Ako je predan null ključ ili ključ koji se ne nalazi u tablici,
	 * metoda vraća null.
	 * 
	 * @param key ključ
	 * @return vrijednost ili null
	 */
	public V get(Object key) {
		TableEntry<K, V> entry = findEntryByKey(key);
		
		return (entry != null) ? entry.value : null;
	}
	
	/**
	 * Metoda koja vraća broj uređenih parova u tablici.
	 * 
	 * @return broj parova
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Metoda koja provjerava nalazi li se u tablici uređeni par
	 * sa zadanim ključem.
	 * 
	 * @param key ključ
	 * @return true ako se nalazi, false inače
	 */
	public boolean containsKey(Object key) {
		TableEntry<K, V> entry = findEntryByKey(key);
		
		return entry != null;
	}
	
	/**
	 * Metoda koja provjerava nalazi li se u tablici uređeni par
	 * sa zadanom vrijednosti.
	 * 
	 * @param value vrijednost
	 * @return true ako se nalazi, false inače
	 */
	public boolean containsValue(Object value) {
		for(int i = 0, len = table.length; i < len; ++i) {
			TableEntry<K, V> current = table[i];
			
			while(current != null) {
				if((value == null && current.value == null) || current.value.equals(value)) {
					return true;
				}
				current = current.next;
			}
		}
		return false;
	}
	
	/**
	 * Metoda za uklanjanje uređenog para sa zadanim ključem.
	 * 
	 * @param key ključ
	 */
	public void remove(Object key) {
		// ako je predana null referenca ne radi ništa
		if(key == null) {
			return;
		}
		
		// pronađi slot
		int position = Math.abs(key.hashCode() % table.length);

		TableEntry<K, V> current = table[position];
		TableEntry<K, V> previous = current;
		
		// pronađi par
		while(current != null && current.key != null && !current.key.equals(key)) {
			previous = current;
			current = current.next;
		}
		
		// ako postoji par sa zadanim ključem obriši ga
		if(current != null) {
			--size;
			++modificationCount;
			
			// ako je prvi element
			if(current == previous) {
				table[position] = current.next;
			} else {
				// ako je na kraju ili u sredini liste
				previous.next = current.next;
			}
		}
	}
	
	/**
	 * Metoda koja provjerava je li tablica prazna.
	 * 
	 * @return true ako je, false inače
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Metoda koja tablicu pretvara u string.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(int i = 0, len = table.length; i < len; ++i) {
			TableEntry<K, V> current = table[i];
			
			while(current != null && current.key != null) {
				if(sb.toString().length() > 1) {
					sb.append(", ");
					sb.append(current.toString());
				} else {
					sb.append(current.toString());
				}
				current = current.next;
			}
		}
		
		sb.append(']');
		return sb.toString();
	}	
	
	/**
	 * Metoda koja uklanja sve uređene parove iz tablice.
	 */
	public void clear() {
		for(int i = 0, len = table.length; i < len; ++i) {
			table[i] = null;
			size = 0;
		}
		++modificationCount;
	}
	
	/**
	 * Metoda za stvaranje novog iteratora.
	 */
	public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
}
