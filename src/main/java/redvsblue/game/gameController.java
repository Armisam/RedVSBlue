package redvsblue.game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;

import javafx.stage.Stage;
import redvsblue.state.Direction;
import redvsblue.state.GameState;

import redvsblue.repository.databaseQueryHandler;

import javafx.scene.input.MouseEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redvsblue.state.Position;

import java.io.IOException;
import java.util.EnumSet;

/**
 * This class controls the inGame.fxml scene of the app.
 */

public class gameController {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private Label Red_Player_Name;

    @FXML
    private Label Blue_Player_Name;

    @FXML
    private Label Player_To_Move;

    @FXML
    private Button Red_Player_Give_Up;

    @FXML
    private void redGiveUpandle(MouseEvent mouseEvent) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("Blue has won the game! Red Gave up!");
        logger.info("Blue has won the game! Red gave up!");
        a.showAndWait();
        registerMatchInStats("Blue");
        loadMainMenu(mouseEvent);
    }

    @FXML
    private Button Blue_Player_Give_Up;

    @FXML
    private void blueGiveUpandle(MouseEvent mouseEvent) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("Red has won the game! Blue Gave up!");
        logger.info("Red has won the game! Blue gave up!");
        a.showAndWait();
        registerMatchInStats("Red");
        loadMainMenu(mouseEvent);
    }


    @FXML
    private GridPane board;

    private String playerToMoveChecker = "Red";

    private final StringProperty redname = new SimpleStringProperty();
    private final StringProperty bluename = new SimpleStringProperty();


    /**
     * This method bind the red players name which was given at mainMenu.fxml scene to the Red_Player_Name label.
     *
     * @param redname Name of the red player given at the mainMenu.fxml scene.
     */
    public void setredName(String redname) {
        this.redname.set(redname);
    }

    /**
     * This method bind the red players name which was given at mainMenu.fxml scene to the Blue_Player_Name label.
     *
     * @param bluename Name of the red player given at the mainMenu.fxml scene.
     */

    public void setblueName(String bluename) {
        this.bluename.set(bluename);
    }

    private void setNames() {
        Red_Player_Name.textProperty().bind(redname);
        Blue_Player_Name.textProperty().bind(bluename);
        Player_To_Move.textProperty().set(playerToMoveChecker + " to move");
    }


    private StackPane[][] squares = new StackPane[4][5];

    private void loadBoard() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
                squares[j][i] = square;
            }
        }

        board.setStyle("-fx-border-color: black; -fx-border-width: 1");
    }

    private StackPane createSquare(int rowInd, int colInd) {
        var square= new StackPane();
        for (var i = 0; i < 8; i++) {
            if (i < 4 && gameState.getPosition(i).row() == rowInd && gameState.getPosition(i).col() == colInd) {
                square.getChildren().add(new Circle(45, Color.RED));
            }

            if (i >= 4 && gameState.getPosition(i).row() == rowInd && gameState.getPosition(i).col() == colInd) {
                square.getChildren().add(new Circle(45, Color.BLUE));
            }
        }
        square.setOnMouseClicked(this::clickOnCell);
        square.setStyle("-fx-border-color: black; -fx-border-width: 1");

        return square;
    }


    private GameState gameState = new GameState();

    @FXML
    private void initialize() {
        logger.info("Game has loaded.");
        setNames();
        loadBoard();
    }

    private int puckToMove;

    @FXML
    private void clickOnCell(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var rowInd = GridPane.getRowIndex(square);
        var colInd = GridPane.getColumnIndex(square);
        logger.trace("Clicked on cell: {},{}!",rowInd, colInd);
        if (!square.getChildren().isEmpty()) {
            if (playerToMoveChecker == "Red") {
                for (var i = 0; i < 4; i++) {
                    if (gameState.getPosition(i).row() == rowInd && gameState.getPosition(i).col() == colInd) {
                        clearLastPossibleMovements();
                        showCanMove(gameState.getLegalMoves(i), rowInd, colInd, i);
                        puckToMove = i;
                    }
                }
            }

            if (playerToMoveChecker == "Blue") {
                for (var i = 4; i < 8; i++) {
                    if (gameState.getPosition(i).row() == rowInd && gameState.getPosition(i).col() == colInd) {
                        clearLastPossibleMovements();
                        showCanMove(gameState.getLegalMoves(i), rowInd, colInd, i);
                        puckToMove = i;
                    }
                }
            }
        }
    }

    private void showCanMove(EnumSet<Direction> legalDirections, int rowInd, int colInd, int PUCK) {
        if (!legalDirections.isEmpty()) {
            var arrLegalDirections = legalDirections.toArray();

            for (int i = 0; i < arrLegalDirections.length; i++) {
                if (arrLegalDirections[i] == Direction.DOWN){
                    squares[colInd][rowInd + 1].setStyle("-fx-background-color:DARKGREY; -fx-border-color: black; -fx-border-width: 1");
                    squares[colInd][rowInd + 1].setOnMouseClicked(this::movePuck);
                } else if (arrLegalDirections[i] == Direction.UP) {
                    squares[colInd][rowInd - 1].setStyle("-fx-background-color:DARKGREY; -fx-border-color: black; -fx-border-width: 1");
                    squares[colInd][rowInd - 1].setOnMouseClicked(this::movePuck);
                } else if (arrLegalDirections[i] == Direction.LEFT) {
                    squares[colInd - 1][rowInd].setStyle("-fx-background-color:DARKGREY; -fx-border-color: black; -fx-border-width: 1");
                    squares[colInd - 1][rowInd].setOnMouseClicked(this::movePuck);
                } else if (arrLegalDirections[i] == Direction.RIGHT) {
                    squares[colInd + 1][rowInd].setStyle("-fx-background-color:DARKGREY; -fx-border-color: black; -fx-border-width: 1");
                    squares[colInd + 1][rowInd].setOnMouseClicked(this::movePuck);
                }
            }
        }
    }

    private void clearLastPossibleMovements() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                squares[j][i].setOnMouseClicked(this::clickOnCell);
                squares[j][i].setStyle("-fx-background-color:WHITE; -fx-border-color: black; -fx-border-width: 1");
            }
        }
    }

    private void movePuck(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var rowInd = GridPane.getRowIndex(square);
        var colInd = GridPane.getColumnIndex(square);

        squares[gameState.getPosition(puckToMove).col()][ gameState.getPosition(puckToMove).row()].getChildren().clear();
        if (playerToMoveChecker == "Red") {
            square.getChildren().add(new Circle(45, Color.RED));
            playerToMoveChecker = "Blue";
        } else if (playerToMoveChecker == "Blue") {
            square.getChildren().add(new Circle(45, Color.BLUE));
            playerToMoveChecker = "Red";
        }

        Position[] newpositions = new Position[8];
        for (int i = 0; i < 8; i++) {
            if (i == puckToMove) {
                newpositions[i] = new Position(rowInd, colInd);
            }
            else {
                newpositions[i] = gameState.getPosition(i);
            }
        }

        Player_To_Move.textProperty().set(playerToMoveChecker + " to move");
        logger.trace("Clicked on cell: {},{}!",rowInd, colInd);
        logger.debug("Moving from: {},{} to {},{}", gameState.getPosition(puckToMove).row(), gameState.getPosition(puckToMove).col(), rowInd, colInd);
        gameState = new GameState(newpositions);
        clearLastPossibleMovements();
        gameWonHandle(event);
    }

    private void gameWonHandle(MouseEvent event) {
        if (gameState.isWon()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);

            if (playerToMoveChecker == "Blue") {
                a.setContentText("Red has won the game!");
                logger.info("Red has won the game!");
            } else {
                a.setContentText("Blue has won the game!");
                logger.info("Blue has won the game!");
            }
            a.showAndWait();
            registerMatchInStats(playerToMoveChecker == "Blue"? "Red" : "Blue");
            loadMainMenu(event);
        }
    }

    private void loadMainMenu(MouseEvent mouseEvent) {
        try {
            logger.debug("Loading MainMenu");
            var stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ui/mainMenu.fxml"));
            stage.setTitle("RedVsBlue - MainMenu");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            } catch (IOException io) {
                io.getMessage();
            }
    }

    private void registerMatchInStats(String winner) {
        try {
            databaseQueryHandler.db.saveStats(Red_Player_Name.textProperty().get(), winner == "Red");
            databaseQueryHandler.db.saveStats(Blue_Player_Name.textProperty().get(), winner == "Blue");
        } catch (IOException io) {
            io.getMessage();
        }
    }
}
