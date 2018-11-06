package backend.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import backend.database.Database;

import backend.classes.Common;

public class User implements Common {
	public String username;
	public String email;
	public String location;
	public String resumeLink;
	public String moreLinks;
	public String imagePath;

	public static User NOT_FOUND = new User("", "", "", "", "", "");
	
	public User(String username, String email, String location, String resumeLink, String moreLinks, String imagePath) {
		this.username = username;
		this.email = email;
		this.location = location;
		this.resumeLink = resumeLink;
		this.moreLinks = moreLinks;
		this.imagePath = imagePath;
	}
	
	public boolean isNotFound() {
		return this == NOT_FOUND;
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
}
