package it.polimi.ingsw.am45.model.deck.cards;

import it.polimi.ingsw.am45.enumeration.CardColor;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CardTest {
        private PlayableCard playableCard;
        private final int cardId = 1;
        private final String[] images = {"front.png", "back.png"};
        private final String[][] corners = {{"ANIMAL", "PLANT", "INSECT", "FUNGI"}, {"FEATHER", "BOTTLE", "SCROLL", "EMPTY"}};
        private final CardColor cardColor = CardColor.NONE;

        @BeforeEach
        void setUp() {
            playableCard = new PlayableCard(cardId, images, corners, cardColor);
            String[][] center = {{"ANIMAL"}, {"PLANT"}};
            playableCard.setCenter(center);
        }

        @Test
        void testPlayableCardProperties() {
            assertEquals(cardId, playableCard.getCardId());
            assertEquals(images[0], playableCard.getCurrFace());
            assertEquals(corners[0], playableCard.getCurrCorners());
            assertEquals(cardColor, playableCard.getCardColor());
        }

        @Test
        void testSetLocationAndGetLocation() {
            playableCard.setLocation(1, 2);
            assertEquals(1, playableCard.getLocation().getX());
            assertEquals(2, playableCard.getLocation().getY());
        }

        @Test
        void testSetCenterAndGetCurrCenter() {
            String[][] center = {{"ANIMAL"}, {"PLANT"}};
            playableCard.setCenter(center);
            assertEquals(center[0][0], playableCard.getCurrCenter()[0]);
        }

        @Test
        void testGetCorner() {
            assertEquals("ANIMAL", playableCard.getCorner(0));
            assertEquals(cardColor, playableCard.getCardColor());
            assertNull(playableCard.getRequirements());
        }

        @Test
        void testSetInstantPointsAndGetInstantPoints() {
            playableCard.setInstantPoints(5);
            assertEquals(5, playableCard.getInstantPoints());
        }


        @Test
        void testGetImages() {
            assertEquals(images[0], playableCard.getImages()[0]);
            assertEquals(images[1], playableCard.getImages()[1]);
        }

    @Test
    void testFlip() {
        playableCard.setInstantPoints(5);
        assertEquals(images[0], playableCard.getCurrFace());
        assertEquals(5, playableCard.getInstantPoints());

        playableCard.flip();
        assertEquals(images[1], playableCard.getCurrFace());
        assertEquals(0, playableCard.getInstantPoints());

        playableCard.flip();
        assertEquals(images[0], playableCard.getCurrFace());
        assertEquals(5, playableCard.getInstantPoints());
    }
    }
