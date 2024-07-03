package it.polimi.ingsw.am45.model.deck.cards;

/**
 * The Card class represents a generic card in the game.
 */
public abstract class Card {
    /**
     * Each card has a unique identifier that distinguishes it from the others.
     */
    private int cardId;

    public int getCardId() {
        return this.cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}