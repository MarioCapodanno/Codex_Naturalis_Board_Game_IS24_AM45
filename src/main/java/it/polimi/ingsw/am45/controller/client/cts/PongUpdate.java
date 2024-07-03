package it.polimi.ingsw.am45.controller.client.cts;

import java.io.Serializable;

public class PongUpdate extends ClientToServer implements Serializable {

    /**
     * This method updates the game state by sending a pong message to the server.
     */
    @Override
    public void update() {
        this.getServerSocket().pongReceived();
    }
}
