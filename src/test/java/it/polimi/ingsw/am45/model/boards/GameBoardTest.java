package it.polimi.ingsw.am45.model.boards;

import it.polimi.ingsw.am45.model.deck.Deck;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class GameBoardTest {

    private GameBoard gameBoard;
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
        gameBoard = new GameBoard(deck);
    }

    @Test
    void initializeMarketWithCorrectNumberOfMarketCards() {
        List<String> marketCards = gameBoard.getMarketCards();
        assertEquals(8, marketCards.size());
    }

    @Test
    void getCardFromMarket() {
        PlayableCard card = gameBoard.getCard(0);
        assertNotNull(card);
    }

    @Test
    void getCardFromMarketWithInvalidId() {
        assertThrows(IndexOutOfBoundsException.class, () -> gameBoard.getCard(10));
    }

    @Test
    void updateDeckCardsCorrectNumberOfMarketCards() {
        gameBoard.updateDeckCards(0);
        assertEquals(8, gameBoard.getMarketCards().size());
    }

    @Test
    void updateDeckCardsWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> gameBoard.updateDeckCards(10));
    }

    @Test
    void updateDeckCardsWithValidId() {
        int initialSize = gameBoard.getMarketCards().size();
        gameBoard.updateDeckCards(2);
        assertEquals(initialSize, gameBoard.getMarketCards().size());
    }

}

