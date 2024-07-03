package it.polimi.ingsw.am45.model.player;

import it.polimi.ingsw.am45.listener.GameListener;
import it.polimi.ingsw.am45.model.deck.cards.objectiveCards.ObjectiveCard;
import it.polimi.ingsw.am45.model.deck.cards.playableCards.PlayableCard;

import java.util.ArrayList;
import java.util.List;


/**
 * The player's Hand in the game that contains the cards that the player can play on his Board.
 */
public class Hand implements GameListener {

    /**
     * The list of cards in the player's Hand.
     */
    public final List<PlayableCard> hand;
    /**
     * The card selected to be played.
     */
    public PlayableCard selectedCard;
    /**
     * The Initial card of the player.
     */
    public PlayableCard initialCard;
    public ObjectiveCard objectiveCard1;
    public ObjectiveCard objectiveCard2;
    /**
     * Selected objective card of the player.
     */
    public ObjectiveCard selectedObjectiveCard;

    /**
     * Constructor for the Hand class.
     */
    public Hand() {
        this.hand = new ArrayList<>();
    }

    /**
     * Adds a card to the player's Hand at the selected index (position of the card in the Hand)
     *
     * @param card The card to be added to the Hand
     */
    public void addCard(PlayableCard card) {
        if (hand.size() >= 3) {
            throw new IllegalArgumentException("Hand is full");
        }
        hand.add(card);
    }

    /**
     * Method to set up the initial card of at the location (0,0) of the player's playerBoard.
     *
     * @param card The card to be added to the location (0,0) of the player's playerBoard.
     */
    public void setupInitCard(PlayableCard card) {
        this.initialCard = card;
    }

    public void setupObjChoice(ObjectiveCard obj1, ObjectiveCard obj2) {
        this.objectiveCard1 = obj1;
        this.objectiveCard2 = obj2;
    }

    /**
     * Removes the played card from the player's Hand after it has been played.
     *
     * @param card The card to be removed.
     */
    public void removePlayedCard(PlayableCard card) {
        selectedCard = null;
        hand.remove(card);

    }

    /**
     * Selects a card from the 3 cards of the player's Hand.
     *
     * @param id The index of the card to be selected from the Hand.
     */
    @Override
    public void selectCard(int id) {
        this.selectedCard = hand.get(id);
    }

    /**
     * Get the list of cards in the player's Hand.
     *
     * @return The list of cards in the player's Hand.
     */
    public List<PlayableCard> getHandCards() {
        return this.hand;
    }

    /**
     * Method to set the selected secret objective card of the player.
     *
     * @param position The position of the objective card in the player's Hand.
     */
    public void setObj(Boolean position) {
        selectedObjectiveCard = position ? objectiveCard1 : objectiveCard2;
    }

    /**
     * Flips the card in the player's Hand at the selected index.
     *
     * @param id position of the cards in the Hand to flip.
     * @return String of the current face of the card after flipping.
     */
    @Override
    public String flipCard(int id) {
        return hand.get(id).flip();
    }

    /**
     * @param id position of the cards in the Hand to get the Face of the card.
     * @return String of the Face of the card.
     */
    @Override
    public String getCardPng(int id) {
        return hand.get(id).getCurrFace();
    }

    /**
     * @return the index of the selected card in the Hand.
     */
    public ObjectiveCard getSelectedObjectiveCard() {
        return selectedObjectiveCard;
    }

}