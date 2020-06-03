package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred implementira sučelje IWebWorker, a
 * služi za određivanje boje u koju treba
 * obojati pozadinu stranice.
 * Ukoliko postoji trajni parametar bgcolor,
 * stranica će biti te boje, a ako ne postoji, 
 * biti će boje #7F7F7F.
 * Nakon određivanja ispravne boje, zahtjev se 
 * propagira skripti /private/pages/home.smscr.
 * 
 * @author Valentina Križ
 *
 */
public class HomeWorker implements IWebWorker {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgcolor = context.getPersistentParameter("bgcolor");
		
		if(bgcolor == null) {
			bgcolor = "7F7F7F";
		}
		
		context.setTemporaryParameter("background", "#" + bgcolor);
		
		context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
	}

}
