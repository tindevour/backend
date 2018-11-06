package backend.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import backend.classes.Common;
import backend.database.Database;


enum NotificationType {
	REQUEST,
	ACCEPTED,
	REJECTED
};

public class Notification implements Common {
	public int pid;
	public int nid;
	public int type;
	public String username;
	public Date time;
	
	public Notification(int pid, int nid, int type, String username, Date time) {
		this.pid = pid;
		this.nid = nid;
		this.type = type;
		this.username = username;
		this.time = time;
	}
	
	public boolean isNotFound() { return false; }
	
	public static Notification[] getUserNotifications(String username) {
		try {
			String query = "select nid, pid, type, for, time from notifications where for=?";
			ResultSet rs = Database.executeQuery(query, username);
			int size = Database.getNumberOfRows(rs);
			int i = 0;
			
			Notification[] notifs = new Notification[size];
			while(rs.next()) {
				int nid = rs.getInt(1);
				int pid = rs.getInt(2);
				int type = rs.getInt(3);
				Date time = rs.getTimestamp(5);
				
				notifs[i] = new Notification(nid, pid, type, username, time);
				i++;
			}
			return notifs;
		}
		catch (SQLException ex) {
			return new Notification[]{};
		}
	}
};
