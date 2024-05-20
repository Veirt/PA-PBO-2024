package pa.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pa.DB;

import java.sql.Blob;
import java.sql.Date;

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
	protected Blob poster;

	public Anime(int id, String title, String synopsis, int episodes, Date airingDate, String status, String genres,
			String studio, Blob poster) {
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

	public Blob getPoster() {
		return poster;
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
				Blob poster = resultSet.getBlob("poster");
				String type = resultSet.getString("type");
				String season = resultSet.getString("season");

				if (type.equals("Series")) {
					Series series = new Series(id, title, synopsis, episodes, airingDate, status, genres, studio,
							poster, season);
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

	abstract void insert();

	abstract void update();

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
