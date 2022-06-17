import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import query.Download;
import query.List;
import query.Upload;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private BorderPane pane;

    @FXML
    private Button refresh;

    @FXML
    private Button upload;

    @FXML
    private TextField search;

    @FXML
    private VBox list;

    private Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(pane);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                onSearch();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            onSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSearch() throws IOException {
        String query = search.getText().trim();
        System.out.println("SEARCH " + query);
        List s = new List(query);
        s.run("localhost", 9999);
        list.getChildren().clear();
        for (String filename : s.result()) {
            HBox box = new HBox();

            Text text = new Text(filename);
            text.wrappingWidthProperty().bind(pane.widthProperty().subtract(25));
            text.getStyleClass().add("text");
            text.setFont(new Font("Tahoma", 12));
            box.getChildren().add(text);

            Button button = new Button();
            button.setText("Download");
            button.setOnAction(event -> {
                FileChooser chooser = new FileChooser();
                chooser.setInitialFileName(filename);
                File file = chooser.showSaveDialog(scene.getWindow());
                if(file == null)
                    return;
                String destination = file.getAbsolutePath();

                try {
                    Download download = new Download(filename, destination);
                    download.run("localhost", 9999);
                } catch (Exception err) {
                    err.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, err.getMessage(), ButtonType.OK);
                    alert.show();
                }
            });
            box.getChildren().add(button);
            list.getChildren().add(box);
        }
    }

    public void onUpload() {
        System.out.println("UPLOAD");

        // Choose file
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(scene.getWindow());
        if(file == null)
            return;
        String source = file.getAbsolutePath();

        // Rename it
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Rename");
        ButtonType uploadType = new ButtonType("Upload", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(uploadType, ButtonType.CANCEL);

        HBox box = new HBox();
        TextField field = new TextField();
        field.setPromptText("Nom du fichier");
        field.setText(file.getName());

        box.getChildren().add(new Label("Nom du fichier:"));
        box.getChildren().add(field);

        dialog.getDialogPane().lookupButton(uploadType);
        dialog.getDialogPane().setContent(box);

        Platform.runLater(field::requestFocus);
        dialog.setResultConverter(dialogButton -> dialogButton == uploadType? field.getText() : null);

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) return;
        String filename = result.get();

        try {
            Upload upload = new Upload(source, filename);
            upload.run("localhost", 9999);
        } catch (Exception err) {
            Alert alert = new Alert(Alert.AlertType.ERROR, err.getMessage(), ButtonType.OK);
            alert.show();
        }
    }
}
