package pa.controllers;

import java.io.ByteArrayInputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import pa.App;
import pa.Utils;
import pa.models.*;

public class AnimeInfoController {
	public static int currentAnimeId;

	@FXML
	Label titleLabel;
	@FXML
	Label synopsisLabel;
	@FXML
	FlowPane genreFlowPane;
	@FXML
	Label typeLabel;
	@FXML
	Label episodesLabel;
	@FXML
	Label statusLabel;
	@FXML
	Label studioLabel;
	@FXML
	Label airingDateLabel;
	@FXML
	Label seasonLabel;
	@FXML
	ImageView posterImageView;

	@FXML
	private void setSceneToRead() {
		App.setScene("Read");
	}

	@FXML
	private void setSceneToCreate() {
		App.setScene("Create");
	}

	@FXML
	private void deleteAnime() {
		Anime currentAnime = null;
		for (Anime anime : Anime.list) {
			if (anime.getId() == currentAnimeId) {
				currentAnime = anime;
			}
		}

		if (currentAnime == null) {
			Utils.errorMessage("Anime tidak ditemukan.");
		}

		if (!Utils.confirmationMessage("Apakah anda yakin ingin menghapus anime ini?")) {
			return;
		}

		currentAnime.delete();

		App.setScene("Read");
	}

	public void initialize() {
		// loop over Anime.list and find currentAnimeId
		Anime currentAnime = null;

		// set the text of the labels
		for (Anime anime : Anime.list) {
			if (anime.getId() == currentAnimeId) {
				currentAnime = anime;
			}
		}

		if (currentAnime == null) {
			Utils.errorMessage("Anime not found.");
		}

		titleLabel.setText(currentAnime.title);
		synopsisLabel.setText(currentAnime.synopsis);

		// split the genres string into an array
		String[] genres = currentAnime.genres.split(", ");
		for (String genre : genres) {
			Label genreLabel = new Label(genre);
			genreLabel.getStyleClass().add("genre-chip");
			genreLabel.setFont(new Font(13));
			genreFlowPane.getChildren().add(genreLabel);
		}

		typeLabel.setText(currentAnime instanceof Series ? "Series" : "Movie");
		episodesLabel.setText(Integer.toString(currentAnime.episodes));
		statusLabel.setText(currentAnime.status);
		studioLabel.setText(currentAnime.studio);
		airingDateLabel.setText(currentAnime.getAiringDate());
		if (currentAnime instanceof Series) {
			seasonLabel.setText(((Series) currentAnime).getSeason());
		} else {
			seasonLabel.setText("N/A");
		}

		byte[] posterBytes = currentAnime.getPoster();
		ByteArrayInputStream posterStream = new ByteArrayInputStream(posterBytes);
		Image poster = new Image(posterStream, 200, 300, false, true);
		posterImageView.setImage(poster);
		posterImageView.setFitWidth(200);

	}
}
