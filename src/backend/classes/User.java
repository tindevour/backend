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
}
