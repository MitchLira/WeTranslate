package database;

import java.sql.*;

import org.json.JSONArray;

import utils.Converter;

public class Database {
	public static Connection connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sdis","sdis", "sdisdb");
		return connection;
	}
	
	
	public static boolean insertUser(String email, String password) {
		try {
			Connection connection = connect();
			String sql = "INSERT INTO users VALUES (?, ?)";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
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
			String sql = "INSERT INTO requests(content, source, target, user_email) VALUES (?, ?, ?, ?)";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
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
			String sql = "INSERT INTO translations(translated_text, requestid, user_email) VALUES (?, ?, ?)";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
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
	
	
	public static JSONArray getRequests(String source, String target) {
		try {
			Connection connection = connect();
			String sql = ("SELECT * FROM requests WHERE (source = ? AND target = ?)");
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, source);
			stmt.setString(2, target);
			
			ResultSet rs = stmt.executeQuery();
			
			return Converter.toJSON(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static boolean getTranslations(int requestId) {
		try {
			Connection connection = connect();
			String sql = "SELECT * FROM translations WHERE requestid = ?";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, requestId);
			
			ResultSet rs = stmt.executeQuery();
			
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
			return false;
		}
		
		return true;
	}
	
}
