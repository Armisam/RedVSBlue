package redvsblue.game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import redvsblue.repository.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is responsible for controlling the UI of mainMenu.fxml scene of the game.
 */

public class mainMenuController {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private void initialize() {
        createStatItems();
        logger.info("Main menu loaded.");
    }

    private void createStatItems() {
        try {
            Place.setCellValueFactory(new PropertyValueFactory<>("Place"));
            Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
            MatchesPlayed.setCellValueFactory(new PropertyValueFactory<>("Matches_Played"));
            Winrate.setCellValueFactory(new  PropertyValueFactory<>("Winrate"));


            List<ranklistRecord> stats = databaseQueryHandler.db.loadStats();
            stats.sort(Comparator.comparingInt(ranklistRecord::getPlace).thenComparing(ranklistRecord::getMatches_played));
            ObservableList<tableItem> tableItems = FXCollections.observableArrayList();
            for (int i = 0; i < stats.size(); i++) {
                tableItems.add(new tableItem(stats.get(i).getPlace(), stats.get(i).getName(), (int)stats.get(i).getMatches_played(), stats.get(i).getWinrate() * 100));
            }

            tbData.setItems(tableItems);
        } catch (IOException io) {
            io.getMessage();
        }
    }
    @FXML
    private VBox Menu_Buttons_Box;

    @FXML
    private VBox Player_Names_Box;

    @FXML
    private VBox Ranklist_Box;

    @FXML
    private TableView<tableItem> tbData;

    @FXML
    private TableColumn<tableItem, Integer> Place;

    @FXML
    private TableColumn<tableItem, String> Name;

    @FXML
    private TableColumn<tableItem, Double> MatchesPlayed;

    @FXML
    private TableColumn<tableItem, Double> Winrate;


    @FXML
    private Button Start_Button;


    @FXML
    private void clickOnStart(MouseEvent event) {
        Menu_Buttons_Box.setVisible(false);
        Player_Names_Box.setVisible(true);
    }


    @FXML
    private Button Statistics_Button;

    @FXML
    private void clickOnStats(MouseEvent event) {
        Menu_Buttons_Box.setVisible(false);
        Ranklist_Box.setVisible(true);
    }


    @FXML
    private Button Quit_Button;

    @FXML
    private void clickOnQuit(MouseEvent event) {
        Platform.exit();
    }


    @FXML
    private Button Play_Button;

    @FXML
    private TextField Red_Player_Name;

    @FXML
    private TextField Blue_Player_Name;

    @FXML
    private void clickOnPlay(MouseEvent event) throws IOException {
        if (Red_Player_Name.getText().length() != 0 && Blue_Player_Name.getText().length() != 0 &&  Red_Player_Name.getText() != Blue_Player_Name.getText()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/inGame.fxml"));
            Parent root = fxmlLoader.load();
            gameController gamecontroller = fxmlLoader.getController();
            gamecontroller.setredName(Red_Player_Name.getText());
            gamecontroller.setblueName(Blue_Player_Name.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Game: " + Red_Player_Name.getText() + " vs " + Blue_Player_Name.getText());
            stage.show();

            return; // avoid using else state
        }
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Player names are incorrect!");
        logger.warn("Player names are incorrect!");
        a.show();
    }


    @FXML
    private Button Back_From_Player_Names;

    @FXML
    private void clickOnBackFromPlayerNames(MouseEvent event) {
        Menu_Buttons_Box.setVisible(true);
        Player_Names_Box.setVisible(false);
    }


    @FXML
    private Button Back_From_Ranklist;

    @FXML
    private void clickOnBackFromRanklist(MouseEvent event) {
        Menu_Buttons_Box.setVisible(true);
        Ranklist_Box.setVisible(false);
    }
}
