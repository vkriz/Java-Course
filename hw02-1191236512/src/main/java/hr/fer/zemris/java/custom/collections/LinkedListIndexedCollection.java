package hr.fer.zemris.java.custom.collections;

/**
 * Razred koji predstavlja povezanu listu objekata.
 * 
 * @author Valentina Križ
 *
 */
public class LinkedListIndexedCollection extends Collection {
	/*
	 * Pomoćni razred koji predstavlja jedan čvor u povezanoj listi.
	 * Sadrži referencu na prethodni i sljedeći čvor te referencu na vrijednost čvora.
	 *
	 */
	private static class ListNode {
		ListNode previous;
		ListNode next;
		Object value;
	}
	
	private int size;
	private ListNode first;
	private ListNode last;
	
	/**
	 * Konstruktor koji stvara praznu kolekciju.
	 */
	public LinkedListIndexedCollection() {
		first = last = null;
	}
	
	/**
	 * Konstruktor koji stvara novu kolekciju čiji elementi su objekti
	 * iz neke druge kolekcije.
	 * @param other
	 */
	public LinkedListIndexedCollection(Collection other) {
		this();
		addAll(other);
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
	 * Metoda koja u kolekciju ubacuje zadani element na kraj kolekcije.
	 */
	@Override
	public void add(Object value) {
		if(value == null) {
			throw new NullPointerException("U kolekciju nije dopušteno ubacivanje null referenci.");
		}
		
		ListNode newNode = new ListNode();
		newNode.previous = last;
		newNode.next = null;
		newNode.value = value;
		
		if(size == 0) {
			first = newNode;
		}
		
		if(last != null) {
			last.next = newNode;
		}
		last = newNode;
		size += 1;		
	}
	
	/**
	 * Pomoćna metoda koja nalazi ListNode koji se nalazi na zadanom indeksu,
	 * ako je indeks iz dozvoljenog raspona (0 do size-1).
	 * 
	 * @param index pozicija na kojoj se nalazi ListNode
	 * @return čvor na toj poziciji
	 * @throws IndexOutOfBoundsException ako je indeks izvan dozvoljenog raspona
	 */
	private ListNode find(int index) {
		if(index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException("Indeks izvan granica kolekcije.");
		}
		
		ListNode current = first;
		// ako se nalazi u prvoj polovici liste kreni od first, inače od last
		if(index < size / 2) {
			for(int i = 0; i < index; ++i ) {
				current = current.next;
			}	
		} else {
			current = last;
			for(int i = size - 1; i > index; --i ) {
				current = current.previous;
			}
		}
		return current;
	}
	
	/**
	 * Metoda koja pronalazi element kolekcije na zadanoj poziciji.
	 * 
	 * @param index pozicija koja korisnika zanima
	 * @return objekt koji se nalazi na zadanoj poziciji
	 */
	public Object get(int index) {
		return find(index).value;
	}
	
	/**
	 * Metoda koja briše sve elemente iz kolekcije.
	 */
	@Override
	public void clear() {
		first = last = null;
		size = 0;
	}
	
	/**
	 * Metoda koja ubacuje zadani element na zadanu poziciju u kolekciji.
	 * 
	 * @param value element koji korisnik želi ubaciti
	 * @param position pozicija na koju korisnik želi ubaciti element
	 */
	public void insert(Object value, int position) {
		if(value == null) {
			throw new NullPointerException("U kolekciju nije dopušteno ubacivanje null referenci.");
		}

		ListNode newNode = new ListNode();
		newNode.value = value;
		
		// ako ubacujemo na početak
		if(position == 0) {
			newNode.previous = null;
			newNode.next = first;
			first.previous = newNode;
		} else if(position == size) {
			// ako ubacujemo na kraj
			add(value);
		} else {
			// ako ubacujemo u "sredinu" liste
			ListNode current = find(position);
			newNode.previous = current.previous;
			newNode.next = current;
			current.previous.next = newNode;
			current.previous = newNode;
		}
		size += 1;
	}
	
	/**
	 * Metoda koja vraća poziciju zadanog elementa u kolekciji.
	 * 
	 * @param value element čiju poziciju korisnik želi saznati
	 * @return pozicija zadanog elementa u kolekciji
	 */
	public int indexOf(Object value) {
		ListNode current = first;

		for(int i = 0; i < size; ++i) {
			if(current.value.equals(value)) {
				return i;
			}
			current = current.next;
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
		return indexOf(value) != -1;
	}
	
	/**
	 * Metoda koja svaki objekt iz kolekcije obrađuje pomoću procesora. 
	 * 
	 * @param processor procesor koji se koristi za obradu objekata
	 */
	public void forEach(Processor processor) {
		ListNode current = first;

		for(int i = 0; i < size; ++i) {
			processor.process(current.value);
			current = current.next;
		}
	}
	
	/**
	 * Metoda koja sve objekte iz kolekcije sprema u novi array objekata.
	 * 
	 * @return novonastali array objekata
	 */
	public Object[] toArray() {
		Object[] array = new Object[size];
		ListNode current = first;
		for(int i = 0; i < size; ++i) {
			array[i] = current.value;
			current = current.next;
		}
		return array;
	}
	
	/**
	 * Metoda koja briše element na zadanoj poziciji iz kolekcije.
	 * 
	 * @param index pozicija elementa koji korisnik želi obrisati
	 */
	public void remove(int index) {
		if(index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException("Indeks izvan granica kolekcije.");
		}
		
		// ako je prvi element
		if(index == 0) {
			first = first.next;
			first.previous = null;
		} else if(index == size - 1) {
			// ako je zadnji element
			last = last.previous;
			last.next = null;
		} else {
			// ako je u "sredini" liste
			ListNode current = find(index);
			current.next.previous = current.previous;
			current.previous.next = current.next;
		}
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
		/*
		 * U ovo metodi se koristi sličan kod kao u metodi remove(int index), ali
		 * traženje indeksa objekta pa pozivanje metode remove(int index) na tom indeksu nema smisla 
		 * jer bi se tada dva puta prolazilo kroz listu.
		 * Zato samo prolazimo kroz listu i kad nađemo čvor sa zadanom vrijednosti brišemo ga.
		 */
		
		// ako je predana null referenca, znamo da se ona ne nalazi u kolekciji
		if(value == null) {
			return false;
		}
		
		ListNode current = first;
		for(int i = 0; i < size; ++i) {
			if(current.value.equals(value)) {
				// ako je prvi element
				if(i == 0) {
					first = first.next;
					first.previous = null;
				} else if(i == size - 1) {
					// ako je zadnji element
					last = last.previous;
					last.next = null;
				} else {
					// ako je u "sredini" liste
					current.next.previous = current.previous;
					current.previous.next = current.next;
				}
				size -= 1;
				return true;
			} 
			current = current.next;
		}
		
		return false;
	}
}
