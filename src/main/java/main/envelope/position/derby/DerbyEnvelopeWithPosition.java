package main.envelope.position.derby;

import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.envelope.Envelope;
import main.envelope.position.EnvelopeWithPosition;
import main.i18n.Messages;

/**
 * <p> {@link EnvelopeWithPosition} implementation with position data in derby database, in table 'envelopewithposition'.
 * 
 * @author paulodamaso
 *
 */
public final class DerbyEnvelopeWithPosition implements EnvelopeWithPosition {
	
	/*
	 * @todo #129 internationalize DerbyEnvelopeWithPosition log messages
	 */
	private static final Logger logger = Logger.getLogger( DerbyEnvelopeWithPosition.class.getName() );

	private final Envelope origin;
	private final String database;
	
	public DerbyEnvelopeWithPosition(Envelope origin, String database) {
		this.origin = origin;
		this.database = database;
		
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); //$NON-NLS-1$
			 
		}catch (Exception e){
			/* @todo #12 implement better exception handling in choosing database driver for DerbyEnvelopeWithPosition
			 * 
			 */
			e.printStackTrace();
		}
	}
	
	private Connection connect() throws Exception {
		return DriverManager.getConnection("jdbc:derby:"+ database +";"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public int id() {
		return origin.id();
	}

	private final String insert_position_query = "insert into envelopewithposition (id, x, y) values ( ?, ?, ?)"; //$NON-NLS-1$
	private final String update_position_query = "update envelopewithposition set x = ?, y = ? where id = ?"; //$NON-NLS-1$
	public DerbyEnvelopeWithPosition position (Point position) {
		Connection conn = null;
		try {

			PreparedStatement ps = null;
			
			if (position() != null) {
				conn = connect();
				ps = conn.prepareStatement(update_position_query);
				ps.setInt(1, position.x);
				ps.setInt(2, position.y);
				ps.setInt(3, id());
			} else {
				conn = connect();
				ps = conn.prepareStatement(insert_position_query);
				ps.setInt(1, id());
				ps.setInt(2, position.x);
				ps.setInt(3, position.y);
			}

			ps.executeUpdate();
			
			return new DerbyEnvelopeWithPosition(origin, database);

		}catch(Exception e) {
			/* @todo #12 implement better exception handling when saving DerbyEnvelopeWithPosition
			 * 
			 */
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			}catch(Exception e) {
				/* @todo #12 implement better exception handling closing connection after saving DerbyEnvelopeWithPosition.
				 * 
				 */
				e.printStackTrace();
			}
		}
		return null;		
	}
	
	private final String position_query = "select x, y from envelopewithposition where id = ?"; //$NON-NLS-1$
	@Override
	public Point position() {
		Connection conn = null;
		Point position = null;
		try {
			conn = connect();
			PreparedStatement ps = conn.prepareStatement(position_query);
			ps.setInt(1, id());

			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				position =  new Point(rs.getInt(1), rs.getInt(2));
			}

		}catch(Exception e) {
			/* @todo #12 implement better exception handling when retrieving position in DerbyEnvelopeWithPosition
			 * 
			 */
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			}catch(Exception e) {
				/* @todo #12 implement better exception handling closing connection after retrieving position in DerbyEnvelopeWithPosition
				 * 
				 */
				e.printStackTrace();
			}
		}
		return position;
	}

	@Override
	public String text() {
		return origin.text();
	}

	@Override
	public void text(String text) {
		origin.text(text);
	}

	@Override
	public Envelope origin() {
		return this.origin;
	}
	
	private final String delete_position_query = "delete from envelopewithposition where id = ?"; //$NON-NLS-1$
	public void delete () {
		Connection conn = null;
		try {

			PreparedStatement ps = null;

			conn = connect();
			ps = conn.prepareStatement(delete_position_query);
			ps.setInt(1, id());
			ps.executeUpdate();

		}catch(Exception e) {
			logger.log(Level.SEVERE, Messages.getString("DerbyEnvelopeWithPosition.errorDeleting"), e); //$NON-NLS-1$
		}finally {
			try {
				conn.close();
			}catch(Exception e) {
				logger.log(Level.SEVERE, Messages.getString("DerbyEnvelopeWithPosition.errorClosingConnection"), e); //$NON-NLS-1$
			}
		}
		origin.delete();
	}

}
