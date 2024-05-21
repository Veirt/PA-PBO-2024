package pa.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import pa.App;
import pa.models.*;

public class CreateController {

	@FXML
	private void setSceneToAnimeList() throws IOException {
		App.setScene("Read");
	}

	public void initialize() {

	}
}
