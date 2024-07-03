package it.polimi.ingsw.am45.model.player;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.enumeration.GameState;
import it.polimi.ingsw.am45.enumeration.TokenColor;
import it.polimi.ingsw.am45.model.boards.ScoreBoard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import it.polimi.ingsw.am45.utilities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private final String[][] corners = new String[][]{{"FUNGI", "EMPTY", "", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}};
    private final String[][] centers = new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}};
    private final String[] images = new String[]{"1101.png", "resource_red_back.png"};
    private final String[] images2 = new String[]{"1102.png", "resource_red_back.png"};

    private final ScoreBoard scoreBoard = new ScoreBoard(3);
    private final int playerPos = 1;



    @BeforeEach
    void setUp() {
        player = new Player("Mario", TokenColor.YEllOW);
    }

    @Test
    @DisplayName("Test for checking the placement of a card in a empty")
    void addPlayedCardToEmptyValidLocation() {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        PlayableCard card = new PlayableCard(1101, images, corners, CardColor.RED);
        card.setInstantPoints(3);
        card.setCenter(centers);
        int indexPlayedCard = 0;

        for (int[] direction : directions) {
            int x = direction[0];
            int y = direction[1];
            assertTrue(player.addPlayedCard(new Location(x, y), card, scoreBoard, playerPos));
            assertEquals(card, player.getPlayerBoard().getCardFromLocation(x, y));
            assertEquals(card.getCurrFace(), player.getPlayerBoard().getViewGrid().get(indexPlayedCard).getPath());
            indexPlayedCard++;
        }

    }

    @Test
    @DisplayName("Test for checking the correct update of the player resources")
    void updatedResourceAfterPlayedCard() {
        PlayableCard card = new PlayableCard(1101, images, corners, CardColor.RED);
        card.setInstantPoints(3);
        card.setCenter(centers);
        player.addPlayedCard(new Location(0, 1), card, scoreBoard, playerPos);
        assertEquals(2, player.getResources().get(3));
    }

    @Test
    @DisplayName("Test for checking the correct updated of the player score")
    void advancePlayerPos() {
        int initialScore = player.getScore();
        player.advancePlayerPos(5);
        assertEquals(initialScore + 5, player.getScore());
    }

    @Test
    @DisplayName("Test for checking the placement of a card in a occupied location")
    void addPlayedCardToOccupiedLocation() {
        PlayableCard card1 = new PlayableCard(1101, images, corners, CardColor.RED);
        PlayableCard card2 = new PlayableCard(1102, images2, corners, CardColor.RED);
        card1.setInstantPoints(1);
        card2.setInstantPoints(1);
        card1.setCenter(centers);
        card2.setCenter(centers);
        Location location = new Location(1, 0);
        player.addPlayedCard(location, card1, scoreBoard, playerPos);
        assertFalse(player.addPlayedCard(location, card2, scoreBoard, playerPos));
    }

    @Test
    @DisplayName("Adding a card to an empty location should return true")
    void addPlayedCardToEmptyLocation() {
        Location location = new Location(1, 0);
        PlayableCard card = new PlayableCard(1101, images, corners, CardColor.RED);
        card.setInstantPoints(3);
        card.setCenter(centers);
        assertTrue(player.addPlayedCard(location, card, scoreBoard, 1));
    }

    @Test
    public void testGetHand() {
        assertNotNull(player.getHand(), "Hand of the player should not be null");
    }
        @Test
        public void testAdvancePlayerPos() {
            int initialScore = player.getScore();
            int pointsToAdd = 5;
            player.advancePlayerPos(pointsToAdd);
            assertEquals(initialScore + pointsToAdd, player.getScore(), "Score should be increased by the number of points added");
        }

    @Test
    public void testGetPlayedCards() {
        assertNotNull(player.getPlayedCards(), "Played cards of the player should not be null");
    }

    @Test
    public void testGetState() {
        assertNotNull(player.getState(), "Game state of the player should not be null");
    }




    @Test
    @DisplayName("Adding a card should increase player's score")
    void addPlayedCardIncreasesScore() {
        int initialScore = player.getScore();
        Location location = new Location(1, 0);
        PlayableCard card = new PlayableCard(1101, images, corners, CardColor.RED);
        card.setInstantPoints(3);
        card.setCenter(centers);
        player.addPlayedCard(location, card, scoreBoard, 1);
        assertTrue(player.getScore() > initialScore);
    }

    @Test
    @DisplayName("Adding a card should change game state to GAME")
    void addPlayedCardChangesGameState() {
        Location location = new Location(1, 0);
        PlayableCard card = new PlayableCard(1101, images, corners, CardColor.RED);
        card.setInstantPoints(3);
        card.setCenter(centers);
        player.addPlayedCard(location, card, scoreBoard, 1);
        assertEquals(GameState.GAME, player.getState());
    }

    @Test
    @DisplayName("Adding a card should add it to played cards")
    void addPlayedCardAddsToPlayedCards() {
        Location location = new Location(1, 0);
        PlayableCard card = new PlayableCard(1101, images, corners, CardColor.RED);
        card.setInstantPoints(3);
        card.setCenter(centers);
        player.addPlayedCard(location, card, scoreBoard, 1);
        assertTrue(player.getPlayedCards().contains(card));
    }


    @Test
    @DisplayName("Can place card when played cards are empty")
    void canPlaceCardWhenPlayedCardsEmpty() {
        Location location = new Location(1, 0);
        PlayableCard card = new PlayableCard(1101, images, corners, CardColor.RED);
        assertTrue(player.canPlaceCard(location, card));
    }

    @Test
    @DisplayName("Game state should be CHOICES initially")
    void gameStateShouldBeChoicesInitially() {
        assertEquals(GameState.CHOICES, player.getState());
    }
}

