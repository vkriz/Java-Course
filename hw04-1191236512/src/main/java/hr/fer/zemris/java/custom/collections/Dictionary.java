package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji predstavlja kolekciju objekata u odnosu
 * ključ - vrijednost.
 * 
 * @author Valentina Križ
 *
 * @param <K> tip ključa
 * @param <V> tip vrijednosti
 */
public class Dictionary<K, V> {
	private ArrayIndexedCollection<Pair<K, V>> array;
	
	/**
	 * Pomoćni razred koji predstavlja jedan par
	 * ključ - vrijednost.
	 * 
	 * @param <S> tip ključa
	 * @param <T> tip vrijednosti
	 */
	private static class Pair<S, T> {
		private S key;
		private T value;
		
		/**
		 * Konstruktor koji ključ i vrijednost
		 * postavlja na zadane vrijednosti.
		 * 
		 * @param key ključa
		 * @param value vrijednost 
		 */
		private Pair(S key, T value) {
			this.key = key;
			this.value = value;
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
	}
	
	/**
	 * Default konstrutor.
	 */
	public Dictionary() {
		array = new ArrayIndexedCollection<Pair<K, V>>();
	}
	
	/**
	 * Metoda koja provjerava da li je riječnik prazan. 
	 * 
	 * @return true ako je riječnik prazan, false inače
	 */
	public boolean isEmpty() {
		return array.isEmpty();
	}
	
	/**
	 * Metoda koja vraća trenutnu veličinu riječnika.
	 * 
	 * @return broj parova u riječniku
	 */
	public int size() {
		return array.size();
	}
	
	/**
	 * Metoda koja izbacuje sve parove iz riječnika.
	 */
	public void clear() {
		array.clear();
	}
	
	/**
	 * Metoda koja u riječnik ubacuje novi par ključ - vrijednost.
	 * Ukoliko u riječniku već postoji zadani ključ, ne ubacuje se novi
	 * par nego se samo mijenja vrijednost.
	 * 
	 * @param key ključ
	 * @param value vrijednost
	 * 
	 * @throws NullPointerException ako je predan null ključ
	 */
	public void put(K key, V value) {
		if(key == null) {
			throw new NullPointerException("Ključ ne smije biti null.");
		}
		
		Pair<K, V> pair = new Pair<>(key, value);
				
		// ako pronađeš key, updateaj value
		ElementsGetter<Pair<K, V>> getter = array.createElementsGetter();
		while(getter.hasNextElement()) {
			Pair<K, V> currentPair = getter.getNextElement();
			if(currentPair.getKey() == key) {
				array.remove(currentPair);
				array.add(pair);
				return;
			}
		}
	
		// inače dodaj novi par
		array.add(pair);
	}
	
	/**
	 * Metoda koja za zadani ključ vraća vrijednost vezanu za taj 
	 * ključ. Ako pripadna vrijednost ne postoji, vraća null.
	 * 
	 * @param key
	 * @return vrijednost
	 */
	public V get(Object key) {
		// prođi kroz riječnik i probaj naći ključ
		ElementsGetter<Pair<K, V>> getter = array.createElementsGetter();
		while(getter.hasNextElement()) {
			Pair<K, V> currentPair = getter.getNextElement();
			if(currentPair.getKey() == key) {
				return currentPair.getValue();
			}
		}

		// ako ga ne nađeš, vrati null
		return null;
	}
}
