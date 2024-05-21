package pa;

import java.io.IOException;

import atlantafx.base.theme.CupertinoDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        scene = new Scene(loadFXML("Read"));
        primaryStage.setTitle("Anime99");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void setScene(String fxml) {
        try {
            Stage stage = (Stage) scene.getWindow();
            scene = new Scene(loadFXML(fxml));
            scene.getStylesheets().add(App.class.getResource("application.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            Utils.errorMessage("Failed to load the scene");
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        new DB();
        launch(args);
    }
}