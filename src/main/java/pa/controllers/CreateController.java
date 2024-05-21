package pa.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

public class CreateController {
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

		if (!statusComboBox.getValue().equals("Airing") && episodesTextField.getText().equals("0")) {
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
		String genres = "";
		for (int i = 0; i < genreVbox.getChildren().size(); i++) {
			CheckBox genreCheckBox = (CheckBox) genreVbox.getChildren().get(i);
			if (genreCheckBox.isSelected()) {
				genres += genreCheckBox.getText() + ", ";
			}
		}
		return genres;
	}

	@FXML
	private void createAnime() throws SQLException {
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

	}

	@FXML
	private void statusOnChange() {
		String status = statusComboBox.getValue();

		if (status == null) {
			return;
		}

		if (status.equals("Airing")) {
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
	}
}
