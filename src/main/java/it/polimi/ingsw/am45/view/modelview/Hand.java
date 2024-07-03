package it.polimi.ingsw.am45.view.modelview;

import it.polimi.ingsw.am45.controller.server.stc.ChoosingUpdate;
import it.polimi.ingsw.am45.controller.server.stc.HandUpdate;
import it.polimi.ingsw.am45.view.listener.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The Hand class is a singleton that manages the player's hand in the game.
 * It maintains a list of listeners that are notified when the hand is updated.
 */
public class Hand {
    private static Hand instance;
    protected final List<Listener> listeners = new Stack<>();
    private List<String> cards;
    private String initCard;
    private String obj1;
    private String obj2;

    /**
     * Returns the singleton instance of the Hand class.
     * If the instance does not exist, it is created.
     */
    public static Hand getInstance() {
        if (instance == null) instance = new Hand();
        return instance;
    }

    /**
     * Private constructor to enforce the singleton pattern.
     * Initializes the cards list.
     */
    private Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Registers a listener that will be notified when the hand is updated.
     */
    public void registerObserver(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener so that it will no longer be notified when the hand is updated.
     */
    public void removeObserver(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that the hand has been updated.
     */
    public void notifyObserversUpdate() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    /**
     * Notifies all registered listeners that the players have been updated.
     */
    public void notifyObserversUpdatePlayers() {
        for (Listener listener : listeners) {
            listener.updatePlayers();
        }
    }

    /**
     * Updates the hand with the data from a HandUpdate and notifies all registered listeners.
     */
    public void updateHands(HandUpdate handUpdate) {
        this.cards = handUpdate.getHand();
        notifyObserversUpdate();
    }

    /**
     * Updates the hand with the data from a ChoosingUpdate and notifies all registered listeners.
     */
    public void updateHands(ChoosingUpdate choosingUpdate) {
        this.initCard = choosingUpdate.getInitCard();
        this.obj1 = choosingUpdate.getObj1();
        this.obj2 = choosingUpdate.getObj2();
        notifyObserversUpdatePlayers();
    }

    /**
     * Returns the current hand.
     */
    public List<String> getCards() {
        return cards;
    }

    /**
     * Returns the starting card.
     */
    public String getStartingCard() {
        return initCard;
    }

    /**
     * Returns the first objective card.
     */
    public String getObj1() {
        return obj1;
    }

    /**
     * Returns the second objective card.
     */
    public String getObj2() {
        return obj2;
    }

    /**
     * Sets the hand to a specific list of cards.
     * This method is only for testing purposes.
     */
    public void setHand(List<String> hand) {
        this.cards = hand;
    }
}