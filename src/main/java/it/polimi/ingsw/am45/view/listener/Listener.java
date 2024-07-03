package it.polimi.ingsw.am45.view.listener;

/**
 * The Listener interface defines the methods that should be implemented by any class that wants to listen for updates.
 * Classes that implement this interface can register themselves as observers of a particular subject.
 * When the subject's state changes, it notifies all registered observers by calling their update methods.
 */
public interface Listener {

    /**
     * This method is called when the observed subject notifies its observers of a change.
     * Implementing classes should define what action to take when an update occurs.
     */
    void update();

    /**
     * This method is called when the observed subject notifies its observers of a change in the players.
     * Implementing classes should define what action to take when an update on players occurs.
     */
    void updatePlayers();
}