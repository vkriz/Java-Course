package hr.fer.zemris.java.hw13.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet dodaje jedan glas
 * odabranom bendu i sprema ažurirane
 * glasove u datoteku glasanje-rezultati.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeGlasajServlet", urlPatterns = {"/glasanje-glasaj"})
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
		String bandId = req.getParameter("id");
	
		// Zabiljezi glas...
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		// Napravi datoteku ako je potrebno; ažuriraj podatke koji su u njoj...
		if(!Files.exists(Paths.get(fileName))) {
			new File(fileName);
		}
		
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		
		boolean bandExists = false;
		
		for(int i = 0, numLines = lines.size(); i < numLines; ++i) {
			String[] lineParts = lines.get(i).split("\t");
			
			if(lineParts.length != 2) {
				return;
			}
			
			if(lineParts[0].equals(bandId)) {
				try {
					int votes = Integer.parseInt(lineParts[1]) + 1;
					lines.set(i, lineParts[0] + "\t" + votes);
					Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8);
					bandExists = true;
					break;
				} catch(NumberFormatException ex) {
					return;
				}
			}
		}
		
		if(!bandExists) {
			lines.add(bandId + "\t" + 1);
			Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8);
		}
		
		// Kad je gotovo, pošalji redirect pregledniku I dalje NE generiraj odgovor
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
}
