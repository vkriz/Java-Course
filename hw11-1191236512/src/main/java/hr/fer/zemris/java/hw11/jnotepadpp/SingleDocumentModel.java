package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Sučelje predstavlja jedan dokument. Sadrži podatke
 * o putanji do datoteke iz koje je dokument učitan (može biti
 * null za novi dokument), status modificiranosti dokumenta i 
 * referencu na Swing komponentu koja se koristi za uređivanje 
 * (svaki dokument ima svoju komponentu za uređivanje).
 * 
 * @author Valentina Križ
 *
 */
public interface SingleDocumentModel {
	/**
	 * Metoda vraća referencu na Swing komponentu
	 * koja se koristi za uređivanje dokumenta.
	 * 
	 * @return referenca na komponentu
	 */
	public JTextArea getTextComponent();

	/**
	 * Metoda vraća putanju do datoteke
	 * iz koje je dokument učitan.
	 * Vraća null za novi dokument.
	 * 
	 * @return putanja do datoteke ili null
	 */
	public Path getFilePath();
	
	/**
	 * Metoda postavlja putanju do datoteke
	 * iz koje je dokument učitan na zadanu
	 * vrijednost.
	 * 
	 * @param path putanja do datoteke
	 * @throws IllegalArgumentException ako je 
	 * 			path null referenca
	 */
	public void setFilePath(Path path);
	
	
	/**
	 * Metoda provjerava da li postoje
	 * promjene na dokumentu koje nisu spremljene.
	 * 
	 * @return true ako postoje, false inače
	 */
	public boolean isModified();
	
	/**
	 * Metoda služi za mijenjanje statusa
	 * modificiranosti dokumenta.
	 * 
	 * @param modified novi status
	 */
	public void setModified(boolean modified);
	
	/**
	 * Metoda dodaje novi listener za dokumet.
	 * 
	 * @param l
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Metoda uklanja listener iz liste listenera
	 * za dokument.
	 * 
	 * @param l
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
