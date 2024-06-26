package pa.controllers;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pa.App;
import pa.Session;
import pa.models.*;

public class ReadController {
	@FXML
	private FlowPane wrapper;

	@FXML
	private Button animeListButton;

	@FXML
	private Button inputAnimeButton;

	@FXML
	private void setSceneToRead() {
		App.setScene("Read");
	}

	@FXML
	private void setSceneToCreate() {
		InputController.action = "create";
		App.setScene("Input");
	}

	@FXML
	private void logout() {
		Session.currentUser = null;
		App.setScene("Auth");
	}

	@FXML
	private void exit() {
		System.exit(0);
	}

	public void hideComponents() {
		if (Session.currentUser.role.equals("user")) {
			inputAnimeButton.setVisible(false);
		}
	}

	public void initialize() throws SQLException {
		hideComponents();
		animeListButton.setStyle("-fx-background-color: #3f3f3f;");

		wrapper.setPadding(new Insets(10));

		Anime.read();
		for (Anime anime : Anime.list) {
			VBox animeVbox = new VBox();
			animeVbox.setPadding(new Insets(10));
			animeVbox.setMinWidth(250);
			animeVbox.setMaxWidth(250);
			animeVbox.setMaxHeight(350);
			animeVbox.setSpacing(10);

			// animeVbox on click
			animeVbox.setOnMouseClicked(e -> {
				AnimeInfoController.currentAnimeId = anime.getId();
				App.setScene("AnimeInfo");
			});

			// 200x300
			// preserveRatio: true
			// ambil posternya
			byte[] posterBytes = anime.getPoster();
			ByteArrayInputStream posterStream = new ByteArrayInputStream(posterBytes);
			Image poster = new Image(posterStream, 200, 300, false, true);

			// ImageView itu sama kayak Picture Box di VB.
			ImageView imageView = new ImageView(poster);
			Label titleLabel = new Label(anime.title);
			// wrap biar kalo panjang, turun ke bawah
			titleLabel.setWrapText(true);
			titleLabel.setFont(new Font(15));

			// kalau series, tampilkan seasonnya
			// kalau movie, tampilkan tanggal rilisnya
			Label releaseLabel = new Label(anime.getReleaseLabel());

			Label statusLabel = new Label(anime.status);

			animeVbox.getChildren().addAll(imageView, titleLabel, releaseLabel, statusLabel);

			wrapper.getChildren().add(animeVbox);
		}

	}
}
