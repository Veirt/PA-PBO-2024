package pa.models;

import java.sql.Date;

public final class Movie extends Anime {
	public Movie(int id, String title, String synopsis, int episodes, Date airingDate, String status, String genres,
			String studio, byte[] poster) {
		super(id, title, synopsis, episodes, airingDate, status, genres, studio, poster);
		this.type = "movie";
	}

}
