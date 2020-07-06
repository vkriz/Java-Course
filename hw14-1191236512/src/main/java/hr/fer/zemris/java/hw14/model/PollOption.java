package hr.fer.zemris.java.hw14.model;

/**
 * Razred predstavlja jednu opciju
 * u glasanju.
 * 
 * @author Valentina Križ
 *
 */
public class PollOption {
	/**
	 * id opcije
	 */
	private long id;
	
	/**
	 * naslov opcije
	 */
	private String optionTitle;
	
	/**
	 * link opcije
	 */
	private String optionLink;
	
	/**
	 * anketa u kojoj je opcija
	 */
	private long pollId;
	
	/**
	 * broj glasova za tu opciju
	 */
	private long votesCount;

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
	 * Getter za varijablu optionTitle
	 * 
	 * @return optionTitle
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * Setter za varijablu optionTitle
	 * 
	 * @param optionTitle
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * Getter za varijablu optionLink
	 * 
	 * @return optionLink
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * Setter za varijablu optionLink
	 * 
	 * @param optionLink
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * Getter za varijablu pollId
	 * 
	 * @return pollId
	 */
	public long getPollId() {
		return pollId;
	}

	/**
	 * Setter za varijablu pollId
	 * 
	 * @param pollId
	 */
	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	/**
	 * Getter za varijablu votesCount
	 * 
	 * @return votesCount
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * Setter za varijablu votesCount
	 * 
	 * @param votesCount
	 */
	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}
}