package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Sučelje koje predstavlja listenera
 * za MultipleDocumentModel.
 * 
 * @author Valentina Križ
 *
 */
public interface MultipleDocumentListener {
	/**
	 * Metoda koja se pokreće kada dođe do promjene
	 * trenutnog dokumenta.
	 * 
	 * @param previousModel
	 * @param currentModel
	 * @throws IllegalArgumentException ako su i 
	 * 			previousModel i currentModel null
	 * 			reference
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel,
			SingleDocumentModel currentModel);
	
	/**
	 * Metoda koja se pokreće kada se doda novi dokument.
	 * 
	 * @param model
	 */
	void documentAdded(SingleDocumentModel model);
	
	/**
	 * Metoda koja se pokreće kada se ukloni jedan dokument.
	 * 
	 * @param model
	 */
	void documentRemoved(SingleDocumentModel model);
}
