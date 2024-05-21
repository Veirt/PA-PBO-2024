package pa.models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pa.DB;

public abstract class Anime {
	public static ArrayList<Anime> list = new ArrayList<>();

	protected int id;
	public String title;
	public String synopsis;
	public int episodes;
	public Date airingDate;
	public String status;
	public String genres;
	public String studio;
	protected byte[] poster;

	public Anime(int id, String title, String synopsis, int episodes, Date airingDate, String status, String genres,
			String studio, byte[] poster) {
		this.id = id;
		this.title = title;
		this.synopsis = synopsis;
		this.episodes = episodes;
		this.airingDate = airingDate;
		this.status = status;
		this.genres = genres;
		this.studio = studio;
		this.poster = poster;
	}

	public int getId() {
		return id;
	}

	public byte[] getPoster() {
		return poster;
	}

	public void setPoster(byte[] poster) {
		this.poster = poster;
	}

	public String getAiringDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		return sdf.format(airingDate);
	}

	public static void read() {
		// clear biar kalo dipanggil read berkali-kali
		// data yang sama ga numpuk
		list.clear();
		String query = "SELECT * FROM anime";
		try (Statement statement = DB.con.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String title = resultSet.getString("title");
				String synopsis = resultSet.getString("synopsis");
				int episodes = resultSet.getInt("episodes");
				Date airingDate = resultSet.getDate("airingDate");
				String status = resultSet.getString("status");
				String genres = resultSet.getString("genres");
				String studio = resultSet.getString("studio");
				byte[] poster = resultSet.getBytes("poster");
				String type = resultSet.getString("type");

				if (type.equals("Series")) {
					Series series = new Series(id, title, synopsis, episodes, airingDate, status, genres, studio,
							poster);
					list.add(series);
				} else if (type.equals("Movie")) {
					Movie movie = new Movie(id, title, synopsis, episodes, airingDate, status, genres, studio, poster);
					list.add(movie);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error reading anime: " + e.getMessage());
		}
	}

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
			statement.setBytes(8, poster);
			statement.setString(9, "movie");
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error inserting anime: " + e.getMessage());
		}
	}

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
			statement.setBytes(8, poster);
			statement.setString(9, "movie");
			statement.setInt(10, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error updating anime: " + e.getMessage());
		}
	}

	void delete() {
		String query = "DELETE FROM anime WHERE id = ?";
		try (PreparedStatement statement = DB.con.prepareStatement(query)) {
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error deleting anime: " + e.getMessage());
		}
	}

}