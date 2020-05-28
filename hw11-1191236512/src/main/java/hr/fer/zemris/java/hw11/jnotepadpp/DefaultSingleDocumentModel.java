package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Razred predstavlja jedan dokument koji se
 * prikazuje u tekst editoru.
 * Implementira sučelje SingleDocumentModel.
 * 
 * @author Valentina Križ
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	/**
	 * Referenca na JTextArea u kojem se prikazuje tekst
	 * dokumenta
	 */
	private JTextArea textArea;
	
	/**
	 * Putanja do dokumenta
	 */
	private Path filePath;
	
	/**
	 * Varijabla koja govori da li postoje
	 * nespremljene promjene
	 */
	private boolean modified;
	
	/**
	 * Lista listenera
	 */
	private List<SingleDocumentListener> listeners;
	
	/**
	 * Konstruktor koji prima putanju do datoteke
	 * i tekst datoteke.
	 * 
	 * @param filePath
	 * @param textContent
	 */
	public DefaultSingleDocumentModel(Path filePath, String textContent) {		
		textArea = new JTextArea(textContent);
		this.filePath = filePath;
		modified = false;
		
		listeners = new ArrayList<>();
		
		textArea.getDocument().addDocumentListener(new DocumentListener() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}
			
		});
	}
	
	/**
	 * Pomoćna metoda koja obavještava listenere da je
	 * došlo do promjene varijable koja prati ima li 
	 * nespremljenih promjena.
	 */
	private void notifyDocumentModifyStatusUpdated() {
		for(SingleDocumentListener l : listeners) {
			l.documentModifyStatusUpdated(this);
		}
	}
	
	/**
	 * Pomoćna metoda koja obavještava listenere da je
	 * došlo do promjene u putanji dokumenta.
	 */
	private void notifyDocumentFilePathUpdated() {
		for(SingleDocumentListener l : listeners) {
			l.documentFilePathUpdated(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFilePath(Path path) {
		if(path == null) {
			throw new IllegalArgumentException("Putanja do datoteke ne može biti null referenca.");
		}
		
		filePath = path;
		notifyDocumentFilePathUpdated();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isModified() {
		return modified;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		notifyDocumentModifyStatusUpdated();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}
}
