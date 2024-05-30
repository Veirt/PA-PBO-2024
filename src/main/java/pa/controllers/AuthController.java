package pa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pa.App;
import pa.Session;
import pa.Utils;
import pa.models.User;

public class AuthController {
	@FXML
	private TextField usernameTextField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private void login() {
		String username = usernameTextField.getText();
		String password = passwordField.getText();

		User user = new User(username, password);
		if (user.login()) {
			Session.currentUser = user;
			App.setScene("Read");
		} else {
			Utils.errorMessage("Username/password is incorrect.");
		}
	}

	@FXML
	private void register() {
		String username = usernameTextField.getText();
		String password = passwordField.getText();

		User user = new User(username, password);
		user.register();
		Utils.successMessage("User registered successfully.");

		usernameTextField.clear();
		passwordField.clear();
	}
}
