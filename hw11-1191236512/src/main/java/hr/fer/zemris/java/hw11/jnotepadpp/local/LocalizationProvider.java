package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LocalizationProvider extends AbstractLocalizationProvider {
    private static LocalizationProvider instance = new LocalizationProvider();
    private static String language;
    private static ResourceBundle bundle;

	private LocalizationProvider() {
		language = "en";
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.prijevodi", locale);
	}
	
	public static LocalizationProvider getInstance(){
		if(instance == null) {
			instance = new LocalizationProvider();
		}
		return instance;
    }
	
	public void setLanguage(String lang) {
		language = lang;
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.prijevodi", locale);
		fire();
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	public String getLanguage() {
		return language;
	}

}
