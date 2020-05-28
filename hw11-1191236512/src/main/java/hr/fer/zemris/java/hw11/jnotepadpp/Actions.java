package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Razred predstavlja moguće akcije u implementiranom
 * tekst editoru. Sve akcije nasljeđuju LocalizableAction.
 */
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

public class Actions {
	/**
	 * Referenca na tekst editor
	 */
	private JNotepadPP notepad;
	
	/**
	 * Referenca na dokument model koji
	 * sadrži sve otvorene dokumente
	 */
	private DefaultMultipleDocumentModel docsModel;
	
	/**
	 * Referenca na trenutni localization provider
	 */
	private FormLocalizationProvider flp;
	
	/**
	 * Akcija za prebacivanje jezika na hrvatski.
	 */
	public Action hrLangAction;
	
	/**
	 * Akcija za prebacivanje jezika na engleski.
	 */
	public Action enLangAction;
	
	/**
	 * Akcija za prebacivanje jezika na njemački.
	 */
	public Action deLangAction;
	
	/**
	 * Akcija za otvaranje novog dokumenta.
	 */
	public Action newDocumentAction;
	
	/**
	 * Akcija za spremanje dokumenta.
	 */
	public Action saveDocumentAction;
	
	/**
	 * Akcija za spremanje dokumenta na određenu
	 * lokaciju.
	 */
	public Action saveAsDocumentAction;
	
	/**
	 * Akcija za otvaranje postojećeg dokumenta.
	 */
	public Action openDocumentAction;
	
	/**
	 * Akcija za zatvaranje dokumenta.
	 */
	public Action closeDocumentAction;
	
	/**
	 * Akcija za brisanje označenog dijela teksta
	 *  u dokumentu.
	 */
	public Action deleteSelectedPartAction;
	
	/**
	 * Akcija za kopiranje označenog dijela teksta.
	 */
	public Action copySelectedPartAction;
	
	/**
	 * Akcija za rezanje označenog dijela teksta.
	 */
	public Action cutSelectedPartAction;
	
	/**
	 * Akcija za lijepljenje kopiranog dijela teksta.
	 */
	public Action pasteSelectedPartAction;
	
	/**
	 * Akcija za ispis statistike dokumenta.
	 */
	public Action statisticalInfoAction;
	
	/**
	 * Akcija za zatvaranje editora.
	 */
	public Action exitAction;
	
	/**
	 * Akcija za uzlazno sortiranje linija
	 * teksta.
	 */
	public Action sortAscending;
	
	/**
	 * Akcija za silazno sortiranje linija
	 * teksta.
	 */
	public Action sortDescending;
	
	/**
	 * Akcija za promjenu svih slova označenog
	 * dijela teksta u velika.
	 */
	public Action toUppercase;
	
	/**
	 * Akcija za promjenu svih slova označenog
	 * dijela teksta u mala.
	 */
	public Action toLowercase;
	
	/**
	 * Akcija za promjenu malih slova u velika i 
	 * velikih slova u mala.
	 */
	public Action invertCase;
	
	/**
	 * Akcija za izbacivanje linija koje se ponavljaju
	 * iz označenog dijela teksta.
	 */
	public Action uniqueAction;

	
	/**
	 * Konstruktor koji prima referencu na JNotepadPP,
	 * MultimpleDocumentModel i FormLocalizationProvider.
	 * @param notepad
	 * @param docsModel
	 * @param flp
	 */
	public Actions(JNotepadPP notepad, DefaultMultipleDocumentModel docsModel, FormLocalizationProvider flp) {
		this.notepad = notepad;
		this.docsModel = docsModel;
		this.flp = flp;
		if(flp == null) {
			System.out.println("actionsnull");
		}
		initActions();
	}
	
	/**
	 * Metoda inicijalizira sve akcije.
	 */
	private void initActions() {
		sortAscending = new LocalizableAction("sortAscending", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sort("ascending");
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
			}
		};
		
		sortDescending = new LocalizableAction("sortDescending", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sort("descending");
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
			}
		};
		
		uniqueAction = new LocalizableAction("unique", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sort("unique");
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
			}
		};
		
		toUppercase = new LocalizableAction("toUppercase", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeCase("uppercase");
			}
		};
		
		toLowercase = new LocalizableAction("toLowercase", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeCase("lowercase");
			}
		};
		
		invertCase = new LocalizableAction("invert", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeCase("invert");
			}
		};
		
		hrLangAction = new LocalizableAction("croatian", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
		};
		
		enLangAction = new LocalizableAction("english", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
		};
		
		deLangAction = new LocalizableAction("german", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
		};
		
		openDocumentAction = new LocalizableAction("open", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open file");
				if(fc.showOpenDialog(notepad)!=JFileChooser.APPROVE_OPTION) {
					return;
				}
				File fileName = fc.getSelectedFile();
				
				docsModel.loadDocument(fileName.toPath());
			}
		};
		
		closeDocumentAction = new LocalizableAction("close", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				SingleDocumentModel doc = docsModel.getCurrentDocument();
				checkAndCloseDocument(doc, e);
			}
		};
		
		newDocumentAction = new LocalizableAction("new", flp) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				docsModel.createNewDocument();
			}
		};
		
		saveDocumentAction = new LocalizableAction("save", flp) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Path openedFilePath = docsModel.getCurrentDocument().getFilePath();
				
				if(openedFilePath==null) {
					saveAsDocumentAction.actionPerformed(e);
					return;
				}
				
				docsModel.saveDocument(docsModel.getCurrentDocument(), openedFilePath);
				}
		};
		
		saveAsDocumentAction = new LocalizableAction("saveAs", flp) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save document");
				if(jfc.showSaveDialog(notepad)!=JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(
							notepad, 
							"Ništa nije snimljeno.", 
							"Upozorenje", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				Path filePath = jfc.getSelectedFile().toPath();
				
				// ako postoji datoteka s tom putanjom pitaj smije
				// li ju pregaziti
				if(Files.exists(filePath)) {
					// provjeri da li je taj dokument otvoren u editoru
					for(int i = 0, len = docsModel.getNumberOfDocuments(); i < len; ++i) {
						SingleDocumentModel doc = docsModel.getDocument(i);
						if(doc.getFilePath() != null && doc.getFilePath().equals(filePath)) {
							String message = "File " + doc.getFilePath().getFileName() + " is currently open. Cannot save file on that path.";
							JOptionPane.showMessageDialog(null,
								    message);
							return;
						}
					}
					
					Object[] options = {"Yes",
			                "No"};
					String message = "The file " + filePath.getFileName() + " already exists. Do you want to continue and replace existing file?";
					String title = "File exists";
					
					int n = JOptionPane.showOptionDialog(null,
						    message,
						    title,
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.QUESTION_MESSAGE,
						    null,
						    options,
						    options[1]);
					
					if(n == JOptionPane.YES_OPTION) {
						docsModel.saveDocument(docsModel.getCurrentDocument(), filePath);
					} else {
						return;
					}
				}
				
				docsModel.saveDocument(docsModel.getCurrentDocument(), filePath);
			}
		};
		
		copySelectedPartAction = new LocalizableAction("copy", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Action copy = new DefaultEditorKit.CopyAction();
	            copy.actionPerformed(e);
			}
			
		};
		
		cutSelectedPartAction = new LocalizableAction("cut", flp) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
	            Action cut = new DefaultEditorKit.CutAction();
	            cut.actionPerformed(e);
			}
			
		};
		
		pasteSelectedPartAction = new LocalizableAction("paste", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Action paste = new DefaultEditorKit.PasteAction();
	            paste.actionPerformed(e);
			}
			
		};
		
		deleteSelectedPartAction = new LocalizableAction("delete", flp) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea editor = docsModel.getCurrentDocument().getTextComponent(); 
				Document doc = editor.getDocument();
				int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
				if(len==0) return;
				int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
				try {
					doc.remove(offset, len);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				docsModel.getCurrentDocument().setModified(true);
			}
		};
				
		statisticalInfoAction = new LocalizableAction("statisticalInfo", flp) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea textArea = docsModel.getCurrentDocument().getTextComponent();
				int numLines = textArea.getLineCount();
				String tekst = textArea.getText();
				int numChars = tekst.length();
				int numNonWhite = tekst.replaceAll("\\s+","").length();
				
				String message = "Your document has " + numChars + " characters "
								+ numNonWhite + " non-blank characters and "
								+ numLines + " lines.";
				
				JOptionPane.showMessageDialog(null,
					    message);
			}
			
		};
		
		exitAction = new LocalizableAction("exit", flp) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = docsModel.getNumberOfDocuments() - 1; i >= 0; --i){
					SingleDocumentModel doc = docsModel.getDocument(i);
					if(!checkAndCloseDocument(doc, e)) {
						break;
					}
				}
				
				notepad.dispose();
			}
		};
	}
	
	/**
	 * Pomoćna metoda koja zatvara zadani dokument, ali
	 * prije toga provjerava da li postoje nespremljene
	 * promjene i nudi korisniku mogućnost spremanja promjena,
	 * odbacivanja promjena ili odustajanja od zatvaranja.
	 * 
	 * @param doc
	 * @param e
	 * @return true ako je dokument zatvoren, false inače
	 */
	private boolean checkAndCloseDocument(SingleDocumentModel doc, ActionEvent e) {
		if(doc.isModified()) {
			String docName = doc.getFilePath() == null ? "unnamed" : doc.getFilePath().getFileName().toString();
			Object[] options = {"Save changes",
                    "Discard changes",
                    "Abort closing"};
			String message = "Document " + docName + " contains unsaved changes. What do you want to do?";
			String title = "Unsaved changes";
			
			int n = JOptionPane.showOptionDialog(null,
				    message,
				    title,
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[0]);
			
			if(n == JOptionPane.CANCEL_OPTION) {
				return false;
			} else if(n == JOptionPane.YES_OPTION) {
				saveDocumentAction.actionPerformed(e);
			}
			
			docsModel.closeDocument(doc);
		}
		return true;
	}
	
	/**
	 * Pomoćna metoda za sortiranje redaka u odabranom dijelu
	 * teksta ili izbacivanje ponavljanja istih redaka.
	 * sortType može biti "ascending", "descending" ili "unique".
	 * Ako je "ascending" retci se sortiraju uzlazno,
	 * ako je "descending" retci se sortiraju silazno,
	 * a ako je "unique" retci se ne sortiraju nego se izbacuju 
	 * sva ponavljanja istih redaka.
	 * 
	 * @param sortType
	 * @throws BadLocationException
	 */
	private void sort(String sortType) throws BadLocationException {
		JTextArea editor = docsModel.getCurrentDocument().getTextComponent(); 
		Document doc = editor.getDocument();

		int dotOffset = editor.getCaret().getDot();
		int markOffset = editor.getCaret().getMark();
		
		int linesStart = editor.getLineStartOffset(editor.getLineOfOffset(Math.min(dotOffset, markOffset)));
		int linesEnd = editor.getLineEndOffset(editor.getLineOfOffset(Math.max(dotOffset, markOffset)));
		
		if(linesEnd == linesStart) {
			return; 
		}
		String selectedText = doc.getText(linesStart, linesEnd - linesStart);
		List<String> lines = new ArrayList<>(Arrays.asList(selectedText.split("\\r?\\n")));

		Comparator<String> comparator = null;
		Locale locale = new Locale(LocalizationProvider.getInstance().getLanguage());
		Collator collator = Collator.getInstance(locale);
		
		if(sortType.equals("ascending")) {
			comparator = (line1, line2) -> collator.compare(line1, line2);	
		} else if(sortType.equals("descending")) {
			comparator = (line1, line2) -> collator.reversed().compare(line1, line2);	
		}

		if(sortType.equals("unique")) {
			LinkedHashSet<String> hashSet = new LinkedHashSet<>(lines);
			lines = new ArrayList<>(hashSet);
		} else {
			lines.sort(comparator);
		}
				
		StringBuilder sb = new StringBuilder();
		for(String line : lines) {
			sb.append(line + "\n");
		}
		
		doc.remove(linesStart, linesEnd - linesStart);
		doc.insertString(linesStart, sb.toString().substring(0, sb.length() - 1), null);
		
		docsModel.getCurrentDocument().setModified(true);
	}
	
	/**
	 * Pomoćna metoda za promjenu slova.
	 * changeType može biti "lowercase", "uppercase"
	 * ili "invert".
	 * Ako je "lowercase" sva slova prelaze u mala slova,
	 * ako je "uppercase" sva slova prelaze u velika slova,
	 * a ako je "invert" mala slova postaju velika, a velika
	 * postaju mala.
	 * 
	 * @param changeType
	 */
	private void changeCase(String changeType) {
		JTextArea editor = docsModel.getCurrentDocument().getTextComponent(); 
		Document doc = editor.getDocument();
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		int offset = 0;
		if(len==0) {
			return;
		}
		
		offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
	
		try {
			String text = doc.getText(offset, len);
			
			switch(changeType) {
				case "uppercase":
					text = text.toUpperCase();
					break;
				case "lowercase":
					text = text.toLowerCase();
					break;
				case "invert":
					text = toggleCase(text);
			}
			
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch(BadLocationException ex) {
			ex.printStackTrace();
		}
		docsModel.getCurrentDocument().setModified(true);
	}
	
	/**
	 * Pomoćna metoda koja prima tekst i mijenja 
	 * mala slova u velika i velika u mala.
	 * 
	 * @param text
	 * @return promijenjeni tekst
	 */
	private String toggleCase(String text) {
		char[] znakovi = text.toCharArray();
		for(int i = 0; i < znakovi.length; i++) {
			char c = znakovi[i];
			if(Character.isLowerCase(c)) {
				znakovi[i] = Character.toUpperCase(c);
			} else if(Character.isUpperCase(c)) {
				znakovi[i] = Character.toLowerCase(c);
			}
		}
		return new String(znakovi);
	}

	/**
	 * Metoda "kreira" akcije, odnosno za svaku akciju
	 * definira njen opis i kombinaciju tipki koja ju
	 * pokreće.
	 */
	public void createActions() {
		newDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control N")); 
		newDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_N); 
		newDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to create a new blank document."); 
		
		openDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control O")); 
		openDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_O); 
		openDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to open existing file from disk."); 
		
		closeDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control L")); 
		closeDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_L); 
		closeDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to close current document. Changes will not be saved."); 

		saveDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control S")); 
		saveDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_S); 
		saveDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to save current file to disk.");
		
		saveAsDocumentAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("F12")); 
		saveAsDocumentAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_W); 
		saveAsDocumentAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to save current file to disk."); 
		
		deleteSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("F2")); 
		deleteSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_T); 
		deleteSelectedPartAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to delete the selected part of text."); 
		
		uniqueAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control R")); 
		uniqueAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_R); 
		uniqueAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to removes from selected text all lines which are duplicates"); 
		
		copySelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control C")); 
		copySelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_C); 
		copySelectedPartAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to copy characters from selected part of text.");
		
		pasteSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control V")); 
		pasteSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_V); 
		pasteSelectedPartAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to paste coppied characters to current position in document.");
		
		cutSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control X")); 
		cutSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_X); 
		cutSelectedPartAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Used to cut characters from selected part of text.");
		
		statisticalInfoAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control I"));
		statisticalInfoAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_I); 
		statisticalInfoAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Get statistical info about document.");
		
		exitAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control Q"));
		exitAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_Q); 
		exitAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Exit application."); 
		
		hrLangAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control H"));
		hrLangAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_H); 
		hrLangAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Change language to Croatian."); 
		
		deLangAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control G"));
		deLangAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_G); 
		deLangAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Change language to German."); 
		
		enLangAction.putValue(
				Action.ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke("control E"));
		enLangAction.putValue(
				Action.MNEMONIC_KEY, 
				KeyEvent.VK_E); 
		enLangAction.putValue(
				Action.SHORT_DESCRIPTION, 
				"Change language to English."); 
	}
}
