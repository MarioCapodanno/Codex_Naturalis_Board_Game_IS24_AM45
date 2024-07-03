package it.polimi.ingsw.am45.model.deck.cards.playableCards.goldcards;

import it.polimi.ingsw.am45.model.boards.PlayerBoard;
import it.polimi.ingsw.am45.model.player.Player;

import java.util.List;

public class ResourceGoldCard extends GoldCardType {

    /**
     * Resources required to complete this gold card goal.
     * Are enumerated like the resource class enum
     */
    private final int[] resourcesRequired;

    /**
     * Constructor of the class ResourceGoldCard.
     *
     * @param resourcesRequired the resources required to complete the gold objective
     */
    public ResourceGoldCard(int[] resourcesRequired) {
        this.resourcesRequired = resourcesRequired;
    }

    /**
     * The method calculates the total points made by the player after placing the gold card.
     *
     * @param player the player that placed the gold card and that will gain the points
     * @param points the number of points reported on the face of the gold card
     * @return The total points made by the player after placing the gold card.
     */
    public int totalPoints(Player player, int points) {
        int numberOfSetsCompleted = numberOfSetCompleted(player.getPlayerBoard());
        if (numberOfSetsCompleted != 0) {
            //calculate points made by the player
            System.out.println("Player " + player.getNickname() + " has gained " + points * numberOfSetsCompleted + " points.");
            return points * numberOfSetsCompleted + 1;
        } else {
            System.out.println("Player " + player.getNickname() + " has gained 1 points.");
            return 1;
        }
    }

    /**
     * Method to get the number of resource sets completed by the player checking the player board
     *
     * @param playerBoard grid to check if the gold card goal is completed
     * @return the number of resource sets completed by the player
     */
    public int numberOfSetCompleted(PlayerBoard playerBoard) {
        List<Integer> playerBoardResources = playerBoard.getResources();
        int setsCompleted = Integer.MAX_VALUE;
        //check if the player has the resources required to complete the gold card goal.
        for (int i = 0; i < resourcesRequired.length; i++) {
            if (resourcesRequired[i] > 0) {
                setsCompleted = Math.min(setsCompleted, playerBoardResources.get(i) / resourcesRequired[i]);
            }
        }
        return setsCompleted;
    }
}
