package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.Poll;

/**
 * Servlet dohvaća informacije o svim
 * dostupnim anketama iz baze i prikazuje
 * ih korisniku.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "PollsIndexServlet", urlPatterns = {"/servleti/index.html"})
public class PollsIndex extends HttpServlet {

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
		List<Poll> polls = DAOProvider.getDao().getAllPolls();
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter writer = resp.getWriter();
		
		writer.write("<h1>Dostupne ankete:</h1>");
		
		for(Poll poll : polls) {
			writer.write("<a href=\"glasanje?pollID=" + poll.getId() + "\">"
							+ poll.getTitle() + "</a><br>");	
		}
	}
}
