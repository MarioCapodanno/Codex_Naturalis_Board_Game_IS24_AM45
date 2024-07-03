package it.polimi.ingsw.am45.model.deck.cards.objectiveCards;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.exception.InvalidNumberOfPlayersException;
import it.polimi.ingsw.am45.model.Game;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectiveCardTest {

    Game game;

    Player player1;
    Player player2;

    @BeforeEach
    void init() {
        try {
            game = new Game(1100, 2);
        } catch (InvalidNumberOfPlayersException e) {
            throw new RuntimeException(e);
        }
        player1 = new Player("player1", TokenColor.YEllOW);
        player2 = new Player("player2", TokenColor.RED);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.startGame();
        PlayableCard initialCard = game.getDeck().drawInitDeck();
        player1.addPlayedCard(new Location(0, 0), initialCard, game.getScoreBoard(), 1);
        player1.getPlayerBoard().setPlayerBoardResources(new ArrayList<>(Arrays.asList(5, 5, 5, 5, 5, 5, 5)));

    }

    @Test
    @DisplayName("Test for checking the correct calculation of the points of the resource objective card")
    void checkResourceObjectiveCardPointsCalculation() {
        ObjectiveCardType ResourceObjectiveCard = new ResourceObjectiveCard(new int[]{0, 0, 0, 0, 2, 0, 0}, 2);
        ObjectiveCard resourceObjectiveCard = new ObjectiveCard("401.png", 401, ResourceObjectiveCard);
        player1.getPlayerBoard().setPlayerBoardResources(new ArrayList<>(Arrays.asList(0, 0, 1, 3, 7, 2, 1)));
        assertEquals(resourceObjectiveCard.getCardPoints() * 3, resourceObjectiveCard.getPointsMade(player1));

    }

    @Test
    @DisplayName("Test for checking the correct calculation of the points of the diagonal objective card")
    void checkDiagonalObjectiveCardPointsCalculation() {
        ObjectiveCardType DiagonalObjectiveCard = new DiagonalObjectiveCard(CardColor.RED, 2, "left_to_right");
        PlayableCard redCard1 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.RED);
        PlayableCard redCard2 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.RED);
        PlayableCard redCard3 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.RED);
        PlayableCard redCard4 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.RED);
        PlayableCard redCard5 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.RED);
        PlayableCard redCard6 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.RED);

        redCard1.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        redCard2.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        redCard3.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        redCard4.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        redCard5.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        redCard6.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        player1.addPlayedCard(new Location(0, 1), redCard1, game.getScoreBoard(), 1);
        player1.addPlayedCard(new Location(0, 2), redCard2, game.getScoreBoard(), 1);
        player1.addPlayedCard(new Location(0, 3), redCard3, game.getScoreBoard(), 1);
        player1.addPlayedCard(new Location(0, 4), redCard4, game.getScoreBoard(), 1);
        player1.addPlayedCard(new Location(0, 5), redCard5, game.getScoreBoard(), 1);
        player1.addPlayedCard(new Location(0, 6), redCard6, game.getScoreBoard(), 1);

        System.out.println(DiagonalObjectiveCard.totalPoints(player1));
        assertEquals(4, DiagonalObjectiveCard.totalPoints(player1));

    }

    /**
     * Test for checking the correct calculation of the points of the column objective card
     */
    @Test
    @DisplayName("Test for checking the correct calculation of the points of the l_shape objective card")
    void checkLShapeObjectiveCardPointsCalculation() {
        ObjectiveCardType LShapeObjectiveCard = new LShapeObjectiveCard(CardColor.RED, CardColor.BLUE, "bottomRight", 2);
        PlayableCard redCard1 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.RED);
        PlayableCard blueCard2 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.BLUE);
        PlayableCard blueCard3 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"EMPTY", "EMPTY", "EMPTY", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.BLUE);

        redCard1.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        blueCard2.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        blueCard3.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}
        });

        player1.addPlayedCard(new Location(0, 1), redCard1, game.getScoreBoard(), 1);
        player1.addPlayedCard(new Location(-1, 1), blueCard2, game.getScoreBoard(), 1);
        player1.addPlayedCard(new Location(-2, 0), blueCard3, game.getScoreBoard(), 1);

        System.out.println(LShapeObjectiveCard.totalPoints(player1));
        assertEquals(2, LShapeObjectiveCard.totalPoints(player1));
    }


    /**
     * Test for checking the correct calculation of the points of the row objective card
     */

    @Test
    @DisplayName("Test for checking the correct png is associated with the objective card")
    void checkObjectiveCardImage() {
        ObjectiveCard objectiveCard = game.getDeck().drawObjectiveDeck();
        assertEquals(objectiveCard.getCardId() + ".png", objectiveCard.getImage());
    }
}
