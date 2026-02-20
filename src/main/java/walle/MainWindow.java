package walle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


/**
 * The main JavaFX window for the chatbot application.
 * Handles user interactions and displays bot responses.
 */
public class MainWindow {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private Walle walle;

    /**
     * Initializes the UI components after FXML is loaded.
     */

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Sets the main application instance so that UI callbacks can delegate to it.
     *
     * @param w Application instance.
     */
    public void setWalle(Walle w) {
        this.walle = w;
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(walle.getWelcomeMessage())
        );
    }

    /**
     * Handles user input from the text field and displays the bot response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = walle.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getDukeDialog(response)
        );

        userInput.clear();

        if (walle.isExitCommand(input)) {
            if (walle.isExitCommand(input)) {
                userInput.setDisable(true);
                Platform.exit();
            }
        }
    }
}
