package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * Razred predstavlja JMenu komponentu
 * koja sadrži i LocalizationListenera pa se
 * tekst u komponenti mijenja ovisno o trenutnom jeziku.
 * 
 * @author Valentina Križ
 *
 */
public class LJMenu extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String key;
	ILocalizationProvider lp;

	/**
	 * Konstruktor 
	 * @param key
	 * @param lp
	 */
	public LJMenu(String key, ILocalizationProvider lp) {
		this.key = key;
		this.lp = lp;

		this.lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				updateText();
			}
			
		});
		
		updateText();
	}
	
	/**
	 * Pomoćna metoda za mijenjanje teksta koji se prikazuje.
	 */
	private void updateText() {
		String translation = lp.getString(key);
		setText(translation);
	}

}
