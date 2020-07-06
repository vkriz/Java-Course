package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;


/**
 * Servlet dohvaća opcije za koje se može
 * glasati i šalje ih u glasanjeIndex.jsp.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeServlet", urlPatterns = {"/servleti/glasanje/*"})
public class GlasanjeServlet extends HttpServlet {

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
		DAO dao = DAOProvider.getDao();
	
		// dohvati opcije
		long pollId = Long.parseLong(req.getParameter("pollID"));
		
		Poll poll = dao.getPollById(pollId);
		List<PollOption> options = dao.getAllPollOptions(pollId);
		
		req.setAttribute("poll", poll);
		req.setAttribute("options", options);
		
		// Pošalji ih JSP-u...
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
}
