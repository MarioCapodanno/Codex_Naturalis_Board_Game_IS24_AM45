package it.polimi.ingsw.am45.model;

import it.polimi.ingsw.am45.enumeration.GameState;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.exception.InvalidNumberOfPlayersException;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ObjectiveCard;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ObjectiveCardType;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ResourceObjectiveCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    private final int gameID = 1;
    Player player1;
    Player player2;

    @BeforeEach
    void setUp() {
        try {
            game = new Game(1, 4);
        } catch (InvalidNumberOfPlayersException e) {
            throw new RuntimeException(e);
        }
        player1 = new Player("player1", TokenColor.YEllOW);
        player2 = new Player("player2", TokenColor.RED);
        game.addPlayer(player2);
        game.addPlayer(new Player("player3", TokenColor.BLUE));
        game.addPlayer(new Player("player4", TokenColor.GREEN));
        game.addPlayer(player1);
        game.startGame();
        ObjectiveCardType ResourceObjectiveCard = new ResourceObjectiveCard(new int[]{0, 0, 0, 0, 2, 0, 0}, 2);
        ObjectiveCard resourceObjectiveCard = new ObjectiveCard("401.png", 401, ResourceObjectiveCard);
        for (Player player : game.getPlayers()) {
            player.getHand().setupObjChoice(resourceObjectiveCard, resourceObjectiveCard);
            player.getHand().setObj(true);
        }
    }

    @Test
    @DisplayName("Test for checking the number of winner players")
    void testNumberOfWinnerPlayer() {
        player1.advancePlayerPos(21);
        player2.advancePlayerPos(21);
        game.nextTurn();
        game.nextTurn();
        game.nextTurn();
        game.nextTurn();
        assertEquals(2, game.getWinnerPlayer().size());
    }
    @Test
    void testGetPlayerNicknames() {
        Stack<String> expectedNicknames = new Stack<>();
        expectedNicknames.add("player2");
        expectedNicknames.add("player3");
        expectedNicknames.add("player4");
        expectedNicknames.add("player1");

        Stack<String> actualNicknames = game.getPlayerNicknames();
        assertEquals(expectedNicknames, actualNicknames);
    }
    @Test
    void MoreThanFourPlayers() {
    assertThrows(InvalidNumberOfPlayersException.class, () -> new Game(1, 5));
    }

    @Test
    void testContainsNickname() {
        assertTrue(game.containsNickname("player1"));
        assertTrue(game.containsNickname("player2"));
        assertTrue(game.containsNickname("player3"));
        assertTrue(game.containsNickname("player4"));
        assertFalse(game.containsNickname("player5"));
    }

    @Test
    void testPlayCard() {
        Location location = new Location(0, 0); // replace with actual location
        assertTrue(game.playCard(0, location, "player1"));
        assertFalse(game.playCard(1, location, "player1"));
        /*
        3 is the initial position card so it must return false
         */
        assertFalse(game.playCard(3, location, "player1"));
    }
    @Test
    void testCheckState() {
        game.checkState();
        // Verify that the state of all players is not GameState.GAME
        boolean allInGameStateGame = game.getPlayers().stream()
                .allMatch(player -> player.getState() == GameState.GAME);
        assertFalse(allInGameStateGame);
    }
    @Test
    void testGetGameID() {
        assertEquals(gameID, game.getGameID());
    }
    @Test
    void testIsReadyToStart() {
        assertTrue(game.isReadyToStart());
    }

    @Test
    void testGetScoreBoard() {
        assertNotNull(game.getScoreBoard());
    }

    @Test
    void testGetDeck() {
        assertNotNull(game.getDeck());
    }

    @Test
    void testIsStarted() {
        assertTrue(game.isStarted());
    }

    @Test
    void testGetCurrentPlayer() {
        assertEquals(player2, game.getCurrentPlayer());
    }
}

