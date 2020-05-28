package hr.fer.zemris.java.hw11.jnotepadpp.local;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	private boolean connected;
	private ILocalizationProvider provider;
    private ILocalizationListener listener;
	
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = provider;
		listener = new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				fire();
			}
			
		};
	}
	
	@Override
	public String getString(String key) {
		return provider.getString(key);
	}
	
	public void connect() {
		if(!connected) {
			provider.addLocalizationListener(listener);
			connected = true;
		}
	}
	
	public void disconnect() {
		if(connected) {
			provider.removeLocalizationListener(listener);
			connected = false;
		}
	}
}
