package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Razred koji predstavlja kolekciju jednog ili
 * više dokumenta, a koji nasljeđuje JTabbedPane i 
 * implementira MultipleDocumentModel.
 * 
 * @author Valentina Križ
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Lista dokumenata
	 */
	private List<SingleDocumentModel> documents;
	
	/**
	 * Dokument koji se trenutno prikazuje
	 */
	private SingleDocumentModel current;
	
	/**
	 * Lista listenera
	 */
	private List<MultipleDocumentListener> listeners;
	
	/**
	 * Ikona koja označava da ima nespremljenih promjena
	 */
	private static ImageIcon redIcon;
	
	/**
	 * Ikona koja označava da nema nespremljenih promjena
	 */
	private static ImageIcon greenIcon;
	
	/**
	 * Konstruktor.
	 * Učitava potrebne ikone i postavlja listenere.
	 */
	public DefaultMultipleDocumentModel() {
		redIcon = loadIcon("red");
		greenIcon = loadIcon("green");
		
		documents = new ArrayList<>();
		listeners = new ArrayList<>();
		
		addChangeListener(e -> {
            current = documents.get(getSelectedIndex());
            notifyCurrentDocumentChanged(current);
        });
		
		listeners.add(new MultipleDocumentListener() {

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if(previousModel == null && currentModel == null) {
					throw new IllegalArgumentException();
				}
				
				current = currentModel;
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				documents.add(model);
				
				Path filePath = model.getFilePath();
				String name = filePath == null ? "unnamed" : filePath.getFileName().toString();
				String tooltip = filePath == null ? "unnamed" : filePath.toString();
				
				addTab(name, null, new JScrollPane(model.getTextComponent()), tooltip);
				setSelectedIndex(documents.size() - 1);
				setIconAt(documents.size() - 1, greenIcon);
				//current = model;
			}

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				// TODO Auto-generated method stub
				
			}});
		
		current = createNewDocument();
	}
	
	/**
	 * Pomoćna metoda za učitavanje ikona.
	 * Color označava boju ikone koju treba učitati.
	 * 
	 * @param color
	 * @return učitana ikona
	 */
	private ImageIcon loadIcon(String color) {
		InputStream is = this.getClass().getResourceAsStream("icons/" + color + "-icon.png");
		if(is==null) {
			throw new IllegalArgumentException("Wrong icon path.");
		}
		byte[] bytes;
		try {
			bytes = is.readAllBytes();
			is.close();
			
			ImageIcon icon = new ImageIcon(bytes); // load icon
			Image image = icon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(12, 12,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			icon = new ImageIcon(newimg);  // transform it back
			
			return icon;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel document = new DefaultSingleDocumentModel(null, "");
		
		document.addSingleDocumentListener(new SingleDocumentListener() {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				if(model.isModified()) {
					setIconAt(getSelectedIndex(), redIcon);
				} else {
					setIconAt(getSelectedIndex(), greenIcon);
				}
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				Path filePath = model.getFilePath();
				String name = filePath == null ? "unnamed" : filePath.getFileName().toString();
				String tooltip = filePath == null ? "unnamed" : filePath.toString();
				setTitleAt(getSelectedIndex(), name);
				setToolTipTextAt(getSelectedIndex(), tooltip);
			}
		});
		
		notifyDocumentAdded(document);
		notifyCurrentDocumentChanged(document);
		
		return document;
	}

	/**
	 * Pomoćna metoda koja obavještava listenere
	 * da je došlo do promjene dokumenta koji se trenutno prikazuje.
	 * 
	 * @param newCurrent novi dokument
	 */
	private void notifyCurrentDocumentChanged(SingleDocumentModel newCurrent) {
		for(MultipleDocumentListener l : listeners) {
			l.currentDocumentChanged(current, newCurrent);
		}
	}

	/**
	 * Pomoćna metoda koja obavještava listenere
	 * da je došlo do dodavanja novog dokumenta u model.
	 */
	private void notifyDocumentAdded(SingleDocumentModel model) {
		for(MultipleDocumentListener l : listeners) {
			l.documentAdded(model);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleDocumentModel getCurrentDocument() {
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if(!Files.isReadable(path)) {
			JOptionPane.showMessageDialog(
					current.getTextComponent(), 
					"Datoteka "+ path.toAbsolutePath()+" ne postoji!", 
					"Pogreška", 
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		byte[] okteti;
		try {
			okteti = Files.readAllBytes(path);
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(
					current.getTextComponent(), 
					"Pogreška prilikom čitanja datoteke "+path.toAbsolutePath()+".", 
					"Pogreška", 
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		String tekst = new String(okteti, StandardCharsets.UTF_8);

		// ako je već negdje otvoren taj dokument odi u taj tab
		for(SingleDocumentModel doc : documents) {
			if(doc.getFilePath() != null && doc.getFilePath().equals(path)) {
				current = doc;
				notifyCurrentDocumentChanged(doc);
				setSelectedIndex(documents.indexOf(doc));
				
				return current;
			}
		}

		// inače napravi novi tab i otvori ga
		SingleDocumentModel newDoc = new DefaultSingleDocumentModel(path, tekst);
		newDoc.addSingleDocumentListener(new SingleDocumentListener() {

			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				if(model.isModified()) {
					setIconAt(getSelectedIndex(), redIcon);
				} else {
					setIconAt(getSelectedIndex(), greenIcon);
				}
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				Path filePath = model.getFilePath();
				String name = filePath == null ? "unnamed" : filePath.getFileName().toString();
				String tooltip = filePath == null ? "unnamed" : filePath.toString();
				setTitleAt(getSelectedIndex(), name);
				setToolTipTextAt(getSelectedIndex(), tooltip);
			}
		});
		notifyDocumentAdded(newDoc);
		notifyCurrentDocumentChanged(newDoc);
		
		return newDoc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		JTextArea editor = model.getTextComponent(); 

		byte[] podatci = editor.getText().getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(newPath, podatci);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(
					this, 
					"Pogreška prilikom zapisivanja datoteke "+newPath.toFile().getAbsolutePath()+".\nPažnja: nije jasno u kojem je stanju datoteka na disku!", 
					"Pogreška", 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(
				this, 
				"Datoteka je snimljena.", 
				"Informacija", 
				JOptionPane.INFORMATION_MESSAGE);
		
		getCurrentDocument().setModified(false);
		getCurrentDocument().setFilePath(newPath);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		
		// ako je to bio jedini otvoreni dokument otvori novi
		if(documents.size() == 1) {
			createNewDocument();
		}
		
		removeTabAt(index);
		documents.remove(model);
		
		// prebaci se na tab lijevo od trenutnog
		// ili na novostvoreni tab (pozicija 0)
		index = Math.max(0, --index);
		setSelectedIndex(index);
		current = documents.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}

}
