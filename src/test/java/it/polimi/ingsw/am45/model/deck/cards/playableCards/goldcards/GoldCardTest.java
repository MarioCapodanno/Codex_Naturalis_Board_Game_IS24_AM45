package it.polimi.ingsw.am45.model.deck.cards.playableCards.goldcards;

import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.exception.InvalidNumberOfPlayersException;
import it.polimi.ingsw.am45.model.Game;
import it.polimi.ingsw.am45.model.deck.Deck;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.model.player.Player;
import it.polimi.ingsw.am45.utilities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GoldCard
 */
class GoldCardTest {

    Deck decks;
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

    /**
     * Test method for check correct requirements when set
     */
    @Test
    void shouldReturnCorrectRequirementsWhenSet() {
        GoldCard goldCard = game.getDeck().drawGoldDeck();
        int[] cost = new int[]{0, 0, 3, 1, 0, 0, 0};
        goldCard.setCardCost(cost);
        goldCard.flip();
        goldCard.flip();
        assertArrayEquals(cost, goldCard.getRequirements());
    }

    @Test
    void shouldReturnErrorWhenSetWrongRequirements() {
        GoldCard goldCard = game.getDeck().drawGoldDeck();
        int[] cost = new int[]{0, 0, 3, 1, 0, 0};
        assertThrows(IllegalArgumentException.class, () -> goldCard.setCardCost(cost));
    }

    /**
     * Test method for check if flipped card goldcard has null requirements
     */
    @Test
    void shouldReturnNullRequirementsWhenFlipped() {
        GoldCard goldCard = game.getDeck().drawGoldDeck();
        goldCard.flip();
        assertNull(goldCard.getRequirements());
    }

    @Test
    @DisplayName("Test for checking the correct calculation of the points of the resource objective card")
    void checkCoveredCornerGoldCardPointsCalculation() {
        Location[] locations = {
                new Location(1, 0),
                new Location(0, 1),
                new Location(-1, 0),
                new Location(0, -1)
        };

        for (int i = 0; i < 4; i++) {
            player1.addPlayedCard(locations[i], player1.getHand().getHandCards().get(0), game.getScoreBoard(), 1);
            GoldCard goldCard = game.getDeck().drawGoldDeck();
            try {
                player1.getHand().addCard(goldCard);
            } catch (IllegalArgumentException e) {
                System.out.println("Card already in hand");
                }

        }

        for (GoldCard goldCardTest : game.getDeck().getDeckGold()) {
            if (goldCardTest.getCardType() instanceof CoveredAngleGoldCard) {
                int cornerToCheck = 0;
                if (!player1.getPlayerBoard().getCardFromLocation(0, 1).getCorner(2).isEmpty())
                    cornerToCheck++;
                if (!player1.getPlayerBoard().getCardFromLocation(1, 0).getCorner(3).isEmpty())
                    cornerToCheck++;

                assertEquals(cornerToCheck * 2, goldCardTest.getInstantPoints(player1, new Location(1, 1)));
            }
        }

    }
}