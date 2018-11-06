package backend.database;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Database {
	static Connection conn;

	private static String dbname;
	private static String user;
	private static String pass;

	static boolean isInitialized = false;


	public static void setDBName(String name) {
		dbname = name;
	}

	public static void setUser(String username) {
		user = username;
	}

	public static void setPass(String password) {
		pass = password;
	}

	public static void setAutoCommit(boolean status) throws SQLException {
		conn.setAutoCommit(status);
	}
	
	public static void commit() throws SQLException {
		conn.commit(); 
	}
	
	public static void rollback() throws SQLException {
		conn.rollback();
	}
	
	public static void init() {
		dbname = System.getenv("DB_NAME");
		user = System.getenv("DB_USER");
		pass = System.getenv("DB_PASS");

		if (dbname == null || user == null || pass == null) {
			System.out.println("ERROR!! Either of the DB_NAME, DB_USER or DB_PASS is not set correctly");
		}

		init(dbname, user, pass);
	}

	public static void init(String dbname, String user, String pass) {
		try {
			Class.forName("org.postgresql.Driver");
			
			String url = "jdbc:postgresql://localhost/" + dbname;

			try {
				conn = DriverManager.getConnection(url, user, pass);
			}
			catch (SQLException e) {
				System.out.println("Exception: " + e);
				e.printStackTrace();
			}
			isInitialized = true;			
		}
		catch (ClassNotFoundException ex) {
			System.out.println("PostgreSQL JDBC Driver not found");
		}
	}

	public static PreparedStatement createPreparedStatement(String query, Object... args) throws SQLException {
		PreparedStatement st = conn.prepareStatement(query);
		for(int i = 1; i <= args.length; i++) {
			Object arg = args[i - 1];

			String type = arg.getClass().getSimpleName();

			switch(type) {
			case "String":
				st.setString(i, (String) arg);
				break;
			case "Integer":
				st.setInt(i, (Integer) arg);
				break;
			case "Boolean":
				st.setBoolean(i,  (Boolean) arg);
				break;
			case "Array":
				st.setArray(i, (Array) arg);
				break;
			case "Object":
				System.out.print("class is Object :/");
				break;
			}
		}
		return st;
	}

	public static ResultSet executeQuery(String query, Object... args) throws SQLException{
		if (!isInitialized) {
			init();
		}
		PreparedStatement st = createPreparedStatement(query, args);
		return st.executeQuery();
	}

	public static int executeUpdate(String query, Object... args) throws SQLException {
		if (!isInitialized) {
			init();
		}
		PreparedStatement st = createPreparedStatement(query, args);
		return st.executeUpdate();
	}

	public static int getNumberOfRows(ResultSet rs) throws SQLException {
		int current = rs.getRow();
		rs.last();
		int length = rs.getRow();
		rs.absolute(current);
		return length;
	}
}
