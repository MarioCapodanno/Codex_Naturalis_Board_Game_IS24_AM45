package it.polimi.ingsw.am45.view.listener;

import it.polimi.ingsw.am45.view.ViewController;
import it.polimi.ingsw.am45.view.modelview.Hand;

/**
 * The handListener class implements the Listener interface.
 * It is responsible for observing the Hand instance and updating the client view accordingly.
 */
public class handListener implements Listener{

    /**
     * The constructor for the handListener class.
     * It registers this instance as an observer of the Hand instance.
     */
    public handListener() {
        Hand.getInstance().registerObserver(this);
    }

    /**
     * This method is called when the observed Hand instance notifies its observers of a change.
     * It updates the hand in the client view with the latest cards from the Hand instance.
     */
    @Override
    public void update() {
        ViewController.getClientView().updateHand(Hand.getInstance().getCards());
    }

    /**
     * This method is called when the observed Hand instance notifies its observers of a change in the players.
     * It updates the choosing card in the client view with the latest starting card and objectives from the Hand instance.
     */
    @Override
    public void updatePlayers() {
        ViewController.getClientView().updateChoosingCard(Hand.getInstance().getStartingCard(), Hand.getInstance().getObj1(), Hand.getInstance().getObj2());
    }
}