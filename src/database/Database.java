package database;

import java.sql.*;

import org.json.JSONArray;

import node.Node;
import utils.Converter;

public class Database {
	public static Connection connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection("jdbc:postgresql://" + Node.hostName + ":5432/sdis", "sdis", "sdisdb");
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
	
	
	public static boolean validUser(String username, String password) {
		try {
			Connection connection = connect();
			String sql = ("SELECT email, password FROM requests WHERE (email = ? AND password = ?)");

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				String usernamedb = rs.getString("email");
				String passworddb =  rs.getString("password");

				if ((username.equals(usernamedb)) && (password.equals(passworddb)))
					return true;
			}
		}
		catch (Exception e) {
			return false;
		}

		return false;
	}
	
	
	public static boolean userAlreadyExists(String username) {
		try {
			Connection connection = connect();
			String sql = ("SELECT email FROM requests WHERE email = ? ");

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, username);

			ResultSet rs = stmt.executeQuery();
			
			return !rs.next() ? true : false;
		}
		catch (Exception e) {
			return false;
		}
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
	
	
	public static JSONArray getTranslations(int requestId) {
		try {
			Connection connection = connect();
			String sql = "SELECT * FROM translations WHERE requestid = ?";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, requestId);
			
			ResultSet rs = stmt.executeQuery();
			return Converter.toJSON(rs);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
