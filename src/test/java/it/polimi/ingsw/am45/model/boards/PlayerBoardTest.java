package it.polimi.ingsw.am45.model.boards;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {
    PlayerBoard playerBoard;
    PlayableCard card1, card2;  // Declare `card` as an instance variable

    //istanzio due carte per poter fare i test
    @BeforeEach
    void setUp() {
        playerBoard = new PlayerBoard();  // Initialize `playerBoard` here
        // Initialize `card` here so it's available in all test methods

        card1 = new PlayableCard(1101, new String[]{"1101.png", "resource_red_back.png"}, new String[][]{{"FUNGI", "EMPTY", "", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.NONE);
        card2 = new PlayableCard(1102, new String[]{"1102.png", "resource_red_back.png"}, new String[][]{{"FUNGI", "EMPTY", "", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}}, CardColor.NONE);
        card1.setInstantPoints(3);
        card2.setInstantPoints(2);
        card1.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
        card1.setCenter(new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}});
    }

    @Test
    @DisplayName("Test for checking the placement of a card on the player board with enough resources")
    void checkRequirementsWithEnoughResources() {
        PlayerBoard playerBoard = new PlayerBoard();
        int[] requirements = {0, 0, 0, 0, 0, 0, 0};
        assertTrue(playerBoard.checkRequirements(requirements));
    }

    @Test
    @DisplayName("Test for placing a gold card on the player board with not enough resources")
    void checkRequirementsWithInsufficientResources() {
        PlayerBoard playerBoard = new PlayerBoard();
        int[] requirements = {1, 0, 0, 0, 0, 0, 0};
        assertFalse(playerBoard.checkRequirements(requirements));
    }

    @Test
    void placeCardOnEmptyLocation() {
        playerBoard.placeCard(0, 0, card1);
        assertEquals(card1, playerBoard.getCardFromLocation(0, 0));
    }

    @Test
    void placeCardOnOccupiedLocation() {

        playerBoard.placeCard(0, 0, card1);
        playerBoard.placeCard(0, 0, card2);
        assertEquals(card2, playerBoard.getCardFromLocation(0, 0));
    }

    @Test
    void updateAnimalResource() {
        playerBoard.updateResources("ANIMAL", 2);
        assertEquals(2, playerBoard.getResources().get(0));
    }

    @Test
    void updatePlantResource() {
        playerBoard.updateResources("PLANT", 3);
        assertEquals(3, playerBoard.getResources().get(1));
    }

    @Test
    void updateInsectResource() {
        playerBoard.updateResources("INSECT", 4);
        assertEquals(4, playerBoard.getResources().get(2));
    }

    @Test
    void updateFungiResource() {
        playerBoard.updateResources("FUNGI", 5);
        assertEquals(5, playerBoard.getResources().get(3));
    }

    @Test
    void updateFeatherResource() {
        playerBoard.updateResources("FEATHER", 6);
        assertEquals(6, playerBoard.getResources().get(4));
    }

    @Test
    void updateBottleResource() {
        playerBoard.updateResources("BOTTLE", 7);
        assertEquals(7, playerBoard.getResources().get(5));
    }

    @Test
    void updateScrollResource() {
        playerBoard.updateResources("SCROLL", 8);
        assertEquals(8, playerBoard.getResources().get(6));
    }

    @Test
    void setPlayerBoardResources() {
        List<Integer> newResources = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        playerBoard.setPlayerBoardResources(newResources);

        for (int i = 0; i < newResources.size(); i++) {
            assertEquals(newResources.get(i), playerBoard.getResources().get(i));
        }
    }
}



