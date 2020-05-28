package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import java.awt.Container;

/**
 * Razred predstavlja tekst editor u kojemu
 * se istovremeno može otvoriti jedan ili više dokumenata.
 * Dokumenti se mogu uređivati i spremati.
 * 
 * @author Valentina Križ
 *
 */
public class JNotepadPP extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultMultipleDocumentModel docsModel;
	private Actions actions;
	private JLabel infoLabel;
	private JLabel timeLabel;
	private FormLocalizationProvider flp;
	private JMenuItem uppercaseMenu;
	private JMenuItem lowercaseMenu;
	private JMenuItem invertMenu;
	
	/**
	 * Pomoćna klasa za upravljanje zatvaranjem 
	 * prozora tekst editora.
	 * @author Valentina Križ
	 *
	 */
	private class WindowAdapterClosing extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			actions.exitAction.actionPerformed(null);
		}
	}
	
	/**
	 * Konstuktor
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapterClosing());
		setSize(900, 500);
		setLocation(300, 200);
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		
		initGUI();
	}
	
	/**
	 * Pomoćna metoda koja postavlja potrebne komponente
	 * editora.
	 */
	private void initGUI() {
		docsModel = new DefaultMultipleDocumentModel();
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		actions = new Actions(this, docsModel, flp);
		actions.createActions();

		infoLabel = new JLabel();
		timeLabel = new JLabel();
		
		uppercaseMenu = new JMenuItem(actions.toUppercase);
		lowercaseMenu = new JMenuItem(actions.toLowercase);
		invertMenu = new JMenuItem(actions.invertCase);
		
		getCurrentInfo();
		setWindowTitle(null);

		createMenus();
		createToolbars();
		
		new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				LocalDateTime time = LocalDateTime.now();
				String text = String.format("%d/%02d/%02d %02d:%02d:%02d", 
								time.getYear(), time.getMonth().getValue(), time.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond());
				timeLabel.setText(text);
			}
        }).start();
		
		cp.add(docsModel, BorderLayout.CENTER);
		
		docsModel.addMultipleDocumentListener(new MultipleDocumentListener () {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				setWindowTitle(docsModel.getCurrentDocument().getFilePath());
				getCurrentInfo();
				docsModel.getCurrentDocument().getTextComponent().addCaretListener(new CaretListener() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void caretUpdate(CaretEvent e) {
						getCurrentInfo();
					}
					
				});
			}
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void documentAdded(SingleDocumentModel model) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				// TODO Auto-generated method stub
				
			}
			
		});

		docsModel.getCurrentDocument().getTextComponent().addCaretListener(f -> getCurrentInfo());
	}
	
	/**
	 * Pomoćna varijabla za postavljanje naslova tekst editora.
	 * 
	 * @param filePath putanja dokumenta koji se trenutno prikazuje
	 */
	private void setWindowTitle(Path filePath) {
		String title = "";
		if(filePath == null) {
			title = "(unnamed) - JNotepad++";
		} else {
			title = filePath.getFileName().toString() + " - JNotepad++";
		}
		
		setTitle(title);
	}
	
	/**
	 * Pomoćna metoda za stvaranje izbornika
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new LJMenu("file", flp);
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(actions.newDocumentAction));
		fileMenu.add(new JMenuItem(actions.openDocumentAction));
		fileMenu.add(new JMenuItem(actions.saveDocumentAction));
		fileMenu.add(new JMenuItem(actions.saveAsDocumentAction));
		fileMenu.add(new JMenuItem(actions.closeDocumentAction));
		fileMenu.add(new JMenuItem(actions.statisticalInfoAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(actions.exitAction));
		
		JMenu editMenu = new LJMenu("edit", flp);
		
		menuBar.add(editMenu);
		
		editMenu.add(new JMenuItem(actions.copySelectedPartAction));
		editMenu.add(new JMenuItem(actions.cutSelectedPartAction));
		editMenu.add(new JMenuItem(actions.pasteSelectedPartAction));
		editMenu.add(new JMenuItem(actions.deleteSelectedPartAction));
		
		JMenu languageMenu = new LJMenu("languages", flp);
		menuBar.add(languageMenu);
		
		languageMenu.add(new JMenuItem(actions.hrLangAction));
		languageMenu.add(new JMenuItem(actions.enLangAction));
		languageMenu.add(new JMenuItem(actions.deLangAction));
		
		JMenu toolsMenu = new LJMenu("tools", flp);
		menuBar.add(toolsMenu);
		
		toolsMenu.add(uppercaseMenu);
		toolsMenu.add(lowercaseMenu);
		toolsMenu.add(invertMenu);
		toolsMenu.add(new JMenuItem(actions.uniqueAction));
		
		JMenu toolsSubmenu = new LJMenu("sort", flp);
		toolsSubmenu.add(new JMenuItem(actions.sortDescending));
		toolsSubmenu.add(new JMenuItem(actions.sortAscending));
		
		toolsMenu.add(toolsSubmenu);
		
		setJMenuBar(menuBar);
	}

	/**
	 * Pomoćna metoda za stvaranje alatne trake.
	 */
	private void createToolbars() {
		JToolBar topToolBar = new JToolBar();
		topToolBar.setFloatable(true);
		
		topToolBar.add(new JButton(actions.newDocumentAction));
		topToolBar.add(new JButton(actions.openDocumentAction));
		topToolBar.add(new JButton(actions.saveDocumentAction));
		topToolBar.add(new JButton(actions.saveAsDocumentAction));
		topToolBar.add(new JButton(actions.closeDocumentAction));
		topToolBar.add(new JButton(actions.statisticalInfoAction));
		topToolBar.addSeparator();
		topToolBar.add(new JButton(actions.copySelectedPartAction));
		topToolBar.add(new JButton(actions.cutSelectedPartAction));
		topToolBar.add(new JButton(actions.pasteSelectedPartAction));
		
		JToolBar bottomToolBar = new JToolBar();
		bottomToolBar.setFloatable(true);
		bottomToolBar.setLayout(new BorderLayout());
		
		bottomToolBar.add(infoLabel, BorderLayout.LINE_START);
		bottomToolBar.add(timeLabel, BorderLayout.LINE_END);
		
		getContentPane().add(topToolBar, BorderLayout.PAGE_START);
		getContentPane().add(bottomToolBar, BorderLayout.PAGE_END);
	}
	
	/**
	 * Pomoćna metoda koja dohvaća trenutne informacije
	 * o dokumentu koje se prikazuju u donjoj traci.
	 */
	private void getCurrentInfo() {
		JTextArea textArea = docsModel.getCurrentDocument().getTextComponent();
		try {
			int line = textArea.getLineOfOffset(textArea.getCaretPosition());
			int column = textArea.getCaretPosition() - textArea.getLineStartOffset(line);
			int selectedLength = textArea.getSelectionEnd() - textArea.getSelectionStart();
			
			String text = String.format("length: %d            Ln: %d Col: %d Sel: %d", 
								textArea.getText().length(), line + 1, column + 1, selectedLength);

			infoLabel.setText(text);
			
			boolean isEnable = selectedLength != 0;
			invertMenu.setEnabled(isEnable);
			uppercaseMenu.setEnabled(isEnable);
			lowercaseMenu.setEnabled(isEnable);
		} catch(BadLocationException ex) {
			
		}		
	}
	
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				LocalizationProvider.getInstance();
				new JNotepadPP().setVisible(true);
			}
		});
	}
}
