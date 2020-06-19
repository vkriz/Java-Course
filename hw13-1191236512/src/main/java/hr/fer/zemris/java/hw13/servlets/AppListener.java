package hr.fer.zemris.java.hw13.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener koji pamti vrijeme
 * pokretanja aplikacije.
 * 
 * @author Valentina Križ
 *
 */
@WebListener
public class AppListener implements ServletContextListener {
	/**
	 * Kontekst
	 */
	private ServletContext context = null;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		context = sce.getServletContext();
		context.setAttribute("startTime", System.currentTimeMillis());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		context = sce.getServletContext();
		context.removeAttribute("startTime");
	}
}