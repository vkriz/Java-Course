package hr.fer.zemris.java.hw14.model;

/**
 * Razred predstavlja jednu
 * anketu za glasanje.
 * 
 * @author Valentina Križ
 *
 */
public class Poll {
	/**
	 * id ankete
	 */
	private long id;
	
	/**
	 * naslov ankete
	 */
	private String title;
	
	/**
	 * opis ankete
	 */
	private String message;
	
	/**
	 * Getter za varijablu id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter za varijablu id
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter za varijablu title
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter za varijablu title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter za varijablu message
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter za varijablu message
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "id: " + id + "; title: " + title + "; message: " + message;
	}
}
