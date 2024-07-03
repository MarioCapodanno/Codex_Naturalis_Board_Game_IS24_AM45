package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;

import java.io.Serializable;

public class FlipCard extends ClientToServer implements Serializable {

    private final int position;

    /**
     This method updates the game state by flipping a card based on the position.
     It uses the Singleton instance of GamesController to perform this action.
     */
    @Override
    public void update() {
        GamesController.getInstance().flipCard(position, this.getServerSocket());
    }

    /**
     * Class constructor
     * @param position
     */
    public FlipCard(int position) {
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
