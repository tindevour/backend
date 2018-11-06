package backend.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import backend.classes.Common;
import backend.database.Database;

public class Skill implements Common {
	public String skill;
	public int sid;
	
	public Skill(String skill, int sid) {
		this.skill = skill;
		this.sid = sid;
	}
	
	public boolean isNotFound() { return false; }
	
	public static Skill[] getAllSkills() {
		try {
			ResultSet rs = Database.executeQuery("select skill, sid from skills");
			int size = Database.getNumberOfRows(rs);
			Skill[] skills = new Skill[size];
			int i = 0;
			while(rs.next()) {
				String sname = rs.getString(1);
				int sid = rs.getInt(2);				
				Skill skill = new Skill(sname, sid);				
				skills[i] = skill;
				i++;
			}
			return skills;
		}
		catch (SQLException ex) {
			return new Skill[] {};
		}
	}
}
