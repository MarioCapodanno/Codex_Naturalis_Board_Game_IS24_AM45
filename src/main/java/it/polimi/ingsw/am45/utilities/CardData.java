package it.polimi.ingsw.am45.utilities;

import java.io.Serializable;


/**
 * The class Location is used to represent the location of a card in the grid.
 */
public class CardData implements Serializable {

    private final String path;
    private final Location location;

    /**
     * @param path the path of the card
     * @param location the location of the card
     */
    public CardData(String path, Location location) {
        this.path = path;
        this.location = location;
    }
    /**
     * @return location of the card
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return path of the card
     */
    public String getPath() {
        return path;
    }
}

