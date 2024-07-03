package it.polimi.ingsw.am45.model.player;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ObjectiveCard;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ObjectiveCardType;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ResourceObjectiveCard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class HandTest {
    private Hand hand;

    private final String[][] corners = new String[][]{{"FUNGI", "EMPTY", "", "FUNGI"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}};
    private final String[][] centers = new String[][]{{"EMPTY"}, {"FUNGI", "ANIMAL", "EMPTY", "EMPTY"}};

    private final String[] images = new String[]{"1101.png", "resource_red_back.png"};

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    @DisplayName("Test for adding a card to the hand")
    void addCardShouldIncreaseHandSize() {
        String[] images = {"card.png", "f_card.png"};
        String[][] corners = {{"EMPTY", "EMPTY", "EMPTY", "EMPTY"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}};

        PlayableCard card = new PlayableCard(0, images, corners, CardColor.RED);

        hand.addCard(card);

        assertEquals(1, hand.getHandCards().size());
    }

    @Test
    @DisplayName("Test for checking the correct removing of a card from the hand")
    void removePlayedCardShouldDecreaseHandSize() {
        String[] images = {"card.png", "f_card.png"};
        String[][] corners = {{"EMPTY", "EMPTY", "EMPTY", "EMPTY"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}};

        PlayableCard card = new PlayableCard(0, images, corners, CardColor.RED);

        hand.addCard(card);
        hand.removePlayedCard(card);

        assertEquals(0, hand.getHandCards().size());
    }


    @Test
    @DisplayName("Test for checking the correct change of the selected card in the hand")
    void selectCardShouldChangeSelectedCard() {
        String[] images = {"card.png", "f_card.png"};
        String[][] corners = {{"EMPTY", "EMPTY", "EMPTY", "EMPTY"}, {"EMPTY", "EMPTY", "EMPTY", "EMPTY"}};

        PlayableCard card1 = new PlayableCard(0, images, corners, CardColor.RED);
        PlayableCard card2 = new PlayableCard(1, images, corners, CardColor.RED);

        hand.addCard(card1);
        hand.addCard(card2);
        hand.selectCard(0);

        assertEquals(card1.getCurrFace(), hand.getCardPng(0));
    }

    @Test
    @DisplayName("Test for checking the throwing of an exception when selecting a card with an invalid index")
    void selectCardClickShouldThrowExceptionForInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> hand.selectCard(5));
    }

    @Test
    @DisplayName("Test for checking the correct flipping of a card in the hand")
    void flipCardShouldChangeCardFace() {
        PlayableCard card = new PlayableCard(0, images, corners, CardColor.RED);

        card.setCenter(centers);
        hand.addCard(card);
        hand.flipCard(0);

        assertEquals(card.getCurrFace(), hand.getCardPng(0));
    }


    @Test
    @DisplayName("Setting up objective choice with null cards should not throw exception")
    void setupObjectiveChoiceWithNullCardsDoesNotThrow() {
        assertDoesNotThrow(() -> hand.setupObjChoice(null, null));
    }

    @Test
    @DisplayName("Getting selected objective card when none is selected should return null")
    void getSelectedObjectiveCardWhenNoneSelectedReturnsNull() {
        assertNull(hand.getSelectedObjectiveCard());
    }


    @Test
    @DisplayName("Getting selected objective card after setting one should return that card")
    void getSelectedObjectiveCardAfterSettingReturnsThatCard() {
    ObjectiveCardType objectiveCardType = new ResourceObjectiveCard(new int[]{0, 0, 0, 0, 2, 0, 0}, 2);
    String[] images = {"card.png", "f_card.png"};
    ObjectiveCard card1 = new ObjectiveCard(images[0], 401, objectiveCardType);
    ObjectiveCard card2 = new ObjectiveCard(images[1], 402, objectiveCardType);
        hand.setupObjChoice(card1, card2);
        hand.setObj(true);
        assertEquals(card1, hand.getSelectedObjectiveCard());
    }


    @Test
    @DisplayName("Getting selected objective card after setting another one should return the latest")
    void getSelectedObjectiveCardAfterSettingAnotherReturnsLatest() {
        ObjectiveCardType objectiveCardType = new ResourceObjectiveCard(new int[]{0, 0, 0, 0, 2, 0, 0}, 2);
        ObjectiveCard card1 = new ObjectiveCard("401.png", 401, objectiveCardType);
        ObjectiveCard card2 = new ObjectiveCard("402.png", 402, objectiveCardType);
        hand.setupObjChoice(card1, card2);
        hand.setObj(true);
        hand.setObj(false);
        assertEquals(card2, hand.getSelectedObjectiveCard());
    }
}