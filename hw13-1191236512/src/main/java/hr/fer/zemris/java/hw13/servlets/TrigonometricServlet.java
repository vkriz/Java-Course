package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet računa vrijednosti sin(x)
 * i cos(x) za sve vrijednosti x između
 * vrijednosti parametra a i b.
 * Zahtjev s dodanim argumentima se prosljeđuje
 * trigonometric.jsp za ispis.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "trigonometricServlet", urlPatterns = {"/trigonometric"})
public class TrigonometricServlet extends HttpServlet {

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
		int a, b;
		
		try {
			a = Integer.parseInt(req.getParameter("a"));
		} catch(NumberFormatException ex) {
			a = 0;
		}
		
		try {
			b = Integer.parseInt(req.getParameter("b"));
		} catch(NumberFormatException ex) {
			b = 360;
		}
		
		if(a > b) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		b = Math.min(b, a + 720);
		
		List<Integer> xValues = new ArrayList<>();
		List<Double> sinValues = new ArrayList<>();
		List<Double> cosValues = new ArrayList<>();
		
		for(int x = a; x <= b; ++x) {
			xValues.add(x);
			sinValues.add(Math.sin(Math.toRadians(x)));
			cosValues.add(Math.cos(Math.toRadians(x)));
		}
		
		req.setAttribute("xValues", xValues);
		req.setAttribute("sinValues", sinValues);
		req.setAttribute("cosValues", cosValues);
		req.getRequestDispatcher("WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
}
