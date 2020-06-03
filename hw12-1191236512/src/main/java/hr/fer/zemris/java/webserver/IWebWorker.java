package hr.fer.zemris.java.webserver;

/**
 * Sučelje nudi mogućnost procesiranja zahtjeva i
 * kreiranja sadržaja za klijenta.
 * 
 * @author Valentina Križ
 *
 */
public interface IWebWorker {
	/**
	 * Metoda prima referencu na RequestContext 
	 * i procesira taj zahtjev.
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void processRequest(RequestContext context) throws Exception;
}
