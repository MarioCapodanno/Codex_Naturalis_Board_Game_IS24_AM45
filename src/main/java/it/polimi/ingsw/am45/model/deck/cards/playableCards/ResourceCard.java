package it.polimi.ingsw.am45.model.deck.cards.playableCards;


import it.polimi.ingsw.am45.enumeration.CardColor;

/**
 * Class to manage the Resource Cards in the game
 */
public class ResourceCard extends PlayableCard {

    /**
     * Constructor for the Resource Card
     *
     * @param cardId  id of the card
     * @param images  front and back images of the card
     * @param corners of the card
     */
    public ResourceCard(int cardId, String[] images, String[][] corners, CardColor cardColor) {
        super(cardId, images, corners, cardColor);
        int point = 0;
    }


}
