package it.polimi.ingsw.am45.view.listener;

import it.polimi.ingsw.am45.view.ViewController;
import it.polimi.ingsw.am45.view.modelview.Market;

/**
 * The marketListener class implements the Listener interface.
 * It is responsible for observing the Market instance and updating the client view accordingly.
 */
public class marketListener implements Listener{

    /**
     * The constructor for the marketListener class.
     * It registers this instance as an observer of the Market instance.
     */
    public marketListener() {
        Market.getInstance().registerObserver(this);
    }

    /**
     * This method is called when the observed Market instance notifies its observers of a change.
     * It updates the resource in the client view with the latest resource from the Market instance.
     */
    @Override
    public void update() {
        ViewController.getClientView().updateResource(Market.getInstance().getResource());
    }

    /**
     * This method is called when the observed Market instance notifies its observers of a change in the players.
     * It updates the market in the client view with the latest cards from the Market instance.
     */
    @Override
    public void updatePlayers() {
        ViewController.getClientView().updateMarket(Market.getInstance().getCards());
    }
}