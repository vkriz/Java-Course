package hr.fer.zemris.java.hw15.dao;

import java.util.List;

import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author Valentina Križ
 *
 */
public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Dohvaća usera sa zadani nick-om. Ako takav ne postoji,
	 * vraća null.
	 * 
	 * @param nick
	 * @return user ili null
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogUser getBlogUserByNick(String nick) throws DAOException;

	/**
	 * Sprema novog korisnika u bazu.
	 * Pretpostavlja se da su podaci o
	 * korisniku već validirani.
	 * 
	 * @param user novi korisnik
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public void saveUser(BlogUser user) throws DAOException;

	/**
	 * Dohvaća listu svih korisnika bloga
	 * iz baze.
	 * 
	 * @return lista korisnika
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public List<BlogUser> getAllUsers() throws DAOException;

	/**
	 * Sprema ažurirani blog entry u bazu.
	 * 
	 * @param blog
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public void saveBlog(BlogEntry blog) throws DAOException;

	/**
	 * Dohvaća listu svih blogova korisnika
	 * sa zadanim nick-om.
	 * 
	 * @param user
	 * @return lista blogova
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public List<BlogEntry> getBlogEntriesByUser(BlogUser user) throws DAOException;

	/**
	 * Sprema komentar u bazu.
	 * 
	 * @param comment
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public void saveComment(BlogComment comment) throws DAOException;

	/**
	 * Dohvaća korisnika iz baze sa zadanim
	 * id-jem. Ako takav korisnik ne postoji, vraća
	 * null.
	 * 
	 * @param id
	 * @return korisnik ili null ako korisnik ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogUser getBlogUserById(Long id) throws DAOException;	
}