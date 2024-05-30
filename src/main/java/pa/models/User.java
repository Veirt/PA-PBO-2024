package pa.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import pa.DB;

public final class User {
	public String username;
	private String password;
	public String role;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	// Overloading
	public User(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public void register() {
		String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";

		// hash password.
		String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

		try (PreparedStatement statement = DB.con.prepareStatement(query)) {
			statement.setString(1, username);
			statement.setString(2, hashedPassword);
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error inserting user: " + e.getMessage());
		}
	}

	public boolean login() {
		String query = "SELECT * FROM users WHERE username = ?";
		try (PreparedStatement statement = DB.con.prepareStatement(query)) {
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
			if (!result.next()) {
				return false;
			}

			role = result.getString("role");
			String hashedPassword = result.getString("password");
			return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
		} catch (Exception e) {
			System.out.println("Error reading user: " + e.getMessage());
			return false;
		}
	}

	public boolean isUsernameExists() {
		String query = "SELECT * FROM users WHERE username = ?";
		try (PreparedStatement statement = DB.con.prepareStatement(query)) {
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
			return result.next();
		} catch (Exception e) {
			System.out.println("Error reading user: " + e.getMessage());
			return false;
		}
	}
}
