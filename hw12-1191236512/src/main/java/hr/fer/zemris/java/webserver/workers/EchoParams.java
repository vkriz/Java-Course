package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred implementira sučelje IWebWorker,
 * a zahtjev obrađuje tako da kreira HTML
 * stranicu tablicom u kojoj ispisuje sve
 * parametre (ime i vrijednost).
 * 
 * @author Valentina Križ
 *
 */
public class EchoParams implements IWebWorker {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		
		try {
			context.write("<html><body><table>"
					+ "<thead>\r\n" + 
					" <tr><th>Name</th><th>Value</th></tr>\r\n" + 
					" </thead>");
			for(String name : context.getParameterNames()) {
				context.write("<tr><td>" + name + "</td><td>" 
						+ context.getParameter(name) + "</td></tr>");
			}
			context.write("</table></body></html>");
			
		} catch(IOException ex) {
			// Log exception to servers log...
			ex.printStackTrace();
		}
	}

}
