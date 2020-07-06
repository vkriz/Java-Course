package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Index servlet služi isključivo za redirekciju.
 * Ukoliko korisnik pristupi adresi
 * http://localhost:8080/blog ili
 * http://localhost:8080/blog/index.jsp
 * bit će preusmjeren na 
 * http://localhost:8080/blog/servleti/main.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(urlPatterns= {"/index.jsp", "/"})
public class IndexServlet extends HttpServlet {
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

		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
}
