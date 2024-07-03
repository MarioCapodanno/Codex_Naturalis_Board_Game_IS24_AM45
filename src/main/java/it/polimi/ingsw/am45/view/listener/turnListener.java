package it.polimi.ingsw.am45.view.listener;

import it.polimi.ingsw.am45.view.ViewController;
import it.polimi.ingsw.am45.view.modelview.Turn;

/**
 * The turnListener class implements the Listener interface.
 * It is responsible for observing the Turn instance and updating the client view accordingly.
 */
public class turnListener implements Listener{

    /**
     * The constructor for the turnListener class.
     * It registers this instance as an observer of the Turn instance.
     */
    public turnListener() {
        Turn.getInstance().registerObserver(this);
    }

    /**
     * This method is called when the observed Turn instance notifies its observers of a change.
     * It updates the turn in the client view with the latest nickname from the Turn instance.
     */
    @Override
    public void update() {
        ViewController.getClientView().updateTurns(Turn.getInstance().getNickname());
    }

    /**
     * This method is part of the Listener interface but is not used in this class.
     */
    @Override
    public void updatePlayers() {
    }
}