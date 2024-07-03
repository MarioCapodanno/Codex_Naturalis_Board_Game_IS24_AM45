package it.polimi.ingsw.am45.model.deck.cards.playableCards.goldcards;

import it.polimi.ingsw.am45.model.player.Player;

public class InstantGoldCard extends GoldCardType {

    public InstantGoldCard(int points) {
    }

    /**
     * The method calculates the total points made by the player after placing the gold card.
     *
     * @param player the player that placed the gold card and that will gain the points
     * @param points the number of points reported on the face of the gold card
     * @return
     */
    @Override
    public int totalPoints(Player player, int points) {
        return points;
    }

}
