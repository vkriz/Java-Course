package hr.fer.zemris.java.hw15.dao;

/**
 * Klasa predstavlja iznimku koja nasljeđuje
 * RuntimeException, a služi za potrebe 
 * sučelja DAO.
 * 
 * @author Valentina Križ
 *
 */
public class DAOException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor 
	 * 
	 * @param message
	 * @param cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor
	 * 
	 * @param message
	 */
	public DAOException(String message) {
		super(message);
	}
}