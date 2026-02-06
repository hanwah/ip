package walle;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainWindow {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private WALLE walle;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setWalle(WALLE w) {
        this.walle = w;
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(walle.getWelcomeMessage())
        );
    }

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
            // Optional: close window after bye
            // ((Stage) dialogContainer.getScene().getWindow()).close();
        }
    }
}
