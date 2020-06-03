package hr.fer.zemris.java.webserver;

/**
 * Sučelje koje služi za upravljanje
 * zahtjevima. 
 * 
 * @author Valentina Križ
 *
 */
public interface IDispatcher {
	/**
	 * Metoda prosljeđuje zahtjev
	 * sa zadanim url-om.
	 * 
	 * @param urlPath
	 * @throws Exception
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
