package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet čita bendove za koje se može
 * glasati i šalje ih u glasanjeIndex.jsp.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeServlet", urlPatterns = {"/glasanje"})
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
	
		// Učitaj raspoložive bendove
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

		Map<Integer, String> bands = new HashMap<>();
		
		for(String line : lines) {
			String[] lineParts = line.split("\t");
			if(lineParts.length != 3) {
				return;
			}
			
			try {
				Integer id = Integer.parseInt(lineParts[0]);
				bands.put(id, lineParts[1]);
			} catch(NumberFormatException ex) {
				return;
			}
		}
		
		req.setAttribute("bands", bands);
		
		// Pošalji ih JSP-u...
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
}
