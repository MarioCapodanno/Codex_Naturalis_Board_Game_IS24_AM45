package it.polimi.ingsw.am45.view.listener;

import it.polimi.ingsw.am45.view.ViewController;
import it.polimi.ingsw.am45.view.modelview.Boards;

/**
 * The boardsListener class implements the Listener interface.
 * It is responsible for observing the Boards instance and updating the client view accordingly.
 */
public class boardsListener implements Listener{

    /**
     * The constructor for the boardsListener class.
     * It registers this instance as an observer of the Boards instance.
     */
    public boardsListener() {
        Boards.getInstance().registerObserver(this);
    }

    /**
     * This method is called when the observed Boards instance notifies its observers of a change.
     * It updates the chat in the client view with the latest messages from the Boards instance.
     */
    @Override
    public void update() {
        ViewController.getClientView().updateChat(Boards.getInstance().getMessages());
    }

    /**
     * This method is called when the observed Boards instance notifies its observers of a change in the players.
     * It updates the boards in the client view with the latest boards from the Boards instance.
     */
    @Override
    public void updatePlayers() {
        ViewController.getClientView().updateBoards(Boards.getInstance().getBoards());
    }
}