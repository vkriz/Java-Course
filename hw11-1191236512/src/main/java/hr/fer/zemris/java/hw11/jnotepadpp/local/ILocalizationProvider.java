package hr.fer.zemris.java.hw11.jnotepadpp.local;

public interface ILocalizationProvider {
	abstract String getString(String key);
	void addLocalizationListener(ILocalizationListener l);
	void removeLocalizationListener(ILocalizationListener l);
}
