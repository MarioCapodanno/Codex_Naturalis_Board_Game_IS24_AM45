package it.polimi.ingsw.am45.model.deck.cards.objectiveCards;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.model.boards.PlayerBoard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;


public class LShapeObjectiveCard extends ObjectiveCardType {

    private final CardColor baseCardColor;

    private final CardColor columnCardColor;

    /**
     * IDEA: the direction of the L-shape can be changed based on the objective card
     * -----------------------------------------------------------------------------
     * top_right    ->   top_left     ->  bottom_right ->   bottom_left
     * |                |              |
     * |x,y|     |   |x,y|        |   |x-2,y-1|  |     |x-1,y-2| |
     * |x,y+1|       |      |x+1,y|   |   |x-1,y|    |     |x,y-1|   |
     * |x+1,y+2|    |      |x+2,y+1| |        |x,y| |  |x,y|        |
     * -----------------------------------------------------------------------------
     * bottom_right: x+1,y
     * bottom_left: x, y+1
     * top_right: x,y-1
     * top_left: x-1,y
     */
    private final String shapeDirection;

    private final int points;

    public LShapeObjectiveCard(CardColor baseCardColor, CardColor columnCardColor, String shapeDirection, int points) {
        this.shapeDirection = shapeDirection;
        this.baseCardColor = baseCardColor;
        this.columnCardColor = columnCardColor;
        this.points = points;
    }

    @Override
    public int totalPoints(Player player) {
        if (isObjectiveCompleted(player)) {
            return numberOfLShapePattern(player) * getCardPoints();
        }
        return 0;
    }

    @Override
    public boolean isObjectiveCompleted(Player player) {
        return numberOfLShapePattern(player) != 0;
    }

    /**
     * Method to get the number of L-shape patterns completed by the player checking the player board
     * @param player
     * @return the number of L-shape patterns completed by the player
     */
    public int numberOfLShapePattern(Player player) {
        List<PlayableCard> matchedColorCardsPlayed = new Stack<>();
        PlayerBoard playerBoard = player.getPlayerBoard();
        int setCompleted = 0;
        // get all the cards played by the player with the same color of the upperCard of L-shape
        // no need to check card with other colors
        for (PlayableCard card : player.getPlayedCards()) {
            if (card.getCardColor().equals(baseCardColor)) {
                matchedColorCardsPlayed.add(card);
            }
        }
        for (PlayableCard card : matchedColorCardsPlayed) {
            Location currLoc = card.getLocation();
            int currLocX = currLoc.getX();
            int currLocY = currLoc.getY();
            // Define the possible directions and their corresponding offsets
            HashMap<String, int[][]> directions = new HashMap<>();
            directions.put("topRight", new int[][]{{0, 1}, {1, 2}});
            directions.put("topLeft", new int[][]{{1, 0}, {2, 1}});
            directions.put("bottomRight", new int[][]{{-1, 0}, {-2, -1}});
            directions.put("bottomLeft", new int[][]{{0, -1}, {-1, -2}});

            // Get the offsets for the current shape direction
            int[][] offsets = directions.get(shapeDirection);

            // Check if the card has cards in the specified directions with the same color
            if (playerBoard.hasCard(currLocX + offsets[0][0], currLocY + offsets[0][1]) &&
                    playerBoard.hasCard(currLocX + offsets[1][0], currLocY + offsets[1][1]) &&
                    playerBoard.getCardFromLocation(currLocX + offsets[0][0], currLocY + offsets[0][1]).getCardColor().equals(columnCardColor) &&
                    playerBoard.getCardFromLocation(currLocX + offsets[1][0], currLocY + offsets[1][1]).getCardColor().equals(columnCardColor)) {
                setCompleted++;
            }

        }
        return setCompleted;
    }

    public int getCardPoints() {
        return points;
    }

}