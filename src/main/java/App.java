import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class App extends Application {

    public static void main(String[] args) {
        System.out.println("Start running");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(App.class.getResource("view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Controller controller = (Controller)loader.getController();
        controller.setScene(scene);
        stage.setScene(scene);
        stage.setTitle("Funny Client");
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("styles.css")).toExternalForm());
        // stage.getIcons().add(Images.ICON.get());
        stage.centerOnScreen();
        stage.toFront();
        stage.setResizable(false);
        stage.show();
    }
}
