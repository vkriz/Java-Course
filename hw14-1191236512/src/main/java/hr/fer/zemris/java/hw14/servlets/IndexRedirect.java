package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet preusmjerava zahtjev
 * na /servleti/index.html
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "IndexRedirectServlet", urlPatterns = {"/index.html"})
public class IndexRedirect extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {	
		resp.sendRedirect(req.getContextPath() + "/servleti/index.html");
	}
}
