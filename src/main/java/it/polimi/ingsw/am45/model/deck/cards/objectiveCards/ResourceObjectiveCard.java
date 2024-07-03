package it.polimi.ingsw.am45.model.deck.cards.objectiveCards;

import it.polimi.ingsw.am45.model.boards.PlayerBoard;
import it.polimi.ingsw.am45.model.player.Player;

import java.util.List;

//IDEA: resourceNeeded from JSNON (similar to cardCost in GoldCard):
// { 'ANIMAL': 0, 'INSECT': 3, 'FUNGI': 0, ... }
// or
// [0,3,0,...] if we decide a standard order for resources

/**
 * Class to manage the Resource Objective Cards in the game, for each set of resources required
 * it will reward the player with a certain amount of points for each set completed.
 */
public class ResourceObjectiveCard extends ObjectiveCardType {

    /**
     * Resources required to complete the objective.
     * Are enumerated like the resource class enum
     */
    private final int[] resourcesRequired;
    private final int points;

    /**
     * Constructor
     *
     * @param resourcesRequired resources required to complete the objective
     */
    public ResourceObjectiveCard(int[] resourcesRequired, int points) {
        this.resourcesRequired = resourcesRequired;
        this.points = points;
    }

    /**
     * Method to get the total points made by the player
     *
     * @param player to check if the objective is completed
     * @return the total points made by the player with the objective of this card
     */
    @Override
    public int totalPoints(Player player) {
        if (isObjectiveCompleted(player)) {
            //calculate points made by the player
            return this.getCardPoints() * numberOfSetCompleted(player);
        } else return 0;
    }

    @Override
    public boolean isObjectiveCompleted(Player player) {
        return numberOfSetCompleted(player) != 0;
    }

    /**
     * Method to get the number of resource sets completed by the player checking the player board
     * @param player player to check if the resource objective is completed
     * @return the number of resource sets completed by the player
     */
    public int numberOfSetCompleted(Player player) {
        PlayerBoard playerBoard = player.getPlayerBoard();
        List<Integer> playerBoardResources = playerBoard.getResources();
        int setsCompleted = Integer.MAX_VALUE;
        //check if the player has the resources required to complete the objective
        for (int i = 0; i < resourcesRequired.length; i++) {
            if (resourcesRequired[i] > 0) {
                setsCompleted = Math.min(setsCompleted, playerBoardResources.get(i) / resourcesRequired[i]);
            }
        }
        return setsCompleted;
    }

    public int getCardPoints() {
        return points;
    }

}
