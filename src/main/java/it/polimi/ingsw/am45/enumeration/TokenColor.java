package it.polimi.ingsw.am45.enumeration;
import java.io.Serializable;

/**
 * Enumeration of the colors of the tokens to be assigned to each the players.
 */
public enum TokenColor implements Serializable{
    GREEN("GREEN"),
    BLUE("BLUE"),
    YEllOW("YELLOW"),
    RED("RED"),;

    /**
     * The color of the token.
     */
    private final String playerColor;

    /**
     * Constructor of the enumeration.
     * @param playerColor the color of the token
     */
    TokenColor(String playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * This method checks if the color is equal to the color passed as parameter.
     * @param color the color to compare
     * @return true if the colors are equal, false otherwise
     */
    public boolean equals(TokenColor color){
        return this == color;
    }

    public String toString() {
        return this.playerColor;
    }

}
