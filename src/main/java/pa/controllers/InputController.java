package pa.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pa.App;
import pa.Utils;
import pa.models.*;

public class InputController {
	// antara 'create" atau "update"
	public static String action;
	public static int currentId;

	@FXML
	private TextField titleTextField;
	@FXML
	private TextArea synopsisTextArea;
	@FXML
	private TextField episodesTextField;
	@FXML
	private CheckBox unknownCheckBox;
	@FXML
	private DatePicker airingDatePicker;
	@FXML
	private ComboBox<String> statusComboBox;
	@FXML
	private TextField studioTextField;
	@FXML
	private VBox genreVbox;
	@FXML
	private ComboBox<String> typeComboBox;
	@FXML
	private ImageView posterImageView;
	@FXML
	private Button actionButton;
	@FXML
	private Label idLabel;
	@FXML
	private Label idLabelValue;

	@FXML
	private void setSceneToAnimeList() {
		App.setScene("Read");
	}

	@FXML
	private void setUnknownEpisode() {
		// Handle ketika anime nya masih ongoing, dan jumlah episode nya belum
		// diketahui.
		episodesTextField.setDisable(unknownCheckBox.isSelected());
		if (unknownCheckBox.isSelected()) {
			episodesTextField.setText("0");
		} else {
			episodesTextField.setText("");
		}
	}

	private boolean validateAnime() {
		if (titleTextField.getText().isEmpty() || synopsisTextArea.getText().isEmpty()
				|| episodesTextField.getText().isEmpty() || airingDatePicker.getValue() == null
				|| statusComboBox.getValue() == null || studioTextField.getText().isEmpty()
				|| typeComboBox.getValue() == null) {
			Utils.errorMessage("All fields must be filled");
			return false;
		}

		if (!(statusComboBox.getValue().equals("Airing") || statusComboBox.getValue().equals("Upcoming")) && episodesTextField.getText().equals("0")) {
			Utils.errorMessage("Episodes can't be zero for non-ongoing anime");
			return false;
		}

		if (getGenres().isEmpty()) {
			Utils.errorMessage("At least one genre must be selected");
			return false;
		}

		if (posterImageView.getImage() == null) {
			Utils.errorMessage("Poster must be uploaded");
			return false;
		}

		return true;
	}

	public String getGenres() {
		ArrayList<String> genres = new ArrayList<>();
		for (int i = 0; i < genreVbox.getChildren().size(); i++) {
			CheckBox genreCheckBox = (CheckBox) genreVbox.getChildren().get(i);
			if (genreCheckBox.isSelected()) {
				genres.add(genreCheckBox.getText());
			}
		}
		return String.join(", ", genres);
	}

	private void createAnime() {
		String title = titleTextField.getText();
		if (!validateAnime()) {
			return;
		}

		String synopsis = synopsisTextArea.getText();
		int episodes = Integer.parseInt(episodesTextField.getText());
		String airingDate = airingDatePicker.getValue().toString();
		String status = statusComboBox.getValue();
		String studio = studioTextField.getText();
		String type = typeComboBox.getValue();

		String genres = getGenres();

		byte[] poster = null;
		try {
			Image image = posterImageView.getImage();
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos); // format apa aja ga ngaruh.
			poster = baos.toByteArray();
		} catch (IOException e) {
			System.out.println("Error converting image to byte array: " + e.getMessage());
		}

		if (type.equals("series")) {
			Series series = new Series(0, title, synopsis, episodes, Date.valueOf(airingDate), status, genres, studio,
					poster);
			series.insert();
		} else {
			Movie movie = new Movie(0, title, synopsis, episodes, Date.valueOf(airingDate), status, genres, studio,
					poster);
			movie.insert();
		}

		Utils.successMessage("Successfully created anime data");
		App.setScene("Read");
	}

	private void updateAnime() {
		String title = titleTextField.getText();
		if (!validateAnime()) {
			return;
		}

		String synopsis = synopsisTextArea.getText();
		int episodes = Integer.parseInt(episodesTextField.getText());
		String airingDate = airingDatePicker.getValue().toString();
		String status = statusComboBox.getValue();
		String studio = studioTextField.getText();
		String type = typeComboBox.getValue();

		String genres = getGenres();

		byte[] poster = null;
		try {
			Image image = posterImageView.getImage();
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos); // format apa aja ga ngaruh.
			poster = baos.toByteArray();
		} catch (IOException e) {
			System.out.println("Error converting image to byte array: " + e.getMessage());
		}

		if (type.equals("Series")) {
			Series series = new Series(currentId, title, synopsis, episodes, Date.valueOf(airingDate), status, genres,
					studio, poster);
			series.update();
		} else {
			Movie movie = new Movie(currentId, title, synopsis, episodes, Date.valueOf(airingDate), status, genres,
					studio, poster);
			movie.update();
		}

		Utils.successMessage("Successfully updated anime data");
		App.setScene("Read");
	}

	@FXML
	private void browsePoster() {
		// https://github.com/Linaeus14/javaFXmaven/blob/main/src/main/java/com/pbo/controller/authController.java
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Picture");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter(
						"Image Files", "*.png", "*.jpg", "*.jpeg"));
		java.io.File file = fileChooser.showOpenDialog((Stage) App.scene.getWindow());

		if (file != null) {
			String path = file.toURI().toString();

			posterImageView.setImage(new javafx.scene.image.Image(path));
		}

		if (file.length() > 1024 * 1024) {
			Utils.errorMessage("File size must be less than 1MB");
			return;
		}

	}

	@FXML
	private void airingDateOnChange() {
		// jika tanggal rilisnya lebih dari hari ini, maka statusnya upcoming
		// jika tanggal rilisnya kurang dari hari ini, maka statusnya bisa airing atau
		// finished
		String status = statusComboBox.getValue();
		statusComboBox.setValue(null);
		statusComboBox.getItems().clear();

		Date airingDate = Date.valueOf(airingDatePicker.getValue());
		Date currentDate = new Date(System.currentTimeMillis());

		if (airingDate.after(currentDate)) {
			statusComboBox.getItems().add("Upcoming");
			statusComboBox.setValue("Upcoming");
		} else {
			statusComboBox.getItems().addAll("Airing", "Finished");
		}

		// handling jika status sebelumnya masih ada di list status yang baru
		if (statusComboBox.getItems().contains(status)) {
			statusComboBox.setValue(status);
		}

	}

	@FXML
	private void statusOnChange() {
		String status = statusComboBox.getValue();

		if (status == null) {
			return;
		}

		if (status.equals("Airing" ) || status.equals("Upcoming"))  {
			unknownCheckBox.setVisible(true);
		} else {
			episodesTextField.setDisable(false);
			episodesTextField.setText("");
			unknownCheckBox.setVisible(false);
		}
	}

	public void initialize() {
		// Episode hanya bisa diisi dengan angka
		String regex = "[0-9]*";
		episodesTextField.setTextFormatter(
				new TextFormatter<>(change -> change.getControlNewText().matches(regex) ? change : null));

		typeComboBox.getItems().addAll("Series", "Movie");

		if (action.equals("create")) {
			actionButton.setText("Create");
			idLabel.setVisible(false);
			idLabelValue.setVisible(false);

			actionButton.onActionProperty().set(e -> {
				createAnime();
			});
		} else {
			actionButton.setText("Update");
			actionButton.onActionProperty().set(e -> {
				updateAnime();
			});

			// Jika actionnya update, maka set semua field dengan data yang sudah ada
			Anime anime = Anime.getById(currentId);
			if (anime == null) {
				Utils.errorMessage("Anime not found.");
			}

			idLabelValue.setText(Integer.toString(anime.getId()));
			titleTextField.setText(anime.title);
			synopsisTextArea.setText(anime.synopsis);
			episodesTextField.setText(Integer.toString(anime.episodes));
			airingDatePicker.setValue(anime.airingDate.toLocalDate());
			this.airingDateOnChange();
			statusComboBox.setValue(anime.status);

			studioTextField.setText(anime.studio);

			String[] genres = anime.genres.split(", ");
			for (int i = 0; i < genreVbox.getChildren().size(); i++) {
				CheckBox genreCheckBox = (CheckBox) genreVbox.getChildren().get(i);
				for (String genre : genres) {
					if (genreCheckBox.getText().equals(genre)) {
						genreCheckBox.setSelected(true);
					}
				}
			}

			typeComboBox.setValue(anime instanceof Series ? "Series" : "Movie");

			byte[] posterBytes = anime.getPoster();
			ByteArrayInputStream posterStream = new ByteArrayInputStream(posterBytes);
			Image poster = new Image(posterStream, 200, 300, false, true);
			posterImageView.setImage(poster);

		}

	}

}