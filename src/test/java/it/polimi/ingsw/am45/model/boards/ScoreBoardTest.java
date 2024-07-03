package it.polimi.ingsw.am45.model.boards;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.exception.InvalidNumberOfPlayersException;
import it.polimi.ingsw.am45.model.Game;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreBoardTest {

    private Game game;
    private ScoreBoard scoreBoard;
    private PlayableCard card1;
    private PlayableCard card2;
    Player player1;
    Player player2;
    private Location location01, location02;

    @BeforeEach
    void setUp() {
        try {
            game = new Game(1, 4);
        } catch (InvalidNumberOfPlayersException e) {
            throw new RuntimeException(e);
        }
        player1 = new Player("player1", TokenColor.YEllOW);
        player2 = new Player("player2", TokenColor.RED);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();
        game.startGame();
        PlayableCard initialCard = game.getDeck().drawInitDeck();
        player1.addPlayedCard(new Location(0, 0), initialCard, game.getScoreBoard(), 1);
        scoreBoard = new ScoreBoard(2);
        scoreBoard = new ScoreBoard(2);
        location01 = new Location(1, 0);
        location02 = new Location(-1, 0);
        card1 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"FUNGI", "EMPTY", "", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.NONE);
        card2 = new PlayableCard(1102, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"FUNGI", "EMPTY", "", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.NONE);
        card1.setInstantPoints(3); //setto i punti della carta
        card2.setInstantPoints(3); //setto i punti della carta
        card1.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        card2.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
    }

    @Test
    void constructorThrowsExceptionWhenNumPlayersIsLessThanTwo() {
        assertThrows(IllegalArgumentException.class, () -> new ScoreBoard(1));
    }

    @Test
    void constructorThrowsExceptionWhenNumPlayersIsGreaterThanFour() {
        assertThrows(IllegalArgumentException.class, () -> new ScoreBoard(5));
    }

    @Test
    void updateScoreBoardWhenScoreIsValid() {
        scoreBoard.updateScoreBoard(0, 10);
        assertEquals(10, scoreBoard.getScore(0));
    }

    @Test
    void updateScoreBoardThrowsExceptionWhenPlayerNumberIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.updateScoreBoard(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.updateScoreBoard(2, 10));
    }

    @Test
    void updateScoreBoardThrowsExceptionWhenScoreIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.updateScoreBoard(0, -1));
        assertThrows(IllegalArgumentException.class, () -> scoreBoard.updateScoreBoard(0, 30));
    }

    //chek if the score is uptated correctly
    @Test
    void printScoreBoardPrintsCorrectOutputForTwoPlayers() {
        scoreBoard.updateScoreBoard(0, 10);
        scoreBoard.updateScoreBoard(1, 20);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        scoreBoard.printScoreBoard();

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("Player 0: ");
        for (int i = 0; i < 30; i++) {
            if (i == 10) {
                expectedOutput.append(i).append(":[X] |\n");
            } else {
                expectedOutput.append(i).append(":[] |");
            }
        }
        expectedOutput.append("\nPlayer 1: ");
        for (int i = 0; i < 30; i++) {
            if (i == 20) {
                expectedOutput.append(i).append(":[X] |\n");
            } else {
                expectedOutput.append(i).append(":[] |");
            }
        }
        expectedOutput.append("\n");

        assertEquals(expectedOutput.toString(), outContent.toString());
    }

    @Test
    void printScoreBoardPrintsCorrectOutputForNoScores() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        scoreBoard.printScoreBoard();

        StringBuilder expectedOutput = new StringBuilder();
        for (int player = 0; player < 2; player++) {
            expectedOutput.append("Player ").append(player).append(": ");
            for (int i = 0; i < 30; i++) {
                if (i == 0) {
                    expectedOutput.append(i).append(":[X] |\n");
                } else {
                    expectedOutput.append(i).append(":[] |");
                }
            }
            expectedOutput.append("\n");
        }

        assertEquals(expectedOutput.toString(), outContent.toString());
    }
    //controllo che quando il player aggiorna il suo punteggio, si aggiorni anche quello sulla scoreboard

    @Test
    void addPlayedCardTest() {
        player1.addPlayedCard(location01, card1, game.getScoreBoard(), 0);
        player1.addPlayedCard(location02, card2, game.getScoreBoard(), 0);


        //se aggiorno manualmente la scoreboard il test va
        //scoreBoard.updateScoreBoard(1, 3);

        assertEquals(player1.getScore(), game.getScoreBoard().getScore(0));
    }
}