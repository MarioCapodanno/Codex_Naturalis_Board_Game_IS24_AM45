package it.polimi.ingsw.am45.view.modelview;

import it.polimi.ingsw.am45.controller.server.stc.WinnerUpdate;
import it.polimi.ingsw.am45.view.listener.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The End class is a singleton that manages the end of the game.
 * It maintains a list of listeners that are notified when the winners are updated.
 */
public class End {
    private static End instance;

    /**
     * List of listeners that are notified when the winners are updated.
     */
    protected final List<Listener> listeners = new Stack<>();

    /**
     * List of winners at the end of the game.
     */
    private List<String> winners = new ArrayList<>();

    /**
     * Returns the singleton instance of the End class.
     * If the instance does not exist, it is created.
     */
    public static End getInstance() {
        if (instance == null) instance = new End();
        return instance;
    }

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private End() {
    }

    /**
     * Registers a listener that will be notified when the winners are updated.
     */
    public void registerObserver(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener so that it will no longer be notified when the winners are updated.
     */
    public void removeObserver(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that the winners have been updated.
     */
    public void notifyObserversUpdate() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    /**
     * Updates the winners with the data from a WinnerUpdate and notifies all registered listeners.
     */
    public void updateWinners(WinnerUpdate winnerUpdate) {
        this.winners = winnerUpdate.getWinners();
        notifyObserversUpdate();
    }

    /**
     * Returns the current winners.
     */
    public List<String> getWinners() {
        return winners;
    }
}