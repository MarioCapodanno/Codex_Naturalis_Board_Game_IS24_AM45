package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;

import java.io.Serializable;

public class DrawCard extends ClientToServer implements Serializable {

    private final int position;

    /**
     This method updates the game state by drawing a card based on the position.
     It uses the Singleton instance of GamesController to perform this action.
     */
    @Override
    public void update() {
        GamesController.getInstance().drawCard(position, this.getServerSocket());
    }

    /**
     * Class constructor
     * @param position The index of the card to be drawn from the array of market cards.
     */
    public DrawCard(int position) {
        this.position = position;
    }

    /**
     * Getter
     * @return position
     */

    public int getPosition() {
        return this.position;
    }


}
