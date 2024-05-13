package gui.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Blob;
import java.sql.Date;

import gui.DB;

public final class Series extends Anime {
	public String season;

	public Series(int id, String title, String synopsis, int episodes, String status, Date airingDate, String genres,
			String studio, Blob poster, String season) {
		super(id, title, synopsis, episodes, status, airingDate, genres, studio, poster);
		this.season = season;
	}

	// Contoh: Winter 2024
	public String getFullSeason() {
		return season + " " + airingDate.toLocalDate().getYear();
	}

	@Override
	public void insert() {
		String query = "INSERT INTO anime (title, synopsis, episodes, status, airingDate, genres, studio, poster, type, season) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = DB.con.prepareStatement(query)) {
			statement.setString(1, title);
			statement.setString(2, synopsis);
			statement.setInt(3, episodes);
			statement.setString(4, status);
			statement.setDate(5, airingDate);
			statement.setString(6, genres);
			statement.setString(7, studio);
			statement.setBlob(8, poster);
			statement.setString(9, "series");
			statement.setString(10, season);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error inserting series: " + e.getMessage());
		}
	}

	@Override
	public void update() {
		String query = "UPDATE anime SET title = ?, synopsis = ?, episodes = ?, status = ?, airingDate = ?, genres = ?, studio = ?, poster = ?, type = ?, season = ? WHERE id = ?";
		try (PreparedStatement statement = DB.con.prepareStatement(query)) {
			statement.setString(1, title);
			statement.setString(2, synopsis);
			statement.setInt(3, episodes);
			statement.setString(4, status);
			statement.setDate(5, airingDate);
			statement.setString(6, genres);
			statement.setString(7, studio);
			statement.setBlob(8, poster);
			statement.setString(9, "series");
			statement.setString(10, season);
			statement.setInt(11, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error updating series: " + e.getMessage());
		}
	}

}
