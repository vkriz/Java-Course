package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred implementira sučelje IWebWorker, a
 * služi za pamćenje boje pozadine u 
 * trajni parametar bgcolor. Worker kreira
 * HTML dokument s porukom o promjeni boje ili
 * o grešci.
 * 
 * @author Valentina Križ
 *
 */
public class BgColorWorker implements IWebWorker {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		
		String bgcolor = context.getParameter("bgcolor");
		
		if(bgcolor != null && isColor(bgcolor)) {
			context.setPersistentParameter("bgcolor", bgcolor);
			context.write("<html><body> Color is updated! <br>");				
		} else {
			context.write("<html><body> Color is NOT updated! <br>");
		}
		context.write("<a href=\"/index2.html\">Home</a>");
	}

	/**
	 * Pomoćna metoda koja prima string
	 * i vraća true ako je to ispravan RGB
	 * kod boje (6 heksadecimalnih znamenki).
	 * 
	 * @param bgcolor
	 * @return true ako je ispravan kod boje,
	 * 			false inače
	 */
	private boolean isColor(String bgcolor) {
		// 6 znamenki
		if(bgcolor.length() != 6) {
			return false;
		}
		
		// sve su heksadecimalne
		try {
			Long.parseLong(bgcolor, 16);
		} catch(NumberFormatException ex) {
			return false;
		}
		
		return true;
	}

}
