package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Razred predstavlja akcije kojima se mijenja
 * ime ovisno o trenutnom jeziku.
 * 
 * @author Valentina Križ
 *
 */
public class LocalizableAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private ILocalizationProvider lp;
	
	/**
	 * Konstuktor
	 * @param key
	 * @param lp
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = key;
		this.lp = lp;

		this.lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				updateName();
			}
			
		});
		
		updateName();
	}
	
	/**
	 * Pomoćna metoda za promjenu imena
	 */
	private void updateName() {
		String translation = lp.getString(key);
		putValue(NAME, translation);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
