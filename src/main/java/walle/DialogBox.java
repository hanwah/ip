package walle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialog.setText(text);
        displayPicture.setImage(img);
    }

    private static Image loadImage(String path) {
        // path should start with "/" because it’s from resources root
        return new Image(DialogBox.class.getResourceAsStream(path));
    }

    public static DialogBox getUserDialog(String text) {
        Image userImg = loadImage("/images/DIO.jpeg");
        DialogBox db = new DialogBox("You: " + text, userImg);
        db.getStyleClass().add("user-dialog");
        return db;
    }

    public static DialogBox getDukeDialog(String text) {
        Image walleImg = loadImage("/images/JO.jpeg");
        DialogBox db = new DialogBox("WALLE: " + text, walleImg);
        db.getStyleClass().add("walle-dialog");
        return db;
    }
}
