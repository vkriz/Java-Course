package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;


/**
 * Servlet dodaje jedan glas
 * odabranoj opciji i sprema ažurirane
 * glasove u bazu.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeGlasajServlet", urlPatterns = {"/servleti/glasanje-glasaj"})
public class GlasanjeGlasajServlet extends HttpServlet {

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
		long optionId = Long.parseLong(req.getParameter("id"));
		long pollId = DAOProvider.getDao().addVoteToPollOptionById(optionId);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollId);
	}
}
