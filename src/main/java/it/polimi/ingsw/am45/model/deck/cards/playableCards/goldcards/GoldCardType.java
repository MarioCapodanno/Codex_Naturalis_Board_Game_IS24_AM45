package it.polimi.ingsw.am45.model.deck.cards.playableCards.goldcards;

import it.polimi.ingsw.am45.model.player.Player;
/**
 No test needed for this class, it is an abstract class
 */

public abstract class GoldCardType {
    /**
     * @param player the player that placed the gold card
     * @param points the number of points reported on the face of the gold card
     * @return the total points made by the player after placing the gold card
     */
    public abstract int totalPoints(Player player, int points);

}
