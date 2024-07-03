package it.polimi.ingsw.am45.model.deck.cards.objectiveCards;

import it.polimi.ingsw.am45.model.player.Player;

/**
 * The ObjectiveCardType class is an abstract class that defines the methods for an objective card in the game.
 * It is extended by concrete classes that implement these methods for specific types of objective cards.
 * No test is needed for this class as it is an abstract class.
 */
public abstract class ObjectiveCardType {

    /**
     * Calculates the total points for a player based on this objective card.
     * This method is abstract and should be implemented in the concrete classes that extend this class.
     * @param player The player for whom to calculate the total points.
     * @return The total points for the player.
     */
    public abstract int totalPoints(Player player);

    /**
     * Checks if the objective is completed for a player.
     * This method is abstract and should be implemented in the concrete classes that extend this class.
     * @param player The player for whom to check if the objective is completed.
     * @return True if the objective is completed, false otherwise.
     */
    public abstract boolean isObjectiveCompleted(Player player);

    /**
     * Returns the points of this objective card.
     * This method is abstract and should be implemented in the concrete classes that extend this class.
     * @return The points of this objective card.
     */
    public abstract int getCardPoints();
}