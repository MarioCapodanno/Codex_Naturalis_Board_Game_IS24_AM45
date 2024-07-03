package it.polimi.ingsw.am45.model.boards;


import it.polimi.ingsw.am45.model.deck.Deck;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ObjectiveCard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;

import java.util.ArrayList;
import java.util.List;

/**
 * GameBoard for Common objectives and points
 */
public class GameBoard {
    private final Deck deck;
    private final List<PlayableCard> marketCards;
    private final List<ObjectiveCard> marketObjectiveCards;

    public GameBoard(Deck deck) {
        this.deck = deck;
        this.marketCards = new ArrayList<>();
        this.marketObjectiveCards = new ArrayList<>();
        initializeMarket();
        initializeObjective();
    }

    /**
     * Initialize the Market with 3 cards from resource deck and 3 cards from gold deck
     */
    private void initializeMarket() {

        for (int i = 0; i < 6; i++) {
            if (i < 3) {
                marketCards.add(i, deck.drawResourceDeck());

            } else {
                marketCards.add(i, deck.drawGoldDeck());

            }
        }


    }

    /**
     * Initialize the Market with 2 objective cards
     */
    private void initializeObjective() {

        for (int i = 0; i < 2; i++) {
            marketObjectiveCards.add(i, deck.drawObjectiveDeck());
        }
    }

    /**
     * Get the card with the specified id from the Market
     *
     * @param id the id of the card
     * @return the object PlayableCard with the specified id as attribute
     */
    public PlayableCard getCard(int id) {
        return marketCards.get(id);
    }

    /**
     * Update the deck of cards removing the card with the specified id
     *
     * @param id the id of the card to be removed
     */
    public void updateDeckCards(int id) {
        if (id >= 0 && id < marketCards.size()) {
            marketCards.remove(id);
            if (id < 3) {
                marketCards.add(id, deck.drawResourceDeck());
            } else {
                marketCards.add(id, deck.drawGoldDeck());
            }

        } else throw new IllegalArgumentException("Invalid id");
    }

    /**
     * @return the list of Market cards as strings
     */
    public List<String> getMarketCards() {
        List<String> marketCards = new ArrayList<>();
        for (PlayableCard card : this.marketCards) {
            marketCards.add(card.getCurrFace());
        }
        for (ObjectiveCard card : this.marketObjectiveCards) {
            marketCards.add(card.getImage());
        }
        return marketCards;
    }
    public Boolean checkEmptyMarket() {
        for (PlayableCard card : marketCards) {
            if (!card.getCurrFace().equals("end.png")) {
                return false;
            }
        }
        return true;
    }
    public List<ObjectiveCard> getCommonObjectiveCard() {
        return this.marketObjectiveCards;
    }
}
