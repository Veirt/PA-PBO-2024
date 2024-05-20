package pa.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import pa.App;

public class MainController {
	@FXML
	private Button createButton;

	@FXML
	private void setSceneToCreate() throws IOException {
		App.setScene("Create");
	}

	@FXML
	private void setSceneToRead() throws IOException {
		App.setScene("Read");
	}
}
