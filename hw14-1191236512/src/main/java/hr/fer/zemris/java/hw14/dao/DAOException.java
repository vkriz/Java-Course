package hr.fer.zemris.java.hw14.dao;

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
	 */
	public DAOException() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DAOException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

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

	/**
	 * Konstruktor
	 * 
	 * @param cause
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}