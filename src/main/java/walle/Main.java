package walle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private final WALLE walle = new WALLE("data/walle.txt");

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
