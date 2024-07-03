package it.polimi.ingsw.am45.model.deck.cards.objectiveCards;

import it.polimi.ingsw.am45.model.deck.cards.Card;
import it.polimi.ingsw.am45.model.player.Player;

/**
 * Class to manage the Objective Cards in the game
 */
public class ObjectiveCard extends Card {


    private final String image;

    private final ObjectiveCardType objectiveCardType;

    /**
     * Constructor
     *
     * @param cardId id of the objective card
     */
    public ObjectiveCard(String image, int cardId, ObjectiveCardType objectiveCardType) {
        this.image = image;
        this.setCardId(cardId);
        this.objectiveCardType = objectiveCardType;
    }

    /**
     * Method to get the total point made by the player with the objective of this card
     *
     * @param player to check if the objective is completed by the player
     * @return the total points made by the player
     */
    public int getPointsMade(Player player) {
        if (objectiveCardType.isObjectiveCompleted(player)) {
            return objectiveCardType.totalPoints(player);
        } else return 0;
    }

    /**
     * @return the points on the face of the card
     */
    public int getCardPoints() {
        return this.objectiveCardType.getCardPoints();
    }

    public String getImage() {
        return image;
    }
}