package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji predstavlja neku generalnu kolekciju objekata.
 * Razred nudi standardne metode za upravljanje kolekcijom.
 * 
 * @author Valentina Križ
 *
 */
public class Collection {
	/**
	 * Default konstruktor
	 */
	protected Collection() {
		
	}
	
	/**
	 * Metoda koja provjerava da li je kolekcija prazna (sadrži 0 objekata).
	 * 
	 * @return true ako je kolekcija prazna, false inače
	 */
	public boolean isEmpty() {
		if(size() > 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Metoda koja vraća broj objekata u kolekciji.
	 * U ovom razredu uvijek vraća 0.
	 * 
	 * @return broj objekata u kolekciji
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Metoda koja ubacuje objekt u kolekciju.
	 * U ovom razredu metoda ne radi ništa.
	 * 
	 * @param value element koji korisnik želi ubaciti
	 */
	public void add(Object value) {
		
	}
	
	/**
	 * Metoda koja provjerava nalazi li se zadani objekt u kolekciji.
	 * U ovom razredu uvijek vraća false.
	 * 
	 * @param value objekt za koji korisnik želi obaviti provjeru
	 * @return true ako se objekt nalazi u kolekciji, false inače
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Metoda koja iz kolekcije izbacuje zadani objekt.
	 * U ovom razredu metoda uvijek vraća false.
	 * 
	 * @param value objekt koji korisnik želi izbaciti
	 * @return true ako je objekt uspješno izbačen, false inače
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Metoda koja sve objekte iz kolekcije sprema u novi array objekata.
	 * U ovom razredu uvijek baca iznimku UnsupportedOperationException.
	 * 
	 * @return novonastali array objekata
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Metoda koja svaki objekt iz kolekcije obrađuje pomoću procesora. 
	 * U ovom razredu metoda ne radi ništa.
	 * 
	 * @param processor procesor koji se koristi za obradu objekata
	 */
	public void forEach(Processor processor) {
		
	}
	
	/**
	 * Metoda koja kolekciju puni objektima iz neke druge kolekcije.
	 * 
	 * @param other kolekcija iz koje se uzimaju objekti
	 */
	public void addAll(Collection other) {
		class LocalProcessor extends Processor {
			public void process(Object value) {
				add(value);
			}
		}
		
		LocalProcessor lp = new LocalProcessor();
		other.forEach(lp);
	}
	
	/**
	 * Metoda koja izbacuje sve objekte iz kolekcije.
	 * U ovom razredu metoda ne radi ništa.
	 */
	public void clear() {
		
	}
}
