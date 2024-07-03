package it.polimi.ingsw.am45.model.boards;

import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.utilities.CardData;
import it.polimi.ingsw.am45.utilities.Location;

import java.util.*;


/**
 * The Board of every player in the game, contains the cards played by the player and counts their resources.
 */
public class PlayerBoard {

    /**
     * The grid of the player board, every location his identified by a string key that is the concatenation
     * Each location contains or not a single card.
     */
    private final HashMap<String, PlayableCard> grid;
    /**
     * The grid necessary for the view of the player board, contains the face of the card and its position.
     */
    private final Stack<CardData> viewGrid;
    /**
     * The List stores the resources of the player in the following order:
     * ANIMAL, PLANT, INSECT, FUNGI, FEATHER, POTION, SCROLL
     */
    private List<Integer> resources;

    /**
     * Constructor
     */
    public PlayerBoard() {
        grid = new HashMap<>();
        viewGrid = new Stack<>();
        resources = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
    }

    /**
     * @return List of the resources of the card placed on the player board
     */
    public List<Integer> getResources() {
        return resources;
    }

    /**
     * Add the resources of the played card to the player's player board resources.
     *
     * @param resources the resources of the played card
     */
    public void addResources(String[] resources) {
        //il parametro passato sarebbe currCenter e currCorners della carta
        for (String resource : resources) {
            updateResources(resource, 1);
        }
    }


    /**
     * The method checks if the player has enough resources to play the GoldCard.
     *
     * @param requirements requirements of the GoldCard to be played
     * @return true if the player has enough resources, false otherwise
     */
    public boolean checkRequirements(int[] requirements) {
        for (int i = 0; i < requirements.length; i++) {
            if (this.resources.get(i) < requirements[i])
                return false;
        }
        return true;
    }


    /**
     * Place card in the grid of PlayerBoard
     *
     * @param x    position of the card
     * @param y    position of the card
     * @param card card to be placed
     */
    public void placeCard(int x, int y, PlayableCard card) {
        card.setLocation(x, y);
        System.out.println("The card with id: " + card.getCardId() + " placed in (" + x + ", " + y + ")");
        String key = getKey(x, y);
        grid.put(key, card);
        viewGrid.add(new CardData(card.getCurrFace(), new Location(x, y)));
        if (hasCard(x + 1, y))
            updateResources(getCardFromLocation(x + 1, y).getCorner(0), -1);
        if (hasCard(x - 1, y))
            updateResources(getCardFromLocation(x - 1, y).getCorner(2), -1);
        if (hasCard(x, y + 1))
            updateResources(getCardFromLocation(x, y + 1).getCorner(1), -1);
        if (hasCard(x, y - 1))
            updateResources(getCardFromLocation(x, y - 1).getCorner(3), -1);
        System.out.println(grid);
    }

    /**
     * Based on the angle card resource add or subtract the points from the player's resources.
     *
     * @param resource of the angle being checked
     * @param point    how many points to be added or subtracted
     */
    public void updateResources(String resource, int point) {
        switch (resource) {
            case "ANIMAL":
                this.resources.set(0, this.resources.get(0) + point);
                break;
            case "PLANT":
                this.resources.set(1, this.resources.get(1) + point);
                break;
            case "INSECT":
                this.resources.set(2, this.resources.get(2) + point);
                break;
            case "FUNGI":
                this.resources.set(3, this.resources.get(3) + point);
                break;
            case "FEATHER":
                this.resources.set(4, this.resources.get(4) + point);
                break;
            case "BOTTLE":
                this.resources.set(5, this.resources.get(5) + point);
                break;
            case "SCROLL":
                this.resources.set(6, this.resources.get(6) + point);
                break;
        }
    }


    /**
     * Get card from the grid at the location with coordinates x and y
     *
     * @param x position of the card
     * @param y position of the card
     * @return the card in the position x,y
     */
    public PlayableCard getCardFromLocation(int x, int y) {
        String key = getKey(x, y);
        return grid.get(key);
    }

    /**
     * Generate key for the grid to be associated with each couple of coordinates x and y
     *
     * @param x position on the grid
     * @param y position on the grid
     * @return String of the generated key
     */
    private String getKey(int x, int y) {

        return x + "" + y;
    }

    /**
     * Check if there is a card in the position x and y
     *
     * @param x position to be checked.
     * @param y position to be checked.
     * @return true if there is a card in the position, false otherwise.
     */
    public boolean hasCard(int x, int y) {

        return getCardFromLocation(x, y) != null;
    }

    /**
     * Get the grid necessary to create the view.
     *
     * @return the grid of the player board
     */
    public Stack<CardData> getViewGrid() {
        return viewGrid;
    }

    /**
     * Only for test purposes
     *
     * @param resources the resources of the card placed on the player board
     */
    public void setPlayerBoardResources(List<Integer> resources) {
        this.resources = resources;
    }
}
