package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Sučelje za potrebe komunikacije instance razreda MyShell s korisnikom.
 * 
 * @author Valentina Križ
 *
 */
public interface Environment {
	/**
	 * Metoda za čitanje retka koji je unesao korisnik.
	 * 
	 * @return pročitani redak
	 * @throws ShellIOException
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Metoda za ispis teksta korisniku.
	 * 
	 * @param text tekst koji treba ispisati
	 * @throws ShellIOException
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Metoda za ispis linije teksta korisniku.
	 * 
	 * @param text tekst koji treba ispisati
	 * @throws ShellIOException
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Metoda vraća sortiranu mapu svih naredbi koje
	 * instanca MyShell razreda podržava.
	 * Ključ je ime naredbe, a vrijednost sama naredba.
	 * 
	 * @return sortirana mapa naredbi
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Metoda vraća simbol koji je potrebno postaviti
	 * prije imena naredbe koja će se protezati kroz
	 * nekoliko redaka.
	 * 
	 * @return simbol
	 */
	Character getMultilineSymbol();
	
	/**
	 * Metoda postavlja simbol koji je potrebno postaviti
	 * prije imena naredbe koja će se protezati kroz
	 * nekoliko redaka na zadani character.
	 * 
	 * @param symbol
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Metoda vraća simbol koji MyShell ispisuje prilikom
	 * čekanja unosa nove naredbe.
	 * 
	 * @return simbol
	 */
	Character getPromptSymbol();
	
	/**
	 * Metoda postavlja simbol koji MyShell ispisuje prilikom
	 * čekanja unosa nove naredbe na zadani character.
	 * 
	 * @return symbol
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Metoda vraća simbol koji je potrebno postaviti
	 * na početku svakog novog retka kroz koji se proteže
	 * naredba.
	 * 
	 * @return simbol
	 */
	Character getMorelinesSymbol();
	
	/**
	 * Metoda postavlja simbol koji je potrebno postaviti
	 * na početku svakog novog retka kroz koji se proteže
	 * naredba na zadani character.
	 * 
	 * @param symbol
	 */
	void setMorelinesSymbol(Character symbol);
}
