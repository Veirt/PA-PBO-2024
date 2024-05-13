package pa.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import pa.DB;

import java.sql.Blob;
import java.sql.Date;

public final class Movie extends Anime {
	public Movie(int id, String title, String synopsis, int episodes, String status, Date airingDate, String genres,
			String studio, Blob poster) {
		super(id, title, synopsis, episodes, status, airingDate, genres, studio, poster);
	}

	@Override
	public void insert() {
		String query = "INSERT INTO anime (title, synopsis, episodes, status, airingDate, genres, studio, poster, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = DB.con.prepareStatement(query)) {
			statement.setString(1, title);
			statement.setString(2, synopsis);
			statement.setInt(3, episodes);
			statement.setString(4, status);
			statement.setDate(5, airingDate);
			statement.setString(6, genres);
			statement.setString(7, studio);
			statement.setBlob(8, poster);
			statement.setString(9, "movie");
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error inserting movie: " + e.getMessage());
		}
	}

	@Override
	public void update() {
		String query = "UPDATE anime SET title = ?, synopsis = ?, episodes = ?, status = ?, airingDate = ?, genres = ?, studio = ?, poster = ?, type = ? WHERE id = ?";
		try (PreparedStatement statement = DB.con.prepareStatement(query)) {
			statement.setString(1, title);
			statement.setString(2, synopsis);
			statement.setInt(3, episodes);
			statement.setString(4, status);
			statement.setDate(5, airingDate);
			statement.setString(6, genres);
			statement.setString(7, studio);
			statement.setBlob(8, poster);
			statement.setString(9, "movie");
			statement.setInt(10, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error updating movie: " + e.getMessage());
		}

	}
}
