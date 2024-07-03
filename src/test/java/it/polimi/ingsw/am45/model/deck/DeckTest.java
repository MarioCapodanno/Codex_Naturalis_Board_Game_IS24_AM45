package it.polimi.ingsw.am45.model.deck;

import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.exception.InvalidNumberOfPlayersException;
import it.polimi.ingsw.am45.model.Game;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.goldcards.GoldCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    Deck decks;
    Game game;
    Player player1;
    Player player2;

    /**
     * This method initializes the game and player setup before each test.
     * It creates a new deck, game, players, starts the game, and sets up the player's resources.
     */
    @BeforeEach
    void init() {
        decks = new Deck();
        try {
            game = new Game(1100, 2);
        } catch (InvalidNumberOfPlayersException e) {
            throw new RuntimeException(e);
        }
        player1 = new Player("player1", TokenColor.YEllOW);
        player2 = new Player("player2",TokenColor.RED);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();
        decks.getDrawableCards();
        PlayableCard initialCard = game.getDeck().drawInitDeck();
        player1.addPlayedCard(new Location(0, 0), initialCard, game.getScoreBoard(), 1);
        player1.getPlayerBoard().setPlayerBoardResources(new ArrayList<>(Arrays.asList(5, 5, 5, 5, 5, 5, 5)));
    }


    /**
     * This test verifies the functionality of playing different gold cards on the player board.
     * It also checks if the size of the played cards list is as expected after the cards are played.
     */

    @Test
    @DisplayName("Test for playing different gold cards on the player board and check played cards size")
    void playGoldCardTest() {
        int counter = 1;
        Location[] locations = {
                new Location(1, 0),
                new Location(0, 1),
                new Location(-1, 0),
                new Location(0, -1)
        };

        for (int i = 0; i < 4; i++) {
            player1.addPlayedCard(locations[i], player1.getHand().getHandCards().get(0), game.getScoreBoard(), 1);
            GoldCard goldCard = game.getDeck().drawGoldDeck();
            counter++;
            try {
                player1.getHand().addCard(goldCard);
            } catch (IllegalArgumentException e) {
                System.out.println("Card already in hand");
                counter--;
            }
        }

        assertEquals(counter, player1.getPlayedCards().size());

    }

    /**
     * This test verifies the functionality of playing different resource cards on the player board.
     * It also checks if the size of the played cards list is as expected after the cards are played.
     */
    @Test
    @DisplayName("Test to check if the method drawGoldDeck returns a gold card object")
    void drawGoldDeckTest() {
        for (int i = 0; i < decks.deckGold.size(); i++) {
            GoldCard goldCard = decks.drawGoldDeck();
            System.out.println(goldCard.getCardId());
            assertTrue(goldCard instanceof GoldCard);
        }
    }

    //TO DO: If you play a card on a location that is already occupied, it should return error
    @Test
    @DisplayName("Test to check if the method drawResourceDeck returns a PlayableCard object")
    void drawResourceDeck() {
        for (int i = 0; i < decks.deckResource.size(); i++) {
            PlayableCard resourceCard = decks.drawResourceDeck();
            System.out.println(resourceCard.getCardId());
            assertTrue(resourceCard instanceof PlayableCard);
        }
    }

    @Test
    @DisplayName("Test to check if the method drawInit returns a PlayableCard object")
    void drawInitDeck() {
        for (int i = 0; i < decks.deckInit.size(); i++) {
            PlayableCard initialCard = decks.drawInitDeck();
            System.out.println(initialCard.getCardId());
            //check if all the corner of initialCard are not empty
            for (int j = 0; j < 4; j++) {
                assertNotEquals("", initialCard.getCorner(j));
            }

        }
    }


    @Test
    @DisplayName("Test to check if the method shuffle shuffles the decks")
    void shuffleTest() {
        Deck decks2 = new Deck();
        decks2.shuffle();
        assertNotEquals(decks.deckResource, decks2.deckResource);
        assertNotEquals(decks.deckGold, decks2.deckGold);
        assertNotEquals(decks.deckInit, decks2.deckInit);
    }

    @Test
    @DisplayName("Test to check if the method getDrawableCards returns a list of PlayableCard objects")
    void getDrawableCardsTest() {
        assertNull(decks.getDrawableCards());
    }

    /*
    @Test
    @DisplayName("Test to check that if you play a card in a occupied position you get an error")

     */

}

