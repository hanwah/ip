package walle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point of the application.
 * Boots the application and starts the UI runtime.
 */
public class Main extends Application {

    private final WALLE walle = new WALLE("data/walle.txt");

    /**
     * Starts the application.
     *
     * @param args Command-line arguments.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MainWindow controller = fxmlLoader.getController();
        controller.setWalle(walle);

        stage.setScene(scene);
        stage.setTitle("WALLE");
        stage.show();
    }
}
