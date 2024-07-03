package it.polimi.ingsw.am45.view.listener;

import it.polimi.ingsw.am45.view.ViewController;
import it.polimi.ingsw.am45.view.modelview.Msg;

/**
 * The msgListener class implements the Listener interface.
 * It is responsible for observing the Msg instance and updating the client view accordingly.
 */
public class msgListener implements Listener{

    /**
     * The constructor for the msgListener class.
     * It registers this instance as an observer of the Msg instance.
     */
    public msgListener() {
        Msg.getInstance().registerObserver(this);
    }

    /**
     * This method is called when the observed Msg instance notifies its observers of a change.
     * It updates the message in the client view with the latest message from the Msg instance.
     */
    @Override
    public void update() {
        ViewController.getClientView().updateMessage(Msg.getInstance().lookMessage());
    }

    /**
     * This method is called when the observed Msg instance notifies its observers of a change in the players.
     * It updates the players in the client view with the latest nicknames, pings, and points from the Msg instance.
     */
    @Override
    public void updatePlayers() {
        ViewController.getClientView().updatePlayers(Msg.getInstance().lookNicknames(), Msg.getInstance().lookPings(), Msg.getInstance().lookPoints());
    }
}