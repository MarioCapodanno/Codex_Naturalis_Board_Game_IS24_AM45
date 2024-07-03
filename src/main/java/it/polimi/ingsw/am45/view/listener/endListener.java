package it.polimi.ingsw.am45.view.listener;

import it.polimi.ingsw.am45.view.ViewController;
import it.polimi.ingsw.am45.view.modelview.End;

/**
 * The endListener class implements the Listener interface.
 * It is responsible for observing the End instance and updating the client view accordingly.
 */
public class endListener implements Listener{

    /**
     * The constructor for the endListener class.
     * It registers this instance as an observer of the End instance.
     */
    public endListener() {
        End.getInstance().registerObserver(this);
    }

    /**
     * This method is called when the observed End instance notifies its observers of a change.
     * It updates the end in the client view with the latest winners from the End instance.
     */
    @Override
    public void update() {
        ViewController.getClientView().updateEnd(End.getInstance().getWinners());
    }

    /**
     * This method is part of the Listener interface but is not used in this class.
     */
    @Override
    public void updatePlayers() {
    }
}