package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Sučelje koje predstavlja listenera za
 * SingleDocumentModel.
 * 
 * @author Valentina Križ
 *
 */
public interface SingleDocumentListener {
	/**
	 * Metoda koja se pokreće kada dođe do promjene 
	 * modify statusa dokumenta.
	 * 
	 * @param model
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Metoda koja se pokreće kada dođe do promjene
	 * putane dokumenta.
	 * 
	 * @param model
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
