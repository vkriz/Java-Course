package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
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
 * Servlet za početnu stranicu bloga.
 * Prikazuje formu za login ako trenutno
 * nije logiran korisnik, listu svih registriranih
 * korisnika s linkovima na njihove profile.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {
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
		List<BlogUser> blogUsers = DAOProvider.getDAO().getAllUsers();
		req.setAttribute("blogUsers", blogUsers);
		req.getRequestDispatcher("/WEB-INF/pages/Index.jsp").forward(req, resp);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Map<String, String> errors = new HashMap<>();
		
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");
		
		if(nick == null || nick.isBlank()) {
			errors.put("nick", "Field nick is required.");
		} 
		
		if(password == null || password.isBlank()) {
			errors.put("password", "Field password is required.");
		}
		
		if(errors.isEmpty()) {
			try {
				String passwordHash = Crypto.encryptPassword(password);
				BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);
				
				if(!passwordHash.equals(user.getPasswordHash())) {
					errors.put("invalidLogin", "Invalid username or password.");
				} else {
					req.getSession().setAttribute("current.user.id", user.getId());
					req.getSession().setAttribute("current.user.nick", user.getNick());
					req.getSession().setAttribute("current.user.fn", user.getFirstName());
					req.getSession().setAttribute("current.user.ln", user.getLastName());
				}
			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
			}
		} 

		if(!errors.isEmpty()){
			req.setAttribute("errors", errors);
			req.setAttribute("nick", nick);
			doGet(req, resp);
		} else {
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
		}
	}
}
