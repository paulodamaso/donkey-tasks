package main.note.persistence.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.i18n.Messages;
import main.note.Note;

/**
 * <p> {@link Note} decoration for derby embedded persistence.
 * 
 * @author paulodamaso
 *
 */
public final class DerbyNote implements Note {

	/*
	 * @todo #129 internationalize DerbyNote log messages
	 */
	private static final Logger logger = Logger.getLogger( DerbyNote.class.getName() );
	
	private final String database = "resources/database/sticky-notes-db"; //$NON-NLS-1$
	private final int id;
	
	public DerbyNote(int id) {
//		this.database = database;
		this.id = id;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); //$NON-NLS-1$
			
		}catch (Exception e){
			/* @todo #12 implement better exception handling in choosing derby driver here
			 * 
			 */
			e.printStackTrace();
		}
	}
	
	public Connection connect() throws Exception {
		
		return DriverManager.getConnection("jdbc:derby:"+ database +";"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private final String description_query = "select text from note where id = ?"; //$NON-NLS-1$
	@Override
	public String text() {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement ps = conn.prepareStatement(description_query);
			ps.setInt(1, id());
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString(1);
		}catch(Exception e) {
			/* @todo #12 implement better exception handling when returning description.
			 * 
			 */
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			}catch(Exception e) {
				/* @todo #12 implement better exception handling here.
				 * 
				 */
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int id() {
		return id;
	}

	private final String save_query = "update note set text = ? where id = ?"; //$NON-NLS-1$
	@Override
	public void text(String text) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement ps = conn.prepareStatement(save_query);
			ps.setString(1, text);
			ps.setInt(2, this.id());
			ps.executeUpdate();

		}catch(Exception e) {
			/* @todo #12 implement better exception handling when updating text
			 * 
			 */
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			}catch(Exception e) {
				/* @todo #12 implement better exception handling closing connection after updating text
				 * 
				 */
				e.printStackTrace();
			}
		}
	}
	
	private final String delete_query = "delete from note where id = ?"; //$NON-NLS-1$

	@Override
	public void delete() {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement ps = conn.prepareStatement(delete_query);
			ps.setInt(1, this.id);
			ps.executeUpdate();
		}catch(Exception e) {
			logger.log(Level.SEVERE, Messages.getString("DerbyNote.errorDeleting"), e); //$NON-NLS-1$
		}finally {
			try {
				conn.close();
			}catch(Exception e) {
				logger.log(Level.SEVERE, Messages.getString("DerbyNote.errorClosingConnection"), e); //$NON-NLS-1$
			}
		}
	}
}
