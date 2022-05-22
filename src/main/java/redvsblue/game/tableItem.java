package redvsblue.game;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is responsible for containing of a table item at the ranklist of the mainMenu.fxml scene.
 */

public class tableItem {

    private SimpleIntegerProperty place;
    private SimpleStringProperty name;
    private SimpleIntegerProperty matchesPlayed;
    private SimpleDoubleProperty winrate;

    /**
     * The constructor of tableItem class which initializes properties.
     * @param place place property of a table item
     * @param name name property of a table item
     * @param matchesPlayed matchesPlayed property of a table item
     * @param winrate winrate property of a table item
     */

    public tableItem(Integer place, String name, Integer matchesPlayed, Double winrate) {
        this.place = new SimpleIntegerProperty(place);
        this.name = new SimpleStringProperty(name);
        this.matchesPlayed = new SimpleIntegerProperty(matchesPlayed);
        this.winrate = new SimpleDoubleProperty(winrate);
    }

    /**
     * This method gets the place property of a table item.
     * @return the place property of a table item
     */

    public int getPlace() {
        return place.get();
    }

    /**
     * This method sets the place property of a table item.
     *
     * @param place place property to set
     */

    public void setPlace(int place) {
        this.place = new SimpleIntegerProperty(place);
    }

    /**
     * This method gets the name property of a table item.
     * @return the name property of a table item
     */

    public String getName() {
        return name.get();
    }

    /**
     * This method sets the name property of a table item.
     *
     * @param name name property to set
     */

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * This method gets the matchesPlayed property of a table item.
     * @return the matchesPlayed property of a table item
     */

    public int getMatches_Played() {
        return matchesPlayed.get();
    }

    /**
     * This method sets the matchesPlayed property of a table item.
     *
     * @param matchesPlayed matchesPlayed property to set
     */

    public void setMatches_Played(Integer matchesPlayed) {
        this.matchesPlayed = new SimpleIntegerProperty(matchesPlayed);
    }

    /**
     * This method gets the winrate property of a table item.
     * @return the winrate property of a table item
     */

    public Double getWinrate() {
        return winrate.get();
    }

    /**
     * This method sets the winrate property of a table item.
     *
     * @param winrate winrate property to set
     */

    public void setWinrate(Double winrate) {
        this.winrate = new SimpleDoubleProperty(winrate);
    }
}