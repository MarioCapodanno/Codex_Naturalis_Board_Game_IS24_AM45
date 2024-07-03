package it.polimi.ingsw.am45.model.deck.cards.playableCards;

import it.polimi.ingsw.am45.enumeration.CardColor;


/**
 * Class to manage the Initial Cards in the game
 */
public class InitialCard extends PlayableCard {

    /**
     * Constructor for the Initial Card
     *
     * @param cardId  id of the card
     * @param images  front and back images of the card
     * @param corners of the card
     */
    public InitialCard(int cardId, String[] images, String[][] corners, CardColor cardColor) {
        super(cardId, images, corners, cardColor);
    }
}
