package it.polimi.ingsw.am45.utilities;


import java.io.Serializable;

/**
 * The class Location is used to represent the location of a card in the grid.
 */
public class Location implements Serializable {
    public final int x;
    public final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the string representation of the location
     */
    public String toString() {
        return "x: " + x + " y: " + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Location loc) {
            return loc.x == x && loc.y == y;
        }
        return false;
    }

    public int hashCode() {
        return x * 100 + y;
    }
}
