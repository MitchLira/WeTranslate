package database;

import java.sql.*;

public class Database {
	public static Connection connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sdis","sdis", "sdisdb");
		
		return connection;
	}
	
	
	public static boolean insertUser(String email, String password) {
		try {
			Connection connection = connect();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO users VALUES (?, ?)");
			stmt.setString(1, email);
			stmt.setString(2, password);
			stmt.executeUpdate();
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	
	public static boolean insertRequest(String email, String source, String target, String content) {
		try {
			Connection connection = connect();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO requests(content, source_language, target_language, user_email) VALUES (?, ?, ?, ?)");
			stmt.setString(1, content);
			stmt.setString(2, source);
			stmt.setString(3, target);
			stmt.setString(4, email);
			stmt.executeUpdate();
		}
		catch (Exception e) {
			return false;
		}
		
		return true;	
	}
	

	public static boolean insertTranslation(String email, String content, int requestId) {
		try {
			Connection connection = connect();
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO translations(translated_text, requestid, user_email) VALUES (?, ?, ?)");
			stmt.setString(1, content);
			stmt.setInt(2, requestId);
			stmt.setString(3, email);
			stmt.executeUpdate();
		}
		catch (Exception e) {
			return false;
		}
		
		return true;	
	}
	
	
	public static void getRequests() {
		try {
			Connection connection = connect();
			Statement st;
			st = connection.createStatement();
			String sql = ("SELECT * FROM requests");
			ResultSet rs = st.executeQuery(sql);
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
			    for(int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			        Object object = rs.getObject(columnIndex);
			        System.out.printf("%s, ", object == null ? "NULL" : object.toString());
			    }
			    System.out.printf("%n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void getTranslations(int requestId) {
		try {
			Connection connection = connect();
			PreparedStatement st;
			st = connection.prepareStatement("SELECT translated_text FROM translations WHERE requestid = ?");
			st.setInt(1, requestId);
			ResultSet rs = st.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
			    for(int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			        Object object = rs.getObject(columnIndex);
			        System.out.printf("%s, ", object == null ? "NULL" : object.toString());
			    }
			    System.out.printf("%n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
