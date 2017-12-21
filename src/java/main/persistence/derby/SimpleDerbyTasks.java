package main.persistence.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import main.Task;
import main.persistence.Persistent;

/**
 * <p> {@link SimpleDerbyTask} collection of {@link Task} persisted in a Derby database ('task' table).
 * 
 * @author paulodamaso
 *
 */
public final class SimpleDerbyTasks implements DerbyTasks<DerbyTask> {
	
	private final String database;

	public SimpleDerbyTasks(String database) {
		this.database = database;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			 
		}catch (Exception e){
			/* @todo #12 implement better exception handling in choosing derby driver
			 * 
			 */
			e.printStackTrace();
		}
	}
	

	public Connection connect() throws Exception {
		
		return DriverManager.getConnection("jdbc:derby:"+ database +";");
	}

	private final String iterate_query = "select id from task";
	@Override
	public Iterable<DerbyTask> iterate() {
		ArrayList<DerbyTask> it = new ArrayList<DerbyTask>();
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement ps = conn.prepareStatement(iterate_query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				it.add(new SimpleDerbyTask(database, rs.getInt(1)));
			}
		}catch (Exception e) {
			/* @todo #12 implement better exception handling when getting Iterable<Task>.
			 * 
			 */
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return it;
	}

	private final String insert_query = "insert into task (description) values (?)";
	@Override
	public SimpleDerbyTask add(String description) {
		Connection conn = null;
		try {
			conn = connect();
			PreparedStatement ps = conn.prepareStatement(insert_query,  PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, description);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			return new SimpleDerbyTask(database, rs.getInt(1));
			
		}catch (Exception e) {
			/* @todo #12 implement better exception handling when inserting task.
			 * 
			 */
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/* 
	 * @ todo #12 trying to save this task, but what if it's not persistent?
	 */
	@Override
	public void save() {
		for(Task tsk : iterate()) {
			try {
				((Persistent)tsk).save();
			} catch (Exception e) {
				System.out.println("Sorry, task not persistent");
			}
		}
	}


	@Override
	public SimpleDerbyTask add(DerbyTask task) {
		// TODO Auto-generated method stub
		return null;
	}

}
