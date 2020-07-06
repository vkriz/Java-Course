package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet dohvaća rezultate
 * glasanja iz baze, sortira ih
 * po broju glasova i šalje ih u 
 * glasanjeRez.jsp za ispis.
 * Također šalje pobjedničke opcije.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeRezultatiServlet", urlPatterns = {"/servleti/glasanje-rezultati/*"})
public class GlasanjeRezultatiServlet extends HttpServlet {

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

		long pollId = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> options = dao.getAllPollOptions(pollId);
		Poll poll = dao.getPollById(pollId);
		
		List<PollOption> winners = new ArrayList<>();
		
		if(options.size() > 0) {
			long topVotes = options.get(0).getVotesCount();
			
			int index = 0;
			while(options.get(index).getVotesCount() == topVotes) {
				winners.add(options.get(index));
				++index;
			}
		}
		
		
		req.setAttribute("results", options);
		req.setAttribute("winners", winners);
		req.setAttribute("poll", poll);

		// Pošalji ih JSP-u...
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}
