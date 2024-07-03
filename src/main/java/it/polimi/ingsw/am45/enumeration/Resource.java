package it.polimi.ingsw.am45.enumeration;

/**
 * Enumeration of resources of the cards in the game
 */
public enum Resource {
    EMPTY("EMPTY"),
    ANIMAL("ANIMAL"),
    PLANT("PLANT"),
    INSECT("INSECT"),
    FUNGI("FUNGI"),
    FEATHER("FEATHER"),
    POTION("BOTTLE"),
    SCROLL("SCROLL");

    private final String value;

    Resource(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}