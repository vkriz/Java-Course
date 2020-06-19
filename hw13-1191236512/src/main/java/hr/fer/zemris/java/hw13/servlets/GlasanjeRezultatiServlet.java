package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw13.utils.GlasanjeUtils;


/**
 * Servlet dohvaća rezultate
 * glasanja iz datoteke, sortira ih
 * po broju glasova i šalje ih u 
 * glasanjeRez.jsp za ispis.
 * Također šalje pobjedničke pjesme
 * i imena bendova.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeRezultatiServlet", urlPatterns = {"/glasanje-rezultati"})
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
	
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Map<String, Integer> results = GlasanjeUtils.getResults(fileName);
		
		List<Entry<String, Integer>> sorted = new ArrayList<>(results.entrySet());
		sorted.sort(Entry.comparingByValue());
		Collections.reverse(sorted);
		
		int maxVotes = sorted.get(0).getValue();
		
		fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		
		Map<String, String> bands = new HashMap<>();
		Map<String, String> winnerSongs = new HashMap<>();

		for(String line : lines) {
			String[] lineParts = line.split("\t");
			
			if(lineParts.length != 3) {
				System.out.println("nije 3");
				return;
			}
			bands.put(lineParts[0], lineParts[1]);
			
			if(results.get(lineParts[0]).equals(maxVotes)) {
				winnerSongs.put(lineParts[1], lineParts[2]);
			}
		}
		
		req.setAttribute("results", sorted);
		req.setAttribute("bands", bands);
		req.setAttribute("winnerSongs", winnerSongs);
		// Pošalji ih JSP-u...
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}
