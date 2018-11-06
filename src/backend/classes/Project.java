package backend.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import backend.classes.Common;
import backend.database.Database;

public class Project implements Common {
	public String title;
	public String description;
	public User owner;
	public int pid;
	public int numberOfLikes;
	public int numberOfDislikes;
	
	public static Project NOT_FOUND = new Project("", "", User.NOT_FOUND, 0, 0, 0);
	
	public boolean isNotFound() {
		return this == NOT_FOUND;
	}
	
	public Project(String title, String description, User owner, int pid, int numberOfLikes, int numberOfDislikes) {
		this.title = title;
		this.description = description;
		this.owner = owner;
		this.pid = pid;
		this.numberOfLikes = numberOfLikes;
		this.numberOfDislikes = numberOfDislikes;
	}
	
	public static Project getProjectById(int pid) {
		try {
			String query = "select title, description, owner, pid, numberOfLikes, numberOfDislikes from projects where pid=?";
			ResultSet rs = Database.executeQuery(query, pid);
			
			boolean hasNext = rs.next();
			if (hasNext) {
				String title = rs.getString(1);
				String description = rs.getString(2);
				String username = rs.getString(3);
				int numberOfLikes = Integer.parseInt(rs.getString(5));
				int numberOfDislikes = Integer.parseInt(rs.getString(6));
				User user = User.getByUsername(username);
				
				Project project = new Project(title, description, user, pid, numberOfLikes, numberOfDislikes);
				return project;
			}
			else {
				return NOT_FOUND;
			}
		}
		catch (SQLException ex) {
			return NOT_FOUND;
		}
	}

	public static Project create(String title, String description, String owner, int[] aids, int[] sids) {
		try {
			String projectInsertionQuery = "insert into projects(title, description, owner) values(?, ?, ?)";
			Database.executeUpdate(projectInsertionQuery, title, description, owner);

			ResultSet rs = Database.executeQuery("select pid from projects where title=? and owner=? and description=?", title, owner, description);
			rs.next();
			int pid = rs.getInt(1);
			
			String skillInsertionQuery = "insert into project_skills(pid, sid) values(?, ?)";
			for(int i = 0; i < sids.length; i++)
				Database.executeUpdate(skillInsertionQuery, pid, sids[i]);
				
			String areaInsertionQuery = "insert into project_areas(pid, aid) values(?, ?)";
			for(int i = 0; i < aids.length; i++)
				Database.executeUpdate(areaInsertionQuery, pid, aids[i]);
			
			return Project.getProjectById(pid);
		}
		catch (SQLException ex) {
			return null;
		}		
	}
}
