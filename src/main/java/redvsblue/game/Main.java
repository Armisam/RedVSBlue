package redvsblue.game;

import javafx.application.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Main Class of the project.
 */

public class Main {
    private static final Logger logger = LogManager.getLogger();

    /**
     * The main method which launches the app.
     *
     * @param args startings args of the method.
     */

    public static void main(String[] args) {
        logger.debug("Starting app...");
        Application.launch(mainMenuApplication.class, args);
    }

}