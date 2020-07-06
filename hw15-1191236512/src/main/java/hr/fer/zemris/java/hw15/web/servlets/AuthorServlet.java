package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Servlet nudi više opcija ovisno
 * o url-u preko kojeg mu se pristupa:
 * 
 * 1. /servleti/author/NICK:
 * 		prikazuje sve blogove korisnika NICK
 * 		ako je ulogiran korisnik NICK, prikazuje link
 * 			na dodavanje novog bloga

 * 2. /servleti/author/NICK/ID
 * 		prikazuje blog korisnika NICK s id-jem ID (sve podatke
 * 			o blogu, komentare i formu za ostavljanje komentara)
 * 		ako korisnik nema blog s tim id-jem prikazuje grešku
 * 
 * 3. /servleti/author/NICK/new
 * 		prikazuje formu za dodavanje novog bloga ako
 *			je trenutno logiran korisnik NICK
 *
 * 4. /servleti/author/NICK/ID/edit
 * 		prikazuje formu za uređivanje bloga ako je
 * 			trenutno logiran korisnik NICK i ima blog
 * 			s id-jem ID
 * 
 * @author Valentina Križ
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {
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
		
		String pathInfo = req.getPathInfo();
		if(pathInfo != null && !pathInfo.isBlank() && !pathInfo.equals("/")) {
			String[] infoParts = pathInfo.substring(1).split("/");
			
			boolean allowedPage = false;
			// provjeri da li je logiran korisnik s istim nickom
			if(infoParts.length > 0) {
				if(infoParts[0].equals(req.getSession().getAttribute("current.user.nick"))) {
					allowedPage = true;
				}
			}
			
			// ako je /author/NICK
			if(infoParts.length == 1) {
				BlogUser user = null;
				try {
					user = DAOProvider.getDAO().getBlogUserByNick(infoParts[0]);
				} catch(Exception ignorable) {
				}
				
				if(user == null) {
					req.setAttribute("error", "No user with given nick.");
					req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
				} else {
					req.setAttribute("user", user); 
					
					List<BlogEntry> blogs = null;
					try {
						blogs = DAOProvider.getDAO().getBlogEntriesByUser(user);
					} catch(Exception ignorable) {
					}
					
					req.setAttribute("blogs", blogs);
					if(allowedPage) {
						req.setAttribute("allowNewPost", true);
					}
					req.getRequestDispatcher("/WEB-INF/pages/Author.jsp").forward(req, resp);
				}
			}
			
			// ako je /author/NICK/BLOG_ID ili /author/NICK/new
			if(infoParts.length == 2) {
				// /author/NICK/new
				if(infoParts[1].equals("new")) {
					if(!allowedPage) {
						req.setAttribute("error", "Forbidden page requested.");
						req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
					} else {
						req.setAttribute("action", "new");
						req.getRequestDispatcher("/WEB-INF/pages/EditBlog.jsp").forward(req, resp);
					}
				} else {
					// /author/NICK/BLOG_ID
					BlogEntry blog = null;
					try {
						blog = DAOProvider.getDAO().getBlogEntry(Long.parseLong(infoParts[1]));
					} catch(Exception ignorable) {
					}
					
					
					if(blog != null && blog.getCreator().getNick().equals(infoParts[0])) {
						req.setAttribute("blogEntry", blog);
						if(allowedPage) {
							req.setAttribute("allowEditPost", true);
						}
						
						Object userId = req.getSession().getAttribute("current.user.id");
						if(userId != null) {
							try {
								BlogUser user = DAOProvider.getDAO().getBlogUserById(Long.valueOf(userId.toString()));
								if(user != null) {
									req.setAttribute("email", user.getEmail());
								}
							} catch(Exception ignorable) {
							}
						}
						
						req.getRequestDispatcher("/WEB-INF/pages/Blog.jsp").forward(req, resp);
					} else {
						req.setAttribute("error", "Given author has no blog with given id.");
						req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
					}
				}
			}
			
			// ako je /author/NICK/BLOG_ID/edit
			if(infoParts.length == 3) {
				if(infoParts[2].equals("edit")) {
					if(!allowedPage) {
						req.setAttribute("error", "Forbidden page requested.");
						req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
					} else {
						try {
							BlogEntry blog = DAOProvider.getDAO().getBlogEntry(Long.parseLong(infoParts[1]));
							
							if(blog != null && blog.getCreator().getNick().equals(infoParts[0])) {
								req.setAttribute("blog", blog);
								req.setAttribute("editing", true);
								req.setAttribute("action", "edit");
								
								req.getRequestDispatcher("/WEB-INF/pages/EditBlog.jsp").forward(req, resp);
							} else {
								req.setAttribute("error", "Given author has no blog with given id.");
								req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
							}
						} catch(Exception ignorable) {
						}
					}
				} else {
					req.setAttribute("error", "Invalid page url.");
					req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
				}
			}
		} else {
			req.getRequestDispatcher("/servleti/main").forward(req, resp);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String pathInfo = req.getPathInfo();
		if(pathInfo != null && !pathInfo.isBlank() && !pathInfo.equals("/")) {
			String[] infoParts = pathInfo.substring(1).split("/");
			Map<String, String> errors = new HashMap<>();
			
			if(infoParts.length == 2) {
				// ako je /author/NICK/new
				if(infoParts[1].equals("new")) {
					BlogUser user = DAOProvider.getDAO().getBlogUserByNick(infoParts[0]);
					BlogEntry blog = new BlogEntry();
					blog.setComments(new ArrayList<>());
					blog.setCreator(user);
					
					String text = req.getParameter("text");
					String title = req.getParameter("title");
					
					if(title == null || title.isBlank()) {
						errors.put("title", "Field title is required.");
					} else {
						blog.setTitle(title);
					}
					
					if(text == null || text.isBlank()) {
						errors.put("text", "Field text is required.");
					} else {
						blog.setText(text);
					}
					
					if(!errors.isEmpty()) {
						req.setAttribute("errors", errors);
						req.setAttribute("text", text);
						req.setAttribute("title", title);
						doGet(req, resp);
						return;
					}
					
					Date now = new Date();
					blog.setCreatedAt(now);
					blog.setLastModifiedAt(now);
					
					DAOProvider.getDAO().saveBlog(blog);

					resp.sendRedirect(req.getContextPath() + "/servleti/author/" + infoParts[0]);
				} else {
					// ako je /author/NICK/BLOG_ID -> komentiranje
					Long id = null;
					try {
						id = Long.valueOf(infoParts[1]);
					} catch(Exception ignorable) {
					}
					if(id!=null) {
						BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
						if(blogEntry!=null) {
							BlogComment comment = new BlogComment();
							comment.setBlogEntry(blogEntry);
							
							String email = req.getParameter("email");
							String text = req.getParameter("text");
							
							if(email == null || email.isBlank()) {
								errors.put("email", "Field email is required.");
							} else {
								int l = email.length();
								int p = email.indexOf('@');
								if(l < 3 || p == -1 || p == 0 || p == l-1) {
									System.out.println("error");
									errors.put("email", "Wrong email format.");
								}
								
								comment.setUsersEMail(email);
							}
							
							if(text == null || text.isBlank()) {
								errors.put("text", "Field text is required.");
							} else {
								comment.setMessage(text);
							}
							
							if(!errors.isEmpty()) {
								req.setAttribute("errors", errors);
								req.setAttribute("email", email);
								req.setAttribute("text", text);
								doGet(req, resp);
								return;
							}
							Date now = new Date();
							comment.setPostedOn(now);
							
							DAOProvider.getDAO().saveComment(comment);
							
							resp.sendRedirect(req.getContextPath() + "/servleti/author/" + infoParts[0] + "/" + blogEntry.getId());
						}
					}
				}
			}
			
			// ako je /author/NICK/BLOG_ID/edit
			if(infoParts.length == 3) {
				if(infoParts[2].equals("edit")) {
					BlogEntry blog = null;
					try {
						blog = DAOProvider.getDAO().getBlogEntry(Long.parseLong(infoParts[1]));
					} catch(Exception ignorable) {
					}
					
					if(blog != null) {
						String text = req.getParameter("text");
						String title = req.getParameter("title");
						
						if(title == null || title.isBlank()) {
							errors.put("title", "Field title is required.");
						} else {
							blog.setTitle(title);
						}
						
						if(text == null || text.isBlank()) {
							errors.put("text", "Field text is required.");
						} else {
							blog.setText(text);
						}
						
						if(!errors.isEmpty()) {
							req.setAttribute("errors", errors);
							req.setAttribute("text", text);
							req.setAttribute("title", title);
							doGet(req, resp);
							return;
						}
						
						blog.setLastModifiedAt(new Date());
						
						DAOProvider.getDAO().saveBlog(blog);
					}
					
					resp.sendRedirect(req.getContextPath() + "/servleti/author/" + infoParts[0] + "/" + blog.getId());
				}
			}
		}
	}
}
