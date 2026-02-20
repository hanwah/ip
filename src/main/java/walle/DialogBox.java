package walle;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;




/**
 * A UI component that displays dialog bubbles for user and bot messages.
 * Provides factory methods for creating styled dialog boxes.
 */

public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box representing the user's input.
     *
     * @param text User's message.
     * @param img User's avatar image.
     * @return A dialog box for the user.
     */
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

    /**
     * Returns a dialog box styled as a user message.
     *
     * @param text Message text.
     * @return A dialog box for the user.
     */
    public static DialogBox getUserDialog(String text) {
        Image userImg = loadImage("/images/DIO.jpeg");
        DialogBox db = new DialogBox("You: " + text, userImg);
        db.getStyleClass().add("user-dialog");
        return db;
    }

    /**
     * Creates a dialog box representing the bot's response.
     *
     * @param text Bot's message.
     * @param text Bot's avatar image.
     * @return A dialog box for the bot.
     */
    public static DialogBox getDukeDialog(String text) {
        Image walleImg = loadImage("/images/JO.jpeg");
        DialogBox db = new DialogBox("Walle: " + text, walleImg);
        db.getStyleClass().add("walle-dialog");
        return db;
    }
}
