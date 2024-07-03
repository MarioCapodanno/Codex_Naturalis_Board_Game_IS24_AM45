package it.polimi.ingsw.am45.view.modelview;

import it.polimi.ingsw.am45.controller.server.stc.TurnsUpdate;
import it.polimi.ingsw.am45.view.listener.Listener;

import java.util.List;
import java.util.Stack;

/**
 * The Turn class is a singleton that manages the current turn in the game.
 * It maintains a list of listeners that are notified when the turn is updated.
 */
public class Turn {
    private static Turn instance;

    /**
     * List of listeners that are notified when the turn is updated.
     */
    protected final List<Listener> listeners = new Stack<>();

    /**
     * The nickname of the player whose turn it is.
     */
    private String nickname;

    /**
     * Returns the singleton instance of the Turn class.
     * If the instance does not exist, it is created.
     */
    public static Turn getInstance() {
        if (instance == null) instance = new Turn();
        return instance;
    }

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private Turn() {
    }

    /**
     * Registers a listener that will be notified when the turn is updated.
     */
    public void registerObserver(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener so that it will no longer be notified when the turn is updated.
     */
    public void removeObserver(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that the turn has been updated.
     */
    public void notifyObserversUpdate() {
        for(Listener listener : listeners) {
            listener.update();
        }
    }

    /**
     * Updates the turn with the data from a TurnsUpdate and notifies all registered listeners.
     */
    public void updateTurns(TurnsUpdate turnsUpdate) {
        this.nickname = turnsUpdate.nickname();
        notifyObserversUpdate();
    }

    /**
     * Returns the nickname of the player whose turn it is.
     * If no turn is currently active, returns null.
     */
    public String getNickname(){
        if(nickname == null) return null;
        return nickname;
    }
}