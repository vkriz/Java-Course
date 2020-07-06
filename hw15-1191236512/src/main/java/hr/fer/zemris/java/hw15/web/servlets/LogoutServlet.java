package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet za logout. Briše atribute
 * sesije u kojoj su zapamčeni podaci o
 * logiranom korisniku i preusmjerava na početnu
 * stranicu bloga.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet("/servleti/logout")
public class LogoutServlet extends HttpServlet {
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
		req.getSession().removeAttribute("current.user.id");
		req.getSession().removeAttribute("current.user.fn");
		req.getSession().removeAttribute("current.user.ln");
		req.getSession().removeAttribute("current.user.nick");
		
		req.getRequestDispatcher(req.getContextPath() + "/servleti/main").forward(req, resp);
	}
}
