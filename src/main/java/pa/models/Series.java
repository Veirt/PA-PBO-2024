package pa.models;

import java.sql.Date;
import java.util.Map;

public final class Series extends Anime {
	public Series(int id, String title, String synopsis, int episodes, Date airingDate, String status, String genres,
			String studio, byte[] poster) {
		super(id, title, synopsis, episodes, airingDate, status, genres, studio, poster);
		this.type = "series";
	}

	public String getSeason() {
		// get season from airingDate
		Map<Integer, String> monthToSeason = Map.ofEntries(
				Map.entry(1, "Winter"),
				Map.entry(2, "Winter"),
				Map.entry(3, "Spring"),
				Map.entry(4, "Spring"),
				Map.entry(5, "Spring"),
				Map.entry(6, "Summer"),
				Map.entry(7, "Summer"),
				Map.entry(8, "Summer"),
				Map.entry(9, "Fall"),
				Map.entry(10, "Fall"),
				Map.entry(11, "Fall"),
				Map.entry(12, "Winter"));

		int month = airingDate.toLocalDate().getMonthValue();
		return monthToSeason.get(month) + " " + airingDate.toLocalDate().getYear();
	}
}
