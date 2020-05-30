package hr.fer.zemris.java.custom.scripting.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Razred koji predstavlja stog i nudi sve operacije nad stogom.
 * 
 * @author Valentina Križ
 *
 */
public class ObjectStack {
	// služi za spremanje elemenata stoga
	private List<Object> arrayCol;
	
	/**
	 * Konstruktor koji stvara prazan stog.
	 */
	public ObjectStack() {
		arrayCol = new ArrayList<>();
	}
	
	/**
	 * Metoda koja provjerava da li je stog prazan.
	 * 
	 * @return true ako je stog prazan, false inače
	 */
	public boolean isEmpty() {
		return arrayCol.isEmpty();
	}
	
	/**
	 * Metoda koja vraća broj elemenata na stogu.
	 * 
	 * @return broj elemenata
	 */
	public int size() {
		return arrayCol.size();
	}
	
	/**
	 * Metoda koja na vrh stoga dodaje zadani element.
	 * 
	 * @param value objekt koji korisnik želi dodati na stog
	 */
	public void push(Object value) {
		// nije potrebna provjera value != null jer ArrayIndexedCollection ne dopušta obacivanje null referenci
		// složenost je O(1) jer je složenost ArrayIndexedCollection.add također O(1)
		arrayCol.add(value);
	}
	
	/**
	 * Metoda koja skida element s vrha stoga.
	 * 
	 * @return skinuti objekt
	 */
	public Object pop() {
		/*
		 * dohvaća element s vrha stoga ako stog nije prazan (inače generira iznimku)
		 * složenost je O(1) jer je složenost peek i ArrayIndexedCollection.remove(index)
		 * također O(1) (u slučaju index = size - 1)
		 */
		Object top = peek();
		// i briše ga
		arrayCol.remove(size() - 1);
		return top;
	}
	
	/**
	 * Metoda koja provjerava koji element se nalazi na vrhu stoga.
	 * 
	 * @return objekt s vrha stoga
	 * @throws EmptyStackException ako je stog prazan
	 */
	public Object peek() {
		if(size() < 1) {
			throw new EmptyStackException();
		}
		// složenost je O(1) jer je složenost ArrayIndexedCollection.get također O(1)
		return arrayCol.get(size() - 1);
	}
	
	/**
	 * Metoda koja miče sve elemente sa stoga.
	 */
	public void clear() {
		arrayCol.clear();
	}
}
