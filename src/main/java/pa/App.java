package pa;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new Scene(loadFXML("Main"));
        primaryStage.setTitle("Anime99");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void setScene(String fxml) throws IOException {
        Stage stage = (Stage) scene.getWindow();
        scene = new Scene(loadFXML(fxml));
        stage.setScene(scene);
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