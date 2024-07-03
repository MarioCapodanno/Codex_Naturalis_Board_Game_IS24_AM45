package it.polimi.ingsw.am45.controller.client.cts;

import it.polimi.ingsw.am45.controller.GamesController;

import java.io.Serializable;

public class RequestUpdate extends ClientToServer implements Serializable {
    private final Boolean playing;

    /**
     * This method updates the game state by requesting an update.
     * It uses the Singleton instance of GamesController to perform this action.
     */
    @Override
    public void update() {
        if (!playing)
            GamesController.getInstance().requestUpdate(super.getGameId());
    }

    /**
     * Class constructor
     * @param playing
     */
    public RequestUpdate(Boolean playing) {
        this.playing = playing;
    }


}
