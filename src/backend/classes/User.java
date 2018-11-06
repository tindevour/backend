package backend.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import backend.classes.Common;
import backend.classes.Notification;
import backend.classes.UnauthorizedError;
import backend.database.Database;


public class User implements Common {
	public String username;
	public String email;
	public String location;
	public String resumeLink;
	public String moreLinks;
	public String imagePath;

	private boolean isAuthenticated = false;
	
	public static User NOT_FOUND = new User("", "", "", "", "", "");
	
	public User(String username, String email, String location, String resumeLink, String moreLinks, String imagePath) {
		this.username = username;
		this.email = email;
		this.location = location;
		this.resumeLink = resumeLink;
		this.moreLinks = moreLinks;
		this.imagePath = imagePath;
	}
	
	public User(String username) {
		this.username = username;
	}
	
	public boolean isNotFound() {
		return this == NOT_FOUND;
	}
	
	public boolean authenticate(String password) {
		String query = "select password from users where username=?";
		try {
			ResultSet rs = Database.executeQuery(query, this.username);
			boolean hasNext = rs.next();
			if (hasNext) {
				String ogpwd = rs.getString(1);
				this.isAuthenticated = true;
				return password.equals(ogpwd);
			}
			else {
				return false;
			}
		}
		catch (SQLException ex ) {
			return false;
		}
	}
	
	public static User getByUsername(String username) {
		String query = "select username, email, location, resumeLink, moreLinks, imagePath from users where username=?";
		try {
			ResultSet rs = Database.executeQuery(query, username);
			boolean hasNext = rs.next();
			if (hasNext) {
				String email = rs.getString(2);
				String location = rs.getString(3);
				String resumeLink = rs.getString(4);
				String moreLinks = rs.getString(5);
				String imagePath = rs.getString(6);
				return new User(username, email, location, resumeLink, moreLinks, imagePath);
			}
			else {
				return NOT_FOUND;
			}
			
		}
		catch (SQLException ex) {
			return NOT_FOUND;
		}
	}
	
	public Notification[] notifications() throws UnauthorizedError {
		if (this.isAuthenticated) {
			return Notification.getUserNotifications(this.username);
		}
		else {
			throw new UnauthorizedError();
		}
	}
	
	public static User register(String name, String username, String email, String password, int[] aids, int[] sids) {
		try {
			Database.setAutoCommit(false);
			
			String userInsertionQuery = "insert into users(name, username, password, email) values(?, ?, ?, ?)";
			Database.executeUpdate(userInsertionQuery, name, username, password, email);
			
			String skillsInsertionQuery = "insert into user_skills(username, sid) values(?, ?)";
			for(int i = 0; i < sids.length; i++) 
				Database.executeUpdate(skillsInsertionQuery, username, sids[i]);
			
			String areasInsertionQuery = "insert into user_aois(username, aid) values(?, ?)";
			for(int i = 0; i < aids.length; i++) 
				Database.executeUpdate(areasInsertionQuery, username, aids[i]);			

			Database.commit();
			Database.setAutoCommit(true);
			
			User currUser = new User(username);
			currUser.isAuthenticated = true;
			return currUser;
		}
		catch (SQLException ex) {
			try {
				Database.rollback();				
			}
			catch (SQLException exc) {
				
			}
			return null;
		}
	}
	
	public boolean changePassword(String newPassword) throws UnauthorizedError {
		if (this.isAuthenticated) {
			String query = "update users set password=? where username=?";
			try {
				Database.executeUpdate(query, newPassword, this.username);
				return true;
			}
			catch (SQLException ex) {
				return false;
			}
		}
		else {
			throw new UnauthorizedError();
		}
	}
	
	public boolean reportSpam(String otherUser) throws UnauthorizedError {
		if (this.isAuthenticated) {
			try {
				String query = "insert into user_spams(reporter, spammer) values(?, ?)";
				Database.executeQuery(query, this.username, otherUser);
				return true;
			}
			catch(SQLException ex) {
				return false;
			}			
		}
		else {
			throw new UnauthorizedError();
		}
	}
	
	public boolean reportSpam(int pid) throws UnauthorizedError {
		if (this.isAuthenticated) {
			try {
				String query = "insert into project_spams(reporter, pid) values(?, ?)";
				Database.executeQuery(query, this.username, pid);
				return true;
			}
			catch(SQLException ex) {
				return false;
			}			
		}
		else {
			throw new UnauthorizedError();
		}
	}
}
