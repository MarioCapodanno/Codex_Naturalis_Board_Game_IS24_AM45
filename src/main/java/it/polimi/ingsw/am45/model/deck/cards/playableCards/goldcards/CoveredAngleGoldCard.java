package it.polimi.ingsw.am45.model.deck.cards.playableCards.goldcards;

import it.polimi.ingsw.am45.model.boards.PlayerBoard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;

import java.util.HashMap;

/**
 * Class to manage the Covered Angle Gold Card in the game*
 * IDEA: 1) for each card played by the player (x,y), check if the location around that card are empty:
 * |x-1,y|       |x,y-1|
 * |x,y|
 * |x,y+1|       |x+1,y|
 * 2) if the location checked is empty, count how many corners are covered by placing a card in that location,
 * using the function countCoveredCorners() on that location
 * 3) After repeated the process for all the played cards, we know for each possible next card available location
 * and how many corners are covered by placing a card in that location in a dictionary:
 * {location: coveredCorners}
 */
public class CoveredAngleGoldCard extends GoldCardType {

    private final int points;

    public CoveredAngleGoldCard(int points) {
        this.points = points;
    }

    public int totalPoints(Player player, int points) {
        return 0;
    }

    /**
     * The method calculates the total points made by the player after placing the gold card.
     *
     * @param player   The player that placed the gold card.
     * @param location The location where the card is placed,
     * @return The total points made by the player after placing the gold card.
     */
    public int totalPoints(Player player, Location location) {
        HashMap<Location, Integer> locToNumbOfCoveredCorner = calculateCoveredCornersForLocations(player);
        if (locToNumbOfCoveredCorner.get(location) != null) {
            // Multiply the base points of the card by the number of corners covered by placing a card in that location.
            return getBasePoints() * locToNumbOfCoveredCorner.get(location);
        }
        return 0;
    }

    /**
     * The method calculates how many corners are covered by placing a card in each location around the cards played by the player.
     *
     * @param player The player that is going to play the card.
     * @return A dictionary that maps the location of the card to the number of corners covered by placing a card in that location.
     */
    public HashMap<Location, Integer> calculateCoveredCornersForLocations(Player player) {
        PlayerBoard playerBoard = player.getPlayerBoard();
        HashMap<Location, Integer> locToNumbOfCoveredCorner = new HashMap<>();

        for (PlayableCard card : player.getPlayedCards()) {
            int coveredCorners;
            int x = card.getLocation().getX();
            int y = card.getLocation().getY();

            int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                if (!playerBoard.hasCard(newX, newY)) {
                    coveredCorners = countCoveredCorners(playerBoard, newX, newY);
                    locToNumbOfCoveredCorner.putIfAbsent(new Location(newX, newY), coveredCorners);
                }
            }
        }
        return locToNumbOfCoveredCorner;
    }

    /**
     * The method counts how many corners are covered by the card in coordinate (x,y) placed in the player board.
     *
     * @param playerBoard the player board where the card is placed.
     * @param x           the x coordinate of the card placed.
     * @param y           the y coordinate of the card placed.
     * @return The number of corners covered by the card placed in the player board.
     */
    public int countCoveredCorners(PlayerBoard playerBoard, int x, int y) {
        int coveredCorners = 0;
        int[][] corners = {{0, -1, 3}, {1, 0, 0}, {0, 1, 1}, {-1, 0, 2}};

        for (int[] corner : corners) {
            int newX = x + corner[0];
            int newY = y + corner[1];
            int cornerIndex = corner[2];

            if (playerBoard.hasCard(newX, newY) && !playerBoard.getCardFromLocation(newX, newY).getCorner(cornerIndex).isEmpty()) {
                coveredCorners++;
            }
        }
        return coveredCorners;
    }

    /**
     * @return The number of points on the front face of the card.
     */
    public int getBasePoints() {
        return points;
    }
}