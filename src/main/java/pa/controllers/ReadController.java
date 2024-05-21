package pa.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pa.App;
import pa.models.*;

public class ReadController {
	@FXML
	private FlowPane wrapper;

	@FXML
	private void setSceneToCreateAnime() throws IOException {
		App.setScene("Create");
	}

	public void initialize() throws SQLException {
		wrapper.setPadding(new Insets(10));

		Anime.read();
		for (Anime anime : Anime.list) {
			VBox animeVbox = new VBox();
			animeVbox.setPadding(new Insets(10));
			animeVbox.setMaxWidth(250);
			animeVbox.setMaxHeight(350);
			animeVbox.setSpacing(10);

			// 200x300
			// preserveRatio: true
			// ambil posternya
			Image poster = new Image(anime.getPoster().getBinaryStream(), 200, 300, false, true);

			// ImageView itu sama kayak Picture Box di VB.
			ImageView imageView = new ImageView(poster);
			Label titleLabel = new Label(anime.title);
			// wrap biar kalo panjang, turun ke bawah
			titleLabel.setWrapText(true);
			titleLabel.setFont(new Font(15));

			// kalau series, tampilkan seasonnya
			// kalau movie, tampilkan tanggal rilisnya
			Label releaseLabel = new Label(
					anime instanceof Series ? ((Series) anime).getFullSeason() : anime.getAiringDate());

			Label statusLabel = new Label(anime.status);

			animeVbox.getChildren().addAll(imageView, titleLabel, releaseLabel, statusLabel);

			wrapper.getChildren().add(animeVbox);
		}

	}
}
