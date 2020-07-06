package hr.fer.zemris.java.hw14;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.dao.sql.SQLConnectionProvider;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Klasa koja implementira sučelje ServletContextListener,
 * a služi za slušanje promjena o ServetContext-u.
 * Dodatno, pri pokretanju aplikacije stvara
 * i puni potrebne baze u tablici ako ne postoje
 * ili su prazne.
 * 
 * @author Valentina Križ
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String fileName = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");
		Properties props = new Properties();
		
		try {
			InputStream is = new FileInputStream(fileName);
			props.load(is);
		} catch (IOException e) {
			throw new RuntimeException("Pogreška prilikom čitanja datoteke dbsettings.properties.");
		}
		
		if(props.getProperty("host") == null || props.getProperty("port") == null ||
				props.getProperty("name") == null || props.getProperty("user") == null ||
				props.getProperty("password") == null) {
			throw new RuntimeException("Nedostaju potrebni podaci u datoteci dbsettings.properties.");
		}
		
		String connectionURL = "jdbc:derby://" + props.getProperty("host") + ":" 
								+ props.getProperty("port") + "/" 
								+ props.getProperty("name") 
								+ ";user=" + props.getProperty("user") 
								+ ";password=" + props.getProperty("password");

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
		
		Connection con = null;
		try {
			con = cpds.getConnection();
			SQLConnectionProvider.setConnection(con);
			
			DAO dao = DAOProvider.getDao();
			dao.checkAndCreateTable("Polls", 
					"CREATE TABLE Polls " 
					+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," 
					+ "title VARCHAR(150) NOT NULL," 
					+ "message CLOB(2048) NOT NULL)");
			
			dao.checkAndCreateTable("PollOptions", 
					"CREATE TABLE PollOptions " 
					+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
					+ "optionTitle VARCHAR(100) NOT NULL,"
					+ "optionLink VARCHAR(150) NOT NULL,"
					+ "pollID BIGINT,"
					+ "votesCount BIGINT,"
					+ "FOREIGN KEY (pollID) REFERENCES Polls(id))");
			
			int numPolls = dao.getAllPolls().size();
			
			if(numPolls == 0) {
				createPoll1();
				createPoll2();
			} else if(numPolls == 1) {
				createPoll2();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Pomoćna metoda za dodavanje
	 * prve ankete i njezine opcije
	 * u bazu. Radi se o anketi za
	 * omiljeni bend.
	 */
	public void createPoll1() {
		DAO dao = DAOProvider.getDao();
		long pollId = 1;

		Poll poll = new Poll();
		poll.setTitle("Glasanje za omiljeni bend:");
		poll.setMessage("Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!");
		pollId = dao.insertPoll(poll);
		
		if(dao.getAllPollOptions(pollId).size() == 0) {
			PollOption option = new PollOption();
			option.setPollId(pollId);
			
			option.setOptionTitle("The Beatles");
			option.setOptionLink("https://www.youtube.com/watch?v=z9ypq6_5bsg");
			option.setVotesCount(150);
			dao.insertPollOption(option);
			
			option.setOptionTitle("The Platters");
			option.setOptionLink("https://www.youtube.com/watch?v=H2di83WAOhU");
			option.setVotesCount(60);
			dao.insertPollOption(option);
			
			option.setOptionTitle("The Beach Boys");
			option.setOptionLink("https://www.youtube.com/watch?v=2s4slliAtQU");
			option.setVotesCount(150);
			dao.insertPollOption(option);
			
			option.setOptionTitle("The Four Seasons");
			option.setOptionLink("https://www.youtube.com/watch?v=y8yvnqHmFds");
			option.setVotesCount(20);
			dao.insertPollOption(option);
			
			option.setOptionTitle("The Marcels");
			option.setOptionLink("https://www.youtube.com/watch?v=qoi3TH59ZEs");
			option.setVotesCount(33);
			dao.insertPollOption(option);
			
			option.setOptionTitle("The Everly Brothers");
			option.setOptionLink("https://www.youtube.com/watch?v=tbU3zdAgiX8");
			option.setVotesCount(25);
			dao.insertPollOption(option);
			
			option.setOptionTitle("The Mamas And The Papas");
			option.setOptionLink("https://www.youtube.com/watch?v=N-aK6JnyFmk");
			option.setVotesCount(20);
			dao.insertPollOption(option);
		}
	}
	
	/**
	 * Pomoćna metoda za dodavanje
	 * druge ankete i njezine opcije
	 * u bazu. Radi se o anketi za
	 * omiljeni OS.
	 */
	public void createPoll2() {
		DAO dao = DAOProvider.getDao();
		long pollId = 2;

		Poll poll = new Poll();
		poll.setTitle("Glasanje za omiljeni OS:");
		poll.setMessage("Od sljedećih OS-ova, koji Vam je OS najdraži? Kliknite na link kako biste glasali!");
		pollId = dao.insertPoll(poll);
		
		if(dao.getAllPollOptions(pollId).size() == 0) {
			PollOption option = new PollOption();
			option.setPollId(pollId);
			
			option.setOptionTitle("Android");
			option.setOptionLink("https://www.android.com/");
			option.setVotesCount(38);
			dao.insertPollOption(option);
			
			option.setOptionTitle("Windows");
			option.setOptionLink("https://www.microsoft.com/en-us/windows/");
			option.setVotesCount(35);
			dao.insertPollOption(option);
			
			option.setOptionTitle("iOS");
			option.setOptionLink("https://www.apple.com/ios/ios-13/");
			option.setVotesCount(15);
			dao.insertPollOption(option);
			
			option.setOptionTitle("OS X");
			option.setOptionLink("https://www.apple.com/macos/catalina/");
			option.setVotesCount(9);
			dao.insertPollOption(option);
			
			option.setOptionTitle("Linux");
			option.setOptionLink("https://www.linux.org/");
			option.setVotesCount(12);
			dao.insertPollOption(option);
		}
	}
}