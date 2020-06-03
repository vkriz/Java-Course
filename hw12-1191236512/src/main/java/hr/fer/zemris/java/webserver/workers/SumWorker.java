package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred implementira sučelje IWebWorker,
 * a zahtjev obrađuje tako da kreira HTML
 * stranicu s tablicom u kojoj su ispisani
 * privremeni parametri a i b te njihov broj.
 * Ispod tablice se iscrtava jedna od dvije slike,
 * ovisno o parnosti zbroja.
 * 
 * @author Valentina Križ
 *
 */
public class SumWorker implements IWebWorker {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		
		// default vrijednosti ako korisnik nije unesao parametar
		// ili ako nije int
		int a = 1; 
		int b = 2;
		try {
			a = Integer.parseInt(context.getParameter("a"));
		} catch(NumberFormatException ex) {
			
		}
		
		try {
			b = Integer.parseInt(context.getParameter("b"));
		} catch(NumberFormatException ex) {
			
		}

		context.setTemporaryParameter("zbroj", String.valueOf(a + b));
		context.setTemporaryParameter("varA", String.valueOf(a));
		context.setTemporaryParameter("varB", String.valueOf(b));
		
		if((a + b) % 2 == 0) {
			context.setTemporaryParameter("imgName", "/images/tree1.jpg");
		} else {
			context.setTemporaryParameter("imgName", "/images/tree2.png");
		}
		
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
	}

}
