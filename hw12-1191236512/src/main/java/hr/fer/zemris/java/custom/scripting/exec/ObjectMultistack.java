package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * Razred predstavlja strukturu podataka
 * nalik na mape uz sljedeću modifikaciju:
 * za jedan ključ može postojati više različitih
 * vrijednosti, a za svaki ključ je vezana struktura
 * u obliku stoga koji sadrži sve vrijednosti koje
 * su povezane sa zadanim ključem.
 * Ponuđene su operacije pop, push i peek za svaki
 * od stogova u mapi.
 * 
 * @author Valentina Križ
 *
 */
public class ObjectMultistack {
	/**
	 * Mapa koja predstavlja opisanu strukturu
	 */
	Map<String, MultistackEntry> multistack;
	
	/**
	 * Konstruktor
	 */
	public ObjectMultistack() {
		multistack = new HashMap<> ();
	}
	
	/**
	 * Pomoćni razred koji predstavlja jedan stog
	 * u mapi (za jedan ključ).
	 * 
	 * @author Valentina Križ
	 *
	 */
	private static class MultistackEntry {
		/**
		 * Vrijednost 
		 */
		private ValueWrapper value;
		/**
		 * Referenca na sljedeći objekt
		 */
		private MultistackEntry next;
		
		/**
		 * Konstruktor koji prima vrijednost
		 * i referencu na sljedeći objekt na "stogu".
		 * 
		 * @param value
		 * @param next
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
	}
	
	/**
	 * Metoda u stog vezani za zadani ključ ubacuje
	 * novu vrijednost.
	 * 
	 * @param keyName ključ
	 * @param valueWrapper nova vrijednost
	 */
	public void push(String keyName, ValueWrapper valueWrapper ) {
		MultistackEntry newEntry = new MultistackEntry(valueWrapper, multistack.get(keyName));
		multistack.put(keyName, newEntry);
	}
	
	/**
	 * Metoda iz stoga vezanog za zadni ključ
	 * izbacuje vrijednost s vrha stoga i vraća
	 * ju.
	 * 
	 * @param keyName
	 * @return vrijednost s vrha stoga
	 * @throws EmptyStackException ako je stog prazan
	 */
	public ValueWrapper pop(String keyName) {
		if(isEmpty(keyName)) {
			throw new EmptyStackException();
		}
		
		MultistackEntry entry = multistack.get(keyName);
		multistack.put(keyName, entry.next);
		
		return entry.value;
	}
	
	/**
	 * Metoda iz stoga vezanog za zadni ključ
	 * dohvaća vrijednost s vrha stoga bez
	 * izbacivanja.
	 * 
	 * @param keyName
	 * @return vrijednost s vrha stoga
	 * @throws EmptyStackException ako je stog prazan
	 */
	public ValueWrapper peek(String keyName) {
		if(isEmpty(keyName)) {
			throw new EmptyStackException();
		}
		
		return multistack.get(keyName).value;
	}
	
	/**
	 * Metoda provjerava da li je stog
	 * vezan za zadani ključ prazan.
	 * 
	 * @param keyName
	 * @return true ako je prazan, false inače
	 */
	public boolean isEmpty(String keyName) {
		return multistack.get(keyName) == null;
	}
}
