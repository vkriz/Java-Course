package hr.fer.zemris.java.hw15.dao.jpa;

import java.util.List;

import hr.fer.zemris.java.hw15.dao.DAO;
import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Implementacija sučelja DAO tehnologijom JPA, 
 * nudi mogućnosti spremanja i dohvaćanja 
 * podataka iz baze.
 *
 * @author Valentina Križ
 *
 */
public class JPADAOImpl implements DAO {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BlogUser getBlogUserById(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		List<BlogUser> users = JPAEMProvider.getEntityManager()
				.createQuery("SELECT user FROM BlogUser user WHERE user.nick = :nick", BlogUser.class)
				.setParameter("nick", nick)
				.getResultList();
		
		if(users == null || users.size() == 0) {
			return null;
		}
		
		return users.get(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveUser(BlogUser user) throws DAOException {
		JPAEMProvider.getEntityManager().persist(user);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveBlog(BlogEntry blog) throws DAOException {
		JPAEMProvider.getEntityManager().persist(blog);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BlogUser> getAllUsers() throws DAOException {
		return JPAEMProvider.getEntityManager()
				.createQuery("SELECT user FROM BlogUser user", BlogUser.class)
				.getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BlogEntry> getBlogEntriesByUser(BlogUser user) throws DAOException {
		return JPAEMProvider.getEntityManager()
				.createQuery("SELECT blog FROM BlogEntry blog WHERE blog.creator = :user", BlogEntry.class)
				.setParameter("user", user)
				.getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveComment(BlogComment comment) throws DAOException {
		JPAEMProvider.getEntityManager().persist(comment);
	}
}