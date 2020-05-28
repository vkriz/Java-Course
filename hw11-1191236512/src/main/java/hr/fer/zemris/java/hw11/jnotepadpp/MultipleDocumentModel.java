package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Sučelje predstavlja model koji sadrži
 * 0, 1 ili više dokumenata i zna koji od
 * tih dokumenata je trenutni dokument (koji
 * se prikazuje korisniku i na kojem korisnik radi).
 * 
 * @author Valentina Križ
 *
 */
public interface MultipleDocumentModel {
	/**
	 * Metoda kreira novi, prazni dokument i
	 * vraća ga.
	 * 
	 * @return novi dokument
	 */
	SingleDocumentModel createNewDocument();
	
	/**
	 * Metoda vraća dokument koji se trenutno
	 * prikazuje korisniku.
	 * 
	 * @return aktivni dokument
	 */
	SingleDocumentModel getCurrentDocument();
	
	/**
	 * Metoda učitava dokument na temelju
	 * putanje do datoteke iz koje se dokument
	 * treba učitati.
	 * 
	 * @param path putanja do datoteke
	 * @return učitani dokument
	 */
	SingleDocumentModel loadDocument(Path path);
	
	/**
	 * Metoda sprema dokument u datoteku
	 * sa zadanom putanom.
	 * 
	 * @param model dokument
	 * @param newPath putanja do datoteke
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	
	/**
	 * Metoda zatvara zadani dokument.
	 * 
	 * @param model dokument
	 */
	void closeDocument(SingleDocumentModel model);
	
	/**
	 * 
	 * @param l
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * 
	 * @param l
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Metoda vraća broj dokumenata u modelu.
	 * 
	 * @return broj dokumenata
	 */
	int getNumberOfDocuments();
	
	/**
	 * Metoda vraća dokument sa zadanim indeksom.
	 * Indeks predstavlja redni broj dokumenta 
	 * (po redoslijedu otvaranja).
	 * 
	 * @param index
	 * @return dokument
	 */
	SingleDocumentModel getDocument(int index);
}
