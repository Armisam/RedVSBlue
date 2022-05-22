package redvsblue.game;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * This class is responsible for starting the app with mainMenu.fxml scene.
 */

public class mainMenuApplication extends Application {

    private static final Logger logger = LogManager.getLogger();


    /**
     * This method starts the app by loading mainMenu.fxml scene.
     *
     * @param stage stage got by Main Class
     */

    @Override
    public void start(Stage stage) {
        try {
            logger.debug("Loading application");
            Parent root = FXMLLoader.load(getClass().getResource("/ui/mainMenu.fxml"));
            stage.setTitle("RedVsBlue - MainMenu");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ioException) {
            logger.error(ioException.getMessage());
        }
    }
}