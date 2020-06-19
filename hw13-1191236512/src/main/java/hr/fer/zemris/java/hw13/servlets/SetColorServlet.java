package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet postavlja vrijednost session atributa 
 * pickedBgCol na primljenu vrijednost ili na
 * white ako nije poslana niti jedna boja.
 * Nasljeđje klasu HttpServlet.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "setColorServlet", urlPatterns = {"/setcolor"})
public class SetColorServlet extends HttpServlet {

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
		String color = req.getParameter("color");
		
		req.getSession().setAttribute("pickedBgCol", color == null ? "white" : color);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}
