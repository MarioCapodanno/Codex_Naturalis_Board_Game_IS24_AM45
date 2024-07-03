package it.polimi.ingsw.am45.model.deck.cards.objectiveCards;


import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.model.boards.PlayerBoard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;

import java.util.ArrayList;

/**
 * Class of the diagonal objective card, 3 the cards of the same color in a diagonal pattern give points to the player
 */
public class DiagonalObjectiveCard extends ObjectiveCardType {

    private final CardColor objectiveColor;
    /**
     * IDEA: the direction of the L-shape can be changed based on the objective card
     * left_to_right -> right_to_left
     * |x,y-1|    |  |x-1,y|        |
     * |x,y|         |     |x,y|       |
     * |x,y+1|           |        |x+1,y|      |
     */
    private final String shapeDirection;
    private final int points;

    public DiagonalObjectiveCard(CardColor objectiveColor, int points, String shapeDirection) {
        this.objectiveColor = objectiveColor;
        this.shapeDirection = shapeDirection;
        this.points = points;
    }

    @Override
    public int totalPoints(Player player) {
        if (isObjectiveCompleted(player)) {
            return numberOfDiagonalPattern(player) * getCardPoints();
        }
        return 0;
    }

    @Override
    public boolean isObjectiveCompleted(Player player) {
        return numberOfDiagonalPattern(player) != 0;
    }

    /**
     * Methot to get the number of diagonal patterns completed by the player checking the player board
     *
     * @param player player to check if the diagonal objective is completed
     * @return the number of diagonal patterns completed by the player
     */

    public int numberOfDiagonalPattern(Player player) {
        ArrayList<PlayableCard> matchedColorCardsPlayed = new ArrayList<>();
        PlayerBoard playerBoard = player.getPlayerBoard();
        int setCompleted = 0;
        // get all the cards played by the player with the same color of the objective card
        // no need to check card with other colors
        for (PlayableCard card : player.getPlayedCards()) {
            if (card.getCardColor().equals(objectiveColor)) {
                matchedColorCardsPlayed.add(card);
            }
        }

        for (int i = 0; i < matchedColorCardsPlayed.size(); i++) {
            PlayableCard card = matchedColorCardsPlayed.get(i);
            Location currLoc = card.getLocation();
            int currLocX = currLoc.getX();
            int currLocY = currLoc.getY();

            if (shapeDirection.equals("left_to_right")) {
                if (checkCardColor(playerBoard, currLocX, currLocY - 1) && checkCardColor(playerBoard, currLocX, currLocY + 1)) {
                    matchedColorCardsPlayed.remove(i + 1);
                    setCompleted++;
                }
            } else if (shapeDirection.equals("right_to_left")) {
                if (checkCardColor(playerBoard, currLocX - 1, currLocY) && checkCardColor(playerBoard, currLocX + 1, currLocY)) {
                    setCompleted++;
                }
            }
        }

        return setCompleted;

    }

    public int getCardPoints() {
        return points;
    }

    private boolean checkCardColor(PlayerBoard playerBoard, int x, int y) {
        try {
            return playerBoard.hasCard(x, y) && playerBoard.getCardFromLocation(x, y).getCardColor().equals(objectiveColor);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
