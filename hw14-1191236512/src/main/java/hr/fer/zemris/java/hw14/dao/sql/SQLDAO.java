package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author Valentina Križ
 */
public class SQLDAO implements DAO {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkAndCreateTable(String tableName, String createStatement) {
		DatabaseMetaData dmd;
		Connection con = SQLConnectionProvider.getConnection();
	
		try {
			dmd = con.getMetaData();
			ResultSet rs = dmd.getTables(null, con.getSchema(), tableName.toUpperCase(), null);
			// ako ne postoji napravi tablicu
		    if (!rs.next()) {
		    	con.prepareStatement(createStatement).execute();
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Poll> getAllPolls() throws DAOException {
		List<Poll> polls = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, title, message from Polls");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						Poll poll = new Poll();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
						polls.add(poll);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste anketa.", ex);
		}
		return polls;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PollOption> getAllPollOptions(long pollId) throws DAOException {
		List<PollOption> options = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, optionTitle, optionLink, votesCount from PollOptions where pollId = ? order by votesCount desc");
			pst.setLong(1, pollId);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						PollOption option = new PollOption();
						option.setId(rs.getLong(1));
						option.setOptionTitle(rs.getString(2));
						option.setOptionLink(rs.getString(3));
						option.setVotesCount(rs.getLong(4));
						options.add(option);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste opcija ankete.", ex);
		}
		return options;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long insertPoll(Poll poll) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		long newId = -1;
		
		try {
			pst = con.prepareStatement(
				"INSERT INTO Polls (title, message) values (?,?)", 
				Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, poll.getTitle());
			pst.setString(2, poll.getMessage());

			pst.executeUpdate();
			ResultSet rset = pst.getGeneratedKeys();
			
			try {
				if(rset != null && rset.next()) {
					newId = rset.getLong(1); 
				}
			} finally {
				try { rset.close(); } catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		return newId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertPollOption(PollOption option) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement(
				"INSERT INTO PollOptions (optionTitle, optionLink, pollId, votesCount) values (?,?,?,?)");
			pst.setString(1, option.getOptionTitle());
			pst.setString(2, option.getOptionLink());
			pst.setLong(3, option.getPollId());
			pst.setLong(4, option.getVotesCount());
			pst.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Poll getPollById(long pollId) {
		Poll poll = new Poll();
		
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement(
				"SELECT * FROM Polls WHERE id = ?");
			pst.setLong(1, pollId);
			
			ResultSet rset = pst.executeQuery();
			try {
				while(rset.next()) {
					poll.setId(rset.getLong(1));
					poll.setTitle(rset.getString(2));
					poll.setMessage(rset.getString(3));
				}
			} finally {
				try { rset.close(); } catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		return poll;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PollOption getPollOptionById(long optionId) {
		PollOption option = new PollOption();
		
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement(
				"SELECT * FROM PollOptions WHERE id = ?");
			pst.setLong(1, optionId);
			
			ResultSet rset = pst.executeQuery();
			try {
				while(rset.next()) {
					option.setId(rset.getLong(1));
					option.setOptionTitle(rset.getString(2));
					option.setOptionLink(rset.getString(3));
					option.setPollId(rset.getLong(4));
					option.setVotesCount(rset.getLong(5));
				}
			} finally {
				try { rset.close(); } catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		return option;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public long addVoteToPollOptionById(long optionId) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		PollOption option = getPollOptionById(optionId);
		long votesCount = option.getVotesCount() + 1;
		
		try {
			pst = con.prepareStatement(
				"UPDATE PollOptions SET votesCount = ? WHERE id = ?");
			pst.setLong(1, votesCount);
			pst.setLong(2, optionId);
			
			pst.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		return option.getPollId();
	}
}
