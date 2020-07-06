package hr.fer.zemris.java.hw14.dao;

import java.util.List;

import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author Valentina Križ
 *
 */
public interface DAO {
	/**
	 * Metoda provjerava postoje li u bazi tablica
	 * sa zadanim imenom. Ako ne postoji, izvršava se
	 * predana SQL naredba za stvaranje tablice.
	 * 
	 * @param tableName
	 * @param createStatement
	 */
	public void checkAndCreateTable(String tableName, String createStatement);
	
	/**
	 * Metoda dohvaća iz baze sve
	 * ankete i vraća ih u obliku liste
	 * objekata klase Poll.
	 * 
	 * @return lista anketa
	 * @throws DAOException
	 */
	public List<Poll> getAllPolls() throws DAOException;

	/**
	 * Metoda prima referencu na varijablu
	 * tipa Poll i u bazu sprema anketu
	 * sa odgovarajućim podacima.
	 * Vraća id ankete.
	 * 
	 * @param poll
	 * @return id ankete
	 */
	public long insertPoll(Poll poll);

	/**
	 * Metoda dohvaća iz baze sve opcije
	 * u anketi sa zadanim ID-jem i vraća
	 * ih u obliku liste objekata klase
	 * PollOption.
	 * 
	 * @param pollId
	 * @return lista opcija
	 * @throws DAOException
	 */
	public List<PollOption> getAllPollOptions(long pollId) throws DAOException;

	/**
	 * Metoda prima referencu na varijablu
	 * tipa PollOption i u bazu sprema opciju
	 * za anketu sa odgovarajućim podacima.
	 * 
	 * @param option
	 */
	public void insertPollOption(PollOption option);

	/**
	 * Metoda dohvaća iz baze
	 * anketu sa zadanim ID-jem i vraća
	 * ju u obliku objekta klase Poll.
	 * 
	 * @param pollId
	 * @return anketa
	 */
	public Poll getPollById(long pollId);

	/**
	 * Metoda dohvaća iz baze opciju
	 * za anketu sa zadanim ID-jem i vraća
	 * ju u obliku objekta klase PollOption.
	 * 
	 * @param optionId
	 * @return
	 */
	public PollOption getPollOptionById(long optionId);

	/**
	 * Metoda dodaje jedan glas zadanoj
	 * opciji za anketu i sprema ažurirane
	 * podatke u bazu.
	 * Vraća ID ankete u kojoj se opcija
	 * nalazi.
	 * 
	 * @param optionId
	 * @return ID ankete
	 */
	public long addVoteToPollOptionById(long optionId);
}