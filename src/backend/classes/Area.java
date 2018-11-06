package backend.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import backend.database.Database;
import backend.classes.Common;


public class Area implements Common{
	public String area;
	public int aid;
	
	public Area(String area, int aid) {
		this.area = area;
		this.aid = aid;
	}
	
	public boolean isNotFound() { return false; }
	
	public static Area[] getAllAreas() {
		try {
			ResultSet rs = Database.executeQuery("select area, aid from areas");
			int size = Database.getNumberOfRows(rs);
			Area[] areas = new Area[size];

			int i = 0;
			
			while (rs.next()) {
				String aname = rs.getString(1);
				int aid = rs.getInt(2);
				Area aobj = new Area(aname, aid);
				areas[i] = aobj;
			}
			
			return areas;
		}
		catch(SQLException ex) {
			return new Area[]{};
		}
	}
}
