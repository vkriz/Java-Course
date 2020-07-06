package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.crypto.Crypto;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Servlet služi za registraciju novih
 * korisnika na blog.
 * Formular za registraciju se prikazuje na
 * Register.jsp, a potrebno je unesti
 * ime, prezime, nadimak (jedinstveni), email
 * adresu i lozinku.
 * Ukoliko se unese nadimak koji se već koristi,
 * dojavljuje se prigodna greška. Također, ako se
 * ne unese jedan od podataka.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(urlPatterns={"/servleti/register"})
public class RegisterServlet extends HttpServlet {
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
		req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, String> errors = new HashMap<>();
		BlogUser user = new BlogUser();
				
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");
		
		if(firstName == null || firstName.isBlank()) {
			errors.put("firstName", "Field first name is required.");
		} else {
			user.setFirstName(firstName);
		}
		
		if(lastName == null || lastName.isBlank()) {
			errors.put("lastName", "Field last name is required.");
		} else {
			user.setLastName(lastName);
		}
		
		if(email == null || email.isBlank()) {
			errors.put("email", "Field email is required.");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l < 3 || p == -1 || p == 0 || p == l-1) {
				System.out.println("error");
				errors.put("email", "Wrong email format.");
			}
			
			user.setEmail(email);
		}
		
		if(password == null || password.isBlank()) {
			errors.put("password", "Field password is required.");
		} else {
			try {
				user.setPasswordHash(Crypto.encryptPassword(password));
			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
			}
		}
		
		if(nick == null || nick.isBlank()) {
			errors.put("nick", "Field nick is required.");
		} else if(DAOProvider.getDAO().getBlogUserByNick(nick) != null) {
			errors.put("nick", "Nick already in use.");
		} else {
			user.setNick(nick);
		}
		
		if(!errors.isEmpty()) {
			req.setAttribute("errors", errors);
			req.setAttribute("firstName", firstName);
			req.setAttribute("lastName", lastName);
			req.setAttribute("email", email);
			req.setAttribute("password", password);
			req.setAttribute("nick", nick);
			doGet(req, resp);
			return;
		}
		
		user.setBlogs(new ArrayList<>());
		
		DAOProvider.getDAO().saveUser(user);
		req.getRequestDispatcher("/servleti/main").forward(req, resp);
	}
}