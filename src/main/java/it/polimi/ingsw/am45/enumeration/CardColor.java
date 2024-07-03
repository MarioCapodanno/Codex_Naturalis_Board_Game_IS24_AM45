package it.polimi.ingsw.am45.enumeration;

/**
 * Colors of the cards in the game
 */
public enum CardColor {
    GREEN,
    BLUE,
    VIOLET,
    RED,
    NONE;

    /**
     * This method checks if the color is equal to the color passed as parameter.
     * @param color the color to compare
     * @return true if the colors are equal, false otherwise
     */
    public boolean equals(CardColor color) {
        return this == color;
    }

}

