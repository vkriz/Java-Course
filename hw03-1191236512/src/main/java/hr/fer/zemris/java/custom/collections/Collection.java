package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji predstavlja neku generalnu kolekciju objekata.
 * Razred nudi standardne metode za upravljanje kolekcijom.
 * 
 * @author Valentina Križ
 *
 */
public interface Collection {
	/**
	 * Metoda koja dohvaća redom sve elemente iz predane kolekcije, te u trenutnu
	 * kolekciju ubacuje sve elemente koje predani tester prihvati.
	 * 
	 * @param col kolekcija čiji elementi se testiraju
	 * @param test tester
	 */
	default void addAllSatisfying(Collection col, Tester test) {
		ElementsGetter getter = col.createElementsGetter();
		while(getter.hasNextElement()) {
			Object obj = getter.getNextElement();
			if(test.test(obj)) {
				add(obj);
			}
		}
	}
	
	/**
	 * Metoda koja provjerava da li je kolekcija prazna (sadrži 0 objekata).
	 * 
	 * @return true ako je kolekcija prazna, false inače
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Metoda koja vraća broj objekata u kolekciji.
	 * 
	 * @return broj objekata u kolekciji
	 */
	int size();
	
	/**
	 * Metoda koja ubacuje objekt u kolekciju.
	 * 
	 * @param value element koji korisnik želi ubaciti
	 */
	void add(Object value);
	
	/**
	 * Metoda koja provjerava nalazi li se zadani objekt u kolekciji.
	 * 
	 * @param value objekt za koji korisnik želi obaviti provjeru
	 * @return true ako se objekt nalazi u kolekciji, false inače
	 */
	boolean contains(Object value);
	
	/**
	 * Metoda koja iz kolekcije izbacuje zadani objekt.
	 * 
	 * @param value objekt koji korisnik želi izbaciti
	 * @return true ako je objekt uspješno izbačen, false inače
	 */
	boolean remove(Object value);
	
	/**
	 * Metoda koja sve objekte iz kolekcije sprema u novi array objekata.
	 * 
	 * @return novonastali array objekata
	 */
	Object[] toArray();
	
	/**
	 * Metoda koja svaki objekt iz kolekcije obrađuje pomoću procesora. 
	 * 
	 * @param processor procesor koji se koristi za obradu objekata
	 */
	default void forEach(Processor processor) {
		ElementsGetter getter = createElementsGetter();
		while(getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}
	
	/**
	 * Metoda koja kolekciju puni objektima iz neke druge kolekcije.
	 * 
	 * @param other kolekcija iz koje se uzimaju objekti
	 */
	default void addAll(Collection other) {
		class LocalProcessor implements Processor {
			public void process(Object value) {
				add(value);
			}
		}
		
		LocalProcessor lp = new LocalProcessor();
		other.forEach(lp);
	}
	
	/**
	 * Metoda koja izbacuje sve objekte iz kolekcije.
	 */
	void clear();
	
	/**
	 * Metoda za stvaranje objekta koji omogućuje vraćanje
	 * elementa po elementa iz kolekcije.
	 */
	ElementsGetter createElementsGetter();
}
