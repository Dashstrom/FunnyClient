import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import query.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {

    @FXML
    private BorderPane pane;

    @FXML
    private Button refresh;

    @FXML
    private Button upload;

    @FXML
    private ScrollPane sp;

    @FXML
    private TextField search;

    @FXML
    private VBox list;

    private Scene scene;

    private static final String UNAUTHORIZED_CHAR = "[^A-Za-z0-9._-]+";
    private static final int MAX_LENGTH = 200;
    private static final String ACCENT_CHAR = "[^\\p{ASCII}]";

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void handleError(Exception exception) {
        System.err.println("Handled exception :");
        exception.printStackTrace();
        handleError(exception.getMessage());
    }

    public void handleError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setFitToWidth(true);
        try {
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                onSearch();
            });
            onSearch();
        } catch (Exception err) {
            handleError(err);
        }
    }

    public Optional<String> askRename(File file) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Rename");
        ButtonType uploadType = new ButtonType("Upload", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(uploadType, ButtonType.CANCEL);

        HBox box = new HBox();
        TextField field = new TextField();
        field.setPromptText("Nom du fichier");
        field.setText(securityString(file.getName()));

        box.getChildren().add(new Label("Nom du fichier:"));
        box.getChildren().add(field);

        dialog.getDialogPane().lookupButton(uploadType);
        dialog.getDialogPane().setContent(box);

        Platform.runLater(field::requestFocus);
        dialog.setResultConverter(dialogButton -> dialogButton == uploadType? field.getText() : null);

        return dialog.showAndWait();
    }



    public void onSearch() {
        String query = search.getText().trim();
        Search s = new Search(query);

        try {
            s.run("localhost", 9999);
        } catch (DeniedActionException err) {
            handleError("Ce caractère n'est pas valide");
            search.setText("");
            return;
        } catch (IOException err) {
            handleError(err);
        }

        list.getChildren().clear();
        for (String filename : s.result()) {
            HBox box = new HBox();

            Text text = new Text(filename);
            // text.wrappingWidthProperty().bind(pane.widthProperty().subtract(25));
            text.setWrappingWidth(415);
            text.getStyleClass().add("text");
            text.setFont(new Font("Tahoma", 12));
            box.getChildren().add(text);
            VBox.setVgrow(text, Priority.ALWAYS);

            Button downloadButton = new Button();
            downloadButton.setText("Download");
            downloadButton.setMinWidth(80);
            downloadButton.setStyle("-fx-background-color: lightblue");
            downloadButton.setOnAction(event -> {
                FileChooser chooser = new FileChooser();
                chooser.setInitialFileName(filename);
                File file = chooser.showSaveDialog(scene.getWindow());
                if(file == null)
                    return;
                String destination = file.getAbsolutePath();

                try {
                    Download download = new Download(filename, destination);
                    download.run("localhost", 9999);
                } catch (DeniedActionException err) {
                    handleError("Le fichier n'a pu être trouvé");
                    onSearch();
                } catch (IOException err) {
                    handleError(err);
                }
                onSearch();
            });
            box.getChildren().add(downloadButton);

            Button deleteButton = new Button();
            deleteButton.setText("Delete");
            deleteButton.setStyle("-fx-background-color: lightcoral");
            deleteButton.setMinWidth(80);
            deleteButton.setOnAction(event -> {
                Delete delete =  new Delete(filename);
                try {
                    delete.run("localhost", 9999);
                } catch (DeniedActionException err) {
                    handleError("Le fichier n'existe pas");
                    onSearch();
                } catch (IOException err) {
                    handleError(err);
                }
                onSearch();
            });
            box.getChildren().add(deleteButton);
            box.setStyle("-fx-padding: 2px;");

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
        Optional<String> result = askRename(file);
        if (!result.isPresent()) return;
        String filename = result.get();
        System.out.println(filename);

        try {
            Upload upload = new Upload(source, filename);
            upload.run("localhost", 9999);
        }  catch (DeniedActionException err) {
            handleError("Fichier déjà existant ou nom invalide");
        } catch (IOException err) {
            handleError(err);
        }
        onSearch();
    }

    public void onDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
        sp.getStyleClass().setAll("over");
    }

    public void onDragDropped(DragEvent event) {
        event.setDropCompleted(true);
        sp.getStyleClass().setAll();
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            for (File file : db.getFiles()) {
                if (!file.isFile()) continue;
                Upload upload = new Upload(file.toString(), file.getName());
                try {
                    upload.run("localhost", 9999);
                } catch (DeniedActionException err) {
                    handleError("You must rename " + file);
                    break;
                }  catch (IOException err) {
                    handleError(err);
                    break;
                }
                onSearch();
            }
        }
        event.consume();
    }

    public String securityString(String filename) {
        // Shorten the filename if above MAX_LENGTH
        if (filename.length() > MAX_LENGTH)
            filename = filename.substring(0, MAX_LENGTH);

        // Replace accented characters with unaccented characters
        filename = Normalizer.normalize(filename, Normalizer.Form.NFD);
        filename = filename.replaceAll(ACCENT_CHAR, "");

        // Remove unauthorized characters
        filename = filename.replaceAll(UNAUTHORIZED_CHAR, "_");

        return filename;
    }
}
