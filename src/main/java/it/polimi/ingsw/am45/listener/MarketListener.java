package it.polimi.ingsw.am45.listener;

import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;

import java.util.List;

/**
 *
 * Listener for communication between the Market view and the model
 */
public interface MarketListener {

    /**
     * Get the list of the cards in the Market for everyone
     * @return  list of the cards in the Market.
     */
    List<PlayableCard> getDrawableCards();


    /**
     * Select the card in the id position
     * @param id position of the card in the Market
     */
    void selectCardClick(int id);

}
